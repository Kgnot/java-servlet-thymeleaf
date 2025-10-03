package org.server.config.beans;

import org.server.config.shared.Inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Implementación simple del contenedor de beans.
 *
 * <p>Este contenedor administra instancias de clases (beans) registradas,
 * permitiendo su obtención, registro y resolución de dependencias mediante inyección de campos.</p>
 *
 * <p>Utiliza {@link ConcurrentHashMap} para garantizar la seguridad en entornos concurrentes
 * y soportar acceso seguro a múltiples hilos.</p>
 */
public class SimpleBeanContainer implements BeanContainer {
    private final static Logger LOGGER = Logger.getLogger(SimpleBeanContainer.class.getName());

    /** Mapa de beans instanciados, accesibles por su clase. */
    private final Map<Class<?>, Object> beans = new ConcurrentHashMap<>();

    /** Conjunto de clases registradas como beans, aún sin instanciar. */
    private final Set<Class<?>> beansClass = ConcurrentHashMap.newKeySet();

    /**
     * Obtiene una instancia registrada de un bean.
     *
     * @param beanClass clase del bean que se desea obtener
     * @param <T> tipo genérico del bean
     * @return instancia del bean registrado o {@code null} si no existe
     */
    @Override
    public <T> T getBean(Class<T> beanClass) {
        return beanClass.cast(beans.get(beanClass));
    }

    /**
     * Registra un bean con su clase y una instancia específica.
     *
     * @param beanClass clase del bean
     * @param instanceBean instancia del bean
     * @param <T> tipo genérico del bean
     */
    @Override
    public <T> void registerBean(Class<T> beanClass, T instanceBean) {
        beans.put(beanClass, instanceBean);
    }

    /**
     * Registra una clase como bean, sin instanciarla inmediatamente.
     *
     * @param beanClass clase del bean
     * @param <T> tipo genérico del bean
     */
    @Override
    public <T> void registerBeanClass(Class<T> beanClass) {
        beansClass.add(beanClass);
    }

    /**
     * Verifica si una clase está registrada como bean.
     *
     * @param beanClassSearched clase a verificar
     * @param <T> tipo genérico
     * @return {@code true} si la clase está registrada, {@code false} en caso contrario
     */
    @Override
    public <T> boolean isBeanClassRegistered(Class<T> beanClassSearched) {
        return beansClass.stream()
                .anyMatch(clazz -> clazz.equals(beanClassSearched));
    }

    /**
     * Obtiene todas las clases de beans anotadas con una anotación específica.
     *
     * @param annotation anotación a buscar
     * @return lista de clases anotadas
     */
    @Override
    public List<Class<?>> getBeansByAnnotation(Class<? extends Annotation> annotation) {
        return beans.keySet()
                .stream()
                .filter(beanClass -> beanClass.isAnnotationPresent(annotation))
                .collect(Collectors.toList());
    }

    /**
     * Inyecta las dependencias en un objeto, buscando campos anotados con {@code @Inject}.
     *
     * @param bean objeto en el cual se inyectarán las dependencias
     */
    @Override
    public void injectBean(Object bean) {
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                injectField(bean, field);
            }
        }
    }

    /**
     * Método auxiliar para inyectar un campo específico en un bean.
     *
     * @param bean instancia del bean receptor
     * @param field campo sobre el que se realizará la inyección
     */
    private void injectField(Object bean, Field field) {
        Object dependency = beans.get(field.getType());
        if (dependency != null) {
            field.setAccessible(true);
            try {
                field.set(bean, dependency);
            } catch (IllegalAccessException e) {
                LOGGER.log(Level.WARNING, "No se pudo inyectar la dependencia " + dependency, e);
                throw new RuntimeException("Bean no encontrado: ", e);
            }
        } else {
            LOGGER.warning("No se encontró bean " + field.getType());
        }
    }
}
