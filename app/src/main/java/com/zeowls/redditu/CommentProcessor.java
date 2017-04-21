package com.zeowls.redditu;

import android.content.Context;
import android.util.Log;

import com.zeowls.redditu.DetailResponse.Comment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by root on 4/18/17.
 */

class CommentProcessor {

    // Load various details about the comment
    private Comment loadComment(Context context, JSONObject data, int level) {
        Comment comment = new Comment();
        try {
            comment.htmlText = data.getString(context.getString(R.string.body));
            comment.author = data.getString(context.getString(R.string.author));
            comment.points = (data.getInt(context.getString(R.string.ups))
                    - data.getInt(context.getString(R.string.downs)))
                    + "";
            comment.postedOn = Utils.formatTime((long) data.getDouble(context.getString(R.string.created_utc)));
            comment.level = level;
        } catch (Exception e) {
            Log.d(context.getString(R.string.error), context.getString(R.string.parce_comment_error) + e);
        }
        return comment;
    }

    // This is where the comment is actually loaded
    // For each comment, its replies are recursively loaded
    private void process(Context context, ArrayList<Comment> comments
            , JSONArray c, int level)
            throws Exception {
        for (int i = 0; i < c.length(); i++) {
            if (c.getJSONObject(i).optString(context.getString(R.string.kind)) == null)
                continue;
            if (!c.getJSONObject(i).optString(context.getString(R.string.kind)).equals(context.getString(R.string.t1)))
                continue;
            JSONObject data = c.getJSONObject(i).getJSONObject(context.getString(R.string.data));
            Comment comment = loadComment(context, data, level);
            if (comment.author != null) {
                comments.add(comment);
                addReplies(context, comments, data, level + 1);
            }
        }
    }

    // Add replies to the comments
    private void addReplies(Context context, ArrayList<Comment> comments,
                            JSONObject parent, int level) {
        try {
            if (parent.get(context.getString(R.string.replies)).equals("")) {
                // This means the comment has no replies
                return;
            }
            JSONArray r = parent.getJSONObject(context.getString(R.string.replies))
                    .getJSONObject(context.getString(R.string.data))
                    .getJSONArray(context.getString(R.string.children));
            process(context, comments, r, level);
        } catch (Exception e) {
            Log.d(context.getString(R.string.error), context.getString(R.string.add_replies_log) + e);
        }
    }

    // Load the comments as an ArrayList, so that it can be
    // easily passed to the ArrayAdapter
    ArrayList<Comment> fetchComments(Context context, String raw) {
        ArrayList<Comment> comments = new ArrayList<>();
        try {

            JSONArray r = new JSONArray(raw)
                    .getJSONObject(1)
                    .getJSONObject(context.getString(R.string.data))
                    .getJSONArray("children");

            // All comments at this point are at level 0
            // (i.e., they are not replies)
            process(context, comments, r, 0);

        } catch (Exception e) {
            Log.d(context.getString(R.string.error), context.getString(R.string.could_not_connect) + e);
        }
        return comments;
    }
}
