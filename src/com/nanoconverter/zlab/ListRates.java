package com.nanoconverter.zlab;

public class ListRates {
	public int flag;
    public String ratesID;
    public String ratesDISC;
    public String ratesVALUE;
    public ListRates(){
        super();
    }

    public ListRates(int flag, String ratesID, String ratesDISC, String ratesVALUE) {
        super();
        this.flag = flag;
        this.ratesID = ratesID;
        this.ratesDISC = ratesDISC;
        this.ratesVALUE = ratesVALUE;
    }
}