package com.zeowls.redditu.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.URLUtil;

import com.zeowls.redditu.DetailFragment;
import com.zeowls.redditu.R;
import com.zeowls.redditu.WebFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.vpPager)
    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getBundleExtra("extra");
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setUrl(bundle.getString("Permalink"));
        detailFragment.setArguments(bundle);
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), bundle);
        mPager.setAdapter(myPagerAdapter);

//        getSupportFragmentManager().beginTransaction().add(R.id.container ,detailFragment, null).commit();
    }

    static class MyPagerAdapter extends FragmentPagerAdapter {
        private final Bundle bundle;

        MyPagerAdapter(FragmentManager fragmentManager, Bundle bundle) {
            super(fragmentManager);
            this.bundle = bundle;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            if (URLUtil.isValidUrl(bundle.getString("url")) && !bundle.getString("url").contains("https://www.reddit.com")) {
                return 2;
            } else {
                return 1;
            }
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    DetailFragment detailFragment = new DetailFragment();
                    detailFragment.setUrl(bundle.getString("Permalink"));
                    detailFragment.setArguments(bundle);
                    return detailFragment;
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    if (URLUtil.isValidUrl(bundle.getString("url")) && !bundle.getString("url").contains("https://www.reddit.com")) {
                        WebFragment webFragment = new WebFragment();
                        webFragment.setUrl(bundle.getString("url"));
                        webFragment.setArguments(bundle);
                        return webFragment;
                    } else {
                        return null;
                    }
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return "Comments";
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    if (URLUtil.isValidUrl(bundle.getString("url")) && !bundle.getString("url").contains("https://www.reddit.com")) {
                        return "Post";
                    } else {
                        return null;
                    }
                default:
                    return null;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
