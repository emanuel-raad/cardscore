package com.example.emanuel.cardscore;

import android.support.v4.app.Fragment;

public class PlayerWinnerActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new PlayerWinnerFragment();
    }
}
