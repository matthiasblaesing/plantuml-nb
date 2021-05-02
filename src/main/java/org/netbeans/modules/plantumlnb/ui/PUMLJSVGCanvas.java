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

import java.awt.geom.AffineTransform;
import org.apache.batik.swing.JSVGCanvas;

/**
 *
 * @author Venkat Akkineni &lt;sriguru@users.sourceforge.net&gt;
 */
public class PUMLJSVGCanvas extends JSVGCanvas {

    public static final String renderingTransformPropertyName = "renderingTransform";

    @Override
    public void setRenderingTransform(AffineTransform newAt) {
        AffineTransform oldAt = getRenderingTransform();
        super.setRenderingTransform(newAt);
        pcs.firePropertyChange(renderingTransformPropertyName, oldAt, newAt);
    }
}
