package www.felix.cn.webmvc;

import java.io.File;

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
}
