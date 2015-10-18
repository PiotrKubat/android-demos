package com.kubat.piotr.androiddemo;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    private OnMenuListener mCallback = null;

    private Button _calcBtn = null;

    private Button _calc2Btn = null;

    private Button _aboutBtn = null;

    private Button _closeBtn = null;

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        _aboutBtn = (Button)view.findViewById(R.id.button_about);
        _calcBtn = (Button)view.findViewById(R.id.button_calc);
        _calc2Btn = (Button)view.findViewById(R.id.button_calc2);
        _closeBtn = (Button)view.findViewById(R.id.button_close);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCallback != null) {
                    mCallback.onMenuSelected(view.getId());
                }
            }
        };

        _aboutBtn.setOnClickListener(clickListener);
        _calcBtn.setOnClickListener(clickListener);
        _calc2Btn.setOnClickListener(clickListener);
        _closeBtn.setOnClickListener(clickListener);

        return view;
    }

    // działa dla nowszych wersji androida
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        Activity activity;
        if(context instanceof Activity && context instanceof MenuFragment.OnMenuListener) {
            activity = (Activity) context;
            mCallback = (OnMenuListener) activity;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement OnMenuListener");
        }
    }


    // Działa dla starszych wersji androida
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        if(activity instanceof MenuFragment.OnMenuListener) {
            mCallback = (OnMenuListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMenuListener");
        }
    }


    public interface OnMenuListener {

        void onMenuSelected(int menu_id);

    }
}
