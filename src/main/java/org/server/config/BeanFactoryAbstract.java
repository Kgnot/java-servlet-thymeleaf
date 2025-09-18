package org.server.config;

import java.lang.annotation.Annotation;

// Este es un abstract factory solo porque se me ocurrió xd,
// contempla un ComponenteScanner encargado de escanear el proyecto
// Y un BeanContainer, para que a la hora de crear un Bean lo añada
public abstract class BeanFactoryAbstract {
    protected ComponentScanner componentScanner;
    protected BeanContainer beanContainer;

    public BeanFactoryAbstract(ComponentScanner componentScanner, BeanContainer beanContainer) {
        this.componentScanner = componentScanner;
        this.beanContainer = beanContainer;
    }

    public abstract void createBeansForAnnotation(Class<? extends Annotation> clazz);
}
