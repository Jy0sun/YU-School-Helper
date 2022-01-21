package com.example.yu;

import android.content.Context;
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

import java.util.List;

public class BasketCourseListAdapter extends BaseAdapter {

    private Context context;
    private List<Course> courseList;
    private Fragment parent;
    private int userID = MainActivity.userID;
    TextView courseGrade, courseTitle, courseDivide, coursePersonnel, courseRate;
    Button deleteButton;

    public BasketCourseListAdapter(Context context, List<Course> courseList, Fragment parent) {
        this.context = context;
        this.courseList = courseList;
        this.parent = parent;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.basket, null);
        courseGrade =  v.findViewById(R.id.courseGrade);
        courseTitle =  v.findViewById(R.id.courseTitle);
        courseDivide = v.findViewById(R.id.courseDivide);
        coursePersonnel = v.findViewById(R.id.coursePersonnel);
        courseRate = v.findViewById(R.id.courseRate);

        if(courseList.get(i).getCourseGrade().equals("null") || courseList.get(i).getCourseGrade().equals("")) {
            courseGrade.setText("전학년");
        }
        else {
            courseGrade.setText(courseList.get(i).getCourseGrade() + "학년");
        }

        courseTitle.setText(courseList.get(i).getCourseTitle());

        if(courseList.get(i).getCourseDivide().equals("null")) {
            courseDivide.setText("분반없음");
        }
        else {
            courseDivide.setText(courseList.get(i).getCourseDivide());
        }

        if(courseList.get(i).getCoursePersonnel() == 0) {
            coursePersonnel.setText("제한없음");
            courseRate.setText("");
        }
        else {
            coursePersonnel.setText("신청인원 : " + courseList.get(i).getCourseRival() + "/" + courseList.get(i).getCoursePersonnel());
            double rate = (((double)courseList.get(i).getCourseRival() / courseList.get(i).getCoursePersonnel()));
            double finalRate = Math.round(rate*100) / 100.0;
            int personnel = courseList.get(i).getCoursePersonnel() / courseList.get(i).getCoursePersonnel();
            courseRate.setText("경쟁률: " + finalRate + ":" + personnel);
            if (finalRate < personnel) {
                courseRate.setTextColor(parent.getResources().getColor(R.color.colorGreen));
            }
            else if (finalRate == personnel) {
                courseRate.setTextColor(parent.getResources().getColor(R.color.colorAccent));
            }
            else {
                courseRate.setTextColor(parent.getResources().getColor(R.color.colorRed));
            }
        }

        v.setTag(courseList.get(i).getCourseID());

        deleteButton = v.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() { // 강의 삭제
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = (response) -> {
                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                        AlertDialog dialog = builder.setMessage("강의가 삭제되었습니다.").setPositiveButton("확인", null).create();
                        dialog.show();
                        BasketFragment.totalCredit -= courseList.get(i).getCourseCredit();
                        BasketFragment.credit.setText(BasketFragment.totalCredit + "학점");
                        courseList.remove(i);
                        notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                };
                DeleteRequest deleteRequest = new DeleteRequest(userID, courseList.get(i).getCourseID(), responseListener);
                RequestQueue queue = Volley.newRequestQueue(parent.getActivity());
                queue.add(deleteRequest);
            }
        });

        return v;
    }
}
