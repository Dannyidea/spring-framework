package org.idea.spring.framework.context;


import org.idea.spring.framework.beans.config.BeanDefinition;
import org.idea.spring.framework.beans.config.BeanDefinitionReader;
import org.idea.spring.framework.beans.config.BeanWrapper;
import org.idea.spring.framework.common.RestController;
import org.idea.spring.framework.common.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.idea.spring.framework.common.StringUtils.firstCharLower;

/**
 * @author linhao
 * @date created in 6:50 下午 2020/11/2
 */
public class ApplicationContext {

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private Map<String, BeanWrapper> factoryBeanInstanceMapCache = new ConcurrentHashMap<>();

    private Map<String, Object> beanFactoryObjectMapCache = new ConcurrentHashMap<>();

    public ApplicationContext(String... location) {
        BeanDefinitionReader beanDefinitionReader = new BeanDefinitionReader(location);
        try {
            List<BeanDefinition> beanDefinitions = beanDefinitionReader.loadBeanDefinition();
            loadBeanDefinitionMap(beanDefinitions);
            loadBeanWrapper(beanDefinitions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getBean(String beanName) {
        Object beanObj = this.factoryBeanInstanceMapCache.get(beanName);
        if (beanObj != null) {
            return beanObj;
        }
        BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
        if (beanDefinition != null) {
            beanDefinition.getBeanClass();
        }
    }

    /**
     * 加载beanWrapper
     *
     * @param beanDefinitions
     */
    private void loadBeanWrapper(List<BeanDefinition> beanDefinitions) {
        try {
            for (BeanDefinition beanDefinition : beanDefinitions) {
                BeanWrapper beanWrapper = new BeanWrapper();
                beanWrapper.setWrapperInterfaceName(beanDefinition.getBeanClass());
                //这里需要考虑一些额外情况，假设是一个代理对象该怎么优化处理
                Object instance = Class.forName(beanDefinition.getBeanClass()).newInstance();
                beanWrapper.setInstanceObject(instance);
                factoryBeanInstanceMapCache.put(beanDefinition.getBeanClass(), beanWrapper);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 见BeanDefinition加载到map里面
     *
     * @param beanDefinition
     */
    private void loadBeanDefinitionMap(List<BeanDefinition> beanDefinition) {
        for (BeanDefinition definition : beanDefinition) {
            if (beanDefinitionMap.containsKey(definition.getBeanClass()) || beanDefinition.contains(definition.getFactoryBeanName())) {
                throw new RuntimeException("bean definition has conflict key");
            }
            this.beanDefinitionMap.put(definition.getFactoryBeanName(), definition);
            this.beanDefinitionMap.put(definition.getBeanClass(), definition);
        }
    }


    private Object instanceBeanObj(String className){
        Class<?> clazz = null;
        Object object = null;
        try {
            clazz = Class.forName(className);
            if(clazz!=null) {
                if (!clazz.isAnnotationPresent(Service.class)
                        && !clazz.isAnnotationPresent(RestController.class)) {
                    return null;
                }
                object = clazz.newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

}
