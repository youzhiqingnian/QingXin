package com.qingxin.medical.app.homepagetask;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.service.entity.Book;
import com.qingxin.medical.service.manager.NetRequestListManager;
import com.qingxin.medical.service.view.BookView;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by user on 2018-01-22.
 */

public class HomePageTaskActivity extends QingXinActivity implements HomePageTaskContract.View{
    private TextView text;
    private Button button;

    public static final String SHOULD_LOAD_DATA_FROM_REPO_KEY = "SHOULD_LOAD_DATA_FROM_REPO_KEY";


    private HomePageTaskPresenter mHomePageTaskPresenter;

    private HomePageTaskContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView)findViewById(R.id.text);
        button = (Button)findViewById(R.id.button);

        if(savedInstanceState == null){
            savedInstanceState = new Bundle();
        }

        mHomePageTaskPresenter = new HomePageTaskPresenter(savedInstanceState,this,this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.getSearchBooks("金瓶梅", "", 0, 1);
            }
        });

    }

    private BookView mBookView = new BookView() {
        @Override
        public void onSuccess(Book mBook) {
            text.setText(mBook.toString());
        }

        @Override
        public void onError(String result) {
            Toast.makeText(HomePageTaskActivity.this,result, Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onDestroy(){
        super.onDestroy();
//        mBookPresenter.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(HomePageTaskContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
        mPresenter.subscribe();
    }


    @Override
    public void onSuccess(Book mBook) {
        text.setText(mBook.toString());
    }

    @Override
    public void onError(String result) {

    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the state so that next time we know if we need to refresh data.
        outState.putBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY, true);
        outState.putString("name","金瓶梅");
        super.onSaveInstanceState(outState);
    }

}
