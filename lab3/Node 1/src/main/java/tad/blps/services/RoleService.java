/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tad.blps.services;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tad.blps.entity.Role;
import tad.blps.repositories.RoleRepository;

/**
 *
 * @author Never
 */
@Service
public class RoleService {
    private RoleRepository roleRepository; 
    
    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    public Role findByName(String role) {
        return roleRepository.findByName(role).orElse(null);
    }
   
}
