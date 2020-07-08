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
            if (line.startsWith("UNA")) {
                resultMap.put("UNA", true);
            } else if (line.startsWith("UNB")) {
                resultMap.put("UNB", true);
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



