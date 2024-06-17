package com.pda.utils.api_utils;

import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.*;

class StringUtilsTest {

    @Test
    void 생년월일_성별_2000년대생_여자() {
        String residentNumber = "040201-4";
        assertThat(StringUtils.getBirthDate( residentNumber)).isEqualTo("20040201");
        assertThat(StringUtils.getGender( residentNumber)).isEqualTo("female");
    }

    @Test
    void 생년월일_성별_2000년대생_남자() {
        String residentNumber = "040201-3";
        assertThat(StringUtils.getBirthDate( residentNumber)).isEqualTo("20040201");
        assertThat(StringUtils.getGender( "040201-3")).isEqualTo("male");
    }

    @Test
    void 생년월일_성별_1900년대생_여자() {
        String residentNumber = "910203-2";
        assertThat(StringUtils.getBirthDate( residentNumber)).isEqualTo("19910203");
        assertThat(StringUtils.getGender( residentNumber)).isEqualTo("female");
    }

    @Test
    void 생년월일_성별_1900년대생_남자() {
        String residentNumber = "910203-1";
        assertThat(StringUtils.getBirthDate( residentNumber)).isEqualTo("19910203");
        assertThat(StringUtils.getGender( residentNumber)).isEqualTo("male");
    }

    @Test
    void 전화번호_포맷_8자리() {
        assertThat(StringUtils.formatPhone( "+82 10-1234-5678")).isEqualTo("01012345678");
    }

    @Test
    void 전화번호_포맷_7자리() {
        assertThat(StringUtils.formatPhone( "+82 10-123-5678")).isEqualTo("0101235678");
    }
}