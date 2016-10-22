package com.yev.dev.haw_sched2.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;

import com.yev.dev.haw_sched2.R;
import com.yev.dev.haw_sched2.fragments.Fragment_Diagram;
import com.yev.dev.haw_sched2.fragments.Fragment_SubjectsNavigation;
import com.yev.dev.haw_sched2.utils.Utility;
import com.yev.dev.haw_sched2.views.MySlidingPaneLayout;

import java.util.ArrayList;


public class FullScheduleActivity extends Activity implements Fragment_SubjectsNavigation.SubjectsNavigationListener {

    public static final String KEY_TYPE = "type";
    public static final int TYPE_DIAGRAM = 1;
    public static final int TYPE_CALENDAR = 2;
    private int type;

    private MySlidingPaneLayout slider;
    private Utility util = new Utility();

    public interface FullScheduleActivityListener {
        public void setSubjectsList(ArrayList<String> subjects, boolean hideExpired);
    }

    private FullScheduleActivityListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar acb = getActionBar();
        if (acb != null) {
            acb.hide();
        }

        Intent intent = getIntent();
        type = intent.getIntExtra(KEY_TYPE, TYPE_CALENDAR);

        setContentView(R.layout.activity_full_schedule);

        setupViews();

        if(savedInstanceState == null){
            setFragments();
        }
    }

    //SETUP VIEWS
    private void setupViews() {

        slider = (MySlidingPaneLayout) findViewById(R.id.slider);

        slider.setShadowResourceLeft(R.drawable.shadow_left);

        slider.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {

            View navigationPane = findViewById(R.id.frame_navigation);
            View contentPane = findViewById(R.id.container);

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                /*navigationPane.setAlpha(slideOffset);
                navigationPane.setScaleX((float) (0.8 + 0.2 * slideOffset));
                navigationPane.setScaleY((float) (0.8 + 0.2 * slideOffset));

                contentPane.setAlpha((float) (1 - 0.6 * slideOffset));*/

                navigationPane.setX(navigationPane.getWidth() * (slideOffset - 1));
            }

            @Override
            public void onPanelOpened(View panel) {

            }

            @Override
            public void onPanelClosed(View panel) {

            }
        });

    }

    //ON BACK PRESSED
    @Override
    public void onBackPressed() {

        if (slider.isOpen()) {
            slider.closePane();
            return;
        }

        super.onBackPressed();
    }

    private void setFragments(){

        FragmentManager fm = getFragmentManager();

        fm.beginTransaction()
                .replace(R.id.frame_navigation, new Fragment_SubjectsNavigation())
                .commit();

        switch (type){
            case TYPE_CALENDAR:

                break;
            case TYPE_DIAGRAM:
                fm.beginTransaction()
                        .replace(R.id.container, new Fragment_Diagram())
                        .commit();
                break;
        }

    }


    @Override
    public void onSubjectsListChanged(ArrayList<String> subjects, boolean hideExpired) {
        if(listener != null){
            listener.setSubjectsList(subjects, hideExpired);
        }
    }

    @Override
    public void onChangesSaved() {
        setResult(RESULT_OK);
        finish();
    }

    public void setListener(FullScheduleActivityListener listener){
        this.listener = listener;
    }
}