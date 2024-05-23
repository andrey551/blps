package tad.blps.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class PaymentDTO {
    @PositiveOrZero
    private Double balance;
    @NotBlank
    private String withdrawAccount;
}
