package helpersLocal;

import dto.L_PROPERTY;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;

public class LConfig {
    private static Properties properties = new Properties();
    private static LogHandler LOGGER = new LogHandler(Properties.class.getName());

    public static final String RESOURCES_PATH = "/var/task/";
    public static final String DEFAULT_RUN_PROPERTIES_PATH = RESOURCES_PATH + "config.properties";


    public static void readProperties() {
        try {
            properties.load(new FileInputStream(new File(DEFAULT_RUN_PROPERTIES_PATH).getAbsolutePath()));
        } catch (IOException e) {
            throw new RuntimeException("Unable to read Lambda properties", e);
        }
        Arrays.stream(L_PROPERTY.values())
                .forEach(key -> LOGGER.debug("Read Lambda property " +
                                key.name() + ":'" + key.getValueName() + "' -> '" + getProperty(key) + "'"));
    }

    public static String getProperty(final L_PROPERTY propName) {
        return Optional.ofNullable(properties.getProperty(propName.getValueName())).orElse(StringUtils.EMPTY);
    }

    public static void setProperty(final L_PROPERTY propName, final String value) {
        properties.setProperty(propName.getValueName(), value);
    }
}