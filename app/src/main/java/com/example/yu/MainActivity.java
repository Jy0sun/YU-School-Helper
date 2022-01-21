package com.example.yu;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static int userID;
    Button scheduleButton, boardButton, mapButton, infoButton, courseButton;
    LinearLayout notice;
    ArrayList<String> kingList;
    TextView king;
    Map<String, Integer> findKing = new HashMap<String, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userID = getIntent().getIntExtra("userID", 0);

        king = findViewById(R.id.king);

        kingList = new ArrayList<>();

        scheduleButton = findViewById(R.id.scheduleButton);
        boardButton = findViewById(R.id.boardButton);
        mapButton = findViewById(R.id.mapButton);
        infoButton = findViewById(R.id.infoButton);
        courseButton = findViewById(R.id.courseButton);
        notice = findViewById(R.id.notice);

        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notice.setVisibility(View.GONE);
                scheduleButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                boardButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mapButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                infoButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                courseButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new ScheduleFragment());
                fragmentTransaction.commitNow();
            }
        });

        boardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notice.setVisibility(View.GONE);
                scheduleButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                boardButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                mapButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                infoButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                courseButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                Intent intent = new Intent(MainActivity.this, BoardActivity.class);
                startActivity(intent);
                Intent mainIntent = new Intent(MainActivity.this, BoardActivity.class);
                mainIntent.putExtra("userID", userID);
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notice.setVisibility(View.GONE);
                scheduleButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                boardButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mapButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                infoButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                courseButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new MapFragment());
                fragmentTransaction.commitNow();
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notice.setVisibility(View.GONE);
                scheduleButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                boardButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mapButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                infoButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                courseButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new InfoFragment());
                fragmentTransaction.commitNow();
            }
        });

        courseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notice.setVisibility(View.GONE);
                scheduleButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                boardButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mapButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                infoButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                courseButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new CourseFragment());
                fragmentTransaction.commitNow();
            }
        });
    }

    public void replaceBasket() {
        FragmentTransaction frg = getSupportFragmentManager().beginTransaction();
        BasketFragment basketFragment = new BasketFragment();
        frg.replace(R.id.fragment, basketFragment).commit();
        frg = getSupportFragmentManager().beginTransaction();
        frg.replace(R.id.fragment, basketFragment).commit();
    }

    public void modifyPage() {
        FragmentTransaction frg = getSupportFragmentManager().beginTransaction();
        ModifyFragment modifyFragment = new ModifyFragment();
        frg.replace(R.id.fragment, modifyFragment).commit();
        frg = getSupportFragmentManager().beginTransaction();
        frg.replace(R.id.fragment, modifyFragment).commit();
    }

    protected void onResume() {
        super.onResume();
        GetKing getKing = new GetKing();
        getKing.execute();
    }

    class GetKing extends AsyncTask<String, Void, String> {

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

                for(int i=0; i<jsonArray.length(); i++){
                    jsonObject = jsonArray.getJSONObject(i);

                    String postNum = jsonObject.optString("userID");

                    kingList.add(postNum);
                }

                for(int i=0; i< kingList.size(); i++) {
                    if(findKing.containsKey(kingList.get(i))) {
                        findKing.put(kingList.get(i), findKing.get(kingList.get(i)) + 1);
                    }
                    else findKing.put(kingList.get(i), 1);
                }

                Entry<String, Integer> maxEntry = null;

                Set<Entry<String, Integer>> kings = findKing.entrySet();
                for (Entry<String, Integer> theKing : kings) {
                    if (maxEntry == null || theKing.getValue() > maxEntry.getValue()) {
                        maxEntry = theKing;
                    }
                }

                king.setText(maxEntry.getKey());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String server_url = "http://jyosun.dothome.co.kr/GetKing.php";
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
