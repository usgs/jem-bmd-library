JEM BMD Reader Library
Joint Ecosystem Modeling (JEM)
http://jem.usgs.gov
JEM is a partnership among federal agencies (USGS/USFWS/NPS/USACE), universities and other organizations.

DISCLAIMER
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

To compile this application, ensure you have the Java SDK (JDK) installed and Maven. 
Use the Maven tool in the root of this project:
 * http://www.oracle.com/technetwork/java/javase/downloads/index.html
 * https://maven.apache.org/

This library can be compiled as JAR file with the following command:

$ mvn install -P packaging-jar

JavaDocs can be generated with the following command:

$ mvn javadoc:javadoc -P packaging-jar

Example Maven build results for JAR packaging:

$ mvn install -P packaging-jar
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building Binary Modeling Data Library Plug-in 1.0.0-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ gov.usgs.jem.binarymodelingdata ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] skip non existing resourceDirectory /home/mark/JEM_BMD/gov.usgs.jem.binarymodelingdata/src/main/resources
[INFO]
[INFO] --- maven-compiler-plugin:3.3:compile (default-compile) @ gov.usgs.jem.binarymodelingdata ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 14 source files to /home/mark/JEM_BMD/gov.usgs.jem.binarymodelingdata/target/classes
[INFO]
[INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ gov.usgs.jem.binarymodelingdata ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] skip non existing resourceDirectory /home/mark/JEM_BMD/gov.usgs.jem.binarymodelingdata/src/test/resources
[INFO]
[INFO] --- maven-compiler-plugin:3.3:testCompile (default-testCompile) @ gov.usgs.jem.binarymodelingdata ---
[INFO] No sources to compile
[INFO]
[INFO] --- maven-surefire-plugin:2.17:test (default-test) @ gov.usgs.jem.binarymodelingdata ---
[INFO] No tests to run.
[INFO]
[INFO] --- maven-jar-plugin:2.4:jar (default-jar) @ gov.usgs.jem.binarymodelingdata ---
[INFO] Building jar: /home/mark/JEM_BMD/gov.usgs.jem.binarymodelingdata/target/gov.usgs.jem.binarymodelingdata-1.0.0-SNAPSHOT.jar
[INFO]
[INFO] --- maven-install-plugin:2.5.2:install (default-install) @ gov.usgs.jem.binarymodelingdata ---
[INFO] Installing /home/mark/JEM_BMD/gov.usgs.jem.binarymodelingdata/target/gov.usgs.jem.binarymodelingdata-1.0.0-SNAPSHOT.jar to /home/mark/.m2/repository/gov/usgs/jem/bmdconverter/group/gov.usgs.jem.binarymodelingdata/1.0.0-SNAPSHOT/gov.usgs.jem.binarymodelingdata-1.0.0-SNAPSHOT.jar
[INFO] Installing /home/mark/JEM_BMD/gov.usgs.jem.binarymodelingdata/pom.xml to /home/mark/.m2/repository/gov/usgs/jem/bmdconverter/group/gov.usgs.jem.binarymodelingdata/1.0.0-SNAPSHOT/gov.usgs.jem.binarymodelingdata-1.0.0-SNAPSHOT.pom
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 1.414 s
[INFO] Finished at: 2016-08-16T10:33:08-04:00
[INFO] Final Memory: 19M/300M
[INFO] ------------------------------------------------------------------------

Usage:
BMDReader.open(p_FilePath)