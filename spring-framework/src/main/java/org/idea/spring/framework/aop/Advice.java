package org.idea.spring.framework.aop;

import java.lang.reflect.Method;

/**
 * 有点类似于BeanWrapper 封装了Object组装的细节信息 advice可以理解为通知
 *
 * @author linhao
 * @date created in 8:03 下午 2020/10/25
 */
public class Advice {

    /**
     * 需要被通知的方法
     */
    private Method adviceMethod;

    /**
     * 这里应该存储切面的object
     */
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
