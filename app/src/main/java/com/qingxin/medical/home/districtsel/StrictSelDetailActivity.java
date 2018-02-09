package com.qingxin.medical.home.districtsel;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.amap.api.location.AMapLocation;
import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.map.GaoDeMapModel;

/**
 * 严选详情界面
 * Date 2018/2/8
 *
 * @author zhikuo
 */
public class StrictSelDetailActivity extends QingXinActivity {

    public static void startSelf(@NonNull Context context, @NonNull StrictSelBean strictSelBean) {
        Intent intent = new Intent(context, StrictSelDetailActivity.class);
        intent.putExtra(STRICTSEL_BEAN, strictSelBean);
        context.startActivity(intent);
    }

    public static final String STRICTSEL_BEAN = "STRICTSEL_BEAN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strictsel_detail);
        TextView nameTv = findViewById(R.id.nameTv);
        TextView locationTv = findViewById(R.id.locationTv);
        TextView countTv = findViewById(R.id.countTv);
        TextView descrTv = findViewById(R.id.descrTv);
        StrictSelBean strictSelBean = (StrictSelBean) getIntent().getSerializableExtra(STRICTSEL_BEAN);
        nameTv.setText(strictSelBean.getName());
        descrTv.setText(strictSelBean.getSummary());
        countTv.setText(String.format("%s 次播放", strictSelBean.getOrder()));
        AMapLocation aMapLocation = getModel(GaoDeMapModel.class).getAMLocation();
        if (null != aMapLocation) {
            if (aMapLocation.getProvince().equals(aMapLocation.getCity())){
                locationTv.setText(String.format("%s",aMapLocation.getProvince()));
            }else {
                locationTv.setText(String.format("%s%s",aMapLocation.getProvince(),aMapLocation.getCity()));
            }
        }

        VideoView videoView = findViewById(R.id.videoView);
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(Uri.parse("http://alcdn.hls.xiaoka.tv/2017427/14b/7b3/Jzq08Sl8BbyELNTo/index.m3u8"));
        videoView.start();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //Toast.makeText(VideoPlayerActivity.this, "播放完成了", Toast.LENGTH_SHORT).show();
            }
        });
    }
}