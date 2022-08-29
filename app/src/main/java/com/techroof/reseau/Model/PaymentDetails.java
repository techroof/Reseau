package com.techroof.reseau.Model;

public class PaymentDetails {

    String paymentMethodName,paymentMethodTitle,cardNum,cardHolderName,cvv,expiryDate;

    public PaymentDetails() {
    }

    public PaymentDetails(String paymentMethodName, String paymentMethodTitle, String cardNum, String cardHolderName, String cvv, String expiryDate) {
        this.paymentMethodName = paymentMethodName;
        this.paymentMethodTitle = paymentMethodTitle;
        this.cardNum = cardNum;
        this.cardHolderName = cardHolderName;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
    }

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    public String getPaymentMethodTitle() {
        return paymentMethodTitle;
    }

    public void setPaymentMethodTitle(String paymentMethodTitle) {
        this.paymentMethodTitle = paymentMethodTitle;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
