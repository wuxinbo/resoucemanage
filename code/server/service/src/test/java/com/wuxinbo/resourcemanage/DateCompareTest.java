package com.wuxinbo.resourcemanage;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hibernate.type.descriptor.java.JdbcDateTypeDescriptor.DATE_FORMAT;

public class DateCompareTest {
  private static final String TAG="HomeFragment";
  private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年M月dd日");
  @Test
  public void dateCompare() {
    Set<String> days = new HashSet<>();
    days.add("2023年5月01日");
    days.add("2023年6月01日");
    days.add("2022年4月02日");
    days.add("2023年6月01日");
    days.add("2022年3月02日");
    days.add("2023年8月11日");
    days.add("2022年9月22日");
    List<String> collect = days.stream().sorted(((s, t1) -> {
      try {
        return (dateFormat.parse(t1).compareTo(dateFormat.parse(s)));
      } catch (ParseException e) {
      }
      return 0;
    })).collect(Collectors.toList());
    System.out.printf("");
  }
}
