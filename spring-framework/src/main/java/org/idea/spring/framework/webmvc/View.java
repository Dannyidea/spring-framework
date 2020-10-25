package org.idea.spring.framework.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 视图渲染层
 *
 * @author linhao
 * @date created in 9:34 下午 2020/10/24
 */
public class View {

    private File templateFile;


    public View(File templateFile) {
        this.templateFile = templateFile;
    }

    /**
     * 渲染数据信息
     *
     * @param model
     * @param request
     * @param response
     */
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuffer stb = new StringBuffer();
        RandomAccessFile randomAccessFile = new RandomAccessFile(this.templateFile, "r");
        String line = null;
        while (null != (line = randomAccessFile.readLine())) {
            line = new String(line.getBytes("ISO-8859-1"),"UTF-8");
            Pattern pattern = Pattern.compile("¥\\{[^\\}]+\\}",Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()){
                String paramName = matcher.group();
                paramName = paramName.replaceAll("¥\\{|\\}","");
                Object paramValue = model.get(paramName);
                line = matcher.replaceFirst(String.valueOf(paramValue));
                matcher = pattern.matcher(line);
            }
            stb.append(line);
        }
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(String.valueOf(stb));
    }
}
