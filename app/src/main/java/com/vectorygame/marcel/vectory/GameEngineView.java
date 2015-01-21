package com.vectorygame.marcel.vectory;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.Animation;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Marcel on 1/20/2015.
 */
public class GameEngineView extends SurfaceView implements SurfaceHolder.Callback{
    //You have to implement 3-methods for  SurfaceHolder.Callback:
    //(1) surfaceChanged()
    //(2) surfaceCreated()
    //(3) surfaceDestroyed()

    // Key used to persistently store the value of best score and initialized holding variables
    private static final String scoreBest_key = "com.example.marcel.vecotorydynamic.bestscore";
    private Integer userCurrentLevel = 0; //To hold the user current level
    private Integer userCurrentBestScore; //To hold the user current best score
    //private Integer userCurrentScore; //To hold the user current best
    private SharedPreferences saveHighBest; //Instantiate a SharedPreferences object
    private Integer saveWinBestScore;//To hold the best score and store to sharePreferences
    private Integer showCurrentLevel; //To hold the current level


    private static final String TAG = "GameEngineView"; // for logging errors
    private MemoVectorThread memoThread; // controls the game loop
    private Activity activity; // to display Game Over dialog in GUI thread
    private boolean dialogIsDisplayed = false; //used inside surfaceCreated()

    private int n;
    private int initial_x;
    private int initial_y;
    private int save_x;
    private int save_y;
    private int new_x;
    private int new_y;
    private int addingPlus;


    private int initial_x_draw;
    private int initial_y_draw;
    private int speed_X;
    private int speed_Y;
    private int final_x_draw;
    private int final_y_draw;

    //Holding Screen width and height
    private int holdWidth;


    //boolean for direction
    private boolean goes_right;
    private boolean goes_left;
    private boolean draw_right_left;
    private boolean goes_down;
    private boolean goes_up;
    private boolean draw_down_up;

    private int m; //for retriving data from array
    private int int_draw; // for drawing number of line after finishing animation
    private boolean go_draw;
    private boolean go_allow;

    private boolean drawFinalCircle; //to draw final coordinate point
    private boolean checkInitialFinalPoint;//checking final point get to initial point

    private double totalElapsedTime; // elapsed seconds


    // variables for the game loop and tracking statistics
    private boolean gameOver; // is the game over?
    private double timeLeft; // time remaining in seconds

    //for controlling timer and looping
    private boolean loopTimerDrawing;
    private boolean loopForDrawing;
    private boolean killThread;
    private boolean testThread;
    private boolean timeLeftAllow;
    //allow touch screen after drawing animation
    private boolean allowTouch;

    // Paint variables used when drawing each item on the screen
    private Paint textPaint; // Paint used to draw text
    private Paint backgroundPaint; // Paint used to clear the drawing area
    private Paint backgroundFirst; //
    private Paint lineSpeed; // Paint used to draw the cannon
    private Paint squarePaint; //color for the square;
    private Paint linePaint; //color for the square;
    private Paint lineGrid; //drawing background grid line
    private Paint circleInitial; //small circle for initial coordinate
    private Paint circleWinFinal;//redraw final circle wih line color
    private Paint circleFinal; //small circle for final coordinate
    private Paint testLineTouch; //test the toch scheen;
    private Paint finalCircleColor;// color for the final circle point

    //variable for drawing line for for touching
    //canva.drawline(startXco,startYco,finalXco,finalYco,paint)
    private int startXco; // initial x coordinate
    private int startYco; //initital y coordinate
    private int finalXco; //final x coordinate
    private int finalYco; //final y coordinate
    private int testXco;
    private int testYco;
    private boolean swipeDrawLine; //allow to draw the line from finger
    private boolean allowCheckingWinLose;

    private int distX;
    private int distY;
    private int thresholdX=10;//x distance traveled to be considered swipe
    private int theresholdY=10;//y distance traveled to be considered swipe
    private int constraintDiagonal=100; //distance allowed in perpendicular distance
    private int holdX_for_path;
    private int holdY_for_path;

    // The sequence of indices chosen from board
    private ArrayList<Integer> sequencex = new ArrayList<Integer>();
    private ArrayList<Integer> sequencey = new ArrayList<Integer>();

    //array holding the x and y coordinate of each square center
    private ArrayList<Integer> holdCenterSquareX=new ArrayList<Integer>();
    private ArrayList<Integer> holdCenterSquareY=new ArrayList<Integer>();

    //array holding  the x and y coordinate of line draw by the user
    private ArrayList<Integer> holdUserLineX=new ArrayList<Integer>();
    private ArrayList<Integer> holdUserLineY=new ArrayList<Integer>();

    //set the speed of animation
    private int finalSpeedAnimation; //Changes this number to change the speed of the animation
    private final int finalMaxiumWidthPixel=600;



    private int xSquare; //initial x axis to draw square
    private int ySquare; //initial y axis to draw square
    private int xShift; //shift x point of square
    private int yShift; //shift y point of square

    //matrix of square: n_one X n_two
    private int n_one; //number of square for width
    private int n_two; //number of square for height

    private int dePixel;
    private int halfdePixel;
    private int finalYShiftDown; //shift all grid down so you can display anything on the top
    private int finalHalfYShiftDown; //the half of the shift down
    private int finalHeight; //the final height of the space for drawing
    private int finalYHeight; //the final y coordinate for width in y axis

    //canvas for drawing line
    private Canvas canvaDrawTouch;
    private Bitmap mBitmap;
    protected Canvas inCanvas;

    private Path path;
    private Paint paint;
    private Paint showLoseLine; //paint for showing the actual line when lose

    //Draw line after animation
    private Path drawPathAfterAnimation; //hold the value after animation

    //Show the actual path when you lose
    private Path showPathWhenLose;

    //Holding the number of time line passes over final point
    private int numberOfTimeOverFinal;//check number of time lines pass over final point
    private int numberOfTimeUserOverFinal;//check the number of time the user pass over the final point

    private int numberOfTimeSameN; //the number of time to generate the same number
    private final int numberOfTimeRepetition=5; //number of repetition allow for each n
    private final int numberOfAllowN=20; //number of allow n

    //Get the idea of the textview from the activity_main.xml
    private TextView displayCurrentLevel; //instantiate for displaying the level of the game
    private TextView displayCurrentScore;//instantiate for displaying the score of the game
    private TextView displayCurrentBest;//instantiate for displaying the score of the game

    //Animation
    private Animation rotateAnimScore; //Animate the score textView

    //Set text size and stroke width for the line
    private int setTextSize;//set the text size for different screen size
    private int setStrokeWidthLine; //set the line width for different screen size

    //Set the margin for left and right space edge and the size of circle
    private int setMarginLeftRight; //set the space for left and right side for different screen size
    private int setCircleRadius;//set the the circle size for different screen size

    // public constructor
    public GameEngineView(Context context, AttributeSet attrs)
    {


        super(context, attrs); // call superclass constructor
        //Log.e(TAG, "this is GameEngineView constructor");
        activity = (Activity) context; // store reference to MainActivity

        // register SurfaceHolder.Callback listener
        getHolder().addCallback(this);
        setFocusable(true);
        // construct Paints for drawing text
        //these are configured in method onSizeChanged
        textPaint = new Paint();
        backgroundPaint = new Paint();
        backgroundFirst=new Paint();
        lineSpeed = new Paint();
        squarePaint=new Paint();
        linePaint=new Paint();
        lineGrid=new Paint();
        circleInitial=new Paint();
        circleWinFinal=new Paint();
        circleFinal=new Paint();
        testLineTouch=new Paint();
        finalCircleColor=new Paint();

        path= new Path();
        drawPathAfterAnimation=new Path(); //new path hold values for drawing aafter animation
        showPathWhenLose=new Path(); //show actual path when you lose
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        showLoseLine=new Paint(Paint.ANTI_ALIAS_FLAG);


        //getSharereferences has to have reference to MainActivity or to Context to be used
        saveHighBest = activity.getSharedPreferences(scoreBest_key, 0); //Initialize SharedPreferences
        userCurrentBestScore = saveHighBest.getInt(scoreBest_key, 0);//get save value from it and set to variable

        //Level is 1 when app launched
        showCurrentLevel=1;

        //Texteview
        //displayCurrentLevel=(TextView) findViewById(R.id.ShowScore);

    } // end CannonView constructor


    //onSizeChanged is a callback method for View objects in general.
    //This method is always called at least once, after surfaceCreated(android.view.SurfaceHolder).
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        //Set speed of animation depending on the device width screen
        if(w>finalMaxiumWidthPixel){

            finalSpeedAnimation=8;//speed for animation
            setTextSize=100; //text size
            setStrokeWidthLine=6; //size of stroke width
            setMarginLeftRight=28;//margin for left and right edge
            setCircleRadius =14; //circle size
        }else{
            finalSpeedAnimation=4; //speed for animation
            setTextSize=50; //text size
            setStrokeWidthLine=4; //size of stroke width
            setMarginLeftRight=24; //margin for left and right edge
            setCircleRadius =12; //circle size
        }


        //Log.e(TAG, "this is onSizeChanged() method");
        //Log.e("numer of index m", "value of w is " + w+" value of h is "+h);

        holdWidth=w; //hold screen width

        //save_x=244;
        // save_y=327;
        int numOfSquareinGrid=10; //the number of square in the grid, n by n matrix
        int showGridWidth=w-setMarginLeftRight; //10px left margin+10px right margin
        dePixel=(int)(showGridWidth/10);
        halfdePixel=dePixel/2;
        n_one=(int)w/dePixel;
        n_two=(int)h/dePixel;

        int left_Width=w-(dePixel*numOfSquareinGrid);
        //int left_Height=h-(dePixel*n_two);

        xShift=(int)left_Width/2;
        yShift= xShift;
        //yShift=100;
        int totalHalfSpaceGame=(int)(dePixel*numOfSquareinGrid)/2;
        int centerX=xShift+totalHalfSpaceGame;
        int centerY=centerX;

        finalHalfYShiftDown=h-(numOfSquareinGrid*dePixel)-yShift;//calculate the half of shift down
        finalYShiftDown=finalHalfYShiftDown/2 +20; //calculate the final shift down
        finalHeight=h; //the final height of the drawing space

        finalYHeight=finalYShiftDown+holdWidth-2*xShift;//gives the final width in y coordinate

        save_x=centerX;
        save_y=finalYShiftDown+totalHalfSpaceGame;


        //Log.e("numer of index m", "xShift "+xShift+"  Yshift "+yShift);
        //Log.e("center of x and y", "center x "+save_x+"  center y "+save_y);

        // configure Paint objects for drawing game elements
        textPaint.setTextSize(setTextSize); // text size 1/20 of screen width
        textPaint.setAntiAlias(true); // smoothes the text

        lineSpeed.setStrokeWidth(setStrokeWidthLine); // set line thickness

        //backgroundFirst.setColor(Color.rgb(237,224,255)); // set background color
        backgroundFirst.setColor(Color.WHITE); // set background color
        //backgroundPaint.setColor(Color.rgb(161, 167, 175)); // set background color
        backgroundPaint.setColor(Color.rgb(235, 234, 224)); // set background color
        squarePaint.setColor(Color.GREEN);

        // linePaint.setAntiAlias(true);
        //linePaint.setStrokeJoin(Paint.Join.ROUND);
        //Line for Animation
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(setStrokeWidthLine);
        //linePaint.setColor(Color.rgb(138,255,144));
        linePaint.setColor(Color.rgb(242,97,255));

        //set lineGrid features-GRAY COLOR
        lineGrid.setStrokeWidth(setStrokeWidthLine);
        //lineGrid.setColor(Color.rgb(117,125,138)); //set gray color-before
        lineGrid.setColor(Color.rgb(195,207,180)); //set gray color
        //lineGrid.setColor(Color.rgb(119,136,153));

        //circleInitial.setColor(Color.RED); //show initial circle in red
        circleInitial.setColor(0xffc9171e); //show initial circle in red

        //Set color for final win circle
        circleWinFinal.setColor(Color.rgb(242,97,255));

        //circleFinal.setColor(Color.rgb(138,255,144));
        circleFinal.setColor(Color.rgb(242,97,255));
        testLineTouch.setStrokeWidth(setStrokeWidthLine);
        testLineTouch.setColor(Color.rgb(242,97,255));

        finalCircleColor.setColor(Color.rgb(82,191,255)); //color for the final circle point-blue

        n=4;  //set the n number of random generator

        loopTimerDrawing=true;
        loopForDrawing=false;
        killThread=false;
        testThread=true;
        timeLeftAllow=true;

        //Show the path for the animation
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(setStrokeWidthLine);
        paint.setAntiAlias(false);
        paint.setColor(Color.rgb(242,97,255));
        //paint.setColor(Color.RED);

        showLoseLine.setStyle(Paint.Style.STROKE);
        showLoseLine.setStrokeWidth(setStrokeWidthLine);
        showLoseLine.setColor(Color.rgb(191,66,49));

        allowCheckingWinLose=false;
        numberOfTimeSameN=0;

        saveWinBestScore=0; //Initialize to 0 when app launched


        //newGame(); // set up and start a new game
        //call newGame() once during onSizeChanged  and reset variable values but does not execute
        //block inside if(gameOver) statement since gameOver is false for
        // the first launch of the app

    } // end method onSizeChanged


    //get TextView from MainActivity

    public void receiveTextview(TextView receiveTextViewLevel,TextView receiveTextViewScore,TextView receiveTextViewBest,Animation receiveAnimRotate){

        displayCurrentLevel=receiveTextViewLevel; //get the level textview
        displayCurrentScore=receiveTextViewScore; //get the score textview
        displayCurrentBest=receiveTextViewBest; //get the best textview

        displayCurrentLevel.setText(showCurrentLevel.toString());
        displayCurrentBest.setText(userCurrentBestScore.toString());//Show the available best score
        //saved in sharePreference storage
        rotateAnimScore=receiveAnimRotate; //get the rotate animation

    }




    //Clear Level, Score and Best
    public void clearLevelScoreBest(){

        showCurrentLevel=1; //Reset level to 1
        saveWinBestScore=0; //reset score and best to 0
        n = 4;
        numberOfTimeSameN=0;
        showGameOverDialog(2); //Reset Level,Score and Best to Initial

    }


    // reset all the screen elements and start a new game
    public void newGame()
    {

        //Log.e(TAG, "this is newGame() method");
        /*Mistake you did not initial  initial_x and initial_y to their initial values since they
         * change values all time
          * */


        //showGameOverDialog();

        showPathWhenLose.reset();
        path.reset();
        drawPathAfterAnimation.reset();

        if(killThread){
            //Log.e(TAG, "I am killing thread");
            memoThread.setRunning(false); // terminate thread
            killThread=false;
            loopTimerDrawing=true;
        }

        //displayCurrentLevel.setText("yes");

        //showGameOverDialog();


        timeLeftAllow=false;
        loopForDrawing=true;
        allowTouch=false; //not allow using the touch screen
        sequencex.clear();
        sequencey.clear();
        holdUserLineX.clear();
        holdUserLineY.clear();
        holdUserLineX.add(save_x);
        holdUserLineY.add(save_y);

        if(swipeDrawLine){

            //path.reset();
            //showPathWhenLose.reset();
            // drawPathAfterAnimation.reset();
        }

        swipeDrawLine=false;
        allowCheckingWinLose=false;
        drawPathAfterAnimation.moveTo(save_x,save_y);

        holdX_for_path=save_x;
        holdY_for_path=save_y;
        //startXco=save_x;
        //startYco=save_y;
        //finalXco=startXco;
        //finalYco=startYco;

        /*initial_x_draw=0;
        initial_y_draw=0;
        new_x_draw=0;
        new_y_draw=0;*/

        initial_x=save_x;
        initial_y=save_y;
        drawFinalCircle=false;
        checkInitialFinalPoint=false;

        timeLeft = 6; // start the countdown at  6 seconds
        totalElapsedTime = 0.0; // set the time elapsed to zero
        if(n==numberOfAllowN &&numberOfTimeSameN==numberOfTimeRepetition){

            n=4;
            numberOfTimeSameN=1;
            showCurrentLevel=1;
            displayCurrentLevel.setText(showCurrentLevel.toString()); //Show current level

        }else{

            if(numberOfTimeSameN==numberOfTimeRepetition) {
                n = n + 2;
                numberOfTimeSameN=1;
                showCurrentLevel=showCurrentLevel+1; //Add level when n change
                displayCurrentLevel.setText(showCurrentLevel.toString());//Show current level
            }else{
                numberOfTimeSameN++;
            }
        }


        generate_random_number();
        m=0;
        int_draw=0;
        go_draw=false;
        go_allow=true;
        // Log.e("numer of index m", "value of m is " +m);
        initial_x_draw=sequencex.get(m);

        initial_y_draw=sequencey.get(m);

        final_x_draw=sequencex.get(m+1);
        final_y_draw=sequencey.get(m+1);

       // Log.e(TAG, "final_x_draw and final_y_draw "+save_x+"  "+save_y);
        // drawPathAfterAnimation.lineTo(final_x_draw,final_y_draw);
        //drawPathAfterAnimation.lineTo(sequencex.get(m+2),sequencey.get(m+2));
        m=m+1;
        // Log.e("numer of index m", "value of m is " +m);

        check_direction();

        //Check how many time the line pass over the final point
        numberOfTimeOverFinal=0; //set initially to zero
        numberOfTimeUserOverFinal=0;//set initially to zero
        int xFinalPoint=sequencex.get(n); //get final x coordinate point
        int yFinalPoint=sequencey.get(n); //get final y coordinate point

        for(int checkOverPoint=1; checkOverPoint<n-1; checkOverPoint++){

            if(sequencex.get(checkOverPoint).equals(xFinalPoint) && sequencey.get(checkOverPoint).equals(yFinalPoint)){
                numberOfTimeOverFinal++; //add one if there is line passes over the final point
            }

        }
       // Log.e(TAG, "the number of time passing over the final point "+numberOfTimeOverFinal);


        if (gameOver) // starting a new game after the last game ended
        {

            gameOver = false;
            memoThread = new MemoVectorThread(getHolder()); // create thread
            memoThread.start(); // start the game loop thread

           /* if (testThread) {
                memoThread = new MemoVectorThread(getHolder()); // create thread
                memoThread.start(); // start the game loop thread
                Log.e(TAG, "I am inside testThread");
            } else{
                memoThread.setRunning(true); // run thread
                Log.e(TAG, "I am inside nottestThread");
             }*/
            //Log.e(TAG, "I am inside game over");
        }
    } // end method newGame


    // called repeatedly by the CannonThread to update game elements
    private void updatePositions(double elapsedTimeMS) {

        double interval = elapsedTimeMS / 1000.0; // convert to seconds


        timeLeft -= interval; // subtract from time left
        //Log.e(TAG, "this is timeleft value"+timeLeft);


        if (go_allow){
            //add a boolen here to stop all
            if (m <= n) {
                //Log.e("numer of index m", "value of m is " + m);

                //check if going right and update values
                if (goes_right) {

                    speed_X = speed_X + addingPlus; //keep going to right direction

                    if (speed_X > final_x_draw) {
                        goes_right = false;
                        goes_left = false;
                        draw_right_left = false;
                        goes_down = false;
                        goes_up = false;
                        draw_down_up = false;


                        if (m < n) {
                            initial_x_draw = sequencex.get(m);

                            initial_y_draw = sequencey.get(m);

                            final_x_draw = sequencex.get(m + 1);
                            final_y_draw = sequencey.get(m + 1);

                            drawPathAfterAnimation.lineTo(initial_x_draw,initial_y_draw);

                            m = m + 1;

                            go_draw = true;
                            int_draw = int_draw + 1;
                            check_direction();
                            if (goes_right) {
                                //Log.e("goes_right again","goes right_again");
                                speed_X = speed_X + addingPlus;
                            }
                        } //end less than 4

                        else{
                            go_allow=false;
                            int_draw=int_draw+1;
                            drawPathAfterAnimation.lineTo(sequencex.get(n),sequencey.get(n));

                        }
                    }
                } //end goes_right

                //check if going left and update values
                if (goes_left) {

                    speed_X = speed_X + addingPlus; //keep going to right direction

                    if (speed_X < final_x_draw) {
                        goes_right = false;
                        goes_left = false;
                        draw_right_left = false;
                        goes_down = false;
                        goes_up = false;
                        draw_down_up = false;


                        if (m < n) {
                            initial_x_draw = sequencex.get(m);

                            initial_y_draw = sequencey.get(m);

                            final_x_draw = sequencex.get(m + 1);
                            final_y_draw = sequencey.get(m + 1);

                            drawPathAfterAnimation.lineTo(initial_x_draw,initial_y_draw);
                            //continue to the next line following previous line


                            m = m + 1;
                            go_draw = true;
                            int_draw = int_draw + 1;
                            check_direction();
                            if (goes_left) {

                                speed_X = speed_X + addingPlus;
                            }

                        } else{
                            go_allow=false;
                            int_draw=int_draw+1;
                            drawPathAfterAnimation.lineTo(sequencex.get(n),sequencey.get(n));

                        }
                    }
                } //end goes_left

                //check if going down and update values
                if (goes_down) {

                    speed_Y = speed_Y + addingPlus; //keep going to right direction
                    // Log.e("goes_down again", "goes goes_down again");
                    if (speed_Y > final_y_draw) {
                        goes_right = false;
                        goes_left = false;
                        draw_right_left = false;
                        goes_down = false;
                        goes_up = false;
                        draw_down_up = false;

                        if (m < n) {
                            initial_x_draw = sequencex.get(m);

                            initial_y_draw = sequencey.get(m);

                            final_x_draw = sequencex.get(m + 1);
                            final_y_draw = sequencey.get(m + 1);
                            drawPathAfterAnimation.lineTo(initial_x_draw,initial_y_draw);


                            m = m + 1;
                            go_draw = true;
                            int_draw = int_draw + 1;
                            check_direction();
                            if (goes_down) {

                                speed_Y = speed_Y + addingPlus;
                            }
                        }else{
                            go_allow=false;
                            int_draw=int_draw+1;
                            drawPathAfterAnimation.lineTo(sequencex.get(n),sequencey.get(n));

                        }

                    }
                } //end goes_down


                //check if going up and update values
                if (goes_up) {

                    speed_Y = speed_Y + addingPlus; //keep going to right direction
                    // Log.e("goes_up again", "goes goes_up again");
                    if (speed_Y < final_y_draw) {
                        goes_right = false;
                        goes_left = false;
                        draw_right_left = false;
                        goes_down = false;
                        goes_up = false;
                        draw_down_up = false;

                        if (m < n) {
                            initial_x_draw = sequencex.get(m);

                            initial_y_draw = sequencey.get(m);

                            final_x_draw = sequencex.get(m + 1);
                            final_y_draw = sequencey.get(m + 1);

                            drawPathAfterAnimation.lineTo(initial_x_draw,initial_y_draw);
                            //continue to the next line following previous line

                            m = m + 1;
                            go_draw = true;
                            int_draw = int_draw + 1;
                            check_direction();
                            if (goes_up) {

                                speed_Y = speed_Y + addingPlus;
                            }

                        }else{
                            go_allow=false;
                            int_draw=int_draw+1;
                            drawPathAfterAnimation.lineTo(sequencex.get(n),sequencey.get(n));

                        }
                    }
                } //end goes_up

            } //allow to go to n level
            else {
                draw_right_left = false;

            }
        }


        // if the timer reached zero
        if(loopTimerDrawing) {
            if (timeLeftAllow) {
                timeLeft = 0.0;
                timeLeftAllow=false;
                gameOver = true; // the game is over
                memoThread.setRunning(false); // terminate thread
                //showGameOverDialog(R.string.lose); // show the losing dialog
                goes_right = false;
                goes_left = false;
                draw_right_left = false;
                goes_down = false;
                goes_up = false;
                draw_down_up = false;
                go_draw = false;
               // Log.e(TAG, "this is timeleft value"+timeLeft);
                //drawPathAfterAnimation.reset();
                if (loopForDrawing) {
                    //memoThread = new MemoVectorThread(getHolder()); // create thread
                    // memoThread.start(); // start the game loop thread
                    memoThread.setRunning(true); // run thread
                    loopTimerDrawing = false;
                    loopForDrawing=false;
                    killThread=true; //kill thread when start new game

                    allowTouch=true;//allow using the touchscreen
                    //swipeDrawLine=true;
                    path.moveTo(save_x,save_y);
                   // Log.e(TAG, "I am doing allowing redraw again");
                    //testThread=false;
                }

                //Log.e(TAG, "I am inside time left");

            }
        }
    } // end method updatePositions

    // draws the game to the given Canvas
    public void drawGameElements(Canvas canvas)
    {
        // clear the background
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(),backgroundFirst);
        canvas.drawRect(xShift, finalYShiftDown, canvas.getWidth()-xShift, holdWidth-2*xShift+finalYShiftDown,backgroundPaint);


        //With shifting grid down
        for(int k=0; k<holdWidth- xShift; k=k+dePixel) {

            //Add 2 pixel to xshift because you want to cover the open corner, the joint of each vertical and horizontal line for the grid

            canvas.drawLine(xShift-2, finalYShiftDown+k, holdWidth+2- xShift, finalYShiftDown+k, lineGrid);
        }
        //Draw vertical background grid line
        for(int l=0; l<holdWidth- yShift; l=l+dePixel) {
            canvas.drawLine(xShift+l, finalYShiftDown, xShift+l,holdWidth-2*xShift+finalYShiftDown, lineGrid);
        }


        //Draw initial position as a small red circle
        canvas.drawCircle(save_x,save_y, setCircleRadius,circleInitial);

        //Drawing grid of line
        // Draw the line animation, speed_X and speed_Y are changing
        if(draw_right_left) {
            //canvas.drawLine(initial_x_draw, initial_y_draw, speed_X, speed_Y,lineSpeed); //has black oolor
            canvas.drawLine(initial_x_draw, initial_y_draw, speed_X, speed_Y,linePaint);//has green color
            canvas.drawCircle(speed_X, speed_Y, setCircleRadius, circleFinal);
        }

        //Draw the line as static after animation completed
        if(go_draw) {


            canvas.drawPath(drawPathAfterAnimation, paint);
            if(int_draw==n){
                drawFinalCircle=true;
                showPathWhenLose.set(drawPathAfterAnimation);
                drawPathAfterAnimation.reset();
                timeLeftAllow=true;

                // drawPathAfterAnimation.lineTo(sequencex.get(n),sequencey.get(n));
                //go_draw=false;
            }
            //canvas.drawPath(drawPathAfterAnimation, paint);

                /*if(!go_draw){
                    drawPathAfterAnimation.reset();
                }*/

        }
        if(drawFinalCircle) {

            //check if final point get to initial point
            //if yes, draw smaller final circle
            if (sequencex.get(0).equals(sequencex.get(n)) && sequencey.get(0).equals(sequencey.get(n))) {
                canvas.drawCircle(sequencex.get(n), sequencey.get(n), setCircleRadius-2, finalCircleColor);
            } else {

                //Draw final position as a small red circle
                canvas.drawCircle(sequencex.get(n), sequencey.get(n), setCircleRadius, finalCircleColor);
            }
            if (swipeDrawLine) {
                //test drawing with finger touch
                //canvas.drawLine(startXco, startYco, finalXco, finalYco, testLineTouch);

                canvas.drawCircle(holdX_for_path, holdY_for_path, setCircleRadius, circleFinal);
                //change color for initial circle
                canvas.drawCircle(sequencex.get(0), sequencey.get(0), setCircleRadius, circleWinFinal);
                canvas.drawPath(path, paint);

                //allow the check only when there is a new touch

                if(allowCheckingWinLose) {

                    if (sequencex.get(n).equals(holdX_for_path) && sequencey.get(n).equals(holdY_for_path)) {

                        //Check the number of time the user allows to pass over the line
                        //Log.e(TAG, "checking the numberofuser and number of timer over lien " + numberOfTimeUserOverFinal + "  " + numberOfTimeOverFinal);

                        if (numberOfTimeUserOverFinal == numberOfTimeOverFinal) {
                            //check the length of arrays are the same
                            if (sequencex.size() == holdUserLineX.size()) {
                                //Log.e(TAG, "both size are equal");

                                //to use equals, the length of each array has to be the same, so check if
                                //length is the same before comparing array with equal()
                                if (sequencex.equals(holdUserLineX) && sequencey.equals(holdUserLineY)) {

                                    //change color for initial circle
                                    //canvas.drawCircle(sequencex.get(0), sequencey.get(0), 12, circleWinFinal);
                                    // Change color for final circle
                                    canvas.drawCircle(sequencex.get(n), sequencey.get(n), setCircleRadius, circleWinFinal);
                                    canvas.drawText(getResources().getString(
                                           R.string.win), holdWidth / 2-dePixel-halfdePixel, holdWidth/2, textPaint);
                                    allowTouch = false;
                                    memoThread.setRunning(false); // terminate thread

                                    showGameOverDialog(1);//1-->first option in the method
                                   // Log.e(TAG, "every content is the same you win");
                                } else {
                                    allowTouch = false;
                                    memoThread.setRunning(false); // terminate thread
                                    canvas.drawPath(showPathWhenLose, showLoseLine);
                                    canvas.drawCircle(sequencex.get(0), sequencey.get(0), setCircleRadius, circleInitial);
                                    canvas.drawCircle(sequencex.get(n), sequencey.get(n), setCircleRadius, circleInitial);
                                    canvas.drawText(getResources().getString(
                                            R.string.lose), holdWidth / 2-dePixel-halfdePixel, holdWidth/2, textPaint);
                                   // Log.e(TAG, "every content is not the same you lose");
                                }
                            } else {
                                allowTouch = false;
                                memoThread.setRunning(false); // terminate thread
                               // Log.e(TAG, "you lose from equal size");
                                canvas.drawPath(showPathWhenLose, showLoseLine);
                                canvas.drawCircle(sequencex.get(0), sequencey.get(0), setCircleRadius, circleInitial);
                                canvas.drawCircle(sequencex.get(n), sequencey.get(n), setCircleRadius, circleInitial);
                                canvas.drawText(getResources().getString(
                                        R.string.lose), holdWidth / 2-dePixel-halfdePixel, holdWidth/2, textPaint);
                            }

                        } else {

                            numberOfTimeUserOverFinal++;
                        }

                    } //end check sequence

                    allowCheckingWinLose=false;
                }//end allowCheckingWinLose
            }//end swipeDrawLine
        }

        //test drawing with finger touch
        //canvas.drawLine(save_x, save_y, mx, my,testLineTouch);
    } // end method drawGameElements


    // display an Score with Animation when user wins
    private void showGameOverDialog(final int chooseOption)
    {

        // in GUI thread, use FragmentManager to display the DialogFragment
        activity.runOnUiThread(
                new Runnable() {
                    public void run()
                    {

                        switch (chooseOption) {

                            case 1:
                                //Update Score and Best
                                saveWinBestScore = saveWinBestScore + n; //Adding the number of paths to check
                                //the high score and then store
                                displayCurrentScore.startAnimation(rotateAnimScore);
                                displayCurrentScore.setText(saveWinBestScore.toString());//update the score texview

                                //Check the score is higher than the best score stored in sharePreferences
                                if (saveWinBestScore > userCurrentBestScore) {
                                    SharedPreferences.Editor highScoreEditor = saveHighBest.edit();
                                    highScoreEditor.putInt(scoreBest_key, saveWinBestScore);
                                    highScoreEditor.commit();
                                    displayCurrentBest.startAnimation(rotateAnimScore);
                                    displayCurrentBest.setText(saveWinBestScore.toString());//update the best score textview
                                }
                                break;
                            case 2:
                                //Reset Level,Score,and Best to initial

                                displayCurrentLevel.startAnimation(rotateAnimScore);
                                displayCurrentScore.startAnimation(rotateAnimScore);
                                displayCurrentBest.startAnimation(rotateAnimScore);
                                displayCurrentLevel.setText(showCurrentLevel.toString());//Show current level
                                displayCurrentScore.setText(saveWinBestScore.toString());//update the score texview
                                displayCurrentBest.setText(saveWinBestScore.toString());//update the best score textview
                                SharedPreferences.Editor highScoreEditor = saveHighBest.edit();
                                highScoreEditor.putInt(scoreBest_key, saveWinBestScore);
                                highScoreEditor.commit();
                                break;

                        }//end switch
                    }
                } // end Runnable
        ); // end call to runOnUiThread
    } // end method showGameOverDialog


    // stops the game; called by CannonGameFragment's onPause method
    public void stopGame()
    {
       // Log.e(TAG, "this is stopGame()");
        if (memoThread != null) {
            memoThread.setRunning(false); // tell thread to terminate
        }
       // Log.e(TAG, "I am stopping game");
    }

    /*
    *  3-methods of SurfaceHolder.Callback you have to implement
    */

    // called when surface changes size
    //onSurfaceChanged is a callback method for a surfaceholder interface.
    // which many View objects implement (GLSurfaceView, SurfaceView).
    // Comment from Deitel book: called by surfaceChanged when the size of the SurfaceView changes,
    // such as when it's first added to the View hierarchy--occurs ony one time when app is created
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format,int width, int height){

        //you don't do anything when the su6-rface size change
       // Log.e(TAG, "this is surfaceChanged()");
    }

    // called when surface is first created
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {


        //Log.e(TAG, "this is surfaceCreated()");

        //SurfaceHolder: Abstract interface to someone holding a display surface.
        // Allows you to control the surface size and format, edit the pixels in the surface,
        // and monitor changes to the surface.
        //holder -->The SurfaceHolder whose surface is being created.


        //here you call your first game after the surface is created
        if (!dialogIsDisplayed){

            memoThread = new MemoVectorThread(holder); // create thread
            memoThread.setRunning(true); // start game running--start first gaming by assigning
            //true in setRunning()
            memoThread.start(); // start the game loop thread---or start the thread class
           // Log.e(TAG, "just created a new thread");

        }
    }

    // called when the surface is destroyed
    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {

        //Log.e(TAG, "this is surfaceDestroyed()");
        // ensure that thread terminates properly
        boolean retry = true;
        memoThread.setRunning(false); // terminate cannonThread

        while (retry)
        {
            try
            {
                //join()-->Blocks the current Thread (Thread.currentThread()) until the receiver
                // finishes its execution and dies.
                memoThread.join(); // wait for cannonThread to finish
                retry = false;
                //Log.e(TAG, "thread is finished");
            }
            catch (InterruptedException e)
            {
               // Log.e(TAG, "Thread interrupted", e);
            }
        }



    } // end method surfaceDestroyed


    // called when the user touches the screen in this Activity
    @Override
    public boolean onTouchEvent(MotionEvent e)
    {

        //NOTE
        //getEventTime()-->retrive the time this event occurred, in the uptimeMillis() time base
        //Returns the time(in ms) when the user originally pressed down to start a stream
        //of position events
        //Touch Graph:
        //http://developer.android.com/design/patterns/gestures.html

        if(allowTouch) {

            int action = e.getAction();//gives the index of type of action
            //if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            testXco = (int) e.getX();
            testYco = (int) e.getY();

            switch (action) {

                case MotionEvent.ACTION_DOWN:
                    //when finger is down, get x and y coordinate

                    //path.moveTo(save_x,save_y);
                    startXco=(int)e.getX();
                    startYco=(int)e.getY();
                    //Log.e(TAG, "Touching Down "+testXco+" "+testYco);

                    break;
                case MotionEvent.ACTION_MOVE:
                    //When finger is moving, do nothing
                    //swipeDrawLine=true;
                    break;
                case MotionEvent.ACTION_UP:
                    swipeDrawLine=true;
                    allowCheckingWinLose=true;

                    //When finger is up, get its x and y coordinate
                    finalXco=(int)e.getX();
                    finalYco=(int)e.getY();
                    //long delayTimeTouch = e.getEventTime()-e.getDownTime(); //delay between pressing
                    //and leaving screen
                    distX=finalXco-startXco;
                    distY=finalYco-startYco;

                    if(Math.abs(distX)>Math.abs(distY)){

                        if(Math.abs(distX)>20){
                            if(distX>0){
                               //Log.e(TAG, "Swiping to Right");

                                if(holdX_for_path <=holdWidth-dePixel-xShift) {
                                    path.lineTo(holdX_for_path + dePixel, holdY_for_path + 0);
                                    holdX_for_path = holdX_for_path + dePixel;
                                    holdY_for_path = holdY_for_path + 0;
                                    holdUserLineX.add(holdX_for_path);//save the user input x coordinate
                                    holdUserLineY.add(holdY_for_path); //save the user input y coordinate

                                }
                            }else{
                                if(holdX_for_path >=xShift+halfdePixel) {
                                    path.lineTo(holdX_for_path - dePixel, holdY_for_path + 0);
                                    holdX_for_path = holdX_for_path - dePixel;
                                    holdUserLineX.add(holdX_for_path);//save the user input x coordinate
                                    holdUserLineY.add(holdY_for_path); //save the user input y coordinate

                                   //Log.e(TAG, "Swiping to left");

                                }
                            }

                        }

                    }else{
                        if(Math.abs(distY)>20){

                            if(distY>0){
                                if(holdY_for_path <=finalYHeight-halfdePixel) {
                                    path.lineTo(holdX_for_path + 0, holdY_for_path + dePixel);
                                    holdX_for_path = holdX_for_path + 0;
                                    holdY_for_path = holdY_for_path + dePixel;
                                    holdUserLineX.add(holdX_for_path);//save the user input x coordinate
                                    holdUserLineY.add(holdY_for_path); //save the user input y coordinate

                                   // Log.e(TAG, "Swiping Down");

                                }

                            }else{
                                if(holdY_for_path >=finalYShiftDown+halfdePixel) {
                                    path.lineTo(holdX_for_path + 0, holdY_for_path - dePixel);
                                    holdX_for_path = holdX_for_path + 0;
                                    holdY_for_path = holdY_for_path - dePixel;
                                    holdUserLineX.add(holdX_for_path);//save the user input x coordinate
                                    holdUserLineY.add(holdY_for_path); //save the user input y coordinate

                                    //Log.e(TAG, "Swiping Up");

                                }
                            }

                        }

                    }

                    break;

            }//end switch statement


        } //end if allowTouch


        return true;

    } // end method onTouchEvent

    private class MemoVectorThread extends Thread
    {

        private SurfaceHolder surfaceHolder; // for manipulating canvas
        private boolean threadIsRunning = true; // running by default

        // initializes the surface holder
        public MemoVectorThread(SurfaceHolder holder)  //the argument given to constructor is SurfaceHolder
        {
            surfaceHolder = holder;
           // setName("memoThread"); //Sets the name of the Thread.
        }

        // changes running state
        public void setRunning(boolean running)
        {
            threadIsRunning = running; //see the default value above
        }

        // controls the game loop
        @Override
        public void run() //Calls the run() method of the Runnable object the receiver holds.
        {
            Canvas canvas = null; // used for drawing
            long previousFrameTime = System.currentTimeMillis();
            //Log.e(TAG, "this is run()");
            while (threadIsRunning)
            {
                try
                {
                    // get Canvas for exclusive drawing from this thread
                    canvas = surfaceHolder.lockCanvas(null); //locking our canvas and Start editing the pixels in the surface.
                    //Start editing the pixels in the surface.

                    // lock the surfaceHolder for drawing
                    synchronized(surfaceHolder)
                    {
                        long currentTime = System.currentTimeMillis();
                        double elapsedTimeMS = currentTime - previousFrameTime;
                        totalElapsedTime += elapsedTimeMS / 1000.0;
                        updatePositions(elapsedTimeMS); // update game state
                        drawGameElements(canvas); // draw using the canvas
                        previousFrameTime = currentTime; // update previous time
                    }
                }
                finally
                {

                    if (canvas != null)
                        surfaceHolder.unlockCanvasAndPost(canvas);

                }
            } // end while
        } // end method run
    } // end nested class CannonThread


    //generate random number
    public void generate_random_number(){

        sequencex.add(save_x);
        sequencey.add(save_y);
        int step_x=0;
        int step_y=0;
        int p_xel=dePixel;

        Random r = new Random();
        for(int i=0; i<n; i++) {

            int index = r.nextInt(4);
            //Log.i("dibugging ", " pressed pressed  "+index);

            switch (index){

                case 0:
                    step_x=p_xel;
                    step_y=0;
                    break;
                case 1:
                    step_x=0;
                    step_y=p_xel;
                    break;
                case 2:
                    step_x=-1*p_xel;
                    step_y=0;
                    break;
                case 3:
                    step_x=0;
                    step_y=-1*p_xel;
                    break;

            }

            /*Log.e(TAG, "this is initial_x_draw "+initial_x_draw);
              Log.e(TAG, "this is initial_y_draw "+initial_y_draw);
              Log.e(TAG, "this is initial_x_draw "+new_x_draw);
              Log.e(TAG, "this is initial_y_draw "+new_y_draw);*/
            new_x=initial_x+step_x;
            new_y=initial_y+step_y;

            //compensate if there is repetition or bouncing
            if(i>0) {

                //Compensate for repetition
                if (new_x == sequencex.get(i - 1) && new_y == sequencey.get(i - 1)) {
                    //check if going to right, left, up and down
                    // Log.e("checking compensation", "I am inside checking " + "I am inside");
                    //if going to right compensate by continuing to right
                    if (sequencex.get(i) > sequencex.get(i - 1)) {
                        // Log.e("checking compensation", "I am inside checking " + "iam right");
                        int getDirection[] = generate_3_random(1);
                        new_x = initial_x+getDirection[0];
                        new_y = initial_y+getDirection[1];

                    } else if (sequencex.get(i) < sequencex.get(i - 1)) {
                        //going to left compensate by continue going to left
                        // Log.e("checking compensation", "I am inside checking " + "iam left");
                        int getDirection[] = generate_3_random(2);
                        new_x = initial_x+getDirection[0];
                        new_y =initial_y +getDirection[1];

                    } else if (sequencey.get(i) < sequencey.get(i - 1)) {
                        //if going up continue going up
                        // Log.e("checking compensation", "I am inside checking " + "iam up");
                        int getDirection[] = generate_3_random(3);
                        new_x = initial_x+getDirection[0];
                        new_y = initial_y+getDirection[1];

                    } else if (sequencey.get(i) > sequencey.get(i - 1)) {
                        //if going down continue going down
                        //Log.e("checking compensation", "I am inside checking " + "iam down");
                        int getDirection[] = generate_3_random(4);
                        new_x = initial_x+getDirection[0];
                        new_y = initial_y+ getDirection[1];

                    }

                } //end checking repetition

                //compensate for bouncing on right or left or down or up  side of grid square
                if((new_x> holdWidth - xShift)||new_x<xShift){

                    //if bouncing on right side
                    if(new_x>holdWidth-xShift){
                        //Log.e("checking bouncing", "I am going through right side " + "right side");
                        //if going to right compensate by continuing to up or down
                        if (sequencex.get(i) > sequencex.get(i - 1)){

                            if(((sequencey.get(i)-finalYShiftDown)>halfdePixel+2)&&(Math.abs(sequencey.get(i)-finalYHeight)>halfdePixel+2)) {
                                int getWallDirection[] = generate_Wall_random(1); //1-->going to up or down
                                new_x = initial_x + getWallDirection[0]; //assign the new x coordinate
                                new_y = initial_y + getWallDirection[1]; //assign the new y coordinate
                               // Log.e("right side bouncing", "I am correcting " + "up or down");
                            } else if((sequencey.get(i)-yShift)<halfdePixel+2){

                                new_x = initial_x +0 ; //assign the new x coordinate
                                new_y = initial_y + 1*dePixel; //assign the new y coordinate
                                //Log.e("right side bouncing", "I am correcting " + "down");

                            }else{
                                new_x = initial_x +0 ; //assign the new x coordinate
                                new_y = initial_y + (-1*dePixel); //assign the new y coordinate
                                //Log.e("right side bouncing", "I am correcting " + "up");

                            }

                        } //end compensation for going right side
                        else if(sequencey.get(i)>sequencey.get(i-1)){
                            //if go down, go down or left
                            if(((sequencey.get(i)-finalYShiftDown)>halfdePixel+2)&&(Math.abs(sequencey.get(i)-finalYHeight)>halfdePixel+2)) {
                                int getTwoDirection[] = generate_Two_random(1); //1-->going to down or left
                                new_x = initial_x + getTwoDirection[0]; //assign the new x coordinate
                                new_y = initial_y + getTwoDirection[1]; //assign the new y coordinate
                               //Log.e("right side bouncing", "I am correcting " + "down or left");
                            } else{
                                //else go left
                                new_x = initial_x + (-1*dePixel); //assign the new x coordinate
                                new_y = initial_y + 0; //assign the new y coordinate
                                //Log.e("right side bouncing", "I am correcting " + "left_2 for right side");
                            }

                        }else{
                            //if going up, go up or left
                            if(((sequencey.get(i)-finalYShiftDown)>halfdePixel+2)&&(Math.abs(sequencey.get(i)-finalYHeight)>halfdePixel+2)) {
                                int getTwoDirection[] = generate_Two_random(2); //1-->going to up or left
                                new_x = initial_x + getTwoDirection[0]; //assign the new x coordinate
                                new_y = initial_y + getTwoDirection[1]; //assign the new y coordinate
                                //Log.e("right side bouncing", "I am correcting " + "up or left");
                            }else{
                                //else go left
                                new_x = initial_x + (-1*dePixel); //assign the new x coordinate
                                new_y = initial_y + 0; //assign the new y coordinate
                                //Log.e("right side bouncing", "I am correcting " + "left_3 for right side");
                            }
                        }


                    }else{ //if bouncing on left side

                       // Log.e("checking bouncing", "I am going through left side " + "left side");
                        if (sequencex.get(i) < sequencex.get(i - 1)) {

                            if(((sequencey.get(i)-finalYShiftDown)>halfdePixel+2)&&(Math.abs(sequencey.get(i)-finalYHeight)>halfdePixel+2)) {

                                int getWallDirection[] = generate_Wall_random(1);//1-->going to right or left
                                new_x = initial_x + getWallDirection[0]; //assign the new x coordinate
                                new_y = initial_y + getWallDirection[1]; //assign the new y coordinate
                               // Log.e("left side bouncing", "I am correcting " + "up or down");
                            }else if((sequencey.get(i)-finalYShiftDown)<halfdePixel+2){
                                //go down
                                new_x = initial_x +0 ; //assign the new x coordinate
                                new_y = initial_y + 1*dePixel; //assign the new y coordinate
                                //Log.e("left side bouncing", "I am correcting " + "down");

                            }else{
                                //go up
                                new_x = initial_x +0 ; //assign the new x coordinate
                                new_y = initial_y + (-1*dePixel); //assign the new y coordinate
                               // Log.e("checking bouncing", "I am correcting " + "up");

                            }
                        }else if(sequencey.get(i)>sequencey.get(i-1)){
                            //going down so you either go down or right
                            if(((sequencey.get(i)-finalYShiftDown)>halfdePixel+2)&&(Math.abs(sequencey.get(i)-finalYShiftDown)>halfdePixel+2)) {
                                int getTwoDirection[] = generate_Two_random(3); //1-->going to down or right
                                new_x = initial_x + getTwoDirection[0]; //assign the new x coordinate
                                new_y = initial_y + getTwoDirection[1]; //assign the new y coordinate
                               // Log.e("left side bouncing", "I am correcting " + "down or right");
                            }else{
                                //else go right
                                new_x = initial_x + 1*dePixel; //assign the new x coordinate
                                new_y = initial_y + 0; //assign the new y coordinate
                               // Log.e("left side bouncing", "I am correcting " + "right_2 for left side");
                            }

                        }else{
                            if(((sequencey.get(i)-finalYShiftDown)>halfdePixel+2)&&(Math.abs(sequencey.get(i)-finalYShiftDown)>halfdePixel+2)) {
                                int getTwoDirection[] = generate_Two_random(4); //1-->going to up or right
                                new_x = initial_x + getTwoDirection[0]; //assign the new x coordinate
                                new_y = initial_y + getTwoDirection[1]; //assign the new y coordinate
                               // Log.e("left side bouncing", "I am correcting " + "up or right");
                            }else{
                                //else go right
                                new_x = initial_x + 1*dePixel; //assign the new x coordinate
                                new_y = initial_y + 0; //assign the new y coordinate
                               // Log.e("left side bouncing", "I am correcting " + "right_3 for left side");
                            }
                        }
                    }

                }else if((new_y> finalYHeight)||new_y<finalYShiftDown){

                    //if bouncing on down side

                    if(new_y>finalYHeight){
                        //Log.e("down side bouncing", "I am going through down side " + "down side");

                        //going down, so turn it to left or right
                        if (sequencey.get(i) > sequencey.get(i - 1)) {

                            if(((sequencex.get(i)-xShift)>halfdePixel+2)&&(Math.abs(sequencex.get(i)-(holdWidth-xShift))>halfdePixel+2)) {
                                int getWallDirection[] = generate_Wall_random(2); //2-->going to right or left
                                new_x = initial_x + getWallDirection[0]; //assign the new x coordinate
                                new_y = initial_y + getWallDirection[1]; //assign the new y coordinate
                                //Log.e("down side bouncing", "I am correcting " + "left or right");
                            }else if((sequencex.get(i)-xShift)<halfdePixel+2){
                                //go right
                                new_x = initial_x +1*dePixel ; //assign the new x coordinate
                                new_y = initial_y + 0; //assign the new y coordinate
                               // Log.e("down side bouncing", "I am correcting " + "right");

                            }else{
                                //go left
                                new_x = initial_x +(-1*dePixel) ; //assign the new x coordinate
                                new_y = initial_y + 0; //assign the new y coordinate
                                //Log.e("down side bouncing", "I am correcting " + "left");

                            }
                        }else if(sequencex.get(i)>sequencex.get(i-1)){
                            //coming from the left=going right
                            if(((sequencex.get(i)-xShift)>halfdePixel+2)&&(Math.abs(sequencex.get(i)-(holdWidth-xShift))>halfdePixel+2)) {
                                int getTwoDirection[] = generate_Two_random(4); //1-->going to up or right
                                new_x = initial_x + getTwoDirection[0]; //assign the new x coordinate
                                new_y = initial_y + getTwoDirection[1]; //assign the new y coordinate
                                //Log.e("down side bouncing", "I am correcting " + "up or right");
                            }else{

                                //else go up
                                new_x = initial_x +0 ; //assign the new x coordinate
                                new_y = initial_y + -1*dePixel; //assign the new y coordinate
                                //Log.e("down side bouncing", "I am correcting " + "up_2 for down side");
                            }

                        }else{

                            //coming from the right=going left
                            if(((sequencex.get(i)-xShift)>halfdePixel+2)&&(Math.abs(sequencex.get(i)-(holdWidth-xShift))>halfdePixel+2)) {
                                int getTwoDirection[] = generate_Two_random(2); //1-->going to up or left
                                new_x = initial_x + getTwoDirection[0]; //assign the new x coordinate
                                new_y = initial_y + getTwoDirection[1]; //assign the new y coordinate
                                //Log.e("down side bouncing", "I am correcting " + "up or left");
                            }else{

                                //else go up
                                new_x = initial_x +0 ; //assign the new x coordinate
                                new_y = initial_y + -1*dePixel; //assign the new y coordinate
                                //Log.e("down side bouncing", "I am correcting " + "up_3 for down side");
                            }

                        }

                    }else{ //if bouncing on up side, go to right or left

                       // Log.e("checking bouncing", "I am going through up side " + "up side");
                        if (sequencey.get(i) < sequencey.get(i - 1)) {
                            //go right or left

                            if(((sequencex.get(i)-xShift)>halfdePixel+2)&&(Math.abs(sequencex.get(i)-(holdWidth-xShift))>halfdePixel+2)) {
                                int getWallDirection[] = generate_Wall_random(2);//2-->going to right or left
                                new_x = initial_x + getWallDirection[0]; //assign the new x coordinate
                                new_y = initial_y + getWallDirection[1]; //assign the new y coordinate
                                //Log.e("up side bouncing", "I am correcting " + "left or right");
                            }else if((sequencex.get(i)-xShift)<halfdePixel+2){

                                new_x = initial_x +1*dePixel ; //assign the new x coordinate
                                new_y = initial_y + 0; //assign the new y coordinate
                               // Log.e("up side bouncing", "I am correcting " + "right");

                            }else{
                                new_x = initial_x +(-1*dePixel) ; //assign the new x coordinate
                                new_y = initial_y + 0; //assign the new y coordinate
                                //Log.e("upside bouncing", "I am correcting " + "left");

                            }


                        }else if(sequencex.get(i)>sequencex.get(i-1)){
                            //coming from the left=going right
                            if(((sequencex.get(i)-xShift)>halfdePixel+2)&&(Math.abs(sequencex.get(i)-(holdWidth-xShift))>halfdePixel+2)) {
                                int getTwoDirection[] = generate_Two_random(3); //1-->going to down or right
                                new_x = initial_x + getTwoDirection[0]; //assign the new x coordinate
                                new_y = initial_y + getTwoDirection[1]; //assign the new y coordinate
                                //Log.e("up side bouncing", "I am correcting " + "down or right");
                            }else{
                                //going down
                                new_x = initial_x +0 ; //assign the new x coordinate
                                new_y = initial_y + 1*dePixel; //assign the new y coordinate
                                //Log.e("up side bouncing", "I am correcting " + "down_2 for up side");

                            }

                        }else{
                            //coming from the right=going left
                            if(((sequencex.get(i)-xShift)>halfdePixel+2)&&(Math.abs(sequencex.get(i)-(holdWidth-xShift))>halfdePixel+2)) {
                                int getTwoDirection[] = generate_Two_random(1); //1-->going to down or left
                                new_x = initial_x + getTwoDirection[0]; //assign the new x coordinate
                                new_y = initial_y + getTwoDirection[1]; //assign the new y coordinate
                               // Log.e("up side bouncing", "I am correcting " + "down or left");
                            }else{
                                //going down
                                new_x = initial_x +0 ; //assign the new x coordinate
                                new_y = initial_y + 1*dePixel; //assign the new y coordinate
                                //Log.e("up side bouncing", "I am correcting " + "down_3 for up side");
                            }
                        }

                    }

                }
                //end compensating bouncing



            } //end if (i>0)


            sequencex.add(new_x);
            sequencey.add(new_y);
            initial_x=new_x;
            initial_y=new_y;


        }


    } //end generate_random_number()


    //generat 3 random for going right, left up or down

    public int[] generate_3_random(int selectDirection) {

        int r_step_x = 0;
        int r_step_y = 0;
        int r_p_xel = dePixel;

        Random rr = new Random();
        int indexs = rr.nextInt(3); //either 0 or 1 or 2
        //Log.i("dibugging ", " pressed pressed  "+index);

        //1-->goes right
        //2-goes left
        //3-goes up
        //4-goes down


        //going right, you can go to right, up or down
        if(selectDirection==1) {
            switch (indexs) {

                case 0:
                    //go forward--right
                    r_step_x = 1 * r_p_xel;
                    r_step_y = 0;
                    break;
                case 1:
                    //go up
                    r_step_x = 0;
                    r_step_y =1* r_p_xel;
                    break;
                case 2:
                    //go down
                    r_step_x = 0;
                    r_step_y = -1 * r_p_xel;
                    break;

            }
        }else if(selectDirection==2){ //going left-backward, you go left or up or down
            switch (indexs) {

                case 0:
                    //go left, backward
                    r_step_x = -1 * r_p_xel;
                    r_step_y = 0;
                    break;
                case 1:
                    //go up
                    r_step_x = 0;
                    r_step_y =1* r_p_xel;
                    break;
                case 2:
                    //go down
                    r_step_x = 0;
                    r_step_y = -1 * r_p_xel;
                    break;

            }

        }else if(selectDirection==3){ //go up, you can go up, left or right
            switch (indexs) {

                case 0:
                    //go right
                    r_step_x = 1 * r_p_xel;
                    r_step_y = 0;
                    break;
                case 1:
                    //go left
                    r_step_x = -1 * r_p_xel;
                    r_step_y =0;
                    break;
                case 2:
                    //go up
                    r_step_x = 0;
                    r_step_y = -1 * r_p_xel; //this goes up
                    break;

            }

        }else if(selectDirection==4) {//go down, you can go down, left or right
            switch (indexs) {

                case 0:
                    //go right
                    r_step_x = 1 * r_p_xel;
                    r_step_y = 0;
                    break;
                case 1:
                    //go left
                    r_step_x = -1 * r_p_xel;
                    r_step_y = 0;
                    break;
                case 2:
                    //go down
                    r_step_x = 0;
                    r_step_y = 1 * r_p_xel; //going down
                    break;

            }


        }


        // Log.e("returned value", "step_x " +r_step_x+ "step_y "+r_step_y);
        return new int[]{r_step_x, r_step_y}; //return new x and y coordinate

    } //end generate_3_random method

    //generate 2 random for going up or down when bouncing to screen wall
    public int[] generate_Wall_random(int selectWallDirection){
        int wall_step_x = 0;
        int wall_step_y = 0;
        int wall_p_xel = dePixel;

        Random wall_r = new Random();
        int wall_index = wall_r.nextInt(2); //either 0 or 1


        //1-->goes right
        //2-goes left
        //3-goes up
        //4-goes down


        //going right, you can go to right, up or down
        if(selectWallDirection==1) {
            switch (wall_index) {

                case 0:
                    //go down
                    wall_step_x = 0;
                    wall_step_y =1* wall_p_xel;
                    break;
                case 1:
                    //go up
                    wall_step_x = 0;
                    wall_step_y =-1* wall_p_xel;
                    break;

            }
        }else if(selectWallDirection==2){ //going up or down, you go left or right now
            switch (wall_index) {

                case 0:
                    //go right
                    wall_step_x = 1*wall_p_xel;
                    wall_step_y =0;
                    break;
                case 1:
                    //go left
                    wall_step_x = -1*wall_p_xel;
                    wall_step_y =0;
                    break;

            }

        }

        // Log.e("returned value", "step_x " +r_step_x+ "step_y "+r_step_y);
        return new int[]{wall_step_x, wall_step_y}; //return new x and y coordinate

    } //end generate_Wall_random method

    //generate 3 random for going up

    //generate 2 random for going up or down when bouncing to screen wall
    public int[] generate_Two_random(int selectTwoDirection){
        int two_step_x = 0;
        int two_step_y = 0;
        int two_p_xel = dePixel;

        Random two_r = new Random();
        int two_index = two_r.nextInt(2); //either 0 or 1


        //1-->goes right
        //2-goes left
        //3-goes up
        //4-goes down

        if(selectTwoDirection==1) {       //for right side: going down, you can go to left, or down
            switch (two_index) {

                case 0:
                    //go down
                    two_step_x = 0;
                    two_step_y=1* two_p_xel;
                    break;
                case 1:
                    //go left
                    two_step_x= -1* two_p_xel;
                    two_step_y =0;
                    break;

            }
        }else if(selectTwoDirection==2){ //for right side: going up, you go left or up
            switch (two_index) {

                case 0:
                    //go up
                    two_step_x = 0;
                    two_step_y=-1* two_p_xel;
                    break;
                case 1:
                    //go left
                    two_step_x= -1* two_p_xel;
                    two_step_y =0;
                    break;

            }

        }else if(selectTwoDirection==3) { //for left side: going down, you go down or right
            switch (two_index) {

                case 0:
                    //go right
                    two_step_x = 1 * two_p_xel;
                    two_step_y = 0;
                    break;
                case 1:
                    //go down
                    two_step_x = 0;
                    two_step_y = 1 * two_p_xel;
                    break;

            }
        }else if(selectTwoDirection==4) { //for left side: going up, you go up or right
            switch (two_index) {

                case 0:
                    //go right
                    two_step_x = 1 * two_p_xel;
                    two_step_y = 0;
                    break;
                case 1:
                    //go up
                    two_step_x =0;
                    two_step_y = -1 * two_p_xel;
                    break;

            }
        }

        // Log.e("returned value", "step_x " +r_step_x+ "step_y "+r_step_y);
        return new int[]{two_step_x, two_step_y}; //return new x and y coordinate

    } //end generate_Wall_random method

    //generate 3 random for going down



    public void check_direction() {



        //check if direction is right or left
        if(initial_y_draw==final_y_draw){

            //check if direction is right
            if(final_x_draw>initial_x_draw) {
                speed_X = initial_x_draw;
                speed_Y = initial_y_draw;
                addingPlus = finalSpeedAnimation; //animation speed to right
                goes_right= true; //direction is right
                draw_right_left=true; //allow draw for right and left direction
            }else if(final_x_draw<initial_x_draw){ //check if direction is left

                speed_X = initial_x_draw;
                speed_Y = initial_y_draw;
                addingPlus = -finalSpeedAnimation; //animation speed to the left
                goes_left = true; //direction is right
                draw_right_left=true; //allow draw for right and left direction

            }

        } //check if direction is up or down
        else if(initial_x_draw==final_x_draw){
            //check if direccion is down
            if(final_y_draw>initial_y_draw) {
                speed_X = initial_x_draw;
                addingPlus = finalSpeedAnimation;
                speed_Y = initial_y_draw;
                goes_down = true;
                draw_right_left=true;
                draw_down_up=true;
            }else if(final_y_draw<initial_y_draw){ //check if direction is up
                speed_X = initial_x_draw;
                addingPlus = -finalSpeedAnimation;
                speed_Y = initial_y_draw;
                goes_up = true;
                draw_down_up=true;
                draw_right_left=true;
            }
        }
    }//end check_direction() method


}//End GameEngineView class
