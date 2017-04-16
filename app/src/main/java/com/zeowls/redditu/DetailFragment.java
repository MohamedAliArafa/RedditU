package com.zeowls.redditu;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    ArrayList<DetailResponse.Child> children = new ArrayList<>();
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



        RedditApiEndpointInterface apiEndpointInterface = ApiCalls.getClient()
                .create(RedditApiEndpointInterface.class);

        Call<List<DetailResponse>> call = apiEndpointInterface.getDetails("KendrickLamar/comments/65n3lr/if_kendrick_drops_a_new_album_tomorrow_ill_donate");
        call.enqueue(new Callback<List<DetailResponse>>() {
            @Override
            public void onResponse(Call<List<DetailResponse>> call, Response<List<DetailResponse>> response) {
                for (DetailResponse child : response.body()) {
                    data.add(child);
                }
                for (DetailResponse.Child comment : data.get(1).getData().getChildren()){
                    children.add(comment);
                    if (comment.getData().getReplies() != null && !comment.getData().getReplies().toString().isEmpty()) {
                        subReplies(comment.getData().getReplies().toString(), comment.getData().getId());
                    }
                }
                mCommentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<DetailResponse>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void subReplies (String json, String id){
        Log.d("ID:", id);
        DetailResponse subReply = gson.fromJson(json, DetailResponse.class);
        for (DetailResponse.Child child :subReply.getData().getChildren()){
            Log.d("Replies " + id, child.getData().getReplies().toString());
            if (child.getData().getReplies() != null && !child.getData().getReplies().toString().isEmpty()){
                subReplies(child.getData().getReplies().toString(), child.getData().getId());
            }
        }
    }

    class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

        ArrayList<DetailResponse.Child> children;

        CommentAdapter(ArrayList<DetailResponse.Child> children) {
            this.children = children;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView title, author, subreddit, score, time, comments;

            MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.detail_list_title);
//                subreddit = (TextView) view.findViewById(R.id.detail_item_subreddit_text_view);
                author = (TextView) view.findViewById(R.id.detail_list_author);
                score = (TextView) view.findViewById(R.id.detail_list_score);
                time = (TextView) view.findViewById(R.id.detail_list_time);
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
            DetailResponse.Child child = children.get(position);

            try {


            Date lastUpdated = new Date(child.getData().getCreatedUtc().longValue()*1000);
            long now = System.currentTimeMillis();
            String time = (String) DateUtils.getRelativeTimeSpanString(lastUpdated.getTime(), now, DateUtils.HOUR_IN_MILLIS);

            holder.title.setText(child.getData().getBody());
//            holder.subreddit.setText(child.getData().getSubreddit());
            holder.author.setText(child.getData().getAuthor());
            holder.score.setText(String.valueOf(child.getData().getScore()));
            holder.time.setText(time);
//            holder.comments.setText(String.valueOf(child.getData().getNumComments()) + " comments");
//            Glide.with(getActivity()).load(child.getData().getThumbnail()).into(holder.image);

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return children.size();
        }
    }

}
