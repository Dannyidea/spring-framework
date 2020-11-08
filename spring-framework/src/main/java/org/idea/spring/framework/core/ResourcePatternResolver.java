package org.idea.spring.framework.core;

/**
 * @author linhao
 * @date created in 5:40 下午 2020/11/8
 */
public interface ResourcePatternResolver {

    /**
     * 加载资源文件信息
     *
     * @param path
     * @return
     */
    Resource[] getResources(String[] path);
}
