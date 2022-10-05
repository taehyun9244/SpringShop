package com.example.mvcprac.dto.user;

import com.example.mvcprac.validation.Password;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpForm {

    @NotBlank(message = "이름을 입력해 주세요")
    @Pattern(regexp = "^[가-힣]*$")
    private String username;

    @NotBlank
    @Password
    private String password;

    @NotBlank(message = "주민등록 형식의 생년월일을 입력해주세요")
    @Pattern(regexp = "^((19|20)\\d\\d)?([-/.])?(0[1-9]|1[012])([-/.])?(0[1-9]|[12][0-9]|3[01])$")
    private String birthday;

    @NotBlank
    @Size(min = 3, max = 20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$")
    private String nickname;

    @NotBlank(message = "이메일을 @을 넣어 형식에 맞추어 입력해주세요")
    @Email
    private String email;

    @NotBlank(message = "배송받을 신 도로명과 주소를 입력해 주세요")
    @Pattern(regexp = "(([가-힣]|(\\d{1,5}(~|-)\\d{1,5})|\\d{1,5})+(로|길))")
    private String address;

    @NotBlank
    @Pattern(regexp = "^\\d{2,3}\\d{3,4}\\d{4}$")
    private String phoneNumber;
}
