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
package org.vaadin.netbeans.editor.analyzer.ui;

import javax.swing.SwingUtilities;

/**
 * @author denis
 */
public class StatePanel extends javax.swing.JPanel {

    public StatePanel( String name, String superClass ) {
        initComponents();
        if (name != null) {
            myName.setText(name);
        }

        if (superClass == null) {
            superClassLbl.setVisible(false);
            mySuperclass.setVisible(false);
        }
        else {
            mySuperclass.setText(superClass);
        }

        selectText();
    }

    public String getIfaceName() {
        return myName.getText().toString();
    }

    private void selectText() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                if (SwingUtilities.getWindowAncestor(StatePanel.this) == null) {
                    selectText();
                }
                else {
                    myName.requestFocusInWindow();
                    myName.selectAll();
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nameLbl = new javax.swing.JLabel();
        myName = new javax.swing.JTextField();
        superClassLbl = new javax.swing.JLabel();
        mySuperclass = new javax.swing.JTextField();

        nameLbl.setLabelFor(myName);
        org.openide.awt.Mnemonics.setLocalizedText(nameLbl,
                org.openide.util.NbBundle.getMessage(StatePanel.class,
                        "LBL_RpcInterfaceName")); // NOI18N

        superClassLbl.setLabelFor(mySuperclass);
        org.openide.awt.Mnemonics.setLocalizedText(superClassLbl,
                org.openide.util.NbBundle.getMessage(StatePanel.class,
                        "LBL_StateSuperClass")); // NOI18N

        mySuperclass.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                        layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(
                                        layout.createParallelGroup(
                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(nameLbl)
                                                .addComponent(superClassLbl))
                                .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(
                                        layout.createParallelGroup(
                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(
                                                        myName,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        304, Short.MAX_VALUE)
                                                .addComponent(mySuperclass))
                                .addContainerGap()));
        layout.setVerticalGroup(layout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                        layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(
                                        layout.createParallelGroup(
                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(nameLbl)
                                                .addComponent(
                                                        myName,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(
                                        layout.createParallelGroup(
                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(superClassLbl)
                                                .addComponent(
                                                        mySuperclass,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(
                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE)));

        nameLbl.getAccessibleContext().setAccessibleName(
                org.openide.util.NbBundle.getMessage(StatePanel.class,
                        "ACSN_RpcInterfaceName")); // NOI18N
        nameLbl.getAccessibleContext().setAccessibleDescription(
                org.openide.util.NbBundle.getMessage(StatePanel.class,
                        "ACSD_RpcInterfaceName")); // NOI18N
        myName.getAccessibleContext().setAccessibleName(
                nameLbl.getAccessibleContext().getAccessibleName());
        myName.getAccessibleContext().setAccessibleDescription(
                nameLbl.getAccessibleContext().getAccessibleDescription());
        superClassLbl.getAccessibleContext().setAccessibleName(
                org.openide.util.NbBundle.getMessage(StatePanel.class,
                        "ACSN_StateSuperclass")); // NOI18N
        superClassLbl.getAccessibleContext().setAccessibleDescription(
                org.openide.util.NbBundle.getMessage(StatePanel.class,
                        "ACSD_StateSuperclass")); // NOI18N
        mySuperclass.getAccessibleContext().setAccessibleName(
                superClassLbl.getAccessibleContext().getAccessibleName());
        mySuperclass.getAccessibleContext()
                .setAccessibleDescription(
                        superClassLbl.getAccessibleContext()
                                .getAccessibleDescription());
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField myName;

    private javax.swing.JTextField mySuperclass;

    private javax.swing.JLabel nameLbl;

    private javax.swing.JLabel superClassLbl;
    // End of variables declaration//GEN-END:variables
}
