package hr.corvuspay.types;

public enum CorvusPayResponseCodeType {

    CODE_0("0", "Approved/Accepted"),
    CODE_2("2", "Buyer needs to call his bank, the telephone number on the back of his payment card"),
    CODE_100("100", "Declined"),
    CODE_199("199", "Invalid Service Establishment"),
    CODE_400("400", "Refund Accepted"),
    CODE_909("909", "Technical Error / Unable to Process Request"),
    CODE_912("912", "Host Link Down"),
    CODE_930("930", "Transaction Not Found"),
    CODE_931("931", "Card Expired"),
    CODE_1001("1001", "Card Expired"),
    CODE_1002("1002", "Card Suspicious"),
    CODE_1003("1003", "Card Suspended"),
    CODE_1004("1004", "Card Stolen"),
    CODE_1005("1005", "Card Lost"),
    CODE_1011("1011", "Card Not Found"),
    CODE_1012("1012", "Cardholder Not Found"),
    CODE_1014("1014", "Account Not Found"),
    CODE_1015("1015", "Invalid Request"),
    CODE_1016("1016", "Insufficient Funds"),
    CODE_1017("1017", "Previously ReverseD"),
    CODE_1018("1018", "Previously Reversed"),
    CODE_1019("1019", "Further Activity Prevents Reversal"),
    CODE_1020("1020", "Further Activity Prevents Void"),
    CODE_1021("1021", "Original Transaction Has Been Voided"),
    CODE_1022("1022", "Card Does Not Support Preauthorizations"),
    CODE_1023("1023", "Only the Fully Authenticated 3D-Secure Transactions Are Allowed With Empty CVV"),
    CODE_1024("1024", "Installments Are Not Allowed For This Card"),
    CODE_1025("1025", "Transaction With Installments Cannot Be Sent As Preauthorization"),
    CODE_1026("1026", "Installments Are Not Allowed For Non ZABA Cards"),
    CODE_1050("1050", "Transaction Declined"),
    CODE_1051("1051", "Payment number is out of sequence"),
    CODE_1802("1802", "Missing Fields"),
    CODE_1803("1803", "Extra Fields Exist"),
    CODE_1804("1804", "Invalid Card Number"),
    CODE_1806("1806", "Card Not Active"),
    CODE_1808("1808", "Card Not Configured"),
    CODE_1810("1810", "Invalid Amount"),
    CODE_1811("1811", "System Error - Database"),
    CODE_1812("1812", "System Error - Transaction"),
    CODE_1813("1813", "Cardholder Not Active"),
    CODE_1814("1814", "Cardholder Not Configured"),
    CODE_1815("1815", "Cardholder Expired"),
    CODE_1816("1816", "Original Not Found"),
    CODE_1817("1817", "Usage Limit Reached"),
    CODE_1818("1818", "Configuration Error"),
    CODE_1819("1819", "Invalid Terminal"),
    CODE_1820("1820", "Inactive Terminal"),
    CODE_1821("1821", "Invalid Merchant"),
    CODE_1822("1822", "Duplicate Entity"),
    CODE_1823("1823", "Invalid Acquirer"),
    CODE_2000("2000", "Invalid Expiration Date"),
    CODE_2010("2010", "Failed 3D secure authentication"),
    CODE_5001("5001", "RISK : Number of Repeats per PAN"),
    CODE_5003("5003", "RISK : Number of Repeats per BIN"),
    CODE_5005("5005", "RISK : Percentage of Declined Transactions"),
    CODE_5006("5006", "RISK : Number of Refunded Transactions"),
    CODE_5007("5007", "RISK : Percentage Increment of Sum on Amount of Refunded Transactions"),
    CODE_5009("5009", "RISK : Number of Chargebacks"),
    CODE_5010("5010", "RISK : Sum on Amount of Chargebacks"),
    CODE_5011("5011", "RISK : Number of Retrieval Requests"),
    CODE_5012("5012", "RISK : Sum on Amount of Retrieval Requests"),
    CODE_5013("5013", "RISK : Average Amount per Transaction"),
    CODE_5014("5014", "RISK : Percentage Increment of Average Amount per Transaction"),
    CODE_5015("5015", "RISK : Percentage Increment of Number of Transactions"),
    CODE_5016("5016", "RISK : Total Sum on Amount"),
    CODE_5017("5017", "RISK : Percentage Increment of Total Sum on Amount"),
    CODE_5018("5018", "RISK : Minimum Amount per Transaction"),
    CODE_5019("5019", "RISK : Maximum Amount per Transaction"),
    CODE_5020("5020", "RISK : Number of Approved Transactions per PAN"),
    CODE_5021("5021", "RISK : Sum on Amount of Approved Transactions per PAN"),
    CODE_5022("5022", "RISK : Sum on Amount of Approved Transactions per BIN"),
    CODE_5023("5023", "RISK : Number of Approved Transactions per PAN and MCC on Amount"),
    CODE_5050("5050", "RISK : Number of Repeats per IP"),
    CODE_5051("5051", "RISK : Number of Repeats per Cardholder name"),
    CODE_5052("5052", "RISK : Number of Repeats per Cardholder e-mail");

    private String code;
    private String description;

    CorvusPayResponseCodeType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String toString() {
        return getDescription();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
