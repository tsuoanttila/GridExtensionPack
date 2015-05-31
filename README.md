# GridExtensionPack Add-on for Vaadin 7.5

GridExtensionPack is a collection of more or less useful extensions for tweaking Grid UX.

## Online demo

Online demos for extensions are not available right now.

## Download release

Official releases of this add-on are available at Vaadin Directory. For Maven instructions, download and reviews, go to http://vaadin.com/addon/GridExtensionPack

## Building and running demo

git clone <url of the GridExtensionPack repository>
mvn clean install
cd GridExtensionPack-demo
mvn jetty:run

To see the demo, navigate to http://localhost:8080/

## Development with Eclipse IDE

For further development of this add-on, the following tool-chain is recommended:
- Eclipse IDE
- m2e wtp plug-in (install it from Eclipse Marketplace)
- Vaadin Eclipse plug-in (install it from Eclipse Marketplace)
- Chrome browser

### Importing project

Choose File > Import... > Existing Maven Projects

Note that Eclipse may give "Plugin execution not covered by lifecycle configuration" errors for pom.xml. Use "Permanently mark goal resources in pom.xml as ignored in Eclipse build" quick-fix to mark these errors as permanently ignored in your project. Do not worry, the project still works fine. 

### Debugging server-side

If you have not already compiled the widgetset, do it now by running vaadin:install Maven target for GridExtensionPack-root project.

To debug project and make code modifications on the fly in the server-side, right-click the GridExtensionPack-demo project and choose Debug As > Debug on Server. Navigate to http://localhost:8080/GridExtensionPack-demo/ to see the application.

### Debugging client-side

The most common way of debugging and making changes to the client-side code is dev-mode. To create debug configuration for it, open GridExtensionPack-demo project properties and click "Create Development Mode Launch" button on the Vaadin tab. Right-click newly added "GWT development mode for GridExtensionPack-demo.launch" and choose Debug As > Debug Configurations... Open up Classpath tab for the development mode configuration and choose User Entries. Click Advanced... and select Add Folders. Choose Java and Resources under GridExtensionPack/src/main and click ok. Now you are ready to start debugging the client-side code by clicking debug. Click Launch Default Browser button in the GWT Development Mode in the launched application. Now you can modify and breakpoints to client-side classes and see changes by reloading the web page. 

Another way of debugging client-side is superdev mode. To enable it, uncomment devModeRedirectEnabled line from the end of DemoWidgetSet.gwt.xml located under GridExtensionPack-demo resources folder and compile the widgetset once by running vaadin:compile Maven target for GridExtensionPack-demo. Refresh GridExtensionPack-demo project resources by right clicking the project and choosing Refresh. Click "Create SuperDevMode Launch" button on the Vaadin tab of the GridExtensionPack-demo project properties panel to create superder mode code server launch configuration and modify the class path as instructed above. After starting the code server by running SuperDevMode launch as Java application, you can navigate to http://localhost:8080/GridExtensionPack-demo/?superdevmode. Now all code changes you do to your client side will get compiled as soon as you reload the web page. You can also access Java-sources and set breakpoints inside Chrome if you enable source maps from inspector settings. 


## Release notes

### Version 1.0
- Server-side ContextClickEvent
- Table-like client-side selection UX
- PagedGridContainer

## Roadmap

This component is developed as a hobby with no public roadmap or any guarantees of upcoming releases. That said, the following features are planned for upcoming releases:
- Not much

## Issue tracking

The issues for this add-on are tracked on its bitbucket.com page. All bug reports and feature requests are appreciated. 

## Contributions

Contributions are welcome, but there are no guarantees that they are accepted as such. Process for contributing is the following:
- Fork this project
- Create an issue to this project about the contribution (bug or feature) if there is no such issue about it already. Try to keep the scope minimal.
- Develop and test the fix or functionality carefully. Only include minimum amount of code needed to fix the issue.
- Refer to the fixed issue in commit
- Send a pull request for the original project
- Comment on the original issue that you have implemented a fix for it

## License & Author

Add-on is distributed under Apache License 2.0. For license terms, see LICENSE.txt.

GridExtensionPack is written by Teemu Suo-Anttila

# Developer Guide

## Getting started

Here is a simple example on how to try out the add-on component:

Grid grid = new Grid();
new ContextMenuExtension(grid).addContextClickListener(/* Insert context click listener here */);

For a more comprehensive example, see src/test/java/org/vaadin/template/demo/DemoUI.java

## Features

### ContextMenuExtension

ContextMenuExtension catches the context menu events targeting the body of Grid, and sends events similar to ItemClickEvent.

This extension can be relatively easily made to work with ContextMenu addon for displaying a custom context menu for the Grid.

### TableSelectionModeExtension

TableSelectionModeExtension gives you client-side selection UX similar to Table. 
It supports Multiple selection in simple mode and with ctrl + click.

### PagedGridContainer

A simple Container extension on top of any indexed container.

## API

No online JavaDoc available (for now at least).