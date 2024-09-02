package com.teambridge.service;

import com.teambridge.dto.UserDTO;

public interface KeycloakService {

    void userCreate(UserDTO userDTO);

    void userUpdate(UserDTO userDTO);

    void delete(String userName);

}
