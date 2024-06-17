package com.pda.utils.api_utils;


// TODO: 입력값 유효성 검사 + 예외 처리
public class StringUtils {
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
}
