package yell.server.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by abdulkerim on 29.05.2016.
 */
public class DateFormatter {

    private static final String DATE_FORMAT_STRING = "YYYYMMddHHmmss";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_STRING);

    public static Date parse(String date) {
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String format(Date date) {
        return dateFormat.format(date);
    }
}
