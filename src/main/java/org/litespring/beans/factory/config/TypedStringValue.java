package org.litespring.beans.factory.config;

/**
 * xml中string value类型 property
 */
public class TypedStringValue {
    private final String value;

    public TypedStringValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
