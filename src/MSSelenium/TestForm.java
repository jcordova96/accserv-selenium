package MSSelenium;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


public class TestForm extends Tester {
	
	public void run() {
		
//		testAddToCartCheckout();
		
		
		test1();
		
	}
		
	
	public void test1() {
		
		int retries = 10;
		int delay = 1500;
		
		String baseUrlBe = "https://stagingmanage.accountabilityservices.com/";
		String baseUrlFe = "https://stagingorders.accountabilityservices.com/";
		
		setPageUrl(baseUrlBe);
		sleep(delay);
		
		WebElement elem = driver.findElement(By.xpath("//*[@id='loginform-username']"));
		elem.sendKeys("dataskills");
		elem = driver.findElement(By.xpath("//*[@id='loginform-password']"));
		elem.sendKeys("5eren1ty");
		By by = By.xpath("//button[@name='login-button']");
		elem = (new WebDriverWait(driver, retries))
				   .until(ExpectedConditions.elementToBeClickable(by));
		sleep(delay);
		elem.click();
		
		by = By.linkText("Accounts");
		elem = (new WebDriverWait(driver, retries))
				   .until(ExpectedConditions.elementToBeClickable(by));
		sleep(delay);
		elem.click();
		
		
		sleep(3000);
		
		elem = driver.findElement(By.xpath("//*[@name='AccountSearch[user.email]']"));
		elem.sendKeys("test1@testing.com");
		elem.sendKeys(Keys.TAB);
		
		by = By.linkText("login");
		elem = (new WebDriverWait(driver, retries))
				   .until(ExpectedConditions.elementToBeClickable(by));
		sleep(delay);
		elem.click();
		sleep(delay);
		
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
	    driver.switchTo().window(tabs.get(1));
		
		by = By.xpath("//a[contains(@href,'V01TESTA')][1]");
		elem = (new WebDriverWait(driver, retries))
				   .until(ExpectedConditions.elementToBeClickable(by));
		sleep(delay);
		elem.click();		

		//------------------------------------------------------------------------>
		// Fill form
		
		by = By.xpath("(//*[@type='radio'][@name='IndividualReturn2018[healthCare]'])[1]");
		elem = (new WebDriverWait(driver, retries))
				   .until(ExpectedConditions.elementToBeClickable(by));
		this.scrollIntoView(elem);
		sleep(300);
		elem.click();
		sleep(300);
		
		by = By.xpath("//input[@type='checkbox'][@name='IndividualReturn2018[directDepositMyReturn]']");
		elem = (new WebDriverWait(driver, retries))
				   .until(ExpectedConditions.elementToBeClickable(by));
		this.scrollIntoView(elem);
		sleep(300);
		elem.click();
		sleep(300);
		
		by = By.xpath("//input[@type='checkbox'][@name='IndividualReturn2018[payAllEstimatedTaxFromBank]']");
		elem = (new WebDriverWait(driver, retries))
				   .until(ExpectedConditions.elementToBeClickable(by));
		this.scrollIntoView(elem);
		sleep(300);
		elem.click();
		sleep(300);
		
		by = By.xpath("(//*[@type='radio'][@name='IndividualReturn2018[bankInfoOptionId]'])[2]");
		elem = (new WebDriverWait(driver, retries))
				   .until(ExpectedConditions.elementToBeClickable(by));
		this.scrollIntoView(elem);
		sleep(300);
		elem.click();
		sleep(300);
		
		by = By.xpath("//*[@type='text'][@name='IndividualReturn2018[bankName]']");
		elem = driver.findElement(by);
		this.scrollIntoView(elem);
		elem.sendKeys("Bank of America");
		sleep(300);
		
		by = By.xpath("//*[@type='text'][@name='IndividualReturn2018[routingNumber]']");
		elem = driver.findElement(by);
		this.scrollIntoView(elem);
		elem.sendKeys("211370545");
		sleep(300);
		
		by = By.xpath("//*[@type='text'][@name='IndividualReturn2018[bankAccountNumber]']");
		elem = driver.findElement(by);
		this.scrollIntoView(elem);
		elem.sendKeys("12345678");
		sleep(300);
		
		by = By.xpath("//select[@name='IndividualReturn2018[bankAccountTypeId]']");
		Select dropdown = new Select(driver.findElement(by));
		dropdown.selectByIndex(2);
		
		by = By.xpath("(//*[@type='radio'][@name='IndividualReturn2018[havePersonalBusiness]'])[2]");
		elem = (new WebDriverWait(driver, retries))
				   .until(ExpectedConditions.elementToBeClickable(by));
		this.scrollIntoView(elem);
		sleep(300);
		elem.click();
		sleep(300);
		
		by = By.xpath("(//*[@type='radio'][@name='IndividualReturn2018[paidContractor600]'])[1]");
		elem = (new WebDriverWait(driver, retries))
				   .until(ExpectedConditions.elementToBeClickable(by));
		this.scrollIntoView(elem);
		sleep(300);
		elem.click();
		sleep(300);
		
		by = By.xpath("(//*[@type='radio'][@name='IndividualReturn2018[filedAll1099s]'])[2]");
		elem = (new WebDriverWait(driver, retries))
				   .until(ExpectedConditions.elementToBeClickable(by));
		this.scrollIntoView(elem);
		sleep(300);
		elem.click();
		sleep(300);
		
		by = By.xpath("(//*[@type='radio'][@name='IndividualReturn2018[foreignAccounts]'])[2]");
		elem = (new WebDriverWait(driver, retries))
				   .until(ExpectedConditions.elementToBeClickable(by));
		this.scrollIntoView(elem);
		sleep(300);
		elem.click();
		sleep(300);
		
		by = By.xpath("(//*[@type='radio'][@name='IndividualReturn2018[extensionRequest]'])[2]");
		elem = (new WebDriverWait(driver, retries))
				   .until(ExpectedConditions.elementToBeClickable(by));
		this.scrollIntoView(elem);
		sleep(300);
		elem.click();
		sleep(300);
		
		by = By.xpath("//*[@type='text'][@name='IndividualReturn2018[taxPayerName]']");
		elem = driver.findElement(by);
		this.scrollIntoView(elem);
		elem.sendKeys("John Smith");
		sleep(300);
		
		by = By.xpath("//*[@type='text'][@name='IndividualReturn2018[submittedBy]']");
		elem = driver.findElement(by);
		this.scrollIntoView(elem);
		elem.sendKeys("John Smith");
		sleep(300);
		
		by = By.xpath("//*[@type='text'][@name='IndividualReturn2018[daytimePhone]']");
		elem = driver.findElement(by);
		this.scrollIntoView(elem);
		elem.sendKeys("5555555555");
		sleep(300);
		
		by = By.xpath("//*[@type='text'][@name='IndividualReturn2018[email]']");
		elem = driver.findElement(by);
		this.scrollIntoView(elem);
		elem.sendKeys("test1@testing.com");
		sleep(300);
		
		by = By.xpath("(//*[@type='radio'][@name='IndividualReturn2018[requestHardcopy]'])[1]");
		elem = (new WebDriverWait(driver, retries))
				   .until(ExpectedConditions.elementToBeClickable(by));
		this.scrollIntoView(elem);
		sleep(300);
		elem.click();
		sleep(300);
		
		by = By.xpath("//input[@type='checkbox'][@name='TermsForm[confirmInformationWarning]']");
		elem = (new WebDriverWait(driver, retries))
				   .until(ExpectedConditions.elementToBeClickable(by));
		this.scrollIntoView(elem);
		sleep(100);
		elem.click();
		sleep(100);
		
		by = By.xpath("//input[@type='checkbox'][@name='TermsForm[confirmDelayWarning]']");
		elem = (new WebDriverWait(driver, retries))
				   .until(ExpectedConditions.elementToBeClickable(by));
		this.scrollIntoView(elem);
		sleep(100);
		elem.click();
		sleep(100);
		
		by = By.xpath("//input[@type='checkbox'][@name='TermsForm[confirmEmailWarning]']");
		elem = (new WebDriverWait(driver, retries))
				   .until(ExpectedConditions.elementToBeClickable(by));
		this.scrollIntoView(elem);
		sleep(100);
		elem.click();
		sleep(100);
		
		by = By.xpath("//input[@type='checkbox'][@name='TermsForm[confirmDueDateWarning]']");
		elem = (new WebDriverWait(driver, retries))
				   .until(ExpectedConditions.elementToBeClickable(by));
		this.scrollIntoView(elem);
		sleep(100);
		elem.click();
		sleep(100);
		
		by = By.xpath("//input[@type='checkbox'][@name='TermsForm[confirmFilingDeadlineWarning]']");
		elem = (new WebDriverWait(driver, retries))
				   .until(ExpectedConditions.elementToBeClickable(by));
		this.scrollIntoView(elem);
		sleep(100);
		elem.click();
		sleep(100);
		
		by = By.xpath("//input[@type='checkbox'][@name='TermsForm[confirmNoProcessDatesWarning]']");
		elem = (new WebDriverWait(driver, retries))
				   .until(ExpectedConditions.elementToBeClickable(by));
		this.scrollIntoView(elem);
		sleep(100);
		elem.click();
		sleep(100);
		
		by = By.xpath("//input[@type='checkbox'][@name='TermsForm[confirmDepositWarning]']");
		elem = (new WebDriverWait(driver, retries))
				   .until(ExpectedConditions.elementToBeClickable(by));
		this.scrollIntoView(elem);
		sleep(100);
		elem.click();
		sleep(100);
		
		by = By.xpath("//input[@type='checkbox'][@name='TermsForm[confirmServicePaymentWarning]']");
		elem = (new WebDriverWait(driver, retries))
				   .until(ExpectedConditions.elementToBeClickable(by));
		this.scrollIntoView(elem);
		sleep(100);
		elem.click();
		sleep(300);
		
		by = By.xpath("//button[@type='submit']");
		elem = (new WebDriverWait(driver, retries))
				   .until(ExpectedConditions.elementToBeClickable(by));
		sleep(delay);
		elem.click();
		
		sleep(3000);
	    driver.get(baseUrlFe + "site/dashboard");
		sleep(3000);
		
		
		//--------------------------------------------------------------------->
		// Clean-up
		
	    ((JavascriptExecutor)driver).executeScript("window.open()");
	    tabs = new ArrayList<String> (driver.getWindowHandles());
	    driver.switchTo().window(tabs.get(2));
	    driver.get(baseUrlBe + "integration-test/roll-back-test1?accountNumber=V01TESTA&year=2018");
	    
	    
		
		sleep(5000);
		this.getDriver().quit();

		
		
	}
	
	

}
