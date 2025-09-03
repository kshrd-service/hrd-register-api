package kh.com.kshrd.hrdregisterapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@OpenAPIDefinition(info = @Info(title = "HRD Register Api",
        version = "v1",
        description = "This is description"))
@SecurityScheme(
        name = "hrd",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class HrdRegisterApiApplication {



    public static void main(String[] args) {
        SpringApplication.run(HrdRegisterApiApplication.class, args);
    }

}
