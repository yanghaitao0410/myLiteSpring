package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.BeanFactory;
import org.litespring.utils.ClassUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 加载bean的默认工厂
 */
public class DefaultBeanFactory implements BeanFactory,BeanDefinitionRegistry {
    private Map<String, BeanDefinition> beanDefinitionMap;

    public DefaultBeanFactory() {
        beanDefinitionMap = new ConcurrentHashMap<>(64);
    }

    @Override
    public Object getBean(String beanID) {
        BeanDefinition beanDefinition = getBeanDefinition(beanID);
        if(beanDefinition == null) {
            return null;
        }
        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        String beanClassName = beanDefinition.getBeanClassName();
        try {
            Class<?> clazz = classLoader.loadClass(beanClassName);
            return clazz.newInstance();
        } catch (Exception e) {
            throw new BeanCreationException("create bean for "+ beanClassName +" failed",e);
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
}
