package ca.jrvs.apps.grep;

import java.io.*;
import java.util.*;

public interface JavaGrep {
    /**
     * Top level search workflow
     * @throws IOException
     */
    void process() throws IOException;

    /**
     * Traverse a given directory and return all files
     * @param rootDir
     * @return
     */
    List<File> listFiles(String rootDir);

    /**
     * Read a file and return all the lines
     * FileReader: Class used to read the contents of a file as a stream of characters.
     * BufferedReader:  Class that provides reading of text from a character input stream.
     * Character Encoding: Process of translating characters in bytes/binary and vice versa.
     * form.
     *
     * @param inputFile
     * @return
     */
    List<String> readLines(File inputFile);


    /**
     * check if a line contains regex pattern (passed by user)
     * @param line
     * @return
     */
    boolean containsPattern(String line);

    /**
     * Write lines to a file
     * Uses FileOutputStream, OutputStreamWriter, and BufferedWriter
     * @param lines
     * @throws IOException
     */
    void writeToFile(List<String> lines) throws IOException;

    String getRootPath();

    void setRootPath(String rootPath);

    String getRegex();

    void setRegex(String regex);

    String getOutFile();

    void setOutFile(String outFile);
}
