package org.idea.spring.framework.webmvc;

import org.idea.spring.framework.common.util.StringUtils;

import java.io.File;

/**
 * 视图解析对象
 *
 * @author linhao
 * @date created in 8:50 下午 2020/10/24
 */
public class ViewResolver {

    private File templateRootDir;

    /**
     * 不同的模版引擎会有不同的文件后缀格式，这里暂时设置为.html
     */
    private final String DEFAULT_TEMPLATE_SUFFIX=".html";

    public View resolverViewName(String viewName) {
        if(StringUtils.isEmpty(viewName)){
            return null;
        }
        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX)? viewName : viewName+DEFAULT_TEMPLATE_SUFFIX;
        File templateFile = new File((this.templateRootDir.getPath()+"/"+viewName).replaceAll("/+",""));
        return new View(templateFile);
    }


}
