package hr.corvuspay.types;

import static hr.corvuspay.types.CorvusPayRequestFieldType.*;

public enum CorvusPayCreditCardType {
    AMEX(PAYMENT_AMEX),
    DINERS(PAYMENT_DINERS),
    VISA(PAYMENT_VISA),
    MASTER(PAYMENT_MASTER),
    MAESTRO(PAYMENT_MAESTRO),
    DISCOVER(PAYMENT_DISCOVER),
    JCB(PAYMENT_JCB);

    private CorvusPayRequestFieldType creditCardRequestField;

    CorvusPayCreditCardType(CorvusPayRequestFieldType creditCardRequestField){
        this.creditCardRequestField = creditCardRequestField;
    }

    public CorvusPayRequestFieldType getCreditCardRequestField() {
        return creditCardRequestField;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
