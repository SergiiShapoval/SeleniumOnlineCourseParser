package com.sergiishapoval.selenium.pages.course;

import com.sergiishapoval.selenium.webtestsbase.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by @SergiiShapoval on 1/25/2017.
 */
public class SignUpPage extends BasePage {

	@FindBy(className="goto-login-btn")
	private WebElement gotoLoginBtn;

    public SignUpPage() {
        super(false);
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
        return gotoLoginBtn.isDisplayed();
    }

    public void goToLoginPage(){
        gotoLoginBtn.click();
    }
}
