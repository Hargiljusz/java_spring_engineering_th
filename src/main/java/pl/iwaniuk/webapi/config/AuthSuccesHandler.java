package pl.iwaniuk.webapi.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pl.iwaniuk.webapi.repository.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthSuccesHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final long expirationTime;
    private final String secret;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;

    public AuthSuccesHandler(
            @Value("${jwt_prop.expirationTime}") long expirationTime,
            @Value("${jwt_prop.secretKey}") String secret) {
        this.expirationTime = expirationTime;
        this.secret = secret;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        Date dateEXP = new Date(System.currentTimeMillis() + expirationTime+400);
        String token = JWT.create()
                .withSubject(principal.getUsername())
                .withExpiresAt(dateEXP)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .sign(Algorithm.HMAC256(secret));
        response.setContentType("application/json");

        Map<Object, Object> model = new HashMap<>();
        model.put("token",token);
        model.put("timeEXP",dateEXP.getTime());
        model.put("currentTime",new Date(System.currentTimeMillis()).getTime());
        model.put("username",principal.getUsername());
        model.put("userID",userRepository.findByEmail(principal.getUsername()).get().getId());
        model.put("roles",principal.getAuthorities());

        String msg = objectMapper.writeValueAsString(model);

        response.getOutputStream().print(msg);
    }
}
