package com.wqz.vistamanager;

import android.content.Intent;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.wqz.base.BaseActivity;
import com.wqz.utils.ScreenUtils;
import com.wqz.utils.StatusBarUtils;
import com.wqz.view.TitleBar;

public class LonLatSelectActivity extends BaseActivity
{
    TitleBar titleBar;
    MapView mvSelect;
    BaiduMap mBaidumap;
    Marker marker;

    @Override
    protected void onInitUI()
    {
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_lon_lat_select);
        titleBar = (TitleBar)findViewById(R.id.select_lonlat_title_bar);
        setTitleBarParm();
        mvSelect = (MapView)findViewById(R.id.mv_select_lonlat);

        mBaidumap = mvSelect.getMap();

        LatLng cenpt = new LatLng(23.138133,113.280863);
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(18)
                .build();

        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaidumap.setMapStatus(mMapStatusUpdate);

        OverlayOptions options = new MarkerOptions()
                .position(cenpt)  //设置marker的位置
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.icon_openmap_mark))  //设置marker图标
                .zIndex(9)  //设置marker所在层级
                .draggable(true);  //设置手势拖拽
        //将marker添加到地图上
        marker = (Marker) (mBaidumap.addOverlay(options));
    }

    @Override
    protected void onSetListener()
    {
        mBaidumap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener()
        {
            @Override
            public void onTouch(MotionEvent motionEvent)
            {
                LatLng latLng = mBaidumap.getMapStatus().target;
                marker.setPosition(latLng);
            }
        });
    }

    @Override
    protected void onSetDataLogic()
    {

    }

    private void setTitleBarParm()
    {
        titleBar.setBackgroundColor(LonLatSelectActivity.this.getResources().getColor(R.color.colorTitleBar));
        StatusBarUtils.setWindowStatusBarColor(LonLatSelectActivity.this, R.color.colorTitleBar);
        titleBar.setImmersive(false);
        titleBar.setTitle("地图选择经纬度");
        titleBar.setTitleColor(Color.WHITE);
        titleBar.setHeight(ScreenUtils.getScreenHeight(LonLatSelectActivity.this) / 12);

        titleBar.setActionTextColor(Color.WHITE);
        titleBar.addAction(new TitleBar.TextAction("确认")
        {
            @Override
            public void performAction(View view)
            {
                LatLng latLng = mBaidumap.getMapStatus().target;
                Intent in = new Intent();
                in.putExtra("lon",latLng.longitude + "");
                in.putExtra("lat",latLng.latitude + "");
                LonLatSelectActivity.this.setResult(0, in);
                LonLatSelectActivity.this.finish();
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mvSelect.onDestroy();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mvSelect.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mvSelect.onPause();
    }


}
