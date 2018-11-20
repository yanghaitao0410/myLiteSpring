package org.litespring.context.support;

import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.context.ApplicationContext;
import org.litespring.core.io.Resource;

public abstract class AbstractApplicationContext implements ApplicationContext {

    private DefaultBeanFactory factory;

    public AbstractApplicationContext(String path) {
        factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = getResource(path);
        reader.loadBeanDefinitions(resource);
    }

    protected abstract Resource getResource(String path);

    @Override
    public Object getBean(String beanID) {
        return factory.getBean(beanID);
    }
}
