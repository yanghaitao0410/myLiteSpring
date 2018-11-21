package org.litespring.beans.factory.config;

public interface SingletonBeanRegistry {
    void registrySingleton(String beanName, Object object);
    Object getSingleton(String beanName);
}
