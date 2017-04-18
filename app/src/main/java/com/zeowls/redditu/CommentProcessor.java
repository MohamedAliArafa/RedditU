package com.zeowls.redditu;

import android.text.format.DateUtils;
import android.util.Log;

import com.zeowls.redditu.DetailResponse.Comment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by root on 4/18/17.
 */

class CommentProcessor {

    // Load various details about the comment
    private Comment loadComment(JSONObject data, int level) {
        Comment comment = new Comment();
        try {
            Date lastUpdated = new Date((long)data.getDouble("created_utc") * 1000);
            long now = System.currentTimeMillis();
            String time = (String) DateUtils.getRelativeTimeSpanString(lastUpdated.getTime(), now, DateUtils.HOUR_IN_MILLIS);
            comment.htmlText = data.getString("body");
            comment.author = data.getString("author");
            comment.points = (data.getInt("ups")
                    - data.getInt("downs"))
                    + "";
            comment.postedOn = time;
            comment.level = level;
        } catch (Exception e) {
            Log.d("ERROR", "Unable to parse comment : " + e);
        }
        return comment;
    }

    // This is where the comment is actually loaded
    // For each comment, its replies are recursively loaded
    private void process(ArrayList<Comment> comments
            , JSONArray c, int level)
            throws Exception {
        for (int i = 0; i < c.length(); i++) {
            if (c.getJSONObject(i).optString("kind") == null)
                continue;
            if (c.getJSONObject(i).optString("kind").equals("t1") == false)
                continue;
            JSONObject data = c.getJSONObject(i).getJSONObject("data");
            Comment comment = loadComment(data, level);
            if (comment.author != null) {
                comments.add(comment);
                addReplies(comments, data, level + 1);
            }
        }
    }

    // Add replies to the comments
    private void addReplies(ArrayList<Comment> comments,
                            JSONObject parent, int level) {
        try {
            if (parent.get("replies").equals("")) {
                // This means the comment has no replies
                return;
            }
            JSONArray r = parent.getJSONObject("replies")
                    .getJSONObject("data")
                    .getJSONArray("children");
            process(comments, r, level);
        } catch (Exception e) {
            Log.d("ERROR", "addReplies : " + e);
        }
    }

    // Load the comments as an ArrayList, so that it can be
    // easily passed to the ArrayAdapter
    ArrayList<Comment> fetchComments(String raw) {
        ArrayList<Comment> comments = new ArrayList<>();
        try {

            JSONArray r = new JSONArray(raw)
                    .getJSONObject(1)
                    .getJSONObject("data")
                    .getJSONArray("children");

            // All comments at this point are at level 0
            // (i.e., they are not replies)
            process(comments, r, 0);

        } catch (Exception e) {
            Log.d("ERROR", "Could not connect: " + e);
        }
        return comments;
    }
}
