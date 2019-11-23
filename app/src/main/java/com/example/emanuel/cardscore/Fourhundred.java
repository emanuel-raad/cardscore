package com.example.emanuel.cardscore;

import android.content.Context;

/**
 * Created by emanuel on 8/7/15.
 */
public class Fourhundred {

    private PlayerLab mPlayerLab;
    private String mGameType;
    private String mWinnerName;
    private String mPartnerName;

    public void Fourhundred(Context context){
        mPlayerLab = PlayerLab.get(context);
        mGameType = "Fourhundred";
    }

    public String getWinnerName() {
        return mWinnerName;
    }

    public void setWinnerName(String winnerName) {
        mWinnerName = winnerName;
    }

    public String getPartnerName() {
        return mPartnerName;
    }

    public void setPartnerName(String partnerName) {
        mPartnerName = partnerName;
    }
}
