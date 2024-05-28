//package tad.blps.utils;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.JWTVerifier;
//import com.auth0.jwt.exceptions.JWTVerificationException;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import tad.blps.DTO.TokenDTO;
//import tad.blps.entity.User;
//
//import java.io.PrintStream;
//import java.util.Date;
//import java.util.UUID;
//
//public class JwtUtil {
//    private final int LIVE_PERIOD = 144000000;
//
//    private static String secret_key = "tad";
//
//    private static final Algorithm algorithm = Algorithm.HMAC256(secret_key);
//    public TokenDTO generateToken(User user) {
//        String id = user.getMember_passkey();
//        Date crt = new Date(System.currentTimeMillis());
//        Date expires = new Date(System.currentTimeMillis() + LIVE_PERIOD);
//        String tokenStr = JWT.create()
//                .withIssuer(secret_key)
//                .withSubject(user.getMember_passkey())
//                .withClaim("username", user.getUsername())
//                .withIssuedAt(crt)
//                .withExpiresAt(expires)
//                .withJWTId(UUID.randomUUID().toString())
//                .sign(algorithm);
//
//        return new TokenDTO(tokenStr);
//    }
//
//    public DecodedJWT decodeToken(TokenDTO token) {
//        try{
//            JWTVerifier verifier = JWT.require(algorithm)
//                    .withIssuer(secret_key)
//                    .build();
//            return verifier.verify(token.getToken());
//        } catch(JWTVerificationException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    public boolean isExpired(TokenDTO token) {
//        DecodedJWT decode = decodeToken(token);
//        if(decode != null) {
//            return decode
//                    .getExpiresAt()
//                    .compareTo(
//                            new Date(System.currentTimeMillis())
//                    ) < 0;
//        }
//
//        return false;
//    }
//
//    public String getMemberPasskey(TokenDTO token) {
//        DecodedJWT decode = decodeToken(token);
//        if(decode != null) {
//            return decode
//                    .getSubject();
//        }
//
//        return null;
//    }
//
//    public boolean isTokenValid(final String token) {
//        return !isExpired(new TokenDTO(token));
//    }
//
//    public String getUsername(String jwtToken) {
//        DecodedJWT decode = decodeToken(new TokenDTO(jwtToken));
//        if(decode != null) {
//            return decode
//                    .getClaim("username")
//                    .toString();
//        }
//        return null;
//    }
//
//
////    public Collection<? extends GrantedAuthority> getAuthorities(String jwtToken) {
//////        final var claims = getAllClaimsFromToken(token);
////        final var authorities = (List<String>) claims.get(jwtProperties.getAuthoritiesClaim());
////        return authorities.stream()
////                .map(SimpleGrantedAuthority::new)
////                .collect(Collectors.toUnmodifiableSet());
////    }
//}
