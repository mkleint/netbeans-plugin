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
package org.vaadin.netbeans;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.netbeans.api.java.source.ClasspathInfo;
import org.openide.execution.ExecutorTask;
import org.openide.filesystems.FileObject;
import org.vaadin.netbeans.model.ModelOperation;

/**
 * @author denis
 */
public interface VaadinSupport {

    public enum Action {
        COMPILE,
        COMPILE_THEME,
        RUN_JETTY,
        DEBUG_JETTY,
        DEV_MODE,
        DEBUG_DEV_MODE,
        SUPER_DEV_MODE;
    }

    boolean isEnabled();

    boolean isWeb();

    boolean isReady();

    void runModelOperation( ModelOperation operation ) throws IOException;

    void addAction( Action action, ExecutorTask task );

    Collection<ExecutorTask> getTasks( Action action );

    ClasspathInfo getClassPathInfo();

    List<String> getAddonWidgetsets();

    void setAddonWidgetsets( List<String> widgetset );

    FileObject getAddOnConfigFile();

}
