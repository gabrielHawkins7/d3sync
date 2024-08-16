package com.d3sync;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
    
    

    

    public static boolean hasFile(File directory, String targetFileName) throws IOException {
        Stream<Path> stream = Files.walk(directory.toPath());
        boolean ret = stream.filter(Files::isRegularFile).anyMatch(path->path.getFileName().toString().equals(targetFileName));
        stream.close();
        return ret;

    }
    public static long countFiles(File folder) throws IOException{
        Stream<Path> s = Files.walk(folder.toPath());
        long ret = s.filter(Files::isRegularFile).count(); 
        s.close();
        return ret;
    }
    
    public static double calculateFilePercentage(File sourceDir, File targetDir) throws IOException {
        double percent = 0.0;
        long sourceCount = countFiles(sourceDir);
        long targetCount = countCommonFiles(sourceDir,  targetDir);
        return 100 * targetCount / sourceCount;
    }
   private static long countCommonFiles(File source, File target) throws IOException{
    Stream<Path> stream = Files.walk(source.toPath());
    List<String> sourceFiles = stream.filter(Files::isRegularFile).map(path->path.getFileName().toString()).collect(Collectors.toList());
    Stream<Path> stream2 = Files.walk(target.toPath());
    List<String> targetFiles = stream2.filter(Files::isRegularFile).map(path->path.getFileName().toString()).collect(Collectors.toList());
    stream.close();
    stream2.close();
    long count = 0;
    for(String i : sourceFiles){
        if(targetFiles.contains(i)){
            count++;
        }
    }
    return count;
}

   
}
