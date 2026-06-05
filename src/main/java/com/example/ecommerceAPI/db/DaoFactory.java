package com.example.ecommerceAPI.db;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class DaoFactory {
    private final ApplicationContext context;

    public DaoFactory(ApplicationContext context) {
        this.context = context;
    }

    public <D> D getDao(Class<D> daoInterface) {
        return context.getBean(daoInterface);
    }
}
