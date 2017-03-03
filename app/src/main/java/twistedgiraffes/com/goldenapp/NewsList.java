package twistedgiraffes.com.goldenapp;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by rybailey on 2/27/17.
 */
public class NewsList {
    private static NewsList sNewsList;
    private List<News> mNews;

    public static NewsList get(Context context){
        if (sNewsList == null){
            sNewsList = new NewsList(context);
        }
        return sNewsList;
    }

    private NewsList(Context context){
        mNews = new ArrayList<>();
        News news = new News();
        news.setHeadline("Breaking News: This is a Headline");
        news.setFullStory("Blah Blah Blah. You now can see this. In theory. Hopefully");
        mNews.add(news);
        for (int i = 0; i < 15; i++){
            news = new News();
            news.setHeadline("Headline " + i);
            news.setFullStory("Example Story. Will be more here. Currently no database integration either. I will mostly likely will add a picture also because I can.");
            mNews.add(news);
        }
    }

    public List<News> getNewsList() {
        return mNews;
    }

    public News getNews(UUID id){
        for(News news : mNews){
            if (news.getId().equals(id)) {
                return news;
            }
        }
        return null;
    }

    public void delateNewsItem(int position){
        mNews.remove(position);
    }
}
