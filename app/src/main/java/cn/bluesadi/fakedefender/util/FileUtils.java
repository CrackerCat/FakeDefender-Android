package cn.bluesadi.fakedefender.util;

import android.graphics.Bitmap;
import android.provider.MediaStore;
import cn.bluesadi.fakedefender.ui.base.BaseActivity;

public class FileUtils {

    public static void saveImage(Bitmap bitmap){
        MediaStore.Images.Media.insertImage(BaseActivity.getTopActivity().getContentResolver(), bitmap, null, null);
    }

}
