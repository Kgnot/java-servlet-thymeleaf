package org.server.config.beans;

import org.server.config.beans.Bean.BeanType;
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

    /**
     * Mapa de beans instanciados, accesibles por su clase.
     */
    private final Map<BeanType<?>, Object> beans = new ConcurrentHashMap<>(); // un tipo y una instancia

    /**
     * Conjunto de clases registradas como beans, aún sin instanciar.
     */
    private final Set<Class<?>> beansClass = ConcurrentHashMap.newKeySet();


    @Override
    public <T> T getBean(Class<T> beanClass, String beanName) {
        return beanClass.cast(beans.get(new BeanType<T>(beanClass, beanName)));
    }


    @Override
    public <T> void registerBean(BeanType<T> beanType, T instanceBean) {
        beans.put(beanType, instanceBean);
    }

    @Override
    public <T> T getBeanByType(Class<T> beanClass) {
        return beans.entrySet().stream()
                .filter(entry -> beanClass.isAssignableFrom(entry.getKey().getClazz()))
                .map(entry -> beanClass.cast(entry.getValue()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Registra una clase como bean, sin instanciarla inmediatamente.
     *
     * @param beanClass clase del bean
     * @param <T>       tipo genérico del bean
     */
    @Override
    public <T> void registerBeanClass(Class<T> beanClass) {
        //Aquí es el conjunto SET
        beansClass.add(beanClass);
    }

    /**
     * Verifica si una clase está registrada como bean.
     *
     * @param beanClassSearched clase a verificar
     * @param <T>               tipo genérico
     * @return {@code true} si la clase está registrada, {@code false} en caso contrario
     */
    @Override
    public <T> boolean isBeanClassRegistered(Class<T> beanClassSearched) {
        // Esto es del conjunto SET
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
    public List<BeanType<?>> getBeansByAnnotation(Class<? extends Annotation> annotation) {
        return beans.keySet()
                .stream()
                .filter(beanClass -> beanClass.getClazz().isAnnotationPresent(annotation))
                .collect(Collectors.toList());
    }


    @Override
    public void injectBean(Object instance) { // bean es la referencia al objeto
        for (Field field : instance.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {

                injectField(instance, field);
            }
        }
    }


    private void injectField(Object instance, Field field) {
        Class<?> fieldType = field.getType();

        // Obtener el nombre específico desde @Inject si existe
        Inject injectAnnotation = field.getAnnotation(Inject.class);
        String specificBeanName = injectAnnotation.value();

        Object dependency;

        if (!specificBeanName.isEmpty()) {
            // Buscar por tipo Y nombre específico (considerando herencia)
            dependency = findBeanByTypeAndName(fieldType, specificBeanName);
            if (dependency == null) {
                LOGGER.warning("No se encontró bean " + fieldType.getSimpleName() +
                        " con nombre: " + specificBeanName);
                return;
            }
        } else {
            // Buscar solo por tipo
            dependency = findFirstBeanByType(fieldType);
            if (dependency == null) {
                LOGGER.warning("No se encontró bean para tipo: " + fieldType.getSimpleName());
                return;
            }
        }

        field.setAccessible(true);
        try {
            field.set(instance, dependency);
        } catch (IllegalAccessException e) {
            LOGGER.log(Level.WARNING, "No se pudo inyectar " + fieldType.getSimpleName(), e);
            throw new RuntimeException("Error inyectando bean: " + fieldType.getSimpleName(), e);
        }
    }

    // Nuevo método que busca por tipo (considerando herencia) Y nombre
    private Object findBeanByTypeAndName(Class<?> type, String beanName) {
        return beans.entrySet().stream()
                .filter(entry -> type.isAssignableFrom(entry.getKey().getClazz()) &&
                        entry.getKey().getSpecificBean().equals(beanName))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    private Object findFirstBeanByType(Class<?> type) {
        return beans.entrySet().stream()
                .filter(entry -> type.isAssignableFrom(entry.getKey().getClazz()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }
}
