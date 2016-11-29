package hr.corvuspay.dto;

import hr.corvuspay.types.CorvusPayCountryType;
import hr.corvuspay.types.CorvusPayCreditCardType;
import hr.corvuspay.types.CorvusPayRequestFieldType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static hr.corvuspay.exceptions.CorvusPayRuntimeExceptions.Sha1HashCodeInvalidException;
import static hr.corvuspay.types.CorvusPayRequestFieldType.*;
import static hr.corvuspay.utils.CorvusPayFormatUtil.*;
import static hr.corvuspay.utils.CorvusPayHashUtil.*;


public class CorvusPayRequestData {

    private Map<CorvusPayRequestFieldType, String> requestFields;

    public void setRequestFields(Map<CorvusPayRequestFieldType, String> requestFields) {
        this.requestFields = requestFields;
    }

    public Map<String, String> getRequestFields() {
        Map<String, String> formattedRequestFields = new LinkedHashMap<String, String>();
        for (Map.Entry<CorvusPayRequestFieldType, String> entry : this.requestFields.entrySet())
            formattedRequestFields.put(entry.getKey().name().toLowerCase(), entry.getValue());

        return formattedRequestFields;
    }

    public void putRequestField(CorvusPayRequestFieldType fieldType, String fieldValue) {
        this.requestFields.put(fieldType, fieldValue);
    }

    private CorvusPayRequestData(Builder builder) {
        setRequestFields(builder.requestFields);
    }

    public static final class Builder {
        private String secretKey;
        private Map<CorvusPayRequestFieldType, String> requestFields;

        public Builder(final String orderNumber, final String secretKey, final String storeId, final String language,
                       final String currencyIsoCode, final String amount) {

            this.secretKey = secretKey;
            requestFields = new LinkedHashMap<CorvusPayRequestFieldType, String>() {{
                put(TARGET, "_top");
                put(MODE, "form");
                put(ORDER_NUMBER, orderNumber);
                put(STORE_ID, storeId);
                put(LANGUAGE, language);
                put(CURRENCY, currencyIsoCode);
                put(AMOUNT, formatDecimalNumber(amount));
                put(REQUIRE_COMPLETE, Boolean.TRUE.toString());
            }};
        }

        public Builder cart(String cart) {
            requestFields.put(CART, cart);
            return this;
        }

        public Builder newAmount(String newAmount) {
            requestFields.put(NEW_AMOUNT, newAmount);
            return this;
        }

        public Builder subscription(boolean subscription) {
            requestFields.put(SUBSCRIPTION, Boolean.toString(subscription).toLowerCase());
            return this;
        }

        public Builder paymentNumber(String paymentNumber) {
            requestFields.put(PAYMENT_NUMBER, paymentNumber);
            return this;
        }

        public Builder rewriteUrl(String rewriteUrl) {
            requestFields.put(REWRITE_URL, rewriteUrl);
            return this;
        }

        public Builder cardholderName(String cardholderName) {
            requestFields.put(CARDHOLDER_NAME, cardholderName);
            return this;
        }

        public Builder cardholderSurname(String cardholderSurname) {
            requestFields.put(CARDHOLDER_SURNAME, cardholderSurname);
            return this;
        }

        public Builder cardholderCity(String cardholderCity) {
            requestFields.put(CARDHOLDER_CITY, cardholderCity);
            return this;
        }

        public Builder cardholderAddress(String cardholderAddress) {
            requestFields.put(CARDHOLDER_ADDRESS, cardholderAddress);
            return this;
        }

        public Builder cardholderZipCode(String cardholderZipCode) {
            requestFields.put(CARDHOLDER_ZIP_CODE, cardholderZipCode);
            return this;
        }

        public Builder cardholderPhone(String cardholderPhone) {
            requestFields.put(CARDHOLDER_PHONE, cardholderPhone);
            return this;
        }

        public Builder cardholderEmail(String cardholderEmail) {
            requestFields.put(CARDHOLDER_EMAIL, cardholderEmail);
            return this;
        }

        public Builder cardholderCountry(CorvusPayCountryType cardholderCountry) {
            requestFields.put(CARDHOLDER_COUNTRY, cardholderCountry.getAcronym());
            return this;
        }

        public Builder preselectedCard(CorvusPayCreditCardType preselectedCard) {
            requestFields.put(CC_TYPE, preselectedCard.name().toLowerCase());
            return this;
        }

        public Builder preselectedCardholderCountry(CorvusPayCountryType country) {
            requestFields.put(CARDHOLDER_COUNTRY_CODE, country.getAcronym());
            return this;
        }

        public Builder initializeSubscription() {
            requestFields.put(SUBSCRIPTION, Boolean.TRUE.toString());
            requestFields.put(PAYMENT_NUMBER, "1");
            return this;
        }

        public Builder initializeUserCardProfile(String userCardProfilesId) {
            requestFields.put(USER_CARD_PROFILES, Boolean.TRUE.toString());
            requestFields.put(USER_CARD_PROFILES_ID, userCardProfilesId);
            return this;
        }

        public Builder requireCompleteOff() {
            requestFields.put(REQUIRE_COMPLETE, Boolean.FALSE.toString());
            return this;
        }

        public Builder defaultRedirectHash() {
            requestFields.put(HASH, calculateDefaultRedirectHash(
                    this.secretKey,
                    requestFields.get(ORDER_NUMBER),
                    requestFields.get(AMOUNT),
                    requestFields.get(CURRENCY)
            ));
            return this;
        }

        public Builder redirectHashWithDiscount() {
            requestFields.put(HASH, calculateDiscountRedirectHash(
                    this.secretKey,
                    requestFields.get(ORDER_NUMBER),
                    requestFields.get(AMOUNT),
                    requestFields.get(DISCOUNT_AMOUNT),
                    requestFields.get(CURRENCY)
            ));
            return this;
        }

        public Builder redirectHashWithBestBefore() {
            requestFields.put(HASH, calculateBestBeforeRedirectHash(
                    this.secretKey,
                    requestFields.get(ORDER_NUMBER),
                    requestFields.get(AMOUNT),
                    requestFields.get(CURRENCY),
                    requestFields.get(BEST_BEFORE)
            ));
            return this;
        }

        public Builder redirectHashWithUserProfileId() {
            requestFields.put(HASH, calculateProfileRedirectHash(
                    this.secretKey,
                    requestFields.get(ORDER_NUMBER),
                    requestFields.get(AMOUNT),
                    requestFields.get(CURRENCY),
                    requestFields.get(USER_CARD_PROFILES_ID)
            ));
            return this;
        }

        public Builder fixedInstallment(int installment) {
            requestFields.put(NUMBER_OF_INSTALLMENTS, formatTwoDigits(installment));
            return this;
        }

        public Builder allCardsAllInstallments() {
            requestFields.put(PAYMENT_ALL, formatAllInstallments());
            return this;
        }

        public Builder allCardsOnlyOneTimePayment() {
            requestFields.put(PAYMENT_ALL, formatOnlyOneTimePayment());
            return this;
        }

        public Builder allCardsOnlyOneInstallment(int installment) {
            requestFields.put(PAYMENT_ALL, formatOnlyOneInstallment(installment));
            return this;
        }

        public Builder allCardsOneInstallmentWithOneTimePayment(int installment) {
            requestFields.put(PAYMENT_ALL, formatOneInstallmentsWithOneTimePayment(installment));
            return this;
        }

        public Builder allCardsBetweenTwoInstallments(int from, int to) {
            requestFields.put(PAYMENT_ALL, formatBetweenTwoInstallments(from, to));
            return this;
        }

        public Builder chosenCardsAllInstallments(List<CorvusPayCreditCardType> creditCards) {
            setChosenInstallmentsToCardsInternal(creditCards, formatAllInstallments());
            return this;
        }

        public Builder chosenCardsBetweenTwoInstallments(List<CorvusPayCreditCardType> creditCards, int from, int to) {
            setChosenInstallmentsToCardsInternal(creditCards, formatBetweenTwoInstallments(from, to));
            return this;
        }

        public Builder chosenCardsBetweenTwoInstallmentsWithOneTimePayment(List<CorvusPayCreditCardType> creditCards, int from, int to) {
            setChosenInstallmentsToCardsInternal(creditCards, formatBetweenTwoInstallmentsWithOneTimePayment(from, to));
            return this;
        }

        public Builder chosenCardsOnlyOneTimePayment(List<CorvusPayCreditCardType> creditCards) {
            setChosenInstallmentsToCardsInternal(creditCards, formatOnlyOneTimePayment());
            return this;
        }

        public Builder chosenCardsOnlyOneInstallment(List<CorvusPayCreditCardType> creditCards, int installment) {
            setChosenInstallmentsToCardsInternal(creditCards, formatOnlyOneInstallment(installment));
            return this;
        }

        public Builder chosenCardsOneInstallmentWithOneTimePayment(List<CorvusPayCreditCardType> creditCards, int installment) {
            setChosenInstallmentsToCardsInternal(creditCards, formatOneInstallmentsWithOneTimePayment(installment));
            return this;
        }

        private void setChosenInstallmentsToCardsInternal(List<CorvusPayCreditCardType> creditCards, String chosenInstallmentFormat) {
            requestFields.put(CorvusPayRequestFieldType.PAYMENT_ALL_DYNAMIC, Boolean.TRUE.toString().toLowerCase());
            for (CorvusPayCreditCardType creditCard : creditCards) {
                requestFields.put(creditCard.getCreditCardRequestField(), chosenInstallmentFormat);
            }
        }

        public CorvusPayRequestData build() {
            if(!requestFields.containsKey(HASH))
                throw new Sha1HashCodeInvalidException();

            return new CorvusPayRequestData(this);
        }
    }
}
