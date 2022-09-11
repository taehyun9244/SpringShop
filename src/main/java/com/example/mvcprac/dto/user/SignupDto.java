package com.example.mvcprac.dto.user;

import com.example.mvcprac.validation.Password;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupDto {

    @NotBlank(message = "이름을 입력해 주세요")
    @Pattern(regexp = "^[가-힣]*$", message = "띄어쓰기 없이 한글만 입력해주세요")
    private String username;

    @NotBlank
    @Password
    private String password;

    @NotBlank(message = "YYMMDD 형태로 입력해주세요")
    private String birthday;

    @NotBlank(message = "이메일을 @을 넣어 형식에 맞추어 입력해주세요")
    @Email
    private String email;

    @NotBlank(message = "배송받을 주소를 입력해 주세요")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣0-9]*$\\s ", message = "한글과 숫자로만 입력이 가능합니다")
    private String address;

    @NotBlank
    @Pattern(regexp = "^\\d{2,3}\\d{3,4}\\d{4}$", message = " '-'를 제외한 전화번호 11자리를 입력해주세요")
    private String phoneNumber;
}
