package ca.jrvs.apps.grep;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.nio.file.Paths;

public class JavaGrepLambdaImp extends JavaGrepImp{
    private static final Logger logger = LoggerFactory.getLogger(JavaGrepLambdaImp.class);

    public static void main(String[] args) {
        if(args.length != 3)
            throw new IllegalArgumentException("USAGE: JavaGrepLambda regex rootPath outFile");

        BasicConfigurator.configure();

        BasicConfigurator.configure();
        JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
        javaGrepLambdaImp.setRegex(args[0]);
        javaGrepLambdaImp.setRootPath(args[1]);
        javaGrepLambdaImp.setOutFile(args[2]);

        try {
            javaGrepLambdaImp.process();
        } catch (Exception ex) {
            JavaGrepLambdaImp.logger.error("Unable to process regex");
        }
    }

    @Override
    public void process() throws IOException {
        ArrayList<String> matchedLines = new ArrayList<>();

        Stream<File> listOfFiles= listFiles(this.getRootPath()).stream();

        listOfFiles.forEach(file ->
                readLines(file).stream().filter(this::containsPattern)
                        .forEach(matchedLines::add)
        );

        writeToFile(matchedLines);
    }

    @Override
    public List<File> listFiles(String rootDir) {
        Path current = Paths.get(rootDir);
        List<File> foundFiles= new ArrayList<>();

        try (Stream<Path> paths = Files.walk(current)) {
            paths.filter(Files::isRegularFile).forEach(path -> foundFiles.add(path.toFile()) );
        } catch (IOException e) {
            logger.error("IO Error");
        }

        return foundFiles;
    }

    @Override
    public List<String> readLines(File inputFile) {
        List<String> lines = new ArrayList<>();

        try (Stream<String> fileLines = Files.lines(inputFile.toPath()) ) {
            fileLines.forEach(lines::add);
        } catch (IOException e) {
            logger.error("An exception occurred");
        }
        return lines;
    }


    @Override
    public void writeToFile(List<String> lines) throws IOException {
        super.writeToFile(lines);
    }
}
