# Windesheim Codeparser IntelliJ Plugin

JetBrains IntelliJ plugin dependant on the Windesheim Codeparser project to show the found design patterns inside the IDE.

[![Build Status](https://travis-ci.org/Taronyuu/WindesheimTechnologieProjectPlugin.svg?branch=master)](https://travis-ci.org/Taronyuu/WindesheimTechnologieProjectPlugin)

## Installation
To install the plugin you'll have to find the latest resource and download the .zip file. After downloading this open your IntelliJ IDEA editor and install a new plugin from disk by clicking `IntelliJ IDEA -> Preferences -> Plugins -> Install plugin from disk`. 
Now search for the recently downloaded .zip file from the latest release and install. 
Your IDE will ask to if you want to restart itself so it can load the extension.

To build the plugin from source, see below.

## Usage
By default the extension will be loaded in a ToolWindow on the right sight. 
Ofcourse you are free to move the extension to any side of your window or resize it how you want.

If you open a project in your IDE the extension will automatically search for all the used design patterns and show these to you.
If you ever want to refresh your patterns without restarting your IDE, just click the `refresh` button. 
The 'Updated at' text at the bottom will update its timestamp to show you an update has been done.

## Dependencies
This plugin is dependant on the [Windhesheim Project Parser Repository](https://raw.githubusercontent.com/Taronyuu/WindesheimTechnologieProject/master/README.md) for its functionality.

Furthermore for building from source the plugin is dependant on the following plugins:
- Checkstyle: Used to enforce several programming rules.
- PMD: Used to keep an eye on the cyclomatic complexity.
- IntelliJ: Used to run a sandboxed version of the IDE.

