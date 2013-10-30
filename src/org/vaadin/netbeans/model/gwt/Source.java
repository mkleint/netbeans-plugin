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
package org.vaadin.netbeans.model.gwt;

import java.util.List;

/**
 * @author denis
 */
public interface Source extends ModuleComponent {

    List<FilterComponent> getFilters();

    void removeFilter( FilterComponent component );

    void addFilter( FilterComponent component );

    String getPath();

    void setPath( String path );

    String getExcludes();

    void setExcludes( String excludes );

    String getIncludes();

    void setIncludes( String includes );

    boolean isDefaultExcludes();

    void setDefaultExcludes( boolean value );

    boolean isCasesensitive();

    void setCasesensitive( boolean casesensitive );
}