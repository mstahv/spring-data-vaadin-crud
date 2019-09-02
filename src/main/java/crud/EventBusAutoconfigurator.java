/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.RouteNotFoundError;
import java.util.HashSet;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.vaadin.spring.events.EventBus;

/**
 * Configures vaadin4spring EventBus listeners automatically. Currently just supports
 * UIEventBus
 * 
 * @author mstahv
 */
public class EventBusAutoconfigurator implements DestructionAwareBeanPostProcessor {

    public HashSet<Object> listeners = new HashSet<>();

    @Autowired
    ApplicationContext applicationContext;

    public EventBusAutoconfigurator() {
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Component) {
            if(bean instanceof RouteNotFoundError) {
                // These gets created a lot, don't know why, bug?
            } else {
                // TODO currently reports if not listener methods are there
                EventBus.UIEventBus bus = applicationContext.getBean(EventBus.UIEventBus.class);
                bus.subscribe(bean);
                listeners.add(bean);                
            }
        }
        return bean;
    }

    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
        listeners.remove(bean);
        EventBus.UIEventBus bus = applicationContext.getBean(EventBus.UIEventBus.class);
        bus.unsubscribe(bean);
    }

    @Override
    public boolean requiresDestruction(Object bean) {
        return listeners.contains(bean);
    }

}
