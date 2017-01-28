package com.sergiishapoval.selenium.webtestsbase;

import org.apache.commons.lang3.SerializationUtils;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by @author SergiiShapoval on 1/28/2017.
 */
public class CookiesInjector {

    private static final Logger LOGGER = LoggerFactory.getLogger(CookiesInjector.class);
    private static String FILE_OF_COOKIES = "cookies.txt";

    public CookiesInjector() {
    }

    public void injectCookies(String portalUrl, WebDriver driver) {
        driver.get(portalUrl);
        List<Cookie> cookies = new ArrayList<>();
        try {
            cookies = (ArrayList<Cookie>) SerializationUtils.deserialize(new FileInputStream(FILE_OF_COOKIES));
        } catch (FileNotFoundException e) {
            LOGGER.error("cookes file wasn't used", e);
        }
        WebDriver.Options options = driver.manage();
        cookies.forEach(options::addCookie);
        driver.navigate().refresh();
    }

    public void persistCookies(WebDriver driver) {
        Set<Cookie> cookies = driver.manage().getCookies();
        try {
            SerializationUtils.serialize(new ArrayList<>(cookies), new FileOutputStream(FILE_OF_COOKIES));
        } catch (FileNotFoundException e) {
            LOGGER.error("Cookies wasn't serialized: ", e);
        }
    }
}
