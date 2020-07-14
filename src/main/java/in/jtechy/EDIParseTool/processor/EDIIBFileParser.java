package in.jtechy.EDIParseTool.processor;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Component
public class EDIIBFileParser {

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
                // Storing values in UNB variables.
                if (token.startsWith("UNB")) {
                    resultMap.put("UNB", true);
                    String[] subTokens = token.split("\\+");
                    int pivot = 1;
                    for(String subToken : subTokens) {
                        String[] childTokens = subToken.split(":");
                        int flag = 1;
                        for(String childToken : childTokens) {
                            resultMap.put("UNB"+pivot+"."+flag, childToken);
                            flag++;
                        }
                        pivot++;
                    }
                }
                // Storing values in UNG variables.
                if (token.startsWith("UNG")) {
                    resultMap.put("UNG", true);
                    String[] subTokens = token.split("\\+");
                    int pivot = 1;
                    for(String subToken : subTokens) {
                        String[] childTokens = subToken.split(":");
                        int flag = 1;
                        for(String childToken : childTokens) {
                            resultMap.put("UNG"+pivot+"."+flag, childToken);
                            flag++;
                        }
                        pivot++;
                    }
                }
                //Storing values in UNH variables.
                if (token.startsWith("UNH")) {
                    resultMap.put("UNH", true);
                    String[] subTokens = token.split("\\+");
                    int pivot = 1;
                    for(String subToken : subTokens) {
                        String[] childTokens = subToken.split(":");
                        int flag = 1;
                        for(String childToken : childTokens) {
                            resultMap.put("UNH"+pivot+"."+flag, childToken);
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
        Path excelFilePath = outputFilePath.resolve("EDIFACT_IB_LOADSHEET.xls");
        try {
            FileInputStream inputStream = new FileInputStream(excelFilePath.toFile());
            Workbook workbook = WorkbookFactory.create(inputStream);

            Sheet sheet = workbook.getSheetAt(0);

            int rowCount = sheet.getLastRowNum();
            Row row = sheet.createRow(++rowCount);

            int columnCount = 1;
            Cell cell = row.createCell(columnCount);
            //cell.setCellValue(rowCount);

            cell = row.createCell(++columnCount);
            //cell.setCellValue("test");

            // Hardcode values
            cell = row.createCell(8);   cell.setCellValue("IMMEDIATE");
            cell = row.createCell(19);   cell.setCellValue("27");   //TP Segment Terminator
            cell = row.createCell(20);   cell.setCellValue("2B");   //TP Element Delimiter
            cell = row.createCell(21);   cell.setCellValue("3A");   //TP Sub Element Delimiter
            cell = row.createCell(22);   cell.setCellValue("3F");   //TP Release Character
            cell = row.createCell(26);   cell.setCellValue("NO");   //Perform Sequence Number Check
            cell = row.createCell(27);   cell.setCellValue("NO");   //Perform Duplicate Control Check
            cell = row.createCell(29);   cell.setCellValue("1");   //UNG Local Control Number
            cell = row.createCell(30);   cell.setCellValue("1");   //UNH Local Control Number
            cell = row.createCell(36);   cell.setCellValue("EDIFACT");   //Standards Board

            cell = row.createCell(12); //UNA YES or NO
            if((boolean)resultMap.get("UNA")) {
                cell.setCellValue("YES");
            }else {
                cell.setCellValue("NO");
            }

            cell = row.createCell(9); //UNB01.1 Syntax Indentifier
            if((boolean)resultMap.get("UNB")) {
                if(resultMap.get("UNB2.1")!=null) {
                    cell.setCellValue(resultMap.get("UNB2.1").toString());
                }
            }
            cell = row.createCell(10); //UNB01.2 Syntax Version Number
            if((boolean)resultMap.get("UNB")) {
                if(resultMap.get("UNB2.2")!=null) {
                    cell.setCellValue(resultMap.get("UNB2.2").toString());
                }
            }
            cell = row.createCell(15); //UNB02.2 Sender Qualifier
            if((boolean)resultMap.get("UNB")) {
                if(resultMap.get("UNB3.2")!=null) {
                    cell.setCellValue(resultMap.get("UNB3.2").toString());
                }
            }
            cell = row.createCell(16); //UNB02.1 Sender EDI ID
            if((boolean)resultMap.get("UNB")) {
                if(resultMap.get("UNB3.1")!=null) {
                    cell.setCellValue(resultMap.get("UNB3.1").toString());
                }
            }
            cell = row.createCell(45); //UNB02.3 Sender EDI ID
            if((boolean)resultMap.get("UNB")) {
                if(resultMap.get("UNB3.3")!=null) {
                    cell.setCellValue(resultMap.get("UNB3.3").toString());
                }
            }
            cell = row.createCell(39); //UNB03.2 Receiver Qualifier
            if((boolean)resultMap.get("UNB")) {
                if(resultMap.get("UNB4.2")!=null) {
                    cell.setCellValue(resultMap.get("UNB4.2").toString());
                }
            }
            cell = row.createCell(40); //UNB03.1 Receiver EDI ID
            if((boolean)resultMap.get("UNB")) {
                if(resultMap.get("UNB4.1")!=null) {
                    cell.setCellValue(resultMap.get("UNB4.1").toString());
                }
            }
            cell = row.createCell(46); //UNB03.3 Sender EDI ID
            if((boolean)resultMap.get("UNB")) {
                if(resultMap.get("UNB4.3")!=null) {
                    cell.setCellValue(resultMap.get("UNB4.3").toString());
                }
            }
            cell = row.createCell(28);  //UNB05 Local Control Number
            if((boolean)resultMap.get("UNB")) {
                if(resultMap.get("UNB6.1")!=null) {
                    cell.setCellValue(resultMap.get("UNB6.1").toString());
                }
            }
            cell = row.createCell(11); //UNB07 Application Reference
            if((boolean)resultMap.get("UNB")) {
                if(resultMap.get("UNB8.1")!=null) {
                    cell.setCellValue(resultMap.get("UNB8.1").toString());
                }
            }
            cell = row.createCell(48); //UNB10 Communications agreement identification
            if((boolean)resultMap.get("UNB")) {
                if(resultMap.get("UNB11.1")!=null) {
                    cell.setCellValue("UNB10 = " + resultMap.get("UNB11.1").toString());
                }
            }

            cell = row.createCell(7); //UNB11 TEST INDICATION
            if((boolean)resultMap.get("UNB")) {
                if(resultMap.get("UNB12.1")!=null) {
                    cell.setCellValue("T"); }
                    else cell.setCellValue("P");
            }
            cell = row.createCell(13); //UNG YES or NO
            if((boolean)resultMap.get("UNG")) {
                cell.setCellValue("YES");

                cell = row.createCell(17); //UNG02.1 SENDER'S IDENTIFICATION
                if(resultMap.get("UNG3.1")!=null) {
                    cell.setCellValue(resultMap.get("UNG3.1").toString());
                }
                cell = row.createCell(41); //UNG03.1 RECEIVER'S IDENTIFICATION
                if(resultMap.get("UNG4.1")!=null) {
                    cell.setCellValue(resultMap.get("UNG4.1").toString());
                }
                cell = row.createCell(29); //UNG05 FUNCTION GROUP REFERENCE NUMBER
                if(resultMap.get("UNG6.1")!=null) {
                    cell.setCellValue(resultMap.get("UNG6.1").toString());
                }

            }
            else {
                cell.setCellValue("NO");
            }
            cell = row.createCell(33); //UNH02.1 MESSAGE TYPE IDENTIFIER
            if((boolean)resultMap.get("UNH")) {
                if(resultMap.get("UNH3.1")!=null) {
                    cell.setCellValue(resultMap.get("UNH3.1").toString());
                }
            }
            cell = row.createCell(34); //UNH02.2 MESSAGE TYPE VERSION NUMBER
            if((boolean)resultMap.get("UNH")) {
                if(resultMap.get("UNH3.2")!=null) {
                    cell.setCellValue(resultMap.get("UNH3.2").toString());
                }
            }
            cell = row.createCell(32); //UNH02.3 MESSAGE TYPE RELEASE NUMBER
            if((boolean)resultMap.get("UNH")) {
                if(resultMap.get("UNH3.3")!=null) {
                    cell.setCellValue(resultMap.get("UNH3.3").toString());
                }
            }
            cell = row.createCell(35); //UNH02.4 CONTROLLING AGENCY
            if((boolean)resultMap.get("UNH")) {
                if(resultMap.get("UNH3.4")!=null) {
                    cell.setCellValue(resultMap.get("UNH3.4").toString());
                }
            }
            cell = row.createCell(31); //UNH02.5 ASSOCIATION ASSIGNED CODE
            if((boolean)resultMap.get("UNH")) {
                if(resultMap.get("UNH3.5")!=null) {
                    cell.setCellValue(resultMap.get("UNH3.5").toString());
                }
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
