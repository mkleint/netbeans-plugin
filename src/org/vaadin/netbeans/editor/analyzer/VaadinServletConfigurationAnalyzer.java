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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.TypeElement;

import org.netbeans.api.java.source.CompilationInfo;
import org.netbeans.api.java.source.ElementHandle;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.spi.editor.hints.ErrorDescription;
import org.netbeans.spi.editor.hints.ErrorDescriptionFactory;
import org.netbeans.spi.editor.hints.Fix;
import org.netbeans.spi.editor.hints.Severity;
import org.openide.filesystems.FileObject;
import org.openide.util.NbBundle;
import org.vaadin.netbeans.VaadinSupport;
import org.vaadin.netbeans.code.generator.JavaUtils;
import org.vaadin.netbeans.editor.VaadinTaskFactory;
import org.vaadin.netbeans.model.ModelOperation;
import org.vaadin.netbeans.model.VaadinModel;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.AssignmentTree;

/**
 * @author denis
 */
public class VaadinServletConfigurationAnalyzer implements TypeAnalyzer {

    @NbBundle.Messages({
            "configAnnotationError=@VaadinServletConfiguration is intended to use only with VaadinServlet subclass",
            "# {0} - module", "noGwtModule=GWT module {0} does not exist" })
    @Override
    public void analyze( TypeElement type, CompilationInfo info,
            Collection<ErrorDescription> descriptions,
            VaadinTaskFactory factory, AtomicBoolean cancel )
    {
        FileObject fileObject = info.getFileObject();
        Project project = FileOwnerQuery.getOwner(fileObject);
        if (project == null) {
            return;
        }
        VaadinSupport support = project.getLookup().lookup(VaadinSupport.class);
        if (support == null || !support.isEnabled()) {
            return;
        }
        AnnotationMirror config = JavaUtils.getAnnotation(type,
                JavaUtils.VAADIN_SERVLET_CONFIGURATION);
        if (config == null || cancel.get()) {
            return;
        }

        TypeElement servlet = info.getElements().getTypeElement(
                JavaUtils.VAADIN_SERVLET);
        if (servlet != null) {
            if (!info.getTypes().isSubtype(type.asType(), servlet.asType())) {
                descriptions.add(AbstractJavaFix.createExtendServletFix(type,
                        info, Bundle.configAnnotationError(), Severity.ERROR));
            }
        }
        if (cancel.get()) {
            return;
        }

        String widgetset = JavaUtils.getValue(config, JavaUtils.WIDGETSET);
        final FileObject[] gwtXml = new FileObject[1];
        try {
            support.runModelOperation(new ModelOperation() {

                @Override
                public void run( VaadinModel model ) {
                    gwtXml[0] = model.getGwtXml();
                }
            });
        }
        catch (IOException e) {
            Logger.getLogger(VaadinServletConfigurationAnalyzer.class.getName())
                    .log(Level.INFO, null, e);
        }

        if (cancel.get()) {
            return;
        }

        if (gwtXml[0] == null) {
            if (widgetset != null) {
                AnnotationTree annotationTree = (AnnotationTree) info
                        .getTrees().getTree(type, config);
                AssignmentTree assignment = AbstractJavaFix
                        .getAnnotationTreeAttribute(annotationTree,
                                JavaUtils.WIDGETSET);
                List<Integer> positions = AbstractJavaFix.getElementPosition(
                        info, assignment);
                ErrorDescription description = ErrorDescriptionFactory
                        .createErrorDescription(Severity.ERROR, Bundle
                                .noGwtModule(widgetset), Collections
                                .<Fix> singletonList(new CreateGwtModuleFix(
                                        widgetset, fileObject, factory)), info
                                .getFileObject(), positions.get(0), positions
                                .get(1));
                descriptions.add(description);
            }
            else {
                // TODO : add hint to create GWT module (low priority)
            }
            return;
        }
        String foundWidgetset = AbstractJavaFix.getWidgetsetFqn(gwtXml[0]);
        if (widgetset == null) {
            AnnotationTree annotationTree = (AnnotationTree) info.getTrees()
                    .getTree(type, config);
            List<Integer> positions = AbstractJavaFix.getElementPosition(info,
                    annotationTree);
            ErrorDescription description = ErrorDescriptionFactory
                    .createErrorDescription(Severity.HINT, Bundle
                            .noWidgetset(foundWidgetset), Collections
                            .<Fix> singletonList(new SetWidgetsetFix(
                                    foundWidgetset, fileObject, ElementHandle
                                            .create(type))), info
                            .getFileObject(), positions.get(0), positions
                            .get(1));
            descriptions.add(description);
        }
        else if (!foundWidgetset.equals(widgetset)) {
            AnnotationTree annotationTree = (AnnotationTree) info.getTrees()
                    .getTree(type, config);
            AssignmentTree assignment = AbstractJavaFix
                    .getAnnotationTreeAttribute(annotationTree,
                            JavaUtils.WIDGETSET);
            List<Integer> positions = AbstractJavaFix.getElementPosition(info,
                    assignment);
            List<Fix> fixes = new ArrayList<>(1);
            fixes.add(new SetWidgetsetFix(foundWidgetset, fileObject,
                    ElementHandle.create(type)));
            // don't add fix if there is already GWT.xml available
            //fixes.add(new CreateGwtModuleFix(widgetset, fileObject, factory));
            ErrorDescription description = ErrorDescriptionFactory
                    .createErrorDescription(Severity.ERROR,
                            Bundle.noGwtModule(widgetset), fixes,
                            info.getFileObject(), positions.get(0),
                            positions.get(1));
            descriptions.add(description);
        }
    }
}
