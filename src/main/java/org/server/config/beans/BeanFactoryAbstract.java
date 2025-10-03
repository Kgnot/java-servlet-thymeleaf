package org.server.config.beans;

import java.lang.annotation.Annotation;

/**
 * Clase abstracta que representa una fábrica de beans (Abstract Factory)
 * responsable de crear e inicializar beans a partir de anotaciones específicas.
 *
 * <p>Esta fábrica utiliza un {@link ComponentScanner} para explorar el proyecto
 * en busca de clases anotadas, y un {@link BeanContainer} para registrar e
 * inyectar las instancias de beans correspondientes.</p>
 *
 * <p>El patrón Abstract Factory se aplica aquí para permitir la creación
 * personalizada de beans según el tipo de anotación objetivo,
 * dejando la implementación concreta a las subclases.</p>
 */
public abstract class BeanFactoryAbstract {

    /** Componente encargado de escanear el proyecto en busca de clases anotadas. */
    protected ComponentScanner componentScanner;

    /** Contenedor de beans en el cual se registran e inyectan los beans encontrados. */
    protected BeanContainer beanContainer;

    /**
     * Constructor de la fábrica de beans.
     *
     * @param componentScanner el escáner de componentes encargado de localizar clases anotadas
     * @param beanContainer    el contenedor de beans donde se registrarán e inyectarán las instancias
     */
    public BeanFactoryAbstract(ComponentScanner componentScanner, BeanContainer beanContainer) {
        this.componentScanner = componentScanner;
        this.beanContainer = beanContainer;
    }

    /**
     * Crea y registra beans en el {@link BeanContainer} para todas las clases
     * que contengan la anotación especificada.
     *
     * @param clazz la anotación que deben tener las clases para ser registradas como beans
     */
    public abstract void createBeansForAnnotation(Class<? extends Annotation> clazz);

    /**
     * Escanea las clases del proyecto en busca de una anotación específica,
     * sin necesariamente crear o registrar los beans de inmediato.
     *
     * @param clazz la anotación que se desea buscar en las clases del proyecto
     */
    public abstract void scanBeanForAnnotation(Class<? extends Annotation> clazz);
}
