package lucien.demo.config;

import java.util.ArrayList;
import java.util.List;

public class BeanConfig {
    private String id;
    private String className;
    private String scope;
    private List<PropertyConfig> propertyConfigList = new ArrayList<>();

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public String getScope() {
        return scope;
    }
    public void setScope(String scope) {
        this.scope = scope;
    }
    public List<PropertyConfig> getPropertyConfigList() {
        return propertyConfigList;
    }
    public void setPropertyConfigList(List<PropertyConfig> propertyConfigList) {
        this.propertyConfigList = propertyConfigList;
    }
}
