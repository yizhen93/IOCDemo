package lucien.demo.dao;

import lucien.demo.ioc.BeanFactory;
import lucien.demo.lifecycle.BeanFactoryAware;
import lucien.demo.lifecycle.BeanNameAware;
import lucien.demo.lifecycle.BeanPostProcessor;
import lucien.demo.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserDao implements BeanPostProcessor, BeanNameAware, BeanFactoryAware {
    private List<UserModel> mockUserDataList = new ArrayList<>();
    private String beanName;
    private BeanFactory beanFactory;

    public UserModel getUserById(Long id) {
        System.out.println(beanName);
        System.out.println(beanFactory.getBean("userService"));
        return mockUserDataList.stream().filter(x -> id.equals(x.getId())).findAny().orElse(null);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        for (int i = 1; i < 11; i++) {
            mockUserDataList.add(new UserModel().setAge(i + 20).setName("张" + i).setId((long) i));
        }
        return bean;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
