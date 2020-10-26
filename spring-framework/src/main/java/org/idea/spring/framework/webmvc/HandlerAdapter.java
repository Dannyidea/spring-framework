package org.idea.spring.framework.webmvc;

import org.idea.spring.framework.annotation.IRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import static org.idea.spring.framework.common.util.StringUtils.toLowerFirstName;

/**
 * @author linhao
 * @date created in 8:37 下午 2020/10/24
 */
public class HandlerAdapter {

    /**
     * 完成动态参数的匹配
     *
     * @param req
     * @param resp
     * @param handlerMapping
     * @return
     */
    public ModelAndView handle(HttpServletRequest req, HttpServletResponse resp, HandlerMapping handlerMapping) throws InvocationTargetException, IllegalAccessException {
        //参数map 参数名称 参数值是一个数组
        Map<String, String[]> params = req.getParameterMap();
        Method method = handlerMapping.getMethod();

        Class<?>[] parametersTypes = method.getParameterTypes();
        Object[] parametersValues = new Object[parametersTypes.length];
        Annotation[][] annotations = method.getParameterAnnotations();
        for (int i = 0; i < parametersTypes.length; i++) {
            Class<?> parameterType = parametersTypes[i];
            if (parameterType == HttpServletRequest.class) {
                parametersValues[i] = req;
            } else if (parameterType == HttpServletResponse.class) {
                parametersValues[i] = resp;

            } else if (parameterType == String.class) {
                for (Annotation annotation : annotations[i]) {
                    if (annotation instanceof IRequestParam) {
                        String paramName = ((IRequestParam) annotation).name();
                        String value = Arrays.toString(params.get(paramName))
                                .replaceAll("\\[|\\]", "")
                                .replaceAll("\\s", "");
                        System.out.println(value);
                        parametersValues[i] = value;
                    }
                }
            } else if (parameterType == Integer.class) {

            } else if (parameterType == Long.class) {

            }
            parametersValues[i] = null;
        }
        //这一步骤比较耗时，其实可以通过封装一个对象来进行优化
        Object result = method.invoke(handlerMapping.getController(), parametersValues);
        if(result==null || result.getClass().isInstance(Void.class)){
            return null;
        }
        Class<?> returnType = handlerMapping.getMethod().getReturnType();
        if(returnType == ModelAndView.class){
            return (ModelAndView) result;
        }
//        if(returnType.isInstance(Object.class)){
//            return
//        }
        return null;
    }
}
