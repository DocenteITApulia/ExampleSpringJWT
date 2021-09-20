package it.apulia.EsSpringJWT.repository;

import it.apulia.EsSpringJWT.domain.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//NOTA: abbiamo scelto come nome Utente per evitare possano esserci ambiguità con altre classi standard User
@Repository
public interface RepositoryUtente extends JpaRepository<Utente, Long> {
    Utente findByUsername(String username);
}
