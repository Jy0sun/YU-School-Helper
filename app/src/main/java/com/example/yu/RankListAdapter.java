package com.example.yu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.List;

public class RankListAdapter extends BaseAdapter {

    private Context context;
    private List<Course> courseList;
    private Fragment parent;

    TextView rankTextView, courseGrade, courseTitle, courseCredit, courseDivide, coursePersonnel, courseProfessor, courseTime;

    public RankListAdapter(Context context, List<Course> courseList, Fragment parent) {
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
        View v = View.inflate(context, R.layout.rank, null);
        rankTextView = v.findViewById(R.id.rankTextView);
        courseGrade = v.findViewById(R.id.courseGrade);
        courseTitle = v.findViewById(R.id.courseTitle);
        courseCredit = v.findViewById(R.id.courseCredit);
        courseDivide = v.findViewById(R.id.courseDivide);
        coursePersonnel = v.findViewById(R.id.coursePersonnel);
        courseProfessor = v.findViewById(R.id.courseProfessor);
        courseTime = v.findViewById(R.id.courseTime);

        rankTextView.setText((i + 1) + "위");
        if (i != 0) {
            rankTextView.setBackgroundColor(parent.getResources().getColor(R.color.colorPrimary));
        }
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
            courseDivide.setText(courseList.get(i).getCourseDivide());
        }

        if(courseList.get(i).getCoursePersonnel() == 0) {
            coursePersonnel.setText("제한없음");
        }
        else {
            coursePersonnel.setText("제한 인원: " + courseList.get(i).getCoursePersonnel() + "명");
        }

        courseProfessor.setText(courseList.get(i).getCourseProfessor());

        courseTime.setText(courseList.get(i).getCourseTime() + "");
        v.setTag(courseList.get(i).getCourseID());
        return v;
    }
}
