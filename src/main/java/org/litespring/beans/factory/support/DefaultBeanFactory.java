package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;
import org.litespring.utils.ClassUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 加载bean的默认工厂
 */
public class DefaultBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory, BeanDefinitionRegistry {
    private Map<String, BeanDefinition> beanDefinitionMap;
    private ClassLoader classLoader;

    public DefaultBeanFactory() {
        beanDefinitionMap = new ConcurrentHashMap<>(64);
    }

    @Override
    public Object getBean(String beanID) {
        BeanDefinition beanDefinition = getBeanDefinition(beanID);
        if (beanDefinition == null) {
            return null;
        }
        if (beanDefinition.isSingleton()) {
            if (getSingleton(beanID) == null) {
                registrySingleton(beanID, createBean(beanDefinition));
            }
            return getSingleton(beanID);
        }
        return createBean(beanDefinition);
    }

    private Object createBean(BeanDefinition beanDefinition) {

        //创建bean实例
        Object bean = instantiateBean(beanDefinition);
        //设置bean的属性
        populateBean(beanDefinition, bean);
        return bean;
    }

    private void populateBean(BeanDefinition beanDefinition, Object bean) {
        List<PropertyValue> propertyValues = beanDefinition.getPropertyValues();
        if (propertyValues == null || propertyValues.size() == 0) {
            return;
        }
        BeanDefinitionValueResolver resolver = new BeanDefinitionValueResolver(this);
        try {
            for (PropertyValue propertyValue : propertyValues) {
                String propertyName = propertyValue.getName();
                Object originValue = propertyValue.getValue();
                Object resolvedValue = resolver.resolveValueIfNecessary(originValue); //实际属性值
                //使用jdk 属性信息 将属性值设置到bean中
                BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
                //获取属性描述器
                PropertyDescriptor [] pds = beanInfo.getPropertyDescriptors();
                for(PropertyDescriptor pd : pds) {
                    if(pd.getName().equals(propertyName)) {
                        pd.getWriteMethod().invoke(bean, resolvedValue); //set方法
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            throw new BeanCreationException("Failed to obtain BeanInfo for class[" + beanDefinition.getBeanClassName() + "");
        }


    }

    private Object instantiateBean(BeanDefinition beanDefinition) {
        String beanClassName = beanDefinition.getBeanClassName();
        ClassLoader classLoader = getBeanClassLoader();
        try {
            Class<?> clazz = classLoader.loadClass(beanClassName);
            return clazz.newInstance();
        } catch (Exception e) {
            throw new BeanCreationException("create bean for " + beanClassName + " failed", e);
        }
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanID) {
        return beanDefinitionMap.get(beanID);
    }

    @Override
    public void registryBeanDefinition(String beanId, BeanDefinition definition) {
        beanDefinitionMap.put(beanId, definition);
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public ClassLoader getBeanClassLoader() {
        return classLoader == null ? ClassUtils.getDefaultClassLoader() : classLoader;
    }
}
