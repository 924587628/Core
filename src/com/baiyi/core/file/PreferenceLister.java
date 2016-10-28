package com.baiyi.core.file;

/**
 * 
 * @author tangkun
 *
 */
public abstract interface PreferenceLister
{
  public abstract String Get(String paramString);
  
  public abstract String Get(String paramString1, String paramString2);
  
  public abstract int getInt(String paramString, int paramInt);
  
  public abstract long getLong(String paramString, long paramLong);
  
  public abstract boolean getBoolean(String paramString, boolean paramBoolean);
  
  public abstract void Set(String paramString1, String paramString2);
  
  public abstract void Remove(String paramString);
  
  public abstract void clear();
}

