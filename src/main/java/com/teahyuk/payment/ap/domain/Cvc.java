package com.teahyuk.payment.ap.domain;

public class Cvc {
    private final static String INVALID_FORMAT = "Create Cvc error, cvc must be 3 digits number, cvc=%s";
    private final String cvc;

    public Cvc(String cvc) {
        this.cvc = cvc;
        checkValidation();
    }

    private void checkValidation() {
        if(isInt() && is3Digits()){
            throw new IllegalArgumentException(String.format(INVALID_FORMAT, cvc));
        }
    }

    private boolean is3Digits() {
        return cvc.length()!=3;
    }

    private boolean isInt(){
        try {
            Integer.parseInt(cvc);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

}
