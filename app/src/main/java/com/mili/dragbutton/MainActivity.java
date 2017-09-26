package com.mili.dragbutton;

/**
 * Created by Milinda on 7/17/2017.
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mili.dragdropbutton.DragButton;

public class MainActivity extends AppCompatActivity {

    TextView txt;
    Button btn1;
    LinearLayout layout1;

    DragButton dragButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout1 = (LinearLayout) findViewById(R.id.layout1);
        txt = (TextView) findViewById(R.id.txt);
        btn1 = (Button) findViewById(R.id.btn1);

        dragButton = (DragButton) findViewById(R.id.dragLayout);
        dragButton.setOnDraggedListener(new DragButton.OnDraggedListener() {
            @Override
            public void onDragged(DragButton view, MotionEvent e) {
                txt.setText("Dragged");
            }
        });
        dragButton.setOnDragStartedListener(new DragButton.OnDragStartedListener() {
            @Override
            public void onDragStarted(DragButton view, MotionEvent e) {
                txt.setText("Drag started");
            }
        });
        dragButton.setOnDragEndedListener(new DragButton.OnDragEndedListener() {
            @Override
            public void onDragEnded(DragButton view, MotionEvent e) {
                txt.setText("Drag ended");
            }
        });
    }

    public void resetClick(View view) {
        txt.setText("");
    }
}
