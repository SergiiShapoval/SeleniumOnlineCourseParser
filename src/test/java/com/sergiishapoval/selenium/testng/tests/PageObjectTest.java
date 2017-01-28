package com.sergiishapoval.selenium.testng.tests;

import com.sergiishapoval.selenium.pages.YouTubePage;
import com.sergiishapoval.selenium.pages.YouTubeSearchResultsPage;
import com.sergiishapoval.selenium.testng.listeners.ScreenShotOnFailListener;
import com.sergiishapoval.selenium.webtestsbase.WebDriverFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Sidelnikov Mikhail on 19.09.14.
 * Uses TestNG test framework
 * Test demonstrates work with Page Object Pattern(https://code.google.com/p/selenium/wiki/PageObjects)
 */
@Listeners({ScreenShotOnFailListener.class})
public class PageObjectTest {

    @BeforeTest
    public void beforeTest() {
        WebDriverFactory.startBrowser(true);
    }


    @Test
    public void testSearch() {
        String toSearch = "Selenium";
        YouTubePage youTubePage = new YouTubePage();
        youTubePage.insertSearchString(toSearch);
        YouTubeSearchResultsPage resultsPage = youTubePage.doSearch();
        assertTrue("No results were found on results page", resultsPage.hasResults());
    }


    @AfterTest
    public void afterTest() {
        WebDriverFactory.finishBrowser();
    }
}
