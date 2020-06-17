#!/bin/bash -ex

mvn clean
mvn compile

export MAVEN_OPTS=-Dprism.order=sw;
mvn -q exec:java -Dexec.mainClass="cs1302.gallery.GalleryDriver"
