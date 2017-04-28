package com.zeowls.redditu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.zeowls.redditu.DetailFragment;
import com.zeowls.redditu.HomeFragment;
import com.zeowls.redditu.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements HomeFragment.onFragmentInteraction {

    @BindView(R.id.container)
    FrameLayout mContainer;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.spinner_nav)
    Spinner mSpinner;

    private int mPosition = 0;
    HomeFragment homeFragment;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_list_item_array, R.layout.spinner_item);
        mSpinner.setAdapter(adapter);
        homeFragment = new HomeFragment();
        if (savedInstanceState != null)
            mSpinner.setSelection(savedInstanceState.getInt("type"));
        else {
            getSupportFragmentManager().beginTransaction().add(R.id.container, homeFragment, "homeFragmentTag").commit();
        }
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Bundle b = new Bundle();
                mPosition = position;
                b.putInt("type", position);
                HomeFragment homeFragment = ((HomeFragment) getSupportFragmentManager().findFragmentByTag("homeFragmentTag"));
                if (homeFragment!=null)
                homeFragment.onTypeChanged(position);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment, null).commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mTwoPane = findViewById(R.id.detail_container) != null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("type", mPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemSelected(Bundle bundle) {
        if (mTwoPane) {
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_container, detailFragment, null).commit();
        } else {
            Intent in = new Intent(this, DetailActivity.class);
            in.putExtra("extra", bundle);
            startActivity(in);
        }
    }
}
