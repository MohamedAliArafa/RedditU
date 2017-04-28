package com.zeowls.redditu;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zeowls.redditu.data.RedditContract;

import java.util.ArrayList;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.home_recycler_view)
    RecyclerView mHomeRecycler;
    @BindView(R.id.progressbar)
    ProgressBar mProgressbar;
    HomeCursorAdapter mHomeAdapter;
    ArrayList<Child> children = new ArrayList<>();
    private int mPosition = -1;
    private int mSelection = 0;

    private static final String SELECTED_KEY = "selected_position";

    private static final int REDDIT_LOADER = 0;
    // For the forecast view we're showing only a small subset of the stored data.
    // Specify the columns we need.
    private static final String[] REDDIT_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            RedditContract.RedditEntry.TABLE_NAME + "." + RedditContract.RedditEntry._ID,
            RedditContract.RedditEntry.COLUMN_CATEGORY,
            RedditContract.RedditEntry.COLUMN_SUB_REDDIT,
            RedditContract.RedditEntry.COLUMN_SUBREDDIT_ID,
            RedditContract.RedditEntry.COLUMN_SCORE,
            RedditContract.RedditEntry.COLUMN_AUTHOR,
            RedditContract.RedditEntry.COLUMN_NUM_COMMENTS,
            RedditContract.RedditEntry.COLUMN_PERMALINK,
            RedditContract.RedditEntry.COLUMN_URL,
            RedditContract.RedditEntry.COLUMN_TITLE,
            RedditContract.RedditEntry.COLUMN_CREATED_UTC,
            RedditContract.RedditEntry.COLUMN_THUMBNAIL
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COLUMN_ID = 0;
    static final int COLUMN_CATEGORY = 1;
    static final int COLUMN_SUB_REDDIT = 2;
    static final int COLUMN_SUBREDDIT_ID = 3;
    static final int COLUMN_SCORE = 4;
    static final int COLUMN_AUTHOR = 5;
    static final int COLUMN_NUM_COMMENTS = 6;
    static final int COLUMN_PERMALINK = 7;
    static final int COLUMN_URL = 8;
    static final int COLUMN_TITLE = 9;
    static final int COLUMN_CREATED_UTC = 10;
    static final int COLUMN_THUMBNAIL = 11;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Sort order:  Ascending, by date.
        String sortOrder = RedditContract.RedditEntry.COLUMN_CREATED_UTC + " DESC";
        mProgressbar.setVisibility(View.VISIBLE);
        Uri RedditsUri = RedditContract.RedditEntry.CONTENT_URI;
        String selection = "(" + RedditContract.RedditEntry.COLUMN_CATEGORY + "=" + mSelection + ")";
        return new CursorLoader(getActivity(),
                RedditsUri,
                REDDIT_COLUMNS,
                selection,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mHomeAdapter.mCursorAdapter.swapCursor(data);
        mHomeAdapter.notifyDataSetChanged();
        mProgressbar.setVisibility(View.GONE);
//        if (mPosition != RecyclerView.NO_POSITION) {
//            mHomeRecycler.smoothScrollToPosition(mPosition);
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mHomeAdapter.mCursorAdapter.swapCursor(null);
        mHomeAdapter.notifyDataSetChanged();
    }

    public interface onFragmentInteraction {
        void onItemSelected(Bundle bundle);
    }

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("children", children);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onResume() {
        callServer();
//        if (mPosition != RecyclerView.NO_POSITION) {
//            mHomeRecycler.smoothScrollToPosition(mPosition);
//        }
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mHomeRecycler.setLayoutManager(mLayoutManager);
        mHomeRecycler.setItemAnimator(new DefaultItemAnimator());
        if (savedInstanceState != null)
            children = savedInstanceState.getParcelableArrayList("children");
        mHomeAdapter = new HomeCursorAdapter(getContext(), null);
        mHomeRecycler.setAdapter(mHomeAdapter);
    }

    private void callServer() {
        RedditApiEndpointInterface apiEndpointInterface = ApiCalls.getClient()
                .create(RedditApiEndpointInterface.class);
        Call<MainResponse> call;
        switch (mSelection) {
            case 0:
                call = apiEndpointInterface.getPopular();
                break;
            case 1:
                call = apiEndpointInterface.getNew();
                break;
            case 2:
                call = apiEndpointInterface.getNew();
                break;
            case 3:
                call = apiEndpointInterface.getRising();
                break;
            case 4:
                call = apiEndpointInterface.getControversial();
                break;
            case 5:
                call = apiEndpointInterface.getTop();
                break;
            default:
                call = apiEndpointInterface.getPopular();
                break;
        }

        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                children.clear();
                for (Child child : response.body().getData().getChildren()) {
                    children.add(child);
                    Log.d("Child: ", child.getData().getTitle());
                }
                try {
                    updateDatabase(children);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                t.printStackTrace();
                try {
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateDatabase(ArrayList<Child> children) throws Exception {
        Vector<ContentValues> cVVector = new Vector<>(children.size());
        Time dayTime = new Time();
        dayTime.setToNow();

        // we start at the day returned by local time. Otherwise this is a mess.
        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);
        for (int i = 0; i < children.size(); i++) {
            ContentValues redditValues = new ContentValues();
            Child child = children.get(i);
            String COLUMN_ID = child.getData().getId();
            int COLUMN_CATEGORY = mSelection;
            String COLUMN_SUB_REDDIT = child.getData().getSubreddit();
            String COLUMN_SUBREDDIT_ID = child.getData().getSubreddit();
            int COLUMN_SCORE = child.getData().getScore();
            String COLUMN_AUTHOR = child.getData().getAuthor();
            int COLUMN_NUM_COMMENTS = child.getData().getNumComments();
            String COLUMN_PERMALINK = child.getData().getPermalink();
            String COLUMN_URL = child.getData().getUrl();
            String COLUMN_TITLE = child.getData().getTitle();
            Double COLUMN_CREATED_UTC = child.getData().getCreatedUtc();
            String COLUMN_THUMBNAIL = child.getData().getThumbnail();
            redditValues.put(RedditContract.RedditEntry.COLUMN_ID, COLUMN_ID);
            redditValues.put(RedditContract.RedditEntry.COLUMN_CATEGORY, COLUMN_CATEGORY);
            redditValues.put(RedditContract.RedditEntry.COLUMN_SUB_REDDIT, COLUMN_SUB_REDDIT);
            redditValues.put(RedditContract.RedditEntry.COLUMN_SUBREDDIT_ID, COLUMN_SUBREDDIT_ID);
            redditValues.put(RedditContract.RedditEntry.COLUMN_SCORE, COLUMN_SCORE);
            redditValues.put(RedditContract.RedditEntry.COLUMN_AUTHOR, COLUMN_AUTHOR);
            redditValues.put(RedditContract.RedditEntry.COLUMN_NUM_COMMENTS, COLUMN_NUM_COMMENTS);
            redditValues.put(RedditContract.RedditEntry.COLUMN_PERMALINK, COLUMN_PERMALINK);
            redditValues.put(RedditContract.RedditEntry.COLUMN_URL, COLUMN_URL);
            redditValues.put(RedditContract.RedditEntry.COLUMN_TITLE, COLUMN_TITLE);
            redditValues.put(RedditContract.RedditEntry.COLUMN_CREATED_UTC, COLUMN_CREATED_UTC);
            redditValues.put(RedditContract.RedditEntry.COLUMN_THUMBNAIL, COLUMN_THUMBNAIL);

            cVVector.add(redditValues);
        }

        int inserted;
        // add to database
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
//             delete old data so we don't build up an endless history
            int deleted = getContext().getContentResolver().delete(RedditContract.RedditEntry.CONTENT_URI,
                    RedditContract.RedditEntry.COLUMN_CATEGORY + " == ?",
                    new String[]{String.valueOf(mSelection)});
            Log.d("Reddits deleted: ", String.valueOf(deleted));
            inserted = getContext().getContentResolver().bulkInsert(RedditContract.RedditEntry.CONTENT_URI, cvArray);
            Log.d("Reddits Inserted: ", String.valueOf(inserted));
            getLoaderManager().restartLoader(REDDIT_LOADER, null, this);
        }
    }

    public void onTypeChanged(int position) {
        mSelection = position;
        getLoaderManager().restartLoader(REDDIT_LOADER, null, this);
        callServer();
    }

//    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
//
//        ArrayList<Child> children;
//
//        HomeAdapter(ArrayList<Child> children) {
//            this.children = children;
//        }
//
//        class MyViewHolder extends RecyclerView.ViewHolder {
//            TextView title, author, subreddit, score, time, comments;
//            ImageView image;
//
//            MyViewHolder(View view) {
//                super(view);
//                title = (TextView) view.findViewById(R.id.list_item_title_text_view);
//                subreddit = (TextView) view.findViewById(R.id.list_item_subreddit_text_view);
//                author = (TextView) view.findViewById(R.id.list_item_author_text_view);
//                score = (TextView) view.findViewById(R.id.list_item_score_text_view);
//                time = (TextView) view.findViewById(R.id.list_item_time_text_view);
//                comments = (TextView) view.findViewById(R.id.list_item_comments_text_view);
//                image = (ImageView) view.findViewById(R.id.list_item_main_image_view);
//            }
//        }
//
//        @Override
//        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View itemView = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.main_list_item, parent, false);
//            return new MyViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(final MyViewHolder holder, int position) {
//            final Child child = children.get(position);
//            final String time = Utils.formatTime(child.getData().getCreatedUtc().longValue());
//            holder.title.setText(child.getData().getTitle());
//            holder.subreddit.setText(child.getData().getSubreddit());
//            holder.author.setText(child.getData().getAuthor());
//            holder.score.setText(String.valueOf(child.getData().getScore()));
//            holder.time.setText(time);
//            holder.comments.setText(Utils.formatCommentCount(getActivity(), child.getData().getNumComments()));
//            if (URLUtil.isValidUrl(child.getData().getThumbnail()))
//                Glide.with(getActivity()).load(child.getData().getThumbnail()).into(holder.image);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                holder.image.setTransitionName("Image_trans" + child.getData().getId());
//                holder.title.setTransitionName("Title_trans" + child.getData().getId());
//                holder.subreddit.setTransitionName("SubReddit_trans" + child.getData().getId());
//                holder.author.setTransitionName("Author_trans" + child.getData().getId());
//            }
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    DetailFragment detailFragment = new DetailFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("Title", child.getData().getTitle());
//                    bundle.putString("Subreddit", child.getData().getSubreddit());
//                    bundle.putString("Author", child.getData().getAuthor());
//                    bundle.putString("Score", child.getData().getScore().toString());
//                    bundle.putString("NumComments", Utils.formatCommentCount(getActivity(), child.getData().getNumComments()));
//                    bundle.putString("Time", time);
//                    bundle.putString("Permalink", child.getData().getPermalink());
//                    bundle.putString("url", child.getData().getUrl());
//                    bundle.putString("Id", child.getData().getId());
//                    if (holder.image.getDrawable() != null) {
//                        if (URLUtil.isValidUrl(child.getData().getThumbnail()))
//                            try {
//                                bundle.putParcelable("Image", (((GlideBitmapDrawable) holder.image.getDrawable().getCurrent()).getBitmap()));
//                            } catch (Exception e) {
//                                bundle.putParcelable("Image", (((BitmapDrawable) holder.image.getDrawable().getCurrent()).getBitmap()));
//                            }
//                    } else {
//                        bundle.putParcelable("Image", null);
//                    }
//                    detailFragment.setArguments(bundle);
//                    ((onFragmentInteraction) getActivity()).onItemSelected(bundle);
//
//                }
//            });
//        }
//
//        @Override
//        public int getItemCount() {
//            return children.size();
//        }
//    }

    public class HomeCursorAdapter extends RecyclerView.Adapter<HomeCursorAdapter.ViewHolder> {

        Cursor mCursor;
        // Because RecyclerView.Adapter in its current form doesn't natively
        // support cursors, we wrap a CursorAdapter that will do all the job
        // for us.
        CursorAdapter mCursorAdapter;

        Context mContext;

        public HomeCursorAdapter(Context context, Cursor c) {

            mContext = context;
            mCursor = c;
            mCursorAdapter = new CursorAdapter(mContext, c, 0) {

                @Override
                public View newView(Context context, Cursor cursor, ViewGroup parent) {
                    // Inflate the view here
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.main_list_item, parent, false);
                    ViewHolder viewHolder = new ViewHolder(view);
                    view.setTag(viewHolder);
                    return view;
                }

                private void onCustomClick(int position) {
                    Cursor cursor = mCursor;
                    cursor.moveToPosition(position);
                    String time = Utils.formatTime(((Double) cursor.getDouble(HomeFragment.COLUMN_CREATED_UTC)).longValue());
                    DetailFragment detailFragment = new DetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Title", cursor.getString(HomeFragment.COLUMN_TITLE));
                    bundle.putString("Subreddit", cursor.getString(HomeFragment.COLUMN_SUB_REDDIT));
                    bundle.putString("Author", cursor.getString(HomeFragment.COLUMN_AUTHOR));
                    bundle.putString("Score", String.valueOf(cursor.getInt(HomeFragment.COLUMN_SCORE)));
                    bundle.putString("NumComments", Utils.formatCommentCount(getActivity(), cursor.getInt(HomeFragment.COLUMN_NUM_COMMENTS)));
                    bundle.putString("Time", time);
                    bundle.putString("Permalink", cursor.getString(HomeFragment.COLUMN_PERMALINK));
                    bundle.putString("url", cursor.getString(HomeFragment.COLUMN_URL));
                    bundle.putString("Id", cursor.getString(HomeFragment.COLUMN_ID));
//                    if (holder.image.getDrawable() != null) {
//                        if (URLUtil.isValidUrl(cursor.getString(HomeFragment.COLUMN_THUMBNAIL)))
//                            try {
//                                bundle.putParcelable("Image", (((GlideBitmapDrawable) holder.image.getDrawable().getCurrent()).getBitmap()));
//                            } catch (Exception e) {
//                                bundle.putParcelable("Image", (((BitmapDrawable) holder.image.getDrawable().getCurrent()).getBitmap()));
//                            }
//                    } else {
//                        bundle.putParcelable("Image", null);
//                    }
                    detailFragment.setArguments(bundle);
                    ((onFragmentInteraction) getActivity()).onItemSelected(bundle);
                }

                @Override
                public void bindView(View view, Context context, Cursor cursor) {
                    ViewHolder holder = (ViewHolder) view.getTag();
                    final int position = cursor.getPosition();
                    String time = Utils.formatTime(((Double) cursor.getDouble(HomeFragment.COLUMN_CREATED_UTC)).longValue());
                    // Binding operations
                    holder.title.setText(cursor.getString(HomeFragment.COLUMN_TITLE));
                    holder.author.setText(cursor.getString(HomeFragment.COLUMN_AUTHOR));
                    holder.subreddit.setText(cursor.getString(HomeFragment.COLUMN_SUB_REDDIT));
                    holder.score.setText(String.valueOf(cursor.getInt(HomeFragment.COLUMN_SCORE)));
                    holder.time.setText(time);
                    holder.comments.setText(Utils.formatCommentCount(getActivity(), cursor.getInt(HomeFragment.COLUMN_NUM_COMMENTS)));
                    if (URLUtil.isValidUrl(cursor.getString(HomeFragment.COLUMN_THUMBNAIL)))
                        Glide.with(getActivity()).load(cursor.getString(HomeFragment.COLUMN_THUMBNAIL))
                                .into(holder.image);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onCustomClick(position);
                        }
                    });
                }
            };
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView title, author, subreddit, score, time, comments;
            ImageView image;

            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.list_item_title_text_view);
                subreddit = (TextView) itemView.findViewById(R.id.list_item_subreddit_text_view);
                author = (TextView) itemView.findViewById(R.id.list_item_author_text_view);
                score = (TextView) itemView.findViewById(R.id.list_item_score_text_view);
                time = (TextView) itemView.findViewById(R.id.list_item_time_text_view);
                comments = (TextView) itemView.findViewById(R.id.list_item_comments_text_view);
                image = (ImageView) itemView.findViewById(R.id.list_item_main_image_view);
            }
        }

        @Override
        public int getItemCount() {
            return mCursorAdapter.getCount();
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // Passing the binding operation to cursor loader
            mCursorAdapter.getCursor().moveToPosition(position); //EDITED: added this line as suggested in the comments below, thanks :)
            mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Passing the inflater job to the cursor-adapter
            View v = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
            return new ViewHolder(v);
        }
    }

}
