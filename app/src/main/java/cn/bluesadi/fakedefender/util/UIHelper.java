package cn.bluesadi.fakedefender.util;

import cn.bluesadi.fakedefender.R;
import cn.bluesadi.fakedefender.ui.base.BaseActivity;

public class UIHelper {

    public static String getTagByScore(double score){
        if(score >= Settings.threshold){
            return BaseActivity.getTopActivity().getString(R.string.tx_high_risk);
        }else if(score >= Settings.secondaryThreshold){
            return BaseActivity.getTopActivity().getString(R.string.tx_middle_risk);
        }else{
            return BaseActivity.getTopActivity().getString(R.string.tx_low_risk);
        }
    }

    public static int getColorByScore(double score){
        if(score >= Settings.threshold){
            return BaseActivity.getTopActivity().getColor(R.color.red);
        }else if(score >= Settings.secondaryThreshold){
            return BaseActivity.getTopActivity().getColor(R.color.yellow);
        }else{
            return BaseActivity.getTopActivity().getColor(R.color.green);
        }
    }

    public static String getTagByManual(boolean manual){
        if(manual){
            return BaseActivity.getTopActivity().getString(R.string.tx_manual);
        }else{
            return BaseActivity.getTopActivity().getString(R.string.tx_auto);
        }
    }

}
