package www.felix.cn.webmvc;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: spring-learn
 * @description:
 * @author: WangJie
 * @create: 2019-11-14 17:19
 **/
public class DispatcherServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("get the request");
        resp.getWriter().write("get the request,url:"+req.getRequestURL());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("get the request");
        resp.getWriter().write("get the request,url:"+req.getRequestURL());
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("---init---");
        super.init(config);
    }
}
