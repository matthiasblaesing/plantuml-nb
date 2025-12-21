/*
 * The MIT License
 *
 * Copyright 2013 Venkat Ram Akkineni.
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
package org.netbeans.modules.plantumlnb.ui.actions;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import org.netbeans.modules.plantumlnb.pumlDataObject;
import org.netbeans.modules.plantumlnb.ui.FileFormatable;
import org.netbeans.modules.plantumlnb.ui.filefilter.AbstractImageFileFilter;
import org.netbeans.modules.plantumlnb.ui.filefilter.EPSFileFilter;
import org.netbeans.modules.plantumlnb.ui.filefilter.PNGFileFilter;
import org.netbeans.modules.plantumlnb.ui.filefilter.SVGFileFilter;
import org.netbeans.modules.plantumlnb.ui.io.PUMLGenerator;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.awt.NotificationDisplayer;
import org.openide.awt.StatusDisplayer;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;

/**
 *
 * @author venkat
 * @author markiewb
 */
@ActionID(
        category = "PUML",
        id = "org.netbeans.modules.plantumlnb.ui.actions.ExportAction"
)
@ActionRegistration(
        displayName = "#CTL_ExportAction"
)
@ActionReferences({
    @ActionReference(path = "Loaders/text/x-puml/Actions", position = 110),
    @ActionReference(path = "Editors/text/x-puml/Popup", position = 400)
})
@NbBundle.Messages("CTL_ExportAction=Export as image...")
public class ExportAction implements ActionListener {

    private static final Logger LOG = Logger.getLogger(ExportAction.class.getName());
    private static final Collection<AbstractImageFileFilter> availableFilters = Arrays.asList(new SVGFileFilter(), new PNGFileFilter(), new EPSFileFilter());

    @Override
    public void actionPerformed(ActionEvent e) {
        final pumlDataObject dataObject = Utilities.actionsGlobalContext().lookup(pumlDataObject.class);
        if (null == dataObject) {
            return;
        }
        if (null == dataObject.getPrimaryFile()) {
            return;
        }
        final File sourceFile = FileUtil.toFile(dataObject.getPrimaryFile());

        final JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(false);
        fc.setAcceptAllFileFilterUsed(false);
        //open in directory of current *.puml file
        fc.setCurrentDirectory(sourceFile);
        for (AbstractImageFileFilter filter : availableFilters) {
            fc.addChoosableFileFilter(filter);
        }

        int returnVal = fc.showSaveDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            final pumlDataObject finalDataObject = dataObject;
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    try {
                        File outputFile = fc.getSelectedFile().getCanonicalFile();
                        outputFile = addImageExtToFileNameIfNeeded(outputFile, fc.getFileFilter().getDescription());
                        FileFormatable f = getFileFormatableForFile(outputFile, availableFilters);
                        PUMLGenerator.getInstance().generateIntoFile(dataObject.getPrimaryFile(), outputFile, f.getFileFormat());

                        final String filePath = outputFile.getAbsolutePath();
                        String notificationText = "File " + filePath + " successully exported";
                        StatusDisplayer.getDefault().setStatusText("File saved to " + notificationText);
                        Icon successIcon = ImageUtilities.loadImageIcon("org/netbeans/modules/plantumlnb/ui/actions/check.png", true);
                        NotificationDisplayer.getDefault().notify("File Export", successIcon, notificationText, new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    Desktop.getDesktop().browse(new URI("file://" + filePath));
                                } catch (IOException | URISyntaxException ex) {
                                    LOG.log(Level.INFO, ex.getMessage());
                                }
                            }

                        });

                    } catch (IOException | RuntimeException ex) {
                        Exceptions.printStackTrace(ex);
                        String failNotificationText = "File " + sourceFile.getAbsolutePath() + " saved failed. ";
                        StatusDisplayer.getDefault().setStatusText(failNotificationText);
                        Icon failIcon = ImageUtilities.loadImageIcon("org/netbeans/modules/plantumlnb/ui/actions/close_delete.png", true);
                        NotificationDisplayer.getDefault().notify("File Export", failIcon, failNotificationText, null);
                    }

                }

            });

        }
    }

    private File addImageExtToFileNameIfNeeded(File file, String ext) throws IOException {
        //add default file extension, if no extension is provided
        if (!file.getName().contains(".")) {
            file = new File(file.getCanonicalPath() + "." + ext);
        }
        return file;
    }

    /**
     * Gets the format for the given file.
     *
     * @param file
     * @param availableFilters
     * @return
     */
    private FileFormatable getFileFormatableForFile(File file, Collection<AbstractImageFileFilter> availableFilters) {
        for (AbstractImageFileFilter filter : availableFilters) {
            if (filter.accept(file)) {
                return filter;
            }
        }
        return null;
    }
}
