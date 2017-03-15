package twistedgiraffes.com.goldenapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.UUID;

/**
 * Created by rybailey on 3/8/17.
 */

public class NewsFragment extends Fragment {
    private static final String ARG_NEWS_ID = "news_id";

    private News mNews;
    private TextView mTitle;
    private TextView mDate;
    private TextView mFullStory;

    public static NewsFragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_NEWS_ID, crimeId);

        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called to do initial creation of a fragment.  This is called after
     * {@link #onAttach(Activity)} and before
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * <p>
     * <p>Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point.  If you want to do work once the activity itself is
     * created, see {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>Any restored child fragments will be created before the base
     * <code>Fragment.onCreate</code> method returns.</p>
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID newsId = (UUID) getArguments().getSerializable(ARG_NEWS_ID);
        mNews = NewsList.get(getActivity()).getNews(newsId);
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.news_full_page,container,false);

        mTitle = (TextView) v.findViewById(R.id.full_page_title);
        mDate = (TextView) v.findViewById(R.id.full_page_date);
        mFullStory = (TextView) v.findViewById(R.id.full_page_text);

        mTitle.setText(mNews.getHeadline());
        mDate.setText(mNews.getDate().toString());
        mFullStory.setText(mNews.getFullStory());
        return v;
    }
}
