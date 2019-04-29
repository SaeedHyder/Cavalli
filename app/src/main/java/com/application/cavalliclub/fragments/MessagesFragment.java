package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.EntityMessages;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.WebServiceConstants;
import com.application.cavalliclub.ui.adapters.ArrayListAdapter;
import com.application.cavalliclub.ui.binders.BinderMessages;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MessagesFragment extends BaseFragment {
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.lv_messages)
    ListView lvMessages;
    Unbinder unbinder;

    private ArrayList<EntityMessages> messagesCollection;
    private ArrayListAdapter<EntityMessages> adapter;

    private ArrayList<EntityMessages> getMessagesEntities;
    private ArrayList<EntityMessages> userCollection;

    public static MessagesFragment newInstance() {
        Bundle args = new Bundle();
        MessagesFragment fragment = new MessagesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            adapter = new ArrayListAdapter<>(getDockActivity(), new BinderMessages(getDockActivity(),prefHelper));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMainActivity().hideBottomTab();
        getMessages();
    }

    private void getMessages() {
            serviceHelper.enqueueCall(webService.getMessages(), WebServiceConstants.GET_MESSAGES);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.GET_MESSAGES:
                messagesCollection = (ArrayList<EntityMessages>) result;
                getMyReservationsListData(messagesCollection);
                break;
        }
    }

    private void getMyReservationsListData(ArrayList<EntityMessages> result) {

        userCollection = new ArrayList<>();
        userCollection.addAll(result);
        bindData(userCollection);

        if (userCollection!=null && userCollection.size()!=0) {
            txtNoData.setVisibility(View.GONE);
            lvMessages.setVisibility(View.VISIBLE);
        } else {
            txtNoData.setVisibility(View.VISIBLE);
            lvMessages.setVisibility(View.GONE);
        }
    }

    private void bindData(ArrayList<EntityMessages> myMessagesCollection) {

        adapter.clearList();
        lvMessages.setAdapter(adapter);
        adapter.addAll(myMessagesCollection);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.hideTwoTabsLayout();
       // titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.setSubHeading(getString(R.string.messages));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
