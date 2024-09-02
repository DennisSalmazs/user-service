package com.teambridge.service.impl;

import com.teambridge.dto.UserDTO;
import com.teambridge.entity.User;
import com.teambridge.exception.UserAlreadyExistsException;
import com.teambridge.exception.UserNotFoundException;
import com.teambridge.repository.UserRepository;
import com.teambridge.service.KeycloakService;
import com.teambridge.service.UserService;
import com.teambridge.util.MapperUtil;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;
    private final KeycloakService keycloakService;

    public UserServiceImpl(UserRepository userRepository, MapperUtil mapperUtil, KeycloakService keycloakService) {
        this.userRepository = userRepository;
        this.mapperUtil = mapperUtil;
        this.keycloakService = keycloakService;
    }

    @Override
    public UserDTO create(UserDTO userDTO) {

        Optional<User> foundUser = userRepository.findByUserNameAndIsDeleted(userDTO.getUserName(), false);

        if (foundUser.isPresent()) {
            throw new UserAlreadyExistsException("User already exists.");
        }

        userDTO.setEnabled(true);

        User userToSave = mapperUtil.convert(userDTO, new User());

        //keycloakService.userCreate(userDTO);
        User savedUser = userRepository.save(userToSave);

        return mapperUtil.convert(savedUser, new UserDTO());

    }

    @Override
    public UserDTO readByUserName(String username) {
        User foundUser = userRepository.findByUserNameAndIsDeleted(username, false)
                .orElseThrow(() -> new UserNotFoundException("User does not exist."));
        return mapperUtil.convert(foundUser, new UserDTO());
    }

    @Override
    public List<UserDTO> readAllUsers() {
        List<User> foundUsers = userRepository.findAllByIsDeleted(false, Sort.by("firstName"));
        return foundUsers.stream().map(user -> mapperUtil.convert(user, new UserDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkByUserName(String username) {
        userRepository.findByUserNameAndIsDeleted(username, false)
                .orElseThrow(() -> new UserNotFoundException("User does not exist."));
        return true;
    }

    @Override
    public UserDTO update(String username, UserDTO userDTO) {

        User foundUser = userRepository.findByUserNameAndIsDeleted(username, false)
                .orElseThrow(() -> new UserNotFoundException("User does not exist."));

        userDTO.setUserName(username);
        userDTO.setEnabled(true);
        userDTO.setId(foundUser.getId());

        User userToUpdate = mapperUtil.convert(userDTO, new User());

        //keycloakService.userUpdate(userDTO);
        User updatedUser = userRepository.save(userToUpdate);

        return mapperUtil.convert(updatedUser, new UserDTO());

    }

    @Override
    public void delete(String username) {

        User userToDelete = checkIfUserCanBeDeleted(username);

        userToDelete.setUserName(username + "-" + userToDelete.getId());
        userToDelete.setIsDeleted(true);

        //keycloakService.delete(username);
        userRepository.save(userToDelete);

    }

    private User checkIfUserCanBeDeleted(String username) {

        User userToDelete = userRepository.findByUserNameAndIsDeleted(username, false)
                .orElseThrow(() -> new UserNotFoundException("User does not exist."));

        checkUserConnections(userToDelete);

        return userToDelete;

    }

    private void checkUserConnections(User userToDelete) {

        String role = userToDelete.getRole().getDescription();
        String username = userToDelete.getUserName();

        switch (role) {
            case "Manager":
                checkManagerConnections(username);
                break;
            case "Employee":
                checkEmployeeConnections(username);
                break;
        }

    }

    private void checkManagerConnections(String username) {

        //TODO Get the needed information from project-service

    }

    private void checkEmployeeConnections(String username) {

        //TODO Get the needed information from task-service

    }

    //TODO Extract the authorization token from the original request and add it to the request sent to next microservice

}
