package www.felix.cn.annotation;

import java.lang.annotation.*;

/**
 * @program: spring-learn
 * @description:
 * @author: WangJie
 * @create: 2019-11-15 11:25
 **/
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
}
