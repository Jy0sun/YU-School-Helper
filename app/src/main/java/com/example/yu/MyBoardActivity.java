package com.example.yu;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.ArrayList;

public class MyBoardActivity extends AppCompatActivity {

    ListView listView;

    public int userID;
    ArrayList<String> titleList = new ArrayList<>();
    ArrayList<String> searchTitleList = new ArrayList<>();
    ArrayList<String> seqList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        userID = getIntent().getIntExtra("userID", 0);

        listView = findViewById(R.id.boardListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(MyBoardActivity.this, adapterView.getItemAtPosition(i)+ " 클릭", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MyBoardActivity.this, PostDetailActivity.class);
                intent.putExtra("postNumber", seqList.get(i));
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetMyBoard getMyBoard = new GetMyBoard();
        getMyBoard.execute();
    }


    // 게시물 리스트를 읽어오는 함수
    class GetMyBoard extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            titleList.clear();
            seqList.clear();

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");

                for(int i=0;i<jsonArray.length();i++){
                    jsonObject = jsonArray.getJSONObject(i);

                    String title = jsonObject.getString("postTitle");
                    String postNum = jsonObject.optString("postNum");

                    titleList.add(title);
                    seqList.add(postNum);
                }

                ArrayAdapter arrayAdapter = new ArrayAdapter<String>(MyBoardActivity.this, android.R.layout.simple_list_item_1, titleList);
                listView.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String server_url = null;
            try {
                server_url = "http://jyosun.dothome.co.kr/GetMyBoardList.php?userID=" + URLEncoder.encode(MainActivity.userID+"", "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            URL url;
            try {
                url = new URL(server_url);
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