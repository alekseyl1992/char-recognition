package com.example.alekseyl.charrecognition;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainFragment extends Fragment {

    private PaintView paintView;
    private Button resetTrainingSetButton;
    private Button trainButton;
    private Button clearButton;

    private LinearLayout buttonsLayout;
    private Button numberButtons[] = new Button[10];
    private Button recognizeButton;



    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();
        assert v != null;
        
        paintView = (PaintView) v.findViewById(R.id.paint_view);

        Context context = getActivity();
        // create buttons
        buttonsLayout = (LinearLayout) v.findViewById(R.id.buttons_layout);

        for (int i = 0; i < numberButtons.length; ++i) {
            Button button = new Button(context);
            button.setText(Integer.toString(i));

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    paintView.clear();
                }
            });

            buttonsLayout.addView(button);
            numberButtons[i] = button;
        }

        clearButton = (Button) v.findViewById(R.id.clear_button);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.clear();
            }
        });

        resetTrainingSetButton = (Button) v.findViewById(R.id.reset_training_set_button);
        resetTrainingSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.clear();
            }
        });

        trainButton = (Button) v.findViewById(R.id.train_button);
        trainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.clear();
            }
        });

        recognizeButton = (Button) v.findViewById(R.id.recognize_button);
        recognizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int side = PaintView.imageSideSize;
                boolean pixels[] = paintView.getPixels();

                System.out.println("---------------------");
                for (int i = 0; i < side; ++i) {
                    for (int j = 0; j < side; ++j) {
                        int px = pixels[i * side + j] ? 1 : 0;

                        System.out.print(px);
                        System.out.print(" ");
                    }
                    
                    System.out.println();
                }
                System.out.println("---------------------");
            }
        });
    }
}
