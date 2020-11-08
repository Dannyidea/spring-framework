package org.idea.spring.framework.core;

import org.idea.spring.framework.common.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * 按照路径进行文件资源加载
 *
 * @author linhao
 * @date created in 4:53 下午 2020/11/8
 */
public class PathMatcher implements ResourcePatternResolver {


    @Override
    public Resource[] getResources(String[] paths) {
        Resource[] resources = new Resource[10];
        for (String path : paths) {
////            URL url = this.getClass(x).getResource(path);
////            String filePath = url.getFile();
//            if (StringUtils.isEmpty(filePath)) {
//                return null;
//            }
            if(path.endsWith(".properties")){
                InputStream in = this.getClass().getClassLoader().getResourceAsStream(path);
                try {
                    Resource propertiesResource = new PropertiesResource(in);
                    resources[0]=(propertiesResource);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return resources;
    }

}
