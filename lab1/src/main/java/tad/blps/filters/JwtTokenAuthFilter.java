//package tad.blps.filters;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.NonNull;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.web.filter.OncePerRequestFilter;
//import tad.blps.utils.JwtUtil;
//
//import java.io.IOException;
//
//@Slf4j
//@RequiredArgsConstructor
//public class JwtTokenAuthFilter extends OncePerRequestFilter {
//
//    private static final String AUTHORIZATION_HEADER = "Authorization";
//    private static final String BEARER_TOKEN_PREFIX = "Bearer ";
//
//    private final UserDetailsService userDetailsService;
//    private final JwtUtil jwtUtil;
//
//    @Override
//    protected void doFilterInternal(
//            final HttpServletRequest request,
//            final @NonNull HttpServletResponse response,
//            final @NonNull FilterChain filterChain)
//            throws ServletException, IOException {
//        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);
//
//        if (authHeader == null || authHeader.isEmpty() || !authHeader.startsWith(BEARER_TOKEN_PREFIX)) {
//            log.info("Caught unauthorized request for URL = {}", request.getRequestURL().toString());
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        final String jwtToken = authHeader.replace(BEARER_TOKEN_PREFIX, "");
//        try {
//            if (jwtUtil.isTokenValid(jwtToken)) {
//                final var username = jwtUtil.getUsername(jwtToken);
//                final var user = userDetailsService.loadUserByUsername(username);
//
//                final Authentication authentication =
//                        new UsernamePasswordAuthenticationToken(user, null, jwtUtil.getAuthorities(jwtToken));
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        } catch (final Exception e) {
//            log.warn("Error handling JWT token: {}", e.getMessage());
//            throw e;
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
