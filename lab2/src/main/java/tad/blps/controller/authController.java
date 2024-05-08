package tad.blps.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tad.blps.DTO.AccountDTO;
import tad.blps.DTO.TokenDTO;
import tad.blps.services.AuthenticationService;

@RestController
@RequestMapping("/auth")
public class authController {
    private final AuthenticationService authenticationService;

    public authController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity signUp(@RequestBody @Valid
                                AccountDTO request) {
        TokenDTO token = authenticationService.signUp(request);
        if(token != null) return new ResponseEntity(HttpStatusCode.valueOf(200));
        else return new ResponseEntity(HttpStatusCode.valueOf(400));
    }

    @PostMapping("/login")
    @ResponseBody
    public TokenDTO signIn(@RequestBody @Valid
                               AccountDTO request) {
        return authenticationService.signIn(request);
    }
    
    

}
