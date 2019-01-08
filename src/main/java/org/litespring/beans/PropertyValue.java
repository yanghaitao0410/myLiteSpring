package org.litespring.beans;

public class PropertyValue {
    private final String name;
    private final Object value;

    private boolean converted = false;

    //xml中ref对应的实例
    private Object convertedValue;

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    /**
     * 是否已经进行对象转换
     * @return
     */
    public synchronized boolean isConverted() {
        return this.converted;
    }

    public synchronized Object getConvertedValue() {
        return convertedValue;
    }

    public synchronized void setConvertedValue(Object value) {
        this.convertedValue = value;
        converted = true;
    }
}
