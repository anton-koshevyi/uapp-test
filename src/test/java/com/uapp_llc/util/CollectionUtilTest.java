package com.uapp_llc.util;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class CollectionUtilTest {

  @Test
  public void move_whenNullSource_expectException() {
    Assertions
        .assertThatThrownBy(() -> CollectionUtil.move(null, 4, 1))
        .isExactlyInstanceOf(NullPointerException.class)
        .hasMessage("Source must not be null");
  }

  @Test
  public void move_whenFromIndexEqualToIndex_expectUnchanged() {
    List<Integer> source = Arrays.asList(1, 2, 3, 4, 5);

    Assertions
        .assertThat(CollectionUtil.move(source, 1, 1))
        .isEqualTo(Lists.newArrayList(1, 2, 3, 4, 5));
  }

  @Test
  public void move_whenFromIndexAfterToIndex() {
    List<Integer> source = Arrays.asList(1, 2, 3, 4, 5);

    Assertions
        .assertThat(CollectionUtil.move(source, 4, 1))
        .isEqualTo(Lists.newArrayList(1, 5, 2, 3, 4));
  }

  @Test
  public void move_whenToIndexAfterFromIndex() {
    List<Integer> source = Arrays.asList(1, 2, 3, 4, 5);

    Assertions
        .assertThat(CollectionUtil.move(source, 0, 3))
        .isEqualTo(Lists.newArrayList(2, 3, 4, 1, 5));
  }

}
