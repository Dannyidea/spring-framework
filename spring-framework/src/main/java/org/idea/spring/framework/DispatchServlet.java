package org.idea.spring.framework;

import org.idea.spring.framework.action.TestController;
import org.idea.spring.framework.common.RequestMapping;
import org.idea.spring.framework.common.RestController;
import sun.misc.Request;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author linhao
 * @date created in 10:25 下午 2020/10/29
 */
public class DispatchServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI().replaceAll(req.getContextPath(), "");
        RequestMapping requestMapping = TestController.class.getAnnotation(RequestMapping.class);
        Pattern pattern = Pattern.compile("/tests*");
        if (pattern.matcher(requestMapping.url()).matches()) {
            Method[] methods = TestController.class.getDeclaredMethods();
            for (Method method : methods) {
                String reqMethodUrl = url.replaceAll(requestMapping.url(),"");
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    String methodUrl = method.getAnnotation(RequestMapping.class).url();
                    Pattern methodPattern = Pattern.compile(methodUrl);
                    if(methodPattern.matcher(reqMethodUrl).matches()){
                        System.out.println("method name is "+method.getName());
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }


}
