package com.sergiishapoval.selenium.configuration.properties;

import com.sergiishapoval.selenium.configuration.TestsConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Sidelnikov Mikhail on 18.09.14.
 * Annotates field of {@link TestsConfig} fields for setting to this field
 * value from properties - system or file(about file you could find information in {@link PropertyFile} class javadoc
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Property {
    String value() default "";
}
