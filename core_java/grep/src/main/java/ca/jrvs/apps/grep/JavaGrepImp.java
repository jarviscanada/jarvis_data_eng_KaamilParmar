package ca.jrvs.apps.grep;

import java.io.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.log4j.BasicConfigurator;

import java.util.regex.*;
public class JavaGrepImp implements JavaGrep {

    private final Logger logger = LoggerFactory.getLogger(JavaGrepImp.class);

    private String regex;
    private String rootPath;
    private String outFile;

    public static void main(String[] args) {
        if(args.length != 3)
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");

        BasicConfigurator.configure();

        BasicConfigurator.configure();
        JavaGrepImp javaGrepImp = new JavaGrepImp();
        javaGrepImp.setRegex(args[0]);
        javaGrepImp.setRootPath(args[1]);
        javaGrepImp.setOutFile(args[2]);

        try {
            javaGrepImp.process();
        } catch (Exception ex) {
            javaGrepImp.logger.error("Error: Unable to process", ex);
        }
    }

    @Override
    public void process() throws IOException {
        ArrayList<String> matchedLines = new ArrayList<>();

        for(File file: listFiles(rootPath)){
            for(String line: readLines(file)){
                if(containsPattern(line)) matchedLines.add(line);
            }
        }
        writeToFile(matchedLines);
    }

    @Override
    public List<File> listFiles(String rootDir) {
        File current = new File(rootDir);
        List<File> foundFiles= new ArrayList<>();

        if(current.isDirectory()){
            File[] currFiles = current.listFiles();
            assert currFiles != null;
            for(File file: currFiles){
                if(file.isDirectory())
                    foundFiles.addAll(listFiles(file.getAbsolutePath()));
                else if (file.isFile())
                    foundFiles.add(file);
            }
        }

        return foundFiles;
    }

    @Override
    public List<String> readLines(File inputFile) {
        List<String> lines = new ArrayList<>();

        try (FileReader fileReader = new FileReader(inputFile);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }

        } catch (IOException  FileNotFoundException ) {
            logger.error("An exception occurred");
        }
        return lines;
    }

    @Override
    public boolean containsPattern(String line) {
        try{
            Pattern pattern = Pattern.compile(this.getRegex());
            Matcher matcher = pattern.matcher(line);
            return matcher.find();
        }
        catch(PatternSyntaxException e){
            logger.error("Incorrect REGEX syntax");
        }

        return false;
    }

    @Override
    public void writeToFile(List<String> lines) throws IOException {

        try (
                FileOutputStream fos = new FileOutputStream(this.getOutFile());
             OutputStreamWriter osw = new OutputStreamWriter(fos);
             BufferedWriter writer = new BufferedWriter(osw)
        ) {

            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            // Handle IOException, e.g., log or throw an exception
            logger.error("IOException occurred: "+e);
        }
    }

    @Override
    public String getRootPath() {
        return this.rootPath;
    }

    @Override
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public String getRegex() {
        return this.regex;
    }

    @Override
    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public String getOutFile() {
        return this.outFile;
    }

    @Override
    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }


}
