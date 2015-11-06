package com.kubat.piotr.pogodynka;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * Fragment do wyświetlania informacji o problemach w działaniu aplikacji (np. braku dostępu do internetu)
 */
public class ProblemFragment extends Fragment {

    private String problemDesc;

    private TextView problemTxt;

    private Button btn_retry;

    private OnRetryListener onRetryListener;

    public ProblemFragment() {
        // Required empty public constructor
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        problemDesc = args.getString("msg"); //ustawienie informacji o błędzie

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_problem, container, false);

        problemTxt = (TextView) view.findViewById(R.id.problem_text);
        problemTxt.setText(problemDesc);

        btn_retry = (Button)view.findViewById(R.id.btn_retry);
        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != onRetryListener) {
                    onRetryListener.onRetry();
                }

            }
        });

        return  view;
    }

    public void setOnRetryListener(OnRetryListener onRetryListener) {
        this.onRetryListener = onRetryListener;
    }

    // interfejs pozwalający na wywołanie funkcji ponownego wykonania operacji po pojawieniu się błędu
    public interface OnRetryListener {
        void onRetry();
    }
}
