# PlantUML NB

Netbeans plugin that wraps PlantUML to provide its functionality from within Netbeans.

### Version 1.13

    * Feature: Update to latest version of batik (1.17)
    * Feature: Update to latest version of PlantUML (1.2023.12)
    * Bugfix:  Add missing dependency on ant (prevented diagram generation to work)

### Version 1.12

    * Feature: Update to latest version of PlantUML (1.2023.9)
    * Bugfix:  Update Batik dependency to remove Xalan
    * Bugfix:  Ensure SVG generated on syntax error can be rendered without
               a message dialog opening
    * Bugfix:  Remove unnecessary ant dependency

### Version 1.11

### Version 1.10

    * Bugfix: Image export creates defect files
    * Bugfix: Encoding handling is buggy and not integrated into the NetBeans
              infrastructure
    * Bugfix: Open in Browser does not work
    * Bugfix: Selecting text in preview window raises NoSuchMethodError
    * Bugfix: Invoking "PlantUML from existing java sources" raises exception
    * Bugfix: Scaling with right mouse click is not correctly ended
    * Bugfix: Prevent NullPointerException from preview window
    * Feature: Update bundled PlantUML (2021.3)
    * Feature: Remove need for project for creating PlantUML diagrams
    * Feature: Ensure project can be build on recent netbeans and JDK versions.
    * Feature: Remove lombok usage
    * Feature: Cleanup code generation from the javcc grammar

### Version 1.9

    * latest version of PlantUML (2018.8).
    * PlantUML Version 2018.8 supports stdlib icons.See [plantuml stdlib](http://plantuml.com/stdlib).

### Version 1.8

    * latest version of PlantUML (2018.3).

### Version 1.7

    * latest version of PlantUML (2017.18).

### Version 1.6

This version of PlantUML-NB includes

    * latest version (1.4.0) of PlantUML-dependency.
    * latest version of PlantUML (2017.11).
    * a new wizard to create PlantUML from existing java sources. 
    * a new encoding drop down in options panel.

### Version 1.5

* Upgraded plantuml to 8015 from 7985.

### Version 1.4

Most of the changes being released this time were contributed by Benno Markiewicz.So, many thanks to him. 
( http://netbeans.dzone.com/meet-netbeans-contributor-benno-markiewicz )

    ### What's new

        * You can now select the UML preview window and Ctrl-C to copy the image to clipboard.
        * Export image action is now available as a context menu on the file. 
        * Options window has been added that now allows specification of a dot.exe file.
        * Charset has been hardcoded to UTF-8 for now.     
    

### Version 1.3

    ### What's new

        * Fixed mouse event handlers. You can now use mouse and keyboard combination to perform zoo / rotate / pan.
        * Improved Lexer grammer.
        * Added a help hover button below to show the keyboard shortcuts.
        * Added code template support..
        * Showing document name on Preview panel titlebar.
        * Upgraded plantuml version to 7985.
        * Add support for PlantUML Dependency. Users can now create plantuml files from existing java files. Go to New File >

    ### Known Issues

    * Scrollbar on the canvas is not working properly.
        - WorkAround: Use pan in the mean time.

    ### NOTE

    * I have not researched into all possible code templates. If you, as a plantuml user, find code snippets that could be included inside the plugin, please share these with me via a sourceforge issue, so that everybody can benefit from your contribution.


### Version 1.2.1
    * Fixed the zoom persistence issue.

### Version 1.2.0
    * Added zoom in / out, pan and rotate support.
        - Zoom ( Ctrl + Left Click )
        - Realize zoom ( Shift + Right Click  )
        - Pan ( SHIFT + Left Click )
        - Rotate ( CTRL + Right Click ) 
        - Reset ( CTRL + SHIFT + Right Click )
    * Added toolbar buttons for zoom-in, zoom-out, reset, rotate.
    * Added open in browser toolbar button.

### Version 1.1.0

Added saveas button, dialog. 
    * Save as png 
    * Save as svg
    * Save as eps

### Version 1.0.0

    * create a PUML file
    * Save a PUML file
    * Syntax Highlighting for Keywords, connectors, types, parts identifiers 
