package org.server.config.beans;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * Utilidad encargada de escanear un paquete base para encontrar clases con determinadas anotaciones.
 *
 * <p>Esta clase utiliza la librería {@link Reflections} para realizar el escaneo de clases
 * dentro de un paquete específico.</p>
 *
 * <p>Ejemplo de uso:</p>
 * <pre>{@code
 * ComponentScanner scanner = new ComponentScanner("org.server");
 * Set<Class<?>> servicios = scanner.findClassWithAnnotation(Service.class);
 * }</pre>
 */
public class ComponentScanner {
    private final Reflections reflections;

    /**
     * Crea un escáner de componentes a partir de un paquete base.
     *
     * @param basePackage el paquete raíz desde donde se realizará el escaneo
     */
    public ComponentScanner(String basePackage) {
        reflections = new Reflections(basePackage);
    }

    /**
     * Encuentra todas las clases dentro del paquete base que estén anotadas
     * con la anotación especificada.
     *
     * @param annotation la anotación a buscar en las clases
     * @return un conjunto de clases que contienen la anotación dada
     */
    public Set<Class<?>> findClassWithAnnotation(Class<? extends Annotation> annotation) {
        return reflections.getTypesAnnotatedWith(annotation);
    }
}