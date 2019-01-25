package dto;

import helpersLocal.LConfig;

public enum L_PROPERTY {
    RESOURCES_PATH("resource_path"),
    TEMP("temp"),
    DRIVER_NAME("driver_name"),
    RUNNER_JAR("external_jar_name");

    private String valueName;

    L_PROPERTY(final String param) {
        valueName = param;
    }

    public String getValueName() {
        return valueName;
    }

    public String getValue() {
        return LConfig.getProperty(this);
    }
}
