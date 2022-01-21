package com.example.yu;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    final static private String URL = "http://jyosun.dothome.co.kr/UserRegister.php";
    private Map<String, String> parameters;

    public RegisterRequest(int userID, String userPW, String userName, String userMajor, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null) ;
        parameters = new HashMap<>();
        parameters.put("userID", userID + "");
        parameters.put("userPW", userPW);
        parameters.put("userName", userName);
        parameters.put("userMajor", userMajor);
    }

    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
