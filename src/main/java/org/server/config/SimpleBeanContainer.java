package org.server.config;

import org.server.config.shared.Inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SimpleBeanContainer implements BeanContainer {
    private final static Logger LOGGER = Logger.getLogger(SimpleBeanContainer.class.getName());

    private final Map<Class<?>, Object> beans = new ConcurrentHashMap<>(); // porque concurrent?


    @Override
    public <T> T getBean(Class<T> beanClass) {
        return (T) beanClass.cast(beans.get(beanClass));
    }

    @Override
    public <T> void registerBean(Class<T> beanClass, T instanceBean) {
        beans.put(beanClass, instanceBean);
    }

    @Override
    public List<Class<?>> getBeansByAnnotation(Class<? extends Annotation> annotation) {
        return beans.keySet()
                .stream()
                .filter(beanClass -> beanClass.isAnnotationPresent(annotation))
                .collect(Collectors.toList());
    }

    @Override
    public void injectBean(Object bean) {
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                injectField(bean, field);
            }
        }
    }


    private void injectField(Object bean, Field field) {
        Object dependency = beans.get(field.getType());
        if (dependency != null) {
            field.setAccessible(true);// lo hacemos accesible para modificar
            try {
                field.set(bean, dependency);
            } catch (IllegalAccessException e) {
//                throw new RuntimeException("Bean no encontrado: ", e);
                LOGGER.log(Level.WARNING, "No se pudo inyectar la dependencia " + dependency, e);
            }
        } else {
            LOGGER.warning("No se encontró bean");
        }
    }
}
