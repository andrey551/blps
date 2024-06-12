/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tad.blps.utils;

import org.apache.commons.codec.binary.Base64;
import tad.blps.DTO.AccountDTO;

/**
 *
 * @author Never
 */
public class BasicAuthUtil {
    public static String encode(String username, 
                                String password) {
        String accountInfo = username + ":" + password;
        Base64 base64 = new Base64();
        return new String(base64.encode(accountInfo.getBytes()));
    }
    
    public static AccountDTO decode(String token) {
        System.out.println("token:" + token);
        String decodedString = 
                new String(Base64.decodeBase64(token.getBytes()));
        int colon = decodedString.indexOf((int)':');
        String username = decodedString.substring(0, colon);
        String password = decodedString.substring(colon + 1);
        return new AccountDTO(username, password);
    }
}
