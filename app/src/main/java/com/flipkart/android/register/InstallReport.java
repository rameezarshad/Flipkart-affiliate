package com.flipkart.android.register;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.flipkart.android.register.adapter.AppReportRecycler;
import com.flipkart.android.register.adapter.ResponseRecycler;
import com.flipkart.android.register.network.APIInterface;
import com.flipkart.android.register.network.ApiClient;
import com.flipkart.android.register.network.CallbackWithRetry;
import com.flipkart.android.register.pojo.appInstall.AppInstall;
import com.flipkart.android.register.pojo.appInstall.InstallResponse;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class InstallReport extends AppCompatActivity {
    private int lastFirstVisiblePosition = 0;
    private View rootView;
    private SharedPreferences.Editor editor;
    private APIInterface apiInterface;
    private Map<String, String> header;
    private LinkedHashMap<String, String> query;
    private SharedPreferences preferences;
    private String date;
    private Button startDate, endDate, lastMonth, last30Days, thisMonth, hideBtn, today;
    private View query_layout;
    private Spinner clickSpinner, statusSpinner;
    private Calendar calendar;
    private Boolean valid;
    private RecyclerView recyclerView;
    private AppReportRecycler adapter;
    private List<AppInstall> list = new ArrayList<>();
    private TextView total_amt, install_count, total_list_count;
    private String startDateValue, endDateValue;
    private ProgressBar progressBar;
    private LinearLayout status_layout;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install_report);
        rootView = findViewById(R.id.installView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        startDate = (Button) findViewById(R.id.startDate);
        endDate = (Button) findViewById(R.id.endDate);
        lastMonth = (Button) findViewById(R.id.lastMonth);
        last30Days = (Button) findViewById(R.id.last30Days);
        thisMonth = (Button) findViewById(R.id.thisMonth);
        hideBtn = (Button) findViewById(R.id.hide_btn);
        query_layout = findViewById(R.id.query_layout);
        clickSpinner = (Spinner) findViewById(R.id.click_spinner);
        statusSpinner = (Spinner) findViewById(R.id.status_spinner);
        recyclerView = (RecyclerView) findViewById(R.id.install_response_RV);
        total_amt = (TextView) findViewById(R.id.total_amt);
        install_count = (TextView) findViewById(R.id.install_count_report);
        today = (Button) findViewById(R.id.today);
        total_list_count = (TextView) findViewById(R.id.total_list_count);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        status_layout = (LinearLayout) findViewById(R.id.status_layout);
        setSupportActionBar(toolbar);

        query = new LinkedHashMap<>();

        valid = validateToken();
        setCalendar();

        clickSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               filterResult();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterResult();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(valid){
                   list.clear();
                   count = 0;
                   startDateValue = startDate.getText().toString();
                   endDateValue = endDate.getText().toString();
                   showProgress(true);
                   getInstallReport(startDateValue,endDateValue,"tentative",false);
                   getInstallReport(startDateValue,endDateValue,"approved",false);
                   getInstallReport(startDateValue,endDateValue,"disapproved",true);



                   hideLayout(false);
               }
               else
                   Snackbar.make(view, "Add the Referrer and Api Token Value", Snackbar.LENGTH_LONG)
                           .setAction("Add", new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   changeEditor("", false);
                               }
                           }).show();

            }
        });
    }

    private void filterResult(){
        if(list.size()>0){
            adapter.getFilter().filter(clickSpinner.getSelectedItem().toString() +"/" + statusSpinner.getSelectedItem().toString());
        }
    }


    private void setCalendar(){

        calendar = Calendar.getInstance();

        final SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");

        endDate.setText(s.format(new Date(calendar.getTimeInMillis())));
        calendar.set(Calendar.DATE,calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        startDate.setText(s.format(new Date(calendar.getTimeInMillis())));

        hideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               hideLayout(true);
            }
        });

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String array1[]= startDate.getText().toString().split("-");
                getDate(Integer.parseInt(array1[0]),Integer.parseInt(array1[1]),Integer.parseInt(array1[2]), startDate);
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String array1[]= endDate.getText().toString().split("-");
                getDate(Integer.parseInt(array1[0]),Integer.parseInt(array1[1]),Integer.parseInt(array1[2]), endDate);
            }
        });

        lastMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();

                calendar.add(Calendar.MONTH, -1);
                calendar.set(Calendar.DATE,calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                Date firstDateOfPreviousMonth = calendar.getTime();
                calendar.set(Calendar.DATE,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date lastDateOfPreviousMonth = calendar.getTime();

                startDate.setText(s.format(firstDateOfPreviousMonth));
                endDate.setText(s.format(lastDateOfPreviousMonth));

            }
        });

        last30Days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                endDate.setText(s.format(new Date(calendar.getTimeInMillis())));
                calendar.add(Calendar.DATE, -30);
                startDate.setText(s.format(new Date(calendar.getTimeInMillis())));

            }
        });

        thisMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
                Date firstDayofMonth = calendar.getTime();
                calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
                Date lastDayOfMonth = calendar.getTime();
                startDate.setText(s.format(firstDayofMonth));
                endDate.setText(s.format(lastDayOfMonth));

            }
        });

        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                endDate.setText(s.format(new Date(calendar.getTimeInMillis())));
                startDate.setText(s.format(new Date(calendar.getTimeInMillis())));
            }
        });
    }


    private void showProgress(boolean show){

        if(show && progressBar.getVisibility() == View.GONE){
            progressBar.setVisibility(View.VISIBLE);

        }


        else{
            progressBar.setVisibility(View.GONE);

        }


    }
    private void hideLayout(Boolean visibility){

        if(query_layout.getVisibility() == View.VISIBLE){
            query_layout.animate()
                    .translationY(0)
                    .alpha(0.0f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            query_layout.setVisibility(View.GONE);
                            hideBtn.setText("Show");
                        }
                    });

        }

        else if(visibility){

            query_layout.setVisibility(View.VISIBLE);
            query_layout.setAlpha(0.0f);

            query_layout.animate()
                    .translationY(0)
                    .alpha(1.0f)
                    .setListener(null);
            hideBtn.setText("Hide");
        }

    }

    private Boolean validateToken(){
        preferences =  getSharedPreferences("Referrer", MODE_PRIVATE);
        if(preferences.getString("referrer", null) == null || preferences.getString("token", null) == null){
                changeEditor("", false);
                return false;
        }
        else{
            header = new HashMap<>();
            header.put("Fk-Affiliate-Id",preferences.getString("referrer", null));
            header.put("Fk-Affiliate-Token",preferences.getString("token", null));
            apiInterface = ApiClient.getBuilder().baseUrl("https://affiliate-api.flipkart.net/").client(ApiClient.addClient().addInterceptor(ApiClient.addInterceptor(header)).build()).build().create(APIInterface.class);
            return true;
        }
    }

    private void changeEditor(String msg, Boolean cancel) {
        if(msg.isEmpty()){
            msg = "You must input correct details otherwise it will not work..";
        }
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_edit_dailog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        final EditText ref_edit = (EditText) dialogView.findViewById(R.id.affiliate_edit);
        final EditText token_edit = (EditText) dialogView.findViewById(R.id.token_edit);
        ref_edit.setText(preferences.getString("referrer", ""));
        token_edit.setText(preferences.getString("token", ""));
        dialogBuilder.setTitle("Change Tracking Id and Token");
        dialogBuilder.setMessage("\n"+msg);
        dialogBuilder.setPositiveButton("Save", null);

        if(cancel)
            dialogBuilder.setNegativeButton("Cancel", null);

        AlertDialog b = dialogBuilder.create();
        b.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button buttonPositive = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                buttonPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ref_edit.getText().toString().trim().length() != 0 && token_edit.getText().toString().trim().length() != 0){
                            setSharedPref(ref_edit.getText().toString(), token_edit.getText().toString());
                            dialog.dismiss();
                            final Snackbar snackbar = Snackbar.make(rootView, "Successfully Added tracking id : "+ref_edit.getText().toString().trim(), Snackbar.LENGTH_LONG);
                            snackbar.setAction("Change", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    changeEditor("", true);
                                }
                            });
                            snackbar.show();
                        }

                        else if(ref_edit.getText().toString().trim().length() == 0 && token_edit.getText().toString().trim().length() != 0){
                            ref_edit.setError("Affiliate Tracking id cannot be blank");
                            ref_edit.requestFocus();
                        }
                        else if(token_edit.getText().toString().trim().length() == 0 && ref_edit.getText().toString().trim().length() != 0 ) {
                            token_edit.setError("API Token Value cannot be blank");
                            token_edit.requestFocus();
                        }
                        else {
                            ref_edit.setError("Affiliate Tracking id cannot be blank");
                            token_edit.setError("API Token Value cannot be blank");
                        }
                    }
                });
                Button buttonNegative = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                buttonNegative.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        b.show();
    }


    private void setSharedPref(String referrer, String token){
        editor = getSharedPreferences("Referrer", MODE_PRIVATE).edit();
        editor.putString("referrer", referrer.trim());
        editor.putString("token", token.trim());
        editor.apply();
    }

    private void getInstallReport(String startDate, String endDate, final String status, final Boolean clear) {
        Call<InstallResponse> call = apiInterface.getInstallReport(getQueryMap(startDate,endDate,status));

        call.enqueue(new CallbackWithRetry<InstallResponse>(call) {
            @Override
            public void onResponse(Call<InstallResponse> call, Response<InstallResponse> response) {
                /*if(clear)
                    list.clear();*/

                InstallResponse installResponse = response.body();

                try{
                    for(AppInstall a : installResponse.getAppInstalls()){
                        a.setStatus(status);
                        list.add(a);
                    }

                    count++;

                    if(count == 3){
                        showProgress(false);
                        adapter.notifyDataSetChanged();
                        changeAmt(list);
                        filterResult();
                    }

                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<InstallResponse> call, Throwable t) {
                super.onFailure(call, t);

            }
        });


    }

    public void changeAmt(List<AppInstall> installList){
        int commission = 0, count = 0;

        for(AppInstall a : installList){
            count += a.getInstallCount();
            commission += a.getTotalCommission();
        }

        total_list_count.setText("List count : " + String.valueOf(installList.size()));
        total_amt.setText(statusSpinner.getSelectedItem().toString() +" Amount : Rs " + String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(commission)));
        install_count.setText("Install Count : " + String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(count)));

    }
    private LinkedHashMap getQueryMap(String startDate, String endDate, String status){
        LinkedHashMap<String, String> queryMap = new LinkedHashMap<>();

        queryMap.put("startDate",startDate);
        queryMap.put("endDate",endDate);
        queryMap.put("status",status.toLowerCase());

        return queryMap;
    }

    private void getDate(int mYear, int mMonth, int mDay, final Button btn){


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date = String.valueOf(year) + "-" + String.valueOf(month+1) + "-" + String.valueOf(dayOfMonth);
                btn.setText(date);
            }
        },mYear, mMonth-1, mDay);

        datePickerDialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter =new AppReportRecycler(list, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPosition(lastFirstVisiblePosition);
        recyclerView.setNestedScrollingEnabled(false);

    }

    @Override
    protected void onPause() {
        super.onPause();
        lastFirstVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

    }
}
