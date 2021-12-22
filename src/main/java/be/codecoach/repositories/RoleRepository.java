package be.codecoach.repositories;

import be.codecoach.domain.Role;
import be.codecoach.domain.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRole(RoleEnum role);
}
