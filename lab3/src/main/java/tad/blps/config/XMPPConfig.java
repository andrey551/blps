/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tad.blps.config;

import java.io.IOException;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Never
 */
@Configuration
public class XMPPConfig {
    @Bean
    public XMPPConnection xmppConnection() throws SmackException, XMPPException, IOException, InterruptedException {
        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
            .setUsernameAndPassword("admin", "admin")
//            .setHost("0.0")
            .setXmppDomain("localhost")
            .setPort(61222)
            .setConnectTimeout(20000)
            .build();
        System.out.println("not config yet");
        XMPPTCPConnection connection = new XMPPTCPConnection(config);
        System.out.println("not connect yet");
        connection.connect();
        System.out.println("not login yet");
        connection.login();

        return connection;
    }
}
