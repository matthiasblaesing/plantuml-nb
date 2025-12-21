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
package org.netbeans.modules.plantumlnb.ui;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javax.swing.ActionMap;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import org.netbeans.api.settings.ConvertAsProperties;
import org.netbeans.modules.plantumlnb.RenderImageThread;
import org.netbeans.modules.plantumlnb.SVGImagePreviewPanel;
import org.netbeans.modules.plantumlnb.pumlDataObject;
import org.netbeans.modules.plantumlnb.ui.io.PUMLGenerator;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
    dtd = "-//org.netbeans.modules.plantumlnb//PUML//EN",
    autostore = false
)
@TopComponent.Description(
    preferredID = "PUMLTopComponent",
    iconBase = "org/netbeans/modules/plantumlnb/icon.png",
    persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(
    mode = "properties",
    openAtStartup = false
)
@ActionID(
    category = "Window",
    id = "org.netbeans.modules.plantumlnb.PUMLTopComponent"
)
@ActionReference(
    path = "Menu/Window",
    position = 19042
)
@TopComponent.OpenActionRegistration(
    displayName = "#CTL_PUMLAction",
    preferredID = "PUMLTopComponent"
)
@Messages({
    "CTL_PUMLAction=PlantUML",
    "CTL_PUMLTopComponent=PlantUML",
    "HINT_PUMLTopComponent=This is a PlantUML window"
})
public final class PUMLTopComponent extends TopComponent implements Serializable, ExplorerManager.Provider {

    private static final long serialVersionUID = -99094945997905090L;
    private static final RequestProcessor WORKER = new RequestProcessor(PUMLTopComponent.class.getName());

    /**
     * template for finding data in given context. Object used as example,
     * replace with your own data source, for example JavaDataObject etc
     */
    private static final Lookup.Template MY_DATA = new Lookup.Template(pumlDataObject.class);

    private final InstanceContent instanceContent = new InstanceContent();

    /**
     * current context to work on
     */
    private Lookup.Result currentContext;

    /**
     * listener to context changes
     */
    private LookupListener contextListener;

    /**
      * holds UI of this panel
      */
    private SVGImagePreviewPanel panelUI;
    private JScrollPane scrollPane;

    private DataObject currentDataObject;
    private long lastSaveTime = -1;

    private JToolBar jToolBar1 = null;

    private final ExplorerManager em = new ExplorerManager();

    private PUMLTopComponent() {
        initComponents();
        addCustomComponents();
        setName(Bundle.CTL_PUMLTopComponent());
        setToolTipText(Bundle.HINT_PUMLTopComponent());

        //connect standard CTRL-C action (in menu/toolbar) to own action
        ActionMap actionMap = getActionMap();
        actionMap.put("copy-to-clipboard",
                org.openide.awt.Actions.forID("PlantUML", "org.netbeans.modules.plantumlnb.ui.actions.CopyToClipboard"));
        associateLookup(new ProxyLookup(
                ExplorerUtils.createLookup(em, actionMap),
                new AbstractLookup(instanceContent)));

        WindowManager.getDefault().findMode("properties").dockInto(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private  void addCustomComponents(){
        panelUI = SVGImagePreviewPanel.getInstance();
        scrollPane = new javax.swing.JScrollPane();

        addToolbar();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(panelUI);
        panelUI.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 397, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 297, Short.MAX_VALUE)
        );

        scrollPane.setViewportView(panelUI);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addComponent(jToolBar1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                )
        );
    }// </editor-fold>

    private void addToolbar() {
        Toolbar.instance().setSvgImagePreviewPanel(panelUI);
        jToolBar1 = Toolbar.instance().createToolBar();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
          // lookup context and listen to result to get notified about context changes
        currentContext = getLookup().lookup(MY_DATA);
        currentContext.addLookupListener(getContextListener());
        // get actual data and recompute content
        Collection data = currentContext.allInstances();
        currentDataObject = getDataObject(data);

        setNewContent(currentDataObject);
    }

    @Override
    public void componentClosed() {
    }

    @Override
    public void componentActivated(){
        // lookup context and listen to result to get notified about context changes
        currentContext = getLookup().lookup(MY_DATA);
        currentContext.addLookupListener(getContextListener());
        // get actual data and recompute content
        Collection data = currentContext.allInstances();
        currentDataObject = getDataObject(data);
        setNewContent(currentDataObject);
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    /**
     * Not sure why this is needed but seems like it is.
     */
    public void getComponent() {
        if (lastSaveTime == -1) {
            lastSaveTime = System.currentTimeMillis();
        }
        add("Center", SVGImagePreviewPanel.getInstance());
    }

    public void setNewContent(final DataObject dataObject) {
        if (dataObject == null) {
            return;
        }
        if (currentDataObject == dataObject) {
            return;
        }

        currentDataObject = dataObject;

        DataObject dobj = getLookup().lookup(DataObject.class);
        if (dobj != null) {
            this.instanceContent.remove(dobj);
        }
        this.instanceContent.add(dataObject);

        WORKER.post(new Runnable() {
            @Override
            public void run() {
                Set fss = dataObject.files();
                Iterator iter = fss.iterator();
                while (iter.hasNext()) {
                    FileObject fo = (FileObject) iter.next();
                    setNewContent(PUMLGenerator.getInstance().generateSvgString(fo));
                }

                if (panelUI == null) {
                    SVGImagePreviewPanel.getInstance();
                }
            }
        });

    }

    public void setNewContent(final String imageContent) {
        if (imageContent == null) {
            return;
        }

        WORKER.post(new RenderImageThread(this, imageContent));
    }

    private DataObject getDataObject(Collection data) {
        DataObject dataObject = null;
        Iterator it = data.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof pumlDataObject) {
                dataObject = (DataObject) o;
                break;
            }
        }
        return dataObject;
    }

    /**
     * Accessor for listener to context
     */
    private LookupListener getContextListener() {
        if (contextListener == null) {
            contextListener = new PUMLTopComponent.ContextListener();
        }
        return contextListener;
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }

    /**
     * Listens to changes of context and triggers proper action
     */
    private class ContextListener implements LookupListener {

        @Override
        public void resultChanged(LookupEvent ev) {
            Collection data = ((Lookup.Result) ev.getSource()).allInstances();
            currentDataObject = getDataObject(data);
            setNewContent(currentDataObject);
        }
    }

    public static PUMLTopComponent getInstance() {
        return (PUMLTopComponent) WindowManager.getDefault().findTopComponent("PUMLTopComponent");
    }

    public void setCurrentDataObject(DataObject currentDataObject) {
        this.currentDataObject = currentDataObject;
    }

    public DataObject getCurrentDataObject() {
        return currentDataObject;
    }
}
