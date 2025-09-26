package org.server.config.beans;

import java.lang.annotation.Annotation;
import java.util.List;

// La T representa el nombre de la clase o instancia
// Es la forma de los contenedores de los beans
public interface BeanContainer {

    <T> T getBean(Class<T> beanClass);

    <T> void registerBean(Class<T> beanClass, T instanceBean);

    <T> void registerBeanClass(Class<T> beanClass);

    <T> boolean isBeanClassRegistered(Class<T> beanClassSearched);

    List<Class<?>> getBeansByAnnotation(Class<? extends Annotation> annotation);

    void injectBean(Object bean);

}
