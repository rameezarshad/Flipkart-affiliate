package com.flipkart.android.register;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.flipkart.android.register.adapter.ResponseRecycler;
import com.flipkart.android.register.model.ModelView;
import com.flipkart.android.register.network.APIInterface;
import com.flipkart.android.register.network.ApiClient;
import com.flipkart.android.register.pojo.appInstall.InstallResponse;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RegistrationHelper extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static native String getMessage(String str, String str2);
    static {
        System.loadLibrary("myjni");
    }

    private int lastFirstVisiblePosition = 0;
    private String mat_url;

    public static List<String[]> devices, versions,network_codes, screen;
    private SharedPreferences preferences, con_pref, install_pref;
    private SharedPreferences.Editor editor;

    public TextView tt, count;
    public FloatingActionButton fab, stop, moveDown;
    Boolean setRequest;
    RecyclerView recyclerView;
    ResponseRecycler adapter;
    List<ModelView> list = new ArrayList<>();
    Logic logic;
    ModelView modelView;
    int threadCounter = 0;
    private Worker worker;
    Context context;
    View rootView;

    private String directInstall = "direct_install";
    private String fallback = "interstitial_fallback";
    private String installType = fallback;
    private static APIInterface apiInterface, httpInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_helper);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        fab = (FloatingActionButton) findViewById(R.id.fab);
        stop = (FloatingActionButton) findViewById(R.id.stop);
        moveDown = (FloatingActionButton)findViewById(R.id.moveDown);
        recyclerView = (RecyclerView) findViewById(R.id.response_RV);
        rootView = findViewById(R.id.rootView);
        InputStream deviceStream = getResources().openRawResource(R.raw.complete_devices);
        InputStream versionStream = getResources().openRawResource(R.raw.version);
        InputStream networkStream = getResources().openRawResource(R.raw.network);
        InputStream screenStream = getResources().openRawResource(R.raw.screen);

        devices = new DeviceModels(deviceStream).read();
        versions = new DeviceModels(versionStream).read();
        network_codes = new DeviceModels(networkStream).read();
        screen = new DeviceModels(screenStream).read();

        worker = new Worker();

        httpInterface = ApiClient.getBuilder().baseUrl("http://mobileapi.flipkart.net/").client(ApiClient.addClient().build()).build().create(APIInterface.class);

        apiInterface = ApiClient.getBuilder().baseUrl("https://mobileapi.flipkart.net/").client(ApiClient.addClient().build()).build().create(APIInterface.class);

        preferences =  getSharedPreferences("Referrer", MODE_PRIVATE);
        con_pref =  getSharedPreferences("Concurrency", MODE_PRIVATE);
        install_pref =  getSharedPreferences("InstallType", MODE_PRIVATE);

        if(con_pref.getString("concurrency",null) == null || install_pref.getString("install_type",null)== null){
            setConcurrentPref("25","100");
            setInstallPref(fallback);
        }

        if (preferences.getString("referrer", null) == null) {
           changeEditor("",false);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                worker.setConcurrent(Integer.parseInt(con_pref.getString("concurrency","25")));
                worker.setLimit(Integer.parseInt(con_pref.getString("limit","100")));
                worker.setRepeat(worker.getLimit()/worker.getConcurrent());
                worker.setRem(worker.getLimit() % worker.getConcurrent());


                hideFab(true);

                initializeRequest();

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopRequests();

            }
        });

        moveDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            moveDown.hide();
                        }
                    }, 2000);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && moveDown.getVisibility() != View.VISIBLE) {
                    moveDown.show();
                } else if (dy < 0 && moveDown.getVisibility() == View.VISIBLE) {
                    moveDown.hide();
                }
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void stopRequests(){

        for(int i = 0 ; i < list.size(); i++){
            if(!list.get(i).getStatus().equalsIgnoreCase("Success") && !list.get(i).getStatus().equalsIgnoreCase("Stopped")){
                list.get(i).getLogic().cancelRequest();
            }
        }

        hideFab(false);

    }


    public void changeEditor(String msg, Boolean cancel) {
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

    public void concurrentDialog(String msg){
        if(msg.isEmpty()){
            msg = "Change the Concurrency and Request Limit values";
        }
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.concurrent_dialog, null);
        dialogBuilder.setView(dialogView);
        final EditText con = (EditText) dialogView.findViewById(R.id.concurrent);
        final EditText f_limit = (EditText) dialogView.findViewById(R.id.fallback_limit);


        con.setText(con_pref.getString("concurrency","25"));
        f_limit.setText(con_pref.getString("limit","100"));


        dialogBuilder.setTitle("Change Concurrency");
        dialogBuilder.setMessage("\n"+msg);
        dialogBuilder.setPositiveButton("Save", null);
        AlertDialog b = dialogBuilder.create();

        b.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button buttonPositive = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                buttonPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(con.getText().toString().trim().length() != 0 && f_limit.getText().toString().trim().length() != 0){

                            if(Integer.parseInt(f_limit.getText().toString().trim())<Integer.parseInt(con.getText().toString().trim())){
                                f_limit.setError("Request Limit cannot be less than Concurrent Value");
                                f_limit.requestFocus();
                            }
                            else{
                                setConcurrentPref(con.getText().toString().trim(), f_limit.getText().toString().trim());
                                dialog.dismiss();
                            }
                        }
                        else if(con.getText().toString().trim().length() == 0 && f_limit.getText().toString().trim().length() != 0){
                            con.setError("Concurrency cannot be blank");
                            con.requestFocus();
                        }
                        else if(f_limit.getText().toString().trim().length() == 0 && con.getText().toString().trim().length() != 0 ) {
                            f_limit.setError("Request Limit cannot be blank");
                            f_limit.requestFocus();
                        }
                        else {
                            con.setError("Concurrency cannot be blank");
                            f_limit.setError("Request Limit cannot be blank");
                        }

                    }
                });
                Button buttonNegative = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);

                buttonNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
        b.show();

    }

    AlertDialog b;
    int select;
    public void installTypeDialog(){
        if (install_pref.getString("install_type",fallback).equalsIgnoreCase(fallback))
            select = 1;
        else
            select =0;
        final CharSequence[] items = {" Direct Install "," Fallback Install "};
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Select Install Type");
        dialogBuilder.setSingleChoiceItems(items, select, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                select = which;
            }

        });

        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(select)
                {
                    case 0:
                        setInstallPref(directInstall);
                        setConcurrentPref("120","120");
                        snackBar("Direct Install is selected. And Default Values set to: 120 Concurrent and 120 Limit");
                        break;
                    case 1:
                        setInstallPref(fallback);
                        setConcurrentPref("200","1000");
                        snackBar("Fallback Install is selected. And Default Values set to: 200 Concurrent and 1000 Limit");
                        break;
                }
            }
        });

        b = dialogBuilder.create();
        b.show();

    }

    private void snackBar(String text){
        Snackbar.make(rootView, text.trim(), Snackbar.LENGTH_LONG).show();
    }

    private String status="";
    public void initializeRequest(){
        int count = worker.getConcurrent() - worker.getCurrentThreads();
        if (install_pref.getString("install_type", fallback).equalsIgnoreCase(fallback))
                status = "Fallback";
            else
                status = "Direct";

        if (preferences.getString("referrer", null) != null) {
                if (worker.getCurrentThreads() < worker.getConcurrent()) {
                    worker.setRepeat(worker.getRepeat()-1);
                    for (int i = 0; i < count; i++) {
                        //mat_url = "https://173204.measurementapi.com/serve?action=click&publisher_id=173204&site_id=106226&sub_ad=&sub_publisher=" + preferences.getString("referrer", null) + "&sub_placement=" + install_pref.getString("install_type", fallback) + "&sub3=&site_id_android=106226&site_id_ios=107540&site_id_windows=108110&site_id_web=108040&response_format=json";
                        threadCounter++;
                        modelView = new ModelView();


                        modelView.setThread_name(status + " " + String.valueOf(threadCounter));
                        logic = new Logic("", getApplicationContext(), adapter, modelView, worker, mat_url, install_pref.getString("install_type", fallback), preferences.getString("referrer", null) , RegistrationHelper.this);
                        list.add(modelView);
                        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                        //logic.test();
                        logic.startInit();

                    }

                } else
                    Snackbar.make(rootView, "Woah! Wait for some requests to finish. Can send only " + worker.getConcurrent() + " requests at a time", Snackbar.LENGTH_LONG).show();
                setRequest = true;

            } else
                concurrentDialog("");
    }
    @Override
    protected void onResume() {
        super.onResume();
        adapter =new ResponseRecycler(list, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPosition(lastFirstVisiblePosition);

    }

    @Override
    protected void onPause() {
        super.onPause();
        lastFirstVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.registration_helper, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.resend_failed) {
            int j=0;
            /*for(int i=0; i < list.size();i++){
                if(list.get(i).getStatus().equalsIgnoreCase("Failure")){
                    list.get(i).getLogic().sendBackFailed();
                    j++;
                }

            }*/

            for(ModelView mv : list){

                if(mv.getStatus().equalsIgnoreCase("Failure")){
                    mv.getLogic().sendRequest();
                    j++;
                }
            }
            if(j>0)
                hideFab(true);

            snackBar(j + " : Failed Requests Resend");

            //adapter.notifyDataSetChanged();
        }

        else if (id == R.id.refresh) {
            refreshAll();
        }

        else if(id == R.id.resend_stopped){

            int j=0;
            for(int i=0; i < list.size();i++){
                if(list.get(i).getStatus().equalsIgnoreCase("Stopped")){
                    list.get(i).getLogic().sendRequest();
                    j++;
                }

            }

            if(j>0)
                hideFab(true);
            snackBar(j + " : Stopped Requests Resend");
        }

        else if(id == R.id.remove_success){

            int j=0;
            for(int i=0; i < list.size();i++){
                if(list.get(i).getStatus().equalsIgnoreCase("Success")){
                    list.remove(i);
                    j++;
                }

            }
            threadCounter -= j;
            adapter.notifyDataSetChanged();
            snackBar(j + " : Success Requests Removed");

        }
        else if(id == R.id.remove_all){
            stopRequests();
            list.clear();
            threadCounter = 0;
            adapter.notifyDataSetChanged();
        }

        else if(id == R.id.continue_again){
            if(worker.getCurrentThreads()==0){
                if(worker.getRepeat()>0){
                    hideFab(true);
                    initializeRequest();
                }
                else {
                    if(worker.getRem() > 0){
                        hideFab(true);
                        worker.setConcurrent(worker.getRem());
                        worker.setRem(0);
                        initializeRequest();
                    }
                    else {
                        hideFab(false);
                    }
                }
            }

            else{
                int j = 0;
                for(int i = list.size()-1; i >(list.size() - worker.getConcurrent()); i-- ){
                    if(!list.get(i).getStatus().equalsIgnoreCase("Success")){
                        j++;
                    }
                }
                snackBar(j + " Requests got Stuck");
                if(j > 0){
                    snackBar("Trying to Refresh all");
                    refreshAll();
                }
            }

        }

        else if(id == R.id.status){
            try{
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                builder.setTitle("Current Status");
                builder.setMessage(statusList());
                builder.setPositiveButton("OK", null);
                builder.show();

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_tracking) {
            changeEditor("",true);
        }

        else if (id == R.id.nav_concurrency) {

            concurrentDialog("");
        }
        else if (id == R.id.nav_install_type) {

            installTypeDialog();
        }
        else if (id == R.id.nav_install_report) {

            Intent intent= new Intent(this, InstallReport.class);
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void refreshAll(){

        for(ModelView mv : list){
            if(!mv.getStatus().equalsIgnoreCase("Success")){
                hideFab(true);
                if(mv.getStatus().equalsIgnoreCase("Installing") || mv.getStatus().equalsIgnoreCase("Retrying GettingMat")){
                    mv.getLogic().gettingMat();
                }
                else if(mv.getStatus().equalsIgnoreCase("Wait")){
                    mv.getLogic().initRequest();
                }
                else if(mv.getStatus().equalsIgnoreCase("Sending")){
                    mv.getLogic().sendWithDelay();
                }
                else if(mv.getStatus().equalsIgnoreCase("Register") ){
                    mv.getLogic().fetchRequest();
                }
                else if(mv.getStatus().equalsIgnoreCase("Hack 1") ){
                    mv.getLogic().registerAppRequest();
                }
                else if(mv.getStatus().equalsIgnoreCase("Hack 2")){
                    mv.getLogic().firstTuneRequest();
                }
                else if(mv.getStatus().equalsIgnoreCase("Registering Referrer")){
                    mv.getLogic().secondTuneRequest();
                }
                else if(mv.getStatus().equalsIgnoreCase("Stopped") || mv.getStatus().equalsIgnoreCase("Failure")){
                    mv.getLogic().sendRequest();
                }
            }
        }


    }
    private void setSharedPref(String referrer, String token){
        editor = getSharedPreferences("Referrer", MODE_PRIVATE).edit();
        editor.putString("referrer", referrer.trim());
        editor.putString("token", token.trim());
        editor.apply();
    }

    private void setConcurrentPref(String concurrent_value, String request_limit){
        editor = getSharedPreferences("Concurrency", MODE_PRIVATE).edit();
        editor.putString("concurrency", concurrent_value.trim());
        editor.putString("limit", request_limit.trim());
        editor.apply();

    }

    private void setInstallPref(String installType){

        editor = getSharedPreferences("InstallType", MODE_PRIVATE).edit();
        editor.putString("install_type", installType.trim());
        editor.apply();
    }

    public void hideFab(Boolean hide){

        if(hide){
            fab.setVisibility(View.GONE);
            stop.setVisibility(View.VISIBLE);
        }

        else{
            stop.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);

        }
    }

    public static APIInterface getApiInterface(){
        return apiInterface;
    }

    public static APIInterface getHttpInterface(){
        return httpInterface;
    }

    private String statusList(){
        StringBuilder values = new StringBuilder();
        List<String> statusList = new ArrayList<>();
        for (ModelView mv : list) {
            statusList.add(mv.getStatus());
        }
        Map<String, Integer> statusMap = new HashMap<>();
        for(String t: statusList) {
            Integer i = statusMap.get(t);
            if (i ==  null) {
                i = 0;
            }
            statusMap.put(t, i + 1);
        }

        for(Map.Entry<String, Integer> entry : statusMap.entrySet()){
            values.append(entry.getKey()+" = ");
            values.append(entry.getValue());
            values.append("\n\n");
        }

        return values.toString();
    }
}
