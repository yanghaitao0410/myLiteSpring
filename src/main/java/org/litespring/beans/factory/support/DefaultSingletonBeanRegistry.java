package org.litespring.beans.factory.support;

import org.litespring.beans.factory.config.SingletonBeanRegistry;
import org.litespring.utils.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(64);

    @Override
    public void registrySingleton(String beanId, Object singletonObject) {
        Assert.notNull(beanId, "'beanName' must not be null");
        Object oldObject = singletonObjects.get(beanId);
        if (oldObject != null) {
            throw new IllegalStateException("Could not register object [" + singletonObject + "] under bean name '" +
                    beanId + "': there is already object [" + oldObject + "]");
        }
        singletonObjects.put(beanId, singletonObject);
    }

    @Override
    public Object getSingleton(String beanId) {
        return singletonObjects.get(beanId);
    }
}
