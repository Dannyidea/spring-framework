package org.idea.spring.framework.aop;

import java.lang.reflect.Method;

/**
 * @author linhao
 * @date created in 8:03 下午 2020/10/25
 */
public class Advice {

    private Method adviceMethod;

    private Object aspect;

    private String throwName;

    public Advice(Method adviceMethod, Object aspect) {
        this.adviceMethod = adviceMethod;
        this.aspect = aspect;
    }

    public Method getAdviceMethod() {
        return adviceMethod;
    }

    public void setAdviceMethod(Method adviceMethod) {
        this.adviceMethod = adviceMethod;
    }

    public Object getAspect() {
        return aspect;
    }

    public void setAspect(Object aspect) {
        this.aspect = aspect;
    }

    public String getThrowName() {
        return throwName;
    }

    public void setThrowName(String throwName) {
        this.throwName = throwName;
    }
}
