package com.sergiishapoval.selenium.configuration;

import com.sergiishapoval.selenium.configuration.properties.PropertiesLoader;
import com.sergiishapoval.selenium.configuration.properties.Property;
import com.sergiishapoval.selenium.configuration.properties.PropertyFile;
import com.sergiishapoval.selenium.webtestsbase.Browser;
import org.apache.log4j.PropertyConfigurator;

/**
 * Created by Sidelnikov Mikhail on 18.09.14.
 * Class for base tests properties - urls for test, browser name and version
 */
@PropertyFile("config.properties")
public class TestsConfig {

    private static TestsConfig config;

    public static TestsConfig getConfig() {
        if (config == null) {
            config = new TestsConfig();
        }
        return config;
    }

    @Property("browser.name")
    private String browser = "chrome";

    @Property("browser.version")
    private String version = "";

    @Property("course.name")
    private String courseName;
    @Property("portal.url")
    private String portalUrl;
    @Property("content.prefix")
    private String contentPrefix;
    @Property("portal.login")
    private String login;
    @Property("portal.password")
    private String password;

    private String contentPageUrl;


    private TestsConfig() {
        PropertyConfigurator.configure("log4j.properties");
        PropertiesLoader.populate(this);
        contentPageUrl = String.format("%s/%s/%s", portalUrl, courseName, contentPrefix);
    }



    /**
     * getting browser object
     * @return browser object
     */
    public Browser getBrowser() {
        Browser browserForTests = Browser.getByName(browser);
        if (browserForTests != null) {
            return browserForTests;
        } else {
            throw new IllegalStateException("Browser name '" + browser + "' is not valid");
        }
    }

    /**
     * getting browser version
     * @return browser version
     */
    public String getBrowserVersion() {
        return version;
    }


    public String getContentPageUrl() {
        return contentPageUrl;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getPortalUrl() {
        return portalUrl;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
