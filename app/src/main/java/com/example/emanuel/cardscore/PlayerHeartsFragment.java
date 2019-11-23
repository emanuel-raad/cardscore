package com.example.emanuel.cardscore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by emanuel on 8/4/15.
 */
public class PlayerHeartsFragment extends Fragment {

    private TextView mPlayerName1;
    private TextView mPlayerName2;
    private TextView mPlayerName3;
    private TextView mPlayerName4;

    private TextView mPlayerScore1;
    private TextView mPlayerScore2;
    private TextView mPlayerScore3;
    private TextView mPlayerScore4;

    private TextView mPlayerBidName1;
    private TextView mPlayerBidName2;
    private TextView mPlayerBidName3;
    private TextView mPlayerBidName4;

    private EditText mPlayerEditText1;
    private EditText mPlayerEditText2;
    private EditText mPlayerEditText3;
    private EditText mPlayerEditText4;

    private TextView mScoreLeft;

    private Button mNextButton;

    private Player mPlayer1;
    private Player mPlayer2;
    private Player mPlayer3;
    private Player mPlayer4;
    private PlayerLab mPlayerLab;
    private ArrayList<Player> mPlayers;

    private final static String TAG = "PlayerScoreFragment";

    private void calculateAllScore(){
        mPlayer1.setScore(mPlayer1.getScore() + mPlayer1.getRealBid());
        mPlayer2.setScore(mPlayer2.getScore() + mPlayer2.getRealBid());
        mPlayer3.setScore(mPlayer3.getScore() + mPlayer3.getRealBid());
        mPlayer4.setScore(mPlayer4.getScore() + mPlayer4.getRealBid());
    }

    private void refreshTextView() {
        setName(mPlayerName1, mPlayer1);
        setName(mPlayerName2, mPlayer2);
        setName(mPlayerName3, mPlayer3);
        setName(mPlayerName4, mPlayer4);

        setName(mPlayerBidName1, mPlayer1);
        setName(mPlayerBidName2, mPlayer2);
        setName(mPlayerBidName3, mPlayer3);
        setName(mPlayerBidName4, mPlayer4);

        mPlayerScore1.setText(Integer.toString(mPlayer1.getScore()));
        mPlayerScore2.setText(Integer.toString(mPlayer2.getScore()));
        mPlayerScore3.setText(Integer.toString(mPlayer3.getScore()));
        mPlayerScore4.setText(Integer.toString(mPlayer4.getScore()));
    }

    private void setName(TextView textView, Player player){
        if (player.isHmar()){
            textView.setText("Hmar");
        }
        else {
            textView.setText(player.getName());
        }
    }

    public void setEditTextListener(final EditText editText, final Player player){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    int bid = Integer.parseInt(s.toString());
                    player.setRealBid(bid);
                }
                if (s.length() == 0){
                    player.setRealBid(0);
                }
                mScoreLeft.setText("Left: " + (36 - mPlayerLab.getTotalBidsHearts()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void refreshEditText(){
        mPlayerEditText1.getText().clear();
        mPlayerEditText2.getText().clear();
        mPlayerEditText3.getText().clear();
        mPlayerEditText4.getText().clear();
    }

    public boolean areAllBidsValid(){
        for (Player p: mPlayers){
            if (((p.getRealBid() < 0) || (p.getRealBid() > 36))) {
                Toast.makeText(getActivity(), "Score must be between 0 and 36", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (mPlayerLab.getTotalBidsHearts() != 36){
            Toast.makeText(getActivity(), "Score doesn't add up to 36!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void isSomebodyAHmar(){
        for (Player p: mPlayers){
            if (p.isHmar()){
                p.setHmar(false);
            }
            if (p.getRealBid() == 36){
                p.setRealBid(37);
                p.setHmar(true);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPlayerLab = PlayerLab.get(getActivity());
        mPlayerLab.setGameType("hearts");
        getActivity().setTitle("Hearts");
        mPlayer1 = mPlayerLab.getPlayer(0);
        mPlayer2 = mPlayerLab.getPlayer(1);
        mPlayer3 = mPlayerLab.getPlayer(2);
        mPlayer4 = mPlayerLab.getPlayer(3);
        mPlayers = mPlayerLab.getPlayers();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hearts_score, parent, false);

        mPlayerName1 = (TextView)v.findViewById(R.id.player_1_name);
        mPlayerName2 = (TextView)v.findViewById(R.id.player_2_name);
        mPlayerName3 = (TextView)v.findViewById(R.id.player_3_name);
        mPlayerName4 = (TextView)v.findViewById(R.id.player_4_name);

        mPlayerScore1 = (TextView)v.findViewById(R.id.player_1_score);
        mPlayerScore2 = (TextView)v.findViewById(R.id.player_2_score);
        mPlayerScore3 = (TextView)v.findViewById(R.id.player_3_score);
        mPlayerScore4 = (TextView)v.findViewById(R.id.player_4_score);

        mPlayerBidName1 = (TextView)v.findViewById(R.id.player_1_bid_name);
        mPlayerBidName2 = (TextView)v.findViewById(R.id.player_2_bid_name);
        mPlayerBidName3 = (TextView)v.findViewById(R.id.player_3_bid_name);
        mPlayerBidName4 = (TextView)v.findViewById(R.id.player_4_bid_name);

        mScoreLeft = (TextView)v.findViewById(R.id.score_left);

        refreshTextView();

        mPlayerEditText1 = (EditText)v.findViewById(R.id.editText);
        mPlayerEditText2 = (EditText)v.findViewById(R.id.editText2);
        mPlayerEditText3 = (EditText)v.findViewById(R.id.editText3);
        mPlayerEditText4 = (EditText)v.findViewById(R.id.editText4);
        setEditTextListener(mPlayerEditText1, mPlayer1);
        setEditTextListener(mPlayerEditText2, mPlayer2);
        setEditTextListener(mPlayerEditText3, mPlayer3);
        setEditTextListener(mPlayerEditText4, mPlayer4);

        mNextButton = (Button)v.findViewById(R.id.nextButton);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (areAllBidsValid()) {
                    isSomebodyAHmar();
                    calculateAllScore();
                    refreshTextView();
                    refreshEditText();
                    mPlayerLab.resetAllBids();
                    if (mPlayerLab.didSomebodyLoseHearts()) {
                        Intent i = new Intent(getActivity(), PlayerWinnerActivity.class);
                        startActivity(i);
                    }
                }
            }
        });

        return v;
    }
}