package cn.bluesadi.fakedefender.defender;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.bluesadi.fakedefender.util.FileUtils;
import cn.bluesadi.fakedefender.util.UIHelper;

public class CheckRecord {

    public static class Face{
        int x1, y1, x2, y2;
        double score;

        public Face(JSONObject jsonObject){
            this.x1 = jsonObject.getIntValue("x1");
            this.y1 = jsonObject.getIntValue("y1");
            this.x2 = jsonObject.getIntValue("x2");
            this.y2 = jsonObject.getIntValue("y2");
            this.score = jsonObject.getDouble("score");
        }

        @Override
        public String toString() {
            return "Face{" +
                    "x1=" + x1 +
                    ", y1=" + y1 +
                    ", x2=" + x2 +
                    ", y2=" + y2 +
                    ", score=" + score +
                    '}';
        }
    }

    private Bitmap resultImage;
    private List<Face> faces;
    private final Date checkTime;
    private final boolean manual;

    public CheckRecord(Bitmap rawImage, JSONObject jsonResult, Date checkTime, boolean manual){
        this.checkTime = checkTime;
        this.manual = manual;
        this.resultImage = rawImage.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(resultImage);
        Paint paint = new Paint();
        paint.setTextSize(50);
        paint.setStrokeWidth(5);
        this.faces = new ArrayList<>();
        int faceNum = jsonResult.getIntValue("faceNum");
        if(faceNum != 0) {
            JSONArray jsonFaces = jsonResult.getJSONArray("faces");
            for (Object obj : jsonFaces) {
                Face face = new Face((JSONObject)obj);
                paint.setColor(UIHelper.getColorByScore(face.score));
                Rect rect = new Rect(face.x1, face.y2, face.x2, face.y1);
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawRect(rect, paint);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawText(String.format(Locale.getDefault(), "%.2f", face.score), face.x1 + 5, face.y2 - 5, paint);
                this.faces.add(face);
            }
        }
        System.out.println(faces);
    }

    public Bitmap getResultImage() {
        return resultImage;
    }

    public void saveImage(){
        FileUtils.saveImage(resultImage);
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public double getHighestScore(){
        double highestScore = 0;
        for(Face face : faces){
            highestScore = Math.max(highestScore, face.score);
        }
        return highestScore;
    }

    public boolean isManual() {
        return manual;
    }

    public List<Face> getFaces() {
        return faces;
    }
}
