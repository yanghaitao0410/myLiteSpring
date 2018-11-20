package org.litespring.context;

import org.litespring.service.v1.PetStoreService;

public interface ApplicationContext {
    Object getBean(String beanID);
}
