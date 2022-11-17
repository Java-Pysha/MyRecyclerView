package com.javapysh.myrecyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ImageViewHolder> {

    Context context;
    private List<String> images;
    private final OnStateClickListener onClickListener;

    interface OnStateClickListener {
        void onStateClick(int position);
    }

    public MyAdapter(Context context, List<String> images, OnStateClickListener onClickListener) {
        this.images = images;
        this.onClickListener = onClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public MyAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ImageViewHolder holder, int position) {
        Picasso.get().load(images.get(position)).placeholder(R.color.neutral)
                .error(R.drawable.error2).into(holder.iv);

        holder.itemView.setOnClickListener(view -> {
            onClickListener.onStateClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;

        @SuppressLint("WrongViewCast")
        public ImageViewHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.imageView);
        }
    }
}
