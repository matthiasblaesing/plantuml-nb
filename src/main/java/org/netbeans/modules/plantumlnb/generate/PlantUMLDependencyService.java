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
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantumldependency.cli.main.program.PlantUMLDependencyProgram;
import net.sourceforge.plantumldependency.commoncli.command.CommandLine;
import net.sourceforge.plantumldependency.commoncli.command.impl.CommandLineImpl;
import net.sourceforge.plantumldependency.commoncli.exception.CommandLineException;
import net.sourceforge.plantumldependency.commoncli.program.JavaProgram;
import net.sourceforge.plantumldependency.commoncli.program.execution.JavaProgramExecution;

/**
 *
 * @author Venkat Ram Akkineni
 */
public class PlantUMLDependencyService {


    public static void generate(File directory, File outputFile) throws MalformedURLException, CommandLineException, ParseException {
        final CommandLine commandLineArguments = new CommandLineImpl(new String[] {
            "-o", outputFile.getAbsolutePath(),
            "-b", directory.getAbsolutePath(),
            "-e", "**/package-info.java"
        });

        generate(commandLineArguments);
    }

    public static void generate(final PlantUMLGenerationRequest request) throws MalformedURLException, CommandLineException, ParseException {
        List<String> commandLineArgs = new ArrayList<>();
        commandLineArgs.addAll(Arrays.asList(
            "-o", request.getOutputFile().getAbsolutePath(),
            "-b", request.getSourcesDirectoryFile().getAbsolutePath(),
            "-dt", request.getDisplayTypeOptionsString()
        ));

        // Default "**/package-info.java" if unspecified.
        if (StringUtils.isNotEmpty(request.getExcludePatterns())) {
            commandLineArgs.addAll(Arrays.asList("-e", request.getExcludePatterns()));
        }

        // Default "**/*.java" if unspecified
        if (StringUtils.isNotEmpty(request.getIncludePatterns())) {
            commandLineArgs.addAll(Arrays.asList("-i", request.getIncludePatterns()));
        }

        if (StringUtils.isNotEmpty(request.getDisplayNameRegex())) {
            commandLineArgs.addAll(Arrays.asList("-dn", request.getDisplayNameRegex()));
        }

        if (StringUtils.isNotEmpty(request.getDisplayPackageNameRegex())) {
            commandLineArgs.addAll(Arrays.asList("-dp", request.getDisplayPackageNameRegex()));
        }

        final CommandLine commandLineArguments = new CommandLineImpl(commandLineArgs.toArray(String[]::new));

        generate(commandLineArguments);
    }

    private static void generate(final CommandLine commandLineArguments) throws MalformedURLException, CommandLineException, ParseException {

        // Creates the PlantUML Dependency program instance
        final JavaProgram plantumlDependencyProgram = new PlantUMLDependencyProgram();

        // Get the PlantUML Dependency program execution instance following the command line arguments, ready to be executed
        final JavaProgramExecution plantumlDependencyProgramExecution = plantumlDependencyProgram.parseCommandLine(commandLineArguments);

        // Executes the PlantUML Dependency program
        plantumlDependencyProgramExecution.execute();
    }
}