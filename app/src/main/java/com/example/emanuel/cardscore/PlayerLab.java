package com.example.emanuel.cardscore;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by emanuel on 8/3/15.
 */
public class PlayerLab {
    private static PlayerLab sPlayerLab;
    private Context mAppContext;
    private String mGameType;

    private ArrayList<Player> mPlayers;

    /**
        Creates 4 players when this class is first created and adds them to an ArrayList
     */
    private PlayerLab(Context appContext) {
        mAppContext = appContext;
        mPlayers = new ArrayList<>();
        for (int i  = 0; i < 4; i++) {
            Player p = new Player();
            p.setName("Player " + (i+1));
            p.setScore(0);
            p.setLastScore(0);
            p.setIndex(i);
            mPlayers.add(p);
        }
    }

    /**
        Singleton function. Only allows one instance of this class to exist
     */
    public static PlayerLab get(Context c) {
        if (sPlayerLab == null) {
            sPlayerLab = new PlayerLab(c.getApplicationContext());
        }
        return sPlayerLab;
    }

    /**
     *
     * @return Player ArrayList
     */
    public ArrayList<Player> getPlayers() {
        return mPlayers;
    }

    /**
     *
     * @param index 0 - Player 1
     *              1 - Player 2
     *              2 - Player 3
     *              3 - Player 4
     * @return Player with the corresponding index.
     */
    public Player getPlayer(int index){
        for (Player p : mPlayers){
            if (p.getIndex() == index)
                return p;
        }
        return null;
    }

    /**
     *
     * @return The highest score
     */
    public int getBiggestScore(){
        int highestScore = getPlayer(0).getScore();
        for (Player p :mPlayers){
            if (p.getScore() > highestScore)
                highestScore = p.getScore();
        }
        return highestScore;
    }

    /**
     *
     * @return The sum of all the bids. Used in fourhundred only
     */
    public int getTotalBidsFourhundred(){
        int totalBids = 0;
        for (Player p : mPlayers){
            totalBids += getRealBidFromVisualBid(p.getRealBid());
        }
        return totalBids;
    }

    /**
     *
     * @return True if win conditions are met. False otherwise.
     */
    public boolean didSomebodyWinFourhundred(){
        for (Player p : mPlayers){
            if (p.getScore() >= 41){
                int partnerScore = getPlayer(getPartnerIndex(p.getIndex())).getScore();
                if (partnerScore >= 0) {
                    p.setWinner(true);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * @return Instance of Player that won
     */
    public Player getWinner(){
        for (Player p : mPlayers){
            if (p.isWinner()){
                return p;
            }
        }
        return null;
    }

    /**
     * Sets all bids to 0
     */
    public void resetAllBids(){
        for (Player p : mPlayers){
            p.setRealBid(0);
        }
    }


    /**
     * Sets all Players 'last score' to 'current score'
     */
    public void setAllLastScore(){
        for (Player p : mPlayers){
            p.setLastScore(p.getScore());
        }
    }

    /**
     * Sets all Players score to 'last score'
     */
    public void undo(){
        for (Player p : mPlayers){
            p.setScore(p.getLastScore());
        }
    }


    /**
     *
     * @param bid Multiplied bid
     * @return Bid without any multipliers
     */
    public int getRealBidFromVisualBid(int bid){
        switch (bid){
            case 10:
                return 5;
            case 12:
                return 6;
            case 14:
                return 7;
            case 16:
                return 8;
            case 27:
                return 9;
            case 40:
                return 10;
            default:
                return bid;
        }
    }

    /**
     *
     * @param playerIndex Any player index (between 0 and 3)
     * @return The player index's partner index
     */
    public int getPartnerIndex(int playerIndex){
        switch (playerIndex){
            case 0:
                return 2;
            case 1:
                return 3;
            case 2:
                return 0;
            case 3:
                return 1;
        }
        return 0;
    }

    /**
     *
     * @param p Player class
     * @return p's partner name
     */
    public String getPartnerName(Player p){
        return getPlayer(getPartnerIndex(p.getIndex())).getName();
    }

    /**
     *
     * @return String 'fourhundred' or 'hearts'
     */
    public String getGameType() {
        return mGameType;
    }

    /**
     *
     * @param gameType The game to play
     */
    public void setGameType(String gameType) {
        mGameType = gameType;
    }

    /**
     *
     * @return True if somebody lose. False otherwise
     */
    public boolean didSomebodyLoseHearts(){
        for (Player p : mPlayers){
            if(p.getScore() >= 101){
                p.setLoser(true);
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return Player that has mLoser set to true
     */
    public Player getLoser(){
        for (Player p : mPlayers){
            if (p.isLoser()){
                return p;
            }
        }
        return null;
    }

    /**
     * All Player's mDealer set to false
     */
    public void setAllDealersFalse(){
        for (Player p : mPlayers) {
            p.setDealer(false);
        }
    }

    /**
     * Sets the next player as the dealer
     */
    public void setDealerNext(){
        Player currentDealer = getDealer();
        currentDealer.setDealer(false);

        int nextDealerIndex = (currentDealer.getIndex() + 1) % 4;

        Player nextDealer = getPlayer(nextDealerIndex);
        nextDealer.setDealer(true);
    }

    /**
     * Sets the previous player as the dealer
     */
    public void setDealerPrev(){
        Player currentDealer = getDealer();
        currentDealer.setDealer(false);

        int index = currentDealer.getIndex();
        int nextDealerIndex = (index == 0)? 3 : (index - 1);

        Player nextDealer = getPlayer(nextDealerIndex);
        nextDealer.setDealer(true);
    }

    /**
     *
     * @return The player that is the dealer
     */
    public Player getDealer(){
        for (Player p : mPlayers){
            if (p.isDealer()){
                return p;
            }
        }
        return null;
    }

    /**
     *
     * @return The total amount of bids. Used only in Hearts
     */
    public int getTotalBidsHearts(){
        int score = 0;
        for (Player p : mPlayers){
            score += p.getRealBid();
        }
        return score;
    }
}
