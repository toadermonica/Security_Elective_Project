package Utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileUtils {

  // readAllBytes:
  // The method merely hides the use of Paths.

  public static byte[] readAllBytes(String inputFile) {
    byte[] bytesRead = {};
    try {
      bytesRead = Files.readAllBytes(Paths.get(inputFile));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return bytesRead;
  }

  // write:
  // The method merely "hides" the use of Paths.
  // Overwrites if the file exists already; otherwise creates the file
  // This behavior is due to java.nio.file.Files.write

  public static void write(String outputFile, byte[] output) {
    try {
      Files.write(Paths.get(outputFile), output);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String readFile(File file){
    StringBuilder stringBuffer = new StringBuilder();
    BufferedReader bufferedReader = null;

    try {
      bufferedReader = new BufferedReader(new FileReader(file));

      String text;
      while ((text = bufferedReader.readLine()) != null) {
        stringBuffer.append(text);
      }
    } catch (FileNotFoundException ex) {
      System.out.println(ex);
    } catch (IOException ex) {
      System.out.println(ex);
    } finally {
      try {
        bufferedReader.close();
      } catch (IOException ex) {
        System.out.println(ex);
      }
    }
    return stringBuffer.toString();
  }

  // getAllFileNamesWithExt:
  // returns a list of all filenames (strings) in a directory
  // that have the specified extension
  
  // getAllFileNamesWithExt/3: file names must have both a certain filename and a certain extension
 
  public static String[] getAllFileNames(String dir, String name, String ext) {
    ArrayList<String> results = new ArrayList<String>();
    File[] files = new File(dir).listFiles();
    for (File file : files) {
      if (file.isFile()) { 
        String fname = file.getName();
        String[] parts = fname.split("[.]");       
        if (parts[0].equals(name) && parts[parts.length-1].equals(ext)) results.add(fname);
      }
    }
    String[] names = new String[results.size()];
    for (int i=0; i<names.length; i++) {
      names[i] = results.get(i);
    }
    return names;
  }

  // getAllFileNamesWithExt/2: file names need only have a certain extension

  public static String[] getAllFileNames(String dir, String ext) {
    ArrayList<String> results = new ArrayList<String>();
    File[] files = new File(dir).listFiles();
    for (File file : files) {
      if (file.isFile()) { 
        String fname = file.getName();
        String[] parts = fname.split("[.]");       
        if (parts[parts.length-1].equals(ext)) results.add(fname);
      }
    }
    String[] names = new String[results.size()];
    for (int i=0; i<names.length; i++) {
      names[i] = results.get(i);
    }
    return names;
  }
}
