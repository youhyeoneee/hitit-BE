package com.pda.utils.api_utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomNumberUtilsTest {

    @Test
    void 백의자리_반올림() {
        assertThat(CustomNumberUtils.roundToNearestHundred(109272700)).isEqualTo(109273000);
    }
}