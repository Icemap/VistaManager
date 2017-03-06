package com.wqz.vistamanager;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wqz.base.BaseActivity;
import com.wqz.pojo.Proj;
import com.wqz.utils.ScreenUtils;
import com.wqz.utils.StatusBarUtils;
import com.wqz.utils.UrlUtils;
import com.wqz.view.TitleBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class ProjActivity extends BaseActivity
{
    TitleBar titleBar;
    ListView lvProj;
    Proj[] projArray;

    @Override
    protected void onResume()
    {
        super.onResume();

        OkHttpUtils.get().url(UrlUtils.PROJ_SELECT_BY_MANAGERID)//
                .addParams("managerId",getBaseApplication().getManager().getId().toString())
                .build()//
                .execute(new ProjActivity.MyStringCallback());
    }

    @Override
    protected void onInitUI()
    {
        setContentView(R.layout.activity_proj);
        titleBar = (TitleBar)findViewById(R.id.title_bar);
        setTitleBarParm();
        lvProj = (ListView)findViewById(R.id.lv_proj);
    }

    @Override
    protected void onSetListener()
    {

    }

    @Override
    protected void onSetDataLogic()
    {

    }

    private void setTitleBarParm()
    {
        titleBar.setBackgroundColor(ProjActivity.this.getResources().getColor(R.color.colorTitleBar));
        StatusBarUtils.setWindowStatusBarColor(ProjActivity.this, R.color.colorTitleBar);
        titleBar.setImmersive(false);
        titleBar.setTitle(R.string.proj_list);
        titleBar.setTitleColor(Color.WHITE);
        titleBar.setHeight(ScreenUtils.getScreenHeight(ProjActivity.this) / 12);

        titleBar.setActionTextColor(Color.WHITE);
        titleBar.addAction(new TitleBar.TextAction("添加项目") {
            @Override
            public void performAction(View view) {
                startActivity(new Intent(ProjActivity.this,AddProjActivity.class));
            }
        });
    }

    class MyViewBinder implements SimpleAdapter.ViewBinder
    {
        /**
         * view：要绑定数据的视图
         * data：要绑定到视图的数据
         * textRepresentation：一个表示所支持数据的安全的字符串，结果是data.toString()或空字符串，但不能是Null
         * 返回值：如果数据绑定到视图返回真，否则返回假
         */
        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation)
        {
            if((view instanceof TextView)&(data instanceof String))
            {
                TextView tv = (TextView)view;
                String text = (String)data;
                tv.setText(text);
                return true;
            }
            return false;
        }
    }

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
            SimpleAdapter adapter = new SimpleAdapter(
                    ProjActivity.this,
                    getData(response),
                    R.layout.proj_adapter,
                    new String[] {"title","content"},
                    new int[] { R.id.tv_proj_title, R.id.tv_proj_content});
            //setListAdapter(adapter);
            adapter.setViewBinder(new MyViewBinder());
            lvProj.setAdapter(adapter);
            lvProj.setOnItemClickListener(mItemClickListener);
            titleBar.setTitle("项目列表\n" + getBaseApplication().getManager().getName());
        }

    }

    private List<Map<String, String>> getData(String response)
    {
        projArray = new Gson().fromJson(response,Proj[].class);
        List<Map<String, String>> listMap = new ArrayList<>();
        for(Proj proj : projArray)
        {
            Map<String,String> map = new HashMap<>();
            map.put("title",proj.getTitle());
            map.put("content",proj.getContent());
            listMap.add(map);
        }

        return listMap;
    }

    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
        {
            ProjActivity.this.getBaseApplication().setProj(
                    projArray[position]);
            ProjActivity.this.startActivity(new Intent(
                    ProjActivity.this, VistaListActivity.class));
        }
    };
}
