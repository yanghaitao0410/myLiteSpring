package org.litespring.beans.factory.support;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.BeanFactory;
import org.litespring.beans.factory.GenericBeanDefinition;
import org.litespring.utils.ClassUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 加载bean的默认工厂
 */
public class DefaultBeanFactory implements BeanFactory {
    private static final String ID_ATTRIBUTE = "id";
    private static final String CLASS_ATTRIBUTE = "class";
    private Map<String, BeanDefinition> beanDefinitionMap;

    public DefaultBeanFactory(String configFile) {
        beanDefinitionMap = new ConcurrentHashMap<>(64);
        analysisFile(configFile);
    }

    /**
     * 传入xml文件路径，解析文件中的bean
     * @param configFile
     */
    private void analysisFile(String configFile) {
        ClassLoader loader = ClassUtils.getDefaultClassLoader();
        try (InputStream inStream= loader.getResourceAsStream(configFile)){
            SAXReader reader = new SAXReader();
            Document document = reader.read(inStream);
            Element root = document.getRootElement(); //<beans>
            Iterator<Element> iterator = root.elementIterator();
            while (iterator.hasNext()) {
                Element element = iterator.next();
                String id = element.attributeValue(ID_ATTRIBUTE);
                String className = element.attributeValue(CLASS_ATTRIBUTE);
                beanDefinitionMap.put(id, new GenericBeanDefinition(id, className));
            }
        } catch (Exception e) {
            throw new BeanDefinitionStoreException("IOException parsing XML document from " + configFile,e);
        }
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanID) {
        return beanDefinitionMap.get(beanID);
    }

    @Override
    public Object getBean(String beanID) {
        BeanDefinition beanDefinition = getBeanDefinition(beanID);
        if(beanDefinition == null) {
            throw new BeanCreationException("Bean Definition does not exist");
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

}
