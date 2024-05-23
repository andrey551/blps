/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tad.blps.SecurityDetail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tad.blps.entity.Role;
import tad.blps.entity.User;
import tad.blps.repositories.UserRepository;

/**
 *
 * @author Never
 */
@Service
public class UserDetailServiceImpl implements 
                                UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) 
                                throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElse(null);
        for(Role role : user.getRoles()) {
            System.out.println(role.getName());
        }
        if(user == null ) {
            throw new UsernameNotFoundException("Couldn't find user");
        }
        
        return new UserDetailImpl(user);
    }
    
}
