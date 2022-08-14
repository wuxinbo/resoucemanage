package com.wu.sphinxsearch;

public class NativeLib {

    // Used to load the 'sphinxsearch' library on application startup.
    static {
        System.loadLibrary("sphinxsearch");
    }

    /**
     * A native method that is implemented by the 'sphinxsearch' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    /**
     * 执行查询
     * @param keyword
     * @param index
     */
    public native String query(String keyword,String index);
}