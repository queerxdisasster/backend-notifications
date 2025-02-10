package com.queerxdisasster.backendnotifications.config;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.BinaryConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class IgniteConfig {
    @Value("${IGNITE_ADDRESS:127.0.0.1}")
    private String igniteAddr;

    @Value("${IGNITE_PORT:47500..47509}")
    private String ignitePort;

    @Bean
    public Ignite igniteInstance() {
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setClientMode(true);

        // Добавим атрибут
        Map<String, String> attrs = new HashMap<>();
        attrs.put("ROLE", "b1");
        cfg.setUserAttributes(attrs);

        // Discovery
        TcpDiscoverySpi spi = new TcpDiscoverySpi();
        TcpDiscoveryVmIpFinder finder = new TcpDiscoveryVmIpFinder();
        // Список адресов Ignite-сервиса
        finder.setAddresses(Arrays.asList(igniteAddr + ":" + ignitePort));
        spi.setIpFinder(finder);
        cfg.setDiscoverySpi(spi);

        return Ignition.start(cfg);
    }
}
