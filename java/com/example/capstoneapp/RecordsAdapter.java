package com.example.capstoneapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstoneapp.Model.Matches;

import java.util.ArrayList;

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.RecordViewHolder> {
//    List<Records> records;
    ArrayList<Matches> matchesList;
//    Matches matches;
//    Context context;
    boolean showAdditionalData;
    MatchesActivity activity;
    public RecordsAdapter(ArrayList<Matches> matchesList, MatchesActivity activity){
        this.matchesList = matchesList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false);
        RecordViewHolder viewHolder = new RecordViewHolder(itemView);
        return viewHolder;
    }

@Override
public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        holder.match_id.setText(matchesList.get(position).getMatchId());
    holder.date.setText(matchesList.get(position).getDate());
    holder.teamName.setText(matchesList.get(position).getTeamName());
    holder.opponent.setText(matchesList.get(position).getOpponent());
}


    @Override
    public int getItemCount() {
        if (matchesList == null) {
            return 0;
        }
        return matchesList.size();
    }

    public class RecordViewHolder extends RecyclerView.ViewHolder {
        TextView teamName, opponent, date, match_id;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            teamName = itemView.findViewById(R.id.team_name_text_view);
            opponent = itemView.findViewById(R.id.opponent_text_view);
            date = itemView.findViewById(R.id.date_text_view);
            match_id = itemView.findViewById(R.id.match_id_view);
        }
    }
}
