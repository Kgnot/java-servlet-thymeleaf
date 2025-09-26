package org.server.config.beans;

import org.server.config.shared.*;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.logging.Logger;


public class ApplicationContext {
    private static final Logger LOGGER = Logger.getLogger(ApplicationContext.class.getName());

    private final BeanContainer container;
    private final BeanFactoryAbstract beanFactory;


    // instance
    private static ApplicationContext instance;

    private ApplicationContext() {
        this.container = new SimpleBeanContainer();
        ComponentScanner componentScanner = new ComponentScanner("org.server");
        this.beanFactory = new BeanFactoryImpl(componentScanner, container);
    }

    public static ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    public void initialize() {
        beanFactory.createBeansForAnnotation(Configuration.class);
        beanFactory.createBeansForAnnotation(Component.class);
        beanFactory.createBeansForAnnotation(Service.class);
        beanFactory.createBeansForAnnotation(Controller.class);
        beanFactory.createBeansForAnnotation(ViewRender.class);
        beanFactory.createBeansForAnnotation(Websocket.class);

        LOGGER.info("ApplicationContext inicializado correctamente");
        // Inyectar dependencias
        // Aquí creo un arbol de dependencias
        container.getBeansByAnnotation(Configuration.class).forEach(this::injectDependencies);
        container.getBeansByAnnotation(Component.class).forEach(this::injectDependencies);
        container.getBeansByAnnotation(Service.class).forEach(this::injectDependencies);
        container.getBeansByAnnotation(ViewRender.class).forEach(this::injectDependencies);
        container.getBeansByAnnotation(Controller.class).forEach(this::injectDependencies);
        container.getBeansByAnnotation(Websocket.class).forEach(this::injectDependencies);

        LOGGER.info("ApplicationContext inicializado correctamente");
    }

    private void injectDependencies(Class<?> beanClass) {
        Object bean = container.getBean(beanClass);
        if (bean != null) {
            container.injectBean(bean);
        }
    }

    public <T> T getBean(Class<T> clazz) {
        return container.getBean(clazz);
    }

    public List<Class<?>> getBeansByAnnotation(Class<? extends Annotation> annotation) {
        return container.getBeansByAnnotation(annotation);
    }

}
