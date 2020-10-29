package org.idea.spring.framework;

import com.sun.xml.internal.rngom.parse.host.Base;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

/**
 * @author linhao
 * @date created in 9:58 下午 2020/10/29
 */
public class Main {

    private static final String BASE_DIR = System.getProperty("user.dir");

    private static String JAVA_DIR = BASE_DIR + "/src/main/java";

    private static String RESOURCE_DIR = BASE_DIR + "/src/main/resources";

    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(RESOURCE_DIR);
        tomcat.setHostname("localhost");
        tomcat.setPort(8080);
        StandardContext standardContext = (StandardContext) tomcat
                .addWebapp("mvc",JAVA_DIR);
        tomcat.addServlet("/mvc","dispatchServlet",new DispatchServlet());
        standardContext.addServletMappingDecoded("/*","dispatchServlet");
        tomcat.getConnector();
        tomcat.start();
        System.out.println("tomcat init");
        tomcat.getServer().await();
    }
}
