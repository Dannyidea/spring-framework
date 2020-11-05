package org.idea.spring.framework.beans.config;

/**
 * @author linhao
 * @date created in 6:34 下午 2020/11/2
 */
public class BeanDefinition {

    /**
     * 类的class名称，例如：UserService.class
     */
    private String beanClass;

    /**
     * 类的实现名称 例如：userService
     */
    private String factoryBeanName;


    public String getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(String beanClass) {
        this.beanClass = beanClass;
    }

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }
}
