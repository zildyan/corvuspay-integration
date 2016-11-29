package hr.corvuspay.utils;

import hr.corvuspay.types.CorvusPayRequestFieldType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static hr.corvuspay.exceptions.CorvusPayRuntimeExceptions.RequestDataEmptyException;
import static hr.corvuspay.utils.CorvusPayMiscellaneousUtil.join;
import static hr.corvuspay.utils.CorvusPayMiscellaneousUtil.validateInstallment;

public class CorvusPayFormatUtil {

    static String formatByteToHex(byte b) {
        return String.format("%02x", b & 0xff);
    }

    public static String formatTwoDigits(int num) {
        return String.format("%02d", num);
    }

    public static String formatDecimalNumber(String num) {
        if (!num.contains(","))
            return formatRound(num);

        int i = num.indexOf(",");
        if (num.indexOf(",", i) != -1)
            throw new NumberFormatException("Multiple commas in string representation of number");

        return formatRound(num.replace(",", "."));
    }

    private static String formatRound(String value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.toString();
    }

    public static String formatCartEntries(Map<String, Integer> cartEntries) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : cartEntries.entrySet()) {
            sb.append(entry.getKey()).append(" x ").append(entry.getValue()).append(", ");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));

        String cartDetails = sb.toString();
        if (cartDetails.length() > 200) {
            cartDetails = cartDetails.substring(0, 197);
            cartDetails += "...";
        }
        return cartDetails;
    }

    public static String formatRequestFields(Map<CorvusPayRequestFieldType, String> requestData) {
        if(requestData.isEmpty())
            throw new RequestDataEmptyException();

        List<String> formattedRequestFields = new ArrayList<String>();
        for (Map.Entry<CorvusPayRequestFieldType, String> entry : requestData.entrySet()) {
            formattedRequestFields.add(formatField(entry));
        }
        return join(formattedRequestFields, "&");
    }

    private static String formatField(Map.Entry<CorvusPayRequestFieldType, String> entry) {
        return entry.getKey().name().toLowerCase() + "=" + entry.getValue();
    }

    public static String formatAllInstallments(){
        return "Y0299";
    }

    public static String formatOnlyOneTimePayment(){
        return "Y0000";
    }

    public static String formatOnlyOneInstallment(int installment){
        validateInstallment(installment);
        return "N" + formatTwoDigits(installment) + formatTwoDigits(installment);
    }

    public static String formatOneInstallmentsWithOneTimePayment(int installment){
        validateInstallment(installment);
        return "Y" + formatTwoDigits(installment) + formatTwoDigits(installment);
    }

    public static String formatBetweenTwoInstallments(int from, int to){
        validateInstallment(from);
        validateInstallment(to);
        return "N" + formatTwoDigits(from) + formatTwoDigits(to);
    }

    public static String formatBetweenTwoInstallmentsWithOneTimePayment(int from, int to){
        validateInstallment(from);
        validateInstallment(to);
        return "Y" + formatTwoDigits(from) + formatTwoDigits(to);
    }
}
