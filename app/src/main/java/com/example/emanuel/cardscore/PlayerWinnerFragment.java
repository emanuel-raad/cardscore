package com.example.emanuel.cardscore;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by emanuel on 8/6/15.
 */
public class PlayerWinnerFragment extends Fragment {

    private PlayerLab mPlayerLab;
    private TextView mMessage;
    private String mGameType;
    private String mWinLose;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayerLab = PlayerLab.get(getActivity());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_player_winner, parent, false);

        mMessage = (TextView)v.findViewById(R.id.message);
        mGameType = mPlayerLab.getGameType();

        String message = "";
        String mWinLose = "";

        switch (mGameType){
            case "fourhundred": {
                String winnerNameString = (mPlayerLab.getWinner()).getName();
                String partnerNameString = mPlayerLab.getPartnerName(mPlayerLab.getWinner());
                message = "Congratulations! \n\n" + winnerNameString + "\nand\n" + partnerNameString;
                mWinLose = "Winner";
                break;
            }
            case "hearts": {
                String loserName = (mPlayerLab.getLoser()).getName();
                message = loserName + "\nlost!!";
                mWinLose = "Loser";
                break;
            }
        }

        getActivity().setTitle(mWinLose);
        mMessage.setText(message);

        return v;
    }
}
