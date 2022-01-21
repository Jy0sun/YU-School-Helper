package com.example.yu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class PostActivity extends AppCompatActivity {

    final private String TAG = getClass().getSimpleName();

    EditText postTitleText, postContentText;
    Button registerPostButton;

    public int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postTitleText = findViewById(R.id.postTitleText);
        postContentText = findViewById(R.id.postContentText);
        registerPostButton = (Button) findViewById(R.id.registerPostButton);

        userID = getIntent().getIntExtra("userID", 0);

        registerPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String thePostTitle = postTitleText.getText().toString();
                String thePostContent = postContentText.getText().toString();

                Response.Listener<String> responseLister = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success) {
                                Toast.makeText(getApplicationContext(), "게시글 등록되었습니다!", Toast.LENGTH_SHORT).show();
                                Intent mainIntent = new Intent(PostActivity.this, BoardActivity.class);
                                startActivity(mainIntent);
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "게시글 등록 실패!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                AddPostRequest addPostRequest = new AddPostRequest(MainActivity.userID, thePostTitle, thePostContent, responseLister);
                RequestQueue queue = Volley.newRequestQueue(PostActivity.this);
                queue.add(addPostRequest);
            }
        });
    }
}