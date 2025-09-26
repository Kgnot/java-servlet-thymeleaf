package org.server.config.beans;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

public class ComponentScanner {
    private final Reflections reflections;

    public ComponentScanner(String basePackage) {
        reflections = new Reflections(basePackage);
    }

    public Set<Class<?>> findClassWithAnnotation(Class<? extends Annotation> annotation) {
        return reflections.getTypesAnnotatedWith(annotation);
    }

}
