package org.server.config.beans;

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

    /**
     * Constructor de la fábrica de beans.
     *
     * @param componentScanner el escáner encargado de localizar clases anotadas
     * @param beanContainer    el contenedor donde se registrarán los beans creados
     */
    public BeanFactoryImpl(ComponentScanner componentScanner, BeanContainer beanContainer) {
        super(componentScanner, beanContainer);
    }

    /**
     * Crea instancias de todas las clases anotadas con {@code annotationClass},
     * resolviendo sus dependencias a través de los constructores disponibles,
     * y las registra en el {@link BeanContainer}.
     *
     * @param annotationClass la anotación objetivo usada para identificar los beans
     */
    @Override
    public void createBeansForAnnotation(Class<? extends Annotation> annotationClass) {
        Set<Class<?>> classes = componentScanner.findClassWithAnnotation(annotationClass);

        for (Class<?> clazz : classes) {
            try {
                Object instance = createInstance(clazz);
                safeRegister(clazz, instance);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Exception en createBeansForAnnotation: " + e.getMessage());
            }
        }
    }

    /**
     * Escanea las clases anotadas con {@code annotationClass} y las
     * registra en el {@link BeanContainer} sin instanciarlas.
     *
     * <p>Este método sirve para preparar las clases que podrán ser
     * instanciadas más adelante.</p>
     *
     * @param annotationClass la anotación usada para identificar las clases candidatas
     */
    @Override
    public void scanBeanForAnnotation(Class<? extends Annotation> annotationClass) {
        Set<Class<?>> classes = componentScanner.findClassWithAnnotation(annotationClass);

        for (Class<?> clazz : classes) {
            beanContainer.registerBeanClass(clazz);
        }
    }

    /**
     * Crea una nueva instancia de la clase dada utilizando el primer constructor disponible.
     * Si el constructor requiere parámetros, los obtiene del {@link BeanContainer}.
     *
     * @param clazz la clase del bean a instanciar
     * @return la instancia creada
     * @throws Exception si no hay constructores disponibles o falla la creación
     */
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

    /**
     * Registra de manera segura un bean en el {@link BeanContainer},
     * realizando un cast al tipo correcto.
     *
     * @param clazz    la clase del bean
     * @param instance la instancia del bean
     * @param <T>      el tipo del bean
     */
    private <T> void safeRegister(Class<T> clazz, Object instance) {
        beanContainer.registerBean(clazz, clazz.cast(instance));
    }
}
