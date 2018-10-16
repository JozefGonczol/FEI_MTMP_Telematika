package com.example.dgonc.test;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;

import classes.Result;

public class Animate extends AppCompatActivity {
    ArrayList<Result> results;
    FrameLayout anim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animate);

        Intent intent = getIntent();
        results = (ArrayList<Result>) intent.getExtras().get(MainActivity.EXTRA_MESSAGE);
        anim =(FrameLayout)findViewById(R.id.anim);
        animate(0);

    }

    void animate(final int index){

        Result res0 = results.get(index);
        Result res1 = results.get(index+1);

        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("translationX", (float)res1.getX());
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("translationY", (float)-res1.getY());
        ObjectAnimator translateAnimator = ObjectAnimator.ofPropertyValuesHolder(anim, pvhX, pvhY);

        translateAnimator.setDuration(10);

        translateAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
                if (index + 1 < results.size() - 1) {
                    animate(index+1);
                }
            }
        });
        translateAnimator.start();

//        Path pth = new Path();
//
//        pth.moveTo(10,10);
//        pth.moveTo(20,20);
//        pth.moveTo(30,30);
//
//        ObjectAnimator objAnim = ObjectAnimator.ofFloat(anim, View.X, View.Y, pth);
//
//        objAnim.setDuration(200);
//        objAnim.start();
    }
}
