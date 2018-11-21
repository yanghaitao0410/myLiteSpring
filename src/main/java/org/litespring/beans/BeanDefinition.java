package org.litespring.beans;

public interface BeanDefinition {
    String SCOPE_DEFAULT = "";
    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";

    String getBeanClassName();

    boolean isSingleton();

    boolean isPrototype();

    String getScope();

    void setScope(String scope);
}
