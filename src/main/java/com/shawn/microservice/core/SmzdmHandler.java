package com.shawn.microservice.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.shawn.microservice.util.HttpClientUtil;
import com.shawn.microservice.util.JsonUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Shawn on 2017/3/9.
 */
public class SmzdmHandler {
    public static int dayOfMonthForToday = -1;
    public static Long lastExecuteTime;
    public static List<Article> articleHotListAll = new ArrayList<Article>();
    public static List<Article> article9kuai9listAll = new ArrayList<Article>();
    public static List<Article> articlejingxuanlistAll = new ArrayList<Article>();

    public static List<Article> getArticle(String name) {
        return articleHotListAll;
        /*if (name.contains("9kuai9")) {
            return article9kuai9listAll;
        } else {
            return articlejingxuanlistAll;
        }*/
    }

    public static void getArticleNow() {
        getArticleHotList(articleHotListAll, 1, 8);
        Collections.sort(articleHotListAll);
    }

    /*public static void getArticleNow() {
        long now = System.currentTimeMillis()/1000;
        if (lastExecuteTime == null) {
            lastExecuteTime = now;
        } else if (now - lastExecuteTime > 60 * 60 * 2) {
            article9kuai9listAll.clear();
            articlejingxuanlistAll.clear();
        }
        //List<Article> article9kuai9listAll = new ArrayList<Article>();
        getArticle9kuai9List(article9kuai9listAll, now, now);
        Collections.sort(article9kuai9listAll);
        getArticlejingxuanList(articlejingxuanlistAll, now, now);
        Collections.sort(articlejingxuanlistAll);
        //System.out.println(alist);

        for (Article article : article9kuai9listAll) {
            System.out.println(article);
            if (article.getPrice() < 7) {
            }
        }
        //http://faxian.smzdm.com/json_more?filter=h3s0t0f0c0&page=2
    }*/

    public static void removeOldData(List<Article> alistAll) {
        Calendar now = Calendar.getInstance();
        int dayOfMonthNow = now.get(Calendar.DAY_OF_MONTH);
        if(dayOfMonthNow != dayOfMonthForToday) {
            dayOfMonthForToday = dayOfMonthNow;
            //清除旧的数据
            alistAll.clear();

        }
    }

    public static void getArticleHotList(List<Article> alistAll, int page, int maxPage) {
        //http://faxian.smzdm.com/9kuai9/json_more?filter=h2s0t0f0&page=3
        if (page > maxPage) {
            return;
        }
        if(page == 1) {
            removeOldData(alistAll);
        }
        String result = HttpClientUtil.doGet("http://faxian.smzdm.com/9kuai9/json_more?filter=h2s0t0f0&page=" + page, "utf-8");
        //System.out.println(result);
        List<Article> alist = JsonUtils.Json2Bean(result, new TypeReference<List<Article>>() {
        });
        if(!Collections.disjoint(alistAll,alist)) {//有重复
            alist.removeAll(alistAll);
            alistAll.addAll(alist);
            return;
        }
        alistAll.addAll(alist);
        getArticleHotList(alistAll, page+1, maxPage);
    }

    public static void getArticlejingxuanList(List<Article> alistAll, long now, long timestamp) {
        String result = HttpClientUtil.doGet("http://www.smzdm.com/json_more?timesort=" + timestamp, "utf-8");
        //System.out.println(result);
        List<Article> alist = JsonUtils.Json2Bean(result, new TypeReference<List<Article>>() {
        });
        if(!Collections.disjoint(alistAll,alist)) {//有重复
            alist.removeAll(alistAll);
            alistAll.addAll(alist);
            return;
        }
        alistAll.addAll(alist);
        String timesortStr = alist.get(alist.size() - 1).getTimesort();
        Long timesort = Long.parseLong(timesortStr);
        if (now - timesort < 24 * 60 * 60) {
            getArticlejingxuanList(alistAll, now, timesort);
        }
    }

    public static void getArticle9kuai9List(List<Article> alistAll, long now, long timestamp) {
        String result = HttpClientUtil.doGet("http://faxian.smzdm.com/9kuai9/json_more?timesort=" + timestamp, "utf-8");
        //System.out.println(result);
        List<Article> alist = JsonUtils.Json2Bean(result, new TypeReference<List<Article>>() {
        });
        if(!Collections.disjoint(alistAll,alist)) {//有重复
            alist.removeAll(alistAll);
            alistAll.addAll(alist);
            return;
        }
        alistAll.addAll(alist);
        String timesortStr = alist.get(alist.size() - 1).getTimesort();
        Long timesort = Long.parseLong(timesortStr);
        if (now - timesort < 24 * 60 * 60) {
            getArticle9kuai9List(alistAll, now, timesort);
        }
    }


    public static class Article implements Comparable<Article>{
        String article_id;
        String article_url;
        String article_title;
        String article_price;
        String article_mall;
        String timesort;
        String article_top_category;
        String article_date;
        String article_comment;
        String article_worthy;
        Float price;

        public String getArticle_id() {
            return article_id;
        }

        public void setArticle_id(String article_id) {
            this.article_id = article_id;
        }

        public String getArticle_url() {
            return article_url;
        }

        public void setArticle_url(String article_url) {
            this.article_url = article_url;
        }

        public String getArticle_title() {
            return article_title;
        }

        public void setArticle_title(String article_title) {
            this.article_title = article_title;
        }

        public String getArticle_price() {
            return article_price;
        }

        public void setArticle_price(String article_price) {
            this.article_price = article_price;
        }

        public String getArticle_mall() {
            return article_mall;
        }

        public void setArticle_mall(String article_mall) {
            this.article_mall = article_mall;
        }

        public String getTimesort() {
            return timesort;
        }

        public void setTimesort(String timesort) {
            this.timesort = timesort;
        }

        public String getArticle_top_category() {
            return article_top_category;
        }

        public void setArticle_top_category(String article_top_category) {
            this.article_top_category = article_top_category;
        }

        public String getArticle_date() {
            return article_date;
        }

        public void setArticle_date(String article_date) {
            this.article_date = article_date;
        }

        public String getArticle_comment() {
            return article_comment;
        }

        public void setArticle_comment(String article_comment) {
            this.article_comment = article_comment;
        }

        public String getArticle_worthy() {
            return article_worthy;
        }

        public void setArticle_worthy(String article_worthy) {
            this.article_worthy = article_worthy;
        }

        public Float getPrice() {
            if (price != null) {
                return price;
            }
            if (article_price != null) {
                Pattern pat = Pattern.compile("(\\d+(\\.\\d+)?)");
                Matcher matcher = pat.matcher(article_price);
                if (matcher.find()) {
                    price = Float.parseFloat(matcher.group(0));
                    return price;
                }
            }
            price = 999999F;
            return price;
        }
        @Override
        public String toString() {
            return article_mall+
                    " | " + article_price +
                    " | " + article_title +
                    " | " + article_url;
        }

        @Override
        public int hashCode() {
            return article_id.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return article_id.equals(((Article)obj).article_id);
        }

        @Override
        public int compareTo(Article o) {
            float result = this.getPrice() - o.getPrice();
            if (result < 0) {
                return -1;
            } else if (result == 0) {
                return 0;
            } else {
                return 1;
            }
        }
    }
}
