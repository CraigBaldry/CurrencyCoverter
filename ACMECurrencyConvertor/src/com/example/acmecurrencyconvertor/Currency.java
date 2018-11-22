package com.example.acmecurrencyconvertor;

import java.io.Serializable;

public class Currency implements Serializable {
	private String dateSaved;
	private String dateUpdate;
	private String AUD;
	private String GBP;
	private String JPY;
	private String USD;
	private String INR;
	private String CAD;
	private String HKD;
	private String BRL;
	private String NZD;
	private String EUR;
	
	public Currency() {
		dateUpdate = "No updated date";
		dateSaved = "No saved Date";
		AUD = "1";
		GBP = "1";
		JPY = "1";
		USD = "1";
		INR = "1";
		CAD = "1";
		HKD = "1";
		BRL = "1";
		NZD = "1";
		EUR = "1";
	}

	public String getEUR() {
		return EUR;
	}

	public void setEUR(String eur) {
		EUR = eur;
	}

	public String getGBP() {
		return GBP;
	}

	public void setGBP(String gbp) {
		GBP = gbp;
	}

	public String getCAD() {
		return CAD;
	}

	public void setCAD(String cad) {
		CAD = cad;
	}

	public String getAUD() {
		return AUD;
	}

	public void setAUD(String aud) {
		AUD = aud;
	}

	public String getJPY() {
		return JPY;
	}

	public void setJPY(String jpy) {
		JPY = jpy;
	}

	public String getUSD() {
		return USD;
	}

	public void setUSD(String usd) {
		USD = usd;
	}

	public String getINR() {
		return INR;
	}

	public void setINR(String inr) {
		INR = inr;
	}

	public String getHKD() {
		return HKD;
	}

	public void setHKD(String hkd) {
		HKD = hkd;
	}

	public String getBRL() {
		return BRL;
	}

	public void setBRL(String brl) {
		BRL = brl;
	}

	public String getNZD() {
		return NZD;
	}

	public void setNZD(String nzd) {
		NZD = nzd;
	}

	public String getDate() {
		return dateUpdate;
	}

	public void setDate(String date) {
		this.dateUpdate = date;
	}

	public String getDateSaved() {
		return dateSaved;
	}

	public void setDateSaved(String dateSaved) {
		this.dateSaved = dateSaved;
	}
}

