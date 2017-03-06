package com.wqz.vistamanager;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wqz.base.BaseImmersiveActivity;
import com.wqz.pojo.Manager;
import com.wqz.utils.UrlUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class LoginActivity extends BaseImmersiveActivity
{
    Button btnRegister;
    Button btnLogin;
    EditText etUserName;
    EditText etPassword;
    RelativeLayout rootView;

    @Override
    protected void onInitUI()
    {
        setContentView(R.layout.activity_login);
        rootView = (RelativeLayout)findViewById(R.id.activity_login);
        btnRegister = (Button)findViewById(R.id.btn_register);
        btnLogin = (Button)findViewById(R.id.btn_login);
        etUserName = (EditText)findViewById(R.id.et_login_username);
        etPassword = (EditText)findViewById(R.id.et_login_password);

    }

    @Override
    protected void onSetListener()
    {
        btnRegister.setOnClickListener(onClickListener);
        btnLogin.setOnClickListener(onClickListener);
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
            switch(view.getId())
            {
                case R.id.btn_login:
                    OkHttpUtils.get().url(UrlUtils.MANAGER_LOGIN)//
                            .addParams("username",etUserName.getText().toString())
                            .addParams("password",etPassword.getText().toString())
                            .build()//
                            .execute(new MyStringCallback());
                    break;
                case R.id.btn_register:
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                    overridePendingTransition(R.anim.fade, R.anim.hold);
                    break;
            }
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
            if(response == null || response.equals(""))
            {
                Toast.makeText(LoginActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                return;
            }

//                    Snackbar.make(rootView, "登录成功", Snackbar.LENGTH_SHORT)
//                            .setAction("Undo", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    // Perform anything for the action selected
//                                }
//                            })
//                            .show();

            Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
            //把Manager信息放入Application中
            getBaseApplication().setManager(new Gson().fromJson(response,Manager.class));
            String s = getBaseApplication().getManager().toString();
            startActivity(new Intent(LoginActivity.this,ProjActivity.class));
            overridePendingTransition(R.anim.fade, R.anim.hold);
        }
    }
}
