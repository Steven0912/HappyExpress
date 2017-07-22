package happyhappyinc.developer.happyexpress.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import happyhappyinc.developer.happyexpress.SVG.SvgSoftwareLayerSetter;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by Steven on 06/07/2017.
 */

public class Utils {

    private RequestBuilder<PictureDrawable> requestBuilder;

    public static boolean validateTextFormLogin(EditText[] mListTxt) {

        String[] lblErrorLogin = {"El correo no puede estar vacío", "La contraseña no puede estar vacía"};
        boolean isOk = false;

        for (int i = 0; i < mListTxt.length; i++) {
            String txt = mListTxt[i].getText().toString().trim();
            if (txt.equals("")) {
                mListTxt[i].setError(lblErrorLogin[i]);
                isOk = false;
            } else {
                isOk = true;
                mListTxt[i].setError(null);
            }
        }

        return isOk;
    }

    public static SweetAlertDialog alertCustom(Context context, String title, String body) {
        return new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText(title)
                .setContentText(body);
    }

    public static SweetAlertDialog alertError(Context context, String body) {
        return new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(body);
    }

    public static SweetAlertDialog alertSuccess(Context context, String title, String body) {
        return new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(body);
    }

    public static SweetAlertDialog alertProgress(Context context) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(Color.parseColor("#09d4b6"));
        dialog.setTitleText("Cargando...");
        dialog.setCancelable(false);
        return dialog;
    }

    public static SweetAlertDialog alertBtns(Context context, String title, String body, String btnConfirm, String btnCancel) {
        return new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(body)
                .setConfirmText(btnConfirm)
                .setCancelText(btnCancel);
    }

    public static String getCurrentDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getCurrentTime(){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDay() {
        DateFormat dateFormat = new SimpleDateFormat("dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void loadSVG(Context context, int resource, ImageView imageView) {
        requestBuilder = Glide.with(context)
                .as(PictureDrawable.class)
                .transition(withCrossFade())
                .listener(new SvgSoftwareLayerSetter());

        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/"
                + resource);
        requestBuilder.load(uri).into(imageView);
    }
}
