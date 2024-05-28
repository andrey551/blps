package tad.blps.services;

import bitronix.tm.BitronixTransactionManager;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tad.blps.DTO.AccountDTO;
import tad.blps.DTO.TokenDTO;
import tad.blps.entity.Role;
import tad.blps.entity.User;
import tad.blps.utils.BasicAuthUtil;

@Service
@RequiredArgsConstructor
public class AuthenticationService{
    @Autowired
    private BitronixTransactionManager userTransaction;
    
    private final RoleService roleService;
    private final UserService userService;
    
    public TokenDTO signUp(AccountDTO accountDTO) {

        var user = new User(
                    accountDTO.getUsername(),
                    accountDTO.getPassword(),
                    accountDTO.getEmail());
        try {
            Set<Role> roles = new HashSet<>();
            Role role = roleService.findByName(accountDTO.getRole());
            roles.add(role);
            userTransaction.begin();
            user.setRoles(roles);
            userService.create(user);
            
            userTransaction.commit();
        } catch (Exception e) {
            try {
                userTransaction.rollback();
            } catch (IllegalStateException ex) {
                Logger.getLogger(AuthenticationService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(AuthenticationService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (javax.transaction.SystemException ex) {
                Logger.getLogger(AuthenticationService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        

        String jwt = BasicAuthUtil.encode(
                                    user.getUsername(), 
                                    user.getPassword());
        return new TokenDTO(jwt);
    }
    @Transactional
    public TokenDTO signIn(AccountDTO account) {
        try {
            User user = userService.getByUsernameAndPassword(
                                        account.getUsername(),
                                        account.getPassword());

            String jwt = BasicAuthUtil.encode(
                                        user.getUsername(), 
                                        user.getPassword());
            return new TokenDTO(jwt);
        } catch (Exception e) {
            return null;
        }
    }

}
