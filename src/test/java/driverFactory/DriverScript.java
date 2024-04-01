package driverFactory;

import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import commonfunction.FunctionLibrary;
import utilites.ExcelFileUtil;


public class DriverScript {
String inputpath ="./Fileinput/DataEngine.xlsx";
String outputpath ="./Fileoutput/HybridResults.xlsx";
ExtentReports report;
ExtentTest logger;
String  sheet = "MasterTestCases";
WebDriver driver;
public void startTest() throws Throwable
{
String modulestatus =""	;
//create object for excelfileutil class
ExcelFileUtil xl = new ExcelFileUtil(inputpath);
//iterate all in sheet
for(int i=1;i<=xl.rowCount(sheet);i++)
{
	if(xl.getCellData(sheet, i, 2).equalsIgnoreCase("Y"))
	{
		//store all corresponding sheet into TCModule
		String TCModule =xl.getCellData(sheet, i, 1);
		report=new ExtentReports("./target/Reports/"+TCModule+FunctionLibrary.generateDate()+".html");
		logger =report.startTest(TCModule);
		logger.assignAuthor("Ranga");
		//iterate all rows in TCModule
		for(int j=1;j<=xl.rowCount(TCModule);j++)
		{
			//read each cell from TCModule
			String Description =xl.getCellData(TCModule, j, 0);
			String Object_Type = xl.getCellData(TCModule, j, 1);
			String Locator_Type =xl.getCellData(TCModule, j, 2);
			String Locator_Value = xl.getCellData(TCModule, j, 3);
			String Test_Data = xl.getCellData(TCModule, j, 4);
			try {
				if(Object_Type.equalsIgnoreCase("startBrowser"))
				{
					driver = FunctionLibrary.startBrowser();
					logger.log(LogStatus.INFO, Description);
				}
				if(Object_Type.equalsIgnoreCase("openUrl"))
				{
					FunctionLibrary.openUrl();
					logger.log(LogStatus.INFO, Description);
				}
				if(Object_Type.equalsIgnoreCase("waitForElement"))
				{
					FunctionLibrary.waitForElement(Locator_Type, Locator_Value, Test_Data);
					logger.log(LogStatus.INFO, Description);
				}
				if(Object_Type.equalsIgnoreCase("typeAction"))
				{
					FunctionLibrary.typeAction(Locator_Type, Locator_Value, Test_Data);
					logger.log(LogStatus.INFO, Description);
				}
				if(Object_Type.equalsIgnoreCase("clickAction"))
				{
					FunctionLibrary.clickAction(Locator_Type, Locator_Value);
					logger.log(LogStatus.INFO, Description);
				}
				if(Object_Type.equalsIgnoreCase("validateTitle"))
				{
					FunctionLibrary.validateTitle(Test_Data);
					logger.log(LogStatus.INFO, Description);
				}
				if(Object_Type.equalsIgnoreCase("closeBrowser"))
				{
					FunctionLibrary.closeBrowser();
					logger.log(LogStatus.INFO, Description);
				}
				//write a s pass into status cell in TCModule
				xl.setCellData(TCModule, j, 5, "Pass", outputpath);
				logger.log(LogStatus.PASS, Description);
				modulestatus="True";
			}catch(Exception e)
			{
			System.out.println(e.getMessage()); 
			//write a s Fail into status cell in TCModule
			xl.setCellData(TCModule, j, 5, "Fail", outputpath);
			logger.log(LogStatus.FAIL, Description);
			modulestatus="False";
			}
			if(modulestatus.equalsIgnoreCase("True"))
			{
				//write as pass into Sheet status cell
				xl.setCellData(sheet, i, 3, "Pass", outputpath);
			}
			if(modulestatus.equalsIgnoreCase("False"))
			{
				//write as Fail into Sheet status cell
				xl.setCellData(sheet, i, 3, "Fail", outputpath);
			}
			report.endTest(logger);
			report.flush();
		}
	}
	else
	{
		//write as blocked into status cell for Testcases flag to N
		xl.setCellData(sheet, i, 3, "Blocked", outputpath);
	}
}

	

}
}
