package www.felix.cn.context.config;

/**
 * @program: spring-learn
 * @description:
 * @author: WangJie
 * @create: 2019-11-15 15:54
 **/
public class BeanPostProcessor {
    public Object postProcessBeforeInitialization(Object bean,String beanName) throws Exception{
        return bean;
    }
    public Object postProcessAfterInitialization(Object bean, String beanName)throws Exception{
        return bean;
    }
}
