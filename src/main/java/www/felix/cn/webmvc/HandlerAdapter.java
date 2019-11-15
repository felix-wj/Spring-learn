package www.felix.cn.webmvc;

/**
 * @program: spring-learn
 * @description:
 * @author: WangJie
 * @create: 2019-11-15 17:06
 **/
public class HandlerAdapter {

    public boolean supports(Object handler){
        return handler instanceof HandlerMapping;
    }

    public ModeAndView
}
