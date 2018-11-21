package org.litespring.core.io.support;

import org.litespring.core.io.Resource;
import org.litespring.utils.ClassUtils;

import java.io.IOException;
import java.io.InputStream;

public class ClassPathResource implements Resource {
    private String path;
    private ClassLoader classLoader;

    public ClassPathResource(String path) {
        this.path = path;
    }

    public ClassPathResource(String path, ClassLoader classLoader) {
        this.path = path;
        this.classLoader = classLoader;
    }

    @Override
    public String getDescription() {
        return this.path;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return classLoader == null ?
                ClassUtils.getDefaultClassLoader().getResourceAsStream(path) :
                classLoader.getResourceAsStream(path);
    }
}
