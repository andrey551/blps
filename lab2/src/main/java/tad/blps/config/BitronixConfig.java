/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tad.blps.config;

import bitronix.tm.BitronixTransactionManager;
import bitronix.tm.TransactionManagerServices;
import bitronix.tm.resource.jdbc.PoolingDataSource;
import jakarta.transaction.TransactionManager;
import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;
import tad.blps.entity.File;
import tad.blps.entity.Payment;
import tad.blps.entity.User;

/**
 *
 * @author tad
 */
@Configuration
@EnableTransactionManagement
public class BitronixConfig {
    @Bean(name = "bitronixTransactionManager")
    @DependsOn
    public BitronixTransactionManager 
                bitronixTransactionManager() throws Throwable {
                    BitronixTransactionManager bitronixTransactionManager =
                    TransactionManagerServices.getTransactionManager();
                    bitronixTransactionManager.setTransactionTimeout(10000);
                    return bitronixTransactionManager;
    }
                
    @Bean(name = "transactionManager")
    @DependsOn({"bitronixTransactionManager"})
    public PlatformTransactionManager
        transactionManager(TransactionManager
                        bitronixTransactionManager) throws Throwable {
        return
        new JtaTransactionManager(bitronixTransactionManager);
    }
    
    @Bean(name = "primaryMySqlDataSource")
    @Primary
    public DataSource primaryMySqlDataSource() {
        PoolingDataSource bitronixDataSourceBean =
        new PoolingDataSource();
        bitronixDataSourceBean.setMaxPoolSize(5);
        bitronixDataSourceBean
        .setUniqueName("primaryMySqlDataSourceResource");
            bitronixDataSourceBean.setClassName(
                "org.postgresql.xa.PGXADataSource");
            
        Properties properties = new Properties();
        properties.put("user", "postgres");
        properties.put("password", "05052001");
        properties.put("url",
        "jdbc:postgresql://localhost:5432/postgres");
        bitronixDataSourceBean.setDriverProperties(properties);
        return bitronixDataSourceBean;
    }
    
    @Bean(name = "primaryMySqlEntityManagerFactory")
    @Primary
    @DependsOn({"transactionManager", "primaryMySqlDataSource"})
    public LocalContainerEntityManagerFactoryBean
    primaryMySqlEntityManagerFactory(
        EntityManagerFactoryBuilder builder,
        DataSource primaryMySqlDataSource) {
        return builder
        .dataSource(primaryMySqlDataSource)
        .persistenceUnit("primaryUnit")
        .packages(User.class, File.class, Payment.class)
        .build();
    }
}
