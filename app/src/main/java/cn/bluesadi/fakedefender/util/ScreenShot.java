package cn.bluesadi.fakedefender.util;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import java.nio.ByteBuffer;

import static android.app.Activity.RESULT_OK;

public class ScreenShot {

    private final Service service;
    private final Intent resultData;
    private MediaProjection mediaProjection;
    private VirtualDisplay virtualDisplay;
    private final ImageReader imageReader;
    private final int screenWidth;
    private final int screenHeight;
    private final int screenDensity;

    @SuppressLint("WrongConstant")
    public ScreenShot(Service service, Intent resultData){
        this.service = service;
        this.resultData = resultData;
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) service.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        screenDensity = metrics.densityDpi;
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        imageReader = ImageReader.newInstance(screenWidth, screenHeight, PixelFormat.RGBA_8888, 1);
    }

    public void destroy(){
        virtualDisplay.release();
        imageReader.close();
        mediaProjection.stop();
    }

    private void setUpMediaProjection() {
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager)
                service.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        mediaProjection = mediaProjectionManager.getMediaProjection(RESULT_OK, resultData);
    }

    private void startVirtual() {
        if (mediaProjection == null) {
            setUpMediaProjection();
        }
        virtualDisplay();
    }

    private void virtualDisplay() {
        virtualDisplay = mediaProjection.createVirtualDisplay("screen-mirror",
                screenWidth, screenHeight, screenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                imageReader.getSurface(), null, null);
    }

    public void startScreenShot() {
        startVirtual();
    }

    public Bitmap capture(){
        try (Image image = imageReader.acquireLatestImage()) {
            if (image != null) {
                int width = image.getWidth();
                int height = image.getHeight();
                final Image.Plane[] planes = image.getPlanes();
                final ByteBuffer buffer = planes[0].getBuffer();
                int pixelStride = planes[0].getPixelStride();
                int rowStride = planes[0].getRowStride();
                int rowPadding = rowStride - pixelStride * width;
                Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
                bitmap.copyPixelsFromBuffer(buffer);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
                image.close();
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
