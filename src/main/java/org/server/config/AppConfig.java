package org.server.config;

import org.reflections.Reflections;
import org.server.config.shared.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class AppConfig {

    private static final Map<Class<?>, Object> beans = new HashMap<>();
    private static final Logger logger = Logger.getLogger(String.valueOf(AppConfig.class));
    private static final Reflections reflections = new Reflections("org.server");
    private static final boolean detailsLogger = Boolean.parseBoolean(System.getProperty("detailsLogger", "false"));

    static {
        createInstance(Component.class);
        // Create service's instance
        createInstance(Service.class);
        // Create component's instance
        createInstance(Controller.class);
        //Create view render instance
        createInstance(ViewRender.class);
        //create websocket's instance
        createInstance(Websocket.class);
        // Inject dependencies
        for (Object bean : beans.values()) {
            injectDependencies(bean);
        }
        if (detailsLogger) {
            logger.log(Level.INFO, "Los beans registrados: ");
            for (Object bean : beans.values()) {
                logger.log(Level.INFO, "Bean: " + bean);
            }
        }
    }

    private static void createInstance(Class<? extends Annotation> annotationClass) {
        Set<Class<?>> clazz = reflections.getTypesAnnotatedWith(annotationClass);
        for (Class<?> c : clazz) {
            try {
                Constructor<?>[] constructors = c.getConstructors();
                if (constructors.length == 0) continue;

                Constructor<?> constructor = constructors[0]; // puedes mejorar la selección
                Object[] params = Arrays.stream(constructor.getParameterTypes())
                        .map(beans::get)
                        .toArray();

                Object instance = constructor.newInstance(params);
                beans.put(c, instance);
            } catch (Exception e) {
                logger.log(Level.WARNING, c.getName() + "." + e.getMessage());
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        return (T) beans.get(clazz);
    }

    public static List<Class<?>> getTypeClassBean(Class<? extends Annotation> annotation) {
        return beans.keySet()
                .stream()
                .filter(clazz -> clazz.isAnnotationPresent(annotation))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static void injectDependencies(Object bean) {
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                Class<?> dependencyType = field.getType();
                Object dependency = beans.get(dependencyType);

                if (dependency != null) {
                    field.setAccessible(true);
                    try {
                        field.set(bean, dependency);
                    } catch (IllegalAccessException e) {
                        logger.log(Level.WARNING, "Error inyectando en " + bean.getClass().getName() + ": " + e.getMessage());
                    }
                } else {
                    logger.log(Level.WARNING, "No se encontró bean para " + dependencyType.getName());
                }
            }
        }
    }

    public static void start() {
        logger.log(Level.INFO, "Iniciando configuración...");
    }
}
