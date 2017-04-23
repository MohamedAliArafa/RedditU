package com.zeowls.redditu.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.zeowls.redditu.HomeFragment;
import com.zeowls.redditu.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.container)
    FrameLayout mContainer;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.spinner_nav)
    Spinner mSpinner;

    private int mPosition = 0;
    HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_list_item_array, R.layout.spinner_item);
        mSpinner.setAdapter(adapter);
        if (savedInstanceState != null)
            mSpinner.setSelection(savedInstanceState.getInt("type"));
        else {
            homeFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.container, homeFragment, null).commit();
        }
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Bundle b = new Bundle();
                mPosition = position;
                b.putInt("type", position);
                homeFragment.onTypeChanged(position);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment, null).commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("type", mPosition);
        super.onSaveInstanceState(outState);
    }

}
