package org.server.config.beans;

import org.server.config.beans.Bean.BeanType;
import org.server.config.shared.Configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementación concreta de {@link BeanFactoryAbstract} que gestiona la
 * creación e inyección de beans en el contenedor a partir de anotaciones específicas.
 *
 * <p>Utiliza el {@link ComponentScanner} para identificar clases con una anotación
 * determinada y las registra en el {@link BeanContainer}, creando instancias
 * con inyección de dependencias a través de sus constructores.</p>
 */
public class BeanFactoryImpl extends BeanFactoryAbstract {
    private static final Logger logger = Logger.getLogger(BeanFactoryImpl.class.getName());


    public BeanFactoryImpl(ComponentScanner componentScanner, BeanContainer beanContainer) {
        super(componentScanner, beanContainer);
    }

    @Override
    public void createBeansForAnnotation(Class<? extends Annotation> annotationClass) {
        Set<Class<?>> classes = componentScanner.findClassWithAnnotation(annotationClass);
        if (annotationClass.isAssignableFrom(Configuration.class)) {
            logger.log(Level.ALL, "Se econtró Configuration: {}", annotationClass);
        }
        for (Class<?> clazz : classes) {
            try {
                // Obtener el nombre del bean desde la anotación (si tiene valor)
                Annotation annotation = clazz.getAnnotation(annotationClass);
                String beanName = extractAnnotationValue(annotation, clazz.getSimpleName());

                Object instance = createInstance(clazz, beanName);
                // Registrar en el contenedor con BeanType
                safeRegister(clazz, instance, beanName);

            } catch (Exception e) {
                logger.log(Level.SEVERE, "Exception en createBeansForAnnotation: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void scanBeanForAnnotation(Class<? extends Annotation> annotationClass) {
        Set<Class<?>> classes = componentScanner.findClassWithAnnotation(annotationClass);

        for (Class<?> clazz : classes) {
            beanContainer.registerBeanClass(clazz);
        }
    }


    private Object createInstance(Class<?> clazz, String beanName) throws Exception {
        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors.length == 0) {
            throw new RuntimeException("No hay constructores creados disponibles");
        }

        Constructor<?> constructor = constructors[0];


        Object[] params = Arrays.stream(constructor.getParameterTypes())
                .map(paramType -> beanContainer.getBean(paramType, beanName))
                .toArray();

        return constructor.newInstance(params);
    }


    private <T> void safeRegister(Class<T> clazz, Object instance, String beanName) {
        var bean = beanContainer.getBean(clazz, beanName);

        if (bean != null)
            return;

        beanContainer.registerBean(new BeanType<>(clazz, beanName), clazz.cast(instance));
    }

    private String extractAnnotationValue(Annotation annotation, String defaultName) {
        try {
            Object value = annotation.annotationType().getMethod("value").invoke(annotation);
            if (value != null && !value.toString().isBlank()) {
                return value.toString();
            }
        } catch (Exception ignored) {
        }
        return defaultName;
    }
}
