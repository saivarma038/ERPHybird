package utilites;
import java.io.FileInputStream;

import java.io.FileOutputStream;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
public class ExcelFileUtil
{
Workbook wb;
//to read path of excel constructor
public ExcelFileUtil(String Excelpath) throws Throwable
{
	FileInputStream fi = new FileInputStream(Excelpath);
	wb = WorkbookFactory.create(fi);
}
//method for counting rows in sheet
public int rowCount(String sheetName)
{
	return wb.getSheet(sheetName).getLastRowNum();
}
//method for reading cell data
public String getCellData(String sheetName, int row, int column)
{
	String data ="";
	if(wb.getSheet(sheetName).getRow(row).getCell(column).getCellType()==CellType.NUMERIC)
	{
		int celldata=(int) wb.getSheet(sheetName).getRow(row).getCell(column).getNumericCellValue();
		data = String.valueOf(celldata);
	}else
	{
		data=wb.getSheet(sheetName).getRow(row).getCell(column).getStringCellValue();
	}
	return data;
}
public void setCellData(String sheetName,int row,int cloumn,String status,String WritrExcel) throws Throwable
{
	//get sheet from wb
	Sheet ws = wb.getSheet(sheetName);
	//get row from sheet
	Row rowNum = ws.getRow(row); 
	//create cell in row
	Cell cell = rowNum.createCell(cloumn);
	//write status 
	cell.setCellValue(status);
	if(status.equalsIgnoreCase("Pass"))
	{
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setColor(IndexedColors.GREEN.getIndex());
		font.setBold(true);
		style.setFont(font);
		ws.getRow(row).getCell(cloumn).setCellStyle(style);
	}
	else if(status.equalsIgnoreCase("Fail"))
	{
		CellStyle style =wb.createCellStyle();
		Font font = wb.createFont();
		font.setColor(IndexedColors.RED.getIndex());
		font.setBold(true);
		style.setFont(font);
		ws.getRow(row).getCell(cloumn).setCellStyle(style);
	}
	else if(status.equalsIgnoreCase("Blocked"))
	{
		CellStyle style =wb.createCellStyle();
		Font font = wb.createFont();
		font.setColor(IndexedColors.BLUE.getIndex());
		font.setBold(true);
		style.setFont(font);
		ws.getRow(row).getCell(cloumn).setCellStyle(style);
	}
	FileOutputStream fo = new FileOutputStream(WritrExcel);
	wb.write(fo);
}


}
