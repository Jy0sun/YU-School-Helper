package com.example.yu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModifyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModifyFragment extends Fragment {

    private ArrayAdapter adapter;
    private Spinner spinner;
    EditText newPwText, CheckAgainText, nameText;
    Button changeButton;

    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);

        spinner = (Spinner) getView().findViewById(R.id.majorSpinner);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.major, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        newPwText = getView().findViewById(R.id.newPasswordText);
        CheckAgainText = getView().findViewById(R.id.passwordCheckAgainText);
        nameText = getView().findViewById(R.id.nameText);

        changeButton = getView().findViewById(R.id.changeButton);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPW = newPwText.getText().toString();
                String PWCheckAgain = CheckAgainText.getText().toString();
                String userName = nameText.getText().toString();
                String userMajor = spinner.getSelectedItem().toString();

                if((userName.equals("") || userMajor.equals(""))) {
                    Toast.makeText(getActivity(), "빈칸 없이 입력해주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!newPW.equals(PWCheckAgain)) {
                    Toast.makeText(getActivity(), "비밀번호가 서로 다릅니다!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Toast.makeText(getActivity(), "정보변경이 완료하였습니다!", Toast.LENGTH_SHORT).show();
                ModifyRequest modifyRequest = new ModifyRequest(newPW, userName, userMajor, responseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(modifyRequest);
                Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                startActivity(mainIntent);
            }
        });
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ModifyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ModifyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ModifyFragment newInstance(String param1, String param2) {
        ModifyFragment fragment = new ModifyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modify, container, false);
    }
}