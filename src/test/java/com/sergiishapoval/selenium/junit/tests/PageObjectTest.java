package com.sergiishapoval.selenium.junit.tests;

import com.sergiishapoval.selenium.configuration.TestsConfig;
import com.sergiishapoval.selenium.junit.rules.ScreenShotOnFailRule;
import com.sergiishapoval.selenium.pages.course.AllContentPage;
import com.sergiishapoval.selenium.pages.course.LoginPage;
import com.sergiishapoval.selenium.pages.course.SignUpPage;
import com.sergiishapoval.selenium.webtestsbase.WebDriverFactory;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import static org.junit.Assert.assertTrue;

/**
 * Created by Sidelnikov Mikhail on 19.09.14.
 * Uses JUnit test framework
 * Test demonstrates work with Page Object Pattern(https://code.google.com/p/selenium/wiki/PageObjects)
 */
public class PageObjectTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageObjectTest.class);
    private static final String FILE_PREFIX = ".txt";

    private CSVPrinter csvPrinter;
    private StringWriter stringWriter;
    private String contentPageUrl;
    private String courseName;
    private String login;
    private String password;
    @Rule
    public ScreenShotOnFailRule screenShotOnFailRule = new ScreenShotOnFailRule();

    @Before
    public void beforeTest() throws IOException {
        stringWriter = new StringWriter();
        csvPrinter = new CSVPrinter(stringWriter, CSVFormat.INFORMIX_UNLOAD);
        contentPageUrl = TestsConfig.getConfig().getContentPageUrl();
        courseName = TestsConfig.getConfig().getCourseName();
        login = TestsConfig.getConfig().getLogin();
        password = TestsConfig.getConfig().getPassword();
        WebDriverFactory.startBrowser(true);
        WebDriverFactory.injectCookies();
    }

    @Test
    public void parseCourseLectures() throws IOException {
        File courseRecordsFile = new File(courseName + FILE_PREFIX);
        int startPoint = 0;
        if (courseRecordsFile.exists()){
            int linesWithoutHeader = FileUtils.readLines(courseRecordsFile).size();
            startPoint = linesWithoutHeader < 0 ? 0 : linesWithoutHeader;
        }
        WebDriverFactory.getDriver().get(contentPageUrl);

        AllContentPage allContentPage = new AllContentPage(csvPrinter, startPoint, contentPageUrl);
        if (!allContentPage.isPageOpened()){
            SignUpPage signUpPage = new SignUpPage();
            assertTrue(signUpPage.isPageOpened());
            signUpPage.goToLoginPage();

            LoginPage loginPage = new LoginPage(login, password);
            assertTrue(loginPage.isPageOpened());
            loginPage.login();
        }
        PageFactory.initElements(WebDriverFactory.getDriver(), allContentPage);
        assertTrue("allContentPage is not opened correctly", allContentPage.isPageOpened());
        allContentPage.processLectures();
    }


    @After
    public void afterTest() throws IOException {
        csvPrinter.close();
        FileUtils.writeStringToFile(new File(courseName + FILE_PREFIX), stringWriter.getBuffer().toString(), true);
        WebDriverFactory.persistCookies();
        WebDriverFactory.finishBrowser();
    }
}
