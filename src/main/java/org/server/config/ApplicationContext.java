package org.server.config;

import org.server.config.shared.*;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.logging.Logger;


// Aquí se encapsula la lógica de los Beans manuales que implemento yo para manejar
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
        // Crear beans en orden
        beanFactory.createBeansForAnnotation(Component.class);
        beanFactory.createBeansForAnnotation(Service.class);
        beanFactory.createBeansForAnnotation(Controller.class);
        beanFactory.createBeansForAnnotation(ViewRender.class);
        beanFactory.createBeansForAnnotation(Websocket.class);

        // Inyectar dependencias
        container.getBeansByAnnotation(Component.class).forEach(this::injectDependencies);
        container.getBeansByAnnotation(Service.class).forEach(this::injectDependencies);
        container.getBeansByAnnotation(Controller.class).forEach(this::injectDependencies);
        container.getBeansByAnnotation(ViewRender.class).forEach(this::injectDependencies);
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
