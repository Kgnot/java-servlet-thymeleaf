package org.server.config.beans;

import org.server.config.beans.Bean.BeanType;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Interfaz que define un contenedor de beans, responsable de la gestión,
 * registro y obtención de instancias de clases (beans).
 *
 * <p>Un BeanContainer se encarga de almacenar instancias de objetos (beans)
 * y de proporcionar mecanismos para recuperarlos o inyectarlos en otras clases.</p>
 */
public interface BeanContainer {


    <T> T getBean(Class<T> beanClass, String beanName);


    <T> void registerBean(BeanType<T> beanClass, T instanceBean);

    <T> T getBeanByType(Class<T> beanClass);

    <T> void registerBeanClass(Class<T> beanClass);


    <T> boolean isBeanClassRegistered(Class<T> beanClassSearched);


    List<BeanType<?>> getBeansByAnnotation(Class<? extends Annotation> annotation);

    void injectBean(Object bean);
}
