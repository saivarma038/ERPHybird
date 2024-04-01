package commonfunction;

import org.testng.Assert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

public class FunctionLibrary 
{
public static WebDriver driver;
public static Properties conpro;
//method for launching browser
public static WebDriver startBrowser()throws Throwable
{
	conpro = new Properties();
	//load propertyfile
	conpro.load(new FileInputStream("./PropertyFile\\Environment.properties"));
	if(conpro.getProperty("Browser").equalsIgnoreCase("chrome"))
	{
		driver = new ChromeDriver();
		driver.manage().window().maximize();
	}
	else if(conpro.getProperty("Browser").equalsIgnoreCase("firefox"))
	
	{
		driver = new FirefoxDriver();
	}
	else
	{
		Reporter.log("Browser value is not matching",true);
	}
	return driver;
}
//method for launching url
public static void  openUrl()
{
	driver.get(conpro.getProperty("Url"));
}
//method for to wait for any webelement
public static void waitForElement(String locatorType,String Loctorvalue, String TestData)
{
	WebDriverWait mywait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(TestData)));
	if(locatorType.equalsIgnoreCase("name"))
	{
		//wait until element is visible
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.name(Loctorvalue)));
		
	}
	if(locatorType.equalsIgnoreCase("xpath"))
	{
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Loctorvalue)));
	}
	if(locatorType.equalsIgnoreCase("id"))
	{
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Loctorvalue)));
	}
}
//method for textboxes
public static void typeAction(String LoctorType,String Loctorvalue,String TestData)
{
if(LoctorType.equalsIgnoreCase("xpath"))	
{
driver.findElement(By.xpath(Loctorvalue)).clear();
driver.findElement(By.xpath(Loctorvalue)).sendKeys(TestData);
}

if(LoctorType.equalsIgnoreCase("name"))	
{
driver.findElement(By.name(Loctorvalue)).clear();
driver.findElement(By.name(Loctorvalue)).sendKeys(TestData);
}
if(LoctorType.equalsIgnoreCase("id"))
{
driver.findElement(By.id(Loctorvalue)).clear();
driver.findElement(By.id(Loctorvalue)).sendKeys(TestData);
}
}
//method for buttons,radiobuttons,checkboxes,links,images
public static void clickAction(String LoctorType, String Loctorvalue )
{
	if(LoctorType.equalsIgnoreCase("xpath"))
	{
		driver.findElement(By.xpath(Loctorvalue)).click();
	}
	if(LoctorType.equalsIgnoreCase("name"))
	{
		driver.findElement(By.name(Loctorvalue)).click();
	}
	if(LoctorType.equalsIgnoreCase("id"))
	{
		driver.findElement(By.id(Loctorvalue)).sendKeys(Keys.ENTER);
	}
}
//method for validate tittle
public static void validateTitle (String Expected_title)
{

	String Actual_title = driver.getTitle();
	try {
		Assert.assertEquals(Actual_title, Expected_title, "Title is not matching");
	} catch (AssertionError a)
	{
		System.out.println(a.getMessage());
	}
	
}
public static void closeBrowser()
{
driver.quit();
}
//method for date generate
public static String generateDate()
{
	Date date = new Date();
	DateFormat df = new SimpleDateFormat("YYYY_MM_dd hh_mm");
	return df.format(date);
}
//method for listboxes
public static void dropDownAction(String LoctorType, String LoctorValue,String TestData)
{
if(LoctorType.equalsIgnoreCase("id"))
{
int value = Integer.parseInt(TestData);
Select element = new Select(driver.findElement(By.id(LoctorValue)));
element.selectByIndex(value);
}

if(LoctorType.equalsIgnoreCase("name"))
{
int value = Integer.parseInt(TestData);
Select element = new Select(driver.findElement(By.name(LoctorValue)));
element.selectByIndex(value);
}

if(LoctorType.equalsIgnoreCase("xpath"))
{
int value = Integer.parseInt(TestData);
Select element = new Select(driver.findElement(By.xpath(LoctorValue)));
element.selectByIndex(value);
}
  }
//method for capturing stock number into notepad
public static void capturestock(String LoctorType,String LoctorValue) throws Throwable
{
	String stock_Num = "";
	if(LoctorType.equalsIgnoreCase("id"))
	{
		stock_Num= driver.findElement(By.id(LoctorValue)).getAttribute("value");
	}
	if(LoctorType.equalsIgnoreCase("name"))
	{
		stock_Num=driver.findElement(By.name(LoctorValue)).getAttribute("value");
	}
	if(LoctorType.equalsIgnoreCase("xpath"))
	{
		stock_Num=driver.findElement(By.xpath(LoctorValue)).getAttribute("value");
	}
	FileWriter fw =new FileWriter("./CaptureData/stockNumber.txt");
	BufferedWriter bw = new BufferedWriter(fw);
	bw.write(stock_Num);
	bw.flush();
	bw.close();
}
//method for stock table validation
public static void stockTable() throws Throwable
{
// read data from note pad
	FileReader fr = new FileReader("./CaptureData/stockNumber.txt");
	BufferedReader br = new BufferedReader(fr);
	String Exp_Data = br.readLine();
	if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
		driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
	driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
	driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
	driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
	Thread.sleep(3000);
	String Act_Data =driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[8]/div/span/span")).getText();
	Reporter.log(Act_Data+"==================="+Exp_Data,true);
	try {
		Assert.assertEquals(Act_Data, Exp_Data, "Stock not matching");
	} catch (AssertionError a) 
	{
		System.out.println(a.getMessage());
	}
	
}
//method for capture supplier number into notepad
public static  void capturesup(String LocatorType,String LoctorValue) throws Throwable
{
String supplerNum ="";
if(LocatorType.equalsIgnoreCase("xpath"))
{
supplerNum = driver.findElement(By.xpath(LoctorValue)).getAttribute("value");	
}

if(LocatorType.equalsIgnoreCase("id"))
{
supplerNum = driver.findElement(By.id(LoctorValue)).getAttribute("value");	
}

if(LocatorType.equalsIgnoreCase("name"))
{
supplerNum = driver.findElement(By.name(LoctorValue)).getAttribute("value");	
}
//write supplier number into notepad
FileWriter fw = new FileWriter("./CaptureData/Supplier.txt");
BufferedWriter bw = new BufferedWriter(fw);
bw.write(supplerNum);
bw.flush();
bw.close();
}
//method for supplier table
public static void supplierTable() throws Throwable
{
  //read supplier Number from notepad
	FileReader fr = new FileReader("./CaptureData/Supplier.txt");
	BufferedReader br = new BufferedReader(fr);
	String Exp_Data = br.readLine();
	if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
		// click search panel is search textbox not displayed
	driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
	//clear text into textbox
	driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
	driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
	Thread.sleep(2000);
	driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
	Thread.sleep(3000);
	String Act_data =driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[6]//div/span/span/")).getText();
	Reporter.log(Act_data+"=========="+Exp_Data,true);
	try {
	Assert.assertEquals(Act_data, Exp_Data,"Supplier is not Matching" );
	} catch (AssertionError a) 
	{
		System.out.println(a.getMessage());
	}
}
//method for capture customer number into note pad
public static void  capturecus (String LocatorType, String LocatorValue)throws Throwable
{
	String customerNum ="";
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		customerNum = driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
	}
	if(LocatorType.equalsIgnoreCase("id"))
	{
		customerNum = driver.findElement(By.id(LocatorValue)).getAttribute("value");
	}
	if(LocatorType.equalsIgnoreCase("name"))
	{
		customerNum = driver.findElement(By.name(LocatorValue)).getAttribute("value");
	}
	//write supplier number into notepad
	FileWriter fw = new FileWriter("./CaptureData/Supplier.txt");
	BufferedWriter bw = new BufferedWriter(fw);
	bw.write(customerNum);
	bw.flush();
	bw.close();
}
//method customer table
public static void customerTable() throws Throwable
{
//read supplier Number from notepad
	FileReader fr = new FileReader("./CaptureData/customer.txt");
	BufferedReader br = new BufferedReader(fr);
	String Exp_Data = br.readLine();
	if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
		// click search panel is search textbox not displayed
	driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
	//clear text into textbox
	driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
	driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
	Thread.sleep(2000);
	driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
	Thread.sleep(3000);
	String Act_data =driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[5]//div/span/span/")).getText();
	Reporter.log(Act_data+"=========="+Exp_Data,true);
	try {
	Assert.assertEquals(Act_data, Exp_Data,"Customer is not Matching" );
	} catch (AssertionError a) 
	{
		System.out.println(a.getMessage());
	}

}
}
