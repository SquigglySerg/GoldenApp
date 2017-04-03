package twistedgiraffes.com.goldenapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rybailey on 2/27/17.
 */

public class EventFeedFragment extends Fragment implements DataBase.DataBaseChanged{

    private static final String TAG ="EVENT_FEED";
    private static final String LIST_STATE_KEY = "recycler_list_state";

    private RecyclerView mEventFeedRecyclerView;
    private ItemTouchHelper mItemTouchHelper;
    private EventAdapter mAdapter;
    private Callbacks mCallbacks;
    private Parcelable mListState;

    private DataBase mDataBase;

    @Override
    public void itemPosChange(int pos) {
        updateUI(pos);
    }

    public interface Callbacks{
         void onEventSelect(Event event);
    }



    /**
     * Called when a fragment is first attached to its activity.
     * {@link #onCreate(Bundle)} will be called after this.
     *
     * @param activity
     * @deprecated See {@link #onAttach(Context)}.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
        mDataBase.addListener((DataBase.DataBaseChanged) this);
    }

    /**
     * Called when the fragment is no longer attached to its activity.  This
     * is called after {@link #onDestroy()}.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
        mDataBase.clearListeners();
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
        mDataBase = DataBase.get(getContext());
    }


    /**
     * Called to ask the fragment to save its current dynamic state, so it
     * can later be reconstructed in a new instance of its process is
     * restarted.  If a new instance of the fragment later needs to be
     * created, the data you place in the Bundle here will be available
     * in the Bundle given to {@link #onCreate(Bundle)},
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}, and
     * {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>This corresponds to {@link Activity#onSaveInstanceState(Bundle)
     * Activity.onSaveInstanceState(Bundle)} and most of the discussion there
     * applies here as well.  Note however: <em>this method may be called
     * at any time before {@link #onDestroy()}</em>.  There are many situations
     * where a fragment may be mostly torn down (such as when placed on the
     * back stack with no UI showing), but its state will not be saved until
     * its owning activity actually needs to save its state.
     *
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LIST_STATE_KEY, mEventFeedRecyclerView.getLayoutManager().onSaveInstanceState());
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
        View view = inflater.inflate(R.layout.content_event_feed, container, false);

        if (savedInstanceState != null){
            mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
        } else {
            mListState = null;
        }

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.END){
            /**
             * Called when ItemTouchHelper wants to move the dragged item from its old position to
             * the new position.
             * <p>
             * If this method returns true, ItemTouchHelper assumes {@code viewHolder} has been moved
             * to the adapter position of {@code target} ViewHolder
             * ({@link ViewHolder#getAdapterPosition()
             * ViewHolder#getAdapterPosition()}).
             * <p>
             * If you don't support drag & drop, this method will never be called.
             *
             * @param recyclerView The RecyclerView to which ItemTouchHelper is attached to.
             * @param viewHolder   The ViewHolder which is being dragged by the user.
             * @param target       The ViewHolder over which the currently active item is being
             *                     dragged.
             * @return True if the {@code viewHolder} has been moved to the adapter position of
             * {@code target}.
             * @see #onMoved(RecyclerView, ViewHolder, int, ViewHolder, int, int, int)
             */
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            /**
             * Called when a ViewHolder is swiped by the user.
             * <p>
             * If you are returning relative directions ({@link #START} , {@link #END}) from the
             * {@link #getMovementFlags(RecyclerView, ViewHolder)} method, this method
             * will also use relative directions. Otherwise, it will use absolute directions.
             * <p>
             * If you don't support swiping, this method will never be called.
             * <p>
             * ItemTouchHelper will keep a reference to the View until it is detached from
             * RecyclerView.
             * As soon as it is detached, ItemTouchHelper will call
             * {@link #clearView(RecyclerView, ViewHolder)}.
             *
             * @param viewHolder The ViewHolder which has been swiped by the user.
             * @param direction  The direction to which the ViewHolder is swiped. It is one of
             *                   {@link #UP}, {@link #DOWN},
             *                   {@link #LEFT} or {@link #RIGHT}. If your
             *                   {@link #getMovementFlags(RecyclerView, ViewHolder)}
             *                   method
             *                   returned relative flags instead of {@link #LEFT} / {@link #RIGHT};
             *                   `direction` will be relative as well. ({@link #START} or {@link
             *                   #END}).
             */
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                mDataBase.delateEventItem(viewHolder.getAdapterPosition());
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        };
        mItemTouchHelper = new ItemTouchHelper(callback);

        mEventFeedRecyclerView = (RecyclerView) view.findViewById(R.id.content_event_feed_recycler);
        mEventFeedRecyclerView.setHasFixedSize(true);
        mEventFeedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mItemTouchHelper.attachToRecyclerView(mEventFeedRecyclerView);

        updateUI(0);

        return view;
    }

    private void updateUI(int pos){
        if(mAdapter == null) {
            mAdapter = new EventAdapter();
            mEventFeedRecyclerView.setAdapter(mAdapter);
            if (mListState != null){
                mEventFeedRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);
            }
        } else {
            mAdapter.notifyItemChanged(pos);
        }
    }

    /**
     * Called when the Fragment is no longer resumed.  This is generally
     * tied to {@link Activity#onPause() Activity.onPause} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onPause() {
        super.onPause();
    }

    private class EventHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener{
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private TextView mFullStory;
        private Event mEvent;

        public EventHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_event_headline);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_event_date);
            mFullStory = (TextView) itemView.findViewById(R.id.list_item_full_story);
        }

        public void bindEvent(Event event){
            mEvent = event;
            mTitleTextView.setText(mEvent.getTitle());
            mDateTextView.setText(mEvent.getDate());
            mFullStory.setText(mEvent.getDescription());
            updateTextBox();
        }

        private void updateTextBox(){
            if (mEvent != null){
                if (mEvent.getToogle()){
                    mFullStory.setVisibility(View.VISIBLE);
                } else {
                    mFullStory.setVisibility(View.GONE);
                }
            }
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            //mCallbacks.onEventSelect(mEvent);
            mEvent.setToogle(!mEvent.getToogle());
            updateTextBox();
            Log.i(TAG, "Full Text was toogled");
            mAdapter.notifyItemChanged(getAdapterPosition());
        }
    }

    private class EventAdapter extends RecyclerView.Adapter<EventHolder>{

        public EventAdapter(){
        }



        /**
         * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
         * an item.
         * <p>
         * This new ViewHolder should be constructed with a new View that can represent the items
         * of the given type. You can either create a new View manually or inflate it from an XML
         * layout file.
         * <p>
         * The new ViewHolder will be used to display items of the adapter using
         * {@link #onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
         * different items in the data set, it is a good idea to cache references to sub views of
         * the View to avoid unnecessary {@link View#findViewById(int)} calls.
         *
         * @param parent   The ViewGroup into which the new View will be added after it is bound to
         *                 an adapter position.
         * @param viewType The view type of the new View.
         * @return A new ViewHolder that holds a View of the given view type.
         * @see #getItemViewType(int)
         * @see #onBindViewHolder(ViewHolder, int)
         */
        @Override
        public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_event, parent, false);
            return new EventHolder(view);
        }

        /**
         * Called by RecyclerView to display the data at the specified position. This method should
         * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
         * position.
         * <p>
         * Note that unlike {@link ListView}, RecyclerView will not call this method
         * again if the position of the item changes in the data set unless the item itself is
         * invalidated or the new position cannot be determined. For this reason, you should only
         * use the <code>position</code> parameter while acquiring the related data item inside
         * this method and should not keep a copy of it. If you need the position of an item later
         * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
         * have the updated adapter position.
         * <p>
         * Override {@link #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
         * handle efficient partial bind.
         *
         * @param holder   The ViewHolder which should be updated to represent the contents of the
         *                 item at the given position in the data set.
         * @param position The position of the item within the adapter's data set.
         */
        @Override
        public void onBindViewHolder(EventHolder holder, int position) {
            Event event = mDataBase.getEventList().get(position);
            holder.bindEvent(event);
        }

        /**
         * Returns the total number of items in the data set held by the adapter.
         *
         * @return The total number of items in this adapter.
         */
        @Override
        public int getItemCount() {
            return mDataBase.size();
        }


    }


}
