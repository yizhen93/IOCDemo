package lucien.demo.ioc;

import lucien.demo.config.BeanConfig;
import lucien.demo.config.PropertyConfig;
import lucien.demo.lifecycle.Aware;
import lucien.demo.lifecycle.BeanFactoryAware;
import lucien.demo.lifecycle.BeanNameAware;
import lucien.demo.lifecycle.BeanPostProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractBeanFactory implements BeanFactory {
    protected static Map<String, BeanConfig> beanConfigMap = new HashMap<>();
    protected static Map<String, Object> beanMap = new ConcurrentHashMap<>();

    protected abstract void loadBeanDefinition(String... path);

    public void refresh() {
        beanConfigMap.keySet().forEach(this::getBean);
    }

    @Override
    public Object getBean(String id) {
        if (beanConfigMap.containsKey(id)) {
            BeanConfig beanConfig = beanConfigMap.get(id);
            String scope = beanConfig.getScope();
            if (Objects.isNull(scope) || scope.isEmpty()) {
                scope = DEFAULT_SCOPE;
            } else if (DEFAULT_SCOPE.equalsIgnoreCase(scope) && containsBean(id)) {
                return beanMap.get(id);
            }

            try {
                Class<?> classInfo = Class.forName(beanConfig.getClassName());
                Object currentBean = classInfo.newInstance();
                if (DEFAULT_SCOPE.equalsIgnoreCase(scope)) {
                    beanMap.put(id, currentBean);
                }

                for (PropertyConfig propertyConfig : beanConfig.getPropertyConfigList()) {
                    setProperty(classInfo, currentBean, propertyConfig);
                }

                handleAwareCallback(currentBean, id);
                handleBeanPostProcessorBefore(currentBean, id);

                return currentBean;
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public boolean isSingleton(String id) {
        return DEFAULT_SCOPE.equalsIgnoreCase(beanConfigMap.getOrDefault(id, new BeanConfig()).getScope());
    }

    private void handleAwareCallback(Object bean, String name) {
        if (bean instanceof Aware) {
            if (bean instanceof BeanNameAware) {
                ((BeanNameAware) bean).setBeanName(name);
            }
            if (bean instanceof BeanFactoryAware) {
                ((BeanFactoryAware) bean).setBeanFactory(this);
            }
        }
    }

    protected abstract void setProperty(Class<?> classInfo, Object currentBean, PropertyConfig propertyConfig);


    private void handleBeanPostProcessorBefore(Object currentBean, String beanId){
        if (currentBean instanceof BeanPostProcessor) {
            ((BeanPostProcessor) currentBean).postProcessBeforeInitialization(currentBean, beanId);
        }
    }

}
