package be.codecoach.repositories;

import be.codecoach.domain.CoachInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoachInformationRepository extends JpaRepository<CoachInformation, String> {
}
