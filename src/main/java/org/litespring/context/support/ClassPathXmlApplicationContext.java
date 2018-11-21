package org.litespring.context.support;

import org.litespring.core.io.support.ClassPathResource;
import org.litespring.core.io.Resource;

public class ClassPathXmlApplicationContext extends AbstractApplicationContext {
    public ClassPathXmlApplicationContext(String configFile) {
        super(configFile);
    }

    @Override
    public Resource getResourceByPath(String path) {
        return new ClassPathResource(path, getBeanClassLoader());
    }
}
