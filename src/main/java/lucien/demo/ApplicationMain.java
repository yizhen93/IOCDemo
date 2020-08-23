package lucien.demo;

import lucien.demo.controller.UserController;
import lucien.demo.ioc.BeanFactory;
import lucien.demo.ioc.XMLBeanFactoryImpl;

public class ApplicationMain {
    public static void main(String[] args) {
        BeanFactory beanfactory = XMLBeanFactoryImpl.getInstance("BeanConfig.xml");
        UserController userController = (UserController) beanfactory.getBean("userController");
        System.out.println(userController.getUserById(10L).toString());
    }
}
