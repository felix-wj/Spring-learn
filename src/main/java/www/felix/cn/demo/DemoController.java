package www.felix.cn.demo;

import www.felix.cn.annotation.Autowired;
import www.felix.cn.annotation.Controller;
import www.felix.cn.annotation.RequestMapping;
import www.felix.cn.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: spring-learn
 * @description:
 * @author: WangJie
 * @create: 2019-11-17 17:28
 **/
@Controller
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private DemoService demoService;
    @RequestMapping("/print")
    public void print(@RequestParam("name") String name, HttpServletResponse response) throws IOException {
        response.getWriter().write("name:"+name);
    }
}
