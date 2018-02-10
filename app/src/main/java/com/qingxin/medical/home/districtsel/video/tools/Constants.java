package com.qingxin.medical.home.districtsel.video.tools;

import android.os.Environment;

public class Constants {
	public final static String SDPATH = Environment.getExternalStorageDirectory() + "/";
	public final static String APPDIR = "weread";
	public final static String APPPATH = SDPATH + APPDIR;
	public final static String PATH_MEDIA = SDPATH + APPDIR;
	public final static String HEADER_ICON_FILE = SDPATH + "/header.jpg";
	public static final String KEY_SHARE_SDK_SMS = "577c217d41f6";
	public static final String SECRECT_SHARE_SDK_SMS = "effdc6b4f88086b7dc7c86373fa9ff76";
	
	public final static String SUFFIX_IMAGE_GIF = ".gif";

    public static final String FLAG_JUMP_2_ARTICLE_LIST_TYPE = "flag_jump_2_article_list_type";
    
    public static final int FETCH_DATA_PAGE_NUM = 10;
    
    public static final int TYPE_DIALOG_LOGIN = 4;
    public static final int TYPE_DIALOG_REGISTER = 5;
    
    public static final String NO_ACCOUNT_NOTICE = "还没有账号？<font color=red>赶快注册一个!</font>";
    public static final String HAS_ACCOUNT_NOTICE = "已经有账号了，<font color=red>直接登陆</font>";
    
    public static final String CACHE_PREF_NAME = "cache_pref_name";
    public static final String CATEGORY_CACHE_ARTICLE_LIST = "category_cache_article_list_";
    public static final String CATEGORY_CACHE_NOTE_LIST = "category_cache_note_list";
    public static final String CATEGORY_CACHE_FAVORITE_LIST = "category_cache_favorite_list";
    public static final String CATEGORY_CACHE_NOTIFICATION_LIST = "category_cache_notification_list";
    
    public static final int MSG_FIRST_REFRESH = 100;
    public static final int MSG_FIRST_REFRESH_TIME_UP = 101;
    public static final int MSG_PROGRESS_BAR_LOADING = 102;
    public static final int TIME_SPACE_PB_LOADING = 100;
    public static final int TIME_OUT_NETWORK_REQUEST = 3 * 1000;
    
	public static final int USER_TYPE_TOURIST = 1;
	public static final int USER_TYPE_PLAT = 3;
	public static final int USER_TYPE_NORMAL = 2;
	public static final int USER_TYPE_NONE = 4;
	
	public static final String USER_TYPE_PLAT_REGISTER = "register";
	
	public static final int EVENT_TYPE_USER_NORMAL_LOGIN = 100;
	public static final int EVENT_TYPE_USER_NORMAL_REGISTER = 101;
	public static final int EVENT_TYPE_USER_PLAT_LOGIN = 102;
	
	public static final int ACCOUNT_MAX_LENGTH = 24;
	public static final int ACCOUNT_MIN_LENGTH = 3;
	
	public static final int PASSWORD_MAX_LENGTH = 20;
	public static final int PASSWORD_MIN_LENGTH = 6;
	public final static String SHARE_TITLE_CONTENT = " 来自@单读";
	
	public final static float DELTA_MIN_SLIDE_MENU_DRAW_SHDOW = 0.01F;
	public final static float MIN_VALID_X_VALUE_WHEN_DRAW_SHDOW = 0.01F;
	public static final int LIMIT_CELL_PHONE_NUMBER = 11;
	
	public static final String INTENT_PHOTO_PATH = "intent_photo_path";
	public static final String INTENT_CROPPED_IMAGE_BYTE_ARR = "intent_cropped_image_byte_arr";
	public static final int MAX_SIZE_HEADER_ICON_KB = 100;
	
	public static final String[] IMAGE_ARR = {
		"http://static.wezeit.com/wp-content/uploads/Picture/2015-11-11/5642e33ce89b4.jpg",
		"http://static.wezeit.com/wp-content/uploads/Picture/2015-10-13/561c9df12e720.jpg",
        "http://static.wezeit.com/wp-content/uploads/Picture/2015-10-12/561b2fd8910d3.jpg",
        "http://static.wezeit.com/wp-content/uploads/Picture/2015-10-12/561b4a505603e.jpg",
        "http://static.wezeit.com/wp-content/uploads/Picture/2015-10-10/5618bb3b89fc0.jpg",
        "http://static.wezeit.com/wp-content/uploads/Picture/2015-10-12/561b210dbfae5.jpg",
        "http://static.wezeit.com/wp-content/uploads/Picture/2015-10-13/561c7a1bcdcec.jpg",
        "http://static.wezeit.com/wp-content/uploads/Picture/2015-10-12/561b2730c6953.jpg"};
	
	public static final String KEY_ACTION_INTENT = "KEY_ACTION_INTENT";
	public static final int ACTION_INTENT_PREPARE_LOGIN_REGISER = 100;
	
	public static final int ACTION_INTENT_NONE = 0;
	public static final int ACTION_INTENT_ADD_FAVORITE = 1;
	public static final int ACTION_INTENT_COMMENT_DIAN_ZAN = 2;
	public static final int ACTION_INTENT_COMMENT = 3;
	public static final int ACTION_INTENT_COMMENT_REPLAY = 4;
	public static final int ACTION_INTENT_JUST_LOGIN_REGISTER = 5;
	public static final int ACTION_INTENT_FAVORITE_ARTICLE = 6;
	public static final int ACTION_INTENT_MY_COMMENT = 7;
	public static final int ACTION_INTENT_CHANGE_NICKNAME = 8;
	public static final int ACTION_INTENT_SHARE = 11;
	
	
	public static final int ACTION_INTENT_FROM_PERSONAL_PAGE = 1000;
	
	public static final String TYPE_HOME = "0";
	public static final String TYPE_WORDS = "1";
	public static final String TYPE_VIDEO = "2";
	public static final String TYPE_VOICE = "3";
	public static final String TYPE_CALENDAR = "4";
	public static final String TYPE_LEFT_MENU = "100";
	public static final String TYPE_RIGHT_MENU = "101";
	
	public static final String TYPE_COMMENT = "20";
	public static final String TYPE_FAVORITE = "21";
	
	public static final String KEY_FRAGMENT_TYPE = "KEY_FRAGMENT_TYPE";
	
	
	//------------
	public static final String KEY_INTENT_OBJ = "KEY_INTENT_OBJ";
	public static final String KEY_INTENT_TYPE = "KEY_INTENT_TYPE";
	public static final String KEY_INTENT_VALUE = "KEY_INTENT_VALUE";
	public static final String KEY_INTENT_COMMENT_COUNT = "KEY_INTENT_COMMENT_COUNT";
	public static final String KEY_INTENT_LIKE_COUNT = "KEY_INTENT_LIKE_COUNT";
	public static final int CODE_UPDATE_COMMENT_LIKE_COUNT = 200;
	public static final String KEY_INTENT_POST_ID = "post_id";
	public static final String KEY_INTENT_IS_FROM_HOME = "KEY_INTENT_IS_FROM_HOME";
	
	//-----------video------------------
	public static final String URL_VIDEO = "http://static.wezeit.com/o_1a4plr4mf1gqe12eo1hno1en910q9t.mp4";
	public static final String URL_VOICE = "http://static.wezeit.com/ls.mp3";
	
	public static final int MSG_SET_VIDEO_VIEW_TRANSPARENT = 500;
	public static final int MSG_DISMISS_VIDEO_CONTROL_BAR = 501;
	public static final int MSG_CANCEL_VIEW_SELECTED_STATE = 502;
	public static final int DELAY_MSG_DISMISS_VIDEO_CONTROL_BAR = 4000;
	public static final int DELAY_MSG_CANCEL_VIEW_SELECTED_STATE = 500;
	public static final int DELAY_MSG_SET_VIDEO_VIEW_TRANSPARENT = 1000;
	
	public static final String STATUS_SHOW_VIDEO_NORMAL = "0";
	public static final String STATUS_SHOW_VIDEO_HIDE = "1";
	
	public static final String VALUE_TYPE_MSG_COMMENT_MEPO= "1";
	public static final String VALUE_TYPE_MSG_LIKE_MEPO = "2";
	public static final String VALUE_TYPE_MSG_COMMENT_ARTICLE = "3";
	public static final String VALUE_TYPE_MSG_LIKE_ARTICLE = "4";
	public static final String VALUE_TYPE_MSG_LIKE_COMMENT = "5";
	public static final String VALUE_TYPE_REPLAY_COMMENT = "6";
	public static final String VALUE_TYPE_SYSTEM_MSG = "7";
	
	public static final String PATH_VIDEO_SCREENSHOT = APPPATH + "/video_screenshot.jpg";
}
