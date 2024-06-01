package resources;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;

public class ReadExcelData {
	//Add 
	
	public static Object[][]  readExcelData(String testcase, String testDataFile) throws IOException{
		
		
		List<List<String>> testDataMultiRowList= new ArrayList<List<String>>();
		List<String> testDataSingleRowList = null;
		
		FileInputStream fis = new FileInputStream(testDataFile);
		XSSFWorkbook excelWorkbook = new XSSFWorkbook(fis);
		XSSFSheet sheetTestData = excelWorkbook.getSheet("TestData");
		
		Iterator<Row>  rows = sheetTestData.iterator();
		int countOfTestDataFields =0;
		while(rows.hasNext()) {
			testDataSingleRowList= new ArrayList<String>();
			Row current_row  =rows.next();
			if (current_row.getCell(0).getStringCellValue().equalsIgnoreCase(testcase)== false) {
				continue;
			}
			Iterator<Cell> cells = current_row.iterator();
			cells.next();
			while(cells.hasNext()) {
				Cell current_cell = cells.next();
				if (current_cell.getCellType() == CellType.STRING) {
					testDataSingleRowList.add(current_cell.getStringCellValue());
				}else {
					testDataSingleRowList.add(NumberToTextConverter.toText( current_cell.getNumericCellValue()));
				}
										
			}
			countOfTestDataFields = testDataSingleRowList.size();
			testDataMultiRowList.add(testDataSingleRowList);
		}
		
		Object[][] objtestDataMultiRowList = new Object[testDataMultiRowList.size()][countOfTestDataFields];
		int i=0;
		for(List<String> eachRow: testDataMultiRowList) {
			int j=0;
			for(String eachData: eachRow) {
				
				objtestDataMultiRowList[i][j] = eachData;
				j++;
			
			}
			i++;
		}
				
		fis.close();	
		return objtestDataMultiRowList;
		
		
	}
}
