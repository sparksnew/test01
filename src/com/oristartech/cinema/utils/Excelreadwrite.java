package com.oristartech.cinema.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class Excelreadwrite {


public static void main(String[] args) throws Exception{
	excelRead();
/*String[][] data ;
data = excelRead();

String expectedtitle;
for (int i = 1; i < data.length; i++ ) {
int LastRow = i;
expectedtitle = login(data[i][0],data[i][1]); 
System.out.println("page title after login is" + expectedtitle );

if(expectedtitle.equalsIgnoreCase(":: IRCTC :: - Plan My Travel")){

    System.out.println("PASS");
    String status="PASS";
    excelwrite(status,LastRow);
    }
else{
    System.out.println("FAIL");
    String status = "FAIL";
    excelwrite(status,LastRow);
    }
} 

} */

/*public static String login(String username,String password) throws InterruptedException{

//Step 1 Open Firefox
WebDriver driver = new FirefoxDriver();

//Step 2 Go to url
driver.get("https://www.irctc.co.in/");
String actualtitle= driver.getTitle(); 
WebDriverWait wait = new WebDriverWait(driver,60);
wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("userName")));
Thread.sleep(3000);

driver.findElement(By.name("userName")).sendKeys(username);
driver.findElement(By.name("password")).sendKeys(password);
driver.findElement(By.id("button")).click();
Thread.sleep(3000);   

String expectedtitle= driver.getTitle();

   driver.close();
   return expectedtitle;


}*/
}


public static String[][] excelRead() throws Exception {
	File excel = new File("D:\\Work\\test2.xls");
	FileInputStream fis = new FileInputStream(excel);
	HSSFWorkbook wb = new HSSFWorkbook(fis);
	HSSFSheet ws = wb.getSheet("Sheet1");
	int rowNum = ws.getLastRowNum() + 1;
	int colNum = ws.getRow(0).getLastCellNum();
	String[][] data = new String[rowNum][colNum];
	for (int i = 0 ; i < rowNum ; i++) {
	HSSFRow row = ws.getRow(i);
	for (int j=0  ; j < colNum ; j++){
	HSSFCell cell = row.getCell(j);
	String value = cellToString(cell);
	data[i][j] = value;
	// System.out.println("The value is" + value);
	
	
	}
	}
	return data;

}
	public static void  excelwrite(String status, int LastRow) throws Exception {
	try{
	    FileInputStream file = new FileInputStream(new File("D:\\Work\\test2.xls"));
	
	    HSSFWorkbook workbook = new HSSFWorkbook(file);
	    HSSFSheet sheet = workbook.getSheetAt(0);
	
	    Row row = sheet.getRow(LastRow);
	
	    Cell cell2 = row.createCell(2);
	    cell2.setCellValue(status);
	    System.out.println(status);
	
	    file.close();
	    FileOutputStream outFile =new FileOutputStream(new File("D:\\Work\\test2.xls"));
	    workbook.write(outFile);
	
		  }
		
		   catch (FileNotFoundException e) {
		    e.printStackTrace();
		 } 
		   catch (IOException e) {
		    e.printStackTrace();
		}
		   catch (HeadlessException e) 
		{
		    e.printStackTrace();
		}
}


public static String cellToString(HSSFCell cell) {
	int type;
	Object result ;
	type = cell.getCellType();
	switch (type) {
	case 0 :
	result = cell.getNumericCellValue();
	break;
	case 1 :
	result = cell.getStringCellValue();
	break;
	default :
	throw new RuntimeException("There are no support for this type of cell");
	}
	return result.toString();
	}
}