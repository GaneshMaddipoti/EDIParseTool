package in.jtechy.EDIParseTool.processor;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Component
public class EDIFileParser {

    public static void parse(Path inputFile, Path outputFilePath) throws IOException {
        Map<String, Object> resultMap = new HashMap<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile.toFile()));
        String line = bufferedReader.readLine();
        while (line != null) {
            String[] tokens = line.split("\'");
            for(String token : tokens) {
                if (token.startsWith("UNA")) {
                    resultMap.put("UNA", true);
                }
                if (token.startsWith("UNB")) {
                    resultMap.put("UNB", true);
                    String[] subTokens = token.split("\\+");
                    int pivot = 1;
                    for(String subToken : subTokens) {
                        String[] childTokens = subToken.split(":");
                        int flag = 1;
                        for(String childToken : childTokens) {
                            resultMap.put("UNB"+pivot+"."+flag, childTokens);
                            flag++;
                        }
                        pivot++;
                    }
                }
            }
            line = bufferedReader.readLine();
        }
        writeToExcel(resultMap, outputFilePath);
    }

    private static void writeToExcel(Map<String, Object> resultMap, Path outputFilePath) {
        Path excelFilePath = outputFilePath.resolve("EDIFACT_LOADSHEET.xls");
        try {
            FileInputStream inputStream = new FileInputStream(excelFilePath.toFile());
            Workbook workbook = WorkbookFactory.create(inputStream);

            Sheet sheet = workbook.getSheetAt(0);

            int rowCount = sheet.getLastRowNum();
            Row row = sheet.createRow(++rowCount);

            int columnCount = 0;
            Cell cell = row.createCell(columnCount);
            cell.setCellValue(rowCount);

            cell = row.createCell(++columnCount);
            cell.setCellValue("test");

            cell = row.createCell(12); //UNA
            if((boolean)resultMap.get("UNA")) {
                cell.setCellValue("YES");
            }else {
                cell.setCellValue("NO");
            }
            cell = row.createCell(9); //UNB01.1
            if((boolean)resultMap.get("UNB")) {
                cell.setCellValue((String) resultMap.get("UNB01.1"));
            }

            inputStream.close();

            FileOutputStream outputStream = new FileOutputStream(excelFilePath.toFile());
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}



