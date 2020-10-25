package org.idea.spring.framework.core;

import java.net.URL;

/**
 * @author linhao
 * @date created in 10:47 上午 2020/10/23
 */
public class ClassLoaderDemo {

    public void displayFile(String basePackage){
        URL url = this.getClass().getClassLoader().getResource(basePackage);
        System.out.println(url.getFile());
    }

    public void displayFile2(String basePackage){
        URL url = this.getClass().getResource(basePackage);
        System.out.println(url);
    }

    public static void main(String[] args) {
        ClassLoaderDemo classLoaderDemo = new ClassLoaderDemo();
        classLoaderDemo.displayFile("org/idea/spring/framework");
        classLoaderDemo.displayFile2("/TestServlet.java");
    }
}
