/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tad.blps.WebSocketTransmition;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.AbstractXMPPConnection;
//import org.jivesoftware.smack.Chat;
//import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.stringprep.XmppStringprepException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Never
 */
@Component
public class XmppSender {

        public void sendMessage(String to, String message) {
        try {
            XMPPTCPConnectionConfiguration config = config();
//            XMPPTCPConnectionConfiguration config = config();
            AbstractXMPPConnection connection = connect(config);
            
            ChatManager chatManager = ChatManager.getInstanceFor(connection);
            
//            Chat chat = chatManager.chatWith(to + "@" + connection.getXMPPServiceDomain());
//            chat.send(message);
            
            connection.disconnect();
            
        } catch (SmackException | 
                XMPPException | 
                InterruptedException | 
                IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private XMPPTCPConnectionConfiguration config() 
                                        throws XmppStringprepException {
            return XMPPTCPConnectionConfiguration.builder()
                        .setUsernameAndPassword("admin", "05052001")
                        .setHost("tad")
                        .setPort(61222)
                        .build();
    }
    
    private AbstractXMPPConnection connect 
                                    (XMPPTCPConnectionConfiguration config) 
                                            throws  SmackException, 
                                                    IOException, 
                                                    XMPPException, 
                                                    InterruptedException {
        AbstractXMPPConnection connection = new XMPPTCPConnection(config);
        connection.connect();
        connection.login();
        
        return connection;
    }
                                    


}
