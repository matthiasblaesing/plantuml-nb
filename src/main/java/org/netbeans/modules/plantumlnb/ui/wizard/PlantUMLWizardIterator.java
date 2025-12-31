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
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import net.sourceforge.plantumldependency.commoncli.exception.CommandLineException;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.templates.TemplateRegistration;
import org.netbeans.modules.plantumlnb.generate.PlantUMLDependencyService;
import org.netbeans.modules.plantumlnb.generate.PlantUMLGenerationRequest;
import org.openide.WizardDescriptor;
import org.openide.awt.NotificationDisplayer;
import org.openide.awt.StatusDisplayer;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.ImageUtilities;

// TODO define position attribute
@TemplateRegistration(folder = "PlantUML",
        displayName = "#PlantUMLWizardIterator_displayName",
        iconBase = "org/netbeans/modules/plantumlnb/icon.png",
        description = "PlantUMLFromExistingSourcesDescription.html")
public final class PlantUMLWizardIterator implements WizardDescriptor.InstantiatingIterator<WizardDescriptor> {

    private static final Logger LOG = Logger.getLogger(PlantUMLWizardIterator.class.getName());

    private int index;

    private WizardDescriptor wizard;
    private List<WizardDescriptor.Panel<WizardDescriptor>> panels;

    private List<WizardDescriptor.Panel<WizardDescriptor>> getPanels() {
        if (panels == null) {
            panels = new ArrayList<>();
            panels.add(new PlantUMLWizardPanel1());
            panels.add(new PlantUMLWizardPanel2());
            panels.add(new PlantUMLWizardPanel3());
            panels.add(new PlantUMLWizardPanel4());
            panels.add(new PlantUMLWizardPanel5());
            String[] steps = createSteps();
            for (int i = 0; i < panels.size(); i++) {
                Component c = panels.get(i).getComponent();
                if (steps[i] == null) {
                    // Default step name to component name of panel. Mainly
                    // useful for getting the name of the target chooser to
                    // appear in the list of steps.
                    steps[i] = c.getName();
                }
                if (c instanceof JComponent jc) { // assume Swing components
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, i);
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
                    jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, true);
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, true);
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, true);
                }
            }
        }
        return panels;
    }

    // TODO return set of FileObject (or DataObject) you have created
    @Override
    public Set<?> instantiate() throws IOException {
        PlantUMLVisualPanel1 firstPanel = (PlantUMLVisualPanel1) panels.get(0).getComponent();
        PlantUMLVisualPanel2 secondPanel = (PlantUMLVisualPanel2) panels.get(1).getComponent();
        PlantUMLVisualPanel3 thirdPanel = (PlantUMLVisualPanel3) panels.get(2).getComponent();
        PlantUMLVisualPanel4 fourthPanel = (PlantUMLVisualPanel4) panels.get(3).getComponent();
        PlantUMLVisualPanel5 fifthPanel = (PlantUMLVisualPanel5) panels.get(4).getComponent();

        PlantUMLGenerationRequest request = new PlantUMLGenerationRequest();
        loadFirstPanelData(firstPanel, request);
        loadSecondPanelData(secondPanel, request);
        loadThirdPanelData(thirdPanel, request);
        loadFourthPanelData(fourthPanel, request);
        loadFifthPanelData(fifthPanel, request);

        try {
            PlantUMLDependencyService.generate(request);
        } catch (MalformedURLException | CommandLineException | ParseException ex) {
            LOG.log(Level.INFO, "PlantUML file creation from existing java sources failed.", ex);
            String failNotificationText = "Creation of " + request.getOutputFileName() + " PlantUML file for java files in " + request
                    .getSourcesDirectory() + " failed. ";
            StatusDisplayer.getDefault()
                    .setStatusText(failNotificationText);
            Icon failIcon = ImageUtilities.loadImageIcon("org/netbeans/modules/plantumlnb/ui/actions/close_delete.png",
                    true);
            NotificationDisplayer.getDefault()
                    .notify("PlantUML File From Java Sources", failIcon, failNotificationText, null);
        }

        Set<FileObject> fileSet = new HashSet<>();
        if(request.getOutputFile() != null) {
            fileSet.add(FileUtil.toFileObject(request.getOutputFile()));
        }

        return fileSet;
    }

    private void loadFirstPanelData(final PlantUMLVisualPanel1 firstPanel, final PlantUMLGenerationRequest request) {
        request.setDestinationDirectory(firstPanel.getDestinationDirectoryTextField().getText());
        request.setOutputFileName(firstPanel.getPlantumlFileNameTextField().getText());
        request.setSourcesDirectory(getSourcesDirectory(firstPanel));
    }

    private void loadSecondPanelData(final PlantUMLVisualPanel2 secondPanel, final PlantUMLGenerationRequest request) {
        request.setIncludePatterns(secondPanel.getIncludePattern());
        request.setExcludePatterns(secondPanel.getExcludePattern());
    }

    private void loadThirdPanelData(final PlantUMLVisualPanel3 thirdPanel, final PlantUMLGenerationRequest request) {
        request.setAbstractClasses(thirdPanel.getAbstractClasses());
        request.setAnnotations(thirdPanel.getAnnotations());
        request.setClasses(thirdPanel.getClasses());
        request.setEnums(thirdPanel.getEnums());
        request.setExtensions(thirdPanel.getExtensions());
        request.setImplementations(thirdPanel.getImplementations());
        request.setImports(thirdPanel.getImports());
        request.setInterfaces(thirdPanel.getInterfaces());
        request.setNativeMethods(thirdPanel.getNativeMethods());
        request.setStaticImports(thirdPanel.getStaticImports());
    }

    private void loadFourthPanelData(final PlantUMLVisualPanel4 fourthPanel, final PlantUMLGenerationRequest request) {
        request.setDisplayPackageNameRegex(fourthPanel.getDisplayPackageNameRegex());
    }

    private void loadFifthPanelData(final PlantUMLVisualPanel5 fifthPanel, final PlantUMLGenerationRequest request) {
        request.setDisplayNameRegex(fifthPanel.getDisplayNameRegex());
    }

    private String getSourcesDirectory(PlantUMLVisualPanel1 firstPanel) {
        JComboBox sourceGroupsComboBox = firstPanel.getSourceGroupsComboBox();
        SourceGroup sourceGroup = (SourceGroup) sourceGroupsComboBox.getSelectedItem();

        JComboBox packageSelectionComboBox = firstPanel.getPackageSelectionComboBox();
        String packageName = packageSelectionComboBox.getEditor().getItem().toString();
        packageName = packageName.replace(".", "/");

        return sourceGroup.getRootFolder().getPath() + "/" + packageName;
    }

    @Override
    public void initialize(WizardDescriptor wizard) {
        this.wizard = wizard;
    }

    @Override
    public void uninitialize(WizardDescriptor wizard) {
        panels = null;
    }

    @Override
    public WizardDescriptor.Panel<WizardDescriptor> current() {
        return getPanels().get(index);
    }

    @Override
    public String name() {
        return index + 1 + ". from " + getPanels().size();
    }

    @Override
    public boolean hasNext() {
        return index < getPanels().size() - 1;
    }

    @Override
    public boolean hasPrevious() {
        return index > 0;
    }

    @Override
    public void nextPanel() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        index++;
    }

    @Override
    public void previousPanel() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        index--;
    }

    // If nothing unusual changes in the middle of the wizard, simply:
    @Override
    public void addChangeListener(ChangeListener l) {
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
    }
    // If something changes dynamically (besides moving between panels), e.g.
    // the number of panels changes in response to user input, then use
    // ChangeSupport to implement add/removeChangeListener and call fireChange
    // when needed

    // You could safely ignore this method. Is is here to keep steps which were
    // there before this wizard was instantiated. It should be better handled
    // by NetBeans Wizard API itself rather than needed to be implemented by a
    // client code.
    private String[] createSteps() {
        String[] beforeSteps = (String[]) wizard.getProperty("WizardPanel_contentData");
        assert beforeSteps != null : "This wizard may only be used embedded in the template wizard";
        String[] res = new String[(beforeSteps.length - 1) + panels.size()];
        for (int i = 0; i < res.length; i++) {
            if (i < (beforeSteps.length - 1)) {
                res[i] = beforeSteps[i];
            } else {
                res[i] = panels.get(i - beforeSteps.length + 1).getComponent().getName();
            }
        }
        return res;
    }

}
