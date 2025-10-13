package org.server.config.beans;

import org.server.config.beans.Bean.BeanType;
import org.server.config.shared.*;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.logging.Logger;


/**
 * Contexto principal de la aplicación encargado de gestionar el ciclo de vida de los beans
 * y la inyección de dependencias.
 *
 * <p>Funciona como un contenedor IoC (Inversion of Control), inspirado en frameworks como Spring,
 * permitiendo registrar, inicializar e inyectar dependencias en clases anotadas con anotaciones
 * personalizadas (e.g., {@code @Component}, {@code @Service}, {@code @Controller}, etc.).</p>
 *
 * <p>El {@link ApplicationContext} se implementa como un Singleton, garantizando que exista
 * una única instancia accesible globalmente mediante {@link #getInstance()}.</p>
 */
public class ApplicationContext {
    private static final Logger LOGGER = Logger.getLogger(ApplicationContext.class.getName());

    /**
     * Contenedor de beans que administra instancias y sus dependencias.
     */
    private final BeanContainer container;

    /**
     * Fábrica de beans que se encarga de escanear y crear instancias según anotaciones.
     */
    private final BeanFactoryAbstract beanFactory;

    /**
     * Instancia única de ApplicationContext (Singleton).
     */
    private static ApplicationContext instance;

    /**
     * Constructor privado para forzar el uso del patrón Singleton.
     * Inicializa el contenedor y la fábrica de beans con un {@link ComponentScanner}.
     */
    private ApplicationContext() {
        this.container = new SimpleBeanContainer();
        ComponentScanner componentScanner = new ComponentScanner("org.server");
        this.beanFactory = new BeanFactoryImpl(componentScanner, container);
    }

    /**
     * Devuelve la instancia única de {@link ApplicationContext}.
     * Si no existe, la crea.
     *
     * @return instancia única del contexto
     */
    public static ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    /**
     * Inicializa el contexto de la aplicación:
     * <ul>
     *   <li>Escanea el paquete base en busca de clases anotadas.</li>
     *   <li>Crea instancias de beans anotados con {@code @Configuration}, {@code @Component},
     *       {@code @Service}, {@code @Controller}, {@code @ViewRender}, {@code @Websocket}.</li>
     *   <li>Inyecta dependencias en cada bean detectado.</li>
     * </ul>
     */
    public void initialize() {
        beanFactory.createBeansForAnnotation(Configuration.class);
        beanFactory.createBeansForAnnotation(Component.class);
        beanFactory.createBeansForAnnotation(Service.class);
        beanFactory.createBeansForAnnotation(Controller.class);
        beanFactory.createBeansForAnnotation(ViewRender.class);
        beanFactory.createBeansForAnnotation(Websocket.class);

        LOGGER.info("ApplicationContext inicializado correctamente");

        LOGGER.info(container.getBeansByAnnotation(Configuration.class) + "");

        // Inyectar dependencias en todos los beans detectados
        container.getBeansByAnnotation(Configuration.class).forEach(this::injectDependencies);
        container.getBeansByAnnotation(Component.class).forEach(this::injectDependencies);
        container.getBeansByAnnotation(Service.class).forEach(this::injectDependencies);
        container.getBeansByAnnotation(ViewRender.class).forEach(this::injectDependencies);
        container.getBeansByAnnotation(Controller.class).forEach(this::injectDependencies);
        container.getBeansByAnnotation(Websocket.class).forEach(this::injectDependencies);

        LOGGER.info("Dependencias inyectadas en todos los beans");
    }


    private void injectDependencies(BeanType<?> beanType) {
        Object bean = container.getBean(beanType.getClazz(), beanType.getSpecificBean()); // Obtenemos la instancia
        if (bean != null) {
            container.injectBean(bean);
        }
    }

    public <T> T getBean(Class<T> clazz, String beanName) {
        return container.getBean(clazz, beanName);
    }

    public <T> T getBeanByType(Class<T> clazz) {
        return container.getBeanByType(clazz);
    }

    /**
     * Obtiene todas las clases anotadas con una anotación específica dentro del contexto.
     *
     * @param annotation anotación a buscar
     * @return lista de clases que poseen la anotación indicada
     */
    public List<BeanType<?>> getBeansByAnnotation(Class<? extends Annotation> annotation) {
        return container.getBeansByAnnotation(annotation);
    }
}

