# Introduction
This Java Grep App emulates the Linux command line tool "grep", which allows for cross-platform usage of it beyond Linux
through Java implementation. It was developed using core Java 8 and managed with Maven for dependency handling, 
compilation, and project organization. Moreover, the app provides two kinds of implementation. One relies on traditional
language features, including for loops, whilst the other leverages lambda functions and Stream APIs to replace tradition
Java coding conventions, offering a more concise and memory-efficient alternative. Users can choose between the two 
implementations based on their preferences. Finally, testing with a logger class ensures the 
reliability and accuracy of both implementations.

# Quick Start
There are two main ways to start the application.
## 1. Using jar file made after maven compile
```bash
mvn clean package
java -jar ./target/grep-1.0-SNAPSHOT.jar [arg1] [arg2] [arg3]
```

## 2. Using the Docker Image
```bash
docker pull klparmar/grep
docker run --rm \
-v `pwd`/data:/data -v `pwd`/log:/log \
klparmar/grep [arg1] [arg2] [arg3]
```

# Implemenation
This app essentially uses a process method that streamlines the sequence of operations being executed. 
The pseudocode for the process method is outlined below:

## Pseudocode
```java
matchedLines = []
for file in listFilesRecursively(rootDir)
  for line in readLines(file)
      if containsPattern(line)
        matchedLines.add(line)
writeToFile(matchedLines)
```

## Performance Issue
The Java Grep Application faces performance issues with large files due to its original design, storing each line in 
memory. The JavaGrepLambdaImp class resolves this by integrating Java Stream and Lambda functionalities. These features 
allow functional programming, supporting map-reduce transformations on collections without data storage. To further 
optimize, adjusting the JVM heap size aids in addressing memory concerns during the processing of extensive files.

# Test
Testing was done through the Logger instance to handles errors and ensure functionality during implementation.
Furthermore, after implementation, different comand-line arguments were tested.
# Deployment
There is a docker file provided which contains the commands needed to create a docker image of the app. Be sure to
compile and clean the maven project first and the shade plugin if it is not already present.
After, the image can be created from the docker file and then run locally or added to your
docker hub profile.


# Improvement
List three things you can improve in this project.