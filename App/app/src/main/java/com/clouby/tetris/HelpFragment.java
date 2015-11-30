package com.clouby.tetris;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by nboneh on 11/15/2015.
 */
public class HelpFragment extends Fragment {

    private RadioGroup radioGroup;
    private ViewPager viewPager;

    private int[] images = {
            R.drawable.bulbasaur,
            R.drawable.charmander,
            R.drawable.squirtle
    };

    private ImageSwitcher imgSwitcher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_help, container, false);

        CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(getActivity());
        final ViewPager viewPager = (ViewPager) v.findViewById(R.id.pager);
        viewPager.setAdapter(customPagerAdapter);

        //Adding radiobuttons to control pager
        radioGroup = (RadioGroup) v.findViewById(R.id.radiogroup);
        RadioButton button;
        for (int i = 0; i < images.length; i++) {
            button = new RadioButton(getActivity());
            radioGroup.addView(button);
        }

        radioGroup.check(radioGroup.getChildAt(0).getId());

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Setting page to the number of radio button
                viewPager.setCurrentItem(group.indexOfChild(group.findViewById(checkedId)));
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //Setting checked radio button to the correct position
                radioGroup.check(radioGroup.getChildAt(position).getId());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return v;
    }


    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.help_item, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imageView.setImageResource(images[position]);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

}
