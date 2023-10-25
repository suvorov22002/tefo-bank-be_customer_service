package com.tefo.customerservice.core.config;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.ext.mongodb.database.MongoLiquibaseDatabase;
import liquibase.integration.spring.SpringResourceAccessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.File;

@Configuration
public class LiquibaseConfig implements InitializingBean {
    @Value("${spring.liquibase.url}")
    private String uri;

    @Value("${spring.liquibase.change-log}")
    private Resource[] resources;

    @Override
    public void afterPropertiesSet() throws Exception {
        try (MongoLiquibaseDatabase openDatabase = (MongoLiquibaseDatabase) DatabaseFactory.getInstance()
                .openDatabase(uri, null, null, null, null, null)) {

            for (Resource resource : resources) {
                String filename = resource.getFilename();
                String parentFolder = resource.getFile().getParentFile().getName();
                try (Liquibase liquibase = new Liquibase(String.format("%s%s%s", parentFolder, File.separator, filename)
                        , new SpringResourceAccessor(new DefaultResourceLoader()), openDatabase)) {
                    liquibase.update(new Contexts());
                }
            }
        }
    }
}
