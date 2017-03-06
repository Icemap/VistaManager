package com.wqz.vistamanager;

import android.graphics.Color;
import android.view.View;
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

public class AddProjActivity extends BaseActivity
{
    TitleBar titleBar;
    EditText etTitle;
    EditText etContent;
    Button btnAdd;

    @Override
    protected void onInitUI()
    {
        setContentView(R.layout.activity_add_proj);
        titleBar = (TitleBar)findViewById(R.id.add_proj_title_bar);
        setTitleBarParm();

        etTitle = (EditText)findViewById(R.id.et_add_proj_title);
        etContent = (EditText)findViewById(R.id.et_add_proj_content);
        btnAdd = (Button)findViewById(R.id.btn_add_proj);
    }

    @Override
    protected void onSetListener()
    {
        btnAdd.setOnClickListener(listener);
    }

    @Override
    protected void onSetDataLogic()
    {

    }

    private void setTitleBarParm()
    {
        titleBar.setBackgroundColor(AddProjActivity.this.getResources().getColor(R.color.colorTitleBar));
        StatusBarUtils.setWindowStatusBarColor(AddProjActivity.this, R.color.colorTitleBar);
        titleBar.setImmersive(false);
        titleBar.setTitle(R.string.proj_add);
        titleBar.setTitleColor(Color.WHITE);
        titleBar.setHeight(ScreenUtils.getScreenHeight(AddProjActivity.this) / 12);
    }

    View.OnClickListener listener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            OkHttpUtils.get().url(UrlUtils.PROJ_CREATE)//
                    .addParams("managerId",getBaseApplication().getManager().getId().toString())
                    .addParams("title",etTitle.getText().toString())
                    .addParams("content",etContent.getText().toString())
                    .build()//
                    .execute(new AddProjActivity.MyStringCallback());
        }
    };

    public class MyStringCallback extends StringCallback
    {
        @Override
        public void onError(Call call, Exception e, int id)
        {
            e.printStackTrace();
        }

        @Override
        public void onResponse(String response, int id)
        {
            Boolean rlt = new Gson().fromJson(response,Boolean.class);
            if(rlt)
            {
                Toast.makeText(AddProjActivity.this,"创建成功",Toast.LENGTH_SHORT).show();
                AddProjActivity.this.finish();
            }
            else
            {
                Toast.makeText(AddProjActivity.this,"创建失败",Toast.LENGTH_SHORT).show();
            }
        }

    }
}
