package www.felix.cn.webmvc;

import lombok.Data;

import java.util.Map;

/**
 * @program: spring-learn
 * @description:
 * @author: WangJie
 * @create: 2019-11-15 17:08
 **/
@Data
public class ModeAndView {

    private String viewName;

    private Map<String,?> model;

    public ModeAndView(String viewName) {
        this.viewName = viewName;
    }

    public ModeAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }
}
