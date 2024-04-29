package commonFunctions;

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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

public class FunctionLibrary {
	public static WebDriver driver;
	public static Properties conpro;
	//method for launch browser
	public static WebDriver startBrowser()throws Throwable
	{
		conpro= new Properties();
		conpro.load(new FileInputStream("./PropertyFiles/Environment.properties"));
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
			Reporter.log("Browser Value is Not Matching",true);
		}
		return driver;
	}
	//method for launch url
	public static void openUrl(WebDriver driver)
	{
		driver.get(conpro.getProperty("Url"));
	}
	//method for any webelement wait time
	public static void waitForElement(WebDriver driver,String Locator_Type,String Locator_Value,String Test_Data)
	{
		WebDriverWait myWait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(Test_Data)));
		if(Locator_Type.equalsIgnoreCase("id"))
		{
			myWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(Locator_Value)));
		}
		if(Locator_Type.equalsIgnoreCase("name"))
		{
			myWait.until(ExpectedConditions.visibilityOfElementLocated(By.name(Locator_Value)));
		}
		if(Locator_Type.equalsIgnoreCase("xpath"))
		{
			myWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locator_Value)));
		}
	}
	//method for textboxes
	public static void typeAction(WebDriver driver,String Locator_Type,String Locator_Value,String Test_Data)
	{
		if(Locator_Type.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(Locator_Value)).clear();
			driver.findElement(By.name(Locator_Value)).sendKeys(Test_Data);
		}
		if(Locator_Type.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(Locator_Value)).clear();
			driver.findElement(By.id(Locator_Value)).sendKeys(Test_Data);
		}
		if(Locator_Type.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(Locator_Value)).clear();
			driver.findElement(By.xpath(Locator_Value)).sendKeys(Test_Data);
		}
	}
	//method for click action
	public static void clickAction(WebDriver driver,String Locator_type,String Locator_Value)
	{
		if(Locator_type.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(Locator_Value)).click();
		}
		if(Locator_type.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(Locator_Value)).click();
		}
		if(Locator_type.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(Locator_Value)).sendKeys(Keys.ENTER);
		}
	}
	public static void validateTitle(WebDriver driver,String Expected_Title)
	{
		String Actual_Title = driver.getTitle();
		try {
			Assert.assertEquals(Actual_Title, Expected_Title, "Title is Not Matching");
		}catch(Throwable t)
		{
			System.out.println(t.getMessage());
		}
	}
	public static void closeBrowser(WebDriver driver)
	{
		driver.quit();
	}
	public static String generateDate()
	{
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("YYYY_MM_dd hh_mm_ss");
		return df.format(date);
	}
	//method for mouse click
	public static void mouseClick(WebDriver driver) throws Throwable
	{
		Actions ac = new Actions(driver);
		ac.moveToElement(driver.findElement(By.xpath("//a[starts-with(text(),'Stock Items ')]")));
		ac.perform();
		Thread.sleep(2000);
		ac.moveToElement(driver.findElement(By.xpath("(//a[contains(text(),'Stock Categories')])[2]")));
		ac.pause(3000).click().perform();
	}
	//method for categoryTable
	public static void categoryTable(WebDriver driver,String Exp_Data) throws Throwable
	{
		if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
			driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
		driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
		Thread.sleep(2000);
		String Act_Data= driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[4]/div/span/span")).getText();
		Reporter.log(Exp_Data+"       "+Act_Data,true);
		try {
			Assert.assertEquals(Exp_Data, Act_Data,"Category Name Not Matching");
		}catch(Throwable t)
		{
			System.out.println(t.getMessage());
		}
	}

public static void dropDownAction(WebDriver driver,String Locator_Type,String Locator_Value,String Test_Data)
{
	if(Locator_Type.equalsIgnoreCase("id"))
	{
		int value =Integer.parseInt(Test_Data);
		WebElement element =driver.findElement(By.id(Locator_Value));
		Select select = new Select(element);
		select.selectByIndex(value);
	}
	if(Locator_Type.equalsIgnoreCase("xpath"))
	{
		int value =Integer.parseInt(Test_Data);
		WebElement element =driver.findElement(By.xpath(Locator_Value));
		Select select = new Select(element);
		select.selectByIndex(value);
	}
	if(Locator_Type.equalsIgnoreCase("name"))
	{
		int value =Integer.parseInt(Test_Data);
		WebElement element =driver.findElement(By.name(Locator_Value));
		Select select = new Select(element);
		select.selectByIndex(value);
	}
}
//method for capturing stock number into note pad
public static void capturestock(WebDriver driver,String Locator_Type,String Locator_Value)throws Throwable
{
	String StockNumber ="";
	if(Locator_Type.equalsIgnoreCase("name"))
	{
		StockNumber = driver.findElement(By.name(Locator_Value)).getAttribute("value");
	}
	if(Locator_Type.equalsIgnoreCase("xpath"))
	{
		StockNumber = driver.findElement(By.xpath(Locator_Value)).getAttribute("value");	
	}
	if(Locator_Type.equalsIgnoreCase("id"))
	{
		StockNumber = driver.findElement(By.id(Locator_Value)).getAttribute("value");
	}
	FileWriter fw = new FileWriter("./CaptureData/stocknumber.txt");
	BufferedWriter bw = new BufferedWriter(fw);
	bw.write(StockNumber);
	bw.flush();
	bw.close();
	
}
//method for stock table validation
public static void stockTable(WebDriver driver)throws Throwable
{
	//read stock number from notepad
	FileReader fr = new FileReader("./CaptureData/stocknumber.txt");
	BufferedReader br = new BufferedReader(fr);
	String Exp_Data=br.readLine();
	if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
		driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
	driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
	driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
	driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
	Thread.sleep(2000);
	String Act_Data =driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[8]/div/span/span")).getText();
	Reporter.log(Exp_Data+"      "+Act_Data,true);
	try {
	Assert.assertEquals(Exp_Data, Act_Data, "Stock Number is Not Matching");
	}catch(Throwable t)
	{
		System.out.println(t.getMessage());
	}
}
//method for capture supplier number
public static void capturesupplier(WebDriver driver,String Locator_type,String Locator_value)throws Throwable
{
	String supplierNumber ="";
	if(Locator_type.equalsIgnoreCase("name"))
	{
		supplierNumber = driver.findElement(By.name(Locator_value)).getAttribute("value");
	}
	if(Locator_type.equalsIgnoreCase("id"))
	{
		supplierNumber = driver.findElement(By.id(Locator_value)).getAttribute("value");
	}
	if(Locator_type.equalsIgnoreCase("xpath"))
	{
		supplierNumber = driver.findElement(By.xpath(Locator_value)).getAttribute("value");
	}
	FileWriter fw = new FileWriter("./CaptureData/suppliernumber.txt");
	BufferedWriter bw = new BufferedWriter(fw);
	bw.write(supplierNumber);
	bw.flush();
	bw.close();
}
//method for supplier table
public static void supplierTable(WebDriver driver)throws Throwable
{
	FileReader fr = new FileReader("./CaptureData/suppliernumber.txt");
	BufferedReader br = new BufferedReader(fr);
	String Exp_Data =br.readLine();
	if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
		driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
	driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
	driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
	driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
	Thread.sleep(2000);
	String Act_Data = driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[6]/div/span/span")).getText();
	Reporter.log(Exp_Data+"     "+Act_Data,true);
	try {
	Assert.assertEquals(Exp_Data, Act_Data, "Supplier number Not Matching");
	}catch(Throwable t)
	{
		System.out.println(t.getMessage());
	}
}
}
	
	
