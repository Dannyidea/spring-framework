package org.idea.spring.framework.beans;

/**
 * @author linhao
 * @date created in 2:59 下午 2020/10/24
 */
public class BeanWrapper {

    private Object wrapperInstance;

    private String wrapperInstanceInterface;

    public BeanWrapper(Object instance,String interfaceName) {
        this.wrapperInstance = instance;
        this.wrapperInstanceInterface = interfaceName;
    }

    public Object getWrapperInstance() {
        return wrapperInstance;
    }

    public void setWrapperInstance(Object wrapperInstance) {
        this.wrapperInstance = wrapperInstance;
    }

    public String getWrapperInstanceInterface() {
        return wrapperInstanceInterface;
    }

    public void setWrapperInstanceInterface(String wrapperInstanceInterface) {
        this.wrapperInstanceInterface = wrapperInstanceInterface;
    }
}
