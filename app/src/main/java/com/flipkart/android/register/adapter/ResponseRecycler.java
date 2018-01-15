package com.flipkart.android.register.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flipkart.android.register.R;
import com.flipkart.android.register.RegistrationHelper;
import com.flipkart.android.register.model.ModelView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VectoR on 19-09-2017.
 */

public class ResponseRecycler extends RecyclerView.Adapter<ResponseRecycler.MyViewHolder> {

    private List<ModelView> list = new ArrayList<>();
    private Context context;


    public ResponseRecycler(List<ModelView> list, Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.response_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final ModelView modelView = list.get(position);
        holder.responseTV.setText(modelView.getResponse());
        holder.statusTV.setText(modelView.getStatus());
        if(modelView.getStatus().equalsIgnoreCase("Failure")||modelView.getStatus().equalsIgnoreCase("Stopped")){
            holder.statusTV.setTextColor(context.getResources().getColor(R.color.failure));
            holder.progressBar.setVisibility(View.GONE);
            holder.icon_profile.setImageResource(R.drawable.ic_loading);
            holder.icon_profile.clearColorFilter();
            holder.icon_text.setText("");
            holder.icon_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(modelView.getStatus().equalsIgnoreCase("Failure")||modelView.getStatus().equalsIgnoreCase("Stopped") || modelView.getStatus().contains("NullPointer")){
                        Animation rotation = AnimationUtils.loadAnimation(context, R.anim.refresh);
                        rotation.setRepeatCount(Animation.INFINITE);
                        holder.icon_profile.startAnimation(rotation);

                        ((RegistrationHelper)context).hideFab(true);

                        modelView.setStatus("Wait");
                        modelView.getLogic().sendAgain();
                    }
                    }
            });
        }
        else{
            if(modelView.getStatus().equalsIgnoreCase("Success")) {
                holder.statusTV.setTextColor(context.getResources().getColor(R.color.success));
                holder.progressBar.setVisibility(View.GONE);
            }
            else{
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.statusTV.setTextColor(context.getResources().getColor(R.color.wait));
            }

            String[] sub = modelView.getThread_name().split(" ");
            holder.icon_text.setText(sub[0].substring(0,1)+sub[1].substring(0,1));
            holder.icon_profile.setImageResource(R.drawable.bg_circle);
            holder.icon_profile.setColorFilter(modelView.getColor());
        }
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                    builder.setTitle("All Randomly Generated Values");
                    builder.setMessage(modelView.getLogic().allGenValues());
                    builder.setPositiveButton("OK", null);
                    builder.show();

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        holder.threadName.setText(modelView.getThread_name());
        holder.timestamp.setText(modelView.getTimeStamp());
        holder.cv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!modelView.getStatus().equalsIgnoreCase("Success")){
                    try{
                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                        builder.setTitle("Stop the Request");
                        builder.setMessage("Do you Really want to stop, " + modelView.getThread_name());
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                modelView.getLogic().cancelRequest();
                            }
                        });
                        builder.setNegativeButton("Cancel", null);
                        builder.show();

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView responseTV, statusTV, threadName, timestamp, icon_text;
        private ImageView icon_profile;
        private ProgressBar progressBar;
        private CardView cv;
        public MyViewHolder(View itemView) {
            super(itemView);
            responseTV = (TextView) itemView.findViewById(R.id.card_textView);
            statusTV = (TextView) itemView.findViewById(R.id.card_status);
            cv = (CardView) itemView.findViewById(R.id.card_response);
            icon_profile = (ImageView) itemView.findViewById(R.id.icon_profile);
            threadName = (TextView)itemView.findViewById(R.id.thread_name);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp_value);
            icon_text = (TextView)itemView.findViewById(R.id.icon_text);
            progressBar = (ProgressBar)itemView.findViewById(R.id.progressBar);

        }
    }


}
