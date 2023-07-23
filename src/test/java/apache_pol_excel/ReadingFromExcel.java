package apache_pol_excel;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class ReadingFromExcel {
    public static void main(String[] args) throws IOException {
        //Assign path of the file to string
        String excelFilePath = "test_data/Book1.xlsx";

        // Reaching out the file
        FileInputStream fileInputStream = new FileInputStream(excelFilePath);

        // Opening the file where we specified the path
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

        // Going into specific sheet in the workbook
        XSSFSheet sheet = workbook.getSheet("Sheet1");

        // getting first name of the first row
        String name = sheet.getRow(1).getCell(0).getStringCellValue();
        System.out.println(name);

        // Getting the dept name of second row.
        String dept = sheet.getRow(2).getCell(2).getStringCellValue();
        System.out.println(dept);
    }
}
