package permoveo.com.wikisearch.activity;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import permoveo.com.wikisearch.R;
import permoveo.com.wikisearch.model.SearchPage;

import static permoveo.com.wikisearch.fragment.MainActivityFragment.EXTRA_SEARCH_PAGE;
import static permoveo.com.wikisearch.fragment.MainActivityFragment.EXTRA_VIEW_HEIGHT;
import static permoveo.com.wikisearch.fragment.MainActivityFragment.EXTRA_VIEW_LEFT;
import static permoveo.com.wikisearch.fragment.MainActivityFragment.EXTRA_VIEW_TOP;
import static permoveo.com.wikisearch.fragment.MainActivityFragment.EXTRA_VIEW_WIDTH;

/**
 * Created by byfieldj on 5/22/17.
 */

public class ImageInfoActivity extends Activity {


    @Bind(R.id.ivImage)
    ImageView mImage;

    @Bind(R.id.tvImageHeight)
    TextView mImageHeight;

    @Bind(R.id.tvImageWidth)
    TextView mImageWidth;

    @Bind(R.id.tvImageUrl)
    TextView mImageSource;

    @Bind(R.id.tvTitle)
    TextView mTitle;

    @Bind(R.id.rlContainer)
    RelativeLayout mContainer;

    private String mImageUrl;
    private Bundle mStartValues;

    // Bundle that will contain the transition end values
    private Bundle mEndValues = new Bundle();

    private static final AccelerateDecelerateInterpolator DEFAULT_INTERPOLATOR = new AccelerateDecelerateInterpolator();

    // Default animation duration
    private static final int DEFAULT_DURATION = 400;

    private SearchPage mSearchPage;

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("ImageInfoActivity", "OnResume");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_info);
        ButterKnife.bind(this);
        Log.d("ImageInfoActivity", "onCreate");


        if (getIntent().hasExtra("Page Info")) {

            mStartValues = getIntent().getBundleExtra("Page Info");

            Log.d("ImageInfoActivity", "Found extras");


            if (getIntent().getExtras().getSerializable(EXTRA_SEARCH_PAGE) != null) {

                Log.d("ImageInfoActivity", "Found search page");

                mSearchPage = (SearchPage) getIntent().getExtras().getSerializable(EXTRA_SEARCH_PAGE);
                if (mSearchPage != null) {
                    mImageUrl = mSearchPage.getImage().getSource();
                    Log.d("ImageInfoActivity", "mImageUrl -> " + mImageUrl);
                }

            }

        }

        Picasso.with(this).load(mImageUrl)
                .fit().centerCrop().into(mImage, new Callback() {
            @Override
            public void onSuccess() {

                onUiReady();
            }

            @Override
            public void onError() {

            }
        });
    }

    private void onUiReady() {
        Log.d("ImageInfoActivity", "OnUiReady");
        mImage.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                // remove previous listener
                mImage.getViewTreeObserver().removeOnPreDrawListener(this);
                // prep the scene
                prepareScene();
                // run the animation
                runEnterAnimation();
                return true;
            }
        });
    }

    private void runEnterAnimation() {

        // Let's make our views visible
        mImage.setVisibility(View.VISIBLE);
        mTitle.setVisibility(View.VISIBLE);
        mImageHeight.setVisibility(View.VISIBLE);
        mImageWidth.setVisibility(View.VISIBLE);
        mImageSource.setVisibility(View.VISIBLE);
        mContainer.setVisibility(View.VISIBLE);


        mContainer.setBackgroundColor((ContextCompat.getColor(this, android.R.color.white)));

        // Finally, run the animation
        mImage.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                // Add a little "kick" at the end
                YoYo.with(Techniques.RubberBand).duration(300).playOn(mImage);

                mTitle.setText(mSearchPage.getTitle());
                mImageWidth.setText("Image Width: " + mSearchPage.getImage().getWidth());
                mImageHeight.setText("Image Height: " + mSearchPage.getImage().getHeight());
                mImageSource.setText("Image Url: " + mSearchPage.getImage().getSource());


            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        })
                .setDuration(DEFAULT_DURATION)
                .setInterpolator(DEFAULT_INTERPOLATOR)
                .scaleX(1f)
                .scaleY(1f)
                .translationX(0)
                .translationY(0)
                .start();
    }


    private void runExitAnimation() {
        // re-calculate deltas
        int deltaX = translationDelta(mStartValues, mEndValues, EXTRA_VIEW_LEFT);
        int deltaY = translationDelta(mStartValues, mEndValues, EXTRA_VIEW_TOP);
        float scaleX = scaleDelta(mStartValues, mEndValues, EXTRA_VIEW_WIDTH);
        float scaleY = scaleDelta(mStartValues, mEndValues, EXTRA_VIEW_HEIGHT);

        mImage.animate()
                .setDuration(DEFAULT_DURATION)
                .setInterpolator(DEFAULT_INTERPOLATOR)
                .scaleX(scaleX)
                .scaleY(scaleY)
                .translationX(deltaX)
                .translationY(deltaY)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        overridePendingTransition(0, 0);
                    }
                }).start();
    }

    private void prepareScene() {

        // capture the end values in the destination view
        mEndValues = captureValues(mImage);

        // calculate the scale and position deltas
        float scaleX = scaleDelta(mStartValues, mEndValues, EXTRA_VIEW_WIDTH);
        float scaleY = scaleDelta(mStartValues, mEndValues, EXTRA_VIEW_HEIGHT);
        int deltaX = translationDelta(mStartValues, mEndValues, EXTRA_VIEW_TOP);
        int deltaY = translationDelta(mStartValues, mEndValues, EXTRA_VIEW_LEFT);

        // scale and reposition the image
        mImage.setScaleX(scaleX);
        mImage.setScaleY(scaleY);
        mImage.setTranslationX(deltaX);
        mImage.setTranslationY(deltaY);
    }

    private Bundle captureValues(ImageView image) {
        Bundle b = new Bundle();
        int[] screenLocation = new int[2];
        image.getLocationOnScreen(screenLocation);
        b.putInt(EXTRA_VIEW_LEFT, screenLocation[0]);
        b.putInt(EXTRA_VIEW_TOP, screenLocation[1]);
        b.putInt(EXTRA_VIEW_WIDTH, image.getWidth());
        b.putInt(EXTRA_VIEW_HEIGHT, image.getHeight());
        return b;

    }

    @Override
    public void onBackPressed() {
        // run the exit animation
        runExitAnimation();
    }

    private float scaleDelta(
            @NonNull Bundle startValues,
            @NonNull Bundle endValues,
            @NonNull String propertyName) {

        int startValue = startValues.getInt(propertyName);
        int endValue = endValues.getInt(propertyName);
        float delta = (float) startValue / endValue;


        return delta;
    }

    private int translationDelta(
            @NonNull Bundle startValues,
            @NonNull Bundle endValues,
            @NonNull String propertyName) {

        int startValue = startValues.getInt(propertyName);
        int endValue = endValues.getInt(propertyName);
        int delta = startValue - endValue;


        return delta;
    }


}



