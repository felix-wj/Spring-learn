package www.felix.cn.webmvc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @program: spring-learn
 * @description:
 * @author: WangJie
 * @create: 2019-11-15 17:05
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HandlerMapping {
    private Object controller;
    private Method method;
    private Pattern pattern;
}
