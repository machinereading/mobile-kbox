# Mobile K-Box

## About
This module provides an RDF triple store working on Android mobile devices, which has the capability of SPARQL querying, synchronizing from external triples stores, and populating from natural language texts.

## Environmental settings
* `Ubuntu 16.04 LTS`
* `Python 3`

## External dependencies

## How to run
1. Clone this repository on your server.

2. Install dependencies by executing `sh install-dependency.sh`.

3. Download a knowledge base (KB) from `http://qamel.kaist.ac.kr/resource/KB.zip`, and locate all the files contained in `KB.zip` under the folder `data/KB`.

4. Set IP address and port number of your server by editting `service-address.json`.

5. Finally, start REST API of L2K by executing `sh start-service.sh`.

## How to use
You can use REST API of L2K by a cURL command. The sample command is as follows:

- sample cURL command: `curl -d '{"date": "2018-02-18", "content": "17일 강원 강릉 아이스아레나에서 열린 2018평창동계올림픽 쇼트트랙 여자 1,500m 결승전에서 최민정이 금메달을 획득하며 환호하고 있다."}'  http://(IP-address):(port-number)/service`

- sample output: 
```
[
    [
        "http://ko.dbpedia.org/resource/2018년_동계_올림픽",
        "http://dbpedia.org/ontology/date",
        "2018-02-17",
        "0.9473046064376831"
    ],
    [
        "최민정",
        "http://dbpedia.org/property/record",
        "http://ko.dbpedia.org/resource/금메달",
        "0.8844758868217468"
    ],
    [
        "2018_평창_동계올림픽_쇼트트랙_여자_1,500m_결승_A",
        "http://dbpedia.org/ontology/date",
        "2018-02-17",
        "0.9989437460899353"
    ],
    [
        "2018_평창_동계올림픽_쇼트트랙_여자_1,500m_결승_A",
        "http://www.bbc.co.uk/ontologies/sport/hasCompetitor",
        "최민정",
        "0.9996953010559082"
    ],
    [
        "최민정",
        "http://www.bbc.co.uk/ontologies/sport/competesIn",
        "2018_평창_동계올림픽_쇼트트랙_여자_1,500m_결승_A",
        "0.5068147778511047"
    ],
    [
        "최민정",
        "http://dbpedia.org/property/record",
        "2018",
        "0.546393632888794"
    ]
]
```

## Licenses
* `CC BY-NC-SA` [Attribution-NonCommercial-ShareAlike](https://creativecommons.org/licenses/by-nc-sa/2.0/)
* If you want to commercialize this resource, [please contact to us](http://mrlab.kaist.ac.kr/contact)

## Maintainer
Jiseong Kim `jiseong@kaist.ac.kr`

## Publisher
[Machine Reading Lab](http://mrlab.kaist.ac.kr/) @ KAIST

## Acknowledgement
This research was financially supported by the Ministry of Trade, Industry and Energy(MOTIE) and Korea Institute for Advancement of Technology(KIAT) through the International Cooperative R&D program.
