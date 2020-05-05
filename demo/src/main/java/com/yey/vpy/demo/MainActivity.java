package com.yey.vpy.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.yey.viewpagery.ResType;
import com.yey.viewpagery.ViewPagerY;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewPagerY mVpy;
    private ArrayList<ResType> resTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVpy = (ViewPagerY) findViewById(R.id.vpy);
        resTypes = new ArrayList<>();
        resTypes.add(new ResType<String>("https://wx1.sinaimg.cn/mw690/006LHkGply1gaftm9dxcpj30tk0tzwv6.jpg", ResType.Type.URL));
        resTypes.add(new ResType<Integer>(R.mipmap.image1, ResType.Type.IAMG));
        resTypes.add(new ResType<String>("https://wx1.sinaimg.cn/mw690/006LHkGply1gaftm45nvhj334022te83.jpg", ResType.Type.URL));
        resTypes.add(new ResType<Integer>(R.mipmap.image2, ResType.Type.IAMG));
        resTypes.add(new ResType<String>("https://wx3.sinaimg.cn/mw690/006LHkGply1gebsmxnod3j30v80uxh18.jpg", ResType.Type.URL));
        resTypes.add(new ResType<Integer>(R.layout.layout_test_vpy, ResType.Type.LAYOUT));
        // 设置资源集合
        mVpy.setRes(resTypes);
        // 设置界面切换时间
        mVpy.setScrollDuration(200);
    }
}
