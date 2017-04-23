/* 
 * The MIT License
 *
 * Copyright 2017 Venkat Ram Akkineni.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.netbeans.modules.plantumlnb.ui.wizard;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors
public final class PlantUMLVisualPanel2 extends JPanel implements GenericDocumentListener,
        Initializable<PlantUMLWizardPanel2>, GenericChangeListener {

    @Setter
    @Getter
    private PlantUMLWizardPanel2 plantUMLWizardPanel2;

    private final List<ChangeListener> listeners = new ArrayList<>();

    /**
     * Creates new form PlantUMLVisualPanel2
     */
    private PlantUMLVisualPanel2() {
        initComponents();
    }

    @Override
    public void init(final PlantUMLWizardPanel2 plantUMLWizardPanel2) {
        this.plantUMLWizardPanel2 = plantUMLWizardPanel2;

        excludePatternTextField.getDocument()
                .addDocumentListener(this);
        includePatternTextField.getDocument()
                .addDocumentListener(this);
    }

    @Override
    public String getName() {
        return "Enter include/exclude patterns";
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        includePatternsLabel = new javax.swing.JLabel();
        excludePatternTextField = new javax.swing.JTextField();
        excludePatternsLabel = new javax.swing.JLabel();
        includePatternTextField = new javax.swing.JTextField();
        helpTextLabel = new javax.swing.JLabel();
        excludePatternsLabel1 = new javax.swing.JLabel();
        excludePatternsLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();

        includePatternsLabel.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(includePatternsLabel, org.openide.util.NbBundle.getMessage(PlantUMLVisualPanel2.class, "PlantUMLVisualPanel2.includePatternsLabel.text")); // NOI18N

        excludePatternTextField.setText(org.openide.util.NbBundle.getMessage(PlantUMLVisualPanel2.class, "PlantUMLVisualPanel2.excludePatternTextField.text")); // NOI18N

        excludePatternsLabel.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(excludePatternsLabel, org.openide.util.NbBundle.getMessage(PlantUMLVisualPanel2.class, "PlantUMLVisualPanel2.excludePatternsLabel.text")); // NOI18N

        includePatternTextField.setText(org.openide.util.NbBundle.getMessage(PlantUMLVisualPanel2.class, "PlantUMLVisualPanel2.includePatternTextField.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(helpTextLabel, org.openide.util.NbBundle.getMessage(PlantUMLVisualPanel2.class, "PlantUMLVisualPanel2.helpTextLabel.text")); // NOI18N
        helpTextLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        excludePatternsLabel1.setFont(new java.awt.Font("Dialog", 2, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(excludePatternsLabel1, org.openide.util.NbBundle.getMessage(PlantUMLVisualPanel2.class, "PlantUMLVisualPanel2.excludePatternsLabel1.text")); // NOI18N

        excludePatternsLabel2.setFont(new java.awt.Font("Dialog", 2, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(excludePatternsLabel2, org.openide.util.NbBundle.getMessage(PlantUMLVisualPanel2.class, "PlantUMLVisualPanel2.excludePatternsLabel2.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(helpTextLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 599, Short.MAX_VALUE)
                    .addComponent(excludePatternTextField)
                    .addComponent(jSeparator1)
                    .addComponent(includePatternTextField)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(includePatternsLabel)
                            .addComponent(excludePatternsLabel1)
                            .addComponent(excludePatternsLabel)
                            .addComponent(excludePatternsLabel2))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jSeparator2))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(includePatternsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(excludePatternTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(excludePatternsLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(excludePatternsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(includePatternTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(excludePatternsLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(helpTextLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField excludePatternTextField;
    private javax.swing.JLabel excludePatternsLabel;
    private javax.swing.JLabel excludePatternsLabel1;
    private javax.swing.JLabel excludePatternsLabel2;
    private javax.swing.JLabel helpTextLabel;
    private javax.swing.JTextField includePatternTextField;
    private javax.swing.JLabel includePatternsLabel;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    // End of variables declaration//GEN-END:variables

    public String getIncludePattern() {
        return includePatternTextField.getText();
    }
    
    public String getExcludePattern() {
        return excludePatternTextField.getText();
    }

    @Override
    public ValidatingWizardPanel getValidatingWizardPanel() {
        return plantUMLWizardPanel2;
    }

    public static PlantUMLVisualPanel2 createInstance(final PlantUMLWizardPanel2 plantUMLWizardPanel2) {
        PlantUMLVisualPanel2 plantUMLVisualPanel2 = new PlantUMLVisualPanel2();
        plantUMLVisualPanel2.init(plantUMLWizardPanel2);
        return plantUMLVisualPanel2;
    }

    @Override
    public void updateUI(DocumentEvent e) {
    }

    @Override
    public List<ChangeListener> getChangeListeners() {
        return listeners;
    }
    
}
