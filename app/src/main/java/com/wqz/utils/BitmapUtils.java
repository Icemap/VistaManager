package com.wqz.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import com.wqz.vistamanager.R;

import java.io.InputStream;

/**
 * Created by Administrator on 2017/3/15.
 */

public class BitmapUtils
{
    public static Bitmap getBitmap(Context context)
    {
        return BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_openmap_mark);
    }
}
