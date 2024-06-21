package com.pda.utils.api_utils;


import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

// TODO: 입력값 유효성 검사 + 예외 처리
public class CustomStringUtils {
    public static String getGender(String residentNumber){
        String backDigit = residentNumber.split("-")[1];
        int genderCode = Integer.parseInt(backDigit);
        if (genderCode % 2 == 0){
            return "female";
        }
        return "male";
    }

    public static String getBirthDate(String residentNumber){
        String[] digits = residentNumber.split("-");
        String frontDigit = digits[0];
        String backDigit =  digits[1];
        int divisionCode = Integer.parseInt(backDigit);

        if (divisionCode == 1 || divisionCode == 2){
            return  "19" + frontDigit;
        } else if (divisionCode == 3 || divisionCode == 4){
            return  "20" + frontDigit;
        }
        return null;
    }

    public static String formatPhone(String phone) {
        String result = phone.replaceAll("[^0-9]", "");
        if (result.startsWith("82")) {
            result = "0" + result.substring(2);
        }
        return result.replaceFirst("(\\d{3})(\\d{4})(\\d+)", "$1-$2-$3");
    }

    public static String getToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public static String formatMoney(int money) {
        DecimalFormat decimalFormat = new DecimalFormat();
        return decimalFormat.format(money);
    }

    public static int calculateAge(String birthdate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate birthDate = LocalDate.parse(birthdate, formatter);
        LocalDate currentDate = LocalDate.now();

        int age = (int) ChronoUnit.YEARS.between(birthDate, currentDate);
        // 생일이 지났는지 확인하여 만 나이 계산
        if (currentDate.getDayOfYear() < birthDate.getDayOfYear()) {
            age--;
        }
        return age;
    }

}
