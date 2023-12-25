package com.swe.challenge.util;

import java.util.Random;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class RandomUtils {
  private static final Random random = new Random();

  /**
   * Generate a random int from 0 to max(exclusive). Max should greater than 0.
   *
   * @param max the max
   * @return the int
   */
  public static int nextInt(int max) {
    return random.nextInt(max);
  }
}
