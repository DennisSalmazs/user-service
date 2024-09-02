package com.teambridge.service.impl;

import com.teambridge.dto.RoleDTO;
import com.teambridge.entity.Role;
import com.teambridge.exception.RoleNotFoundException;
import com.teambridge.repository.RoleRepository;
import com.teambridge.service.RoleService;
import com.teambridge.util.MapperUtil;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {


    private final RoleRepository roleRepository;
    private final MapperUtil mapperUtil;

    public RoleServiceImpl(RoleRepository roleRepository, MapperUtil mapperUtil) {
        this.roleRepository = roleRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public RoleDTO readByDescription(String description) {
        Role role = roleRepository.findByDescription(description)
                .orElseThrow(() -> new RoleNotFoundException("Role does not exist."));
        return mapperUtil.convert(role, new RoleDTO());
    }

}
