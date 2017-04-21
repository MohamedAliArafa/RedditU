package com.zeowls.redditu;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    @BindView(R.id.home_recycler_view)
    RecyclerView mHomeRecycler;
    HomeAdapter mHomeAdapter;
    ArrayList<Child> children = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mHomeRecycler.setLayoutManager(mLayoutManager);
        mHomeRecycler.setItemAnimator(new DefaultItemAnimator());
        mHomeAdapter = new HomeAdapter(children);
        mHomeRecycler.setAdapter(mHomeAdapter);

        RedditApiEndpointInterface apiEndpointInterface = ApiCalls.getClient()
                .create(RedditApiEndpointInterface.class);

        Call<MainResponse> call = apiEndpointInterface.getPopular();
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                for (Child child : response.body().getData().getChildren()) {
                    children.add(child);
                    Log.d("Child: ", child.getData().getTitle());
                }
                mHomeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        ArrayList<Child> children;

        HomeAdapter(ArrayList<Child> children) {
            this.children = children;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView title, author, subreddit, score, time, comments;
            ImageView image;

            MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.list_item_title_text_view);
                subreddit = (TextView) view.findViewById(R.id.list_item_subreddit_text_view);
                author = (TextView) view.findViewById(R.id.list_item_author_text_view);
                score = (TextView) view.findViewById(R.id.list_item_score_text_view);
                time = (TextView) view.findViewById(R.id.list_item_time_text_view);
                comments = (TextView) view.findViewById(R.id.list_item_comments_text_view);
                image = (ImageView) view.findViewById(R.id.list_item_main_image_view);
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.main_list_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final Child child = children.get(position);

            Date lastUpdated = new Date(child.getData().getCreatedUtc().longValue() * 1000);
            long now = System.currentTimeMillis();
            final String time = (String) DateUtils.getRelativeTimeSpanString(lastUpdated.getTime(), now, DateUtils.HOUR_IN_MILLIS);

            holder.title.setText(child.getData().getTitle());
            holder.subreddit.setText(child.getData().getSubreddit());
            holder.author.setText(child.getData().getAuthor());
            holder.score.setText(String.valueOf(child.getData().getScore()));
            holder.time.setText(time);
            holder.comments.setText(Utils.formatCommentCount(getActivity(), child.getData().getNumComments()));
            Glide.with(getActivity()).load(child.getData().getThumbnail()).into(holder.image);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.image.setTransitionName("Image_trans" + child.getData().getId());
                holder.title.setTransitionName("Title_trans" + child.getData().getId());
                holder.subreddit.setTransitionName("SubReddit_trans" + child.getData().getId());
                holder.author.setTransitionName("Author_trans" + child.getData().getId());
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetailFragment detailFragment = new DetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Title", child.getData().getTitle());
                    bundle.putString("Subreddit", child.getData().getSubreddit());
                    bundle.putString("Author", child.getData().getAuthor());
                    bundle.putString("Score", child.getData().getScore().toString());
                    bundle.putString("NumComments", Utils.formatCommentCount(getActivity(), child.getData().getNumComments()));
                    bundle.putString("Time", time);
                    bundle.putParcelable("Image", (((GlideBitmapDrawable) holder.image.getDrawable().getCurrent()).getBitmap()));
                    detailFragment.setArguments(bundle);
                    detailFragment.setUrl(child.getData().getPermalink());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        setSharedElementReturnTransition(TransitionInflater.from(
                                getActivity()).inflateTransition(R.transition.change_image_trans));
                        setExitTransition(TransitionInflater.from(
                                getActivity()).inflateTransition(android.R.transition.fade));

                        detailFragment.setSharedElementEnterTransition(TransitionInflater.from(
                                getActivity()).inflateTransition(R.transition.change_image_trans));
                        detailFragment.setEnterTransition(TransitionInflater.from(
                                getActivity()).inflateTransition(android.R.transition.fade));
                        bundle.putString("image_trans", holder.image.getTransitionName());
                        bundle.putString("title_trans", holder.title.getTransitionName());
                        bundle.putString("sub_trans", holder.subreddit.getTransitionName());
                        bundle.putString("author_trans", holder.author.getTransitionName());
                        getFragmentManager().beginTransaction().add(R.id.container, detailFragment)
                                .addSharedElement(holder.image, holder.image.getTransitionName())
                                .addSharedElement(holder.title, holder.title.getTransitionName())
                                .addSharedElement(holder.subreddit, holder.subreddit.getTransitionName())
                                .addSharedElement(holder.author, holder.author.getTransitionName())
                                .addToBackStack(null).commit();
                    } else {
                        getFragmentManager().beginTransaction().replace(R.id.container, detailFragment)
                                .addToBackStack(null).commit();
                    }


                }
            });
        }

        @Override
        public int getItemCount() {
            return children.size();
        }
    }

}
