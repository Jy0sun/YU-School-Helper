package com.example.yu;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AddCommentRequest extends StringRequest {
    final static private String URL = "http://jyosun.dothome.co.kr/AddComment.php";
    private Map<String, String> parameters;

    public AddCommentRequest(int postNum, int userID, String postComment, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null) ;
        parameters = new HashMap<>();
        parameters.put("postNum", postNum + "");
        parameters.put("userID", userID + "");
        parameters.put("postComment", postComment);
    }

    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
