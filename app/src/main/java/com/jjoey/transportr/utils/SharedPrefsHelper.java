package com.jjoey.transportr.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsHelper {

    public Context context;
    public SharedPreferences logOutPrefs, accountPrefs, cardPaymentPrefs, cashModePrefs;
    public SharedPreferences.Editor editor, accountEditor, cardEditor, cashModeEditor;

    public static final String KEY_LOG_OUT_PREFS = "hasLoggedOut";
    public static final String LOG_OUT = "logOut";

    public static final String KEY_ACCOUNT_PREFS = "hasAccount";
    public static final String HAS_ACCOUNT = "account";

    public static String CARD_PREFS = "cardPayment";
    public static final String KEY_CARD_HOLDER_NAME = "card_holder_name";
    public static final String KEY_CARD_NUMBER = "card_number";
    public static final String KEY_CARD_EXPIRY_YEAR = "card_expiry_year";
    public static final String KEY_CARD_EXPIRY_MONTH = "card_expiry_month";
    public static final String KEY_CARD_CVV_NUM = "card_cvv_num";

    public static String KEY_CASH_MODE = "cash_payment";
    public static String CASH_MODE_VALUE = "cash_mode" ;

    public SharedPrefsHelper(Context context) {
        this.context = context;

        logOutPrefs = context.getSharedPreferences(KEY_LOG_OUT_PREFS, Context.MODE_PRIVATE);
        editor = logOutPrefs.edit();

        accountPrefs = context.getSharedPreferences(KEY_ACCOUNT_PREFS, Context.MODE_PRIVATE);
        accountEditor = accountPrefs.edit();

        cardPaymentPrefs = context.getSharedPreferences(CARD_PREFS, Context.MODE_PRIVATE);
        cardEditor = cardPaymentPrefs.edit();

        cashModePrefs = context.getSharedPreferences(KEY_CASH_MODE, Context.MODE_PRIVATE);
        cashModeEditor = cashModePrefs.edit();

    }

    public void setHasAccount(boolean hasAccount) {
        accountEditor.putBoolean(HAS_ACCOUNT, hasAccount);
        accountEditor.commit();
    }

    public boolean getHasAccount() {
        return accountPrefs.getBoolean(HAS_ACCOUNT, false);
    }

    public void setLoggedOut(boolean value) {
        editor.putBoolean(LOG_OUT, value);
        editor.commit();
    }

    public boolean getLoggedOut(){
        return logOutPrefs.getBoolean(LOG_OUT, false);
    }

    public void saveCashMode(boolean isCashMode){
        cashModeEditor.putBoolean(CASH_MODE_VALUE, isCashMode);
        cashModeEditor.commit();
    }

    public boolean getCashMode(){
        return cashModePrefs.getBoolean(CASH_MODE_VALUE, false);
    }

    // TODO: 5/30/2018 Save Card and PayPal Modes

    public void saveCardDetails(String name, String number, String expiryYear, String expiryMonth, String cvv) {
        cardEditor.putString(KEY_CARD_HOLDER_NAME, name);
        cardEditor.putString(KEY_CARD_NUMBER, number);
        cardEditor.putString(KEY_CARD_EXPIRY_YEAR, expiryYear);
        cardEditor.putString(KEY_CARD_EXPIRY_MONTH, expiryMonth);
        cardEditor.putString(KEY_CARD_CVV_NUM, cvv);
        cardEditor.commit();
    }

    public String getCardHolderName(){
        return cardPaymentPrefs.getString(KEY_CARD_HOLDER_NAME, null);
    }

    public String getCardNumber() {
        return cardPaymentPrefs.getString(KEY_CARD_NUMBER, null);
    }

    public String getCardExpiryYear() {
        return cardPaymentPrefs.getString(KEY_CARD_EXPIRY_YEAR, null);
    }

    public String getCardExpiryMonth() {
        return cardPaymentPrefs.getString(KEY_CARD_EXPIRY_MONTH, null);
    }

    public String getCardCVVNumber(){
        return cardPaymentPrefs.getString(KEY_CARD_CVV_NUM, null);
    }

}
