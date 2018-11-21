package org.litespring.beans.factory.xml;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.GenericBeanDefinition;
import org.litespring.beans.factory.support.BeanDefinitionRegistry;
import org.litespring.core.io.Resource;
import org.litespring.utils.ClassUtils;

import java.io.InputStream;
import java.util.Iterator;

public class XmlBeanDefinitionReader {
    private static final String ID_ATTRIBUTE = "beanId";
    private static final String CLASS_ATTRIBUTE = "class";
    private static final String SCOPE_ATTRIBUTE = "scope";

    BeanDefinitionRegistry registry;

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    /**
     * 传入xml文件路径，解析文件中的bean
     * @param configFile
     */
    public void registryBeanDefinition(String configFile) {
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
                registry.registryBeanDefinition(id, new GenericBeanDefinition(id, className));
            }
        } catch (Exception e) {
            throw new BeanDefinitionStoreException("IOException parsing XML document from " + configFile,e);
        }
    }

    public void registryBeanDefinition(Resource resource) {
        try (InputStream inStream= resource.getInputStream()){
            SAXReader reader = new SAXReader();
            Document document = reader.read(inStream);
            Element root = document.getRootElement(); //<beans>
            Iterator<Element> iterator = root.elementIterator();
            while (iterator.hasNext()) {
                Element element = iterator.next();
                String beanId = element.attributeValue(ID_ATTRIBUTE);
                String className = element.attributeValue(CLASS_ATTRIBUTE);
                BeanDefinition bd = new GenericBeanDefinition(beanId, className);
                if(element.attribute(SCOPE_ATTRIBUTE) != null) {
                    bd.setScope(element.attributeValue(SCOPE_ATTRIBUTE));
                }
                registry.registryBeanDefinition(beanId,bd);
            }
        } catch (Exception e) {
            throw new BeanDefinitionStoreException("IOException parsing XML document error ", e);
        }
    }
}
