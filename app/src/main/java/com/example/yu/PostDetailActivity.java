package com.example.yu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class PostDetailActivity extends AppCompatActivity {

    TextView postTitle, postContent, postTime;
    LinearLayout commentLayout;
    EditText commentText;
    Button registerCommentButton;
    String board_seq = "";
    String userid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        board_seq = getIntent().getStringExtra("postNumber");
        userid = getIntent().getStringExtra("userid");

        postTitle = findViewById(R.id.postTitle);
        postContent = findViewById(R.id.postContent);


        postTime = findViewById(R.id.postTime);

        commentLayout = findViewById(R.id.commentLayout);
        commentText = findViewById(R.id.commentText);
        registerCommentButton = findViewById(R.id.registerCommentButton);

        registerCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String theComment = commentText.getText().toString();

                Response.Listener<String> responseLister = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success) {
                                Toast.makeText(getApplicationContext(), "댓글 등록되었습니다!", Toast.LENGTH_SHORT).show();
                                LoadCmt loadCmt = new LoadCmt();
                                loadCmt.execute(board_seq);
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "댓글 등록 실패!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                AddCommentRequest addCommentRequest = new AddCommentRequest(Integer.parseInt(board_seq), MainActivity.userID, theComment, responseLister);
                RequestQueue queue = Volley.newRequestQueue(PostDetailActivity.this);
                queue.add(addCommentRequest);
            }
        });

        InitData();
    }

    private void InitData(){

        LoadBoard loadBoard = new LoadBoard();
        loadBoard.execute();
    }

    class LoadBoard extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String postCurrTime;
                String postDetailTitle;
                String postDetailContent;
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    postCurrTime = object.optString("postTime");
                    postDetailTitle = object.optString("postTitle");
                    postDetailContent = object.optString("postContent");
                    postTitle.setText(postDetailTitle);
                    postTime.setText(postCurrTime);
                    postContent.setText(postDetailContent);
                    count++;
                }
                LoadCmt loadCmt = new LoadCmt();
                loadCmt.execute();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String target="";
            try {
                target = "http://jyosun.dothome.co.kr/PostDetail.php?postNum=" + URLEncoder.encode(board_seq + "", "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            URL url;
            try {
                url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class LoadCmt extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            commentLayout.removeAllViews();

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                LayoutInflater layoutInflater = LayoutInflater.from(PostDetailActivity.this);
                while (count < jsonArray.length()) {
                    View customView = layoutInflater.inflate(R.layout.comment, null);
                    JSONObject object = jsonArray.getJSONObject(count);

                    String userID= object.optString("userID");
                    String postComment = object.optString("postComment");
                    String commentTime = object.optString("commentTime");

                    ((TextView)customView.findViewById(R.id.commentUserName)).setText(userID);
                    ((TextView)customView.findViewById(R.id.commentContent)).setText(postComment);
                    ((TextView)customView.findViewById(R.id.commentDate)).setText(commentTime);

                    commentLayout.addView(customView);
                    count++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String target="";
            try {
                target = "http://jyosun.dothome.co.kr/CommentList.php?postNum=" + URLEncoder.encode(board_seq + "", "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            URL url;
            try {
                url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}