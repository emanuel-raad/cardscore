package com.example.emanuel.cardscore;

/**
 * Created by emanuel on 8/3/15.
 */
public class Player {

    private String mName;
    private int mScore;
    private int mRealBid;
    private int mLastScore;
    private int mIndex;
    private boolean mWinner;
    private boolean mLoser;
    private boolean mHmar;
    private boolean mDealer;

    public Player() {
    }

    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        mIndex = index;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getScore() {
        return mScore;
    }

    public void setScore(int score) {
        mScore = score;
    }

    public int getRealBid() {
        return mRealBid;
    }

    public void setRealBid(int realBid) {
        mRealBid = realBid;
    }

    public boolean isWinner() {
        return mWinner;
    }

    public void setWinner(boolean winner) {
        mWinner = winner;
    }

    public boolean isLoser() {
        return mLoser;
    }

    public void setLoser(boolean loser) {
        mLoser = loser;
    }

    public boolean isHmar() {
        return mHmar;
    }

    public void setHmar(boolean hmar) {
        mHmar = hmar;
    }

    public int getLastScore() {
        return mLastScore;
    }

    public void setLastScore(int lastScore) {
        mLastScore = lastScore;
    }

    public boolean isDealer() {
        return mDealer;
    }

    public void setDealer(boolean dealer) {
        mDealer = dealer;
    }
}
