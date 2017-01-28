package com.sergiishapoval.selenium.configuration.properties;

import com.sergiishapoval.selenium.configuration.TestsConfig;
import com.sergiishapoval.selenium.exceptions.TestsConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * Created by Sidelnikov Mikhail on 18.09.14.
 * Class for loading base tests properties. It gets properties - system or from file (by specified names) and sets it to fields of TestConfig object
 */
public class PropertiesLoader {

    /**
     * Sets TestConfig object fields values to specified properties values
     *
     * @param config {@link TestsConfig} object
     */
    public static void populate(TestsConfig config) {
        Properties properties;
        PropertyFile propertyFileAnnotation = config.getClass().getAnnotation(PropertyFile.class);
        if (propertyFileAnnotation != null) {
            String propertyFileName = propertyFileAnnotation.value();
            properties = new Properties();
            try {
                InputStream stream = PropertiesLoader.class.getClassLoader().getResourceAsStream(propertyFileName);
                if(stream != null) {
                    properties.load(stream);
                } else {
                    throw new TestsConfigurationException("Unable to read property file with name '" + propertyFileName + "' - file not found");
                }
            } catch (IOException e) {
                throw new TestsConfigurationException("Error while reading property file with name '" + propertyFileName + "' : " + e.getMessage(), e);
            }
        } else {
            properties = System.getProperties();
        }
        Field[] fields = config.getClass().getDeclaredFields();
        for (Field field : fields) {
            Property propertyAnnotation = field.getAnnotation(Property.class);
            if (propertyAnnotation != null) {
                String propertyName = propertyAnnotation.value();
                String propertyValue = properties.getProperty(propertyName);
                if (propertyValue != null) {
                    try {
                        field.setAccessible(true);
                        field.set(config, propertyValue);
                    } catch (IllegalAccessException e) {
                        throw new TestsConfigurationException("Field cannot be set...", e);
                    }
                }
            }
        }
    }

}
