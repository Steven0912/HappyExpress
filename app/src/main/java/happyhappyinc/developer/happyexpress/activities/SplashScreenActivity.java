package happyhappyinc.developer.happyexpress.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.RequestBuilder;

import happyhappyinc.developer.happyexpress.R;
import happyhappyinc.developer.happyexpress.utils.Utils;

public class SplashScreenActivity extends AppCompatActivity {

    private Context mContext;
    private RequestBuilder<PictureDrawable> requestBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mContext = this;
        ImageView imageView = (ImageView) findViewById(R.id.iv_icon_happy);

        new Utils().loadSVG(mContext, R.raw.icon_happy, imageView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
