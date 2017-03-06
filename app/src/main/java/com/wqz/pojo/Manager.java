package com.wqz.pojo;

/**
 * Created by WangQiZhi on 2016/12/24.
 */

public class Manager
{
    private Integer id;

    private String name;

    private String username;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name == null ? null : name.trim();
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username == null ? null : username.trim();
    }

    public String toString()
    {
        return "id:"+id+"  name:"+name+"  username:"+username;
    }
}
