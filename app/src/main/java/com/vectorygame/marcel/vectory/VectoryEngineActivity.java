package com.vectorygame.marcel.vectory;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.TextView;


public class VectoryEngineActivity extends Activity {

    private GameEngineView memoVectorView; // custom view to display the game
    private static final String TAG = "Activity"; // for logging errors
    private TextView displayCurrentLevelMain; //instantiate for displaying the level of the game
    private TextView displayCurrentScoreMain; //instantiate for displaying the score of the game
    private TextView displayCurrentBestMain; //instantiate for displaying the best of the game
    private TextView holdPopoWindow; //View to hold the pop window;

    private Animation animRotate; //instantiate animation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vectory_engine);

        memoVectorView = (GameEngineView)findViewById(R.id.memoVectorView);//Get referece to view
        displayCurrentLevelMain=(TextView) findViewById(R.id.displayLevel);//Get referece to Level Textview
        displayCurrentScoreMain=(TextView) findViewById(R.id.displayScore);//Get referece to score Textview
        displayCurrentBestMain=(TextView) findViewById(R.id.displayBest);//Get referece to best Textview
        holdPopoWindow=(TextView)findViewById(R.id.spaceLeft);//Get referece to spaceLeft Textview

        //load the animation
        animRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        //Pass refernces of Level,Score,and Best TextView to GameEngineView for editing
        memoVectorView.receiveTextview(displayCurrentLevelMain, displayCurrentScoreMain, displayCurrentBestMain,animRotate);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vectory_engine, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (item.getItemId()){

            case R.id.action_info:
                //Log.e(TAG, "I am in menu item");
                //Pop up the window to show the instruction of how to play the game
                PopupWindow popupWindow=new PopupWindow(VectoryEngineActivity.this);
                View layout=getLayoutInflater().inflate(R.layout.pop_window_instruction,null);
                popupWindow.setContentView(layout);
                popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);

                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);

                //popupWindow.showAsDropDown(layout); //gives this error, View is not attached to a window
                popupWindow.showAsDropDown(holdPopoWindow,30,-50);

                return true;

            case R.id.action_reset:
                memoVectorView.clearLevelScoreBest();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }//end switch
    }

    public void startMyGame(View v) {

        //Log.e(TAG, "this is pressing button");
        memoVectorView.newGame();
        //changeButtonName.setText("Next Game");
        //displayCurrentLevel.setText("yes");
    }

}
