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

public class CalcFragment extends Fragment {

    private TextView resultView = null;

    private List<Button> digitButtons = null;

    private View.OnClickListener digitClicked = null;

    private double _result = 0;

    private String _operation = "";

    private OnCalcListener mCallback;
    private String packageName;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calc, container, false);

        resultView = (TextView) view.findViewById(R.id.resultView);

        digitButtons = new ArrayList<Button>();

        digitClicked = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetError(view.getTag().toString());
            }
        };

        for(int i = 0; i < 10; i++) {
            int id = getResources().getIdentifier("button_"+i, "id", packageName);
            Button btn = (Button)view.findViewById(id);
            btn.setTag(i);
            btn.setOnClickListener(digitClicked);
        }

        return view;
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



    public void digitClicked(View view) {
        Button btn = (Button)view;
        String str_dig = btn.getText().toString();
        SetError(str_dig);
    }

    public void operClicked(View view) {
        Button btn = (Button)view;

    }


    private void SetResult(double result) {
        resultView.setText(String.valueOf(result));
    }

    private void SetError(String error) {
        resultView.setText(error);
    }

    public interface OnCalcListener {

    }
}
