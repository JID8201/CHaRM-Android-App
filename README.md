# **CHaRM-Android-App**

This application was developed for Georgia Tech's Junior Design program.

The web application that accompanies this mobile application can be found [here](https://github.com/JID8201/CHaRM-Web-App)

Development was done by
- [Lucas Maurer](lmaurer9@gatech.edu)
- [Olivia Powell](opowell6@gatech.edu)
- [Jonathon Humphries](jhumphries30@gatech.edu)
- [Ishtyaq Ponir](iponir3@gatech.edu)
- [Bowen Ran](bran3@gatech.edu)
- [Quynh Nhu Nguyen](qnguyen47@gatech.edu)

This project was sponsored by [Live Thrive](http://livethrive.org/) and Peggy Whitlow Ratcliffe

# *Release Notes*

## New Features
- Users can now submit multiple recycle orders at once
- Changed the donate button to link to Live Thrive site
- Verified checked UI is fluid and easy to use

## Bug Fixes
- Fixed bug that prevented submitting recycling orders

## Known Bugs
- The App icon on devices sometimes shows up as the Orange CHaRM logo and sometimes shows up as the default android logo

# *Install Guide*

## Pre-requisites
- [Android Studio 3+](https://developer.android.com/studio/)
- Device with Android 5+

## Dependencies
- Gradle 3.2+
- Google-services 4.1+

## Download Instructions

The mobile app code can be downloaded:
- as a zip file by clicking [here](https://github.com/JID8201/CHaRM-Android-App/archive/master.zip)

or 

- by cloning this repository by downloading [git](https://git-scm.com/downloads) and running the following command in a terminal
```
git clone git@github.com:JID8201/CHaRM-Android-App.git
```

## Build Instructions

**Pre-Note** : If you downloaded the zip file of the code, unzip the file and place the code where you wish it to go.

To open the CHaRM-Android-App project in Android studio follow the instructions found [here](https://github.com/dogriffiths/HeadFirstAndroid/wiki/How-to-open-a-project-in-Android-Studio)

Once you have opened the CHaRM android app in Android Studio, press the little green hammer button found in the top right corner of the screen

This should build the Mobile application for you

See the Troubleshooting section bellow if you experience any problems with building the mobile application.

## Installation Instructions

- To install the CHaRM Android App as a user, visit the google play store and search CHaRM Android App or click the link [here]
- To install the CHaRM Android App as a developer:

  1. Set your development device to developer mode by following the instructions [here](https://developer.android.com/studio/debug/dev-options)
  2. Once you've set your device to developer mode, plug the phone or tablet into your development computer using a phone to USB cable
  3. Once this is done, press the green sideways triangle in the top right-hand corner of your Android Studio Window (this is the same window you used to build the Android App previously)

## Run Instructions
Once the CHaRM App is installed, all you need to do to run it is and open it (should be named CHaRM).

## Troubleshooting
- If you get an error when submitting a recycling order, you have internet/cell signal, and the live website is up and running, then the problem likely is in the web app backend so go check there first.
- LOG statements can be used for dynamic debugging. [Here](https://developer.android.com/reference/android/util/Log) is a helpful link to inform you of how to use LOG statements if you need it.
- Android studio provides static debugging, in case you've spelled a variable name wrong or forgot to close a set of braces. Use it!


