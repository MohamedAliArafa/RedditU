package com.zeowls.redditu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 4/16/17.
 */

public class DetailResponse {

    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    class Child {

        @SerializedName("kind")
        @Expose
        private String kind;
        @SerializedName("data")
        @Expose
        private Data_ data;

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public Data_ getData() {
            return data;
        }

        public void setData(Data_ data) {
            this.data = data;
        }
    }

    class Data {

//        @SerializedName("modhash")
//        @Expose
//        private String modhash;
        @SerializedName("children")
        @Expose
        private List<Child> children = new ArrayList<Child>();
        @SerializedName("after")
        @Expose
        private Object after;
        @SerializedName("before")
        @Expose
        private Object before;

//        public String getModhash() {
//            return modhash;
//        }
//
//        public void setModhash(String modhash) {
//            this.modhash = modhash;
//        }

        public List<Child> getChildren() {
            return children;
        }

        public void setChildren(List<Child> children) {
            this.children = children;
        }

        public Object getAfter() {
            return after;
        }

        public void setAfter(Object after) {
            this.after = after;
        }

        public Object getBefore() {
            return before;
        }

        public void setBefore(Object before) {
            this.before = before;
        }
    }

    class Data_ {

        @SerializedName("contest_mode")
        @Expose
        private Boolean contestMode;
        @SerializedName("banned_by")
        @Expose
        private Object bannedBy;
        @SerializedName("media_embed")
        @Expose
        private MediaEmbed mediaEmbed;
        @SerializedName("subreddit")
        @Expose
        private String subreddit;
        @SerializedName("selftext_html")
        @Expose
        private Object selftextHtml;
        @SerializedName("selftext")
        @Expose
        private String selftext;
        @SerializedName("likes")
        @Expose
        private Object likes;
        @SerializedName("suggested_sort")
        @Expose
        private Object suggestedSort;
        @SerializedName("user_reports")
        @Expose
        private List<Object> userReports = new ArrayList<Object>();
        @SerializedName("secure_media")
        @Expose
        private Object secureMedia;
        @SerializedName("saved")
        @Expose
        private Boolean saved;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("gilded")
        @Expose
        private Integer gilded;
        @SerializedName("secure_media_embed")
        @Expose
        private SecureMediaEmbed secureMediaEmbed;
        @SerializedName("clicked")
        @Expose
        private Boolean clicked;
        @SerializedName("score")
        @Expose
        private Integer score;
        @SerializedName("report_reasons")
        @Expose
        private Object reportReasons;
        @SerializedName("author")
        @Expose
        private String author;
        @SerializedName("link_flair_text")
        @Expose
        private Object linkFlairText;
        @SerializedName("subreddit_name_prefixed")
        @Expose
        private String subredditNamePrefixed;
        @SerializedName("approved_by")
        @Expose
        private Object approvedBy;
        @SerializedName("over_18")
        @Expose
        private Boolean over18;
        @SerializedName("domain")
        @Expose
        private String domain;
        @SerializedName("hidden")
        @Expose
        private Boolean hidden;
        @SerializedName("preview")
        @Expose
        private Preview preview;
        @SerializedName("num_comments")
        @Expose
        private Integer numComments;
        @SerializedName("thumbnail")
        @Expose
        private String thumbnail;
        @SerializedName("subreddit_id")
        @Expose
        private String subredditId;
//        @SerializedName("edited")
//        @Expose
//        private Boolean edited;
        @SerializedName("link_flair_css_class")
        @Expose
        private Object linkFlairCssClass;
        @SerializedName("author_flair_css_class")
        @Expose
        private Object authorFlairCssClass;
        @SerializedName("downs")
        @Expose
        private Integer downs;
        @SerializedName("brand_safe")
        @Expose
        private Boolean brandSafe;
        @SerializedName("archived")
        @Expose
        private Boolean archived;
        @SerializedName("removal_reason")
        @Expose
        private Object removalReason;
        @SerializedName("post_hint")
        @Expose
        private String postHint;
        @SerializedName("stickied")
        @Expose
        private Boolean stickied;
        @SerializedName("is_self")
        @Expose
        private Boolean isSelf;
        @SerializedName("hide_score")
        @Expose
        private Boolean hideScore;
        @SerializedName("spoiler")
        @Expose
        private Boolean spoiler;
        @SerializedName("permalink")
        @Expose
        private String permalink;
        @SerializedName("subreddit_type")
        @Expose
        private String subredditType;
        @SerializedName("locked")
        @Expose
        private Boolean locked;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("created")
        @Expose
        private Double created;
        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("author_flair_text")
        @Expose
        private Object authorFlairText;
        @SerializedName("quarantine")
        @Expose
        private Boolean quarantine;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("created_utc")
        @Expose
        private Double createdUtc;
        @SerializedName("ups")
        @Expose
        private Integer ups;
        @SerializedName("media")
        @Expose
        private Object media;
        @SerializedName("upvote_ratio")
        @Expose
        private Double upvoteRatio;
        @SerializedName("mod_reports")
        @Expose
        private List<Object> modReports = new ArrayList<Object>();
        @SerializedName("visited")
        @Expose
        private Boolean visited;
        @SerializedName("num_reports")
        @Expose
        private Object numReports;
        @SerializedName("distinguished")
        @Expose
        private Object distinguished;
        @SerializedName("link_id")
        @Expose
        private String linkId;
        @SerializedName("replies")
        @Expose
        private Object replies;
        @SerializedName("parent_id")
        @Expose
        private String parentId;
        @SerializedName("controversiality")
        @Expose
        private Integer controversiality;
        @SerializedName("body")
        @Expose
        private String body;
        @SerializedName("body_html")
        @Expose
        private String bodyHtml;
        @SerializedName("score_hidden")
        @Expose
        private Boolean scoreHidden;
        @SerializedName("depth")
        @Expose
        private Integer depth;
        @SerializedName("count")
        @Expose
        private Integer count;
        @SerializedName("children")
        @Expose
        private List<String> children = new ArrayList<String>();

        public Boolean getContestMode() {
            return contestMode;
        }

        public void setContestMode(Boolean contestMode) {
            this.contestMode = contestMode;
        }

        public Object getBannedBy() {
            return bannedBy;
        }

        public void setBannedBy(Object bannedBy) {
            this.bannedBy = bannedBy;
        }

        public MediaEmbed getMediaEmbed() {
            return mediaEmbed;
        }

        public void setMediaEmbed(MediaEmbed mediaEmbed) {
            this.mediaEmbed = mediaEmbed;
        }

        public String getSubreddit() {
            return subreddit;
        }

        public void setSubreddit(String subreddit) {
            this.subreddit = subreddit;
        }

        public Object getSelftextHtml() {
            return selftextHtml;
        }

        public void setSelftextHtml(Object selftextHtml) {
            this.selftextHtml = selftextHtml;
        }

        public String getSelftext() {
            return selftext;
        }

        public void setSelftext(String selftext) {
            this.selftext = selftext;
        }

        public Object getLikes() {
            return likes;
        }

        public void setLikes(Object likes) {
            this.likes = likes;
        }

        public Object getSuggestedSort() {
            return suggestedSort;
        }

        public void setSuggestedSort(Object suggestedSort) {
            this.suggestedSort = suggestedSort;
        }

        public List<Object> getUserReports() {
            return userReports;
        }

        public void setUserReports(List<Object> userReports) {
            this.userReports = userReports;
        }

        public Object getSecureMedia() {
            return secureMedia;
        }

        public void setSecureMedia(Object secureMedia) {
            this.secureMedia = secureMedia;
        }

        public Boolean getSaved() {
            return saved;
        }

        public void setSaved(Boolean saved) {
            this.saved = saved;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Integer getGilded() {
            return gilded;
        }

        public void setGilded(Integer gilded) {
            this.gilded = gilded;
        }

        public SecureMediaEmbed getSecureMediaEmbed() {
            return secureMediaEmbed;
        }

        public void setSecureMediaEmbed(SecureMediaEmbed secureMediaEmbed) {
            this.secureMediaEmbed = secureMediaEmbed;
        }

        public Boolean getClicked() {
            return clicked;
        }

        public void setClicked(Boolean clicked) {
            this.clicked = clicked;
        }

        public Integer getScore() {
            return score;
        }

        public void setScore(Integer score) {
            this.score = score;
        }

        public Object getReportReasons() {
            return reportReasons;
        }

        public void setReportReasons(Object reportReasons) {
            this.reportReasons = reportReasons;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public Object getLinkFlairText() {
            return linkFlairText;
        }

        public void setLinkFlairText(Object linkFlairText) {
            this.linkFlairText = linkFlairText;
        }

        public String getSubredditNamePrefixed() {
            return subredditNamePrefixed;
        }

        public void setSubredditNamePrefixed(String subredditNamePrefixed) {
            this.subredditNamePrefixed = subredditNamePrefixed;
        }

        public Object getApprovedBy() {
            return approvedBy;
        }

        public void setApprovedBy(Object approvedBy) {
            this.approvedBy = approvedBy;
        }

        public Boolean getOver18() {
            return over18;
        }

        public void setOver18(Boolean over18) {
            this.over18 = over18;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public Boolean getHidden() {
            return hidden;
        }

        public void setHidden(Boolean hidden) {
            this.hidden = hidden;
        }

        public Preview getPreview() {
            return preview;
        }

        public void setPreview(Preview preview) {
            this.preview = preview;
        }

        public Integer getNumComments() {
            return numComments;
        }

        public void setNumComments(Integer numComments) {
            this.numComments = numComments;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getSubredditId() {
            return subredditId;
        }

        public void setSubredditId(String subredditId) {
            this.subredditId = subredditId;
        }

//        public Boolean getEdited() {
//            return edited;
//        }
//
//        public void setEdited(Boolean edited) {
//            this.edited = edited;
//        }

        public Object getLinkFlairCssClass() {
            return linkFlairCssClass;
        }

        public void setLinkFlairCssClass(Object linkFlairCssClass) {
            this.linkFlairCssClass = linkFlairCssClass;
        }

        public Object getAuthorFlairCssClass() {
            return authorFlairCssClass;
        }

        public void setAuthorFlairCssClass(Object authorFlairCssClass) {
            this.authorFlairCssClass = authorFlairCssClass;
        }

        public Integer getDowns() {
            return downs;
        }

        public void setDowns(Integer downs) {
            this.downs = downs;
        }

        public Boolean getBrandSafe() {
            return brandSafe;
        }

        public void setBrandSafe(Boolean brandSafe) {
            this.brandSafe = brandSafe;
        }

        public Boolean getArchived() {
            return archived;
        }

        public void setArchived(Boolean archived) {
            this.archived = archived;
        }

        public Object getRemovalReason() {
            return removalReason;
        }

        public void setRemovalReason(Object removalReason) {
            this.removalReason = removalReason;
        }

        public String getPostHint() {
            return postHint;
        }

        public void setPostHint(String postHint) {
            this.postHint = postHint;
        }

        public Boolean getStickied() {
            return stickied;
        }

        public void setStickied(Boolean stickied) {
            this.stickied = stickied;
        }

        public Boolean getIsSelf() {
            return isSelf;
        }

        public void setIsSelf(Boolean isSelf) {
            this.isSelf = isSelf;
        }

        public Boolean getHideScore() {
            return hideScore;
        }

        public void setHideScore(Boolean hideScore) {
            this.hideScore = hideScore;
        }

        public Boolean getSpoiler() {
            return spoiler;
        }

        public void setSpoiler(Boolean spoiler) {
            this.spoiler = spoiler;
        }

        public String getPermalink() {
            return permalink;
        }

        public void setPermalink(String permalink) {
            this.permalink = permalink;
        }

        public String getSubredditType() {
            return subredditType;
        }

        public void setSubredditType(String subredditType) {
            this.subredditType = subredditType;
        }

        public Boolean getLocked() {
            return locked;
        }

        public void setLocked(Boolean locked) {
            this.locked = locked;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getCreated() {
            return created;
        }

        public void setCreated(Double created) {
            this.created = created;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Object getAuthorFlairText() {
            return authorFlairText;
        }

        public void setAuthorFlairText(Object authorFlairText) {
            this.authorFlairText = authorFlairText;
        }

        public Boolean getQuarantine() {
            return quarantine;
        }

        public void setQuarantine(Boolean quarantine) {
            this.quarantine = quarantine;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Double getCreatedUtc() {
            return createdUtc;
        }

        public void setCreatedUtc(Double createdUtc) {
            this.createdUtc = createdUtc;
        }

        public Integer getUps() {
            return ups;
        }

        public void setUps(Integer ups) {
            this.ups = ups;
        }

        public Object getMedia() {
            return media;
        }

        public void setMedia(Object media) {
            this.media = media;
        }

        public Double getUpvoteRatio() {
            return upvoteRatio;
        }

        public void setUpvoteRatio(Double upvoteRatio) {
            this.upvoteRatio = upvoteRatio;
        }

        public List<Object> getModReports() {
            return modReports;
        }

        public void setModReports(List<Object> modReports) {
            this.modReports = modReports;
        }

        public Boolean getVisited() {
            return visited;
        }

        public void setVisited(Boolean visited) {
            this.visited = visited;
        }

        public Object getNumReports() {
            return numReports;
        }

        public void setNumReports(Object numReports) {
            this.numReports = numReports;
        }

        public Object getDistinguished() {
            return distinguished;
        }

        public void setDistinguished(Object distinguished) {
            this.distinguished = distinguished;
        }

        public String getLinkId() {
            return linkId;
        }

        public void setLinkId(String linkId) {
            this.linkId = linkId;
        }

        public Object getReplies() {
            return replies;
        }

        public void setReplies(Object replies) {
            this.replies = replies;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public Integer getControversiality() {
            return controversiality;
        }

        public void setControversiality(Integer controversiality) {
            this.controversiality = controversiality;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getBodyHtml() {
            return bodyHtml;
        }

        public void setBodyHtml(String bodyHtml) {
            this.bodyHtml = bodyHtml;
        }

        public Boolean getScoreHidden() {
            return scoreHidden;
        }

        public void setScoreHidden(Boolean scoreHidden) {
            this.scoreHidden = scoreHidden;
        }

        public Integer getDepth() {
            return depth;
        }

        public void setDepth(Integer depth) {
            this.depth = depth;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public List<String> getChildren() {
            return children;
        }

        public void setChildren(List<String> children) {
            this.children = children;
        }
    }

    class Image {

        @SerializedName("source")
        @Expose
        private Source source;
        @SerializedName("resolutions")
        @Expose
        private List<Resolution> resolutions = new ArrayList<Resolution>();
        @SerializedName("variants")
        @Expose
        private Variants variants;
        @SerializedName("id")
        @Expose
        private String id;

        public Source getSource() {
            return source;
        }

        public void setSource(Source source) {
            this.source = source;
        }

        public List<Resolution> getResolutions() {
            return resolutions;
        }

        public void setResolutions(List<Resolution> resolutions) {
            this.resolutions = resolutions;
        }

        public Variants getVariants() {
            return variants;
        }

        public void setVariants(Variants variants) {
            this.variants = variants;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    class Preview {

        @SerializedName("images")
        @Expose
        private List<Image> images = new ArrayList<Image>();
        @SerializedName("enabled")
        @Expose
        private Boolean enabled;

        public List<Image> getImages() {
            return images;
        }

        public void setImages(List<Image> images) {
            this.images = images;
        }

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }
    }

    class Resolution {

        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("width")
        @Expose
        private Integer width;
        @SerializedName("height")
        @Expose
        private Integer height;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }
    }

    class Source {

        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("width")
        @Expose
        private Integer width;
        @SerializedName("height")
        @Expose
        private Integer height;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

    }

//    static class RepliesDeserializer implements JsonDeserializer<DetailResponse> {
//        @Override
//        public DetailResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//            DetailResponse data = new Gson().fromJson(json, DetailResponse.class);
//            JsonArray jsonArray = json.getAsJsonObject().get("data").getAsJsonObject().get("children").getAsJsonArray();
//            for (int i = 0; i < jsonArray.size(); i++) {
//                JsonElement jsonElement = jsonArray.get(i);
//                JsonObject jsonObject = jsonElement.getAsJsonObject();
//                Gson gson = new Gson();
//                if (jsonObject.get("data").getAsJsonObject().has("replies")) {
//                    JsonElement elem = jsonObject.get("replies");
//                    if (elem != null && !elem.isJsonNull()) {
//                        if (elem.isJsonPrimitive()) {
//                            data.getData().getChildren().get(i).getData().setReplies(null);
//                        } else {
//                            data.getData().getChildren().get(i).getData().setReplies(gson.fromJson(elem.getAsJsonObject().get("replies")
//                                    .getAsJsonObject(), DetailResponse.class));
//                        }
//                    }
//                }
//            }
//            return data;
//        }
//    }
}
