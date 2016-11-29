package hr.corvuspay.utils;


import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Iterator;

import static hr.corvuspay.exceptions.CorvusPayRuntimeExceptions.InvalidNumberOfInstallments;

public class CorvusPayMiscellaneousUtil {

    public static String getIP(HttpServletRequest request) {
        String userIP = request.getHeader("X-FORWARDED-FOR");
        if (userIP == null)
            userIP = request.getRemoteAddr();
        return userIP;
    }

    public static boolean isValidData(String... fields) {
        for (String field : fields) {
            if (field == null || field.isEmpty())
                return false;
        }
        return true;
    }

    public static void validateInstallment(int installment) {
        if (installment <= 1)
            throw new InvalidNumberOfInstallments();
    }

    public static String join(Collection coll, String delimiter) {
        StringBuilder joinBuilder = new StringBuilder();

        for (Iterator iter = coll.iterator(); iter.hasNext(); joinBuilder.append(iter.next())) {
            if (joinBuilder.length() != 0)
                joinBuilder.append(delimiter);
        }
        return joinBuilder.toString();
    }


}
