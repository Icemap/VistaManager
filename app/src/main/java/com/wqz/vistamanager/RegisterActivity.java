package com.wqz.vistamanager;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wqz.base.BaseImmersiveActivity;
import com.wqz.utils.UrlUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class RegisterActivity extends BaseImmersiveActivity
{
    EditText etNickName;
    EditText etUserName;
    EditText etPassword;
    Button btnConfirm;
    RelativeLayout rootView;

    @Override
    protected void onInitUI()
    {
        setContentView(R.layout.activity_register);

        rootView = (RelativeLayout)findViewById(R.id.activity_register);
        etNickName = (EditText)findViewById(R.id.et_register_name);
        etUserName = (EditText)findViewById(R.id.et_register_username);
        etPassword = (EditText)findViewById(R.id.et_register_password);
        btnConfirm = (Button)findViewById(R.id.btn_registe_register);
    }

    @Override
    protected void onSetListener()
    {
        btnConfirm.setOnClickListener(onClickListener);
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
            OkHttpUtils.get().url(UrlUtils.MANAGER_REGISTER)//
                    .addParams("name",etNickName.getText().toString())
                    .addParams("username",etUserName.getText().toString())
                    .addParams("password",etPassword.getText().toString())
                    .build()//
                    .execute(new RegisterActivity.MyStringCallback());
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
            Integer sign = new Gson().fromJson(response,Integer.class);
            switch (sign)
            {
                case -1:
                    Toast.makeText(RegisterActivity.this,"账号已经存在",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                    RegisterActivity.this.finish();
                    break;
            }
        }

    }
}
