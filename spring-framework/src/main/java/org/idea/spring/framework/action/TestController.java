package org.idea.spring.framework.action;

import org.idea.spring.framework.annotation.Autowired;
import org.idea.spring.framework.annotation.IController;
import org.idea.spring.framework.annotation.IRequestMapping;
import org.idea.spring.framework.annotation.IRequestParam;
import org.idea.spring.framework.webmvc.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


/**
 * @author linhao
 * @date created in 11:13 下午 2020/10/22
 */
@IController
@IRequestMapping(url = "/tc")
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    private TestService testService2;

    @IRequestMapping(url = "/test")
    public String doTest(@IRequestParam(name = "name") String name,
                         @IRequestParam(name = "id") String id){
        System.out.println("do test");
        testService2.doEcho(2);
        return "success-1";
    }

    @IRequestMapping(url = "/test2")
    public String doTest2(@IRequestParam(name = "name") String name,
                         @IRequestParam(name = "id") String c){
        System.out.println("=========do test=========");
        testService.doTest();
        testService.doEcho(1);
        testService2.doEcho(2);
        return "success-2";
    }

    @IRequestMapping(url = "/test3")
    public ModelAndView doTest3(HttpServletResponse response){
        System.out.println("this is model and view test");
        Map result = new HashMap();
        result.put("response",response);
        result.put("hello","hello this is hello world");
        throw new NullPointerException();
//        return new ModelAndView("hello.html",result);
    }

    @IRequestMapping(url = "/index")
    public ModelAndView index(){
        Map result = new HashMap(2);
        result.put("username","idea");
        result.put("word","hello this is hello world");
        return new ModelAndView("index",result);
    }
}
