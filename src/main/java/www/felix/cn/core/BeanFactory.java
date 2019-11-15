package www.felix.cn.core;

/**
 * @program: spring-learn
 * @description:
 * @author: WangJie
 * @create: 2019-11-15 11:27
 **/
public interface BeanFactory {

    Object getBean(String beanName) throws Exception;
    Object getBean(Class<?> beanClass) throws Exception;
}
