package csc445.missouriwestern.edu.jaunt.fragments.places;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import csc445.missouriwestern.edu.jaunt.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmptyFragment extends Fragment {

    public EmptyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_empty, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
