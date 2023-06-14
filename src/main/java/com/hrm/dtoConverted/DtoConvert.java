package com.hrm.dtoConverted;

import com.hrm.dto.Leave.LeaveDto;
import com.hrm.dto.department.DepartmentDto;
import com.hrm.dto.user.UserDto;
import com.hrm.entities.AppRole;
import com.hrm.entities.AppUser;
import com.hrm.entities.Department;
import com.hrm.entities.Leave;
import com.hrm.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DtoConvert {

    private final UserRepository userRepository;
    private final DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public UserDto convertUserToDto(AppUser appUser)
    {
        List<String> lstRole = new ArrayList<>();
        var userDto = new UserDto()
                .setId(appUser.getId())
                .setUsername(appUser.getUsername())
                .setFullName(appUser.getFullName())
                .setEmail(appUser.getEmail())
                .setPhone(appUser.getPhone())
                .setAddress(appUser.getAddress())
                .setBankAccount(appUser.getBankAccount())
                .setBankFullName(appUser.getBankFullName())
                .setBankShortName(appUser.getBankShortName());
        for(AppRole role : appUser.getRoles()){
            lstRole.add(role.getRoleName());
        }
//        .setRole(appUser.getRoles().stream().map(AppRole::getRoleName).toList());
        userDto.setRole(lstRole);
        var isActivated = appUser.getIsActivated()
                ? "Hoạt động" : "Không hoạt động";
        userDto.setIsActivated(isActivated);
        return userDto;
    }

    public LeaveDto convertLeaveToDto(Leave leave){
        var durationDto = leave.getDuration() == 0 ? "Cả ngày" : "Nửa ngày";
//        var isActivatedDto = leave.getIsActivated() ? "Đồng ý" : "";
        return new LeaveDto()
                .setId(leave.getId())
                .setEplName(userRepository.getReferenceById(leave.getRegisterEmployee()).getUsername())
                .setDateApply(leave.getDateApply().format(formatters))
                .setDuration(durationDto)
                .setDescription(leave.getDescription())
                .setIsActivated(leave.getIsActivated());
    }

//    public DepartmentDto convertedDpmDto(Department department){
//        return new DepartmentDto()
//                .setId(department.getId())
//                .setMngUser(userRepository.getReferenceById(department.getManagementUser().getId()).getFullName())
//                .setDepartmentCode(department.getDepartmentCode())
//                .set
//    }
}
