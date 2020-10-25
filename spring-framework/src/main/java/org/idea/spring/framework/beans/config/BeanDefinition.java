package org.idea.spring.framework.beans.config;

/**
 * @author linhao
 * @date created in 2:56 下午 2020/10/24
 */
public class BeanDefinition {

    private String factoryBeanName;

    private String beanClassName;

    private boolean isLazy;

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public boolean isLazy() {
        return isLazy;
    }

    public void setLazy(boolean lazy) {
        isLazy = lazy;
    }

}
