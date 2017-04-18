package com.zeowls.redditu;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

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
    CommentAdapter mCommentAdapter;
    ArrayList<DetailResponse.Comment> children = new ArrayList<>();
    ArrayList<DetailResponse> data = new ArrayList<>();
    Gson gson = new Gson();

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, v);
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

        RedditApiEndpointInterface apiEndpointInterface = ApiCalls.getDetailClient()
                .create(RedditApiEndpointInterface.class);

        Call<String> call = apiEndpointInterface.getDetails("KendrickLamar/comments/65n3lr/if_kendrick_drops_a_new_album_tomorrow_ill_donate");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                mCommentAdapter.children = new CommentProcessor().fetchComments(response.body());;
                mCommentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void subReplies(String json, String id) {
        Log.d("ID:", id);
        DetailResponse subReply = gson.fromJson(json, DetailResponse.class);
        for (DetailResponse.Child child : subReply.getData().getChildren()) {
            Log.d("Replies " + id, child.getData().getReplies().toString());
            if (child.getData().getReplies() != null && !child.getData().getReplies().toString().isEmpty()) {
                subReplies(child.getData().getReplies().toString(), child.getData().getId());
            }
        }
    }

    class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

        ArrayList<DetailResponse.Comment> children;

        CommentAdapter(ArrayList<DetailResponse.Comment> children) {
            this.children = children;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView title, author, subreddit, score, time, comments;
            View padding;

            MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.detail_list_title);
//                subreddit = (TextView) view.findViewById(R.id.detail_item_subreddit_text_view);
                author = (TextView) view.findViewById(R.id.detail_list_author);
                score = (TextView) view.findViewById(R.id.detail_list_score);
                time = (TextView) view.findViewById(R.id.detail_list_time);
                padding =  view.findViewById(R.id.detail_list_pading);
//                comments = (TextView) view.findViewById(R.id.list_item_comments_text_view);
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.comment_list_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            DetailResponse.Comment child = children.get(position);
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            try {
//            Date lastUpdated = new Date(child.getData().getCreatedUtc().longValue()*1000);
//            long now = System.currentTimeMillis();
//            String time = (String) DateUtils.getRelativeTimeSpanString(lastUpdated.getTime(), now, DateUtils.HOUR_IN_MILLIS);
                holder.itemView.setPadding(child.getLevel()*10, 0, 0, 0);
                holder.title.setText(child.getHtmlText() + ":" + String.valueOf(child.getLevel()));
//            holder.subreddit.setText(child.getData().getSubreddit());
                holder.author.setText(child.getAuthor());
                holder.score.setText(String.valueOf(child.getPoints()));
                holder.time.setText(child.getPostedOn());
                holder.padding.setBackgroundColor(color);

//            holder.comments.setText(String.valueOf(child.getData().getNumComments()) + " comments");
//            Glide.with(getActivity()).load(child.getData().getThumbnail()).into(holder.image);

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
