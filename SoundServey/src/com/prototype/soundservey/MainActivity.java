package com.prototype.soundservey;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

public class MainActivity extends ActionBarActivity {

	DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
	ViewPager mViewPager;
	ActionBar actionBar;
	ArrayList<Fragment> fragments;
	private MyReceiver receiver;
	private IntentFilter filter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_holder);
		receiver = new MyReceiver();
		filter = new IntentFilter(MainActivity.class.getName());
		actionBar = getSupportActionBar();
		fragments = new ArrayList<Fragment>();

		// Add 3 fragments m too lazy for a for loop
		fragments.add(new RecordingSurveyFragment());
		fragments.add(new RecordingSurveyFragment());
		fragments.add(new RecordingSurveyFragment());

		mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(
				getSupportFragmentManager(), fragments);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mDemoCollectionPagerAdapter);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		registerReceiver(receiver, filter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub

			int positiontoRemove = arg1.getIntExtra(GlobalVariable.pos, 0);
			if (fragments.size() != 0) {
				fragments.remove(positiontoRemove);
				mViewPager.setAdapter(mDemoCollectionPagerAdapter);
				if (fragments.size() == 0) {
					fragments.add(new CompleteSurveyFragment());
					mViewPager.setAdapter(mDemoCollectionPagerAdapter);

				}
			}
		}

	}

	public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
		ArrayList<Fragment> fragments;

		public DemoCollectionPagerAdapter(FragmentManager fm,
				ArrayList<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int i) {
			Fragment fragment;
			if (fragments.get(i) instanceof CompleteSurveyFragment) {
				fragment = (CompleteSurveyFragment) fragments.get(i);
				Bundle b = new Bundle();
				b.putInt(GlobalVariable.pos, i);
				fragment.setArguments(b);
			} else {
				fragment = (RecordingSurveyFragment) fragments.get(i);
				Bundle b = new Bundle();
				b.putInt(GlobalVariable.pos, i);
				fragment.setArguments(b);
			}

			return fragment;
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "Survey " + (position + 1);
		}
	}

}
