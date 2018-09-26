# Mobile K-Box

## About
This repository provides an RDF triple store for Android mobile devices, called mobile K-Box (Knowledge-Box), which has the capability of querying by SPARQL query language, synchronizing from already existing external RDF triple stores, and populating by knowlege extraction from external documents written in natural language, especially Korean.

This repository contains two Android Studio projects:
* `mobile_kbox`: an Android Studio project that only contains APIs for manipulating mobile K-Box. 
* `mobile_kbox_demo`: an Android Studio project that contains APIs for manipulating mobile K-Box and implementation of visual user interfaces for interacting between mobile K-Box and user.

The above mentioned Android Studio projects should be opened and runned by Android Studio (https://developer.android.com/studio/).

## Target Android version
Mobile K-Box has been developed by targetting the latest Android versions as follows.
* API 24, 25: Nougat
* API 26, 27: Oreo
* API 28

For real testing, you should prepare Android devices with the above mentioned Android versions.

## Prerequisite
* `Ubuntu 16.04 LTS`
* `Java Development Kit (JDK) 8`
* `Android Studio 3.1.4`

## External dependencies
* This module is implemented on the top of RDF4A, which is an Android library for manipulating an RDF triple store. Please go to the github page https://github.com/dice-group/qamel/tree/master/rdf4a and install it (Please note that before installing RDF4A, JDK8 have to be installed in advance).

## How to use
1. Open the Android Studio project `mobile_kbox` by Android Studio.
2. Initialize `GlobalVariable.kbox` at the main activity as follows: `GlobalVariable.kbox = new Kbox(this)`.
2. After that, you can access mobile K-Box from everywhere by referring to the variable `GlobalVariable.kbox`.
3. For example, if you want to query on mobile K-Box, just call the API `GlobalVariable.kbox.sparqlQuery("your SPARQL query")`.
4. There are also other APIs for manipulating mobile K-Box, e.g., `kbox.syncDb(...)`, `kbox.populateDb(...)`, and so on.
5. If you want to know more details about APIs of mobile K-Box, please refer to the documentation of mobile K-Box: https://docs.google.com/document/d/1frKds01a8duU6OgHzpOtNCKpai-fMeojO7NZB_LB0Ls/edit?usp=sharing.

## Licenses
* `CC BY-NC-SA` [Attribution-NonCommercial-ShareAlike](https://creativecommons.org/licenses/by-nc-sa/2.0/)
* If you want to commercialize this resource, [please contact to us](http://mrlab.kaist.ac.kr/contact)

## Maintainer
Jiseong Kim `jiseong@kaist.ac.kr`

## Publisher
[Machine Reading Lab](http://mrlab.kaist.ac.kr/) @ KAIST

## Acknowledgement
This research was financially supported by the Ministry of Trade, Industry and Energy(MOTIE) and Korea Institute for Advancement of Technology(KIAT) through the International Cooperative R&D program.
