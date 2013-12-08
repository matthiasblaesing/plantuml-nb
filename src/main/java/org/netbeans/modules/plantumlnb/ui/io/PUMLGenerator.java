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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOException;
import javax.swing.filechooser.FileFilter;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import org.netbeans.modules.plantumlnb.PrettyPrinter;
import org.netbeans.modules.plantumlnb.ui.ImageUtils;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;

/**
 *
 * @author venkat
 */
public class PUMLGenerator {
    
    private static String genericUrl = "file://${path}/${name}.png";
    
    private HashMap<String, Method> methodMap =  new HashMap<String, Method>();
    private static final Logger logger = Logger.getLogger(PUMLGenerator.class.getName());
    
    public PUMLGenerator() {
        Class c = getClass();

        try {
            methodMap.put(ImageUtils.png, c.getMethod("generatePNGFile"));
            methodMap.put(ImageUtils.svg, c.getMethod("generateSVGFile"));
        } catch (NoSuchMethodException ex) {
            logger.log(Level.WARNING, ex.getMessage());
        } catch (SecurityException ex) {
            logger.log(Level.WARNING, ex.getMessage());
        }
        
    }

    public ByteArrayInputStream generate(FileObject fo){
        ByteArrayInputStream pumlImageInputStream = null;
        
        try {
            SourceStringReader pumlReader = new SourceStringReader(fo.asText());
//            String name = fo.getName();
//            name = "file://" + fo.getParent().getPath() + "/" + name + ".png";
//            
//            name = genericUrl.replace("${path}", fo.getParent().getPath())
//                    .replace("${name}", name);
//
            ByteArrayOutputStream pumlImageOutputStream = new ByteArrayOutputStream();

//            File pumlImageFile = new File(name);
//            // if file doesnt exists, then create it
//            pumlImageFile.mkdirs();
//            if (!pumlImageFile.exists()) {
//                pumlImageFile.createNewFile();
//            }
//                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
//                    BufferedWriter bw = new BufferedWriter(fw);
//                    bw.write(content);
//                    bw.close();
            pumlReader.generateImage(pumlImageOutputStream);
            pumlImageInputStream = new ByteArrayInputStream(pumlImageOutputStream.toByteArray());
        } catch (IOException ex) {
            logger.log(Level.WARNING, ex.getMessage());
        } 
        return pumlImageInputStream;
    }
    
    public String generateSVG(FileObject fo) {
        SourceStringReader reader;
        String svg = null;
        
        try {
            reader = new SourceStringReader(fo.asText());
            ByteArrayOutputStream os = new ByteArrayOutputStream();

            // Write the first image to "os"
            String desc = reader.generateImage(os, new FileFormatOption(FileFormat.SVG));
            svg = new String(os.toByteArray());
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        return svg;       
    }
    
    /**
     * As the name suggests reads the supplied inputStream and returns a string
     * representation of the contents of the inputStream.
     * 
     * @param is
     * @return 
     */
    public String stringify(InputStream is) {        
        InputStreamReader isr = new InputStreamReader(is);
        StringBuilder sb=new StringBuilder();
        BufferedReader br = new BufferedReader(isr);
        String read = null;
        try {
            read = br.readLine();
        } catch (IOException ex) {
            logger.log(Level.WARNING, ex.getMessage());
        } finally {
            try {
                is.close();
                isr.close();
            } catch (IOException ex) {
                logger.log(Level.WARNING, ex.getMessage());
            }            
        }

        try {
            while(read != null) {
                sb.append(read);            
                read = br.readLine();            
            }
        } catch (IOException ex) {
                logger.log(Level.WARNING, ex.getMessage());
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                logger.log(Level.WARNING, ex.getMessage());
            }
        }

        return sb.toString();
    }
    
    public void generateFile(InputStream is, FileFilter ftf) throws IIOException {
        try {
            String methodName = "generate" + ftf.getDescription().toUpperCase() + "File";
            Class c = getClass();            
            c.getMethod(methodName).invoke(is);            
        } catch (NoSuchMethodException ex) {
            logger.log(Level.WARNING, ex.getMessage());
        } catch (SecurityException ex) {
            logger.log(Level.WARNING, ex.getMessage());
        }  catch (IllegalAccessException ex) {
            logger.log(Level.WARNING, ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.log(Level.WARNING, ex.getMessage());
        } catch (InvocationTargetException ex) {
            logger.log(Level.WARNING, ex.getMessage());
        }
    }
    
    public void generateFile(FileObject fo, FileFormat fileFormat, File file) {        
        try {
            String methodName = "generate" + fileFormat.name() + "File";
            Class c = getClass();            
            c.getMethod(methodName, FileObject.class, File.class).invoke(this, fo, file);            
        } catch (NoSuchMethodException ex) {
            logger.log(Level.WARNING, ex.getMessage());
        } catch (SecurityException ex) {
            logger.log(Level.WARNING, ex.getMessage());
        }  catch (IllegalAccessException ex) {
            logger.log(Level.WARNING, ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.log(Level.WARNING, ex.getMessage());
        } catch (InvocationTargetException ex) {
            logger.log(Level.WARNING, ex.getMessage());
        }
        
    }     
            
    
    public void generatePNGFile(FileObject fo, File file) { 
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;        
        
        try {
            SourceStringReader reader = new SourceStringReader(fo.asText());
            // Write the first image to "os"
            String desc = reader.generateImage(os, new FileFormatOption(FileFormat.PNG));

            file.createNewFile();
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(os.toByteArray());

        } catch (IOException ex) {
            logger.log(Level.WARNING, ex.getMessage());
        } finally {
            try {
                os.close();
                if (fos != null) {
                    fos.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException ex) {
                logger.log(Level.WARNING, ex.getMessage());
            }
        }
    }
    
    public void generateSVGFile(FileObject fo, File file) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            SourceStringReader reader = new SourceStringReader(fo.asText());
            // Write the first image to "os"
            String desc = reader.generateImage(os, new FileFormatOption(FileFormat.SVG));
            String svg = new String(os.toByteArray());

            file.createNewFile();
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.write(PrettyPrinter.formatXml(svg));
            bw.flush();

        } catch (IOException ex) {
            logger.log(Level.WARNING, ex.getMessage());
        } finally {
            try {
                os.close();
                if (fw != null) {
                    fw.close();
                }
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException ex) {
                logger.log(Level.WARNING, ex.getMessage());
            }
        }  
    }
    
    public void generateEPSFile(FileObject fo, File file) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;        
        
        try {
            SourceStringReader reader = new SourceStringReader(fo.asText());
            // Write the first image to "os"
            String desc = reader.generateImage(os, new FileFormatOption(FileFormat.EPS));

            file.createNewFile();
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(os.toByteArray());

        } catch (IOException ex) {
            logger.log(Level.WARNING, ex.getMessage());
        } finally {
            try {
                os.close();
                if (fos != null) {
                    fos.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException ex) {
                logger.log(Level.WARNING, ex.getMessage());
            }
        }
    }
    
//    public void generatePDFFile(InputStream is) {
//        generateFile(is, FileFormat.PDF);
//    }
//    
//    public void generateHTMLFile(InputStream is) {
//        generateFile(is, FileFormat.HTML);
//    }
//    
//    public void generateHTML5File(InputStream is) {
//        generateFile(is, FileFormat.HTML5);
//    }
    
}
