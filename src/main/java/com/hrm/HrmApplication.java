package com.hrm;

import com.hrm.configurations.SwaggerConfig;
import com.hrm.entities.AppRole;
import com.hrm.entities.ContractType;
import com.hrm.repositories.ContractTypeRepository;
import com.hrm.repositories.RoleRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
@SecurityScheme(
        name = SwaggerConfig.SECURITY_NAME,
        scheme = SwaggerConfig.SECURITY_SCHEME,
        description = "Enter JWT token for authentication (no prefix required)",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER)
@OpenAPIDefinition(
        info = @Info(
                title = "HRM System API",
                version = "1.0",
                description = "API document for EMS"))
public class HrmApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(HrmApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(RoleRepository roleRepository,
                                        ContractTypeRepository contractTypeRepository)
    {
        String[] roleToCreate = {
                "ROLE_SYSADMIN",
                "ROLE_HR",
                "ROLE_EMPLOYEE"};
        var roles = new ArrayList<AppRole>();

        String[] contractTypeToCreate = {
                "Internship", "Partnership", "Fixed-term contract", "Indefinite contract"
        };
        var contractTypes = new ArrayList<ContractType>();

        return args -> {
            //Seed some roles:
            for ( var role : roleToCreate ) {
                if ( !roleRepository.existByName(role) ) {
                    roles.add(new AppRole().setRoleName(role));
                }
            }
            if ( !roles.isEmpty() ) {
                roleRepository.saveAll(roles);
            }
            //Seed some contract types:
            for ( var type : contractTypeToCreate ) {
                if ( !contractTypeRepository.existByName(type) ) {
                    contractTypes.add(new ContractType().setTypeName(type));
                }
            }
            if ( !contractTypes.isEmpty() ) {
                contractTypeRepository.saveAll(contractTypes);
            }
        };
    }
}
