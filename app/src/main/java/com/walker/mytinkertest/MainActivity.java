package com.walker.mytinkertest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final String FILE_END = ".apk";//文件后缀
    private String FILEDIR;//文件路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tvMessage = findViewById(R.id.tvMessage);
        tvMessage.setText("tinker is successful !");
        tvMessage.setText("hello");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "tinker test is successful !", Snackbar.LENGTH_LONG).show();
            }
        });

        FILEDIR = getExternalCacheDir().getAbsolutePath() + "/tinker_patch/";
        //创建路径对应的文件夹
        File file = new File(FILEDIR);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public String getPatchName() {
        return FILEDIR.concat("tinker").concat(FILE_END);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScreenUtils.setBackground(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScreenUtils.setBackground(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            TinkerManager.loadPatch(getPatchName());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
