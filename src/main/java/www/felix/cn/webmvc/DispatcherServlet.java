package www.felix.cn.webmvc;

import lombok.extern.slf4j.Slf4j;
import www.felix.cn.annotation.Controller;
import www.felix.cn.annotation.RequestMapping;
import www.felix.cn.context.ApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: spring-learn
 * @description:
 * @author: WangJie
 * @create: 2019-11-14 17:19
 **/
@Slf4j
public class DispatcherServlet extends HttpServlet {
    private final String LOCATION = "contextConfigLocation";
    private List<HandlerMapping> handlerMappings = new ArrayList<>();
    private Map<HandlerMapping,HandlerAdapter> handlerAdapters = new HashMap<>();

    private List<ViewResolver> viewResolvers = new ArrayList<>();
    private ApplicationContext context;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            doDispatch(req,resp);
        }catch (Exception e){
            resp.getWriter().write("<font size = '25' color='blue'>500 Exception</font><br/>Details:<br/>"
            + Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]","")
            .replaceAll("\\s","\r\n")+
                    "<font color='green'><i><Copyright@Felix</i></font>");
            e.printStackTrace();
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HandlerMapping handler = getHandler(req);
        if (handler==null){
            processDispatchResult(req,resp,new ModelAndView("404"));
            return;
        }
        HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
        ModelAndView modelAndView = handlerAdapter.handler(req,resp,handler);
        processDispatchResult(req,resp,modelAndView);
    }

    private HandlerAdapter getHandlerAdapter(HandlerMapping handler) {
        if (this.handlerAdapters.isEmpty()){
            return null;
        }
        HandlerAdapter handlerAdapter = this.handlerAdapters.get(handler);
        if (handlerAdapter.supports(handler)){
            return handlerAdapter;
        }
        return null;
    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, ModelAndView modelAndView) throws Exception {
        if (modelAndView == null ){
            return;
        }
        if (this.viewResolvers.isEmpty()){
            return;
        }
        for (ViewResolver viewResolver : this.viewResolvers) {
            View view = viewResolver.resolveViewName(modelAndView.getViewName(), null);
            if (view!=null){
                view.render(modelAndView.getModel(),req,resp);
                return;
            }
        }
    }

    private HandlerMapping getHandler(HttpServletRequest req) {
        if (this.handlerMappings.isEmpty()){
            return null;
        }
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath,"").replaceAll("/+","/");
        for (HandlerMapping handlerMapping : this.handlerMappings) {
            Matcher matcher = handlerMapping.getPattern().matcher(url);
            if (!matcher.matches()){
                continue;
            }
            return handlerMapping;
        }
        return null;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        context = new ApplicationContext(config.getInitParameter(LOCATION));
        initStrategies(context);
    }

    private void initStrategies(ApplicationContext context) {
        //文件上传解析，请求类型是multipart,通过MultipartResolver进行文件上传解析
        initMultipartResolver(context);
        initLocalResolver(context);
        initThemeResolver(context);
        initHandlerMapping(context);
        initHandlerAdapters(context);
        initHandlerExceptionResolvers(context);
        initRequestToViewNameTranslator(context);
        //initViewResolvers(context);
        initFlashMapManager(context);

    }

    private void initViewResolvers(ApplicationContext context) {
        String templateRoot = context.getConfig().getProperty("templateRoot");

        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        File templateDir = new File(templateRootPath);
        for (File file : templateDir.listFiles()) {
            this.viewResolvers.add(new ViewResolver(templateRoot));
        }
    }

    private void initHandlerAdapters(ApplicationContext context) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            this.handlerAdapters.put(handlerMapping,new HandlerAdapter());
        }
    }

    private void initHandlerMapping(ApplicationContext context) {
        String[] beannames = context.getBeanDefinitionNames();
        try{
            for (String beanname : beannames) {
                Object controller = context.getBean(beanname);
                Class<?> clazz = controller.getClass();
                if (!clazz.isAnnotationPresent(Controller.class)){
                    continue;
                }
                String baseurl = "";
                if (clazz.isAnnotationPresent(RequestMapping.class)){
                    RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                    baseurl = requestMapping.value();
                }
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (!method.isAnnotationPresent(RequestMapping.class)){
                        continue;
                    }
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    String regex =("/"+baseurl+requestMapping.value())
                            .replaceAll("\\*",".")
                            .replaceAll("/+","/");
                    Pattern pattern = Pattern.compile(regex);
                    this.handlerMappings.add(new HandlerMapping(controller,method,pattern));
                    log.info("Mapping:{},{}",regex,method);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initThemeResolver(ApplicationContext context) { }
    private void initLocalResolver(ApplicationContext context) { }
    private void initMultipartResolver(ApplicationContext context) { }
    private void initHandlerExceptionResolvers(ApplicationContext context) { }
    private void initRequestToViewNameTranslator(ApplicationContext context) { }
    private void initFlashMapManager(ApplicationContext context) { }


}
