/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tad.blps.config;

import bitronix.tm.BitronixTransactionManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.transaction.SystemException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 *
 * @author tad
 */
@Configuration
@EnableTransactionManagement
public class TransactionConfig {

    @Bean(name="bitronixTransaction")
    public BitronixTransactionManager transactionManager() {
        try {
            BitronixTransactionManager transactionManager = new BitronixTransactionManager();
            transactionManager.setTransactionTimeout(60);
            return transactionManager;
        } catch (SystemException ex) {
            Logger.getLogger(TransactionConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }


}