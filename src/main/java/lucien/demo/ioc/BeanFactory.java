package lucien.demo.ioc;

public interface BeanFactory {
    String DEFAULT_SCOPE = "singleton";

    Object getBean(String id);
    boolean containsBean(String id);
    boolean isSingleton(String id);

}
