package org.idea.spring.framework.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author linhao
 * @date created in 8:04 下午 2020/10/25
 */
public class JDKDynamicAopProxy implements InvocationHandler {

    private AdviceSupport adviceSupport;

    public JDKDynamicAopProxy(AdviceSupport adviceSupport) {
        this.adviceSupport = adviceSupport;
    }

    public Object getProxy() {
        return Proxy.newProxyInstance(this.getClass().getClassLoader(),
                this.adviceSupport.getTargetClass().getInterfaces(),
                //默认会回调到this对象里面的invoke函数
                this);
    }

    //代码织入的过程
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Map<String, Advice> adviceMap = this.adviceSupport.getAdvice(method, proxy, args);
        this.doAdvice(adviceMap.get("before"));
        //真正调用目标对象的代码
        Object value = null;
        try {
            value = method.invoke(this.adviceSupport.getTarget(), args);
        }catch (Exception e){
            this.doAdvice(adviceMap.get("afterThrowing"));
            e.printStackTrace();
        }
        this.doAdvice(adviceMap.get("after"));
        return value;
    }

    private void doAdvice(Advice advice) throws InvocationTargetException, IllegalAccessException {
        if(advice==null){
            return;
        }
        advice.getAdviceMethod().invoke(advice.getAspect());
    }

}
