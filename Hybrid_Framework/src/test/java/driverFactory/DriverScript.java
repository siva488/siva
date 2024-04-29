package driverFactory;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

import commonFunctions.FunctionLibrary;
import utilities.ExcelFileUtil;

public class DriverScript {
	public WebDriver driver;
	String inputpath ="./FileInput/Controller.xlsx";
	String outputpath ="./FileOutput/HybridResults.xlsx";
	ExtentReports report;
	ExtentTest logger;
	String TestCases ="MasterTestCases";
	
	public void startTest() throws Throwable
	{
		String Module_Status ="";
		//create object for Excelfile util class
		ExcelFileUtil xl = new ExcelFileUtil(inputpath);
		//iterate all rows in testcases sheet
		for(int i=1;i<=xl.rowCount(TestCases);i++)
		{
			//read cell
			String Execution_Status =xl.getCellData(TestCases, i, 2);
			if(Execution_Status.equalsIgnoreCase("Y"))
			{
				//store all Modules into variable
				String TCModule =xl.getCellData(TestCases, i, 1);
				//iterate all rows in TCModule
				for(int j=1;j<=xl.rowCount(TCModule);j++)
				{
					//read all cells from TCModule
					String Description =xl.getCellData(TCModule, j, 0);
					String Object_type = xl.getCellData(TCModule, j, 1);
					String Locator_Type = xl.getCellData(TCModule, j, 2);
					String Locator_Value = xl.getCellData(TCModule, j, 3);
					String Test_Data = xl.getCellData(TCModule, j, 4);
					try {
						if(Object_type.equalsIgnoreCase("startBrowser"))
						{
							driver =FunctionLibrary.startBrowser();
						}
						if(Object_type.equalsIgnoreCase("openUrl"))
						{
							FunctionLibrary.openUrl(driver);
						}
						if(Object_type.equalsIgnoreCase("waitForElement"))
						{
							FunctionLibrary.waitForElement(driver, Locator_Type, Locator_Value, Test_Data);
						}
						if(Object_type.equalsIgnoreCase("typeAction"))
						{
							FunctionLibrary.typeAction(driver, Locator_Type, Locator_Value, Test_Data);
						}
						if(Object_type.equalsIgnoreCase("clickAction"))
						{
							FunctionLibrary.clickAction(driver, Locator_Type, Locator_Value);
						}
						if(Object_type.equalsIgnoreCase("validateTitle"))
						{
							FunctionLibrary.validateTitle(driver, Test_Data);
						}
						if(Object_type.equalsIgnoreCase("closeBrowser"))
						{
							FunctionLibrary.closeBrowser(driver);
						}
						if(Object_type.equalsIgnoreCase("mouseClick"))
						{
							FunctionLibrary.mouseClick(driver);
						}
						if(Object_type.equalsIgnoreCase("categoryTable"))
						{
							FunctionLibrary.categoryTable(driver, Test_Data);
						}
						//write as pass into TCModule
						xl.setCellData(TCModule, j, 5, "Pass", outputpath);
						Module_Status="True";
					}catch(Exception e)
					{
						//write as Fail into TCModule
						xl.setCellData(TCModule, j, 5, "Fail", outputpath);
						Module_Status="False";
						System.out.println(e.getMessage());
					}
					if(Module_Status.equalsIgnoreCase("true"))
					{
						//write as pass into Testcases
						xl.setCellData(TestCases, i, 3, "Pass", outputpath);
					}
					if(Module_Status.equalsIgnoreCase("False"))
					{
						//write as Fail into Testcases
						xl.setCellData(TestCases, i, 3, "Fail", outputpath);
					}
				}
			}
			else
			{
				//write as blocked into Test cases sheet for flag N
				xl.setCellData(TestCases, i, 3, "Blocked", outputpath);
			}
		}
	}
}
