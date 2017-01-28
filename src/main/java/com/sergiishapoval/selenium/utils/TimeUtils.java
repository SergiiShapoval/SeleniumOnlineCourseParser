package com.sergiishapoval.selenium.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by Sidelnikov Mikhail on 19.09.14.
 * Operations with time
 */
public class TimeUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeUtils.class);

    /**
     * waiting for seconds
     * @param timeoutInSeconds timeout in seconds for wait
     */
    public static void waitForSeconds(int timeoutInSeconds) {
        try {
            TimeUnit.SECONDS.sleep(timeoutInSeconds);
        } catch (InterruptedException e) {
            LOGGER.error("#waitForSeconds interrupted:\n",e);
        }
    }
}
