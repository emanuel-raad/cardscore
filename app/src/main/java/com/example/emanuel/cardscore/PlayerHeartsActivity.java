package com.example.emanuel.cardscore;

import android.support.v4.app.Fragment;

public class PlayerHeartsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new PlayerHeartsFragment();
    }
}
