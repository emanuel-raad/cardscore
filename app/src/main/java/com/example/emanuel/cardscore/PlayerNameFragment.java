package com.example.emanuel.cardscore;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlayerNameFragment extends Fragment {

    private PlayerLab mPlayerLab;
    private ArrayList<Player> mPlayers;

    private EditText mPlayerName1,
                mPlayerName2,
                mPlayerName3,
                mPlayerName4;
    private ArrayList<EditText> mPlayerNames;

    private Button mFourhundredButton;
    private Button mHeartsButton;

    private RadioGroup mRadioGroup;

    private final static String TAG = "PlayerNameFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /**
     *
     * @param editText EditText to add an addTextChangedListener. Sets the text typed to a player's
     *                 name
     * @param player   The player the EditText corresponds to
     */
    public void setEditTextListener(final EditText editText, final Player player){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                player.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_player_name, parent, false);

        mPlayerLab = PlayerLab.get(getActivity());
        mPlayers = mPlayerLab.getPlayers();

        mPlayerName1 = (EditText)v.findViewById(R.id.player_name_1_EditText);
        mPlayerName2 = (EditText)v.findViewById(R.id.player_name_2_EditText);
        mPlayerName3 = (EditText)v.findViewById(R.id.player_name_3_EditText);
        mPlayerName4 = (EditText)v.findViewById(R.id.player_name_4_EditText);

        mPlayerNames = new ArrayList<>();
        mPlayerNames.add(0, mPlayerName1);
        mPlayerNames.add(1, mPlayerName2);
        mPlayerNames.add(2, mPlayerName3);
        mPlayerNames.add(3, mPlayerName4);

        for (Player p : mPlayers) {
            EditText editText = mPlayerNames.get(p.getIndex());
            setEditTextListener(editText, p);
        }

        mPlayers.get(0).setDealer(true);

        /*
            RadioButtons aligned with the EditTexts. Sets the dealer to the selected
            radio button.
         */
        mRadioGroup = (RadioGroup)v.findViewById(R.id.radioGroup);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mPlayerLab.setAllDealersFalse();

                View radioButton = mRadioGroup.findViewById(checkedId);
                int index = mRadioGroup.indexOfChild(radioButton);
                /*
                    player index ------- radio index
                    0                   0
                    2                   1

                    1                   2
                    3                   3
                 */
                if (index == 1)
                    index = 2;
                else if (index == 2)
                    index = 1;
                (mPlayerLab.getPlayer(index)).setDealer(true);
            }
        });

        /*
            Starts the appropriate game based on button input
         */
        mFourhundredButton = (Button)v.findViewById(R.id.fourhundred_button);
        mFourhundredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PlayerFourhundredActivity.class);
                startActivity(i);
            }
        });
        mHeartsButton = (Button)v.findViewById(R.id.hearts_button);
        mHeartsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PlayerHeartsActivity.class);
                startActivity(i);
            }
        });

        return v;
    }
}