package www.felix.cn.context;

import lombok.extern.slf4j.Slf4j;
import www.felix.cn.annotation.Autowired;
import www.felix.cn.annotation.Controller;
import www.felix.cn.annotation.Service;
import www.felix.cn.beans.BeanDefinition;
import www.felix.cn.beans.BeanWrapper;
import www.felix.cn.context.config.BeanPostProcessor;
import www.felix.cn.context.support.BeanDefinitionReader;
import www.felix.cn.context.support.DefaultListableBeanFactory;
import www.felix.cn.core.BeanFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: spring-learn
 * @description:
 * @author: WangJie
 * @create: 2019-11-15 11:46
 **/
@Slf4j
public class ApplicationContext  extends DefaultListableBeanFactory implements BeanFactory {

    private String[] configLocations;
    private BeanDefinitionReader reader;
    private Map<String,Object> factoryBeanObjectCache = new ConcurrentHashMap<>();
    private Map<String, BeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<>();

    public ApplicationContext() {
    }

    public ApplicationContext(String... configLocations) {
        this.configLocations = configLocations;
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refresh() throws Exception {
        reader = new BeanDefinitionReader(configLocations);
        List<BeanDefinition> beanDefinitions = reader.loadBeanDefinitions();
        doRegisterBeanDefintion(beanDefinitions);
        doAutowired();
    }

    private void doAutowired() {
        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : super.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            if(!beanDefinitionEntry.getValue().isLazyInit()){
                try {
                    getBean(beanName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doRegisterBeanDefintion(List<BeanDefinition> beanDefinitions) throws Exception {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            if (super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())){
                throw new Exception("The \""+beanDefinition.getFactoryBeanName()+"\" is exist!");
            }
            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(),beanDefinition);
        }
    }

    @Override
    public Object getBean(String beanName) throws Exception {
        BeanDefinition beanDefinition = super.beanDefinitionMap.get(beanName);
        try{
            //生成事件通知
            BeanPostProcessor beanPostProcessor = new BeanPostProcessor();
            Object instance = instantiateBean(beanDefinition);
            if (instance==null){
                return null;
            }
            //实例初始化之前调用
            beanPostProcessor.postProcessBeforeInitialization(instance,beanName);
            BeanWrapper beanWrapper = new BeanWrapper(instance);
            this.factoryBeanInstanceCache.put(beanName,beanWrapper);
            //在实例初始化之后调用
            beanPostProcessor.postProcessAfterInitialization(instance,beanName);
            populateBean(beanName,instance);
            return this.factoryBeanInstanceCache.get(beanName).getWrappedInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void populateBean(String beanName, Object instance) {
        Class clazz = instance.getClass();
        if (!clazz.isAnnotationPresent(Controller.class)||clazz.isAnnotationPresent(Service.class)){
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Autowired.class)){
                continue;
            }
            Autowired autowired = field.getAnnotation(Autowired.class);
            String autowiredBeanName = autowired.value().trim();
            if ("".equals(autowiredBeanName)){
                autowiredBeanName = field.getType().getName();
            }
            field.setAccessible(true);
            try {
                field.set(instance,this.factoryBeanInstanceCache.get(autowiredBeanName).getWrappedInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private Object instantiateBean(BeanDefinition beanDefinition) {
        Object instance = null;
        String className = beanDefinition.getBeanClassName();
        Class<?> clazz = null;
        try {
            if(this.factoryBeanObjectCache.containsKey(className)){
                instance = this.factoryBeanObjectCache.get(className);
            }else {
                clazz = Class.forName(className);
                instance = clazz.newInstance();
                this.factoryBeanObjectCache.put(beanDefinition.getFactoryBeanName(),instance);
            }
            return instance;
        } catch (Exception e){
            System.out.println("---className:"+clazz.getName());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object getBean(Class<?> beanClass) throws Exception {
        return getBean(beanClass.getName());
    }

    public String[] getBeanDefinitionNames(){
        return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }
    public int getBeandefinitionCount(){
        return this.beanDefinitionMap.size();
    }
    public Properties getConfig(){
        return this.reader.getConfig();
    }
}
