package tad.blps.DTO;

import lombok.Data;

@Data
public class TokenDTO {
    private final String token;

    public TokenDTO(String token) {
        this.token = token;
    }
}
