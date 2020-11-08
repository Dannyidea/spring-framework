package org.idea.spring.framework.core;

import jdk.internal.util.xml.impl.Input;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * @author linhao
 * @date created in 5:44 下午 2020/11/8
 */
public  class PropertiesResource implements Resource {

    private Properties properties = new Properties();

    private String filePath;

    private String fileName;

    private InputStream inputStream;

    private PropertiesResource() {
    }

    public PropertiesResource(InputStream inputStream) throws IOException {
        this.properties.load(inputStream);
        this.inputStream = inputStream;
    }

    @Override
    public boolean isExists() {
        return properties != null;
    }

    @Override
    public String getFilename() {
        return fileName;
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public boolean isOpen() {
        return this.inputStream!=null;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
