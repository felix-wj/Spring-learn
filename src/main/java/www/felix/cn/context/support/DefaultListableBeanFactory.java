package www.felix.cn.context.support;

import www.felix.cn.beans.BeanDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: spring-learn
 * @description:
 * @author: WangJie
 * @create: 2019-11-15 11:41
 **/
public class DefaultListableBeanFactory extends AbstractApplicationContext {
    protected final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
}
