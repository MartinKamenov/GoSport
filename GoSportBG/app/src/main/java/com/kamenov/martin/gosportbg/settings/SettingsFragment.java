package com.kamenov.martin.gosportbg.settings;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.kamenov.martin.gosportbg.GoSportApplication;
import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.models.SettingsConfiguration;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements SettingsContracts.ISettingsView {


    private SettingsContracts.ISettingsPresenter mPresenter;
    private View root;
    private Spinner mMapsSpinner;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_settings, container, false);
        SettingsConfiguration settingsConfiguration = mPresenter.getSettingsConfiguration();
        mMapsSpinner = root.findViewById(R.id.maps_type);
        mMapsSpinner.setAdapter(getMapTypesAdapter());
        return root;
    }

    @Override
    public void setPresenter(BaseContracts.Presenter presenter) {
        this.mPresenter = (SettingsContracts.ISettingsPresenter)presenter;
    }

    @Override
    public ArrayAdapter<String> getMapTypesAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                mPresenter.getMapTypes()
        );

        return adapter;
    }

    @Override
    public GoSportApplication getGoSportApplication() {
        return (GoSportApplication)getActivity().getApplication();
    }
}
