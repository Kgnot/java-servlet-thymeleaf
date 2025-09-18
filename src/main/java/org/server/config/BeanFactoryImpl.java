package org.server.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Set;

public class BeanFactoryImpl extends BeanFactoryAbstract {


    public BeanFactoryImpl(ComponentScanner componentScanner, BeanContainer beanContainer) {
        super(componentScanner, beanContainer);
    }

    @Override
    public void createBeansForAnnotation(Class<? extends Annotation> annotationClas) {
        Set<Class<?>> classes = componentScanner.findClassWithAnnotation(annotationClas);

        for (Class<?> clazz : classes) {
            try {
                Object instance = createInstance(clazz);
                safeRegister(clazz, instance);
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }
        }
    }

    private Object createInstance(Class<?> clazz) throws Exception {
        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors.length == 0) {
            throw new RuntimeException("No hay constructores disponibles");
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
