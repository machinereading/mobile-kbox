# Mobile K-Box

## About
This repository provides an RDF triple store for Android mobile devices, called mobile K-Box, which has the capability of querying by SPARQL query language, synchronizing from external RDF triple stores, and populating from external documents written in natural language, especially Korean.

This repository contains two Android Studio projects:
* `mobile_kbox`: an Android Studio project that only contains APIs for manipulating mobile K-Box. 
* `mobile_kbox_demo`: an Android Studio project that contains APIs for manipulating mobile K-Box and implementation of visual user interfaces for interacting with mobile K-Box.

## Prerequisite
* `Ubuntu 16.04 LTS`
* `Java Development Kit 8`
* `Android Studio 3.1.4`

## External dependencies
* This module is implemented on the top of RDF4A, which is an Android library for implementing an RDF triple store. Please go to the github page https://github.com/dice-group/qamel/tree/master/rdf4a and install it.

## How to use
1. Open your Android Studio editor and load the Android Studio project `mobile_kbox`.
2. After that, you can access mobile K-Box by referring to the variable `GlobalVariable.kbox`.
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
