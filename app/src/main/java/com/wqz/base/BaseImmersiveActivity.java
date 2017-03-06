package com.wqz.base;

import android.os.Bundle;
import android.view.WindowManager;

/**
 * Created by WangQiZhi on 2016/12/24.
 */

public abstract class BaseImmersiveActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        super.onCreate(savedInstanceState);
    }
}
