/*
 * Copyright 2000-2013 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.vaadin.netbeans.editor.analyzer;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

import org.netbeans.api.java.source.ElementHandle;
import org.netbeans.spi.editor.hints.ErrorDescription;
import org.netbeans.spi.editor.hints.ErrorDescriptionFactory;
import org.netbeans.spi.editor.hints.Fix;
import org.netbeans.spi.editor.hints.Severity;
import org.netbeans.spi.java.hints.HintContext;
import org.openide.filesystems.FileObject;
import org.openide.util.NbBundle;
import org.vaadin.netbeans.code.WidgetUtils;
import org.vaadin.netbeans.editor.hints.Analyzer;
import org.vaadin.netbeans.utils.JavaUtils;

/**
 * @author denis
 */
public class StateAccessorAnalyzer extends Analyzer {

    public StateAccessorAnalyzer( HintContext context ) {
        super(context);
    }

    @Override
    public void analyze() {
        TypeElement type = getType();
        if (type == null) {
            return;
        }
        Types types = getInfo().getTypes();
        TypeElement abstractComponent =
                getInfo().getElements().getTypeElement(
                        WidgetUtils.ABSTRACT_COMPONENT);
        if (abstractComponent != null) {
            if (types.isSubtype(type.asType(), abstractComponent.asType())
                    && !hasGetStateMethod())
            {
                TypeElement connector =
                        WidgetUtils.getConnector(getType(), getInfo());
                addDescription(true, connector,
                        getTypeFqn(WidgetUtils
                                .getStateMethodReturnType(connector)));
            }
        }
        TypeElement abstractConnector =
                getInfo().getElements().getTypeElement(
                        WidgetUtils.ABSTRACT_COMPONENT_CONNECTOR);
        if (abstractConnector != null) {
            if (types.isSubtype(type.asType(), abstractConnector.asType())
                    && !hasGetStateMethod())
            {
                TypeElement serverComponent =
                        WidgetUtils.getServerComponent(getType(), getInfo());
                addDescription(false, serverComponent,
                        getTypeFqn(WidgetUtils
                                .getStateMethodReturnType(serverComponent)));
            }
        }
    }

    @NbBundle.Messages({ "noState=Widget doesn't override getState method" })
    private void addDescription( boolean isServer, TypeElement pairClass,
            String stateFqn )
    {
        List<Integer> positions =
                AbstractJavaFix.getElementPosition(getInfo(), getType());

        TypeElement sharedState =
                getInfo().getElements().getTypeElement(
                        SharedStateAnalyzer.SHARED_STATE);
        List<Fix> fixes = new LinkedList<>();
        if (stateFqn == null) {
            if (sharedState != null) {
                try {
                    Set<TypeElement> states =
                            JavaUtils.getSubclasses(sharedState, getInfo());
                    Set<FileObject> sourceRoots =
                            IsInSourceQuery.getSourceRoots(getInfo());
                    for (TypeElement state : states) {
                        Set<Modifier> modifiers = state.getModifiers();
                        if (IsInSourceQuery.isInSourceRoots(state, getInfo(),
                                sourceRoots)
                                && !modifiers.contains(Modifier.ABSTRACT)
                                && !modifiers.contains(Modifier.PRIVATE))
                        {
                            if (pairClass != null
                                    && IsInSourceQuery.isInSource(pairClass,
                                            getInfo()))
                            {
                                fixes.add(new StateAccessorFix(getInfo()
                                        .getFileObject(), state
                                        .getQualifiedName().toString(),
                                        ElementHandle.create(getType()),
                                        ElementHandle.create(pairClass)));
                            }
                            fixes.add(new StateAccessorFix(getInfo()
                                    .getFileObject(), state.getQualifiedName()
                                    .toString(), ElementHandle
                                    .create(getType()), null));

                        }
                    }
                }
                catch (InterruptedException ignore) {
                }
            }
            if (pairClass != null
                    && IsInSourceQuery.isInSource(pairClass, getInfo()))
            {
                fixes.add(new CreateSharedState(getInfo().getFileObject(),
                        isServer, ElementHandle.create(getType()),
                        ElementHandle.create(pairClass)));
            }
            fixes.add(new CreateSharedState(getInfo().getFileObject(),
                    isServer, ElementHandle.create(getType()), null));
        }
        else {
            fixes.add(new StateAccessorFix(getInfo().getFileObject(), stateFqn,
                    ElementHandle.create(getType())));
        }

        ErrorDescription description =
                ErrorDescriptionFactory.createErrorDescription(
                        getSeverity(Severity.HINT), Bundle.noState(), fixes,
                        getInfo().getFileObject(), positions.get(0),
                        positions.get(1));
        getDescriptions().add(description);
    }

    public ErrorDescription getStateDescription() {
        if (getDescriptions().isEmpty()) {
            return null;
        }
        else {
            return getDescriptions().iterator().next();
        }
    }

    private boolean hasGetStateMethod() {
        return getStateMethodReturnType() != null;
    }

    private TypeMirror getStateMethodReturnType() {
        return WidgetUtils.getStateMethodReturnType(getType());
    }

    private String getTypeFqn( TypeMirror type ) {
        if (type == null) {
            return null;
        }
        Element element = getInfo().getTypes().asElement(type);
        if (element instanceof TypeElement) {
            return ((TypeElement) element).getQualifiedName().toString();
        }
        return null;
    }

}
