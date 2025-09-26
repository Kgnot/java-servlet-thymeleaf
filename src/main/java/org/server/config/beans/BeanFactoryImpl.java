package org.server.config.beans;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BeanFactoryImpl extends BeanFactoryAbstract {
	private static final Logger logger = Logger.getLogger(BeanFactoryImpl.class.getName());

    public BeanFactoryImpl(ComponentScanner componentScanner, BeanContainer beanContainer) {
        super(componentScanner, beanContainer);
    }

    @Override
    public void createBeansForAnnotation(Class<? extends Annotation> annotationClass) {
        Set<Class<?>> classes = componentScanner.findClassWithAnnotation(annotationClass);

        for (Class<?> clazz : classes) {
            try {
                Object instance = createInstance(clazz);
                safeRegister(clazz, instance);
            } catch (Exception e) {
                logger.log(Level.SEVERE,"Exception en createBeansForAnnotation: " + e.getMessage());
                
             
            }
        }
    }

    @Override
    public void scanBeanForAnnotation(Class<? extends Annotation> annotationClass) {
        Set<Class<?>> classes = componentScanner.findClassWithAnnotation(annotationClass);

        // Las registramos:
        for (Class<?> clazz : classes) {
            beanContainer.registerBeanClass(clazz);
        }
    }

    // private methods:
    private Object createInstance(Class<?> clazz) throws Exception {
        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors.length == 0) {
            throw new RuntimeException("No hay constructores creados disponibles");
        }

        Constructor<?> constructor = constructors[0];
        Object[] params = Arrays.stream(constructor.getParameterTypes())
                .map(beanContainer::getBean)
                .toArray();

        return constructor.newInstance(params);
    }

    private <T> void safeRegister(Class<T> clazz, Object instance) {
        beanContainer.registerBean(clazz, clazz.cast(instance));
    }
}
