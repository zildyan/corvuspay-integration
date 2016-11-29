package hr.corvuspay.services.impl;

import hr.corvuspay.services.CorvusPayHttpsPostService;
import hr.corvuspay.services.CorvusPayIntegrationService;
import hr.corvuspay.types.CorvusPayRequestFieldType;
import hr.corvuspay.types.CorvusPayResponseType;
import hr.corvuspay.types.CorvusPayViewResolverType;

import java.util.LinkedHashMap;
import java.util.Map;

import static hr.corvuspay.configuration.CorvusPayConfiguration.*;
import static hr.corvuspay.exceptions.CorvusPayRuntimeExceptions.*;
import static hr.corvuspay.types.CorvusPayRequestFieldType.*;
import static hr.corvuspay.utils.CorvusPayFormatUtil.formatDecimalNumber;
import static hr.corvuspay.utils.CorvusPayHashUtil.calculateSha1;
import static hr.corvuspay.utils.CorvusPayMiscellaneousUtil.isValidData;

public abstract class AbstractCorvusPayIntegrationService implements CorvusPayIntegrationService {

    protected CorvusPayHttpsPostService getCorvusPayHttpsPostService(){
        return new CorvusPayHttpsPostServiceImpl();
    }

    @Override
    public String getCorvusPayUrl() {
        switch (getEnvironment()) {
            case PRODUCTION:
                return PRODUCTION_ENVIRONMENT_URL;
            case TEST:
                return TEST_ENVIRONMENT_URL;
            default:
                throw new CorvusPayEnvironmentUndefinedException();
        }
    }

    @Override
    public String getCorvusPayRedirect() {
        return getCorvusPayUrl() + REDIRECT;
    }

    @Override
    public String getCorvusPaySubmitForm(CorvusPayViewResolverType viewResolverType) {
        return viewResolverType.getTemplate();
    }

    @Override
    public void validateTransaction(String orderNumber, String sha1HashCode) {
        if(!sha1HashCode.equals(calculateSha1(getSecretKey(), orderNumber)))
            throw new Sha1HashCodeInvalidException();
    }

    @Override
    public boolean processSubscription(String accountId, String subscriptionExpDate) {
        throw new MethodNotImplementedException();
    }

    @Override
    public boolean completeApiCall(String orderNumber) {
        throw new MethodNotImplementedException();
    }

    @Override
    public boolean cancelApiCall(String orderNumber) {
        throw new MethodNotImplementedException();
    }

    @Override
    public boolean refundApiCall(String orderNumber) {
        throw new MethodNotImplementedException();
    }

    @Override
    public boolean completeForSubscriptionApiCall(String orderNumber) {
        throw new MethodNotImplementedException();
    }

    @Override
    public boolean chargeNextSubscriptionPaymentApiCall(String orderNumber) {
        throw new MethodNotImplementedException();
    }

    @Override
    public boolean partialCompleteApiCall(String orderNumber) {
        throw new MethodNotImplementedException();
    }

    @Override
    public boolean partialRefundApiCall(String orderNumber) {
        throw new MethodNotImplementedException();
    }

    @Override
    public boolean checkTransactionStatusApiCall(String orderNumber, String timestamp) {
        throw new MethodNotImplementedException();
    }

    protected Map<CorvusPayResponseType, String> completeApiCallInternal(String orderNumber) {
        return executeHttpsPost(COMPLETE_MAPPING, getBasicRequestFields(orderNumber));
    }

    protected Map<CorvusPayResponseType, String> cancelApiCallInternal(String orderNumber) {
        return executeHttpsPost(CANCEL_MAPPING, getBasicRequestFields(orderNumber));
    }

    protected Map<CorvusPayResponseType, String> refundApiCallInternal(String orderNumber) {
        return executeHttpsPost(REFUND_MAPPING, getBasicRequestFields(orderNumber));
    }

    protected Map<CorvusPayResponseType, String> partialCompleteApiCallInternal(String orderNumber, String newAmount) {
        validateNecessaryFields(newAmount);
        Map<CorvusPayRequestFieldType, String> requestFields = getBasicRequestFields(orderNumber);
        requestFields.put(NEW_AMOUNT, formatDecimalNumber(newAmount));
        return executeHttpsPost(PARTIAL_COMPLETE_MAPPING, requestFields);
    }

    protected Map<CorvusPayResponseType, String> partialRefundApiCallInternal(String orderNumber, String newAmount) {
        validateNecessaryFields(newAmount);
        Map<CorvusPayRequestFieldType, String> requestFields = getBasicRequestFields(orderNumber);
        requestFields.put(NEW_AMOUNT, formatDecimalNumber(newAmount));
        return executeHttpsPost(PARTIAL_REFUND_MAPPING, requestFields);
    }

    protected Map<CorvusPayResponseType, String> checkTransactionStatusApiCallInternal(final String orderNumber, final String timestamp) {
        validateNecessaryFields(getStoreId(), orderNumber, getStoreId(), getCurrency(), timestamp);
        final String hash = calculateSha1(getSecretKey(), orderNumber, getStoreId(), getCurrency(), timestamp);

        Map<CorvusPayRequestFieldType, String> requestFields = new LinkedHashMap<CorvusPayRequestFieldType, String>(){{
            put(STORE_ID, getStoreId());
            put(ORDER_NUMBER, orderNumber);
            put(HASH, hash);
            put(CURRENCY, getCurrency());
            put(TIMESTAMP, timestamp);
        }};

        return executeHttpsPost(CHECK_STATUS_MAPPING, requestFields);
    }

    protected Map<CorvusPayResponseType, String> completeForSubscriptionApiCallInternal(String orderNumber, String subscription, String paymentNumber, String accountId) {
        return executeHttpsPost(COMPLETE_MAPPING, getSubscriptionRequestFields(orderNumber, subscription, paymentNumber, accountId));
    }

    protected Map<CorvusPayResponseType, String> chargeNextSubscriptionPaymentApiCallInternal(String orderNumber, String subscription, String paymentNumber, String accountId) {
        return executeHttpsPost(NEXT_SUBSCRIPTION_PAYMENT_MAPPING, getSubscriptionRequestFields(orderNumber, subscription, paymentNumber, accountId));
    }

    protected Map<CorvusPayResponseType, String> chargeNextSubscriptionPaymentDifferentAmountApiCallInternal(String orderNumber, String subscription,
                                                                                              String paymentNumber, String accountId, String newAmount) {
        validateNecessaryFields(newAmount);
        Map<CorvusPayRequestFieldType, String> requestFields = getSubscriptionRequestFields(orderNumber, subscription, paymentNumber, accountId);
        requestFields.put(NEW_AMOUNT, formatDecimalNumber(newAmount));
        return executeHttpsPost(NEXT_SUBSCRIPTION_PAYMENT_MAPPING, requestFields);
    }

    private Map<CorvusPayRequestFieldType, String> getSubscriptionRequestFields(String orderNumber, String subscription, String paymentNumber, String accountId) {
        validateNecessaryFields(subscription, paymentNumber, accountId);
        Map<CorvusPayRequestFieldType, String> requestFields = getBasicRequestFields(orderNumber);
        requestFields.put(SUBSCRIPTION, subscription);
        requestFields.put(PAYMENT_NUMBER, paymentNumber);
        requestFields.put(ACCOUNT_ID, accountId);
        return requestFields;
    }

    private Map<CorvusPayRequestFieldType, String> getBasicRequestFields(final String orderNumber) {
        validateNecessaryFields(getStoreId(), orderNumber, getStoreId());
        final String hash = calculateSha1(getSecretKey(), orderNumber, getStoreId());

        return new LinkedHashMap<CorvusPayRequestFieldType, String>(){{
            put(STORE_ID, getStoreId());
            put(ORDER_NUMBER, orderNumber);
            put(HASH, hash);
        }};
    }

    protected void validateNecessaryFields(String... fields){
        if(!isValidData(fields))
            throw new NecessaryFieldsNotSetException();
    }

    private Map<CorvusPayResponseType, String> executeHttpsPost(String requestMapping, Map<CorvusPayRequestFieldType, String> requestFields){
        return getCorvusPayHttpsPostService().executeHttpsPost(getCorvusPayUrl() + requestMapping, requestFields);
    }
}
