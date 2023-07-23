package apache_pol_excel;
import utils.ExcelUtil;
import java.util.List;

public class TestingExcelUtil {
    public static void main(String[] args) {
        ExcelUtil.openExcelFile("Book1", "Sheet1");

        List<String> columnValues = ExcelUtil.getColumnValues(2);
        System.out.println("Values from the 2d column: " + columnValues);

        String singleCell = ExcelUtil.getValue(1, 2);
        System.out.println(singleCell);

        List<String> rowValues = ExcelUtil.getRowValues(2);
        System.out.println("Values from the 3rd row: " + rowValues);

        List<List<String>> tableValues = ExcelUtil.getValues();
        System.out.println(tableValues);

        // Iterate over all rows.
        for(List<String> row: tableValues){
            // iterate over all cells in the current row
            for (String cell : row) {
                // Print the value of the current cell.
                System.out.print(cell + " | ");
            }
            System.out.println();
        }
    }
}
