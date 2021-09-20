package it.apulia.EsSpringJWT.repository;

import it.apulia.EsSpringJWT.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByNome(String nome);
}
