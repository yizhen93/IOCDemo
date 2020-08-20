package lucien.demo.ioc;

import lucien.demo.config.BeanConfig;
import lucien.demo.config.PropertyConfig;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class XMLBeanFactoryImpl extends AbstractBeanFactory {
    private static volatile BeanFactory xMLBeanFactoryImpl = null;

    private XMLBeanFactoryImpl(String configPath) {
        this.loadBeanDefinition(configPath);
    }
    public static BeanFactory getInstance(String configPath) {
        if (xMLBeanFactoryImpl == null) {
            synchronized (XMLBeanFactoryImpl.class) {
                if (xMLBeanFactoryImpl == null) {
                    xMLBeanFactoryImpl = new XMLBeanFactoryImpl(configPath);
                }
            }
        }
        return xMLBeanFactoryImpl;
    }


    public boolean containsBean(String id) {
        return beanMap.containsKey(id);
    }

    @Override
    protected void loadBeanDefinition(String... configPath) {
        try (InputStream inputStream = XMLBeanFactoryImpl.class.getClassLoader()
                .getResourceAsStream(configPath[0])) {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
            NodeList beanConfigList = document.getElementsByTagName("bean");
            int beanConfigListLength = beanConfigList.getLength();
            for (int beanIndex = 0; beanIndex < beanConfigListLength; beanIndex++) {
                Element beanElement = (Element) beanConfigList.item(beanIndex);
                BeanConfig beanConfig = addBeanConfigIntoBeanConfigMap(beanElement);

                NodeList propertyNodeList = beanElement.getElementsByTagName("property");
                if (propertyNodeList == null) {
                    continue;
                }
                int propertyNodeListLength = propertyNodeList.getLength();
                for (int propertyIndex = 0; propertyIndex < propertyNodeListLength; propertyIndex++) {
                    Element propertyElement = (Element) propertyNodeList.item(propertyIndex);
                    addPropertyConfigIntoPropertyConfigMap(beanConfig, propertyElement);
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setProperty(Class<?> classInfo, Object currentBean, PropertyConfig propertyConfig) {
        String methodName = getPropertySetMethodName(propertyConfig.getName());
        Method currentMethod = getMethodByName(classInfo, methodName);
        String dependency = propertyConfig.getDependency();
        if (currentMethod != null && Objects.nonNull(dependency) && !dependency.isEmpty()) {
            try {
                currentMethod.invoke(currentBean, this.getBean(dependency));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    private Method getMethodByName(Class<?> classInfo, String methodName) {
        Method[] methodArr = classInfo.getMethods();
        for (Method method : methodArr) {
            if (methodName.equals(method.getName())) {
                return method;
            }
        }
        return null;
    }

    private String getPropertySetMethodName(String propertyName) {
        return "set" + propertyName.substring(0, 1).toUpperCase() +
                propertyName.substring(1);
    }

    private BeanConfig addBeanConfigIntoBeanConfigMap(Element element) {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setId(element.getAttribute("id"));
        beanConfig.setClassName(element.getAttribute("class"));
        beanConfig.setScope(element.getAttribute("scope"));
        beanConfigMap.put(beanConfig.getId(), beanConfig);
        return beanConfig;
    }

    private void addPropertyConfigIntoPropertyConfigMap(BeanConfig beanConfig, Element element) {
        PropertyConfig propertyConfig = new PropertyConfig();
        propertyConfig.setDependency(element.getAttribute("dependency"));
        propertyConfig.setName(element.getAttribute("name"));
        beanConfig.getPropertyConfigList().add(propertyConfig);
    }

}
