package org.idea.spring.framework.aspect;

import org.idea.spring.framework.annotation.IService;

/**
 * @author linhao
 * @date created in 7:46 下午 2020/10/25
 */
@IService
public class LogAspect {

    public void doBefore(){
        System.out.println("this is before");
    }

    public void doAfter(){
        System.out.println("this is after");
    }

    public void afterThrowing(){
        System.out.println("throw some thing");
    }

}
