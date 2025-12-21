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

import java.io.File;
import java.util.StringJoiner;
import org.netbeans.modules.plantumlnb.StringUtils;

/**
 *
 * @author venkat
 */
public class PlantUMLGenerationRequest {

    private String destinationDirectory;

    private String outputFileName;

    private String sourcesDirectory;

    private String includePatterns;

    private String excludePatterns;

    // Display type options
    private boolean abstractClasses = true;

    private boolean annotations = true;

    private boolean classes = true;

    private boolean enums = true;

    private boolean extensions = true;

    private boolean implementations = true;

    private boolean imports = true;

    private boolean interfaces = true;

    private boolean nativeMethods = true;

    private boolean staticImports = true;
    // End Display type options

    private String displayNameRegex = "";

    private String displayPackageNameRegex = "";

    public PlantUMLGenerationRequest() {
    }

    public String getDisplayTypeOptionsString() {
        StringJoiner displayTypeOptionsString = new StringJoiner(",");

        if(abstractClasses) {
            displayTypeOptionsString.add(DisplayTypeOptions.abstract_classes.name());
        }

        if(annotations) {
            displayTypeOptionsString.add(DisplayTypeOptions.annotations.name());
        }

        if(classes) {
            displayTypeOptionsString.add(DisplayTypeOptions.classes.name());
        }

        if(enums) {
            displayTypeOptionsString.add(DisplayTypeOptions.enums.name());
        }

        if(extensions) {
            displayTypeOptionsString.add(DisplayTypeOptions.extensions.name());
        }

        if(implementations) {
            displayTypeOptionsString.add(DisplayTypeOptions.implementations.name());
        }

        if(imports) {
            displayTypeOptionsString.add(DisplayTypeOptions.imports.name());
        }

        if(interfaces) {
            displayTypeOptionsString.add(DisplayTypeOptions.interfaces.name());
        }

        if(nativeMethods) {
            displayTypeOptionsString.add(DisplayTypeOptions.native_methods.name());
        }

        if(staticImports) {
            displayTypeOptionsString.add(DisplayTypeOptions.static_imports.name());
        }

        return displayTypeOptionsString.toString();
    }

    public File getOutputFile() {
        if (StringUtils.isEmpty(destinationDirectory) || StringUtils.isEmpty(outputFileName)) {
            return null;
        }

        return new File(destinationDirectory + "/" + outputFileName + ".puml");
    }

    public File getSourcesDirectoryFile() {
        if (StringUtils.isEmpty(sourcesDirectory)) {
            return null;
        }

        return new File(sourcesDirectory);
    }

    public String getDestinationDirectory() {
        return destinationDirectory;
    }

    public void setDestinationDirectory(String destinationDirectory) {
        this.destinationDirectory = destinationDirectory;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public String getSourcesDirectory() {
        return sourcesDirectory;
    }

    public void setSourcesDirectory(String sourcesDirectory) {
        this.sourcesDirectory = sourcesDirectory;
    }

    public String getIncludePatterns() {
        return includePatterns;
    }

    public void setIncludePatterns(String includePatterns) {
        this.includePatterns = includePatterns;
    }

    public String getExcludePatterns() {
        return excludePatterns;
    }

    public void setExcludePatterns(String excludePatterns) {
        this.excludePatterns = excludePatterns;
    }

    public boolean isAbstractClasses() {
        return abstractClasses;
    }

    public void setAbstractClasses(boolean abstractClasses) {
        this.abstractClasses = abstractClasses;
    }

    public boolean isAnnotations() {
        return annotations;
    }

    public void setAnnotations(boolean annotations) {
        this.annotations = annotations;
    }

    public boolean isClasses() {
        return classes;
    }

    public void setClasses(boolean classes) {
        this.classes = classes;
    }

    public boolean isEnums() {
        return enums;
    }

    public void setEnums(boolean enums) {
        this.enums = enums;
    }

    public boolean isExtensions() {
        return extensions;
    }

    public void setExtensions(boolean extensions) {
        this.extensions = extensions;
    }

    public boolean isImplementations() {
        return implementations;
    }

    public void setImplementations(boolean implementations) {
        this.implementations = implementations;
    }

    public boolean isImports() {
        return imports;
    }

    public void setImports(boolean imports) {
        this.imports = imports;
    }

    public boolean isInterfaces() {
        return interfaces;
    }

    public void setInterfaces(boolean interfaces) {
        this.interfaces = interfaces;
    }

    public boolean isNativeMethods() {
        return nativeMethods;
    }

    public void setNativeMethods(boolean nativeMethods) {
        this.nativeMethods = nativeMethods;
    }

    public boolean isStaticImports() {
        return staticImports;
    }

    public void setStaticImports(boolean staticImports) {
        this.staticImports = staticImports;
    }

    public String getDisplayNameRegex() {
        return displayNameRegex;
    }

    public void setDisplayNameRegex(String displayNameRegex) {
        this.displayNameRegex = displayNameRegex;
    }

    public String getDisplayPackageNameRegex() {
        return displayPackageNameRegex;
    }

    public void setDisplayPackageNameRegex(String displayPackageNameRegex) {
        this.displayPackageNameRegex = displayPackageNameRegex;
    }
}
