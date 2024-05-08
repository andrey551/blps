package tad.blps.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tad.blps.DTO.TokenDTO;
import tad.blps.entity.Payment;
import tad.blps.entity.User;
import tad.blps.repositories.PaymentRepository;
import tad.blps.repositories.UserRepository;
import tad.blps.utils.JwtUtil;

@Service("UserService")
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final JwtUtil jwtUtil = new JwtUtil();

    @Transactional
    public void create(User user) {
        if(userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username existed");
        }

        if(userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email existed");
        }

        userRepository.save(user);
    }
    
    @Transactional
    public void delete(User user) {
        userRepository.delete(user);
        Payment payment = paymentRepository
                .findByUserId(
                        user.getId()
                ).orElse(null);
        paymentRepository.delete(payment);
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
        return (UserDetailsService) this::getByUsername;
    }

    public User getCurrentUser(TokenDTO token) {
        if(jwtUtil.isExpired(token)) return null;
        String username = jwtUtil.getUsername(token.getToken());
        username = username.substring(1, username.length() - 1);
        try{
            return getByUsername(username);
        } catch(Exception e) {
            return null;
        }

    }
}
