package com.betterjr.common.config;

import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
/**
 *
 * spring配置文件自定义加载器
 *
 */
public class BetterPropertyPlaceholderConfigurer extends PropertySourcesPlaceholderConfigurer {
    private static final String BETTER_CONFIG = "better.config";

    public void setBetterLocations(final Set<String> betterLocations) {
        final String betterConfig = System.getProperty(BETTER_CONFIG);
        final Properties properties = new Properties();
        if (betterConfig == null) {
            for (final String betterLocation : betterLocations) {
                try {
                    final Resource resource = new ClassPathResource(betterLocation);
                    logger.info("Default Loading properites file from " + resource.getFilename());
                    try (InputStream inputStream = resource.getInputStream()) {
                        final Properties prop = new Properties();
                        prop.load(inputStream);
                        if (prop != null) {
                            properties.putAll(prop);
                        }
                    }
                }
                catch (final Exception e) {
                    logger.fatal("配置文件加载错误!");
                }
            }
        }
        else {
            for (final String betterLocation : betterLocations) {
                try {
                    final Resource resource = new FileSystemResource(betterConfig + betterLocation);
                    logger.info("Config Loading properites file from " + resource.getFilename());
                    try (InputStream inputStream = resource.getInputStream()) {
                        final Properties prop = new Properties();
                        prop.load(inputStream);
                        if (prop != null) {
                            properties.putAll(prop);
                        }
                    }
                }
                catch (final Exception e) {
                    logger.fatal("配置文件加载错误!");
                }
            }
        }
        this.setProperties(properties); // 关键方法,调用的PropertyPlaceholderConfigurer中的方法,
    }

}