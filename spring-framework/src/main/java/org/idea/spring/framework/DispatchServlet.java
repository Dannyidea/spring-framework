package org.idea.spring.framework;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author linhao
 * @date created in 10:25 下午 2020/10/29
 */
public class DispatchServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("do post");
        String url = req.getRequestURI();
        System.out.println(url);
        //localhost:8080/mvc/test?name=idea&&id=1
        String path = req.getContextPath();
        url.replaceAll(path,"").replaceAll("//?+","");
        System.out.println(path);
        Map<String,String[]> paramMap = req.getParameterMap();
        for (String paramItem : paramMap.keySet()) {
            System.out.println(paramItem);
            System.out.println(paramMap.get(paramItem));
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }


}
