package tad.blps.services;

import bitronix.tm.BitronixTransactionManager;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tad.blps.DTO.AccountDTO;
import tad.blps.DTO.TokenDTO;
import tad.blps.SecurityDetail.UserDetailImpl;
import tad.blps.entity.Payment;
import tad.blps.entity.User;
import tad.blps.repositories.PaymentRepository;
import tad.blps.repositories.UserRepository;
import tad.blps.utils.BasicAuthUtil;

@Service("UserService")
@RequiredArgsConstructor
public class UserService {
//    @Autowired
//    private BitronixTransactionManager userTransaction;
    
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    
    @Transactional
    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    @Transactional
    public void create(User user) {
        if(userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username existed");
        }

        if(userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email existed");
        }
//        try {
//            userTransaction.begin();
            userRepository.save(user);
            User user2 = getByUsername(user.getUsername());
            Payment paymentToAdd = new Payment();
            paymentToAdd.setUserId(user2.getId().intValue());
            paymentRepository.save(paymentToAdd);
//            userTransaction.commit();
//        } catch ( Exception e) {
//            try {
//                userTransaction.rollback();
//            } catch (IllegalStateException ex) {
//                Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (SecurityException ex) {
//                Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (javax.transaction.SystemException ex) {
//                Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
    }
    @Transactional
    public void delete(User user) {
//        try {
//            userTransaction.begin();
            
            userRepository.delete(user);
            Payment payment = paymentRepository
                    .findByUserId(
                            user.getId()
                    ).orElse(null);
            paymentRepository.delete(payment);
//            userTransaction.commit();
//        } catch ( Exception e) {
//            try {
//                userTransaction.rollback();
//            } catch (IllegalStateException ex) {
//                Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (SecurityException ex) {
//                Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (javax.transaction.SystemException ex) {
//                Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
//            }
//           
//        }
    }
    
    
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                "Username does not exist"));
    }

    public User getByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(() 
                        -> new UsernameNotFoundException(
                                "Username or password is not correct"));
    }

    public UserDetailsService userDetailsService() {
        return username -> new UserDetailImpl( this.getByUsername(username));
    }

    public User getCurrentUser(TokenDTO token) {
        AccountDTO account = BasicAuthUtil.decode(token.getToken());
        try{
            return getByUsernameAndPassword(
                    account.getUsername(), 
                    account.getPassword());
        } catch(Exception e) {
            return null;
        }
    }
}
