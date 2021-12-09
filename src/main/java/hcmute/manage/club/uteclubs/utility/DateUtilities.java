package hcmute.manage.club.uteclubs.utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtilities {
    public static long differenceInYear(Date inputDate) {
        Date today = new Date();
        long differenceInTime = today.getTime() - inputDate.getTime();
        return differenceInTime / (1000L * 60 * 60 * 24 * 365);
    }

    public static Date convertToUTC(Date localDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat utcDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        utcDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return utcDateFormat.parse(simpleDateFormat.format(localDate));
        } catch (Exception ex) {
            return new Date();
        }
    }
}
