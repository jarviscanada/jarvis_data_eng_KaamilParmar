package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.util.List;

import java.util.ArrayList;

public class JavaGrepImp implements JavaGrep {

    private String regex;
    private String rootPath;
    private String outFile;


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
        File[] currFiles;

        if(current.isDirectory()){
            currFiles = current.listFiles();
            assert currFiles != null;
            for(File file: currFiles){
                if(file.isDirectory())
                    listFiles(file.getAbsolutePath());
                else if (file.isFile())
                    foundFiles.add(file);
            }
        }

        return foundFiles;
    }

    @Override
    public List<String> readLines(File inputFile) {
        return null;
    }

    @Override
    public boolean containsPattern(String line) {
        return false;
    }

    @Override
    public void writeToFile(List<String> lines) throws IOException {

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

    public static void main(String[] args) {

    }
}
