package com.prototype.soundservey;

import java.io.File;
import java.io.IOException;

import com.prototype.soundservey.GlobalVariable.ProgressListener;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	// When requested, this adapter returns a DemoObjectFragment,
	// representing an object in the collection.
	DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
	ViewPager mViewPager;
	ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_holder);

		actionBar = getSupportActionBar();
		// Specify that tabs should be displayed in the action bar.
		// actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// ViewPager and its adapters use support library
		// fragments, so use getSupportFragmentManager.
		mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(
				getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mDemoCollectionPagerAdapter);
		// // Create a tab listener that is called when the user changes tabs.
		// ActionBar.TabListener tabListener = new ActionBar.TabListener() {
		//
		// @Override
		// public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		// // TODO Auto-generated method stub
		// mViewPager.setCurrentItem(arg0.getPosition());
		//
		// }
		//
		// @Override
		// public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// };
		// Add 3 tabs, specifying the tab's text and TabListener
		// for (int i = 0; i < 3; i++) {
		// actionBar.addTab(actionBar.newTab().setText("Survey " + (i + 1))
		// .setTabListener(tabListener));
		// }

		// mViewPager
		// .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
		// @Override
		// public void onPageSelected(int position) {
		// // When swiping between pages, select the
		// // corresponding tab.
		// getSupportActionBar().setSelectedNavigationItem(position);
		// }
		// });

	}

	// Since this is an object collection, use a FragmentStatePagerAdapter,
	// and NOT a FragmentPagerAdapter.
	public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
		public DemoCollectionPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			Fragment fragment = new RecordingSurveyFragment();
			Bundle args = new Bundle();
			// Our object is just an integer :-P
			args.putInt(GlobalVariable.pos, i);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "Survey " + (position + 1);
		}
	}

}
