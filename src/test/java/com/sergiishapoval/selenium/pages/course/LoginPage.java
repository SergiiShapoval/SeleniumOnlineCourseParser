package com.sergiishapoval.selenium.pages.course;

import com.sergiishapoval.selenium.webtestsbase.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Created by @SergiiShapoval on 1/25/2017.
 */
public class LoginPage extends BasePage {

    private String login;
    private String password;


    @FindBy(xpath="//form[contains(@action,'/join/login-popup')]//input[@id='id_email']")
	private WebElement emailInput;

	@FindBy(xpath="//form[contains(@action,'/join/login-popup')]//input[@id='id_password']")
	private WebElement passwordInput;

    @FindBy(xpath="//form[contains(@action,'/join/login-popup')]//input[@id='submit-id-submit']")
    private WebElement logInButton;

    public LoginPage(String login, String password) {
        super(false);
        this.login = login;
        this.password = password;
    }

    /**
     * In subclasses  should be used for page opening
     */
    @Override
    protected void openPage() {
    }

    /**
     * checks is page opened
     *
     * @return true if opened
     */
    @Override
    public boolean isPageOpened() {
        return emailInput.isDisplayed();
    }

    public void login(){
        emailInput.sendKeys(login);
        passwordInput.sendKeys(password);
        logInButton.click();
    }
}
