package com.pda.user_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pda.user_service.jpa.User;
import com.pda.utils.api_utils.StringUtils;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
public class UserDto {
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;
    // TODO: 비밀번호 형식 있다면 추가하기
    @Setter
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
    @NotBlank(message = "전화번호를 입력해주세요.")
    @Pattern(regexp = "^(01(?:0|1|[6-9])-\\d{4}-\\d{4})$", message = "전화번호 형식에 맞지 않습니다.")
    private String phone;
    // TODO: 조건별 주민등록번호 유효성 검사 메소드 추가하기 (ex. 90년대: 1-2, 00년대: 4-5 / 월별 일자)
    @NotBlank(message = "주민등록번호를 입력해주세요.")
    @Pattern(regexp = "^(\\d{2})(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])-[1-4]$", message = "주민등록번호 형식에 맞지 않습니다.")
    @JsonProperty("resident_number")
    private String residentNumber;

    public User toEntity() {
        String birthdate = StringUtils.getBirthDate(residentNumber);
        String gender = StringUtils.getGender(residentNumber);
        return new User(email, name, password, phone, birthdate, gender);
    }
}
