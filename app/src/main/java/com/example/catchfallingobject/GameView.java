package com.example.catchfallingobject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class GameView extends View {

    private int screenWidth, screenHeight;
    private int basketX, basketY, basketWidth, basketHeight;
    private int objectX, objectY, objectSize;
    private int boomX, boomY, boomSize;
    private int score = 0;
    private boolean isGameOver = false;
    private Paint paint;
    private Random random;

    private Bitmap basketBitmap;
    private Bitmap objectBitmap;
    private Bitmap boomBitmap;
    private Bitmap restartButtonBitmap;
    private Bitmap bgImageBitmap;

    private Rect restartButtonRect;
    private float objectSpeed = 10; // Initial speed of the falling object
    private float objectRotation = 0; // Current rotation angle of the object
    private float boomSpeed = 10; // Initial speed of the falling object
    private float boomRotation = 0;
    private int spawnInterval = 200; // Spawn interval for new objects and booms
    private final int minSpawnInterval = 50; // Minimum spawn interval
/*
    public GameView(Context context) {
        super(context);
        paint = new Paint();
        random = new Random();

        // Load images
        basketBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_menu);
        objectBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.obj);
        boomBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.boom);
        restartButtonBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.restart);

        // Scale basket image
        basketWidth = 200;
        basketHeight = 100;
        basketBitmap = Bitmap.createScaledBitmap(basketBitmap, basketWidth, basketHeight, false);

        // Scale object image
        objectSize = 100;
        objectBitmap = Bitmap.createScaledBitmap(objectBitmap, objectSize, objectSize, false);

        // Scale boom image
        boomSize = 100;
        boomBitmap = Bitmap.createScaledBitmap(boomBitmap, boomSize, boomSize, false);

        // Scale restart button
        restartButtonBitmap = Bitmap.createScaledBitmap(restartButtonBitmap, 300, 150, false);
    }
*/
public GameView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context); // Call a method to initialize the view
}

    private void init(Context context) {
        // Perform your initialization here
        paint = new Paint();
        random = new Random();

        // Load images
        basketBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.basket);
        objectBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.obj);
        boomBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.boom);
        restartButtonBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.restart);
        bgImageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bgimage);

        // Scale basket image
        basketWidth = 300;
        basketHeight = 300;
        basketBitmap = Bitmap.createScaledBitmap(basketBitmap, basketWidth, basketHeight, false);

        // Scale object image
        objectSize = 150;
        objectBitmap = Bitmap.createScaledBitmap(objectBitmap, objectSize, objectSize, false);

        // Scale boom image
        boomSize = 150;
        boomBitmap = Bitmap.createScaledBitmap(boomBitmap, boomSize, boomSize, false);

        // Scale restart button
        restartButtonBitmap = Bitmap.createScaledBitmap(restartButtonBitmap, 300, 150, false);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w;
        screenHeight = h;

        // Scale background image to fit the screen
        bgImageBitmap = Bitmap.createScaledBitmap(bgImageBitmap, screenWidth, screenHeight, false);

        // Initialize basket position
        basketX = screenWidth / 2 - basketWidth / 2;
        basketY = screenHeight - basketHeight - 30;

        // Initialize object and boom positions
        resetObject();
        resetBoom();

        // Initialize restart button rectangle
        restartButtonRect = new Rect(
                screenWidth / 2 - 150,
                screenHeight / 2 + 100,
                screenWidth / 2 + 150,
                screenHeight / 2 + 250
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isGameOver) {
            // Draw Game Over Screen
            paint.setTextSize(80);
            canvas.drawText("Game Over!", screenWidth / 4, screenHeight / 2, paint);
            paint.setTextSize(50);
            canvas.drawText("Score: " + score, screenWidth / 3, screenHeight / 2 + 100, paint);

            // Draw restart button
            canvas.drawBitmap(restartButtonBitmap, restartButtonRect.left, restartButtonRect.top, paint);
            return;
        }

        // Draw background color
        canvas.drawBitmap(bgImageBitmap, 0, 0, paint);

        // Draw basket
        canvas.drawBitmap(basketBitmap, basketX, basketY, paint);

        // Draw falling object with rotation
        Matrix objectmatrix = new Matrix();
        objectmatrix.postRotate(objectRotation, objectSize / 2f, objectSize / 2f);
        objectmatrix.postTranslate(objectX, objectY);
        canvas.drawBitmap(objectBitmap, objectmatrix, paint);

        // Draw boom
        Matrix boommatrix = new Matrix();
        boommatrix.postRotate(boomRotation, boomSize / 2f, boomSize / 2f);
        boommatrix.postTranslate(boomX, boomY);
        canvas.drawBitmap(boomBitmap,boommatrix, paint);

        // Draw score
        paint.setTextSize(50);
        canvas.drawText("Score: " + score, 50, 100, paint);

        // Update object and boom positions
        update();
    }

    private void update() {
        objectY += objectSpeed;  // Update falling speed
        boomY += objectSpeed;   // Update boom speed
        objectRotation += 10;  // Update rotation angle
        if (objectRotation >= 360) {
            objectRotation = 0;
        }
        boomRotation += 10;  // Update boom rotation angle
        if (boomRotation >= 360) {
            boomRotation = 0;
        }

        // Gradually increase object speed
        if (score > 0 && score % 10 == 0) {
            objectSpeed = Math.min(objectSpeed + 2,20); // Cap speed at 50
        }

        // Decrease spawn interval every 50 points
        if (score > 0 && score % 15 == 0) {
            spawnInterval = Math.max(spawnInterval - 10, minSpawnInterval);
        }

        // Check if object is caught by the basket
        if (objectY + objectSize > basketY &&
                objectX + objectSize / 2 > basketX &&
                objectX + objectSize / 2 < basketX + basketWidth) {
            score++;
            resetObject();
        }

        // Check if boom is caught by the basket
        if (boomY + boomSize > basketY &&
                boomX + boomSize / 2 > basketX &&
                boomX + boomSize / 2 < basketX + basketWidth) {
            isGameOver = true;
            invalidate();  // Redraw the game view
            return;
        }

        // Check if boom missed
        if (objectY > screenHeight) {
            isGameOver = true;
            invalidate();
            return;
        }

        invalidate();  // Redraw the game view
    }

    private void resetObject() {
        objectX = random.nextInt(screenWidth - objectSize);
        objectY = 0;  // Start at the top of the screen
    }

    private void resetBoom() {
        boomX = random.nextInt(screenWidth - boomSize);
        boomY = 0;  // Start at the top of the screen
    }

    private void resetGame() {
        score = 0;
        isGameOver = false;
        objectSpeed = 10; // Reset speed
        spawnInterval = 200; // Reset spawn interval
        resetObject();
        resetBoom();
        invalidate();  // Redraw the game view
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isGameOver) {
            // Check if restart button is clicked
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (restartButtonRect.contains((int) event.getX(), (int) event.getY())) {
                    resetGame();
                }
            }
        } else {
            // Move the basket based on touch position
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                basketX = (int) event.getX() - basketWidth / 2;
                if (basketX < 0) basketX = 0;
                if (basketX + basketWidth > screenWidth) basketX = screenWidth - basketWidth;
            }
        }
        return true;
    }
}