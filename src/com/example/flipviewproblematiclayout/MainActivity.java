package com.example.flipviewproblematiclayout;

import android.app.Activity;
import android.os.Bundle;

import com.aphidmobile.flip.FlipViewController;

import entities.GalleryFlipAdapter;

public class MainActivity extends Activity
{

	private FlipViewController flipView;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		flipView = new FlipViewController(this);

		flipView.setAdapter(new GalleryFlipAdapter(this));

		setContentView(flipView);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		flipView.onResume();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		flipView.onPause();
	}
}
