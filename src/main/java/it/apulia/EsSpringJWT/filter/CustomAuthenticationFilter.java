package it.apulia.EsSpringJWT.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        log.info("L'username {} ha inserito la password {}", username,password);

        //nel metodo successivo sarebbe stato possibile inserire ulteriori parametri
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password); //generiamo il token con i dati dell'utente
        authenticationManager.authenticate(authenticationToken); //diciamo all'authenticationManager che il token creato è valido

        return authenticationManager.authenticate(authenticationToken);
    }

    //il sito jwt.io ci permette di ottenere i dati dal token
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        User user = (User)authentication.getPrincipal(); //da questo utente prendiamo le informazioni per la generazione del token

        //in una situazione reale il seed sottostante è generato in qualche modo ed è criptato e quindi di volta in volta viene caricato
        //da una risorsa esterna che se ne occupa e dunque passato
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes()); //secret è la parola che fa da seed

        //in questo caso sto passando l'username perché diamo per assodato sia unico
        //All'interno di date sono passati 10 min
        String access_token = JWT.create().withSubject(user.getUsername()).withExpiresAt(new Date(System.currentTimeMillis()+ 10*60*1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm); //il token è creato
        String refresh_token = JWT.create().withSubject(user.getUsername()).withExpiresAt(new Date(System.currentTimeMillis()+ 30*60*1000))
                .withIssuer(request.getRequestURL().toString()).sign(algorithm);
        //response.addHeader("access_token",access_token);
        //response.addHeader("refresh_token",refresh_token);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
    //analogamente sarebbe possibile gestire il caso in cui vi è l'unsuccessful authentication
}
