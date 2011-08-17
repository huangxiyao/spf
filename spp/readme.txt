* Deploy 3rd party jars to our repository:

mvn -s your_settings.xml deploy:deploy-file 
-DrepositoryId=SPP -Durl=file:///c:/path/to/spp-repository 
-DgroupId=x -DartifactId=x -Dversion=x -Dpackaging=jar -Dfile=<path to .jar>


* Deploy our releases to our repository:

mvn -s your_settings.xml deploy

