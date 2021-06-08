package com.mooc.libnavannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @author zhangruiping
 */
@Target(ElementType.TYPE)
public @interface ActivityDestination {

    /**
     * 页面地址
     *
     * @return pageUrl
     */
    String pageUrl();

    /**
     * 是否需要登录
     *
     * @return needLogin
     */
    boolean needLogin() default false;

    /**
     * 是否作为启动页面
     *
     * @return asStarter
     */
    boolean asStarter() default false;
}
