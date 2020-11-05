package org.idea.spring.framework.beans.config;

/**
 * @author linhao
 * @date created in 8:24 下午 2020/11/2
 */
public class BeanWrapper {

    private String wrapperInterfaceName;

    private Object instanceObject;

    public String getWrapperInterfaceName() {
        return wrapperInterfaceName;
    }

    public void setWrapperInterfaceName(String wrapperInterfaceName) {
        this.wrapperInterfaceName = wrapperInterfaceName;
    }

    public Object getInstanceObject() {
        return instanceObject;
    }

    public void setInstanceObject(Object instanceObject) {
        this.instanceObject = instanceObject;
    }
}
