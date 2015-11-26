The project with the EIS is tecs.eis.childModel.

We still have to parse the name of the PALClient as a GOAL argument in the init method, by now is hardcoded "childsimulator"
I've created a test agent to use the EIS. It's the project ChildTest

The other projects of PAL have been updated so all of them are Maven projects.

palTECS-utils requires the dependency "tecs-lib.1.0.jar"
pal-utils requires the dependency "palTECS-utils-1.0.jar"
createClients requires the dependency "pal-utils-1.0.jar"

The EIS (tecs.eis.childModel) requires the dependency "pal-utils-1.0.jar"

You need to install the dependencies in the local Maven repository, to do so you have to run the following command:

mvn install:install-file -Dfile=path\to\file.jar -DgroupId=tecs-lib or pal-utils or palTECS-utils 
-DartifactId=the same than groupId -Dversion=1.0 -Dpackaging=jar

Then the pom.xml automatically detects the jar and builds the project without errors.

If you make some changes in the EIS you need to run the pom.xml as maven install and copy the "tecs.eis.childModel-1.0-rnull-jar-with-dependencies.jar" to the ChildTest project folder.


Cheers,
Santi
