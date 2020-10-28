package org.idea.spring.framework.context;

import org.idea.spring.framework.annotation.Autowired;
import org.idea.spring.framework.annotation.IController;
import org.idea.spring.framework.annotation.IService;
import org.idea.spring.framework.aop.AdviceSupport;
import org.idea.spring.framework.aop.AopConfig;
import org.idea.spring.framework.aop.JDKDynamicAopProxy;
import org.idea.spring.framework.beans.BeanWrapper;
import org.idea.spring.framework.beans.config.BeanDefinition;
import org.idea.spring.framework.beans.support.BeanDefinitionReader;
import org.idea.spring.framework.common.util.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author linhao
 * @date created in 2:47 下午 2020/10/24
 */
public class ApplicationContext {

    private String[] configLocations;
    private BeanDefinitionReader reader;
    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<String, BeanDefinition>();

    /**
     * 存储对象实例的一个cache
     */
    private Map<String, BeanWrapper> factoryBeanInstanceCache = new HashMap<String, BeanWrapper>();

    /**
     * 这里面存储一份单例对象的缓存作为备份
     */
    private Map<String, Object> factoryBeanObjectCache = new HashMap<String, Object>();

    public ApplicationContext(String... configurationLocations) {
        this.configLocations = configurationLocations;
        this.reader = new BeanDefinitionReader(this.configLocations);
        //加载配置文件
        List<BeanDefinition> beanDefinitions = reader.doLoadBeanDefinition();
        //将beanDefinition缓存起来
        doRegisterBeanDefinition(beanDefinitions);
        //根据beanDefinitionMap去做对象的实例化
        doCreateBean();
    }

    private void doCreateBean() {
        for (String beanName : beanDefinitionMap.keySet()) {
            Object bean = getBean(beanName);
        }
    }

    private void doRegisterBeanDefinition(List<BeanDefinition> beanDefinitions) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            if (this.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                throw new RuntimeException(beanDefinition.getFactoryBeanName() + " has exist");
            }
            //一般是bean的class全称 例如说com.sise.TestService
            this.beanDefinitionMap.put(beanDefinition.getBeanClassName(), beanDefinition);
            //bean的唯一称呼，不能重复 例如说testService
            this.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
            //这里面我们可以理解为 beanName包含来类形全称和bean自身定义的beanName
        }
    }

    //职责
    //1.完成bean的创建实例
    //2.完成依赖注入
    public Object getBean(String beanName) {
        if (this.factoryBeanObjectCache.containsKey(beanName)) {
            return factoryBeanObjectCache.get(beanName);
        }
        //1. 拿到beanName对应的配置信息，根据BeanDefinition创建对象信息
        BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
        //初始化创建对象
        Object instance = instanceObject(beanName, beanDefinition);
        //如果有对象，则进行实例化，还有可能会创建一个代理
        if (instance != null) {
            BeanWrapper beanWrapper = new BeanWrapper(instance, beanDefinition.getBeanClassName());
            //将beanWrapper缓存到ioc容器里面，实现DI
            populateBean(beanName, beanDefinition, beanWrapper);
            factoryBeanObjectCache.put(beanName, instance);
            factoryBeanInstanceCache.put(beanName, beanWrapper);
            return beanWrapper.getWrapperInstance();
        }
        return null;
    }

    /**
     * 将bean放入到ioc容器里
     *
     * @param beanName
     * @param beanDefinition
     * @param beanWrapper
     */
    private void populateBean(String beanName, BeanDefinition beanDefinition, BeanWrapper beanWrapper) {

        Object instance = beanWrapper.getWrapperInstance();
        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAccessible()) {
                //private 转为 public
                field.setAccessible(true);
            }
            if (!field.isAnnotationPresent(Autowired.class)) {
                continue;
            }
            Autowired autowired = field.getAnnotation(Autowired.class);
            String autowiredBeanName = autowired.value();
            if (StringUtils.isEmpty(autowiredBeanName)) {
                autowiredBeanName = field.getType().getName();
            }
            try {
                if (!this.factoryBeanInstanceCache.containsKey(autowiredBeanName)) {
                    Object object = this.getBean(autowiredBeanName);
                    if (object == null) {
                        throw new RuntimeException(autowiredBeanName + " do not existed");
                    }
                }
                //从ioc容器中查询对应的实例，进行注入
                field.set(instance, this.factoryBeanInstanceCache.get(autowiredBeanName).getWrapperInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    public Object getBean(Class className) {
        return this.getBean(className.getName());
    }

    private Object instanceObject(String beanName, BeanDefinition beanDefinition) {
        String className = beanDefinition.getBeanClassName();
        Object instance = null;
        try {
            Class<?> aClass = Class.forName(className);
            if (!aClass.isAnnotationPresent(IController.class) &&
                    !aClass.isAnnotationPresent(IService.class)) {
                return null;
            }
            instance = aClass.newInstance();
            factoryBeanObjectCache.put(beanName, instance);
            //aop需要配置一个切面表达式
            //如果慢则aop的正则表达式规则，就在这里进行代理对象的创建
            AdviceSupport adviceSupport = instanceAdviceSupport(beanDefinition);
            adviceSupport.setTarget(instance);
            adviceSupport.setTargetClass(aClass);
            adviceSupport.parse();

            //如果满足切面规则
            if(adviceSupport.pointCutMatch()){
                instance = new JDKDynamicAopProxy(adviceSupport).getProxy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }

    private AdviceSupport instanceAdviceSupport(BeanDefinition beanDefinition) {
        AopConfig aopConfig = new AopConfig();
        aopConfig.setPointCut(this.reader.getConfig().getProperty("pointCut"));
        aopConfig.setAspectAfter(this.reader.getConfig().getProperty("aspectAfter"));
        aopConfig.setAspectAfterThrow(this.reader.getConfig().getProperty("aspectAfterThrow"));
        aopConfig.setAspectAfterThrowingName(this.reader.getConfig().getProperty("aspectAfterThrowingName"));
        aopConfig.setAspectBefore(this.reader.getConfig().getProperty("aspectBefore"));
        aopConfig.setAspectClass(this.reader.getConfig().getProperty("aspectClass"));
        return new AdviceSupport(aopConfig);
    }



    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new String[getBeanDefinitionCount()]);
    }

    public Properties getConfig(){
        return this.reader.getConfig();
    }
}
