package com.example.yu;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class ModifyRequest extends StringRequest {

    private static int userID = MainActivity.userID;

    static private String URL;

    static {
        try {
            URL = "http://jyosun.dothome.co.kr/UserModify.php?userID=" + URLEncoder.encode(userID + "", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> parameters;

    public ModifyRequest(String userPW, String userName, String userMajor, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null) ;
        parameters = new HashMap<>();
        parameters.put("userPW", userPW);
        parameters.put("userName", userName);
        parameters.put("userMajor", userMajor);
    }

    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
