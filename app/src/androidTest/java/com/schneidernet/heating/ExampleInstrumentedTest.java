package com.schneidernet.heating;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.schneidernet.heating", appContext.getPackageName());
    }

    @Test
    public void fetchTest() throws JSONException {
        JSONArray json = Fetch.getJSON("/sensor/10-000800355f27/lastvalue?count=10");

        for (int i = 0;i<json.length();i++) {
            JSONObject jsonObject = json.getJSONObject(i);
            System.out.println(jsonObject.getString("Date"));
            System.out.println(jsonObject.getDouble("Value"));
        }
        System.out.println(json);
    }
}
