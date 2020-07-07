package in.jtechy.EDIParseTool.processor;

import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

public class EDIFileParser {

    public void parse(MultipartFile file) {
        Map<String, String > resultMap = new HashMap<>();
        //Parse logic


        writeToExcel(resultMap);
    }

    private void writeToExcel(Map<String, String> resultMap) {
        //Excel creation logic
    }


}
