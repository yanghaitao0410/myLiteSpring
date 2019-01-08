package org.litespring.beans.factory.config;

/**
 * xml中ref类型property
 */
public class RuntimeBeanReference {
    private String refName;

    public RuntimeBeanReference(String refName) {
        this.refName = refName;
    }

    public String getRefName() {
        return this.refName;
    }

}
