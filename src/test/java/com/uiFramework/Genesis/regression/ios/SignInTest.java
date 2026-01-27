package com.uiFramework.Genesis.regression.ios;

import org.testng.annotations.Test;

import com.uiFramework.Genesis.base.BaseTest;
import com.uiFramework.Genesis.ios.pages.AlertViewsOldBase;
import com.uiFramework.Genesis.ios.pages.LoginPage;

public class SignInTest extends BaseTest{	
//	LoginPage loginPage = new LoginPage(driver);
	
	@Test(priority = 1)
    public void VerifyUserReceivesErrorMsgWhenClickOnSignInWithoutEnteringDetails() {

    }

    @Test(priority = 2)
    public void VerifyUserReceivesErrorMsgWhenClickOnSignInWithoutPassword() {

    }

    @Test(priority = 3)
    public void VerifyUserReceivesErrorMsgWhenClickOnSignInWithoutUsername() {

    }

    @Test(priority = 4)
    public void VerifyUserIsNotAbleToLoginWithInvalidCredentials() {

    }
    
    @Test(priority = 5)
    public void VerifyUserIsAbleToLoginWithValidCredentials() {

    }

}
