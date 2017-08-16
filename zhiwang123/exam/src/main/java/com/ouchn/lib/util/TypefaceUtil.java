/*
 * Copyright (c) 2011 NeuLion, Inc. All Rights Reserved.
 */
package com.ouchn.lib.util;

import java.util.Hashtable;

import android.graphics.Typeface;

import com.ouchn.lib.OuchnExamApplication;


public class TypefaceUtil
{
	public static final String MICROSOFT_YAHEI = "microsoftyahei.ttf";
	
    private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();
    
    public static Typeface get(String name)
    {
        synchronized (cache)
        {
            if (!cache.containsKey(name))
            {
                Typeface t = Typeface.createFromAsset(OuchnExamApplication.getAppContext().getAssets(), name);
                cache.put(name, t);
            }
            return cache.get(name);
        }
    }
    
}
