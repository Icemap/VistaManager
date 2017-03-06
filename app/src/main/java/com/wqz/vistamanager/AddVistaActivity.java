package com.wqz.vistamanager;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wqz.base.BaseActivity;
import com.wqz.utils.ScreenUtils;
import com.wqz.utils.StatusBarUtils;
import com.wqz.utils.UrlUtils;
import com.wqz.view.TitleBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class AddVistaActivity extends BaseActivity
{
    TitleBar titleBar;
    Button btnSerach;
    WebView wvPano;
    EditText etContent,etUrl,etLat,etLon;

    @Override
    protected void onInitUI()
    {
        setContentView(R.layout.activity_add_vista);
        titleBar = (TitleBar)findViewById(R.id.add_vista_title_bar);
        setTitleBarParm();
        btnSerach = (Button)findViewById(R.id.btn_add_vista_serach);
        wvPano = (WebView)findViewById(R.id.wv_pano);
        initWebView();
        etContent = (EditText)findViewById(R.id.et_add_vista_content);
        etUrl = (EditText)findViewById(R.id.et_add_vista_url);
        etLat = (EditText)findViewById(R.id.et_add_vista_lat);
        etLon = (EditText)findViewById(R.id.et_add_vista_lon);
    }

    @Override
    protected void onSetListener()
    {
        btnSerach.setOnClickListener(onClickListener);
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
            String sUrl = etUrl.getText().toString();
            if(!sUrl.startsWith("http://"))
                etUrl.setText("http://" + sUrl);

            wvPano.loadUrl(etUrl.getText().toString());
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
        titleBar.setBackgroundColor(AddVistaActivity.this.getResources().getColor(R.color.colorTitleBar));
        StatusBarUtils.setWindowStatusBarColor(AddVistaActivity.this, R.color.colorTitleBar);
        titleBar.setImmersive(false);
        titleBar.setTitle("添加全景");
        titleBar.setTitleColor(Color.WHITE);
        titleBar.setHeight(ScreenUtils.getScreenHeight(AddVistaActivity.this) / 12);

        titleBar.setActionTextColor(Color.WHITE);
        titleBar.addAction(new TitleBar.TextAction("确认")
        {
            @Override
            public void performAction(View view)
            {
                if(etUrl.getText().toString().isEmpty() || etContent.getText().toString().isEmpty() ||
                   etLat.getText().toString().isEmpty() || etLon.getText().toString().isEmpty())
                {
                    Toast.makeText(AddVistaActivity.this,"请完成内容填写",Toast.LENGTH_SHORT).show();
                    return;
                }

                OkHttpUtils.post().url(UrlUtils.VISTA_CREATE)//
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
                                Toast.makeText(AddVistaActivity.this, "网络错误",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String response, int id)
                            {
                                Boolean rlt = new Gson().fromJson(response,Boolean.class);
                                Toast.makeText(AddVistaActivity.this, rlt ? "添加成功" : "添加失败",Toast.LENGTH_SHORT).show();
                                AddVistaActivity.this.finish();
                            }
                        });
            }
        });
    }
}
