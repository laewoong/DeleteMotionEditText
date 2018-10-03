package com.laewoong.deletemotionedittext;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    DeleteMotionEditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (DeleteMotionEditText)findViewById(R.id.edittext_delete_motion);
        editText.setOnEndDeleteMotionListener(new DeleteMotionEditText.OnEndDeleteMotionListener() {
            @Override
            public void onEndDeleteMotion() {
                Log.i("fff", "Called End Motion Callback");
            }
        });
    }
}
