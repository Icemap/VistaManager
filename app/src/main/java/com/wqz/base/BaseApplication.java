package com.wqz.base;

import android.app.Application;

import com.wqz.pojo.Manager;
import com.wqz.pojo.Proj;
import com.wqz.pojo.Vista;

/**
 * Created by Wqz on 2016/12/23.
 */

public class BaseApplication extends Application
{
    Manager manager;
    Proj proj;
    Vista vista;

    public Manager getManager()
    {
        return manager;
    }

    public void setManager(Manager manager)
    {
        this.manager = manager;
    }

    public Proj getProj()
    {
        return proj;
    }

    public void setProj(Proj proj)
    {
        this.proj = proj;
    }

    public Vista getVista()
    {
        return vista;
    }

    public void setVista(Vista vista)
    {
        this.vista = vista;
    }
    @Override
    public void onCreate()
    {
        super.onCreate();
        manager = new Manager();
    }
}
