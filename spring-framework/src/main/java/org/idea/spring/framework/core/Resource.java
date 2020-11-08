package org.idea.spring.framework.core;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * 资源管理接口
 *
 * @author linhao
 * @date created in 10:36 下午 2020/11/7
 */
public interface Resource extends InputStreamResource {

    boolean isExists();

    default boolean isReadable(){
        return this.isExists();
    }

    default boolean isOpen(){
        return false;
    }

    default boolean isFile(){
        return false;
    }

    String getFilename();

}
