package user.service.User.Service.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email, password;
}
