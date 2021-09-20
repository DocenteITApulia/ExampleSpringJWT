package it.apulia.EsSpringJWT.Service;

import it.apulia.EsSpringJWT.domain.Role;
import it.apulia.EsSpringJWT.domain.Utente;

import java.util.List;

public interface ServiceUtente {
    Utente saveUtente(Utente utente);
    Role saveRole(Role role);
    void addRoleToUtente(String username, String roleName);
    Utente getUtente(String username);
    List<Utente> getUtenti();
}
