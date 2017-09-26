package com.mili.dragbutton;

/**
 * Created by Milinda on 7/17/2017.
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.mili.dragdropbutton.DragButton;

public class MainActivity extends AppCompatActivity {
    TextView txt;
    DragButton dragButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt = (TextView) findViewById(R.id.txt);

        DragButton dragButton = (DragButton) findViewById(R.id.dragBtn);
        dragButton.setOnDraggedListener(new DragButton.OnDraggedListener() {
            @Override
            public void onDragged(DragButton view, MotionEvent e) {
                txt.setText("Dragged");
                Log.i("DragBtn", "Dragged");
            }
        });
        dragButton.setOnDragStartedListener(new DragButton.OnDragStartedListener() {
            @Override
            public void onDragStarted(DragButton view, MotionEvent e) {
                txt.setText("Drag started");
                Log.i("DragBtn", "Drag started");
            }
        });
        dragButton.setOnDragEndedListener(new DragButton.OnDragEndedListener() {
            @Override
            public void onDragEnded(DragButton view, MotionEvent e) {
                txt.setText("Drag ended");
                Log.i("DragBtn", "Drag ended");
            }
        });
    }

    public void resetClick(View view) {
        txt.setText("");
    }
}
