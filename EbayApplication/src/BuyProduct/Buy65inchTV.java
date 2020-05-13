package BuyProduct;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.javascript.host.ScreenOrientation;
import com.gargoylesoftware.htmlunit.javascript.host.Set;
import com.google.common.collect.ImmutableMap;

import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Buy65inchTV {

	/*****************************************
	 * Author: Don Kaduppil Thankachan       *
	 * Date Written: 14-May-2020             *
	 *****************************************/

	AndroidDriver driver; 
	ScreenOrientation orientation;
	String Title=null;

	@BeforeTest
	public void testEbay() throws InterruptedException, IOException{
		//-----------------------------------------------------------------------------------------------------------------------------------------------//
		//getting device info from properties file

		Properties p1 = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"\\fileinputstream\\deviceinfo.properties");
		p1.load(fis);
		//-----------------------------------------------------------------------------------------------------------------------------------------------//
		//defining capabilities of phone using to test app

		DesiredCapabilities capability=new DesiredCapabilities();
		capability.setCapability("deviceName", p1.getProperty("deviceName"));
		capability.setCapability("platformVersion", p1.getProperty("platformVersion"));
		capability.setCapability("platformName", p1.getProperty("platformName"));
		/*File fpath=new File(System.getProperty("user.dir")+"\\apk\\Amazon_shopping.apk");
		capability.setCapability("app",fpath.getAbsolutePath());*/

		capability.setCapability("appPackage", "com.amazon.mShop.android.shopping");
		capability.setCapability("appActivity", "com.amazon.mShop.splashscreen.StartupActivity");

		driver = new AndroidDriver(new URL(p1.getProperty("AppiumURL")),capability);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	//-----------------------------------------------------------------------------------------------------------------------------------------------//

	@Test
	//Login test case
	public void login() throws BiffException, IOException, InterruptedException{

		driver.findElementById("com.amazon.mShop.android.shopping:id/sign_in_button").click();

		//reading login credentials from excel

		Workbook wb = Workbook.getWorkbook(new File(System.getProperty("user.dir")+"\\usercredentials\\usercredentials.xls"));
		Sheet sh = wb.getSheet(0);
		int y = sh.getRows();

		String username=null;
		String password=null;
		for (int i = 1; i < y; i++)    
		{
			username =  sh.getCell(0, i).getContents();    
			password =  sh.getCell(1, i).getContents();   
		}		
		Thread.sleep(3000);

		//entering user name 

		driver.findElementByClassName("android.widget.EditText").sendKeys(username);
		Thread.sleep(1000);
		driver.findElementByClassName("android.widget.Button").click();

		//entering password
		driver.findElementByClassName("android.widget.CheckBox").click();
		Thread.sleep(3000);
		driver.findElementByClassName("android.widget.EditText").sendKeys(password);
		Thread.sleep(1000);
		driver.findElementByClassName("android.widget.Button").click();
		Thread.sleep(3000);
	}
	//------------------------------------------------------------------------------------------------------------------------------------------------//
	//search and select TV test case

	@Test(dependsOnMethods = {"login"})
	public void searchTV() throws InterruptedException{
		driver.findElement(By.id("com.amazon.mShop.android.shopping:id/rs_search_src_text")).sendKeys("65-inch TV");

		Actions a = new Actions(driver);
		a.sendKeys(Keys.ENTER).build().perform();
		Thread.sleep(3000);

		//-------------------------------------------------------------------------------------------//	
		//swipe down

		/*	int screenwidth = driver.manage().window().getSize().getWidth();
		int screeheight = driver.manage().window().getSize().getHeight();
		int xStrat= screenwidth/2;
		int xEnd = screenwidth/2;
		int yStart = screeheight*8/10;
		int yEnd = screeheight/10;

		driver.swipe(xStrat, yStart, xEnd, yEnd, 5000);*/

		//---------------------------------------------------------------------------------------------------------------------------------------//

		//scrolling
		//driver.scrollTo("Samsung 65").click();

		//---------------------------------------------------------------------------------------------------------------------------------------//

			//clicking TV at index of 23

			List<WebElement> tv = driver.findElementsByClassName("android.view.View");
			Title = tv.get(23).getText();
			System.out.println(tv.get(23).getText());
			tv.get(23).click();
            Thread.sleep(5000);
			//---------------------------------------------------------------------------------------------------------------------------------------//
			//changing screen orientation to landscape

			/*orientation = ScreenOrientation.LANDSCAPE;
		driver.execute(DriverCommand.SET_SCREEN_ORIENTATION,ImmutableMap.of("orientation",orientation.value().toUppercase()));*/

			//changing screen orientation to portrait

			/*orientation = ScreenOrientation.PORTRAIT;
		driver.execute(DriverCommand.SET_SCREEN_ORIENTATION,ImmutableMap.of("orientation",orientation.value().toUppercase()));*/

			//---------------------------------------------------------------------------------------------------------------------------------------//

			//finding Title of the product

			List <WebElement> details = driver.findElementsByClassName("android.webkit.WebView");
			String title = details.get(0).getText();
			Thread.sleep(2000);

		}
		//---------------------------------------------------------------------------------------------------------------------------------------//
		//zooming image

		/*WebElement image = driver.findElementByXPath("//img[@id='main-image']");
		image.click();
		driver.zoom(image);*/

		//--------------------------------------------------------------------------------------------------------------------------------------//
 
	    //Add to cart and finding title and price
	
		@Test(dependsOnMethods = {"login","searchTV"})
		public void addToCart() throws InterruptedException{
			List <WebElement> element = driver.findElementsByClassName("android.view.View");
			String title1 = element.get(14).getText();
			String price1 = element.get(40).getText().trim();
			
			Thread.sleep(2000);

			System.out.println(price1);
			System.out.println(title1);
			Thread.sleep(2000);

			//verifying title
			Assert.assertEquals(title1, Title);
			
            //clicking on add to cart button
			driver.findElementByClassName("android.widget.Button").click();
			Thread.sleep(2000);
			
            //clicking on cart
			driver.findElementByClassName("android.widget.TextView").click();
			Thread.sleep(2000);

			List<WebElement> element2 = driver.findElementsByClassName("android.widget.TextView");
			String price2 = element2.get(2).getText();
			System.out.println(price2);
				
			//verifying price
			Assert.assertEquals(price2, price1);
			
			//clicking on proceed to checkout
			driver.findElementByClassName("android.widget.Button").click();
			Thread.sleep(2000);
		}
//-----------------------------------------------------------------------------------------------------------------------------------------------//
		//checkout
		
		@Test(dependsOnMethods = {"login","searchTV","addToCart"})
		public void checkout() throws BiffException, IOException, InterruptedException{
			
			//reading details from file
			
			//reading login credentials from excel

			Workbook wb = Workbook.getWorkbook(new File(System.getProperty("user.dir")+"\\usercredentials\\usercredentials.xls"));
			Sheet sh = wb.getSheet(1);
			int y = sh.getRows();

			String Fullname=null;
			String phonenum=null;
			String Address1=null;
			String Address2=null;
			String postcode=null;
			String city=null;
			String state=null;
			
			for (int i = 1; i < y; i++)    
			{
				 Fullname =  sh.getCell(0, i).getContents();    
				 phonenum =  sh.getCell(1, i).getContents();   
				 Address1 =  sh.getCell(2, i).getContents();    
				 Address2 =  sh.getCell(3, i).getContents();
				 postcode =  sh.getCell(4, i).getContents();    
				 city =  sh.getCell(5, i).getContents();   
				 state =  sh.getCell(6, i).getContents();
			}		
			Thread.sleep(3000);
			
			//sending address information
			
			List<WebElement> details = driver.findElementsByClassName("android.widget.EditText");
			
			details.get(0).sendKeys(Fullname);
			details.get(1).sendKeys(phonenum);
			details.get(2).sendKeys(Address1);
			details.get(3).sendKeys(Address2);
			details.get(4).sendKeys(postcode);
			List<WebElement> suburb = driver.findElementsByClassName("android.widget.ListView");
			for(WebElement suburbs: suburb){
				if(suburbs.getText().equals(city))
				{
					suburbs.click();
					break;
				}
			}
			 
			details.get(6).sendKeys(state);
			
			//checkout
			
			Thread.sleep(2000);
			driver.findElementByClassName("android.widget.Button").click();
			Thread.sleep(2000);
		}
//-----------------------------------------------------------------------------------------------------------------------------------------------//
       //After Test
		@AfterTest
		public void quitapp(){
			driver.quit();
		}
	}
//----------------------------------------------------------------------------------------------------------------------------------------------//