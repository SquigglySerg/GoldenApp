package twistedgiraffes.com.goldenapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

/**
 * Created by rybailey on 3/8/17.
 */

public class NewsActivity extends AppCompatActivity {
        private static final String EXTRA_NEWS_ID = "com.csci448.rybailey.news_id";

        private ViewPager mViewPager;
        private List<News> mNews;

    public static Intent newIntent(Context packageContext, UUID crimeId){
        Intent intent = new Intent(packageContext, NewsActivity.class);
        intent.putExtra(EXTRA_NEWS_ID, crimeId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_pager);

        UUID newsId = (UUID) getIntent().getSerializableExtra(EXTRA_NEWS_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_news_pager_view_pager);

        mNews = NewsList.get(this).getNewsList();
        FragmentManager fragmentManager = getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                News news = mNews.get(position);
                return NewsFragment.newInstance(news.getId());
            }

            @Override
            public int getCount() {
                return mNews.size();
            }
        });

        for (int i = 0; i < mNews.size(); i++){
            if (mNews.get(i).getId().equals(newsId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
