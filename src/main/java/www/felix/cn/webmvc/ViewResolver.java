package www.felix.cn.webmvc;

import java.io.File;
import java.util.Locale;

/**
 * @program: spring-learn
 * @description:
 * @author: WangJie
 * @create: 2019-11-15 17:09
 **/
public class ViewResolver {
    private final  String DEFAULT_TEMPLATE_SUFFIX = ".html";

    private File templateRootDir;
    private String viewName;

    public ViewResolver() {
    }

    public ViewResolver(String templateRoot) {
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        this.templateRootDir = new File(templateRoot);
    }
    public View resolveViewName(String viewName, Locale locale){
        this.viewName = viewName;
        if (null == viewName ||"".equals(viewName.trim())){
            return null;
        }
        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX)?viewName:viewName+DEFAULT_TEMPLATE_SUFFIX;
        File templateFile = new File((templateRootDir.getPath()+"/"+viewName).replaceAll("/+","/"));
        return new View(templateFile);
    }

    public String getViewName() {
        return viewName;
    }
}
