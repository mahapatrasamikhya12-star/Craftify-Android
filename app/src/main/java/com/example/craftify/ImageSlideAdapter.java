
package com.example.craftify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.SliderViewHolder> {

    private Context context;
    private List<Object> images; // Can hold int (resource) or String (URL)

    public ImageSliderAdapter(Context context, List<Object> images) {
        this.context = context;
        this.images  = images;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image_slider, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        Object image = images.get(position);

        if (image instanceof Integer) {
            // Local drawable resource
            int resId = (Integer) image;
            if (resId != 0) {
                holder.ivSlider.setImageResource(resId);
            } else {
                holder.ivSlider.setBackgroundColor(context.getResources().getColor(R.color.cream));
            }
        } else if (image instanceof String) {
            // URL from server
            Glide.with(context)
                    .load((String) image)
                    .placeholder(R.color.cream)
                    .centerCrop()
                    .into(holder.ivSlider);
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    static class SliderViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSlider;

        SliderViewHolder(View itemView) {
            super(itemView);
            ivSlider = itemView.findViewById(R.id.ivSlider);
        }
    }
}