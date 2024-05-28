package tad.blps.controller;

import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tad.blps.DTO.PaymentDTO;
import tad.blps.DTO.TokenDTO;
import tad.blps.services.PaymentService;
import tad.blps.services.UserService;

@RestController
@RequestMapping("/payment")
public class paymentController {
    private final PaymentService paymentService;
    private final UserService userService;

    @Autowired
    public paymentController(PaymentService paymentService,
                             UserService userService) {
        this.paymentService = paymentService;
        this.userService = userService;
    }

    @PostMapping(path = "/update")
    @RolesAllowed({"USER", "ADMIN"})
    public ResponseEntity<?> updatePayment(
            @RequestBody PaymentDTO paymentDTO,
            @RequestHeader("Authorization") TokenDTO token
    ) {
        long userId = userService.getCurrentUser(token).getId();
        paymentService.updatePayment(paymentDTO, userId);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

}
