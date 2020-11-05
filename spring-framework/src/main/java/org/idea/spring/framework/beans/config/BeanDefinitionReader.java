package org.idea.spring.framework.beans.config;

import org.idea.spring.framework.common.RestController;
import org.idea.spring.framework.common.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.idea.spring.framework.common.StringUtils.firstCharLower;

/**
 * @author linhao
 * @date created in 6:40 下午 2020/11/2
 */
public class BeanDefinitionReader {

    private Properties properties;

    private List<String> registerClassNameList = new ArrayList<>();

    public BeanDefinitionReader(String[] resourceLocations) {
        String location = resourceLocations[0];
        if (location != null) {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(location);
            try {
                properties.load(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String basePackage = properties.getProperty("basePackage");
        //加载class名称到列表里面
        doScan(basePackage);
    }

    /**
     * 加载bean的定义列表
     *
     * @return
     */
    public List<BeanDefinition> loadBeanDefinition() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        List<BeanDefinition> beanDefinitionList = new CopyOnWriteArrayList<>();
        for (String className : registerClassNameList) {
            Class<?> clazz = Class.forName(className);
            if(clazz!=null){
                if(!clazz.isAnnotationPresent(Service.class)
                   && !clazz.isAnnotationPresent(RestController.class)){
                    String beanName = clazz.getSimpleName();
                    String lowerName = firstCharLower(beanName);
                    BeanDefinition beanDefinition = new BeanDefinition();
                    beanDefinition.setBeanClass(className);
                    beanDefinition.setFactoryBeanName(lowerName);
                    beanDefinitionList.add(beanDefinition);
                }
            }
        }
        return beanDefinitionList;
    }




    private void doScan(String filePath) {
        try {
            URL url = this.getClass().getClassLoader().getResource(filePath);
            File classPathFile = new File(url.getFile());
            for (File file : classPathFile.listFiles()) {
                if (file.isDirectory()) {
                    doScan(filePath + "" + file.getName());
                } else {
                    if (file.getName().contains(".class")) {
                        filePath = filePath.trim().replace("/",".");
                        String className = filePath + "." + file.getName();
                        className.replaceAll(".class","");
                        registerClassNameList.add(className);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
