package com.example.yu;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class CourseListAdapter extends BaseAdapter {

    private Context context;
    private List<Course> courseList;
    private int userID = MainActivity.userID;
    private Schedule schedule = new Schedule();
    private List<Integer> courseIDList;
    public static int totalCredit = 0;
    public int maxCredit = 20;

    TextView courseGrade, courseTitle, courseCredit, courseDivide, coursePersonnel, courseProfessor, courseTime;
    Button addButton;

    public CourseListAdapter(Context context, List<Course> courseList, Fragment parent) {
        this.context = context;
        this.courseList = courseList;
        schedule = new Schedule();
        courseIDList = new ArrayList<Integer>();
        new CourseListAdapter.BackgroundTask().execute();
        totalCredit = 0;
    }

    @Override
    public int getCount() {
        return courseList.size();
    }

    @Override
    public Object getItem(int i) {
        return courseList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.course, null);
        courseGrade = v.findViewById(R.id.courseGrade);
        courseTitle = v.findViewById(R.id.courseTitle);
        courseCredit = v.findViewById(R.id.courseCredit);
        courseDivide = v.findViewById(R.id.courseDivide);
        coursePersonnel = v.findViewById(R.id.coursePersonnel);
        courseProfessor = v.findViewById(R.id.courseProfessor);
        courseTime = v.findViewById(R.id.courseTime);

        if(courseList.get(i).getCourseGrade().equals("null") || courseList.get(i).getCourseGrade().equals("")) {
            courseGrade.setText("전학년");
        }
        else {
            courseGrade.setText(courseList.get(i).getCourseGrade() + "학년");
        }
        courseTitle.setText(courseList.get(i).getCourseTitle());
        courseCredit.setText(courseList.get(i).getCourseCredit() + "학점");

        if(courseList.get(i).getCourseDivide().equals("null")) {
            courseDivide.setText("분반없음");
        }
        else {
            courseDivide.setText(courseList.get(i).getCourseDivide() + "분반");
        }

        if(courseList.get(i).getCoursePersonnel() == 0) {
            coursePersonnel.setText("제한없음");
        }
        else {
            coursePersonnel.setText("제한인원 : " + courseList.get(i).getCoursePersonnel() + "명");
        }

        courseProfessor.setText(courseList.get(i).getCourseProfessor());
        courseTime.setText(courseList.get(i).getCourseTime() + "");

        v.setTag(courseList.get(i).getCourseID());

        addButton = v.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() { // 강의 추가
            @Override
            public void onClick(View v) {
                boolean validate = false;
                validate = schedule.validate(courseList.get(i).getCourseTime());
                if(!alreadyIn(courseIDList, courseList.get(i).getCourseID())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                    AlertDialog dialog = builder.setMessage("이미 추가한 강의입니다!").setPositiveButton("다시 시도", null).create();
                    dialog.show();
                }
                else if (totalCredit + courseList.get(i).getCourseCredit() > maxCredit) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                    AlertDialog dialog = builder.setMessage("20학점을 초과할 수 없습니다!").setPositiveButton("다시 시도", null).create();
                    dialog.show();
                }
                else if (validate == false) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                    AlertDialog dialog = builder.setMessage("시간표가 중복됩니다!").setPositiveButton("다시 시도", null).create();
                    dialog.show();
                }
                else {
                    Response.Listener<String> responseListener = (response) -> {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                                AlertDialog dialog = builder.setMessage("강의가 추가되었습니다.").setPositiveButton("확인", null).create();
                                dialog.show();
                                courseIDList.add(courseList.get(i).getCourseID());
                                schedule.addSchedule(courseList.get(i).getCourseTime());
                                totalCredit += courseList.get(i).getCourseCredit();
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                                AlertDialog dialog = builder.setMessage("강의추가가 실패하였습니다.").setPositiveButton("확인", null).create();
                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    };
                    AddRequest addRequest = new AddRequest(userID, courseList.get(i).getCourseID(), responseListener);
                    RequestQueue queue = Volley.newRequestQueue(parent.getContext());
                    queue.add(addRequest);
                }
                }
        });
        return v;
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            try {
                target = "http://jyosun.dothome.co.kr/ScheduleList.php?userID=" + URLEncoder.encode(userID + "", "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
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

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        @Override
        public void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String courseTime;
                int courseID;
                totalCredit = 0;
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    courseID = object.getInt("courseID");
                    courseTime = object.getString("courseTime");
                    totalCredit += object.getInt("courseCredit");
                    courseIDList.add(courseID);
                    schedule.addSchedule(courseTime);
                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean alreadyIn(List<Integer> courseIDList, int item) {
        for (int i=0; i<courseIDList.size(); i++) {
            if(courseIDList.get(i) == item) {
                return false;
            }
        }
        return true;
    }

}
