# Mobile K-Box

## About
This page provides an RDF triple store for Android mobile devices, called mobile K-Box, which has the capability of querying by SPARQL query language, synchronizing from external triple stores, and populating from external documents written in Korean.

## Environmental settings
* `Ubuntu 16.04 LTS`
* `Java Development Kit 8`
* `Android Studio 3.1.4`

## External dependencies
* This module is implemented on the top of RDF4A, which is an RDF triple store working on Android OS. Please go to https://github.com/dice-group/qamel.git and install it.

## How to use
1. Open your Android Studio editor, and load the Android project `mobile_kbox`.
2. You can access mobile K-Box by referring to `GlobalVariable.kbox`.
3. For example, if you want to query on mobile K-Box, just call `GlobalVariable.kbox.sparqlQuery("your SPARQL query")`.
4. Other APIs are also usable, e.g., `kbox.syncDb(...)`, `kbox.populateDb(...)`, and so on.
5. If you want to know more details about APIs, please the documentation of mobile K-Box here: https://docs.google.com/document/d/1frKds01a8duU6OgHzpOtNCKpai-fMeojO7NZB_LB0Ls/edit?usp=sharing

## Licenses
* `CC BY-NC-SA` [Attribution-NonCommercial-ShareAlike](https://creativecommons.org/licenses/by-nc-sa/2.0/)
* If you want to commercialize this resource, [please contact to us](http://mrlab.kaist.ac.kr/contact)

## Maintainer
Jiseong Kim `jiseong@kaist.ac.kr`

## Publisher
[Machine Reading Lab](http://mrlab.kaist.ac.kr/) @ KAIST

## Acknowledgement
This research was financially supported by the Ministry of Trade, Industry and Energy(MOTIE) and Korea Institute for Advancement of Technology(KIAT) through the International Cooperative R&D program.
