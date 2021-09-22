package cn.bluesadi.fakedefender.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import cn.bluesadi.fakedefender.R;
import cn.bluesadi.fakedefender.defender.CheckRecord;
import cn.bluesadi.fakedefender.defender.FakeChecker;
import cn.bluesadi.fakedefender.ui.activity.ResultActivity;
import cn.bluesadi.fakedefender.ui.base.BaseActivity;
import cn.bluesadi.fakedefender.util.UIHelper;

public class RecordListAdapter extends RecyclerView.Adapter<RecordListAdapter.RecordHolder> {

    private static final RecordListAdapter instance = new RecordListAdapter();

    public static RecordListAdapter getInstance() {
        return instance;
    }

    @NonNull
    @Override
    public RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item,parent,false);
        return new RecordHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordHolder holder, int position) {
        CheckRecord record = FakeChecker.getInstance().getRecordList().get(position);
        Date dateObj = record.getCheckTime();
        String date = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault()).format(dateObj);
        String time = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault()).format(dateObj);
        double highestScore = record.getHighestScore();
        holder.txRecord.setTextColor(UIHelper.getColorByScore(highestScore));
        holder.txRecord.setText(String.format("%s%s %s %s",
                UIHelper.getTagByScore(highestScore),
                UIHelper.getTagByManual(record.isManual()),
                date,
                time)
        );
        holder.txRecord.setOnClickListener(view->{
            System.out.println(position);
            Bundle bundle = new Bundle();
            bundle.putInt("index", position);
            BaseActivity.getTopActivity().startActivity(ResultActivity.class, bundle);
        });
    }

    @Override
    public int getItemCount() {
        int size = FakeChecker.getInstance().getRecordList().size();
        return Math.min(size, 100);
    }

    public static class RecordHolder extends RecyclerView.ViewHolder{

        private final TextView txRecord;

        public RecordHolder(@NonNull View itemView) {
            super(itemView);
            txRecord = itemView.findViewById(R.id.tx_record);
        }
    }

}
