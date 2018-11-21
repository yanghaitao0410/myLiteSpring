package org.litespring.beans.factory;

import org.litespring.beans.BeanDefinition;

public class GenericBeanDefinition implements BeanDefinition {
    private String beanId;
    private String className;
    private String scope = SCOPE_DEFAULT;
    private boolean singleton = true;
    private boolean prototype = false;

    public GenericBeanDefinition (String beanId, String className) {
        this.beanId = beanId;
        this.className = className;
    }

    @Override
    public String getBeanClassName() {
        return className;
    }

    @Override
    public boolean isSingleton() {
        return singleton;
    }

    @Override
    public boolean isPrototype() {
        return prototype;
    }

    @Override
    public String getScope() {
        return scope;
    }

    @Override
    public void setScope(String scope) {
        this.scope = scope;
        this.singleton = scope.equals(SCOPE_DEFAULT)|| scope.equals(SCOPE_SINGLETON);
        this.prototype = scope.equals(SCOPE_PROTOTYPE);
    }
}
