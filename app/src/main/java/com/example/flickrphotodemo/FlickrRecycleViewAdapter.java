package com.example.flickrphotodemo;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

class FlickrRecycleViewAdapter extends RecyclerView.Adapter<FlickrRecycleViewAdapter.FlickrImageViewHolder>{
    private static final String TAG = "FlickrRecycleViewAdapt";
    private ArrayList<PhotoData> photoDataArrayList;
    private Context context;


    @NonNull
    @Override
    public FlickrImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse,parent,false);
        return new FlickrImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlickrImageViewHolder holder, int position) {
        if(photoDataArrayList==null||photoDataArrayList.size()==0){
            holder.thumbnail.setImageResource(R.drawable.placeholder);
            holder.title.setText(R.string.error404);
        }else {
            PhotoData photoItem = photoDataArrayList.get(position);
            Log.d(TAG, "onBindViewHolder:" + photoItem.getTitle() + "--->" + position);
            Glide.with(context)
                    .load(photoItem.getImage())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.placeholder)
                            .optionalCenterCrop()
                    )
                    .into(holder.thumbnail);
            holder.title.setText(photoItem.getTitle());
        }
    }

    @Override
    public int getItemCount() {

        return (((photoDataArrayList!=null)&&(photoDataArrayList.size()!=0)) ? photoDataArrayList.size() : 1);
    }

    void loadNewData(ArrayList<PhotoData> newphotoData){
        photoDataArrayList =newphotoData;
        notifyDataSetChanged();
     }

    public PhotoData getPhotoData(int position){
        return ( (photoDataArrayList!=null) && (photoDataArrayList.size()>0) ? photoDataArrayList.get(position) : null) ;
    }

    public FlickrRecycleViewAdapter(ArrayList<PhotoData> PhotoDataArrayList, Context Context) {
        photoDataArrayList = PhotoDataArrayList;
        context = Context;
    }

    static class  FlickrImageViewHolder extends RecyclerView.ViewHolder{
        ImageView thumbnail = null;
        TextView title =null;

        public FlickrImageViewHolder(View itemView) {
            super(itemView);
            this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            this.title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
