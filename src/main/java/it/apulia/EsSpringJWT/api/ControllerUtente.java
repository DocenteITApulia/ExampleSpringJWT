package it.apulia.EsSpringJWT.api;

import it.apulia.EsSpringJWT.Service.ServiceUtente;
import it.apulia.EsSpringJWT.domain.Role;
import it.apulia.EsSpringJWT.domain.Utente;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ControllerUtente {
    private final ServiceUtente serviceUtente;

    @GetMapping("/utenti")
    public ResponseEntity<List<Utente>>getUtenti(){
        return ResponseEntity.ok(this.serviceUtente.getUtenti());
    }

    @PostMapping("/utenti")
    public ResponseEntity<Utente> saveUtente(@RequestBody Utente utente)
    {
        //TODO verificare uri sia corretto, si dovrebbe fare in modo che l'uri indichi il path preciso della risorsa creata
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/utenti").toUriString());
        System.out.println(uri.toString()); //TODO da rimuovere, per test
        return ResponseEntity.created(uri).body(this.serviceUtente.saveUtente(utente));
    }

    @PostMapping("/roles")
    public ResponseEntity<Role> saveRole(@RequestBody Role ruolo)
    {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/roles").toUriString());
        return ResponseEntity.created(uri).body(this.serviceUtente.saveRole(ruolo));
    }

    @PostMapping("/roles/addToUtente")
    public ResponseEntity<?> addRoleToUtente(@RequestBody RoleToUtenteForm ruolo)
    {
        this.serviceUtente.addRoleToUtente(ruolo.getUsername(), ruolo.getRoleName());
        return ResponseEntity.ok().build();
    }
}

@Data
class RoleToUtenteForm {
    private String username;
    private String roleName;
}
