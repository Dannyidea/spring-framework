package org.idea.spring.framework.webmvc;

import java.util.Map;

/**
 * @author linhao
 * @date created in 9:04 下午 2020/10/24
 */
public class ModelAndView {

    private String viewName;
    private Map<String,?> model;

    public ModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public ModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public Map<String, ?> getModel() {
        return model;
    }
}
