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
package org.vaadin.netbeans.model.gwt.impl;

import org.vaadin.netbeans.model.gwt.GwtComponent;
import org.vaadin.netbeans.model.gwt.GwtComponentVisitor;
import org.vaadin.netbeans.model.gwt.SetProperty;
import org.w3c.dom.Element;

/**
 * @author denis
 */
class SetPropertyImpl extends ValuedComponentImpl implements SetProperty {

    static final String SET_PROPERTY = "set-property"; // NOI18N

    SetPropertyImpl( GwtModelImpl model, Element e ) {
        super(model, e);
    }

    SetPropertyImpl( GwtModelImpl model ) {
        this(model, createNewElement(SET_PROPERTY, model));
    }

    @Override
    public Class<? extends GwtComponent> getComponentType() {
        return SetProperty.class;
    }

    @Override
    public void accept( GwtComponentVisitor visitor ) {
        visitor.visit(this);
    }

}
