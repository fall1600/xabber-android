package com.xabber.android.ui;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.xabber.android.data.message.AbstractChat;
import com.xabber.android.ui.adapter.ChatListAdapter;
import com.xabber.androiddev.R;

import java.util.ArrayList;
import java.util.List;

public class RecentChatFragment extends ListFragment {
    private RecentChatFragmentInteractionListener listener;

    public static RecentChatFragment newInstance() {
        return  new RecentChatFragment();
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecentChatFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ChatListAdapter(getActivity()));
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (RecentChatFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement RecentChatFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        ArrayList<AbstractChat> activeChats = ((ChatViewer) getActivity()).getChatViewerAdapter().getActiveChats();
        ((ChatListAdapter) getListAdapter()).updateChats(activeChats);

        if (getListAdapter().isEmpty()) {
            Activity activity = getActivity();
            Toast.makeText(activity, R.string.chat_list_is_empty, Toast.LENGTH_LONG).show();
            activity.finish();
        }

        return inflater.inflate(R.layout.list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        ((ChatViewer)getActivity()).registerRecentChatsList(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        ((ChatViewer)getActivity()).unregisterRecentChatsList(this);
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != listener) {
            listener.onRecentChatSelected((AbstractChat) getListAdapter().getItem(position));
        }
    }

    public interface RecentChatFragmentInteractionListener {
        public void onRecentChatSelected(AbstractChat chat);
    }

    public void updateChats(List<AbstractChat> chats) {
        ((ChatListAdapter) getListAdapter()).updateChats(chats);
    }
}