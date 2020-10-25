package org.idea.spring.framework.starter;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.idea.spring.framework.webmvc.MyDispatcherServlet;

import java.io.File;

import static org.idea.spring.framework.common.constants.CommonsConstants.WEB_PREFIX;

/**
 * @author linhao
 * @date created in 10:47 下午 2020/10/22
 */
public class Main {

    Tomcat tomcat;

    {
        tomcat = new Tomcat();
    }

    public void run() throws LifecycleException {
        tomcat.setBaseDir("/Users/linhao/IdeaProjects/2020-project/spring-framework/src/main/resources/baseDir");
        tomcat.setHostname("localhost");
        tomcat.setPort(8080);

        StandardContext myCtx = (StandardContext) tomcat
                .addWebapp("/mvc", System.getProperty("user.dir") + File.separator + "src/main");

        //一级目录
        tomcat.addServlet("/mvc","myDispatcherServlet",new MyDispatcherServlet());
        // servlet mapping
        myCtx.addServletMappingDecoded("/"+WEB_PREFIX+"/*", "myDispatcherServlet");
        tomcat.getConnector();
        tomcat.start();
        System.out.println("tomcat is starting");
        tomcat.getServer().await();
    }

    public static void main(String[] args) throws LifecycleException {
        new Main().run();
    }
}
