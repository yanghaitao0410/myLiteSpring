package org.litespring.beans.factory.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.GenericBeanDefinition;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;
import org.litespring.beans.factory.support.BeanDefinitionRegistry;
import org.litespring.core.io.Resource;
import org.litespring.utils.ClassUtils;
import org.litespring.utils.StringUtils;

import java.io.InputStream;
import java.util.Iterator;

public class XmlBeanDefinitionReader {
    private static final String ID_ATTRIBUTE = "beanId";
    private static final String CLASS_ATTRIBUTE = "class";
    private static final String SCOPE_ATTRIBUTE = "scope";
    private static final String REF_ATTRIBUTE = "ref";
    private static final String VALUE_ATTRIBUTE = "value";
    public static final String PROPERTY_ELEMENT = "property";
    public static final String NAME_ATTRIBUTE = "name";

    BeanDefinitionRegistry registry;

    protected final Log logger = LogFactory.getLog(getClass());

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    /**
     * 传入xml文件路径，解析文件中的bean
     *
     * @param configFile
     */
    public void registryBeanDefinition(String configFile) {
        ClassLoader loader = ClassUtils.getDefaultClassLoader();
        try (InputStream inStream = loader.getResourceAsStream(configFile)) {
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
            throw new BeanDefinitionStoreException("IOException parsing XML document from " + configFile, e);
        }
    }

    public void registryBeanDefinition(Resource resource) {
        try (InputStream inStream = resource.getInputStream()) {
            SAXReader reader = new SAXReader();
            Document document = reader.read(inStream);
            Element root = document.getRootElement(); //<beans>
            Iterator<Element> iterator = root.elementIterator();
            while (iterator.hasNext()) {
                Element element = iterator.next();
                String beanId = element.attributeValue(ID_ATTRIBUTE);
                String className = element.attributeValue(CLASS_ATTRIBUTE);
                BeanDefinition bd = new GenericBeanDefinition(beanId, className);
                if (element.attribute(SCOPE_ATTRIBUTE) != null) {
                    bd.setScope(element.attributeValue(SCOPE_ATTRIBUTE));
                }
                //解析标签内子标签
                parsePropertyElement(element, bd);

                registry.registryBeanDefinition(beanId, bd);
            }
        } catch (Exception e) {
            throw new BeanDefinitionStoreException("IOException parsing XML document error ", e);
        }
    }

    private void parsePropertyElement(Element node, BeanDefinition bd) {
        Iterator<Element> propertys = node.elementIterator(PROPERTY_ELEMENT);
        while (propertys.hasNext()) {
            Element propElement = propertys.next();
            String propertyName = propElement.attributeValue(NAME_ATTRIBUTE);
            if (!StringUtils.hasLength(propertyName)) {
                logger.fatal("Tag 'property' must have a 'name' attribute");
                return;
            }
            Object val = parsePropertyValue(propElement);
            PropertyValue propertyValue = new PropertyValue(propertyName, val);
            bd.getPropertyValues().add(propertyValue);
        }
    }


    private Object parsePropertyValue(Element node) {
        String propertyName = node.attributeValue(NAME_ATTRIBUTE);
        String elementName = (propertyName != null) ?
                "<property> element for property '" + propertyName + "'" :
                "<constructor-arg> element";

        boolean hasRuntimeRefAttribute = (node.attributeValue(REF_ATTRIBUTE) != null);
        boolean hasTypedStringAttribute = (node.attributeValue(VALUE_ATTRIBUTE) != null);

        if (hasRuntimeRefAttribute) {
            String refName = node.attributeValue(REF_ATTRIBUTE);
            //可能出现设置了属性名，未设置属性值的情况
            if (!StringUtils.hasText(refName)) {
                logger.error(elementName + " contains empty 'ref' attribute");
            }
            RuntimeBeanReference reference = new RuntimeBeanReference(refName);
            return reference;
        } else if(hasTypedStringAttribute) {
            String value = node.attributeValue(VALUE_ATTRIBUTE);
            TypedStringValue valueHolder = new TypedStringValue(value);
            return valueHolder;
        } else {
            throw new RuntimeException(elementName + " must specify a ref or value");
        }
    }


}
