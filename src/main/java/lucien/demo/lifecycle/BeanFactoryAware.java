package lucien.demo.lifecycle;

import lucien.demo.ioc.BeanFactory;

public interface BeanFactoryAware extends Aware {
    void setBeanFactory(BeanFactory var1);
}
