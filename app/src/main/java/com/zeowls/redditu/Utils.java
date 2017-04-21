package com.zeowls.redditu;

import android.content.Context;
import android.text.format.DateUtils;

import java.util.Date;

/**
 * Created by root on 4/20/17.
 */
 
class Utils {
    
    static String formatTime(long time){
        Date lastUpdated = new Date(time * 1000);
        long now = System.currentTimeMillis();
//        if ((now - lastUpdated.getTime()) > (60*60*100)) {
//            return (String) DateUtils.getRelativeTimeSpanString(lastUpdated.getTime(), now, DateUtils.HOUR_IN_MILLIS);
//        }else {
            return (String) DateUtils.getRelativeTimeSpanString(lastUpdated.getTime(), now, DateUtils.SECOND_IN_MILLIS);
//        }
    }
    
    static String formatCommentCount(Context context, int count){
        return String.valueOf(count);
    }
}
