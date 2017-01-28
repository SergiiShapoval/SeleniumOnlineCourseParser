package com.sergiishapoval.selenium.exceptions;

/**
 * Created by @SergiiShapoval on 14.07.15.
 */
public class TestsConfigurationException extends RuntimeException {

    public TestsConfigurationException(String message) {
        super(message);
    }

    public TestsConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
