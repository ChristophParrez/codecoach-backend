package be.codecoach.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import be.codecoach.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByEmail(String email);
    boolean existsByEmail(String email);
}
