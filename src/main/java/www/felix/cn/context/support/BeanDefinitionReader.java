package www.felix.cn.context.support;

import www.felix.cn.annotation.Controller;
import www.felix.cn.annotation.Service;
import www.felix.cn.beans.BeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @program: spring-learn
 * @description:
 * @author: WangJie
 * @create: 2019-11-15 11:53
 **/
public class BeanDefinitionReader {
    private List<String> registyBeanClasses = new ArrayList<>();
    private Properties config = new Properties();
    private final String SCAN_PACKAGE = "scanPackage";

    public BeanDefinitionReader(String... locations) {
        try(
                InputStream is = this.getClass().getClassLoader().getResourceAsStream(locations[0].replace("classpath:",""))
        ) {
            config.load(is);
        }catch (IOException e){
            e.printStackTrace();
        }
        doScanner(config.getProperty(SCAN_PACKAGE));
    }

    private void doScanner(String scanPackage) {
        URL url = this.getClass().getClassLoader().getResource("/"+scanPackage.replaceAll("\\.","/"));
        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()){
            if (file.isDirectory()){
                doScanner(scanPackage+"."+file.getName());
            }else {
                if (!file.getName().contains(".class")){
                    continue;
                }
                String className = scanPackage+"."+file.getName().replace(".class","");
                registyBeanClasses.add(className);
            }
        }
    }

    public List<BeanDefinition> loadBeanDefinitions(){
        List<BeanDefinition> beanDefinitions = new ArrayList<>();
        try{
            for (String registyBeanClass : registyBeanClasses) {
                Class<?> beanClass = Class.forName(registyBeanClass);
                if (beanClass.isInterface()){
                    continue;
                }
                if (beanClass.isAnnotationPresent(Controller.class)||beanClass.isAnnotationPresent(Service.class)) {
                    beanDefinitions.add(doCreateBeanDefintion(toLowerFirstCase(beanClass.getSimpleName()), beanClass.getName()));
                    Class<?>[] interfaces = beanClass.getInterfaces();
                    for (Class<?> anInterface : interfaces) {
                        beanDefinitions.add(doCreateBeanDefintion(anInterface.getName(), beanClass.getName()));
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return beanDefinitions;
    }

    private BeanDefinition doCreateBeanDefintion(String factoryBeanName, String beanClassName) {
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClassName(beanClassName);
        beanDefinition.setFactoryBeanName(factoryBeanName);
        return beanDefinition;
    }

    private String toLowerFirstCase(String name) {
        char[] chars = name.toCharArray();
        chars[0]+=32;
        return String.valueOf(chars);
    }

    public Properties getConfig() {
        return config;
    }

}
