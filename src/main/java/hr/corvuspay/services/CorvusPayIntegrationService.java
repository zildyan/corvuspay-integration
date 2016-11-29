package hr.corvuspay.services;

import hr.corvuspay.dto.CorvusPayRequestData;
import hr.corvuspay.types.CorvusPayCountryType;
import hr.corvuspay.types.CorvusPayEnvironmentType;
import hr.corvuspay.types.CorvusPayViewResolverType;

public interface CorvusPayIntegrationService {

    CorvusPayEnvironmentType getEnvironment();

    String getCorvusPayUrl();

    String getCorvusPayRedirect();

    String getSecretKey();

    String getStoreId();

    String getCurrency();

    String getLanguage();

    String getCorvusPaySubmitForm(CorvusPayViewResolverType viewResolverType);

    CorvusPayRequestData getRequestData(String orderNumber);

    void validateTransaction(String orderNumber, String sha1HashCode);

    boolean processSubscription(String accountId, String subscriptionExpDate);

    boolean completeApiCall(String orderNumber);

    boolean cancelApiCall(String orderNumber);

    boolean refundApiCall(String orderNumber);

    boolean completeForSubscriptionApiCall(String orderNumber);

    boolean chargeNextSubscriptionPaymentApiCall(String orderNumber);

    boolean partialCompleteApiCall(String orderNumber);

    boolean partialRefundApiCall(String orderNumber);

    boolean checkTransactionStatusApiCall(String orderNumber, String timestamp);
}
