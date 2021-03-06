package be.codecoach.services;

import be.codecoach.domain.CoachInformation;
import be.codecoach.repositories.CoachInformationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CoachInformationService {

    private final CoachInformationRepository coachInformationRepository;

    public CoachInformationService(CoachInformationRepository coachInformationRepository) {
        this.coachInformationRepository = coachInformationRepository;
    }

    public CoachInformation save(CoachInformation coachInformation) {
        return coachInformationRepository.save(coachInformation);
    }
}
