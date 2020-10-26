package org.idea.spring.framework.webmvc;

import org.idea.spring.framework.annotation.*;
import org.idea.spring.framework.context.ApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.idea.spring.framework.common.constants.CommonsConstants.BASE_SERVLET;
import static org.idea.spring.framework.common.constants.CommonsConstants.WEB_PREFIX;
import static org.idea.spring.framework.common.util.StringUtils.toLowerFirstName;
import static org.idea.spring.framework.webmvc.ViewResolver.DEFAULT_TEMPLATE_SUFFIX;

/**
 * @author linhao
 * @date created in 8:55 上午 2020/10/20
 */
public class MyDispatcherServlet extends HttpServlet {

    private List<HandlerMapping> handlerMappings = new ArrayList<>();

    private Map<HandlerMapping, HandlerAdapter> handlerAdapterMap = new HashMap<>();

    private ApplicationContext applicationContext;

    private List<ViewResolver> viewResolvers = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //负责请求的分发
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            Map<String, Object> exceptionMap = new HashMap<>(2);
            exceptionMap.put("errorMsg", "500 exception detail");
            exceptionMap.put("stackTrace",((InvocationTargetException) e).getTargetException().toString());
            processDispatchResult(req, resp, new ModelAndView("500", exceptionMap));
            e.printStackTrace();
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws InvocationTargetException, IllegalAccessException {
        HandlerMapping handlerMapping = getHandler(req);
        if (handlerMapping == null) {
            //返回404响应
            System.out.println("===404 response===");
            processDispatchResult(req, resp, new ModelAndView("404"));
            return;
        }
        HandlerAdapter handlerAdapter = getHandlerAdapter(handlerMapping);
        //根据请求参数获取参数匹配器，然后返回相关modelandview
        ModelAndView modelAndView = handlerAdapter.handle(req, resp, handlerMapping);
        //根据modelAndView来选择使用哪个viewResolver进行解析和渲染
        processDispatchResult(req, resp, modelAndView);
    }

    /**
     * 获取到modelAndView之后需要做视图的解析
     *
     * @param req
     * @param resp
     * @param modelAndView
     */
    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, ModelAndView modelAndView) {

        if (modelAndView == null) {
            return;
        }
        if (this.viewResolvers.isEmpty()) {
            return;
        }

        //每个viewResolver就是一个页面
        for (ViewResolver viewResolver : viewResolvers) {
            String viewName = modelAndView.getViewName();
            viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX) ? viewName : viewName + DEFAULT_TEMPLATE_SUFFIX;
            if (!viewResolver.getViewName().equals(viewName)) {
                continue;
            }
            View view = viewResolver.resolverViewName(modelAndView.getViewName());
            try {
                //核心渲染页面的模块
                view.render(modelAndView.getModel(), req, resp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private HandlerAdapter getHandlerAdapter(HandlerMapping handlerMapping) {
        if (this.handlerAdapterMap.isEmpty()) {
            return null;
        }
        return this.handlerAdapterMap.get(handlerMapping);
    }

    private HandlerMapping getHandler(HttpServletRequest req) {
        String url = req.getRequestURI();
        for (HandlerMapping handlerMapping : handlerMappings) {
            Matcher matcher = handlerMapping.getPattern().matcher(url);
            if (!matcher.matches()) {
                continue;
            }
            return handlerMapping;
        }
        return null;
    }

//    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        String url = req.getRequestURI();
//        String contextPath = req.getContextPath();
//
//        url = url.replaceAll(WEB_PREFIX, "").replaceAll(contextPath, "").replaceAll("/+", "/");
//        boolean containUrl = false;
//        HandlerMapping matchHandlerMapping = null;
//        for (HandlerMapping handlerMapping : this.handlerMappings) {
//            if (handlerMapping.getPattern().matcher(url).matches()){
//                containUrl = true;
//                matchHandlerMapping = handlerMapping;
//            }
//        }
//        if (!containUrl) {
//            resp.getWriter().write("404 not found");
//            return;
//        }
//        //参数map 参数名称 参数值是一个数组
//        Map<String, String[]> params = req.getParameterMap();
//        Method method = matchHandlerMapping.getMethod();
//
//        Class<?>[] parametersTypes = method.getParameterTypes();
//        Object[] parametersValues = new Object[parametersTypes.length];
//        Annotation[][] annotations = method.getParameterAnnotations();
//        for (int i = 0; i < parametersTypes.length; i++) {
//            Class<?> parameterType = parametersTypes[i];
//            if (parameterType == HttpServletRequest.class) {
//                parametersValues[i] = req;
//            } else if (parameterType == HttpServletResponse.class) {
//                parametersValues[i] = resp;
//
//            } else if (parameterType == String.class) {
//                for (Annotation annotation : annotations[i]) {
//                    if (annotation instanceof IRequestParam) {
//                        String paramName = ((IRequestParam) annotation).name();
//                        String value = Arrays.toString(params.get(paramName))
//                                .replaceAll("\\[|\\]", "")
//                                .replaceAll("\\s", "");
//                        System.out.println(value);
//                        parametersValues[i] = value;
//                    }
//                }
//            } else if (parameterType == Integer.class) {
//
//            } else if (parameterType == Long.class) {
//
//            }
//            parametersValues[i] = null;
//        }
//
//        String beanName = toLowerFirstName(method.getDeclaringClass().getSimpleName());
//        try {
//            //这一步骤比较耗时，其实可以通过封装一个对象来进行优化
//            method.invoke(this.applicationContext.getBean(beanName), parametersValues);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    @Override
    public void init(ServletConfig servletConfig) {

        //============== IoC and DI =================
        applicationContext = new ApplicationContext("application.properties");

        //==============MVC=================
        this.initStrategies(applicationContext);

        System.out.println("idea-spring-framework is init");

    }

    /**
     * 初始化原先的9大组件
     *
     * @param context
     */
    protected void initStrategies(ApplicationContext context) {
        //初始化url映射
        initHandlerMapping(context);
        //初始化参数包装器
        initHandlerAdapters(context);
        //初始化视图组件
        initViewResolvers(context);
    }

    private void initHandlerAdapters(ApplicationContext context) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            handlerAdapterMap.put(handlerMapping, new HandlerAdapter());
        }
    }

    private void initViewResolvers(ApplicationContext context) {
        String templateRoot = getConfig().getProperty("templateRoot");
        String templateFilePath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        File newFile = new File(templateFilePath);
        for (File file : newFile.listFiles()) {
            if (file.isFile()) {
                //将html文件加载到viewResolver里面去
                viewResolvers.add(new ViewResolver(file));
            }
        }


    }

    private void initHandlerMapping(ApplicationContext applicationContext) {
        if (applicationContext.getBeanDefinitionCount() == 0) {
            return;
        }
        String[] beanDefinitionNamesArr = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanDefinitionNamesArr) {
            Object instance = applicationContext.getBean(beanName);
            Class<?> clazz = instance.getClass();
            if (clazz.isAnnotationPresent(IController.class)) {

                String baseUrl = "";
                //controller头部上边的baseUrl
                if (clazz.isAnnotationPresent(IRequestMapping.class)) {
                    baseUrl = clazz.getAnnotation(IRequestMapping.class).url();
                }
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (!method.isAnnotationPresent(IRequestMapping.class)) {
                        continue;
                    }
                    IRequestMapping iRequestMapping = method.getAnnotation(IRequestMapping.class);
                    //使用正则表达式替换多于斜杠
                    String url = ("/"+BASE_SERVLET + "/" + WEB_PREFIX + "/" + baseUrl + "/" + iRequestMapping.url()).trim().replaceAll("/+", "/");
                    Pattern pattern = Pattern.compile(url);
                    HandlerMapping handlerMapping = new HandlerMapping(pattern, instance, method);
                    handlerMappings.add(handlerMapping);
                    System.out.println("Mapped:" + url + "," + method);
                }
            }
        }

    }

    private Properties getConfig() {
        return this.applicationContext.getConfig();
    }

}
