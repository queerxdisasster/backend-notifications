package com.queerxdisasster.backendnotifications.config;

import org.jobrunr.jobs.mappers.JobMapper;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.storage.sql.common.SqlStorageProviderFactory;
import org.jobrunr.utils.mapper.jackson.JacksonJsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class JobRunrConfiguration {

    @Bean
    public JacksonJsonMapper jacksonJsonMapper() {
        return new JacksonJsonMapper();
    }

    @Bean
    public JobMapper jobMapper(JacksonJsonMapper jsonMapper) {
        return new JobMapper(jsonMapper);
    }

    @Bean
    public StorageProvider storageProvider(DataSource dataSource, JobMapper jobMapper) {
        StorageProvider storageProvider = SqlStorageProviderFactory.using(dataSource);
        storageProvider.setJobMapper(jobMapper);
        return storageProvider;
    }
}