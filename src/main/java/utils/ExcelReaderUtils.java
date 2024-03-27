package utils;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ExcelReaderUtils {
    public static List<LinkedHashMap<String, String>> getExcelDataAsList(String excelFileName, String sheetName) throws IOException {
        List<LinkedHashMap<String, String>> dataFromExcel = new ArrayList<>();
        //access excel file
        Workbook workbook = WorkbookFactory.create(new File(Constants.excelFilePath + excelFileName + ".xlsx"));
        //access desired sheet
        Sheet sheet = workbook.getSheet(sheetName);
        //Fetch total number of rows
        int totalRows = sheet.getPhysicalNumberOfRows();
        List<String> allKeys = new ArrayList<>();
        DataFormatter dataFormatter = new DataFormatter();
        LinkedHashMap<String, String> mapData;
        for (int i = 0; i < totalRows; i++) {
            mapData = new LinkedHashMap<>();
            if (i == 0) {
                int totalCols = sheet.getRow(i).getPhysicalNumberOfCells();
                //This for loop helps to fetch columns title from first row.
                for (int j = 0; j < totalCols; j++) {
                    allKeys.add(sheet.getRow(i).getCell(j).getStringCellValue());
                }
            } else {
                //This code fetch data from 2nd row onwards
                int totalCols = sheet.getRow(i).getPhysicalNumberOfCells();
                for (int j = 0; j < totalCols; j++) {
                    //Cell value fetched is converted in String.
                    String cellValue = dataFormatter.formatCellValue(sheet.getRow(i).getCell(j));
                    //data is stored in key value pair.
                    mapData.put(allKeys.get(j), cellValue);
                }
                dataFromExcel.add(mapData);

            }
        }


        return dataFromExcel;
    }
}
