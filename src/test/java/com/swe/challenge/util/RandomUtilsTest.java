package com.swe.challenge.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RandomUtilsTest {

  @Test
  void nextInt() {
    for (int i = 0; i < 10; i++) {
      int num = RandomUtils.nextInt(2);
      assertTrue(num >= 0 && num <= 2);
    }
  }
}
