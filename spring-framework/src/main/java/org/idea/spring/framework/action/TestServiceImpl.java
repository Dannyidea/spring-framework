package org.idea.spring.framework.action;

import org.idea.spring.framework.annotation.IService;

/**
 * @author linhao
 * @date created in 2:08 下午 2020/10/24
 */
@IService
public class TestServiceImpl implements TestService{


    @Override
    public void doTest() {
        System.out.println("this is doTest");
    }

    @Override
    public void doEcho(int id) {
        System.out.println("this is echo1"+id);
    }
}
