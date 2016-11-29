package hr.corvuspay.exceptions;

public class CorvusPayRuntimeExceptions {

    public static class CorvusPayEnvironmentUndefinedException extends RuntimeException {}

    public static class Sha1HashCodeInvalidException extends RuntimeException {}

    public static class Sha1CalculatingHashInvalidFieldException extends RuntimeException {}

    public static class MethodNotImplementedException extends RuntimeException {}

    public static class InvalidNumberOfInstallments extends RuntimeException {}

    public static class NecessaryFieldsNotSetException extends RuntimeException {}

    public static class RequestDataEmptyException extends RuntimeException {}

    public static class KeyStoreAndCertificateRequiredDataException extends RuntimeException {
        public KeyStoreAndCertificateRequiredDataException(){
            super("KeyStore name, password or certificate file path not set.");
        }
    }

    public static class InvalidInstallmentException extends RuntimeException {
        public InvalidInstallmentException(){
            super("For one time payment choose corresponding option, 1 installment is forbidden.");
        }
    }

    public static class CorvusPayResponseParseException extends RuntimeException {
        public CorvusPayResponseParseException(Throwable e) {
            super(e);
        }
    }

    public static class CorvusPayInvalidResponse extends RuntimeException {
        public CorvusPayInvalidResponse(String e) {
            super(e);
        }
    }

    public static class ClientCertificateCreationException extends RuntimeException {
        public ClientCertificateCreationException(Throwable e) {
            super(e);
        }
    }

    public static class KeyStoreCreationException extends RuntimeException {
        public KeyStoreCreationException(Throwable e) {
            super(e);
        }
    }

    public static class TrustManagerFactoryCreationException extends RuntimeException {
        public TrustManagerFactoryCreationException(Throwable e) {
            super(e);
        }
    }

    public static class SslContextCreationException extends RuntimeException {
        public SslContextCreationException(Throwable e) {
            super(e);
        }
    }

    public static class HttpRespondStatusCodeNotOkException extends RuntimeException {
        public HttpRespondStatusCodeNotOkException(String e){
            super(e);
        }
    }

    public static class CorvusPayHttpsPostException extends RuntimeException{
        public CorvusPayHttpsPostException(Throwable e){
            super(e);
        }
    }

    public static class Sha1NoAlgorithmException extends RuntimeException {
        public Sha1NoAlgorithmException(Throwable e){ super(e);}
    }
}
