package com.example.listycitylab3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements
        AddCityFragment.AddCityDialogListener {

    private ArrayList<City> dataList;
    private ListView cityList;
    private CityArrayAdapter cityAdapter;

    @Override
    public void addCity(City city) {
        cityAdapter.add(city);
        cityAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] cities = {
                "Edmonton", "Vancouver", "Toronto"
        };

        String[] provinces = {"AB","BC","ON"};


        dataList = new ArrayList<>();
        for (int i = 0; i < cities.length; i++) {
            dataList.add(new City(cities[i], provinces[i]));
        }

        cityList = findViewById(R.id.city_list);
        cityAdapter = new CityArrayAdapter(this , dataList);
        cityList.setAdapter(cityAdapter);

        cityList.setOnItemLongClickListener((parent,view,position,id) ->{
            City current = dataList.get(position);

            EditText nameInput = new EditText(this);
            nameInput.setHint("City");
            nameInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            nameInput.setText(current.getName());

            EditText provinceInput = new EditText(this);
            provinceInput.setHint("Province");
            provinceInput.setInputType((InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS));
            provinceInput.setText(current.getProvince());

            LinearLayout container = new LinearLayout(this);
            container.setOrientation(LinearLayout.VERTICAL);
            int padding = (int) (16 * getResources().getDisplayMetrics().density);
            container.setPadding(padding,padding,padding,0);
            container.addView(nameInput,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            container.addView(provinceInput,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            new AlertDialog.Builder(this)
                    .setTitle("Edit City")
                    .setView(container)
                    .setPositiveButton("Save",(dialog, which) -> {
                        String newName = nameInput.getText().toString().trim();
                        String newProv = provinceInput.getText().toString().trim().toUpperCase();

                        if (newName.isEmpty()) newName = current.getName();
                        if (newProv.isEmpty()) newProv = current.getProvince();

                        dataList.set(position, new City(newName,newProv));
                        cityAdapter.notifyDataSetChanged();


                    })
                    .setNegativeButton("Cancel",null)
                    .show();

            return true;

        });

        FloatingActionButton fab = findViewById(R.id.button_add_city);
        fab.setOnClickListener(v -> {
            new AddCityFragment().show(getSupportFragmentManager(), "Add City");
        });
    }


}