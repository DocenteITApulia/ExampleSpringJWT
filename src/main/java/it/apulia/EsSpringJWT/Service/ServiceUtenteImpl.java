package it.apulia.EsSpringJWT.Service;

import it.apulia.EsSpringJWT.domain.Role;
import it.apulia.EsSpringJWT.domain.Utente;
import it.apulia.EsSpringJWT.repository.RepositoryUtente;
import it.apulia.EsSpringJWT.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor //lombok crea un costruttore per quei campi dichiarati final o @NonNull
@Transactional
@Slf4j //utile per il logging
public class ServiceUtenteImpl implements ServiceUtente{

    private final RepositoryUtente repositoryUtente;
    private final RoleRepository roleRepository;


    @Override
    public Utente saveUtente(Utente utente) {
        log.info("Salvataggio dell'utente {} all'interno del database", utente.getUsername());
        //esempio di log, realisticamente da utilizzare in maniera sensata un po' ovunque
        return this.repositoryUtente.save(utente);
    }

    @Override
    public Role saveRole(Role role) {
        return this.roleRepository.save(role);
    }

    //in questo caso l'operazione viene eseguita dato il tag @transactional definita all'inizio della classe
    //in casi reali si fa più attenzione sull'utilizzo di questa annotazione, ci saranno altri check da fare
    @Override
    public void addRoleToUtente(String username, String roleName) {
        Utente utente = this.repositoryUtente.findByUsername(username);
        Role role = this.roleRepository.findByNome(roleName);
        log.info("Aggiungo il ruolo {} all'utente {}.",role.getNome(),utente.getUsername());
        utente.getRoles().add(role);
    }

    @Override
    public Utente getUtente(String username) {
        return this.repositoryUtente.findByUsername(username);
    }

    @Override
    public List<Utente> getUtenti() {
        return this.repositoryUtente.findAll();
    }
}
