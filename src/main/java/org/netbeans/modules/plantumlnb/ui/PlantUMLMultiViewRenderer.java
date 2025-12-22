/*
 * The MIT License
 *
 * Copyright 2025 matthias.
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
package org.netbeans.modules.plantumlnb.ui;

import java.awt.geom.AffineTransform;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.netbeans.modules.plantumlnb.SVGImagePreviewPanel;
import org.netbeans.modules.plantumlnb.pumlDataObject;
import org.netbeans.modules.plantumlnb.ui.io.PUMLGenerator;
import org.openide.awt.UndoRedo;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.Lookups;

import static org.openide.windows.TopComponent.PERSISTENCE_ONLY_OPENED;

@NbBundle.Messages({
    "MV_PlantUML=PlantUML"
})
@MultiViewElement.Registration(
        mimeType = "text/x-puml",
        persistenceType = PERSISTENCE_ONLY_OPENED,
        preferredID = "PlantUMLMultiViewRenderer",
        displayName = "#MV_PlantUML",
        position = 2000
)
public class PlantUMLMultiViewRenderer implements MultiViewElement, Externalizable {

    static final long serialVersionUID = 1;

    private final SVGImagePreviewPanel panelUI;
    private final JToolBar toolbar;
    private MultiViewElementCallback multiViewElementCallback;
    private pumlDataObject dataObject;
    private FileObject fileObject;
    private final FileChangeListener fcl = new FileChangeListener() {
        @Override
        public void fileFolderCreated(FileEvent fe) {
        }

        @Override
        public void fileDataCreated(FileEvent fe) {
        }

        @Override
        public void fileChanged(FileEvent fe) {
            render();
        }

        @Override
        public void fileDeleted(FileEvent fe) {
        }

        @Override
        public void fileRenamed(FileRenameEvent fre) {
        }

        @Override
        public void fileAttributeChanged(FileAttributeEvent fae) {
        }
    };

    public PlantUMLMultiViewRenderer() {
        this.panelUI = new SVGImagePreviewPanel();
        this.toolbar = new Toolbar(panelUI);
    }

    public PlantUMLMultiViewRenderer(Lookup lookup) {
        this();
        this.dataObject = lookup.lookup(pumlDataObject.class);
        this.fileObject = dataObject.getPrimaryFile();
        this.fileObject.addFileChangeListener( WeakListeners.create(FileChangeListener.class, fcl, this.fileObject));
    }

    @Override
    public JComponent getVisualRepresentation() {
        return panelUI;
    }

    @Override
    public JComponent getToolbarRepresentation() {
        return toolbar;
    }

    @Override
    public Action[] getActions() {
        return multiViewElementCallback.createDefaultActions();
    }

    @Override
    public Lookup getLookup() {
        if (dataObject != null) {
            return dataObject.getLookup();
        } else {
            return Lookups.fixed();
        }
    }

    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
    }

    @Override
    public void componentShowing() {
        render();
    }

    @Override
    public void componentHidden() {
    }

    @Override
    public void componentActivated() {
    }

    @Override
    public void componentDeactivated() {
    }

    @Override
    public UndoRedo getUndoRedo() {
        return null;
    }

    @Override
    public void setMultiViewCallback(MultiViewElementCallback mvec) {
        this.multiViewElementCallback = mvec;
    }

    @Override
    public CloseOperationState canCloseElement() {
        return CloseOperationState.STATE_OK;
    }

    private void render() {
        if (dataObject != null) {
            panelUI.renderSVGFile(() -> {
                FileObject fo = dataObject.getPrimaryFile();
                return PUMLGenerator.getInstance().generateSvgString(fo);
            });
        }
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.write(1);
        out.writeObject(dataObject);
        AffineTransform transform = panelUI.getRenderingTransform();
        out.writeObject(transform);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int version = in.read();
        dataObject = (pumlDataObject) in.readObject();
        fileObject = dataObject.getPrimaryFile();
        fileObject.addFileChangeListener( WeakListeners.create(FileChangeListener.class, fcl, this.fileObject));
        AffineTransform transform = (AffineTransform) in.readObject();
        panelUI.setRenderingTransform(transform, false);
        render();
    }

}
