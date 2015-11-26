package com.piotrkubat.lokalizator.menu;


import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.piotrkubat.lokalizator.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    public static String AVATAR_KEY = "com.piotrkubat.lokalizator.menu.AVATAR_KEY";

    public static String USERNAME_KEY = "com.piotrkubat.lokalizator.menu.USERNAME_KEY";

    private ImageView avatarImageView;

    private TextView usernameTextView;

    private OnOptionClickedListener optionClickedListener;

    private OnPlaceTypeClickedListener placeTypeClickedListener;

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        avatarImageView = (ImageView)view.findViewById(R.id.avatar);
        usernameTextView = (TextView)view.findViewById(R.id.username);



        return view;
    }

    public interface OnOptionClickedListener {
        void optionClicked(int id);
    }

    public interface OnPlaceTypeClickedListener {
        void placeTypeClicked(String type);
    }

}
