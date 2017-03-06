package com.wqz.vistamanager;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class VistaListActivity extends BaseActivity
{
    TitleBar titleBar;
    ListView lvVista;
    Vista[] vistaList;
    EditText etTitle,etContent;
    Button btnUpdate;

    @Override
    protected void onResume()
    {
        super.onResume();

        OkHttpUtils.get().url(UrlUtils.VISTA_SELECT_BY_PROJID)//
                .addParams("projId",getBaseApplication().getProj().getId().toString())
                .build()//
                .execute(new VistaListActivity.MyStringCallback());
        etTitle.setText(getBaseApplication().getProj().getTitle());
        etContent.setText(getBaseApplication().getProj().getContent());
    }

    @Override
    protected void onInitUI()
    {
        setContentView(R.layout.activity_vista_list);
        titleBar = (TitleBar)findViewById(R.id.vista_title_bar);
        setTitleBarParm();
        lvVista = (ListView)findViewById(R.id.lv_vista);
        etTitle = (EditText)findViewById(R.id.et_vista_title);
        etContent = (EditText)findViewById(R.id.et_vista_content);
        btnUpdate = (Button)findViewById(R.id.btn_update);
    }

    @Override
    protected void onSetListener()
    {
        btnUpdate.setOnClickListener(onClickListener);
    }

    @Override
    protected void onSetDataLogic()
    {

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
                    VistaListActivity.this,
                    getData(response),
                    R.layout.proj_adapter,
                    new String[] {"content", "url"},
                    new int[] { R.id.tv_proj_title, R.id.tv_proj_content});
            //setListAdapter(adapter);
            adapter.setViewBinder(new VistaListActivity.MyViewBinder());
            lvVista.setAdapter(adapter);
            lvVista.setOnItemClickListener(mItemClickListener);
            titleBar.setTitle("全景列表\n" + getBaseApplication().getProj().getTitle());
        }
    }

    private List<Map<String, String>> getData(String response)
    {
        vistaList = new Gson().fromJson(response, Vista[].class);
        List<Map<String, String>> listMap = new ArrayList<>();
        for(Vista vista : vistaList)
        {
            Map<String,String> map = new HashMap<>();
            map.put("content",vista.getContent());
            map.put("url",UrlUtils.HTML_VISTA + "?lon=" + vista.getLon() + "&lat=" + vista.getLat());
            listMap.add(map);
        }

        return listMap;
    }

    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
        {
            VistaListActivity.this.getBaseApplication().setVista(
                    vistaList[position]);
            VistaListActivity.this.startActivity(new Intent(
                    VistaListActivity.this, VistaActivity.class));
        }
    };

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

    private void setTitleBarParm()
    {
        titleBar.setBackgroundColor(VistaListActivity.this.getResources().getColor(R.color.colorTitleBar));
        StatusBarUtils.setWindowStatusBarColor(VistaListActivity.this, R.color.colorTitleBar);
        titleBar.setImmersive(false);
        titleBar.setTitle(R.string.vista_list);
        titleBar.setTitleColor(Color.WHITE);
        titleBar.setHeight(ScreenUtils.getScreenHeight(VistaListActivity.this) / 12);

        titleBar.setActionTextColor(Color.WHITE);
        titleBar.addAction(new TitleBar.TextAction("添加") {
            @Override
            public void performAction(View view) {
                startActivity(new Intent(VistaListActivity.this,AddVistaActivity.class));
            }
        });

        titleBar.addAction(new TitleBar.TextAction("删除项目") {
            @Override
            public void performAction(View view) {
                OkHttpUtils.post().url(UrlUtils.PROJ_DELETE_BY_ID)//
                        .addParams("id",getBaseApplication().getProj().getId().toString())
                        .build()//
                        .execute(new StringCallback()
                        {
                            @Override
                            public void onError(Call call, Exception e, int id)
                            {
                                Toast.makeText(VistaListActivity.this, "网络错误",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String response, int id)
                            {
                                Boolean rlt = new Gson().fromJson(response,Boolean.class);
                                Toast.makeText(VistaListActivity.this, rlt ? "删除成功" : "删除失败",Toast.LENGTH_SHORT).show();
                                VistaListActivity.this.finish();
                            }
                        });
            }
        });
    }

    private View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            OkHttpUtils.post().url(UrlUtils.PROJ_UPDATE_BY_ID)//
                    .addParams("id",getBaseApplication().getProj().getId().toString())
                    .addParams("title",etTitle.getText().toString())
                    .addParams("content",etContent.getText().toString())
                    .build()//
                    .execute(new StringCallback()
                    {
                        @Override
                        public void onError(Call call, Exception e, int id)
                        {
                            Toast.makeText(VistaListActivity.this, "网络错误",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String response, int id)
                        {
                            Boolean rlt = new Gson().fromJson(response,Boolean.class);
                            Toast.makeText(VistaListActivity.this, rlt ? "更新成功" : "更新失败",Toast.LENGTH_SHORT).show();
                            titleBar.setTitle("全景列表\n" + etTitle.getText().toString());
                        }
                    });
        }
    };
}
