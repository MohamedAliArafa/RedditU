package com.zeowls.redditu;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.zeowls.redditu.data.RedditContract;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int COMMENT_LOADER = 0;
    // For the forecast view we're showing only a small subset of the stored data.
    // Specify the columns we need.
    private static final String[] COMMENT_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            RedditContract.CommentEntry.TABLE_NAME + "." + RedditContract.CommentEntry._ID,
            RedditContract.CommentEntry.COLUMN_REDIIT_KEY,
            RedditContract.CommentEntry.COLUMN_REDIIT_ID,
            RedditContract.CommentEntry.COLUMN_HTML_TEXT,
            RedditContract.CommentEntry.COLUMN_AUTHOR,
            RedditContract.CommentEntry.COLUMN_POINTS,
            RedditContract.CommentEntry.COLUMN_LEVEL,
            RedditContract.CommentEntry.COLUMN_POSTED_ON
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COLUMN_ID = 0;
    static final int COLUMN_REDIIT_KEY = 1;
    static final int COLUMN_REDIIT_ID = 2;
    static final int COLUMN_HTML_TEXT = 3;
    static final int COLUMN_AUTHOR = 4;
    static final int COLUMN_POINTS = 5;
    static final int COLUMN_LEVEL = 5;
    static final int COLUMN_POSTED_ON = 6;

    @BindView(R.id.detail_recycler_view)
    RecyclerView mDetailRecycler;
    @BindView(R.id.list_item_main_image_view)
    ImageView mMainImage;
    @BindView(R.id.detail_item_title_text_view)
    TextView mTitle;
    @BindView(R.id.detail_item_subreddit_text_view)
    TextView mSubReddit;
    @BindView(R.id.detail_item_author_text_view)
    TextView mAuthor;
    @BindView(R.id.detail_item_time_text_view)
    TextView mTime;
    @BindView(R.id.list_item_score_text_view)
    TextView mScore;
    @BindView(R.id.list_item_comments_text_view)
    TextView mCommentsCount;
    @BindView(R.id.detail_header)
    LinearLayout mHeader;
    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;
    HomeCursorAdapter mCommentAdapter;
    ArrayList<DetailResponse.Comment> children = new ArrayList<>();
    Gson gson = new Gson();

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Sort order:  Ascending, by date.
        String sortOrder = RedditContract.CommentEntry.COLUMN_POSTED_ON + " DESC";
        Uri CommentssUri = RedditContract.CommentEntry.CONTENT_URI;
        String selection = "(" + RedditContract.CommentEntry.COLUMN_REDIIT_ID + "=" + getArguments().getString("Id") + ")";
        return new CursorLoader(getActivity(),
                CommentssUri,
                COMMENT_COLUMNS,
                selection,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCommentAdapter.mCursorAdapter.swapCursor(data);
        mProgressBar.setVisibility(View.GONE);
        mCommentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCommentAdapter.mCursorAdapter.swapCursor(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, v);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mMainImage.setTransitionName(getArguments().getString("image_trans"));
                mTitle.setTransitionName(getArguments().getString("title_trans"));
                mSubReddit.setTransitionName(getArguments().getString("sub_trans"));
                mAuthor.setTransitionName(getArguments().getString("author_trans"));
            }
            mMainImage.setImageBitmap((Bitmap) getArguments().getParcelable("Image"));
            mTitle.setText(getArguments().getString("Title"));
            mSubReddit.setText(getArguments().getString("Subreddit"));
            mAuthor.setText(getArguments().getString("Author"));
            mScore.setText(String.valueOf(getArguments().getString("Score")));
            mCommentsCount.setText(getArguments().getString("NumComments"));
            mTime.setText(getArguments().getString("Time"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mDetailRecycler.setLayoutManager(mLayoutManager);
        mDetailRecycler.setItemAnimator(new DefaultItemAnimator());
        mCommentAdapter = new HomeCursorAdapter(getContext(), null);
        mDetailRecycler.setAdapter(mCommentAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        RedditApiEndpointInterface apiEndpointInterface = ApiCalls.getDetailClient()
                .create(RedditApiEndpointInterface.class);
        String url = getArguments().getString("Permalink");
        Call<String> call = apiEndpointInterface.getDetails(url);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONArray r = new JSONArray(response.body())
                            .getJSONObject(0)
                            .getJSONObject("data")
                            .getJSONArray("children");
                    DetailResponse.Child header = gson.fromJson(r.get(0).toString(), DetailResponse.Child.class);
                    if (URLUtil.isValidUrl(header.getData().getThumbnail()))
                        Glide.with(getActivity()).load(header.getData().getThumbnail()).into(mMainImage);
                    mTitle.setText(header.getData().getTitle());
                    mSubReddit.setText(header.getData().getSubreddit());
                    mAuthor.setText(header.getData().getAuthor());
                    mScore.setText(String.valueOf(header.getData().getScore()));
                    mCommentsCount.setText(Utils.formatCommentCount(getActivity(), header.getData().getNumComments()));
                    mTime.setText(Utils.formatTime(header.getData().getCreatedUtc().longValue()));
                    ArrayList<DetailResponse.Comment> children = new CommentProcessor().fetchComments(getActivity(), response.body(), header.getData().getId());
                    updateDatabase(children);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void updateDatabase(ArrayList<DetailResponse.Comment> children) throws Exception {
        Vector<ContentValues> cVVector = new Vector<>(children.size());
        Time dayTime = new Time();
        dayTime.setToNow();

        // we start at the day returned by local time. Otherwise this is a mess.
        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);
        for (int i = 0; i < children.size(); i++) {
            ContentValues redditValues = new ContentValues();
            DetailResponse.Comment child = children.get(i);
            redditValues.put(RedditContract.CommentEntry.COLUMN_REDIIT_ID, getArguments().getString("Id"));
            redditValues.put(RedditContract.CommentEntry.COLUMN_HTML_TEXT, child.getHtmlText());
            redditValues.put(RedditContract.CommentEntry.COLUMN_AUTHOR, child.getAuthor());
            redditValues.put(RedditContract.CommentEntry.COLUMN_POINTS, child.getPoints());
            redditValues.put(RedditContract.CommentEntry.COLUMN_POSTED_ON, child.getPostedOn());
            redditValues.put(RedditContract.CommentEntry.COLUMN_LEVEL, child.getLevel());

            cVVector.add(redditValues);
        }

        int inserted;
        // add to database
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
//             delete old data so we don't build up an endless history
            int deleted = getContext().getContentResolver().delete(RedditContract.CommentEntry.CONTENT_URI,
                    RedditContract.CommentEntry.COLUMN_REDIIT_ID + " == ?",
                    new String[]{getArguments().getString("Id")});
            Log.d("Reddits deleted: ", String.valueOf(deleted));
            inserted = getContext().getContentResolver().bulkInsert(RedditContract.CommentEntry.CONTENT_URI, cvArray);
            Log.d("Reddits Inserted: ", String.valueOf(inserted));
            getLoaderManager().restartLoader(COMMENT_LOADER, null, this);
        }
    }

    class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

        ArrayList<DetailResponse.Comment> children;

        CommentAdapter(ArrayList<DetailResponse.Comment> children) {
            this.children = children;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView title, author, score, time;
            View padding;

            MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.detail_list_title);
                author = (TextView) view.findViewById(R.id.detail_list_author);
                score = (TextView) view.findViewById(R.id.detail_list_score);
                time = (TextView) view.findViewById(R.id.detail_list_time);
                padding = view.findViewById(R.id.detail_list_pading);
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.comment_list_item, parent, false);
            return new MyViewHolder(itemView);
        }

        int color;
        ArrayList<Integer> colors = new ArrayList<>();

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            DetailResponse.Comment child = children.get(position);
            if (colors.size() < child.getLevel() - 1) {
                Random rnd = new Random();
                color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                colors.add(color);
            }
            try {
                holder.itemView.setPadding(child.getLevel() * 10, 0, 0, 0);
                holder.title.setText(child.getHtmlText());
                holder.author.setText(child.getAuthor());
                holder.score.setText(String.valueOf(child.getPoints()));
                holder.time.setText(Utils.formatTime((long)child.getPostedOn()));
                holder.padding.setBackgroundColor(colors.get(child.getLevel()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return children.size();
        }
    }

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

                int color;
                ArrayList<Integer> colors = new ArrayList<>();

                @Override
                public View newView(Context context, Cursor cursor, ViewGroup parent) {
                    // Inflate the view here
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.comment_list_item, parent, false);
                    ViewHolder viewHolder = new ViewHolder(view);
                    view.setTag(viewHolder);
                    return view;
                }

                @Override
                public void bindView(View view, Context context, Cursor cursor) {
                    ViewHolder holder = (ViewHolder) view.getTag();

                    DetailResponse.Comment comment = new DetailResponse.Comment();
                    comment.setAuthor(cursor.getString(DetailFragment.COLUMN_AUTHOR));
                    comment.setHtmlText(cursor.getString(DetailFragment.COLUMN_HTML_TEXT));
                    comment.setLevel(cursor.getInt(DetailFragment.COLUMN_LEVEL));
                    comment.setPoints(String.valueOf(cursor.getInt(DetailFragment.COLUMN_POINTS)));
                    comment.setPostedOn(cursor.getDouble(DetailFragment.COLUMN_POSTED_ON));
                    comment.setRedditID(getArguments().getString("Id"));

                    String time = Utils.formatTime((long) comment.getPostedOn());

                    if (colors.size() < comment.getLevel() - 1) {
                        Random rnd = new Random();
                        color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                        colors.add(color);
                    }
                    try {
                        holder.itemView.setPadding(comment.getLevel() * 10, 0, 0, 0);
                        holder.title.setText(comment.getHtmlText());
                        holder.author.setText(comment.getAuthor());
                        holder.score.setText(comment.getPoints());
                        holder.time.setText(time);
                        holder.padding.setBackgroundColor(colors.get(comment.getLevel()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView title, author, score, time;
            View padding;

            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.detail_list_title);
                author = (TextView) itemView.findViewById(R.id.detail_list_author);
                score = (TextView) itemView.findViewById(R.id.detail_list_score);
                time = (TextView) itemView.findViewById(R.id.detail_list_time);
                padding = itemView.findViewById(R.id.detail_list_pading);
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
