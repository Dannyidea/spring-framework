package org.idea.spring.framework.action;

import org.idea.spring.framework.common.RequestMapping;
import org.idea.spring.framework.common.RestController;

/**
 * @author linhao
 * @date created in 10:38 下午 2020/10/29
 */
@RestController
@RequestMapping(url = "/tests")
public class TestController {


    @RequestMapping(url = "/test2")
    public String test(){
        System.out.println("do test2");
        return "success";
    }
}
