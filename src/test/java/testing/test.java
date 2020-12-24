package testing;




import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Sheet;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


import Utilities.Logimp;
import Utilities.ReadExcel;
//import jdk.internal.jline.internal.Log;

import TestReports.TestReports;
import Utilities.TestUtil;
import testing.KeywordsApp;



public class test {
	
	public static Random randomGenerator = new Random(); 
	public static String currentTest;
	public static String currentTSID;
	public static String TestDescription;
	public static String TestData;
	public static String stepDescription;
	public static String proceedOnFail;
	public static String testStatus;
	public static int  testRepeat;
//	public static int nSelPort;
	//public static String sSelPort;
	public static Calendar cal = new GregorianCalendar();
	public static  int month = cal.get(Calendar.MONTH);
	public static int year = cal.get(Calendar.YEAR);
	public static  int sec =cal.get(Calendar.SECOND);
	public static  int min =cal.get(Calendar.MINUTE);
	public static  int date = cal.get(Calendar.DATE);
	public static  int day =cal.get(Calendar.HOUR_OF_DAY);
	public static String strDate;
	public static String result;
	public static String mailresult=" - Script successfully executed - no errors found";
	public static RemoteWebDriver driver = null;
	//public static WebDriver driver = null;
	public static String keyword;
	public static String object;
	
	



	//Get the current system time - used for generated unique file ids (ex: Screenshots, Reports etc on every test run)
	public static String getCurrentTimeStamp()
    { 
          SimpleDateFormat CurrentDate = new SimpleDateFormat("MM-dd-yyyy"+"_"+"HH-mm-ss");
          Date now = new Date(); 
         String CDate = CurrentDate.format(now); 
          return CDate; 
    }
	
   	@BeforeSuite
	public void startTesting() throws Exception{
   		
   		// Code to Generate random numbers
   		
	//	 nSelPort = randomGenerator.nextInt(40000);
		 strDate=getCurrentTimeStamp();
     	System.out.println("date time stamp :"+strDate);
		 
		 // Start testing method will start generating the Test Reports from the beginning       
     	TestReports.startTesting(System.getProperty("user.dir")+"\\src\\TestReports" +strDate+ ".html",TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"),"3.1");
		
		
      
		
	}
	
	//public static Logger log = Logger.getLogger(test.class);
//	public static void main(String[] args) {
		// TODO Auto-generated method stub
		@Test(dataProvider="getData",threadPoolSize=3)
		public void testlaunch (String TestCase, String description, String browserval) throws InterruptedException, IOException {

			int i = 1;		
			String startTime=null;
			TestReports.startSuite("Suite " + String.valueOf(i));
			i = i +1;
			URL url = new URL("http://localhost:4444/wd/hub");
		try {
			// log messages
			Logimp log = new Logimp();
			// Read excel file
	    	
	    startTime=TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa");
	    
	    TestCase = TestCase.substring(0, (TestCase.length() -3));
	    description = description.substring(0, (description.length() -3));
	    browserval = browserval.substring(0, (browserval.length() -3));
	    
	    
	    if (browserval.equals("firefox")) {
			System.setProperty("webdriver.gecko.driver", "D:\\Gecko Driver\\geckodriver.exe");
	    	FirefoxOptions options1 = new FirefoxOptions();
	    	options1.setCapability(CapabilityType.PLATFORM_NAME, Platform.WINDOWS);
	    	options1.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
	    	options1.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
	    	driver = new RemoteWebDriver(url, options1);
	    	
	    	/*cap.merge(options);	
	    	cap.merge(options);*/
	    //	System.out.println("this is test testng xslt");	
		//	DesiredCapabilities cap = DesiredCapabilities.firefox();
			// cap.setCapability("gecko", true);
			//cap.setBrowserName("firefox");
			//cap.setPlatform(Platform.ANY);
	    	//cap = new DesiredCapabilities();
			//options.merge(cap);
		//	 driver= new FirefoxDriver();
	    	System.out.println("The thread ID for Firefox is "+ Thread.currentThread().getId());
			
		}else if (browserval.equals("chrome")) {
			ChromeOptions options = new ChromeOptions();
			options.setCapability(CapabilityType.PLATFORM_NAME, Platform.WINDOWS);
			options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
			options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			options.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
			System.out.println("The thread ID for chrome is "+ Thread.currentThread().getId());
			//cap.setCapability(ChromeOptions.CAPABILITY, options);
			driver = new RemoteWebDriver(url, options);
		    
			 	
		}
	    //	log.logmessagetofile("Desired capabilities");
	    	
	    	//RemoteWebDriver driver = new RemoteWebDriver(url, cap);
	    currentTest = TestCase;
	  //  currentTSID=sndkey;
		TestDescription=description;

			 // Thread.sleep(5000);
			driver.manage().window().maximize();
	    
			driver.manage().timeouts().pageLoadTimeout(300,TimeUnit.SECONDS);
			ReadExcel DataExcelFile = new ReadExcel();
			//Prepare the path of excel file
			String filePath = System.getProperty("user.dir")+"\\src\\test\\java\\testdata";

	    //Call read file method of the class to read data
			Sheet sheetName = DataExcelFile.readExcelrows(filePath,"Core.xlsx",currentTest);
			int rowcount = sheetName.getLastRowNum() +1;
			for (int steprowcount =1;steprowcount<=(rowcount -1);steprowcount++) {
		    	
		    		keyword=DataExcelFile.getCellData(sheetName, steprowcount, 2);
		    		if (keyword != null) {
		    			keyword = keyword.substring(0, (keyword.length() -3));
		    		}
					object=DataExcelFile.getCellData(sheetName, steprowcount, 3);
					if (object != null) {
						object = object.substring(0, (object.length() -3));
					}
					currentTSID=DataExcelFile.getCellData(sheetName, steprowcount, 0);
					currentTSID = currentTSID.substring(0, (currentTSID.length() -3));
					stepDescription=DataExcelFile.getCellData(sheetName, steprowcount, 1);
					stepDescription = stepDescription.substring(0, (stepDescription.length() -3));
					TestData = DataExcelFile.getCellData(sheetName, steprowcount, 5);
					if (TestData != null) {
						TestData = TestData.substring(0, (TestData.length() -3));
					}
					System.out.println(keyword);
					Method method= KeywordsApp.class.getMethod(keyword);
					result = (String)method.invoke(method);
					if (result == "Pass") {
						  TestReports.addKeyword(stepDescription,keyword , result, "No Screen shot for pass values");
						  testStatus = result;
					  }
					  else if (result == "Fail") {
						  TestUtil.captureScreenshot();
						  TestReports.addKeyword(stepDescription,keyword , result, TestUtil.imageName); 
						  testStatus = result;
						  
					  }
					  
					 
			}	
			
			//driver.get("https://www.google.com/");
			  	
			//  PageExam pgobject = PageFactory.initElements(driver, PageExam.class);
			  //driver.findElement(By.xpath("//input[@name='q']")).sendKeys(learnkey);
			//  WebDriverWait wait = new WebDriverWait(driver, 5000); 
		      //wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='q']")));
		    //  driver.findElement(By.xpath("//input[@name='q']")).sendKeys(sndkey);
			 // driver.findElement(By.("//input[@name='q']")).sendKeys(dispkey);
			  //pgobject.searchgoog.sendKeys(sndkey);
			  //pgobject.searchgoog.sendKeys(dispkey);
			  //log.logmessagetofile("The messages are "+ sndkey +" Second key is " + dispkey + " Browser Name "+browserval);
			 driver.close();
			 
				TestReports.addTestCase(TestDescription, currentTest, 
						TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"), 
						TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"),
						testStatus );
		}
		catch(Throwable t) {
			System.out.println(t.getMessage());
		}
			
		} 
		
		
		@AfterSuite
		public static void endScript() throws Exception{

			TestReports.endSuite();
			
			// Once the test is completed update the end time in HTML report
			TestReports.updateEndTime(TestUtil.now("dd.MMMMM.yyyy hh.mm.ss aaa"));
			
		
			
			
			// Sending Mail After Execution of All TestCases ON HOLD AND RECOMENDED ONLY FOR COMPLETE BVT
			
			//monitoringMail.sendMail(TestConfig.server, TestConfig.from, TestConfig.to, TestConfig.subject+mailresult, TestConfig.messageBody, TestConfig.attachmentPath, TestConfig.attachmentName);
			
			//or
			
			//	monitoringMail.sendMail(TestConfig.server, TestConfig.from, TestConfig.to, TestConfig.subject, TestConfig.messageBody, TestConfig.attachmentPath, TestConfig.attachmentName);
			
		//driver.quit();
			

		} 
		
		@DataProvider(parallel=true)
		public Object[][] getData(){
			 Object[][] data = null;
			 ArrayList<String> TestNamelist = new ArrayList<String>();
			 ArrayList<String> Testdesclist = new ArrayList<String>();
			 ArrayList<String> TestbrowList = new ArrayList<String>();
			
			try {
			
				ReadExcel objExcelFile = new ReadExcel();

			    //Prepare the path of excel file
			    String filePath = System.getProperty("user.dir")+"\\src\\test\\java\\testdata";

			    //Call read file method of the class to read data
			    Sheet sheetName = objExcelFile.readExcelrows(filePath,"Core.xlsx","Suite1");
				
			    int rowcount = sheetName.getLastRowNum() +1;
			    data = new Object[rowcount][3];
			    for (int i =1;i<=(rowcount - 1);i++) {
			    	System.out.println(objExcelFile.getCellData(sheetName, i, 3));
			    	String canRun = objExcelFile.getCellData(sheetName, i, 3);
			    	if (canRun.contains("Y")) {
			    		TestNamelist.add(objExcelFile.getCellData(sheetName, i, 0));
			    		Testdesclist.add(objExcelFile.getCellData(sheetName, i, 1));
			    		TestbrowList.add(objExcelFile.getCellData(sheetName, i, 2));
			    	} // if
			    }
			   // objExcelFile.getCellData(sheetName, 0, 1);
			    data = new Object[TestNamelist.size()][3];
				
			    for (int datarow =0; datarow <=(TestNamelist.size() -1);datarow++) {
			    	data[datarow][0]=TestNamelist.get(datarow);
			    	data[datarow][1]=Testdesclist.get(datarow);
			    	data[datarow][2] = TestbrowList.get(datarow);
			    }
				
			//first row
			    /*	data[0][0] = "this is to test1 row 1 col";
			data[0][1] = "this is to test 1row 2 col";
			data[0][2] = "chrome";
			data[0][3] = "Pass";
			data[0][4] = "1st Data Row";
			//second row
			data[1][0] = "this is to test 2 row 1 col";
			data[1][1] = "this is to test2row 2 col";
			data[1][2] = "firefox";
			data[1][3] = "Pass";
			data[1][4] = "2nd Data Row";
			// Thirdrow
			data[2][0] = "this is to test 3 row 1 col";
			data[2][1] = "this is to test 3 row 2 col";
			data[2][2] = "firefox";
			data[2][3] = "Pass";
			data[2][4] = "3rd Data Row";
			
			// 4row
			data[3][0] = "this is to test 4 row 1 col";
			data[3][1] = "this is to test 4 row 2 col";
			data[3][2] = "chrome";
			data[3][3] = "Pass";
			data[3][4] = "4th Data Row";
						// 5
					data[4][0] = "this is to test 5 row 1 col";
			data[4][1] = "this is to test 5 row 2 col";
			data[4][2] = "firefox";
			data[4][3] = "Fail";
			data[4][4] = "5th Data Row";
						// 6
			data[5][0] = "this is to test 6 row 1 col";
			data[5][1] = "this is to test 6 row 2 col";
			data[5][2] = "chrome";
			data[5][3] = "Pass";
			data[5][4] = "6th Data Row";
						// 7
			data[6][0] = "this is to test 7 row 1 col";
			data[6][1] = "this is to test 7 row 2 col";
			data[6][2] = "firefox";
			data[6][3] = "Fail";
			data[6][4] = "7th Data Row";
						// 8
			data[7][0] = "this is to test 8 row 1 col";
			data[7][1] = "this is to test 8 row 2 col";
			data[7][2] = "chrome";
			data[7][3] = "Pass";
			data[7][4] = "8th Data Row";
						// 9
			data[8][0] = "this is to test 9 row 1 col";
			data[8][1] = "this is to test 9 row 2 col";
			data[8][2] = "firefox";
			data[8][3] = "Fail";
			data[8][4] = "9th Data Row";
						// 10
			data[9][0] = "this is to test 10 row 1 col";
			data[9][1] = "this is to test 10 row 2 col";
			data[9][2] = "chrome";
			data[9][3] = "Pass";
			data[9][4] = "10th Data Row";
						// 11
			data[10][0] = "this is to test 11 row 1 col";
			data[10][1] = "this is to test 11 row 2 col";
			data[10][2] = "firefox";
			data[10][3] = "Fail";
			data[10][4] = "11th Data Row";
						// 12
			data[11][0] = "this is to test 12 row 1 col";
			data[11][1] = "this is to test 12 row 2 col";
			data[11][2] = "chrome";
			data[11][3] = "Pass";
			data[12][4] = "12th Data Row";
						// 13
			data[12][0] = "this is to test 13 row 1 col";
			data[12][1] = "this is to test 13 row 2 col";
			data[12][2] = "firefox";
			data[12][3] = "Fail";
			data[13][4] = "13th Data Row";*/
			}
			catch(Throwable t) {
				System.out.println(t.getMessage());
			}
			
			return data;
}

}
