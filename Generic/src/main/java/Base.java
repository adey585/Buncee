import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Base {
    public WebDriver driver = null;
    public Logger log = Logger.getLogger(Base.class.getName());

    @Parameters
    ({"useSauceLab", "userName", "key", "appURL", "os", "browserName", "browserVersion"})

    @BeforeMethod
    public void setUp(boolean useSauceLab, String userName, String key, String appURL, String os, String browserName, String browserVersion)
    throws IOException {
        if (useSauceLab == true) {
            getSauceLabDriver(userName, key, os, browserName, browserVersion);
        } else {
            getLocalDriver(os, browserName);
        }
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.navigate().to(appURL);
        driver.manage().window().maximize();
        log.info("Browser Loaded with App");
    }

//Get Sauce Lab Driver
    public WebDriver getSauceLabDriver(String userName, String key, String os, String browserName, String browserVersion) throws IOException {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("platform", os);
        cap.setBrowserName(browserName);
        cap.setCapability("version", browserVersion);
        cap.setCapability("name", "BunceeTest");
        cap.setCapability("extendedDebugging", "true");

        driver = new RemoteWebDriver(new URL("http://" + userName + ":" + key +
                "@ondemand.saucelabs.com:80/wd/hub"), cap);

        return driver;
    }

//Get Local Driver
    public WebDriver getLocalDriver(String os, String browserName) {
        if (browserName.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
        } else if (browserName.equalsIgnoreCase("chrome")) {
            if (os.equalsIgnoreCase("window")) {
                System.setProperty("webdriver.chrome.driver", "C:\\Users\\iamam\\Desktop\\BunRe\\Generic\\src\\main\\driver\\chromedriver.exe");
            } else {
                System.setProperty("webdriver.chrome.driver", "C:\\Users\\iamam\\Desktop\\BunRe\\Generic\\src\\main\\driver\\chromedriver.exe");
            }
            driver = new ChromeDriver();
        }
        return driver;
    }

    @AfterMethod
    public void cleanUp() throws InterruptedException {
        log.info("Driver is quiting");
        driver.quit();
    }

    public void clickByXpath(String xpath) throws InterruptedException {
        Thread.sleep(2000);
        driver.findElement(By.xpath(xpath)).click();
    }

    public void sendKeyXpath(String xpath, String data) throws InterruptedException {
        Thread.sleep(2000);
        driver.findElement(By.xpath(xpath)).sendKeys(data);
    }

    public void enterKeyXpath(String xpath) throws InterruptedException {
        Thread.sleep(2000);
        driver.findElement(By.xpath(xpath)).sendKeys(Keys.ENTER);
    }

    public String readFromExcel(String fileRef, String sheetRef, String cellRef) throws IOException {
        FileInputStream fis = new FileInputStream(fileRef);
        Workbook wb = new XSSFWorkbook(fis);
        Sheet sheet = wb.getSheet(sheetRef);
        DataFormatter formatter = new DataFormatter();
        CellReference cellReference = new CellReference(cellRef);
        Row row = sheet.getRow(cellReference.getRow());
        Cell cell = row.getCell(cellReference.getCol());
        String value = "";
        if (value != null) {
            value = formatter.formatCellValue(cell);
        }
        return value;
    }

    public void takeScreenshot(String testCaseName) throws Exception {
        Thread.sleep(2000);
        String screenshotLocation = "C:\\Users\\iamam\\Desktop\\BunRe\\Application\\src\\test\\TestResults\\Screenshots\\";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
        Date date = new Date();
        String currentTime = dateFormat.format(date);
        System.out.println("------------------------------"+ currentTime);

        TakesScreenshot screenshot = (TakesScreenshot)driver;
        File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
        File destFile = new File(screenshotLocation + testCaseName + "_" + currentTime + ".png");
        FileUtils.copyFile(srcFile, destFile);
    }










}
