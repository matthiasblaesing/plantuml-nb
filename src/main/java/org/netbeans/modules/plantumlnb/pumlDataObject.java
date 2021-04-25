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
package org.netbeans.modules.plantumlnb;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import org.netbeans.core.api.multiview.MultiViews;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.text.MultiViewEditorElement;
import org.netbeans.modules.plantumlnb.ui.PUMLTopComponent;
import org.netbeans.modules.plantumlnb.ui.io.PUMLGenerator;
import org.netbeans.modules.plantumlnb.ui.pumlVisualElement;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.text.CloneableEditorSupport;
import org.openide.text.CloneableEditorSupport.Pane;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

@Messages({
    "LBL_PUML_LOADER=Files of PUML"
})
@MIMEResolver.ExtensionRegistration(
    displayName = "#LBL_PUML_LOADER",
    mimeType = "text/x-puml",
    extension = {"puml", "PUML"},
    position = 2043
)
@DataObject.Registration(
    mimeType = "text/x-puml",
    iconBase = "org/netbeans/modules/plantumlnb/icon.png",
    displayName = "#LBL_PUML_LOADER",
    position = 300
)
@ActionReferences({
    @ActionReference(
        path = "Loaders/text/x-puml/Actions",
        id = @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
        position = 100,
        separatorAfter = 200
    ),
    @ActionReference(
        path = "Loaders/text/x-puml/Actions",
        id = @ActionID(category = "Edit", id = "org.openide.actions.CutAction"),
        position = 300
    ),
    @ActionReference(
        path = "Loaders/text/x-puml/Actions",
        id = @ActionID(category = "Edit", id = "org.openide.actions.CopyAction"),
        position = 400,
        separatorAfter = 500
    ),
    @ActionReference(
        path = "Loaders/text/x-puml/Actions",
        id = @ActionID(category = "Edit", id = "org.openide.actions.DeleteAction"),
        position = 600
    ),
    @ActionReference(
        path = "Loaders/text/x-puml/Actions",
        id = @ActionID(category = "System", id = "org.openide.actions.RenameAction"),
        position = 700,
        separatorAfter = 800
    ),
    @ActionReference(
        path = "Loaders/text/x-puml/Actions",
        id = @ActionID(category = "System", id = "org.openide.actions.SaveAsTemplateAction"),
        position = 900,
        separatorAfter = 1000
    ),
    @ActionReference(
        path = "Loaders/text/x-puml/Actions",
        id = @ActionID(category = "System", id = "org.openide.actions.FileSystemAction"),
        position = 1100,
        separatorAfter = 1200
    ),
    @ActionReference(
        path = "Loaders/text/x-puml/Actions",
        id = @ActionID(category = "System", id = "org.openide.actions.ToolsAction"),
        position = 1300
    ),
    @ActionReference(
        path = "Loaders/text/x-puml/Actions",
        id = @ActionID(category = "System", id = "org.openide.actions.PropertiesAction"),
        position = 1400
    )
})
public class pumlDataObject extends MultiDataObject implements FileChangeListener, Callable<CloneableEditorSupport.Pane>, PropertyChangeListener {
    private static final Logger LOG = Logger.getLogger(pumlDataObject.class.getName());
    private FileObject fileObject;
    private AffineTransform currentAT;

    public pumlDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);        
        fileObject = pf;
        pf.addFileChangeListener(this);
        registerEditor("text/x-puml", true);
        
        this.currentAT = new AffineTransform();
    }

    @Override
    protected int associateLookup() {
        return 1;
    }
    
    @MultiViewElement.Registration(
        displayName = "#LBL_puml_EDITOR",
    iconBase = "org/netbeans/modules/plantumlnb/icon.png",
    mimeType = "text/x-puml",
    persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED,
    preferredID = "puml",
    position = 1200)
    @Messages("LBL_puml_EDITOR=Source")
    public static MultiViewEditorElement createEditor(Lookup lkp) {
        return ((MultiViewEditorElement ) new pumlVisualElement(lkp));
    }
    

    
    // Michael Wever 26/09/2001
    /** Gets image for the image data 
     * @return the image or <code>null</code> if image could not be created
     * @return  java.io.IOException  if an error occurs during reading
     */
    public Image getImage() throws IOException {
        InputStream inputStream = getPrimaryFile().getInputStream();
        BufferedImage image = null;
        try {            
            image = ImageIO.read(inputStream);        
        } catch(IOException e){
            Logger.getLogger(pumlDataObject.class.getName()).info(e.getMessage());
        }
        
        return image;       
    }

    @Override
    public void fileFolderCreated(FileEvent fe) {}

    @Override
    public void fileDataCreated(FileEvent fe) {}

    @Override
    public void fileChanged(FileEvent fe) {
        
        final DataObject.Registry registries = DataObject.getRegistry();

        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                PUMLTopComponent tc = PUMLTopComponent.getInstance();

                DataObject[] objects = registries.getModified();

                tc.setCurrentDataObject(pumlDataObject.this);

                tc.setNewContent(PUMLGenerator.getInstance().generateSvgString(fileObject));
            }

        });

    }

    @Override
    public void fileDeleted(FileEvent fe) {}

    @Override
    public void fileRenamed(FileRenameEvent fre) {}

    @Override
    public void fileAttributeChanged(FileAttributeEvent fae) {}

    /**
     * https://blogs.oracle.com/geertjan/entry/adding_a_history_tab_to
     * @return
     * @throws Exception 
     */
    @Override
    public Pane call() throws Exception {
        return (Pane) MultiViews.createCloneableMultiView("text/x-puml", this);
    }

    public AffineTransform getCurrentAT() {
        return currentAT;
    }

    public void setCurrentAT(AffineTransform currentAT) {
        this.currentAT = currentAT;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.currentAT = (AffineTransform) evt.getNewValue();
        double[] flatMatrix = new double[6];
        this.currentAT.getMatrix(flatMatrix);
        LOG.log(Level.INFO, "Current Transform: " + Arrays.toString(flatMatrix));
    }
    
}
