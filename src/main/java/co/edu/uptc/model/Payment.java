package co.edu.uptc.model;

public class Payment {

    private String nameCard;
    private int cardNumber;
    private String securityCode;
    private int expirationYear;
    private String expirationMonth;
    
    public Payment(String nameCard, int cardNumber, String securityCode, int expirationYear,
            String expirationMonth) {
        this.nameCard = nameCard;
        this.cardNumber = cardNumber;
        this.securityCode = securityCode;
        this.expirationYear = expirationYear;
        this.expirationMonth = expirationMonth;
    }
    public String getNameCard() {
        return nameCard;
    }
    public void setNameCard(String nameCard) {
        this.nameCard = nameCard;
    }
    public int getCardNumber() {
        return cardNumber;
    }
    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }
    public String getSecurityCode() {
        return securityCode;
    }
    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }
    public int getExpirationYear() {
        return expirationYear;
    }
    public void setExpirationYear(int expirationYear) {
        this.expirationYear = expirationYear;
    }
    public String getExpirationMonth() {
        return expirationMonth;
    }
    public void setExpirationMonth(String expirationMonth) {
        this.expirationMonth = expirationMonth;
    }
    @Override
    public String toString() {
        return "Payment [nameCard=" + nameCard + ", cardNumber=" + cardNumber + ", securityCode=" + securityCode
                + ", expirationYear=" + expirationYear + ", expirationMonth=" + expirationMonth + "]";
    }

   

}
