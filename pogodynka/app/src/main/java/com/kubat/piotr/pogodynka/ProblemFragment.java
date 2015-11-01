package com.kubat.piotr.pogodynka;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProblemFragment extends Fragment {

    private String problemDesc;

    private TextView problemTxt;

    public ProblemFragment() {
        // Required empty public constructor
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        problemDesc = args.getString("msg");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_problem, container, false);

        problemTxt = (TextView) view.findViewById(R.id.problem_text);
        problemTxt.setText(problemDesc);
        return  view;
    }


}
