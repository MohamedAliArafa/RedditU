package com.zeowls.redditu;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

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
    CommentAdapter mCommentAdapter;
    ArrayList<DetailResponse.Comment> children = new ArrayList<>();
    Gson gson = new Gson();
    private String url;

    public DetailFragment() {
        // Required empty public constructor
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
        } catch (Exception e){
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
        mCommentAdapter = new CommentAdapter(children);
        mDetailRecycler.setAdapter(mCommentAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        RedditApiEndpointInterface apiEndpointInterface = ApiCalls.getDetailClient()
                .create(RedditApiEndpointInterface.class);
        url = getArguments().getString("Permalink");
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mCommentAdapter.children = new CommentProcessor().fetchComments(getActivity(), response.body());
                mProgressBar.setVisibility(View.GONE);
                mCommentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

//    private void subReplies(String json, String id) {
//        Log.d("ID:", id);
//        DetailResponse subReply = gson.fromJson(json, DetailResponse.class);
//        for (DetailResponse.Child child : subReply.getData().getChildren()) {
//            Log.d("Replies " + id, child.getData().getReplies().toString());
//            if (child.getData().getReplies() != null && !child.getData().getReplies().toString().isEmpty()) {
//                subReplies(child.getData().getReplies().toString(), child.getData().getId());
//            }
//        }
//    }

    public void setUrl(String url) {
        this.url = url;
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
                holder.time.setText(child.getPostedOn());
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

}
