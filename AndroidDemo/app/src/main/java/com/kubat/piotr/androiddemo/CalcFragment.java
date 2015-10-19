package com.kubat.piotr.androiddemo;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CalcFragment extends Fragment implements View.OnClickListener {

    private TextView resultView = null;

    private double _result = 0;

    private String _operation = "";

    private OnCalcListener mCallback;
    private String packageName;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calc, container, false);

        resultView = (TextView) view.findViewById(R.id.resultView);

        Button btn = null;
        for(int i = 0; i < 10; i++) {
            int button_id = getResources().getIdentifier("button_"+i, "id", packageName);
            initializeButton(view, button_id);
        }

        initializeButton(view, R.id.button_back);
        initializeButton(view, R.id.button_plus);
        initializeButton(view, R.id.button_minus);
        initializeButton(view, R.id.button_mul);
        initializeButton(view, R.id.button_div);
        initializeButton(view, R.id.button_dot);
        initializeButton(view, R.id.button_);

        return view;
    }

    private void initializeButton(View view, int button_id) {
        Button btn;
        btn = (Button)view.findViewById(button_id);
        btn.setOnClickListener(this);
    }

    // Działa dla starszych wersji androida
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        if(activity instanceof MenuFragment.OnMenuListener) {
            mCallback = (OnCalcListener) activity;

            packageName = activity.getPackageName();

        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMenuListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void onClick(View view) {

        if(mCallback == null) return;

        Button btn = (Button)view;
        String token = btn.getText().toString();
        String result = mCallback.getResult(token);
        setResult(result);
    }

    private void setResult(String result) {
        resultView.setText(result);
    }

    public interface OnCalcListener {
        String getResult(String token);
    }
}
