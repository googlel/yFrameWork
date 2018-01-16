package com.liy.today.statistic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The creator is Leone && E-mail: butleone@163.com
 *
 * @author Leone
 * @date 15/11/28
 * @description Edit it! Change it! Beat it! Whatever, just do it!
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PageEvent {

    String pageId() default "";

    String pageName() default "";

    String referPageId() default "";

    String referPageName() default "";

}
