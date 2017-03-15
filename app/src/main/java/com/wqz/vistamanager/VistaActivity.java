package com.wqz.vistamanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wqz.base.BaseActivity;
import com.wqz.pojo.Vista;
import com.wqz.utils.ScreenUtils;
import com.wqz.utils.StatusBarUtils;
import com.wqz.utils.UrlUtils;
import com.wqz.view.TitleBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class VistaActivity extends BaseActivity
{
    TitleBar titleBar;
    Button btnSerach,btnMap;
    WebView wvPano;
    EditText etUrl,etContent,etLat,etLon;
    TextView tvId;
    Vista curr;

    @Override
    protected void onResume()
    {
        super.onResume();

        curr = getBaseApplication().getVista();
        etContent.setText(curr.getContent());
        etUrl.setText(curr.getUrl());
        etLat.setText(curr.getLat().toString());
        etLon.setText(curr.getLon().toString());
        tvId.setText("全景ID号为："+curr.getId());
        wvPano.loadUrl(UrlUtils.HTML_VISTA + "?lat=" + curr.getLat() + "&lon=" + curr.getLon());
    }

    @Override
    protected void onInitUI()
    {
        setContentView(R.layout.activity_vista);
        titleBar = (TitleBar)findViewById(R.id.vista_title_bar);
        setTitleBarParm();
        btnSerach = (Button)findViewById(R.id.btn_vista_serach);
        btnMap = (Button)findViewById(R.id.btn_vista_map);
        wvPano = (WebView)findViewById(R.id.wv_vista_pano);
        initWebView();
        etContent = (EditText)findViewById(R.id.et_vista_content);
        etUrl = (EditText)findViewById(R.id.et_vista_url);
        etLat = (EditText)findViewById(R.id.et_vista_lat);
        etLon = (EditText)findViewById(R.id.et_vista_lon);
        tvId = (TextView)findViewById(R.id.tv_vista_id);
    }

    @Override
    protected void onSetListener()
    {
        btnSerach.setOnClickListener(onClickListener);
        btnMap.setOnClickListener(onClickListener);
    }

    @Override
    protected void onSetDataLogic()
    {

    }

    View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            switch (view.getId())
            {
                case R.id.btn_vista_serach:
                    String sUrl = UrlUtils.HTML_VISTA + "?lat=" +
                            etLat.getText().toString() + "&lon=" + etLon.getText().toString();

                    wvPano.loadUrl(sUrl);
                    break;
                case R.id.btn_vista_map:
                    startActivityForResult(new Intent(
                            VistaActivity.this,LonLatSelectActivity.class), 0);
                    break;
            }
        }
    };

    void initWebView()
    {
        wvPano.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });

        WebSettings settings = wvPano.getSettings();
        settings.setJavaScriptEnabled(true);
    }

    private void setTitleBarParm()
    {
        titleBar.setBackgroundColor(VistaActivity.this.getResources().getColor(R.color.colorTitleBar));
        StatusBarUtils.setWindowStatusBarColor(VistaActivity.this, R.color.colorTitleBar);
        titleBar.setImmersive(false);
        titleBar.setTitle("全景详情");
        titleBar.setTitleColor(Color.WHITE);
        titleBar.setHeight(ScreenUtils.getScreenHeight(VistaActivity.this) / 12);

        titleBar.setActionTextColor(Color.WHITE);
        titleBar.addAction(new TitleBar.TextAction("更新")
        {
            @Override
            public void performAction(View view)
            {
                if( etContent.getText().toString().isEmpty() ||
                        etLat.getText().toString().isEmpty() || etLon.getText().toString().isEmpty())
                {
                    Toast.makeText(VistaActivity.this,"请完成内容填写",Toast.LENGTH_SHORT).show();
                    return;
                }

                OkHttpUtils.post().url(UrlUtils.VISTA_UPDATE)//
                        .addParams("id",curr.getId().toString())
                        .addParams("content",etContent.getText().toString())
                        .addParams("url",etUrl.getText().toString())
                        .addParams("lon",etLon.getText().toString())
                        .addParams("lat",etLat.getText().toString())
                        .addParams("projId",getBaseApplication().getProj().getId().toString())
                        .build()//
                        .execute(new StringCallback()
                        {
                            @Override
                            public void onError(Call call, Exception e, int id)
                            {
                                Toast.makeText(VistaActivity.this, "网络错误",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String response, int id)
                            {
                                Boolean rlt = new Gson().fromJson(response,Boolean.class);
                                Toast.makeText(VistaActivity.this, rlt ? "更改成功" : "更改失败",Toast.LENGTH_SHORT).show();
                                VistaActivity.this.finish();
                            }
                        });
            }
        });

        titleBar.addAction(new TitleBar.TextAction("删除")
        {
            @Override
            public void performAction(View view)
            {
                OkHttpUtils.post().url(UrlUtils.VISTA_DELETE)//
                        .addParams("id",curr.getId().toString())
                        .build()//
                        .execute(new StringCallback()
                        {
                            @Override
                            public void onError(Call call, Exception e, int id)
                            {
                                Toast.makeText(VistaActivity.this, "网络错误",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String response, int id)
                            {
                                Boolean rlt = new Gson().fromJson(response,Boolean.class);
                                Toast.makeText(VistaActivity.this, rlt ? "删除成功" : "删除失败",Toast.LENGTH_SHORT).show();
                                VistaActivity.this.finish();
                            }
                        });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        String sLat = data.getStringExtra("lat");
        String sLon = data.getStringExtra("lon");
        etLat.setText(sLat);
        etLon.setText(sLon);
    }
}
