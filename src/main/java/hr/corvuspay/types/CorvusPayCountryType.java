package hr.corvuspay.types;

public enum CorvusPayCountryType {
    BIH("BIH", "BAM", "ba"), CROATIA("HR", "HRK", "hr"), SERBIA("RS", "RSD", "rs");

    private String acronym;
    private String currency;
    private String language;

    CorvusPayCountryType(String acronym, String currency, String language) {
        this.acronym = acronym;
        this.currency = currency;
        this.language = language;
    }

    public String getAcronym() {
        return acronym;
    }

    public String getCurrency(){
        return this.currency;
    }

    public String getLanguage() {
        return language;
    }
}
