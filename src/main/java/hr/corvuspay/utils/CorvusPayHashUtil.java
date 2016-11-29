package hr.corvuspay.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static hr.corvuspay.exceptions.CorvusPayRuntimeExceptions.Sha1CalculatingHashInvalidFieldException;
import static hr.corvuspay.exceptions.CorvusPayRuntimeExceptions.Sha1NoAlgorithmException;
import static hr.corvuspay.utils.CorvusPayFormatUtil.formatByteToHex;
import static hr.corvuspay.utils.CorvusPayMiscellaneousUtil.isValidData;
import static hr.corvuspay.utils.CorvusPayMiscellaneousUtil.join;
import static java.util.Arrays.asList;

public class CorvusPayHashUtil {

    public static String calculateDefaultRedirectHash(String secretKey, String orderNumber, String amount, String currency) {
        return calculateSha1WithDelimiter(secretKey, orderNumber, amount, currency);
    }

    public static String calculateBestBeforeRedirectHash(String secretKey, String orderNumber, String amount, String currency, String bestBefore) {
        return calculateSha1WithDelimiter(secretKey, orderNumber, amount, currency, bestBefore);
    }

    public static String calculateDiscountRedirectHash(String secretKey, String orderNumber, String amount, String discountAmount, String currency) {
        return calculateSha1WithDelimiter(secretKey, orderNumber, amount, discountAmount, currency);
    }

    public static String calculateProfileRedirectHash(String secretKey, String orderNumber, String amount, String currency, String userCardProfilesId) {
        return calculateSha1WithDelimiter(secretKey, orderNumber, amount, currency, userCardProfilesId);
    }

    public static String calculateSha1WithDelimiter(String... fields) {
        return calculateSha1Internal(true, fields);
    }

    public static String calculateSha1(String... fields) {
        return calculateSha1Internal(false, fields);
    }

    private static String calculateSha1Internal(boolean useColon, String... fields) {
        if (fields == null || !isValidData(fields))
            throw new Sha1CalculatingHashInvalidFieldException();

        String hashMessage = getHashMessage(useColon, fields);
        byte[] sha1Bytes = getSha1Bytes(hashMessage);
        return sha1BytesToHex(sha1Bytes);
    }

    private static String getHashMessage(boolean useColon, String[] fields) {
        return join(asList(fields), useColon ? ":" : "");
    }

    private static byte[] getSha1Bytes(String hashMessage){
        try {
            return MessageDigest.getInstance("SHA1").digest(hashMessage.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new Sha1NoAlgorithmException(e);
        }
    }

    private static String sha1BytesToHex(byte[] byteArray) {
        StringBuilder sha1Hex = new StringBuilder();
        for (byte b : byteArray)
            sha1Hex.append(formatByteToHex(b));
        return sha1Hex.toString();
    }
}
