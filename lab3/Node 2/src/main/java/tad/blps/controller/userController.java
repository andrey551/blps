/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tad.blps.controller;

import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tad.blps.DTO.TokenDTO;
import tad.blps.entity.User;
import tad.blps.services.UserService;

/**
 *
 * @author Never
 */
@Controller
@RequestMapping("/user")
public class userController {
    private final UserService userService;
    
    @Autowired
    public userController(UserService userService) {
        this.userService = userService;
    }
    
    @DeleteMapping("/del")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity deleteUser(
                            @RequestParam String username,
                            @RequestHeader("Authorization") TokenDTO token) {
        User userToDel = userService.getByUsername(username);
        userService.delete(userToDel);
        return new ResponseEntity(HttpStatusCode.valueOf(200));
    }
    
}
