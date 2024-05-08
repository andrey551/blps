package tad.blps.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tad.blps.DTO.AccountDTO;
import tad.blps.DTO.TokenDTO;
import tad.blps.entity.User;
import tad.blps.utils.JwtUtil;

@Service
@RequiredArgsConstructor
public class AuthenticationService{
    private final UserService userService;
    private final JwtUtil jwtUtil = new JwtUtil();
    public TokenDTO signUp(AccountDTO accountDTO) {

        System.out.println(accountDTO.getUsername() + " " +
                accountDTO.getPassword() + " " +
                accountDTO.getEmail());

        var user = new User(
                    accountDTO.getUsername(),
                    accountDTO.getPassword(),
                    accountDTO.getEmail());
        userService.create(user);

        var jwt = jwtUtil.generateToken( user);

        return new TokenDTO(jwt.getToken());
    }

    public TokenDTO signIn(AccountDTO account) {
        try {
            User user = userService.getByUsernameAndPassword(
                    account.getUsername(),
                    account.getPassword()
            );


            var jwt = jwtUtil.generateToken(user);
            return new TokenDTO(jwt.getToken());
        } catch (Exception e) {
            return null;
        }
    }

}
