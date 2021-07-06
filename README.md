# MIT App Inventor Extension for Ethereum: QR code display

This repository is the java source code used to build a QR code display extension

This repo is using the extension template from 
https://github.com/mit-cml/extension-template


## to rebuild

You will need:

* java 1.8 (either OpenJDK or Oracle)
  * Do not use Java 8 or newer features as our build system does not
    yet support this.
* ant 1.10 or higher
* git 2.3.10 or higher

After cloning this repository, use the following command:

```shell
git submodule init
git submodule update
```

Then build using ant

## usage

This extension goes along with the Ethereum extension and allows
to display QR codes
This extension is already included in the the Ethereum  squeleton application

## notes

It is very possible that appinventor is able to display
QR code without this extension, but it did not occur to us
how to do it.
