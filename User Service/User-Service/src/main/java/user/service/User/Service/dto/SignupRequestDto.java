package user.service.User.Service.dto;

import lombok.Data;

@Data
public class SignupRequestDto {
    private String name, email, password;
}
