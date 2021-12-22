package be.codecoach.services;

import be.codecoach.api.dtos.RoleDto;
import be.codecoach.domain.Role;
import be.codecoach.domain.RoleEnum;
import be.codecoach.repositories.RoleRepository;
import be.codecoach.services.mappers.RoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    public Role findByRole(RoleEnum roleEnum) {
        return roleRepository.findByRole(roleEnum);
    }

}
