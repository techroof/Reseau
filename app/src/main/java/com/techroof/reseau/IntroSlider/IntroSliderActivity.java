package com.techroof.reseau.IntroSlider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.techroof.reseau.Adapter.IntroSliderPagerAdapter;
import com.techroof.reseau.R;
import com.techroof.reseau.StartActivity;

import io.paperdb.Paper;

public class IntroSliderActivity extends AppCompatActivity {

    private ViewPager mPager;
    public int[] layouts = {R.layout.firstslider, R.layout.second_slide, R.layout.third_slide};
    private IntroSliderPagerAdapter pagerAdapter;
    private LinearLayout dots_Layout;
    private ImageView[] dots;
    private FloatingActionButton nextButton;
    private ShowingFirstIntroSlider sliderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_slider);

        if (Build.VERSION.SDK_INT >= 19) {


            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }

        mPager =findViewById(R.id.slider_viewpager);
        nextButton=findViewById(R.id.home_fab);

        pagerAdapter = new IntroSliderPagerAdapter(layouts, this);
        mPager.setAdapter(pagerAdapter);

        dots_Layout = (LinearLayout) findViewById(R.id.dotsLayout);
        createDots(0);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Paper.book().write("introSlider","yes");

                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position==layouts.length-1){

                    nextButton.setVisibility(View.VISIBLE);

                }else{

                    nextButton.setVisibility(View.INVISIBLE);

                }

              /*  int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    mPager.setCurrentItem(current);
                } else {

                    Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                    startActivity(intent);
                    finish();

                }

               */

            }

            @Override
            public void onPageSelected(int position) {

                createDots(position);
                // changing the next button text 'NEXT' / 'GOT IT'
                if (position == layouts.length - 1) {

                } else {

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void createDots(int poss) {

        if (dots_Layout != null) {

            dots_Layout.removeAllViews();
            dots = new ImageView[layouts.length];
            for (int i = 0; i < layouts.length; i++) {

                dots[i] = new ImageView(this);

                if (i == poss) {

                    dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.indicator_bg));
                } else {

                    dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.indicator_grey_bg));

                }

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(4, 0, 4, 0);
                dots_Layout.addView(dots[i], params);
            }
        }
    }

    private void launchHomeScreen() {
        sliderManager.setFirstTimeLaunch(false);
        startActivity(new Intent(IntroSliderActivity.this, StartActivity.class));
        finish();
    }

    private int getItem(int i) {
        return mPager.getCurrentItem();
    }

}