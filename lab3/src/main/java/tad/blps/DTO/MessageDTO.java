/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tad.blps.DTO;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import tad.blps.entity.User;

/**
 *
 * @author Never
 */
@Getter
@Setter
public class MessageDTO implements Serializable{
    private User from;
    private User to;
    private String body;
    
    public MessageDTO (User from, User to, String body) {
        this.from = from;
        this.to = to;
        this.body = body;
    }
}
