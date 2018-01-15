package com.flipkart.android.register.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flipkart.android.register.InstallReport;
import com.flipkart.android.register.R;
import com.flipkart.android.register.pojo.appInstall.AppInstall;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by VectoR on 23-11-2017.
 */

public class AppReportRecycler extends RecyclerView.Adapter<AppReportRecycler.MyViewHolder> implements Filterable {

    private List<AppInstall> appInstallList;
    private List<AppInstall> appInstallListFiltered;
    private Context context;

    public AppReportRecycler(List<AppInstall> appInstallList, Context context){
        this.appInstallList = appInstallList;
        this.appInstallListFiltered = appInstallList;
        this.context = context;
    }

    @Override
    public AppReportRecycler.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.install_report_card, parent, false);

        return new AppReportRecycler.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AppReportRecycler.MyViewHolder holder, int position) {

        final AppInstall appInstall = appInstallList.get(position);
        holder.install_type.setText(appInstall.getClickType());
        holder.install_status.setText(appInstall.getStatus());
        holder.install_date.setText(appInstall.getDate());
        holder.install_count.setText("Install Count : " + String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(appInstall.getInstallCount())));
        holder.install_commission.setText("Total Commission : " + String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(appInstall.getTotalCommission())));

        if(appInstall.getClickType().equalsIgnoreCase("Fallback"))
            holder.install_report_ll.setBackgroundResource(R.drawable.rectangle_button);

        else
            holder.install_report_ll.setBackgroundResource(0);

    }

    @Override
    public int getItemCount() {
        return appInstallList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString[] = constraint.toString().split("/");
                String charString1 = charString[0];
                String charString2 = charString[1];
                if (constraint.toString().isEmpty()|| (charString1.equalsIgnoreCase("All") && charString2.equalsIgnoreCase("All"))) {
                    appInstallList = appInstallListFiltered;
                }

                else if(charString1.equalsIgnoreCase("All") && !charString2.equalsIgnoreCase("All")){

                    List<AppInstall> filteredList = new ArrayList<>();
                    for (AppInstall row : appInstallListFiltered) {

                        if (row.getStatus().equalsIgnoreCase(charString2)) {
                            filteredList.add(row);
                        }
                    }

                    appInstallList = filteredList;
                }
                else if(charString2.equalsIgnoreCase("All") && !charString1.equalsIgnoreCase("All")){

                    List<AppInstall> filteredList = new ArrayList<>();
                    for (AppInstall row : appInstallListFiltered) {

                        if (row.getClickType().equalsIgnoreCase(charString1)) {
                            filteredList.add(row);
                        }
                    }

                    appInstallList = filteredList;
                }
                else {
                    List<AppInstall> filteredList = new ArrayList<>();
                    for (AppInstall row : appInstallListFiltered) {

                        if (row.getClickType().equalsIgnoreCase(charString1) && row.getStatus().equalsIgnoreCase(charString2)) {
                            filteredList.add(row);
                        }
                    }

                    appInstallList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = appInstallList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                appInstallList = (ArrayList<AppInstall>) results.values;
                notifyDataSetChanged();
                ((InstallReport)context).changeAmt(appInstallList);
            }
        };
    }

    public interface AppInstallAdapterListener {
        void onAppInstallSelected(AppInstall install);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView install_type, install_date, install_status, install_count, install_commission;
        private LinearLayout install_report_ll;

        public MyViewHolder(View itemView) {
            super(itemView);
            install_type = (TextView) itemView.findViewById(R.id.install_type);
            install_status = (TextView) itemView.findViewById(R.id.install_status);
            install_date = (TextView) itemView.findViewById(R.id.installed_date);
            install_count = (TextView) itemView.findViewById(R.id.install_count);
            install_commission = (TextView) itemView.findViewById(R.id.total_commision);
            install_report_ll = (LinearLayout) itemView.findViewById(R.id.install_report_ll);
        }
    }


}
