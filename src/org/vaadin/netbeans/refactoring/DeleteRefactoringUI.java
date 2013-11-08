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
package org.vaadin.netbeans.refactoring;

import javax.swing.event.ChangeListener;

import org.netbeans.modules.refactoring.api.AbstractRefactoring;
import org.netbeans.modules.refactoring.api.Problem;
import org.netbeans.modules.refactoring.api.SafeDeleteRefactoring;
import org.netbeans.modules.refactoring.spi.ui.CustomRefactoringPanel;
import org.netbeans.modules.refactoring.spi.ui.RefactoringUI;
import org.openide.filesystems.FileObject;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.lookup.Lookups;
import org.vaadin.netbeans.utils.XmlUtils;

/**
 * @author denis
 */
class DeleteRefactoringUI implements RefactoringUI {

    DeleteRefactoringUI( FileObject gwtXml ) {
        myRefactoring = new SafeDeleteRefactoring(Lookups.singleton(gwtXml));
    }

    @Override
    public Problem checkParameters() {
        return null;
    }

    @NbBundle.Messages({ "# {0} - module name", "deleteDescription=Delete {0}" })
    @Override
    public String getDescription() {
        return Bundle.deleteDescription(getModuleName());
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null;
    }

    @NbBundle.Messages("delete=Delete")
    @Override
    public String getName() {
        return Bundle.delete();
    }

    @Override
    public CustomRefactoringPanel getPanel( ChangeListener listener ) {
        return null;
    }

    @Override
    public AbstractRefactoring getRefactoring() {
        return myRefactoring;
    }

    @Override
    public boolean hasParameters() {
        return false;
    }

    @Override
    public boolean isQuery() {
        return false;
    }

    @Override
    public Problem setParameters() {
        return null;
    }

    protected String getModuleName() {
        String name = myRefactoring.getRefactoringSource()
                .lookup(FileObject.class).getName();
        if (name.endsWith(XmlUtils.GWT)) {
            // Should always happen
            name = name.substring(0, name.length() - XmlUtils.GWT.length());
        }
        return name;
    }

    private final SafeDeleteRefactoring myRefactoring;

}
