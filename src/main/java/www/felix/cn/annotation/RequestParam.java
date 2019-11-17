package www.felix.cn.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @program: spring-learn
 * @description:
 * @author: WangJie
 * @create: 2019-11-17 16:42
 **/
@Documented
@Target({ElementType.PARAMETER})
public @interface RequestParam {
    String value();
}
