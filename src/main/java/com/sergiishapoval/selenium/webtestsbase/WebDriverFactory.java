package com.sergiishapoval.selenium.webtestsbase;

import com.sergiishapoval.selenium.configuration.TestsConfig;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sidelnikov Mikhail on 18.09.14.
 * Base class for web tests. It contains web driver {@link org.openqa.selenium.WebDriver} instance, used in all tests.
 * All communications with driver should be done through this class
 */
public class WebDriverFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebDriverFactory.class);
    private static final long IMPLICIT_WAIT_TIMEOUT = 8;
    private static WebDriver driver;
    /**
     * added to handle cookies (open page without authorization)
     */
    private static CookiesInjector cookiesInjector = new CookiesInjector();

    /**
     * Getting of pre-configured {@link org.openqa.selenium.WebDriver} instance.
     * Please use this method only after call {@link #startBrowser(boolean) startBrowser} method
     *
     * @return webdriver object, or throw IllegalStateException, if driver has not been initialized
     */
    public static WebDriver getDriver() {
        if (driver != null) {
            return driver;
        } else {
            throw new IllegalStateException("Driver has not been initialized. " +
                    "Please call WebDriverFactory.startBrowser() before use this method");
        }
    }

    /**
     * Main method of class - it initialize driver and starts browser.
     *
     * @param isLocal - is tests will be started local or not
     */
    public static void startBrowser(boolean isLocal) {

        if (driver == null) {
            Browser browser = TestsConfig.getConfig().getBrowser();
            if (!isLocal) {
                driver = new RemoteWebDriver(CapabilitiesGenerator.getDefaultCapabilities(browser));
            } else {
                switch (browser) {
                    case FIREFOX:
//                      @SergiiShapoval added new library (io.github.bonigarcia.webdrivermanager) to handle paths to drivers
                        FirefoxDriverManager.getInstance().setup();
                        driver = new FirefoxDriver();
                        break;
                    case CHROME:
//                      @SergiiShapoval added new library (io.github.bonigarcia.webdrivermanager) to handle paths to drivers
                        ChromeDriverManager.getInstance().setup();
                        driver = new ChromeDriver();
                        break;
                    case IE10:
                        InternetExplorerDriverManager.getInstance().setup();
                        driver = new InternetExplorerDriver(CapabilitiesGenerator.getDefaultCapabilities(Browser.IE10));
                        break;
                    case SAFARI:
                        driver = new SafariDriver(CapabilitiesGenerator.getDefaultCapabilities(Browser.SAFARI));
                        break;
                    default:
                        throw new IllegalStateException("Unsupported browser type");
                }
            }
            driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT_TIMEOUT, TimeUnit.SECONDS);
        } else {
            throw new IllegalStateException("Driver has already been initialized. Quit it before using this method");
        }
    }

    /**
     * Finishes browser
     */
    public static void finishBrowser() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    /**
     * Method for screenshot taking. It is empty now, because you could save your screenshot as you want.
     * This method calls in tests listeners on test fail
     */
    public static void takeScreenShot() {
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File(new SimpleDateFormat("yyyy-MM-dd hh-mm-ss'.png'").format(new Date())));
            LOGGER.info("ScreenShot taken");
        } catch (IOException e) {
            LOGGER.error("Unable to save screenshot due to: \n", e);
        }
    }

    public static void injectCookies() {
        cookiesInjector.injectCookies(TestsConfig.getConfig().getPortalUrl(), driver);
    }

    public static void persistCookies() {
        cookiesInjector.persistCookies(driver);
    }
}
