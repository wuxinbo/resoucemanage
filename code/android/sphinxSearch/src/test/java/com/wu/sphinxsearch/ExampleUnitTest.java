package com.wu.sphinxsearch;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        NativeLib nativeLib =new NativeLib();
        System.out.println(nativeLib.stringFromJNI());
        //assertEquals(4, 2 + 2);
    }
}