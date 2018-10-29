[![Published on Vaadin  Directory](https://img.shields.io/badge/Vaadin%20Directory-published-00b4f0.svg)](https://vaadin.com/directory/component/gridextensionpack-add-on)
[![Stars on Vaadin Directory](https://img.shields.io/vaadin-directory/star/gridextensionpack-add-on.svg)](https://vaadin.com/directory/component/gridextensionpack-add-on)

# GridExtensionPack Add-on for Vaadin

GridExtensionPack is a collection of more or less useful extensions for tweaking 
Grid UX.

## Online demo

Online demos for extensions are not available right now.

## Download release

Official releases of this add-on are available at Vaadin Directory. For Maven 
instructions, download and reviews, go to https://vaadin.com/directory/component/gridextensionpack-add-on

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

### Version 2.0.0
- Vaadin 8 support
- Most old features ported for the Vaadin 8 version
- Requires the use of SelectGrid instead of Grid to allow TableSelectionModel
- Removed features available in the Framework itself

### Version 1.1.0
- CacheStrategyExtension has been added
- Many previously private methods are now protected
- A bug with filtering in PagedContainer has been fixed
- New version of header wrapping extension
- Header wrapping can now be used with alternative header row heights with other
  themes than Valo
- Header wrapping is now compatible with resizable columns

### Version 1.0.0
- Based on Vaadin 7.6
- TableSelectionModel instead of a extension hack
- TableSelectionModel now has simple Shift selection logic
- GridRefresher for forcing redraw an item
- Removed ContextClickExtension

### Version 0.2.4
- ContextClick and SidebarMenu bug fixes
- Header wrapping extension

### Version 0.2.3
- Bug fixes

### Version 0.2.2
- Server-side ContextClickEvent
- Table-like client-side selection UX
- PagedContainer
- SidebarMenuExtension

## Roadmap

This component is developed as a hobby with no public roadmap or any guarantees 
of upcoming releases. That said, the following features are planned for upcoming 
releases:
- Not much for now

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

SidebarMenuExtension is written by Anna Koskinen

# Developer Guide

## Getting started

Every extension has it's own API that you can read from the classes. TableSelectionModel is
(as the name indicates) a SelectionModel. Use it like this
```java
TableSelectionModel model = new TableSelectionModel();
model.setMode(TableSelectionMode.SIMPLE);
grid.setSelectionModel(model);
```

Using the PagedDataProvider:

```java
// This is your existing data
PagedDataProvider<?> dataProvider = new PagedDataProvider<>(
				DataProvider.ofCollection(data));
Grid grid = new Grid();
grid.setDataProvider(dataProvider);

// PagedDataProvider has a helper class for page manipulation
PagingControls controls = container.setGrid(grid);

// Set grid to certain size
controls.setPageLength(5);

// Jump to fourth page (0-based indexing)
controls.setPage(3);

// Jump to next (fifth) page
controls.nextPage();
```

For a more comprehensive example, see DemoUI.java in GridExtensionPack-demo

## Features

### TableSelectionModel

TableSelectionModel gives you client-side selection UX similar 
to Table. It supports Multiple selection in simple mode and with ctrl + 
click.

### PagedDataProvider

A simple data provider wrap. Works through paging and provides its own PagingControls for a cleaner API.

This does not work with Table.

### PagedContainer (Vaadin 7.x only)

A simple Container wrap on top of any indexed container. Works through 
paging and provides its own PagingControls for a cleaner API.

This also works with Table, but I don't intend to track any possible 
issues with it.

### SidebarMenuExtension

This extension provides a way to add custom items in the Grids 
SidebarMenu. Setting styles and custom captions along with running 
certain code when the menu item is clicked, this is the easiest way to 
get your special actions in to said menu.

### WrappingGridExtension

This extension makes possible to wrap text in headers. Sometimes you
need long header description in columns where the data content is short
(e.g. number). With this extension you can enable wrapping text in
header row and it auto adjusts the header row height accordingly.  

### GridRefresher

A simple helper extension that gives you an API that you can use to
force the repaint of a row.

### CacheStrategyExtension

This extensions provides an easy way to configure a custom cache strategy
for Grid client-side. It uses an int value and a double value with a simple
equation to count the limits. See class JavaDoc for example values

## API

No online JavaDoc available (for now at least).
