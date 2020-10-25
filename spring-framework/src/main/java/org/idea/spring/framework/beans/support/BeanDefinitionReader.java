package org.idea.spring.framework.beans.support;

import org.idea.spring.framework.annotation.IBean;
import org.idea.spring.framework.annotation.IController;
import org.idea.spring.framework.annotation.IService;
import org.idea.spring.framework.beans.config.BeanDefinition;
import org.idea.spring.framework.common.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * @author linhao
 * @date created in 2:55 下午 2020/10/24
 */
public class BeanDefinitionReader {


    private Properties contextConfig;

    private List<String> registerClassNameList = new ArrayList<>();

    public BeanDefinitionReader(String[] configLocations) {
        contextConfig = new Properties();
        //加载配置文件
        doLoadContainer(configLocations[0]);
        //解析配置文件，将配置信息封装成BeanDefinition
        doScanner(contextConfig.getProperty("basePackage"));
    }

    public List<BeanDefinition> doLoadBeanDefinition() {
        List<BeanDefinition> beanDefinitions = new ArrayList<>();
        try {
            for (String className : registerClassNameList) {
                Class<?> beanClazz = Class.forName(className);
                if (!beanClazz.isAnnotationPresent(IController.class)
                        && !beanClazz.isAnnotationPresent(IService.class)
                        && !beanClazz.isAnnotationPresent(IBean.class)) {
                    continue;
                }
                //默认使用类名首字母小写来作为beanName
                if (beanClazz.isInterface()) {
                    continue;
                }
                //使用类首字母小写名作为beanName
                //testServiceImpl-->com.sise.TestServiceImpl
                beanDefinitions.add(doCreateBeanDefinition(StringUtils.toLowerFirstName(beanClazz.getSimpleName()), beanClazz.getName()));

                //使用接口全名作为beanName
                // testService-->com.sise.TestServiceImpl
                for (Class<?> clazz : beanClazz.getInterfaces()) {
                    beanDefinitions.add(doCreateBeanDefinition(clazz.getName(), beanClazz.getName()));
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return beanDefinitions;
    }

    private BeanDefinition doCreateBeanDefinition(String beanName, String beanClassName) {
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setFactoryBeanName(beanName);
        beanDefinition.setBeanClassName(beanClassName);
        return beanDefinition;

    }

    private void doScanner(String basePackage) {
        URL url = this.getClass().getClassLoader().getResource(basePackage);
        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()) {
            if (file.isDirectory()) {
                doScanner(basePackage + "/" + file.getName());
            } else {
                if (file.getName().contains(".class")) {
                    String handBasePackage = basePackage.replaceAll("/", ".");
                    String className = handBasePackage + "." + file.getName().replaceAll(".class", "");
                    registerClassNameList.add(className);
                }
            }
        }
    }

    private void doLoadContainer(String contextConfiguration) {
        if (StringUtils.isEmpty(contextConfiguration)) {
            return;
        }
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(contextConfiguration);
        try {
            contextConfig.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Properties getConfig(){
        return contextConfig;
    }
}
