# GridExtensionPack Add-on for Vaadin 7.5

GridExtensionPack is a collection of more or less useful extensions for tweaking 
Grid UX.

## Online demo

Online demos for extensions are not available right now.

## Download release

Official releases of this add-on are available at Vaadin Directory. For Maven 
instructions, download and reviews, go to http://vaadin.com/addon/GridExtensionPack

## Building and running demo

- git clone https://github.com/tsuoanttila/GridExtensionPack.git
- mvn clean install
- cd GridExtensionPack-demo
- mvn jetty:run

To see the demo, navigate to http://localhost:8080/

## Development with Eclipse IDE

For further development of this add-on, the following tool-chain is recommended:
- Eclipse IDE
- m2e wtp plug-in (install it from Eclipse Marketplace)
- Vaadin Eclipse plug-in (install it from Eclipse Marketplace)
- Chrome browser

### Importing project

Choose File > Import... > Existing Maven Projects

Note that Eclipse may give "Plugin execution not covered by lifecycle 
configuration" errors for pom.xml. Use "Permanently mark goal resources in 
pom.xml as ignored in Eclipse build" quick-fix to mark these errors as 
permanently ignored in your project. Do not worry, the project still works fine. 

## Release notes

### Version 1.0
- Server-side ContextClickEvent
- Table-like client-side selection UX
- PagedContainer

## Roadmap

This component is developed as a hobby with no public roadmap or any guarantees 
of upcoming releases. That said, the following features are planned for upcoming 
releases:
- Custom server-side extension for Grid SidebarMenu items

## Issue tracking

The issues for this add-on are tracked on its GitHub page. All bug reports and feature requests are appreciated. 

## Contributions

Contributions are welcome, but there are no guarantees that they are accepted as 
such. Process for contributing is the following:
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
new ContextMenuExtension(grid).addContextClickListener(/* Insert 
context click listener here */);

For a more comprehensive example, see DemoUI.java in GridExtensionPack-demo

## Features

### ContextMenuExtension

ContextMenuExtension catches the context menu events targeting the body 
of Grid, and sends events similar to ItemClickEvent.

This extension can be relatively easily made to work with ContextMenu 
addon for displaying a custom context menu for the Grid.

### TableSelectionModeExtension

TableSelectionModeExtension gives you client-side selection UX similar 
to Table. It supports Multiple selection in simple mode and with ctrl + 
click.

### PagedContainer

A simple Container wrap on top of any indexed container. Works through 
paging and provides its own PagingControls for a cleaner API.

This also works with Table, but I don't intend to track any possible 
issues with it.

## API

No online JavaDoc available (for now at least).
