package com.example.emanuel.cardscore;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

/**
 * Created by emanuel on 8/10/15.
 */
public class ChangeScoreDialogFragment extends DialogFragment{

    public static final String EXTRA_SCORE =
            "com.example.emanuel.cardscore.score";
    public static final String EXTRA_INDEX =
            "com.example.emanuel.cardscore.index";

    private int mScore;
    private int mIndex;



    /** Input the fragment needs to start (although i dont really need int score)
     *  Creates a new bundle object called 'args' and puts the input in there
     *  to be accessed later.
     *
     * @param score Current score
     * @param index Index of the player's score you are changing
     * @return Fragment with those params bundled
     */
    public static ChangeScoreDialogFragment newInstance(int score, int index){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_SCORE, score);
        args.putSerializable(EXTRA_INDEX, index);

        ChangeScoreDialogFragment fragment = new ChangeScoreDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private void sendResult(int resultCode){
        if (getTargetFragment() == null) return;

        Intent i = new Intent();
        i.putExtra(EXTRA_SCORE, mScore);
        i.putExtra(EXTRA_INDEX, mIndex);

        /*
            Since this fragment was started by another fragment, we use
            getTargetFragment().onActivityResult
            In this case, result code is 'sendResult(Activity.RESULT_OK)', or -1.
         */

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, i);
    }

    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState){
        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_score, null);

        mScore = (int)getArguments().getSerializable(EXTRA_SCORE);
        mIndex = (int)getArguments().getSerializable(EXTRA_INDEX);

        EditText editText = (EditText)v.findViewById(R.id.dialog_score_editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mScore = Integer.parseInt(s.toString());
                getArguments().putSerializable(EXTRA_SCORE, mScore);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.dialog_score_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .create();
    }
}
