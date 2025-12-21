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

import java.io.IOException;
import java.util.Iterator;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import org.netbeans.core.spi.multiview.text.MultiViewEditorElement;
import org.netbeans.modules.plantumlnb.pumlDataObject;
import org.openide.util.Lookup;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;


/**
 * https://blogs.oracle.com/geertjan/entry/multiview_of_the_next_release
 * @author venkat
 *
 * NOTE: We don't need VisualElement Registration here. MultiViewEditorElement displays two views already.
 */

public final class pumlVisualElement extends MultiViewEditorElement {

    private static final long serialVersionUID = -6918553698868162650L;

    public static pumlVisualElement currentlyActivePUMLVisualElement = null;

    private pumlDataObject obj;
    private final JToolBar toolbar = new JToolBar();

    public pumlVisualElement(Lookup lkp) {
        super(lkp);
    }


    @Override
    public void componentActivated() {
        super.componentActivated();
        PUMLTopComponent pumltc = PUMLTopComponent.getInstance();
        this.obj = getLookup().lookup(pumlDataObject.class);
        if(pumltc != null) {
            pumltc.setNewContent(obj);
        }
        currentlyActivePUMLVisualElement = this;
    }

    /**
     * http://netbeans-org.1045718.n5.nabble.com/Open-TopComponent-Programatically-td3036307.html
     */
    @Override
    public void componentOpened() {
        super.componentOpened();

        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                PUMLTopComponent pumltc = PUMLTopComponent.getInstance();

                // Register the instantiated PUMLTopComponent with Netbeans.
                Mode propertiesMode = WindowManager.getDefault().findMode("properties");
                propertiesMode.dockInto(pumltc);
                pumltc.open();
                pumltc.setNewContent(obj);
            }
        });

    }


    public PUMLTopComponent retrievePUMLTopComponent() {
        Iterator i = TopComponent.getRegistry().getOpened().iterator();

        PUMLTopComponent pumltc = null;
        while(i.hasNext()){
            TopComponent tc = (TopComponent) i.next();
            if(tc instanceof PUMLTopComponent pUMLTopComponent) {
                pumltc = pUMLTopComponent;
                break;
            }
        }

        return pumltc;
    }

    /**
     * TODO: Implement this method.
     *
     * http://stackoverflow.com/questions/16765304/topcomponent-not-serializable
     * @param out
     * @throws IOException
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {

    }

    /**
     * TODO: Implement this method. Refer to the link in writeObject comment.
     *
     * @param in
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {

    }

}
