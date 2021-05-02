/* 
 * The MIT License
 *
 * Copyright 2017 Venkat Ram Akkineni.
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
package org.netbeans.modules.plantumlnb.generate;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.plantumldependency.commoncli.exception.CommandLineException;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Venkat Ram Akkineni
 */
//@ActionID(
//        category = "Build",
//        id = "org.netbeans.modules.plantumlnb.GeneratePlantUMLAction")
//@ActionRegistration( displayName = "#CTL_OnJavaPackageAction")
//@ActionReferences({
//    @ActionReference(path = "Projects/package/Actions", position = 1400)
//})
@Messages("CTL_OnJavaPackageAction=Generate PlantUML")
public final class GeneratePlantUMLAction implements ActionListener {
    
    private final List<DataFolder> context;
    private static final Logger LOG = Logger.getLogger(GeneratePlantUMLAction.class.getName());
 
    public GeneratePlantUMLAction(List<DataFolder> context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        ArrayList<File> javaFiles = new ArrayList<>();
        
        for(ListIterator contextIter = context.listIterator(); contextIter.hasNext();){
            DataFolder dataFolder = (DataFolder) contextIter.next();

            File dir = FileUtil.toFile(dataFolder.getPrimaryFile());
            
//            File[] javaFiles = dir.listFiles(new FilenameFilter() { 
//                
//    	        @Override 
//                public boolean accept(File dir, String filename) { 
//                     return filename.endsWith(".java"); 
//                 }
//            });
//            
//            
//            
            javaFiles.addAll(list(dir));
            try {
                PlantUMLDependencyService.generate(dir, new File("/tmp/" + dir.getName() + ".puml"));
            } catch(MalformedURLException | CommandLineException | ParseException ex) {
                LOG.log(Level.INFO, ex.getLocalizedMessage());
            }
        }   
        LOG.log(Level.INFO, "Acting already ...");
    }
    
    public ArrayList<File> list(File file) {
        File[] children = file.listFiles();
        ArrayList<File> javaFiles = new ArrayList<>();        
        /**
         * If children is not null then 'file' is a directory, recurse.
         */
        if(children != null) {
            for (File child : children) {
                list(child);
            }
        } else {
            if(file.getName().endsWith(".java")) {
                javaFiles.add(file);                
            }
        }
        return javaFiles;
    }
    
}
