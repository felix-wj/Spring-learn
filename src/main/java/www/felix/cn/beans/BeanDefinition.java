package www.felix.cn.beans;

import lombok.Data;

/**
 * @program: spring-learn
 * @description:
 * @author: WangJie
 * @create: 2019-11-15 11:29
 **/
@Data
public class BeanDefinition {
    /**
     * 原生bean全路径类名
     */
    private String beanClassName;

    private boolean lazyInit = false;

    /**
     * bean保存在IOC容器中的key
     */
    private String factoryBeanName;

}
