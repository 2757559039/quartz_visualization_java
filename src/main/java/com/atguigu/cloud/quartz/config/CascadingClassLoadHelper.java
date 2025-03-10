package com.atguigu.cloud.quartz.config;

import groovy.lang.GroovyClassLoader;
import jakarta.annotation.PostConstruct;
import org.quartz.simpl.*;
import org.quartz.spi.ClassLoadHelper;

import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;



public class CascadingClassLoadHelper implements ClassLoadHelper{

    private LinkedList<ClassLoadHelper> loadHelpers;
    private ClassLoadHelper bestCandidate;


    private  GroovyClassLoader groovyClassLoader;



    // 新增：自定义的 GroovyClassLoadHelperWrapper
    private static class GroovyClassLoadHelperWrapper implements ClassLoadHelper {
        private final GroovyClassLoader groovyClassLoader;
        public GroovyClassLoadHelperWrapper(GroovyClassLoader loader) {
            System.out.println("loader: " + loader);
            this.groovyClassLoader = loader;
        }

        @Override
        public void initialize() {
        }

        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            return groovyClassLoader.loadClass(name);
        }

        @Override
        public <T> Class<? extends T> loadClass(String name, Class<T> clazz) throws ClassNotFoundException {
            return groovyClassLoader.loadClass(name).asSubclass(clazz);
        }

        @Override
        public URL getResource(String name) {
            return groovyClassLoader.getResource(name);
        }

        @Override
        public InputStream getResourceAsStream(String name) {
            return groovyClassLoader.getResourceAsStream(name);
        }

        @Override
        public ClassLoader getClassLoader() {
            return groovyClassLoader;
        }
    }
//    @Bean
//    public GroovyClassLoader groovyClassLoader() {
//        return new GroovyClassLoader(ClassLoader.getSystemClassLoader());
//    }

    @Override
    @PostConstruct
    public void initialize() {

        loadHelpers = new LinkedList<>();
        System.out.println("groovyClassLoader: " + groovyClassLoader);
        this.groovyClassLoader = SpringContextHolder.getBean(GroovyClassLoader.class);
        // 关键修改：将 GroovyClassLoader 的包装类插入到最前面
        loadHelpers.add(new GroovyClassLoadHelperWrapper(groovyClassLoader));
        loadHelpers.add(new LoadingLoaderClassLoadHelper());
        loadHelpers.add(new SimpleClassLoadHelper());
        loadHelpers.add(new ThreadContextClassLoadHelper());
        loadHelpers.add(new InitThreadContextClassLoadHelper());

        for (ClassLoadHelper loadHelper : loadHelpers) {
            loadHelper.initialize();
        }
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (bestCandidate != null) {
            try {
                return bestCandidate.loadClass(name);
            } catch (Throwable t) {
                bestCandidate = null;
            }
        }

        Throwable throwable = null;
        Class<?> clazz = null;
        ClassLoadHelper loadHelper = null;

        Iterator<ClassLoadHelper> iter = loadHelpers.iterator();

        while (iter.hasNext()) {
            loadHelper = iter.next();
            try {
//                clazz = loadHelper.loadClass(name);
                clazz = loadHelper.loadClass(name);
                break;
            } catch (Throwable t) {
                throwable = t;
            }
        }

        if (clazz == null) {
            if (throwable instanceof ClassNotFoundException) {
                throw (ClassNotFoundException) throwable;
            } else {
                throw new ClassNotFoundException(
                        String.format("Unable to load class %s by any known loaders.", name), throwable);
            }
        }

        bestCandidate = loadHelper;
        return clazz;
    }

    @Override
    public <T> Class<? extends T> loadClass(String name, Class<T> clazz) throws ClassNotFoundException {
        return (Class<? extends T>) loadClass(name);
    }

    @Override
    public URL getResource(String name) {
        if (bestCandidate != null) {
            URL result = bestCandidate.getResource(name);
            if (result != null) {
                return result;
            } else {
                bestCandidate = null;
            }
        }

        ClassLoadHelper loadHelper = null;
        URL result = null;

        Iterator<ClassLoadHelper> iter = loadHelpers.iterator();
        while (iter.hasNext()) {
            loadHelper = iter.next();
            result = loadHelper.getResource(name);
            if (result != null) {
                break;
            }
        }

        bestCandidate = loadHelper;
        return result;
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        if (bestCandidate != null) {
            InputStream result = bestCandidate.getResourceAsStream(name);
            if (result != null) {
                return result;
            } else {
                bestCandidate = null;
            }
        }

        ClassLoadHelper loadHelper = null;
        InputStream result = null;

        Iterator<ClassLoadHelper> iter = loadHelpers.iterator();
        while (iter.hasNext()) {
            loadHelper = iter.next();
            result = loadHelper.getResourceAsStream(name);
            if (result != null) {
                break;
            }
        }

        bestCandidate = loadHelper;
        return result;
    }

    @Override
    public ClassLoader getClassLoader() {
        return (this.bestCandidate != null) ?
                this.bestCandidate.getClassLoader() :
                groovyClassLoader; // 直接返回 GroovyClassLoader
    }
}