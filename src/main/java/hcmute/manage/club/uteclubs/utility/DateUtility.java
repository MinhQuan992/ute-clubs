package hcmute.manage.club.uteclubs.utility;

import java.util.Date;

public class DateUtility {
    public static long differenceInYear(Date inputDate) {
        Date today = new Date();
        long differenceInTime = today.getTime() - inputDate.getTime();
        return differenceInTime / (1000L * 60 * 60 * 24 * 365);
    }
}
