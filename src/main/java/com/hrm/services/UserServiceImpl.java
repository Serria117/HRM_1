package com.hrm.services;

import com.hrm.dto.user.UserDto;
import com.hrm.entities.AppUser;
import com.hrm.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service @Slf4j
public class UserServiceImpl {
//    private final UserRepository userRepository;
//
//    public UserServiceImpl(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    public UserDto convertUserToDto(AppUser appUser){
//        UserDto userDto = new UserDto();
//        userDto.setId(appUser.getId());
//        userDto.setUsername(appUser.getUsername());
//        userDto.setFullName(appUser.getFullName());
//        userDto.setEmail(appUser.getEmail());
//        userDto.setPhone(appUser.getPhone());
//        userDto.setAddress(appUser.getAddress());
//        userDto.setBankAccount(appUser.getBankAccount());
//        userDto.setBankFullName(appUser.getBankFullName());
//        userDto.setBankShortName(appUser.getBankShortName());
//        return  userDto;
//    }
//
//    public UserDto getUser(UUID id) throws Exception {
//        var user = userRepository.findById(id).map(this::convertUserToDto).orElseThrow(() -> new Exception("User not found"));
//        return user;
//    }
}
