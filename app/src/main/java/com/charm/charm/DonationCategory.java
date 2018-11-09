package com.charm.charm;

public class DonationCategory {

    private String donation_name;
    private int donation_amount;
    private String unit;
    private String description;

    public DonationCategory( String donation_name, int donation_amount, String unit, String description ) {
        this.donation_name = donation_name;
        this.donation_amount = donation_amount;
        this.unit = unit;
        this.description = description;
    }


    public DonationCategory( String donation_name, int donation_amount, String unit ) {
        this.donation_name = donation_name;
        this.donation_amount = donation_amount;
        this.unit = unit;
        this.description = "";
    }


    public int getDonation_amount() {
        return donation_amount;
    }

    public String getDonation_name() {
        return donation_name;
    }

    public String get_unit() {
        return unit;
    }

    public String get_description() {
        return description;
    }
}
