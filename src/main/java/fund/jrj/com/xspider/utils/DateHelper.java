package fund.jrj.com.xspider.utils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

public class DateHelper {
  public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";
  public static final String yyyyMMddSpaceHHmmss = "yyyy-MM-dd HH:mm:ss";
  public static final String yyyyMMdd = "yyyyMMdd";
  public static final String yyyy_MM_dd = "yyyy-MM-dd";
  public static String getNowFormatyyyyMMddHHmmss() {
    return DateFormatUtils.format(Calendar.getInstance().getTime(), yyyyMMddHHmmss);
  }
  
  public static String getFormatyyyyMMddHHmmss(Date date) {
    if(date==null){
      return "";
    }
    return DateFormatUtils.format(date, yyyyMMddHHmmss);
  }
  public static String getFormatString(Date date,String format) {
    if(date==null){
      return "";
    }
    return DateFormatUtils.format(date, format);
  }
  public static String getFormatyyyyMMdd(Date date) {
    if(date==null){
      return "";
    }
    return DateFormatUtils.format(date, yyyy_MM_dd);
  }
  public static Date parseFormatDateyyyyMMddHHmmss(String dateStr) {
    try {
      return DateUtils.parseDate(dateStr, yyyyMMddHHmmss);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Date parseFormatDateyyyyMMdd(String dateStr) {
    try {
      return DateUtils.parseDate(dateStr, yyyyMMdd);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }
  public static Date parseFormatDateFormat(String dateStr,String format) {
    try {
      return DateUtils.parseDate(dateStr, format);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }
  public static Date getNowDateyyyyMMddHHmmss(){
    Date now=Calendar.getInstance().getTime();
    now= DateUtils.truncate(now, Calendar.SECOND);
    return now;
  }

  /**
   * 返回当前时间
   * 
   * @return
   */
  public static Date now() {
    return Calendar.getInstance().getTime();
  }
}
