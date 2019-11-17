package www.felix.cn.webmvc;

import lombok.Data;

import java.util.Map;

/**
 * @program: spring-learn
 * @description:
 * @author: WangJie
 * @create: 2019-11-17 12:26
 **/
@Data
public class ModelAndView {
    private String viewName;
    private Map<String,Object> model;

    public ModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public ModelAndView(String viewName, Map<String, Object> model) {
        this.viewName = viewName;
        this.model = model;
    }
}

