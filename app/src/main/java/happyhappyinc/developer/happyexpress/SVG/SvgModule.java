package happyhappyinc.developer.happyexpress.SVG;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;

import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.caverock.androidsvg.SVG;

import java.io.InputStream;

/**
 * Created by Steven on 10/07/2017.
 */

@GlideModule
public class SvgModule extends AppGlideModule {
    @Override
    public void registerComponents(Context context, Registry registry) {
        registry.register(SVG.class, PictureDrawable.class, new SvgDrawableTranscoder())
                .append(InputStream.class, SVG.class, new SvgDecoder());
    }

    // Disable manifest parsing to avoid adding similar modules twice.
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
