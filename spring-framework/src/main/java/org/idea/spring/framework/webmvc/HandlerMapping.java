package org.idea.spring.framework.webmvc;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author linhao
 * @date created in 8:25 下午 2020/10/24
 */
public class HandlerMapping {

    private Pattern pattern;
    private Object controller;
    private Method method;

    private HandlerMapping(){

    }

    public HandlerMapping(Pattern pattern, Object controller, Method method) {
        this.pattern = pattern;
        this.controller = controller;
        this.method = method;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public Object getController() {
        return controller;
    }

    public Method getMethod() {
        return method;
    }
}
