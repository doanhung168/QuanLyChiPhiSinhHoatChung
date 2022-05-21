package com.doanhung.spendandcollect.util.event;

public class Event {

    public static final String MESSAGE_EVENT = "MessageEvent";
    public static final String COUNT_DOWN_TIME_EVENT = "CountDownTimeEvent";
    public static final String SIGN_UP_RESULT_EVENT = "SignUpResultEvent";
    public static final String NAVIGATE_TO_SIGN_IN_FRAGMENT_EVENT = "NavigateToSignInFragmentEvent";
    public static final String NAVIGATE_TO_VERIFY_FRAGMENT_EVENT = "NavigateToVerifyFragmentEvent";
    public static final String NAVIGATE_TO_SIGN_UP_FRAGMENT_EVENT = "NavigateToSignUpFragmentEvent";
    public static final String SIGN_IN_RESULT_EVENT = "SignInResultEvent";

    public static final String CREATE_GROUP_RESULT_EVENT = "CreateGroupResultEvent";
    public static final String CREATE_ITEM_RESULT_EVENT = "CreateItemResultEvent";
    public static final String LOADING_EVENT = "LoadingEvent";
    public static final String GET_USER_BY_ID_EVENT = "GetUserByIdEvent";
    public static final String LOADING_STATE_FRIEND = "LoadingStateEvent";
    public static final String FAIL_AUTH_EVENT = "FailAuthEvent";
    public static final String REFRESH_DATA_EVENT = "RefreshDataEvent";

    public static final String GET_AUTH_FOR_SEARCH = "GetAuthForSearch";

    private String name;

    public Event(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
