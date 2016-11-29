package hr.corvuspay.types;

public enum CorvusPayViewResolverType {
    JSP("payment/corvusPayJspSubmitForm"),
    THYMELEAF("payment/corvusPayThymeleafSubmitForm");

    private String template;

    CorvusPayViewResolverType(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }

}
