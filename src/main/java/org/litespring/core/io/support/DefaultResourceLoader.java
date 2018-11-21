package org.litespring.core.io.support;

import org.litespring.core.io.ResourceLoader;
import org.litespring.utils.ClassUtils;

public abstract class DefaultResourceLoader implements ResourceLoader {
    private ClassLoader classLoader;

    @Override
    public ClassLoader getBeanClassLoader() {
        return classLoader == null ? ClassUtils.getDefaultClassLoader() : classLoader;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

}
