package be.codecoach.repositories;

import be.codecoach.domain.CoachingTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoachingTopicRepository extends JpaRepository<CoachingTopic, String> {
}
