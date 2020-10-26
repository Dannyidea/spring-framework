package org.idea.spring.framework.aop;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author linhao
 * @date created in 8:03 下午 2020/10/25
 */
public class AdviceSupport {

    private Class<?> targetClass;

    private Object target;

    private AopConfig aopConfig;

    private Pattern pointCutClassPattern;

    private Map<Method, Map<String, Advice>> methodMap;

    public AdviceSupport(AopConfig aopConfig) {
        this.aopConfig = aopConfig;
    }

    public boolean pointCutMatch() {
        //匹配相应包目录下边的类
        return this.pointCutClassPattern.matcher(this.targetClass.getName()).matches();
    }

    public void setTargetClass(Class<?> aClass) {
        this.targetClass = aClass;
        parse();
    }

    private void parse() {
        //无反射，不框架
        //无正则，不架构
        //目标对象和代理对象是否满足织入规则

        String pointCutRegex = this.aopConfig.getPointCut()
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\\\.\\*", ".*")
                .replaceAll("\\(", "\\\\(")
                .replaceAll("\\)", "\\\\)");
        //解析出 public .* org.idea.spring.framework.action..*Service..*(.*)
        String pointCutForClassRegex = pointCutRegex.substring(pointCutRegex.lastIndexOf("(") - 4);
        //解析出 org.idea.spring.framework.action..*Service
        this.pointCutClassPattern = Pattern.compile(pointCutForClassRegex.substring(pointCutForClassRegex.lastIndexOf(" ") + 1));

        //建立目标对象和切面逻辑的关系
        methodMap = new HashMap<>();

        Map<String,Method> aspectMethodCache = new HashMap<String,Method>();
        try {
            Class aspectClass = Class.forName(this.aopConfig.getAspectClass());
            for (Method method : aspectClass.getMethods()) {
                aspectMethodCache.put(method.getName(),method);
            }

            Pattern pointPattern = Pattern.compile(pointCutRegex);
            for(Method method: this.targetClass.getMethods()){
                String methodName = method.toString();
                Map<String,Advice> adviceMap = new HashMap<>();
                if(methodName.contains("throws")){
                    methodName = methodName.substring(0,methodName.lastIndexOf("throws")).trim();
                }
                Matcher matcher = pointPattern.matcher(methodName);
                //匹配到目标对象到方法
                if(matcher.find()){
                    if(!(null == this.aopConfig.getAspectBefore() || "".equals(this.aopConfig.getAspectBefore()))){
                        adviceMap.put("before",new Advice(aspectMethodCache.get(methodName),target));
                    }
                    if(!(null == this.aopConfig.getAspectAfter() || "".equals(this.aopConfig.getAspectAfter()))){
                        adviceMap.put("after",new Advice(aspectMethodCache.get(methodName),target));
                    }
                    if(!(null == this.aopConfig.getAspectAfterThrow() || "".equals(this.aopConfig.getAspectAfterThrow()))){
                        Advice advice = new Advice(aspectMethodCache.get(methodName),target);
                        advice.setThrowName(this.aopConfig.getAspectAfterThrowingName());
                        adviceMap.put("afterThrowing",advice);
                    }
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void setTarget(Object instance) {
        this.target = instance;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Object getTarget() {
        return target;
    }

    public AopConfig getAopConfig() {
        return aopConfig;
    }

    public Pattern getPointCutClassPattern() {
        return pointCutClassPattern;
    }

    /**
     * 获取到植入方法
     *
     * @param method
     * @param proxy
     * @param args
     * @return
     */
    public Map<String, Advice> getAdvice(Method method, Object proxy, Object[] args) throws NoSuchMethodException {
        Map<String,Advice> cache = this.methodMap.get(method);
        if( null == cache){
            Method m = targetClass.getMethod(method.getName(),method.getParameterTypes());
            cache = methodMap.get(m);
            this.methodMap.put(method,cache);
        }
        return cache;
    }
}
