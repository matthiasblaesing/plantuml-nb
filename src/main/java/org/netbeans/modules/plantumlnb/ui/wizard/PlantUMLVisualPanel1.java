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

import java.awt.Component;
import java.io.File;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.sourceforge.plantumldependency.common.constants.CharacterConstants;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.project.ui.OpenProjects;
import org.netbeans.spi.java.project.support.ui.PackageView;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;

import static org.netbeans.modules.plantumlnb.StringUtils.isNotEmpty;

public final class PlantUMLVisualPanel1 extends JPanel {

    private static final Logger LOG = Logger.getLogger(PlantUMLVisualPanel1.class.getName());

    private final PlantUMLWizardPanel1 plantUMLWizardPanel1;
    private JFileChooser fileChooser = null;
    private SourceGroup[] sourceGroups;

    /**
     * Creates new form PlantUMLVisualPanel1
     */
    public PlantUMLVisualPanel1(PlantUMLWizardPanel1 plantUMLWizardPanel1) {
        initComponents();

        this.plantUMLWizardPanel1 = plantUMLWizardPanel1;

        sourceGroupsComboBox.setRenderer(new SourceGroupListCellRenderer());
        packageSelectionComboBox.setRenderer(PackageView.listRenderer());

        getPlantumlFileNameTextField().getDocument().addDocumentListener(new GenericDocumentListener());
        getDestinationDirectoryTextField().getDocument().addDocumentListener(new GenericDocumentListener());
    }

    @Override
    public String getName() {
        return "Choose a source package";
    }

    @Override
    public void validate() {
        super.validate();
    }

    public void updateUI(DocumentEvent e) {
        if (getPlantumlFileNameTextField().getDocument() == e.getDocument()) {
            firePropertyChange(DESTINATION_DIRECTORY, null, destinationDirectoryTextField.getText());
        }
//        if (getPackageSelectionInputDirectory().getDocument() == e.getDocument()) {
//            firePropertyChange(PLANTUML_FILE_NAME, null, plantumlFileNameTextField.getText());
//        }
//        if (getPackageSelectionInputDirectory().getDocument() == e.getDocument()) {
//            firePropertyChange(PACKAGE_SELECTION, null, packageSelectionInputDirectory.getText());
//        }
    }

    public void initValues( FileObject template, FileObject preselectedFolder, SourceGroup[] sourceGroups ) {
        this.sourceGroups = sourceGroups;

        sourceGroupsComboBox.setModel(new DefaultComboBoxModel(sourceGroups));
        SourceGroup preselectedGroup = getPreselectedGroup(preselectedFolder);
        sourceGroupsComboBox.setSelectedItem(preselectedGroup);
        sourceGroupsComboBox.setToolTipText(preselectedGroup.getDisplayName());
        updatePackages(preselectedGroup, getSelectedPackage(preselectedFolder)
                .orElse(""));

//        setSelectedPackage(preselectedFolder);
    }

    private String showOpenDialog(String dialogTitle) {
        fileChooser = new JFileChooser(new File(System.getProperty("user.home")));
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle(dialogTitle);
        Project mainProject = OpenProjects.getDefault().getMainProject();
        File currentDirectory = new File(System.getProperty("user.home"));

        if(mainProject != null) {
            currentDirectory = FileUtil.toFile(mainProject.getProjectDirectory());
        }

        fileChooser.setCurrentDirectory(currentDirectory);
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            //This is where a real application would open the file.
            LOG.log(Level.INFO, "Opening: {0}.", new Object[]{file.getName()});
            return fileChooser.getSelectedFile().getAbsolutePath();
        }

        LOG.log(Level.INFO, "Open command cancelled by user.");
        return null;

    }

    private RequestProcessor.Task updatePackagesTask = null;

    private static final ComboBoxModel WAIT_MODEL = new DefaultComboBoxModel(
        new String[] {
            NbBundle.getMessage( PlantUMLVisualPanel1.class, "PlantUMLVisualPanel1_PackageName_PleaseWait" ) // NOI18N
        }
    );

    private void setSelectedPackage(final FileObject selectedDirectory) {
        Optional<String> selectedPackage = getSelectedPackage(selectedDirectory);

        int selectedIndex = IntStream.of(packageSelectionComboBox.getItemCount())
            .filter(index -> packageSelectionComboBox.getItemAt(index)
            .equals(selectedPackage.orElse("")))
            .findFirst()
            .getAsInt();

        packageSelectionComboBox.setSelectedIndex(selectedIndex);
        packageSelectionComboBox.setToolTipText(selectedPackage.orElse(""));
    }

    private void updatePackages(final SourceGroup selectedSourceGroup, final String selDir) {
        WAIT_MODEL.setSelectedItem(packageSelectionComboBox.getEditor()
                .getItem());
        packageSelectionComboBox.setModel( WAIT_MODEL );
        if ( updatePackagesTask != null) {
            updatePackagesTask.cancel();
        }

        if (sourceGroups != null && sourceGroups.length > 0) {
            SourceGroup sourceGroup = selectedSourceGroup == null ? sourceGroups[0] : selectedSourceGroup;

            updatePackagesTask = new RequestProcessor("ComboUpdatePackages").post(() -> {
                final ComboBoxModel model = PackageView.createListView(sourceGroup);

                SwingUtilities.invokeLater(() -> {
                    model.setSelectedItem(""); // A work around to make sure selection works everytime.
                    model.setSelectedItem(isNotEmpty(selDir) ? selDir : packageSelectionComboBox.getEditor().getItem());
                    packageSelectionComboBox.setModel(model);
                });
            });
        }
    }

    private SourceGroup getPreselectedGroup(FileObject folder) {
        if (folder == null) {
            return sourceGroups[0];
        }

        Optional<SourceGroup> selectedSourceGroup = Arrays.asList(sourceGroups).stream().filter(sourceGroup -> {
            FileObject root = ((SourceGroup) sourceGroup).getRootFolder();
            return root.equals(folder) || FileUtil.isParentOf(root, folder);
        }).findFirst();

        if (!selectedSourceGroup.isPresent()) {
            return Arrays.asList(sourceGroups).get(0);
        }

        return selectedSourceGroup.get();
    }

    /**
     * Get a package combo model item for the package the user selected before opening the wizard.
     * May return null if it cannot find it; or a String instance if there is a well-defined
     * package but it is not listed among the packages shown in the list model.
     */
    private Optional<String> getSelectedPackage(FileObject selectedDirectory) {
        SourceGroup selectedGroup = getPreselectedGroup( selectedDirectory );
        if ( selectedDirectory == null || selectedGroup == null ) {
            return Optional.empty();
        }
        FileObject root = selectedGroup.getRootFolder();
        String relPath = FileUtil.getRelativePath( root, selectedDirectory );

        if ( relPath != null ) {
            relPath = relPath.replace('/', '.');
        }
        return Optional.ofNullable(relPath);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        existingSourcesPaneLabel = new javax.swing.JLabel();
        plantumlFileNameTextField = new javax.swing.JTextField();
        destinationDirectoryTextField = new javax.swing.JTextField();
        destinationDirectoryButton = new javax.swing.JButton();
        pumlFileNameLabel = new javax.swing.JLabel();
        generatedFileNameLabel = new javax.swing.JLabel();
        packageSelectionLabel = new javax.swing.JLabel();
        destinationDirectoryLabel = new javax.swing.JLabel();
        packageSelectionComboBox = new javax.swing.JComboBox<>();
        sourceGroupsComboBox = new javax.swing.JComboBox<>();
        packageSelectionLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        generatedFileNameDisplayTextArea = new javax.swing.JTextArea();

        org.openide.awt.Mnemonics.setLocalizedText(existingSourcesPaneLabel, org.openide.util.NbBundle.getMessage(PlantUMLVisualPanel1.class, "PlantUMLVisualPanel1.existingSourcesPaneLabel.text")); // NOI18N

        plantumlFileNameTextField.setText(org.openide.util.NbBundle.getMessage(PlantUMLVisualPanel1.class, "PlantUMLVisualPanel1.plantumlFileNameTextField.text")); // NOI18N
        plantumlFileNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                updateGeneratedFileNameDisplayLabel();
            }
        });

        destinationDirectoryTextField.setText(org.openide.util.NbBundle.getMessage(PlantUMLVisualPanel1.class, "PlantUMLVisualPanel1.destinationDirectoryTextField.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(destinationDirectoryButton, org.openide.util.NbBundle.getMessage(PlantUMLVisualPanel1.class, "PlantUMLVisualPanel1.destinationDirectoryButton.text")); // NOI18N
        destinationDirectoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                destinationDirectoryButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(pumlFileNameLabel, org.openide.util.NbBundle.getMessage(PlantUMLVisualPanel1.class, "PlantUMLVisualPanel1.pumlFileNameLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(generatedFileNameLabel, org.openide.util.NbBundle.getMessage(PlantUMLVisualPanel1.class, "PlantUMLVisualPanel1.generatedFileNameLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(packageSelectionLabel, org.openide.util.NbBundle.getMessage(PlantUMLVisualPanel1.class, "PlantUMLVisualPanel1.packageSelectionLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(destinationDirectoryLabel, org.openide.util.NbBundle.getMessage(PlantUMLVisualPanel1.class, "PlantUMLVisualPanel1.destinationDirectoryLabel.text")); // NOI18N

        packageSelectionComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                packageSelectionComboBoxActionPerformed(evt);
            }
        });

        sourceGroupsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handleSourceGroupChange(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(packageSelectionLabel1, org.openide.util.NbBundle.getMessage(PlantUMLVisualPanel1.class, "PlantUMLVisualPanel1.packageSelectionLabel1.text")); // NOI18N

        generatedFileNameDisplayTextArea.setEditable(false);
        generatedFileNameDisplayTextArea.setColumns(20);
        generatedFileNameDisplayTextArea.setRows(5);
        generatedFileNameDisplayTextArea.setWrapStyleWord(true);
        jScrollPane1.setViewportView(generatedFileNameDisplayTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(packageSelectionComboBox, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(existingSourcesPaneLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(plantumlFileNameTextField)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(packageSelectionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(275, 275, 275))
                    .addComponent(destinationDirectoryLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(destinationDirectoryTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(destinationDirectoryButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2))
                    .addComponent(packageSelectionLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(generatedFileNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addComponent(sourceGroupsComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pumlFileNameLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(existingSourcesPaneLabel)
                .addGap(18, 18, 18)
                .addComponent(pumlFileNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(plantumlFileNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(destinationDirectoryLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(destinationDirectoryTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(destinationDirectoryButton, javax.swing.GroupLayout.PREFERRED_SIZE, 21, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(packageSelectionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sourceGroupsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(packageSelectionLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(packageSelectionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(generatedFileNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void destinationDirectoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_destinationDirectoryButtonActionPerformed
        String returnVal = showOpenDialog(NbBundle.getMessage(PlantUMLVisualPanel1.class, "PlantUMLVisualPanel1.destinationDirectoryButton.text"));
        if(returnVal != null) {
            destinationDirectoryTextField.setText(returnVal);
            destinationDirectoryTextField.setToolTipText(returnVal);
            updateGeneratedFileNameDisplayLabel();
        }
    }//GEN-LAST:event_destinationDirectoryButtonActionPerformed

    private void updateGeneratedFileNameDisplayLabel() {//GEN-FIRST:event_updateGeneratedFileNameDisplayLabel
        if (destinationDirectoryTextField.getText() != null && plantumlFileNameTextField.getText() != null) {
            String destDir = destinationDirectoryTextField.getText();
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                destDir += destDir.endsWith(CharacterConstants.BACK_SLASH_CHAR) ? "" : CharacterConstants.BACK_SLASH_CHAR;
            } else {
                destDir += destDir.endsWith(CharacterConstants.SLASH_CHAR) ? "" : CharacterConstants.SLASH_CHAR;
            }

            String fqFileName = destDir + plantumlFileNameTextField.getText() + ".puml";
            generatedFileNameDisplayTextArea.setText(fqFileName);
            generatedFileNameDisplayTextArea.setToolTipText(fqFileName);
        }
    }//GEN-LAST:event_updateGeneratedFileNameDisplayLabel

    private void handleSourceGroupChange(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_handleSourceGroupChange
        SourceGroup selectedSourceGroup = (SourceGroup) ((JComboBox) evt.getSource()).getSelectedItem();
        updatePackages(selectedSourceGroup, null);
    }//GEN-LAST:event_handleSourceGroupChange

    private void packageSelectionComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_packageSelectionComboBoxActionPerformed
        plantUMLWizardPanel1.fireChangeEvent();
        updateUI();
    }//GEN-LAST:event_packageSelectionComboBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton destinationDirectoryButton;
    private javax.swing.JLabel destinationDirectoryLabel;
    private javax.swing.JTextField destinationDirectoryTextField;
    private javax.swing.JLabel existingSourcesPaneLabel;
    private javax.swing.JTextArea generatedFileNameDisplayTextArea;
    private javax.swing.JLabel generatedFileNameLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JComboBox<String> packageSelectionComboBox;
    private javax.swing.JLabel packageSelectionLabel;
    private javax.swing.JLabel packageSelectionLabel1;
    private javax.swing.JTextField plantumlFileNameTextField;
    private javax.swing.JLabel pumlFileNameLabel;
    private javax.swing.JComboBox<String> sourceGroupsComboBox;
    // End of variables declaration//GEN-END:variables

    private static final String DESTINATION_DIRECTORY = "destinationDirectoryTextField";
    private static final String PACKAGE_SELECTION = "packageSelectionInputDirectory";


    public JTextField getDestinationDirectoryTextField() {
        return destinationDirectoryTextField;
    }

    public void setDestinationDirectoryTextField(JTextField destinationDirectoryTextField) {
        this.destinationDirectoryTextField = destinationDirectoryTextField;
    }

    public JTextField getPlantumlFileNameTextField() {
        return plantumlFileNameTextField;
    }

    public void setPlantumlFileNameTextField(JTextField plantumlFileNameTextField) {
        this.plantumlFileNameTextField = plantumlFileNameTextField;
    }

    public JComboBox<String> getPackageSelectionComboBox() {
        return packageSelectionComboBox;
    }

    public void setPackageSelectionComboBox(JComboBox<String> packageSelectionComboBox) {
        this.packageSelectionComboBox = packageSelectionComboBox;
    }

    public JComboBox<String> getSourceGroupsComboBox() {
        return sourceGroupsComboBox;
    }

    public void setSourceGroupsComboBox(JComboBox<String> sourceGroupsComboBox) {
        this.sourceGroupsComboBox = sourceGroupsComboBox;
    }

    class GenericDocumentListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            plantUMLWizardPanel1.fireChangeEvent();
            updateUI();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            plantUMLWizardPanel1.fireChangeEvent();
            updateUI();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            plantUMLWizardPanel1.fireChangeEvent();
            updateUI();
        }

    }

     /**
     * Displays a {@link SourceGroup} in {@link #sourceGroupsComboBox}.
     */
    private static final class SourceGroupListCellRenderer extends DefaultListCellRenderer/*<SourceGroup>*/ {

        public SourceGroupListCellRenderer() {}

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value != null && value instanceof SourceGroup) {
                SourceGroup g = (SourceGroup) value;
                String name = g.getDisplayName();
                Icon icon = g.getIcon(false);
                super.getListCellRendererComponent(list, name, index, isSelected, cellHasFocus);
                setIcon(icon);
                setToolTipText(name);
            }
            return this;
        }

    }

}
