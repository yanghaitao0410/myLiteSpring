package org.litespring.core.io;

public interface ResourceLoader  {
    Resource getResourceByPath(String path);
    ClassLoader getBeanClassLoader();
    void setBeanClassLoader(ClassLoader classLoader);
}
