package com.hrm;

import com.hrm.configurations.SwaggerConfig;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

 //   @Bean
//    CommandLineRunner commandLineRunner(RoleRepository roleRepository)
//    {
//        return args -> {
//            var roles = new ArrayList<AppRole>();
//            roles.add(new AppRole().setRoleName("ROLE_ADMIN"));
//            roles.add(new AppRole().setRoleName("ROLE_GUEST"));
//            roleRepository.saveAll(roles);
//        };
//    }
}
