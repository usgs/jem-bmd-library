# JEM BMD Reader Library
Joint Ecosystem Modeling (JEM)  
http://jem.usgs.gov  
JEM is a partnership among federal agencies (USGS/USFWS/NPS/USACE), universities and other organizations.  

[![Build Status](https://travis-ci.org/usgs/jem-bmd-library.svg?branch=master)](https://travis-ci.org/usgs/jem-bmd-library)

## DISCLAIMER
This Software and any support from the JEM Community are provided
"AS IS" and without warranty, express or implied. JEM specifically
disclaim any implied warranties of merchantability for a particular
purpose. In no event will JEM be liable for any damages, including
but not limited to any lost profits, lost savings or any incidental
or consequential damages, whether resulting from impaired or
lost data, software or computer failure or any other cause, or
for any other claim by the user or for any third party claim.
Although this program has been used by the USGS, no warranty,
expressed or implied, is made by the USGS or the United States
Government as to the accuracy and functioning of the program
and related program material nor shall the fact of distribution
constitute any such warranty, and no responsibility is assumed
by the USGS in connection therewith.

## Build
To compile this application, ensure you have the Java SDK (JDK) installed and Maven. 
Use the Maven tool in the root of this project:
 * http://www.oracle.com/technetwork/java/javase/downloads/index.html
 * https://maven.apache.org/

This library can be compiled as JAR file with the following command:

	$ mvn install -P packaging-jar

JavaDocs can be generated with the following command:

	$ mvn javadoc:javadoc -P packaging-jar

## Usage
See examples/gov.usgs.jem.binarymodelingdata.example.Main