package com.kamenov.martin.gosportbg.settings;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kamenov.martin.gosportbg.GoSportApplication;
import com.kamenov.martin.gosportbg.R;
import com.kamenov.martin.gosportbg.base.contracts.BaseContracts;
import com.kamenov.martin.gosportbg.constants.Constants;
import com.kamenov.martin.gosportbg.models.SettingsConfiguration;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements SettingsContracts.ISettingsView, View.OnClickListener {


    private SettingsContracts.ISettingsPresenter mPresenter;
    private View root;
    private Spinner mMapsSpinner;
    private Spinner mColorsSpiner;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_settings, container, false);
        // SettingsConfiguration settingsConfiguration = mPresenter.getSettingsConfiguration();
        root.findViewById(R.id.container).setBackgroundColor(Constants.MAINCOLOR);
        ((TextView)root.findViewById(R.id.header)).setTextColor(Constants.SECONDCOLOR);
        ((TextView)root.findViewById(R.id.theme_header)).setTextColor(Constants.SECONDCOLOR);
        ((TextView)root.findViewById(R.id.map_header)).setTextColor(Constants.SECONDCOLOR);
        mMapsSpinner = root.findViewById(R.id.maps_type);
        mMapsSpinner.setBackgroundColor(Constants.MAINCOLOR);
        mMapsSpinner.setAdapter(getMapTypesAdapter());
        mColorsSpiner = root.findViewById(R.id.theme_colors);
        mColorsSpiner.setBackgroundColor(Constants.MAINCOLOR);
        mColorsSpiner.setAdapter(getColorThemesAdapter());
        root.findViewById(R.id.save_settings_btn).setOnClickListener(this);
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
    public ArrayAdapter<String> getColorThemesAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                mPresenter.getThemes()
        );

        return adapter;
    }

    @Override
    public void stopView() {
        mPresenter.unsubscribe();
        getActivity().finish();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public GoSportApplication getGoSportApplication() {
        return (GoSportApplication)getActivity().getApplication();
    }

    @Override
    public void onClick(View view) {
        String mapType = mMapsSpinner.getSelectedItem().toString();
        int selectedThemeIndex = mColorsSpiner.getSelectedItemPosition();
        Constants.MAINCOLOR = Constants.THEMES[selectedThemeIndex][0];
        Constants.SECONDCOLOR = Constants.THEMES[selectedThemeIndex][1];

        SettingsConfiguration settingsConfiguration = new SettingsConfiguration(mapType, selectedThemeIndex);
        mPresenter.setSettingsConfiguration(settingsConfiguration);
        showMessage("Успешно запазени настройки");
        stopView();
    }
}
