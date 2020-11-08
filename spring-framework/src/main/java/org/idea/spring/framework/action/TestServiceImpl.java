package org.idea.spring.framework.action;

import org.idea.spring.framework.annotation.Autowired;
import org.idea.spring.framework.annotation.IService;
import org.idea.spring.framework.aspect.LogAspect;

/**
 * @author linhao
 * @date created in 2:08 下午 2020/10/24
 */
@IService
public class TestServiceImpl implements TestService{

    @Autowired
    private LogAspect logAspect;

    @Override
    public void doTest() {
        logAspect.doBefore();
        System.out.println("this is doTest");
        logAspect.doAfter();

    }

    @Override
    public void doEcho(int id) {
        System.out.println("this is echo1"+id);
//        throw new RuntimeException("err");
    }
}
