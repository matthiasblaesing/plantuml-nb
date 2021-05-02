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

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.gvt.GVTTreeRendererListener;
import org.apache.batik.util.XMLResourceDescriptor;
import org.netbeans.api.annotations.common.NonNull;
import org.netbeans.modules.plantumlnb.ui.PUMLJSVGCanvas;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.w3c.dom.svg.SVGDocument;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

/**
 *
 * @author venkat
 */
public class SVGImagePreviewPanel extends JPanel {
    
    private PUMLJSVGCanvas canvas;
    private String currentImageContent = "";
    private SVGDocument currentDocument = null;
    private pumlDataObject currentDataObject;
    private static final double ZOOM_OUT_FACTOR = 0.9;
    private static final String ROTATE_FACTOR = "0.0872664626";
    private static final Logger logger = Logger.getLogger(SVGImagePreviewPanel.class.getName());
    private final RenderWithTransformGVTTreeRendererListener gvttrListener = new RenderWithTransformGVTTreeRendererListener();
    
    private static SVGImagePreviewPanel instance = null;
    
    /**
     * 
     * @param svgFile 
     */
    private SVGImagePreviewPanel() {
        canvas = new PUMLJSVGCanvas();

        //http://mail-archives.apache.org/mod_mbox/xmlgraphics-batik-users/200811.mbox/%3C82615BD530B1FA449BAEB584F01FFDBA0169E9FE@UHQEX30.ad.jfcom.mil%3E
        //http://mcc.id.au/2007/09/batik-course/

        JPopupMenu popup = null;
        //add popup menu
        List<? extends Action> actionsForPath = Utilities.actionsForPath("Actions/PlantUML/ImageView/Popupmenu");
        if (!actionsForPath.isEmpty()) {
            Action[] actions = actionsForPath.toArray(new Action[actionsForPath.size()]);
            popup = Utilities.actionsToPopup(actions, Lookup.EMPTY);
        }

        final JPopupMenu finalPopup = popup;

        addComponentListener(new ResizeListener());
        canvas.setEnableImageZoomInteractor(true);
        canvas.setEnablePanInteractor(true);
        canvas.setEnableResetTransformInteractor(true);
        canvas.setEnableRotateInteractor(true);
        canvas.setEnableZoomInteractor(true);
        canvas.addMouseListener(new MouseAdapter() {
            boolean isModifierClick = false;

            @Override
            public void mousePressed(MouseEvent e) {
                this.isModifierClick = e.isShiftDown() || e.isAltDown() || e.isAltGraphDown() || e.isControlDown() || e.isMetaDown();
                if(isModifierClick) {
                    return;
                } else if(e.isPopupTrigger() && finalPopup != null) {
                    finalPopup.show(canvas, e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(isModifierClick) {
                    return;
                } else if(e.isPopupTrigger() && finalPopup != null) {
                    finalPopup.show(canvas, e.getX(), e.getY());
                }
            }
        });

        add("Center", canvas);
    }

    public void renderSVGFileOnTabSwitch(@NonNull String imageContent) {
        canvas.addGVTTreeRendererListener(gvttrListener);
        renderSVGFile(imageContent);
    }

    /**
     * @param imageContent
     */
    public void renderSVGFile(@NonNull String imageContent) {
        if(!"".equals(imageContent)) {
            currentImageContent = imageContent;
            canvas.setSize(getSize());
            canvas.setSVGDocument(createSVGDocument(new StringReader(imageContent)));
        } else {
            logger.log(Level.INFO, "Svg image content is either null or empty, so, refraining for rendering the current plantuml image. ");
        }
    }

    public SVGDocument createSVGDocument(StringReader sr) {
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);                     

        try {
            currentDocument = f.createSVGDocument("http://www.w3.org/2000/svg", sr);
            currentDocument.getRootElement().getAttributes().removeNamedItem("preserveAspectRatio");
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        return currentDocument;
    }
    
    private StringReader readInputStream(InputStream is) {
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);        
        
        String puml = "";
        String sCurrentLine = "";
        try {
            while ((sCurrentLine = br.readLine()) != null) {
                    puml += sCurrentLine;
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            try {
                is.close();
                isr.close();
                br.close(); 
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }            
        }
        
        return new StringReader(puml);
    }

    public PUMLJSVGCanvas getCanvas() {
        return canvas;
    }

    public void setCanvas(PUMLJSVGCanvas canvas) {
        this.canvas = canvas;
    }
        
    public pumlDataObject getCurrentDataObject() {
        return currentDataObject;
    }

    /**
     * Unbind the currentDataObject and bind the new one. This way everytime a
     * tab switch happens, correct dataObject ( i.e. the dataObject of the
     * plantuml currently displaying inside the Preview panel ) is listening to
     * changes in the AffineTransform.
     *
     * @param newDataObject
     */
    public void setCurrentDataObject(@NonNull pumlDataObject newDataObject) {
        canvas.removePropertyChangeListener(PUMLJSVGCanvas.renderingTransformPropertyName, this.currentDataObject);
        this.currentDataObject = newDataObject;
        canvas.addPropertyChangeListener(PUMLJSVGCanvas.renderingTransformPropertyName, this.currentDataObject);
    }

    public static SVGImagePreviewPanel getInstance() {
        if(null == instance) {
            instance = new SVGImagePreviewPanel();
        }
        
        return instance;
    }
    
    //============================================================================
    // Listeners and actions.
    //============================================================================
    
    /**
     * 
     */
    private class ResizeListener implements ComponentListener {
        
        @Override
        public void componentResized(ComponentEvent evt) {
            renderSVGFile(currentImageContent);
        }
        
        
        @Override
        public void componentHidden(ComponentEvent evt) {}
        
        @Override
        public void componentShown(ComponentEvent evt) {}
        
        @Override
        public void componentMoved(ComponentEvent evt) {}
    }
    
    /**
      * A swing action to reset the rendering transform of the canvas.
      */
     public class ResetTransformAction extends AbstractAction {
         @Override
         public void actionPerformed(ActionEvent evt) {
             canvas.setFragmentIdentifier(null);
             canvas.resetRenderingTransform();
             currentDataObject.getCurrentAT().setToIdentity();
         }
     }
 
     /**
      * A swing action to append an affine transform to the current
      * rendering transform. Before the rendering transform is
      * applied the method translates the center of the display to
      * 0,0 so scale and rotate occur around the middle of
      * the display.
      */
     public class AffineAction extends AbstractAction {
         
         private AffineTransform at;
         
         public AffineAction(AffineTransform at) {
             this.at = at;
         }
 
         @Override
         public void actionPerformed(ActionEvent evt) {
//             if (canvas.getgvtRoot == null) {
//                 return;
//             }
             AffineTransform rat = canvas.getRenderingTransform();
             if (at != null) {
                 Dimension dim = canvas.getSize();
                 int x = dim.width / 2;
                 int y = dim.height / 2;
                 AffineTransform t = AffineTransform.getTranslateInstance(x, y);
                 t.concatenate(at);
                 t.translate(-x, -y);
                 t.concatenate(rat);
                 canvas.setRenderingTransform(t);
                 if(SVGImagePreviewPanel.this.currentDataObject != null ) {
                     SVGImagePreviewPanel.this.currentDataObject.setCurrentAT(t);
                 }
             }            
         }
     }

    /**
     * A swing action to apply a zoom factor to the canvas. This can be used to
     * zoom in (scale &gt; 1) and out (scale &lt; 1).
     */
     public class ZoomAction extends AffineAction {
         public ZoomAction(double scale) {
             super(AffineTransform.getScaleInstance(scale, scale));
         }
         public ZoomAction(double scaleX, double scaleY) {
             super(AffineTransform.getScaleInstance(scaleX, scaleY));
         }
     }
 
     /**
      * A swing action to zoom in the canvas.
      */
     public class ZoomInAction extends ZoomAction {
         ZoomInAction() { super(1/ZOOM_OUT_FACTOR); }
     }
 
     /**
      * A swing action to zoom out the canvas.
      */
     public class ZoomOutAction extends ZoomAction {
         ZoomOutAction() { super(ZOOM_OUT_FACTOR); }
     }
 
     /**
      * A swing action to Rotate the canvas.
      */
     public class RotateAction extends AffineAction {
         public RotateAction(double theta) {
             super(AffineTransform.getRotateInstance(theta));
         }
     }
    
     /**
      * http://stackoverflow.com/questions/2885173/java-how-to-create-and-write-to-a-file
      * http://www.coderanch.com/t/478152/java/java/Path-Temporary-Directory
      * http://stackoverflow.com/questions/10967451/open-a-link-in-browser-with-java-button
      */
     public class OpenInBrowserAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
             try {
                 Path tmpFile = Files.createTempFile("", ".svg");
                 try (BufferedWriter bw = Files.newBufferedWriter(tmpFile, UTF_8, TRUNCATE_EXISTING)) {
                     bw.write(currentImageContent);
                 }

                 Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;

                 if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                     try {
                         desktop.browse(tmpFile.toUri());
                     } catch (IOException e) {
                         logger.log(Level.SEVERE, e.getLocalizedMessage());
                     }
                 }
             } catch (IOException ex) {
                 logger.log(Level.SEVERE, ex.getLocalizedMessage());// TODO: Add a user notification
             }
        }
         
     }
     
     public class RenderWithTransformGVTTreeRendererListener implements GVTTreeRendererListener{

            @Override
            public void gvtRenderingPrepare(GVTTreeRendererEvent gvttre) {}

            @Override
            public void gvtRenderingStarted(GVTTreeRendererEvent gvttre) {}

            @Override
            public void gvtRenderingCompleted(GVTTreeRendererEvent gvttre) {
                if (currentDataObject.getCurrentAT() != null) {
                    canvas.setRenderingTransform(currentDataObject.getCurrentAT());                    
                }
                
                /** 
                 * Remove the listener so that it doesn't affect the normal
                 * zoom, rotate and reset transforms.
                 */
                canvas.removeGVTTreeRendererListener(this);
            }

            @Override
            public void gvtRenderingCancelled(GVTTreeRendererEvent gvttre) {}

            @Override
            public void gvtRenderingFailed(GVTTreeRendererEvent gvttre) {}
            
        }
    
     
     public ZoomInAction getZoomInActionInstance() {
         return new ZoomInAction();
     }
     
     public ZoomOutAction getZoomOutActionInstance() {
         return new ZoomOutAction();
     }
     
     /**
      * CCW = Counter Clockwise
      * @return RotateAction
      */
     public RotateAction getCCWRotateActionInstance() {
         return new RotateAction(parseRotateFactor("-" + ROTATE_FACTOR));
     }
     
     /**
      * CW = Clockwise
      * 
      * @return RotateAction
      */
     public RotateAction getCWRotateActionInstance() {
         return new RotateAction(parseRotateFactor(ROTATE_FACTOR));
     }
     
     private double parseRotateFactor(String radians) {
        double d = 0.0d;
        try {
            d = NumberFormat.getInstance(Locale.US).parse(radians).doubleValue();
        } catch (ParseException ex) {
            logger.log(Level.INFO, "Cannot parse the rotate factor in SVGImagePreviewPanel.getRotateActionInstance");
        } 
        return d;
     }
     
     public ResetTransformAction getResetTransformAction() {
         return new ResetTransformAction();
     }
     
     public OpenInBrowserAction getOpenInBrowserAction() {
         return new OpenInBrowserAction();
     }
}
