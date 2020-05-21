package com.example.notes.activities.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.notes.R;
import com.example.notes.model.NoteModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainAdapterViewHolder> {
    private Context context;
    private List<NoteModel> noteList;
    private ItemClickListener itemClickListener;

    MainAdapter(Context context, List<NoteModel> noteList, ItemClickListener itemClickListener) {
        this.context = context;
        this.noteList = noteList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MainAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new MainAdapterViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapterViewHolder holder, int position) {
        NoteModel notePosition = noteList.get(position);
        holder.mTvTitle.setText(notePosition.getmTitle());
        holder.mTvNote.setText(notePosition.getmNote());
        holder.mTvDate.setText(notePosition.getmDate());
        holder.mCardItem.setCardBackgroundColor(notePosition.getmColor());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public static class MainAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemClickListener itemClickListener;
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.tv_note)
        TextView mTvNote;
        @BindView(R.id.tv_date)
        TextView mTvDate;
        @BindView(R.id.card_item)
        CardView mCardItem;

        MainAdapterViewHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            ButterKnife.bind(R.layout.item_note, itemView);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            mTvNote = itemView.findViewById(R.id.tv_note);
            mTvDate = itemView.findViewById(R.id.tv_date);
            mCardItem = itemView.findViewById(R.id.card_item);

            this.itemClickListener = itemClickListener;
            mCardItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClickListener(v, getAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onClickListener(View view, int position);
    }
}
