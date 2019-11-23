package com.example.emanuel.cardscore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by emanuel on 8/4/15.
 */
public class PlayerFourhundredFragment extends Fragment {

    private TextView mBidsLeft;
    private TextView mRoundNumberText;

    private TextView mPlayerName1,
            mPlayerName2,
            mPlayerName3,
            mPlayerName4;
    private ArrayList<TextView> mPlayerNames;

    private TextView mPlayerScore1,
            mPlayerScore2,
            mPlayerScore3,
            mPlayerScore4;
    private ArrayList<TextView> mPlayerScores;

    private TextView mPlayerBidName1,
            mPlayerBidName2,
            mPlayerBidName3,
            mPlayerBidName4;
    private ArrayList<TextView> mPlayerBidNames;

    private CheckBox mPlayerBidCheckBox1,
            mPlayerBidCheckBox2,
            mPlayerBidCheckBox3,
            mPlayerBidCheckBox4;
    private ArrayList<CheckBox> mPlayerBidCheckBoxes;

    private Spinner mSpinner1,
            mSpinner2,
            mSpinner3,
            mSpinner4;
    private ArrayList<Spinner> mSpinners;

    private boolean[] mIsChecked = new boolean[4];

    private Button mNextButton;
    private Button mUndoButton;

    private PlayerLab mPlayerLab;
    private ArrayList<Player> mPlayers;

    private int mRoundNumber = 1;
    private int mRoundState = 0;
    /*
        0 - Getting Bids
        1 - Waiting for next round
     */

    private String mBidsLeftString = "";

    private final static String TAG = "PlayerScoreFragment";
    public static final String DIALOG_SCORE = "score";
    public static final int REQUEST_SCORE = 0;

    private int calculateScore(Player player, boolean gotBid){
        int score = player.getScore();
        int bid = player.getRealBid();

        if (gotBid)
            return score + bid;
        else
            return score - bid;
    }

    private void calculateAllScore(){
        for (Player p : mPlayers) {
            p.setScore(calculateScore(p, mIsChecked[p.getIndex()]));
        }
    }

    private void refreshTextView() {

        mRoundNumberText.setText("Round: " + Integer.toString(mRoundNumber));

        for (Player p : mPlayers){
            int index = p.getIndex();
            TextView playerName = mPlayerNames.get(index);
            TextView playerBidName = mPlayerBidNames.get(index);
            TextView playerScore = mPlayerScores.get(index);

            playerName.setText(p.getName());
            playerBidName.setText(p.getName());
            playerScore.setText(Integer.toString(p.getScore()));
        }
    }

    private void refreshSpinner(){
        for (Player p : mPlayers) {
            Spinner s = mSpinners.get(p.getIndex());
            s.setAdapter(getSpinnerAdapter(p));
        }
    }

    private void resetCheckBoxState(){
        for (Player p : mPlayers){
            CheckBox c = mPlayerBidCheckBoxes.get(p.getIndex());
            c.setChecked(false);
        }
    }

    private void setCheckboxesEnabledState(boolean enabled){
        for (Player p : mPlayers){
            CheckBox c = mPlayerBidCheckBoxes.get(p.getIndex());
            c.setEnabled(enabled);
        }
    }

    private void setSpinnerListener(final Spinner number, final Player player){
        number.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer bid = (Integer) number.getItemAtPosition(position);
                player.setRealBid(bid);
                mBidsLeftString = mPlayerLab.getTotalBidsFourhundred() + "/" + getMinimumBid();
                mBidsLeft.setText(mBidsLeftString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setCheckListener(CheckBox check, final int index){
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mIsChecked[index] = isChecked;
            }
        });
    }

    private int getMinimumBid(){
        int highestScore = mPlayerLab.getBiggestScore();

        if ((highestScore >= 30) && (highestScore <= 39))
            return 12;
        else if (highestScore >= 40)
            return 13;
        return 11;
    }

    private boolean areAllBidsValid(){
        int totalBids = mPlayerLab.getTotalBidsFourhundred();
        int requiredBids = getMinimumBid();

        return (totalBids >= requiredBids);
    }

    private void drawDealer(){
        int dealerColor = getResources().getColor(R.color.dealer);
        int defaultColor = getResources().getColor(R.color.default_font);

        for (Player p : mPlayers){
            TextView textView = mPlayerNames.get(p.getIndex());
            if (p.isDealer()){
                textView.setTextColor(dealerColor);
            }
            else {
                textView.setTextColor(defaultColor);
            }
        }
    }

    private void setSpinnerState(boolean enabled){
        for (Spinner s : mSpinners) {
            s.setEnabled(enabled);
        }
    }

    private ArrayAdapter<Integer> getSpinnerAdapter(Player player){
        Integer[] bidsUnderThirty = new Integer[]{2,3,4,10,12,14,16,27,40};
        Integer[] bidsUnderFourty = new Integer[]{3,4,5,12,14,16,27,40};
        Integer[] bidsOverFourty = new Integer[]{4,5,12,14,16,27,40};
        Integer[] listToUse = new Integer[]{};
        int score = player.getScore();

        if (score <= 29)
            listToUse = bidsUnderThirty;
        else if ((score >= 30) && (score <= 39))
            listToUse = bidsUnderFourty;
        else if (score >= 40)
            listToUse = bidsOverFourty;

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>
                (getActivity(), R.layout.spinner_details, listToUse);

        return adapter;
    }

    private void resetInputsAndRound(){
        mRoundState = 0;
        mNextButton.setText(R.string.check_bid);
        setCheckboxesEnabledState(false);
        setSpinnerState(true);
        refreshTextView();
        refreshSpinner();
        resetCheckBoxState();
        mPlayerLab.resetAllBids();
        drawDealer();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        //Only do something if the user pressed okay on the dialog
        if (resultCode != Activity.RESULT_OK) return;
        /*
            If this fragment starts more than 1 other fragment, you would add else ifs for the
            other possible request codes.
         */
        if (requestCode == REQUEST_SCORE){
            //Get the data from the intent
            int score = (int)data
                    .getSerializableExtra(ChangeScoreDialogFragment.EXTRA_SCORE);
            int index = (int)data
                    .getSerializableExtra(ChangeScoreDialogFragment.EXTRA_INDEX);
            //Use the data to do something
            mPlayers.get(index).setScore(score);
            //Refresh the text. Kinda inefficient cause i refresh every single widget as opposed
            //to just the ones that were changed, but whatever.
            refreshTextView();
            refreshSpinner();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mPlayerLab = PlayerLab.get(getActivity());
        mPlayerLab.setGameType("fourhundred");
        mPlayers = mPlayerLab.getPlayers();
        getActivity().setTitle("Fourhundred");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fourhundred_score, parent, false);

        /*
            I don't know if there's a better way to handle references, but for now,
            it's just a long list. The objects are in an ArrayList which makes things kind of
            easier
         */
        mSpinner1 = (Spinner)v.findViewById(R.id.spinner1);
        mSpinner2 = (Spinner)v.findViewById(R.id.spinner2);
        mSpinner3 = (Spinner)v.findViewById(R.id.spinner3);
        mSpinner4 = (Spinner)v.findViewById(R.id.spinner4);

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

        mPlayerBidCheckBox1 = (CheckBox)v.findViewById(R.id.player_1_bid_checkbox);
        mPlayerBidCheckBox2 = (CheckBox)v.findViewById(R.id.player_2_bid_checkbox);
        mPlayerBidCheckBox3 = (CheckBox)v.findViewById(R.id.player_3_bid_checkbox);
        mPlayerBidCheckBox4 = (CheckBox)v.findViewById(R.id.player_4_bid_checkbox);

        mBidsLeft = (TextView)v.findViewById(R.id.bids_left);
        mRoundNumberText = (TextView)v.findViewById(R.id.round_number);

        /*
            Again, dunno if there's a better way to do thing. Another long list
         */
        mPlayerNames = new ArrayList<>();
        mPlayerNames.add(0, mPlayerName1);
        mPlayerNames.add(1, mPlayerName2);
        mPlayerNames.add(2, mPlayerName3);
        mPlayerNames.add(3, mPlayerName4);

        mPlayerBidNames = new ArrayList<>();
        mPlayerBidNames.add(0, mPlayerBidName1);
        mPlayerBidNames.add(1, mPlayerBidName2);
        mPlayerBidNames.add(2, mPlayerBidName3);
        mPlayerBidNames.add(3, mPlayerBidName4);

        mPlayerScores = new ArrayList<>();
        mPlayerScores.add(0, mPlayerScore1);
        mPlayerScores.add(1, mPlayerScore2);
        mPlayerScores.add(2, mPlayerScore3);
        mPlayerScores.add(3, mPlayerScore4);

        mPlayerBidCheckBoxes = new ArrayList<>();
        mPlayerBidCheckBoxes.add(0, mPlayerBidCheckBox1);
        mPlayerBidCheckBoxes.add(1, mPlayerBidCheckBox2);
        mPlayerBidCheckBoxes.add(2, mPlayerBidCheckBox3);
        mPlayerBidCheckBoxes.add(3, mPlayerBidCheckBox4);

        mSpinners = new ArrayList<>();
        mSpinners.add(0, mSpinner1);
        mSpinners.add(1, mSpinner2);
        mSpinners.add(2, mSpinner3);
        mSpinners.add(3, mSpinner4);

        refreshTextView();
        refreshSpinner();
        drawDealer();

        /*
            I could use one for loop instead of two but I'll just keep them seperate cause they do
            different things. I don't know if there's a better way to set listeners either. In the
            first loop, i call a function and in the second, I just set them inside the loop.
         */
        for (Player p : mPlayers) {
            Spinner s = mSpinners.get(p.getIndex());
            setSpinnerListener(s, p);
        }

        for (Player p: mPlayers) {
            final int index = p.getIndex();
            final int score = p.getScore();
            TextView textView = mPlayerScores.get(index);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getActivity()
                            .getSupportFragmentManager();
                    ChangeScoreDialogFragment dialog = new ChangeScoreDialogFragment()
                            .newInstance(score, index);
                    dialog.setTargetFragment(PlayerFourhundredFragment.this, REQUEST_SCORE);
                    dialog.show(fm, DIALOG_SCORE);
                }
            });
        }

        setCheckboxesEnabledState(false);
        for (int i = 0; i < mPlayerBidCheckBoxes.size(); i++) {
            CheckBox checkBox = mPlayerBidCheckBoxes.get(i);
            setCheckListener(checkBox, i);
        }

        mUndoButton = (Button)v.findViewById(R.id.undoButton);
        mUndoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayerLab.undo();
                mRoundNumber -= 1;
                resetCheckBoxState();
                refreshSpinner();
                refreshTextView();
                setCheckboxesEnabledState(true);
                mUndoButton.setEnabled(false);
                mPlayerLab.setDealerPrev();
                drawDealer();
            }
        });

        //Can't undo on the first round
        if (mRoundNumber == 1){
            mUndoButton.setEnabled(false);
        }

        mNextButton = (Button)v.findViewById(R.id.nextButton);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String errorText = "";
                //Getting Bids
                if (mRoundState == 0){
                    //Bids work. Waiting for the next round
                    if (areAllBidsValid()) {
                        mRoundState = 1;
                        drawDealer();
                        mNextButton.setText(R.string.next_round);
                        setSpinnerState(false);
                        setCheckboxesEnabledState(true);
                    }
                    //Bids are too low. Going to next round
                    else {
                        errorText = "Bids are too low! Reshuffle.";
                        Toast.makeText(getActivity(), errorText, Toast.LENGTH_SHORT).show();
                        mRoundNumber += 1;
                        mUndoButton.setEnabled(true);
                        mPlayerLab.setAllLastScore();
                        mPlayerLab.setDealerNext();
                        resetInputsAndRound();
                    }
                }
                //Checking who got the bids, then next round.
                else if (mRoundState == 1){
                    mRoundNumber += 1;
                    mUndoButton.setEnabled(true);
                    mPlayerLab.setDealerNext();
                    mPlayerLab.setAllLastScore();
                    calculateAllScore();
                    resetInputsAndRound();
                    if (mPlayerLab.didSomebodyWinFourhundred()){
                        Intent i = new Intent(getActivity(), PlayerWinnerActivity.class);
                        startActivity(i);
                    }
                }

            }
        });

        return v;
    }
}
