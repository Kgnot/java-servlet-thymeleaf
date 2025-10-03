package org.server.config.beans;

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

    /**
     * Obtiene un bean registrado en el contenedor según su clase.
     *
     * @param <T>       el tipo del bean
     * @param beanClass la clase del bean a obtener
     * @return la instancia del bean si está registrada, o {@code null} en caso contrario
     */
    <T> T getBean(Class<T> beanClass);

    /**
     * Registra una instancia de un bean en el contenedor.
     *
     * @param <T>         el tipo del bean
     * @param beanClass   la clase del bean
     * @param instanceBean la instancia concreta del bean a registrar
     */
    <T> void registerBean(Class<T> beanClass, T instanceBean);

    /**
     * Registra una clase de bean en el contenedor sin instancia inmediata.
     * Generalmente se utilizará para instanciar el bean más adelante.
     *
     * @param <T>       el tipo del bean
     * @param beanClass la clase del bean a registrar
     */
    <T> void registerBeanClass(Class<T> beanClass);

    /**
     * Verifica si una clase de bean está registrada en el contenedor.
     *
     * @param <T>             el tipo del bean
     * @param beanClassSearched la clase del bean que se desea verificar
     * @return {@code true} si la clase del bean está registrada, {@code false} en caso contrario
     */
    <T> boolean isBeanClassRegistered(Class<T> beanClassSearched);

    /**
     * Obtiene todas las clases de beans que estén anotadas con una anotación específica.
     *
     * @param annotation la anotación que se desea buscar en los beans registrados
     * @return una lista de clases que tienen la anotación indicada
     */
    List<Class<?>> getBeansByAnnotation(Class<? extends Annotation> annotation);

    /**
     * Inyecta las dependencias de un bean dado, resolviendo sus dependencias
     * a partir del contenedor.
     *
     * @param bean la instancia del objeto en el cual se inyectarán los beans necesarios
     */
    void injectBean(Object bean);
}
