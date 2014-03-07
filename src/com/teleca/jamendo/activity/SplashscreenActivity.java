/**
 * 
 */
package com.teleca.jamendo.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;

import com.teleca.jamendo.R;
import com.teleca.jamendo.dialog.TutorialDialog;

/**
 * @author Marcin Gil
 *
 */
public class SplashscreenActivity extends Activity {
	public final static String FIRST_RUN_PREFERENCE = "first_run"; // SharedPreference name.
	
	private Animation endAnimation; // Animation.
	
	private Handler endAnimationHandler; // Handle the animation.
	private Runnable endAnimationRunnable;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		requestWindowFeature(Window.FEATURE_NO_TITLE); // Change to AndroidManifest.xml file
		setContentView(R.layout.splashscreen);
		findViewById(R.id.splashlayout);

		// Animation
		endAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		endAnimation.setFillAfter(true);
		
		endAnimationHandler = new Handler();
		endAnimationRunnable = new Runnable() {
			@Override
			public void run() {
				findViewById(R.id.splashlayout).startAnimation(endAnimation);
			}
		};
		
		endAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {	}
			
			@Override
			public void onAnimationRepeat(Animation animation) { }
			
			@Override
			public void onAnimationEnd(Animation animation) { // Lauch the HomeActivity when the SplashscreenActivity ends.
				HomeActivity.launch(SplashscreenActivity.this); // Lauch the HomeActivity.
				SplashscreenActivity.this.finish(); // Finish the SplashActivity.
			}
		});

		showTutorial(); // Call the function to show tutorial dialog.
	}
	
	/**
	 * Show the tutorial view if the app is first launched.
	 */
	final void showTutorial() {
		boolean showTutorial = PreferenceManager
				.getDefaultSharedPreferences(this)
				.getBoolean(FIRST_RUN_PREFERENCE, true);
		if (showTutorial) { // The first time to run this app.
			final TutorialDialog dlg = new TutorialDialog(this);
			// Allow the creator of a dialog to run some code when the dialog is dismissed.
			dlg.setOnDismissListener(new DialogInterface.OnDismissListener() { 
				@Override
				public void onDismiss(DialogInterface dialog) {
					CheckBox cb = (CheckBox) dlg.findViewById(R.id.toggleFirstRun);
					if (cb != null && cb.isChecked()) { // If the CheckBox is checked.
						SharedPreferences prefs = PreferenceManager
								.getDefaultSharedPreferences(SplashscreenActivity.this); // Change the value of the SharedPerferences.
						prefs.edit().putBoolean(FIRST_RUN_PREFERENCE, false).commit();
					}
					endAnimationHandler.removeCallbacks(endAnimationRunnable); // Remove all pending post.
					endAnimationHandler.postDelayed(endAnimationRunnable, 2000);
				}
			});
			dlg.show();

		} else {
			endAnimationHandler.removeCallbacks(endAnimationRunnable);
			endAnimationHandler.postDelayed(endAnimationRunnable, 1500);
		}
	}
}
