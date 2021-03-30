# Sync Endpoint Web UI

This project is __*actively maintained*__

It is part of the ODK-X Android tools suite.

Sync Endpoint Web UI is the user interface module for the sync-endpoint server.

The developer [wiki](https://github.com/odk-x/tool-suite-X/wiki) (including release notes) and
[issues tracker](https://github.com/odk-x/tool-suite-X/issues) are located under
the [**ODK-X Tool Suite**](https://github.com/odk-x) project.

Engage with the community and get technical support on [the ODK-X forum](https://forum.odk-x.org)

## Prerequisites

 - Docker 17.06.1 or newer

## Build 

`docker build --pull -t odk/sync-web-ui https://github.com/opendatakit/sync-endpoint-web-ui.git`

## Run

Use [RUN.md](RUN.md).

Launch a container with the [Docker file](DOCKER.md)

See [sync-endpoint-default-setup](https://github.com/opendatakit/sync-endpoint-default-setup) for typical usage.

## Acknowledgements

This is a fork of Benetech's [ODK Hamster](https://github.com/benetech/odk-hamsterball-java)

## How to contribute
If you’re new to ODK-X you can check out the documentation:
- [https://docs.odk-x.org](https://docs.odk-x.org)

Once you’re up and running, you can choose an issue to start working on from here: 
- [https://github.com/odk-x/tool-suite-X/issues](https://github.com/odk-x/tool-suite-X/issues)

Issues tagged as [good first issue](https://github.com/odk-x/tool-suite-X/issues?q=is%3Aissue+is%3Aopen+label%3A%22good+first+issue%22) should be a good place to start.

Pull requests are welcome, though please submit them against the development branch. We prefer verbose descriptions of the change you are submitting. If you are fixing a bug please provide steps to reproduce it or a link to a an issue that provides that information. If you are submitting a new feature please provide a description of the need or a link to a forum discussion about it. 

## Links for users
This document is aimed at helping developers and technical contributors. For information on how to get started as a user of ODK-X, see our [online documentation](https://docs.odk-x.org), or to learn more about the Open Data Kit project, visit [https://odk-x.org](https://odk-x.org).
