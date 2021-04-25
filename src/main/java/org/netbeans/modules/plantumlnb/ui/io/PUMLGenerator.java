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
package org.netbeans.modules.plantumlnb.ui.io;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.security.SFile;
import org.netbeans.api.queries.FileEncodingQuery;
import org.netbeans.modules.plantumlnb.PrettyPrinter;
import org.netbeans.modules.plantumlnb.ui.options.PlantUMLPanel;

import static org.netbeans.modules.plantumlnb.ui.options.PlantUMLPanel.DOT_MANUAL_MODE_DOT_PATH;

import org.openide.filesystems.FileObject;
import org.openide.util.NbPreferences;

/**
 *
 * @author venkat
 * @author markiewb (refactoring)
 */
public class PUMLGenerator {

    private static final Logger logger = Logger.getLogger(PUMLGenerator.class.getName());
    private static PUMLGenerator INSTANCE;

    public static PUMLGenerator getInstance() {
        if (null == INSTANCE) {

            INSTANCE = new PUMLGenerator();
        }
        return INSTANCE;
    }

    private PUMLGenerator() {
    }

    private void generateImage(FileObject inputFile, OutputStream os, FileFormat fileFormat) throws IOException {
        final boolean manual = NbPreferences.forModule(PlantUMLPanel.class).getBoolean(PlantUMLPanel.DOT_MANUAL_MODE, false);
        if (manual) {
            String path = NbPreferences.forModule(PlantUMLPanel.class).get(DOT_MANUAL_MODE_DOT_PATH, "");
            System.setProperty("GRAPHVIZ_DOT", path);
        } else {
            System.clearProperty("GRAPHVIZ_DOT");
        }

        Charset charset = FileEncodingQuery.getEncoding(inputFile);
        SourceStringReader reader = new SourceStringReader(
            Defines.createEmpty(),
            inputFile.asText(charset.name()),
            charset.name(),
            Collections.<String>emptyList(),
            new SFile(inputFile.getParent().getPath()));
        // Write the first image to "os"
        reader.outputImage(os, new FileFormatOption(fileFormat));

    }

    public String generateSvgString(FileObject inputFile) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();) {
            generateImage(inputFile, os, FileFormat.SVG);
            // It was observed, that PlantUML returns SVG encoded as UTF-8
            // as this is also the XML default, it is assumed, that this is
            // always the case
            return os.toString("UTF-8");
        } catch (IOException ex) {
            logger.log(Level.WARNING, ex.getMessage());
        }
        return null;
    }

    public void generateIntoFile(FileObject inputFile, File outputFile, FileFormat fileFormat) {
        if (fileFormat == FileFormat.SVG) {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    FileOutputStream fos = new FileOutputStream(outputFile, false);
                    BufferedOutputStream bw = new BufferedOutputStream(fos)) {
                generateImage(inputFile, baos, FileFormat.SVG);
                bw.write(PrettyPrinter.formatXml(baos.toByteArray()));
                bw.flush();
            } catch (IOException ex) {
                logger.log(Level.WARNING, ex.getMessage());
            }
        } else {
            try (FileOutputStream fos = new FileOutputStream(outputFile, false);
                    BufferedOutputStream bw = new BufferedOutputStream(fos)) {
                generateImage(inputFile, bw, fileFormat);
                bw.flush();
            } catch (IOException ex) {
                logger.log(Level.WARNING, ex.getMessage());
            }
        }
    }

}
