package com.application.cavalliclub.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

import com.application.cavalliclub.R;
import com.application.cavalliclub.entities.EntityMessages;
import com.application.cavalliclub.entities.EntityMessagesThread;
import com.application.cavalliclub.fragments.abstracts.BaseFragment;
import com.application.cavalliclub.global.WebServiceConstants;
import com.application.cavalliclub.helpers.InternetHelper;
import com.application.cavalliclub.helpers.UIHelper;
import com.application.cavalliclub.ui.adapters.ArrayListAdapter;
import com.application.cavalliclub.ui.binders.ChatItemBinder;
import com.application.cavalliclub.ui.views.AnyEditTextView;
import com.application.cavalliclub.ui.views.AnyTextView;
import com.application.cavalliclub.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ChatFragment extends BaseFragment {
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.lv_chat)
    ListView lvChat;
    Unbinder unbinder;
    @BindView(R.id.txtSendMessage)
    AnyEditTextView txtSendMessage;
    @BindView(R.id.sendBtn)
    ImageView sendBtn;
    private static int ID;
    ArrayList<String> listItems;

    private ArrayList<EntityMessagesThread> messagesThreadCollection;
    private ArrayListAdapter<EntityMessagesThread> adapter;

    private ArrayList<EntityMessagesThread> getMessagesThreadEntities;
    private ArrayList<EntityMessagesThread> userCollection;


    public static ChatFragment newInstance() {
        Bundle args = new Bundle();

        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ChatFragment newInstance(Integer id, EntityMessages entity) {
        Bundle args = new Bundle();
        ID = id;
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ArrayListAdapter<>(getDockActivity(), new ChatItemBinder(getDockActivity(), prefHelper));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMainActivity().hideBottomTab();
        getDockActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getMessagesThread();
    }

    private void getMessagesThread() {
        serviceHelper.enqueueCall(webService.getMessagesThread(ID + ""), WebServiceConstants.GET_MESSAGES_THREADS);
    }

    private void getMessagesThreadAgain() {
        serviceHelper.enqueueCall(webService.getMessagesThread(ID + ""), WebServiceConstants.GET_MESSAGES_THREADS_AGAIN);
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        super.ResponseSuccess(result, Tag);

        switch (Tag) {
            case WebServiceConstants.GET_MESSAGES_THREADS:
                messagesThreadCollection = (ArrayList<EntityMessagesThread>) result;
                if (messagesThreadCollection != null && messagesThreadCollection.size() != 0) {
                    getMyReservationsListData(messagesThreadCollection);
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), "No previous messages available.");
                }
                break;

            case WebServiceConstants.ADD_MESSAGES:
                getMessagesThreadAgain();
                break;

            case WebServiceConstants.GET_MESSAGES_THREADS_AGAIN:
                messagesThreadCollection = (ArrayList<EntityMessagesThread>) result;
                getMyReservationsListData(messagesThreadCollection);
                break;
        }
    }

    private void getMyReservationsListData(ArrayList<EntityMessagesThread> result) {

        userCollection = new ArrayList<>();
        userCollection.addAll(result);
        bindData(userCollection);
    }

    private void bindData(ArrayList<EntityMessagesThread> myMessagesThreadCollection) {

        adapter.clearList();
        lvChat.setAdapter(adapter);
        adapter.addAll(myMessagesThreadCollection);
        adapter.notifyDataSetChanged();


        lvChat.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                lvChat.setSelection(adapter.getCount() - 1);
            }
        });
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.hideTwoTabsLayout();
     //   titleBar.setTitlebarBackgroundColor(R.drawable.title_header);
        titleBar.setSubHeading(getString(R.string.admin));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.sendBtn)
    public void onViewClicked() {
        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
            if (txtSendMessage.getText().length() > 0) {
                UIHelper.hideSoftKeyboard(getDockActivity(), getView());
                String msg = txtSendMessage.getText().toString();
                if (msg.trim().length() > 0) {
                    serviceHelper.enqueueCall(webService.sendMessage(messagesThreadCollection.get(0).getReceiverDetail().getId() + "", txtSendMessage.getText().toString() + "", messagesThreadCollection.get(0).getRequestId() + ""), WebServiceConstants.ADD_MESSAGES);
                    txtSendMessage.getText().clear();
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), "Please write a valid message to proceed.");
                }
            } else {
                UIHelper.showShortToastInCenter(getDockActivity(), "Please write a message to proceed.");
            }
        }
    }
}
