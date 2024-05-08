/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tad.blps.SecurityInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tad.blps.entity.Privilege;
import tad.blps.entity.Role;
import tad.blps.entity.User;
import tad.blps.repositories.RoleRepository;
import tad.blps.repositories.UserRepository;

/**
 *
 * @author Never
 */
@Service("UserDetailServiceImpl")
@Transactional
public class UserDetailServiceImpl implements 
                                UserDetailsService{
    @Autowired
    private UserRepository userRepository;
    
    
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) 
                                throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElse(null);
        if(user == null ) {
            throw new UsernameNotFoundException("Couldn't find user");
        }
        
        return new UserDetailImpl(user);
    }
    
    private Collection<? extends GrantedAuthority> getAuthorities(
                                                    List<Role> roles) {
 
        return getGrantedAuthorities(getPrivileges(roles));
    }
    
    private List<String> getPrivileges(List<Role> roles) {
 
        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();
        for (Role role : roles) {
            privileges.add(role.getName());
            collection.addAll(role.getPrivileges());
        }
        for (Privilege item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }
    
    private List<GrantedAuthority> getGrantedAuthorities(
                                                List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
