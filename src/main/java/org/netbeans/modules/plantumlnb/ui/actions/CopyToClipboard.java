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

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.swing.ImageSelection;
import org.netbeans.modules.plantumlnb.pumlDataObject;
import org.netbeans.modules.plantumlnb.ui.pumlVisualElement;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author markiewb
 */
@ActionID(
        category = "PlantUML",
        id = "org.netbeans.modules.plantumlnb.ui.actions.CopyToClipboard"
)
@ActionRegistration(
        displayName = "#CTL_CopyToClipboard"
)
@ActionReferences({
    @ActionReference(path = "Actions/PlantUML/ImageView/Popupmenu", position = 0),
    @ActionReference(path = "Editors/text/x-puml/Popup", position = 300)
})
@Messages("CTL_CopyToClipboard=Copy image to clipboard")
public final class CopyToClipboard implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        pumlDataObject dataObject = pumlVisualElement.getActivePUMLEditorDataObject();
        if (dataObject != null) {

            ByteArrayOutputStream os = null;
            ByteArrayInputStream is = null;
            Image image = null;
            try {
                SourceStringReader reader = new SourceStringReader(dataObject.getPrimaryFile().asText());

                os = new ByteArrayOutputStream();
                reader.outputImage(os, new FileFormatOption(FileFormat.PNG));
                is = new ByteArrayInputStream(os.toByteArray());
                image = ImageIO.read(is);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            } finally {
                closeStream(is);
                closeStream(os);
            }
            if (null != image) {
                clipboard.setContents(new ImageSelection(image), null);
            }
        }

    }

    private void closeStream(InputStream stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    private void closeStream(OutputStream stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
