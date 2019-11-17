package www.felix.cn.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: spring-learn
 * @description:
 * @author: WangJie
 * @create: 2019-11-17 12:33
 **/
public class View {
    public static  final String DEFAULT_CONTENT_TYPE="text/html;charset=utf-8";
    private File viewFile;

    public View() {
    }

    public View(File viewFile){
        this.viewFile = viewFile;
    }
    public String getContentType(){
        return DEFAULT_CONTENT_TYPE;
    }
    public void render(Map<String,?> model, HttpServletRequest request, HttpServletResponse response)throws Exception{
        StringBuffer stringBuffer = new StringBuffer();
        RandomAccessFile ra = new RandomAccessFile(this.viewFile,"r");
        try{
            String line = null;
            while(null!=(line=ra.readLine())){
                line = new String(line.getBytes("ISO-8859-1"),"utf-8");
                Pattern pattern = Pattern.compile("￥\\{[^\\}]+\\}",Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()){
                    String paramName = matcher.group();
                    paramName = paramName.replaceAll("￥\\{|\\}","");
                    Object paramValue = model.get(paramName);
                    if (null==paramValue){
                        continue;
                    }
                    line = matcher.replaceFirst(makeStringRegExp(paramValue.toString()));
                    matcher = pattern.matcher(line);
                }
                stringBuffer.append(line);
            }
        }finally {
            ra.close();
        }
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(stringBuffer.toString());
    }

    public static String makeStringRegExp(String str) {
        return str.replace("\\","\\\\")
                .replace("+","\\+")
                .replace("|","\\|")
                .replace("*","\\*")
                .replace("{","\\{").replace("}","\\}")
                .replace("(","\\(").replace(")","\\)")
                .replace("^","\\^").replace("$","\\$")
                .replace("[","\\[").replace("]","\\]")
                .replace("^","\\^").replace("$","\\$")
                .replace("?","\\?").replace(",","\\,")
                .replace(".","\\.")
                .replace("&","\\&");
    }
}
