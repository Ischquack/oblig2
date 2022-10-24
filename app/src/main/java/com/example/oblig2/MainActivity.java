package com.example.oblig2;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.oblig2.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText inName;
    EditText inTel;
    EditText inId;
    TextView printContacts;
    DBHandler dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inName = (EditText) findViewById(R.id.name);
        inTel = (EditText) findViewById(R.id.tel);
        inId = (EditText) findViewById(R.id.id);
        printContacts = (TextView) findViewById(R.id.printContacts);
        dbHelper = new DBHandler(this);
        db = dbHelper.getWritableDatabase();
    }


}