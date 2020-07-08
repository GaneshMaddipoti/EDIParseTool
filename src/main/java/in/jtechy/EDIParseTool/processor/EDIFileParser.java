package in.jtechy.EDIParseTool.processor;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Component
public class EDIFileParser {

    public static void parse(Path file) throws IOException {
        Map<String, Object> resultMap = new HashMap<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file.toFile()));
        String line = bufferedReader.readLine();
        while(line != null) {
            if(line.startsWith("UNA")) {
                resultMap.put("UNA", true);
            }else if(line.startsWith("UNB")) {
                resultMap.put("UNB", true);

            }
        }
        writeToExcel(resultMap);
    }

    private static void writeToExcel(Map<String, Object> resultMap) {
        //Excel creation logic
        //1st column write

    }


}
