package come.texi.driver;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.skyfishjy.library.RippleBackground;
import com.squareup.picasso.Picasso;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import come.texi.driver.Truck.SearchTruck;
import come.texi.driver.utils.BookingDetails;
import come.texi.driver.utils.Common;
import come.texi.driver.utils.Url;

public class TripDetailActivity extends AppCompatActivity {

    private ServerSocket sSocket;
    Handler updateConversationHandler;
    Thread serverThread = null;
    private static final int SERVERPORT = 8081;

    ArrayList<BookingDetails> bookingDetails;
    TextView txt_car_name;
    TextView txt_pickup_point;
    TextView txt_pickup_point_val;
    TextView txt_drop_point;
    TextView txt_drop_point_val;
    TextView txt_truct_type;
    TextView txt_truct_type_val;
    ImageView img_car_image;
    TextView txt_distance;
    TextView txt_distance_val;
    TextView txt_distance_km;
    TextView txt_ast_time;
    TextView txt_ast_time_val, search;
    TextView txt_booking_date;
    TextView txt_booking_date_val;
    RelativeLayout layout_back_arrow;
    TextView txt_total_price;
    TextView txt_total_price_dol;
    TextView txt_total_price_val, txt_to;
    RelativeLayout layout_confirm_request, layout2, layout3, layout4, layout5, layout6, layout7;
    TextView txt_payment_type_val;
    LinearLayout layout1;
    TextView txt_payment_type, txt_vehicle_detail, txt_payment_detail, txt_confirm_request;


    Typeface OpenSans_Regular, Roboto_Regular, Roboto_Medium, Roboto_Bold, OpenSans_Semibold;

    String pickup_point;
    String drop_point;
    String truckIcon;
    String truckType;
    String CabId;
    String AreaId;
    Float distance;
    Float totlePrice;
    String booking_date;
    double PickupLatitude;
    double PickupLongtude;
    double DropLatitude;
    double DropLongtude;
    String DayNight;
    String comment;
    String pickup_date_time;
    String transfertype;
    String PaymentType;
    String person;
    String transaction_id;
    String BookingType;
    String AstTime;


    SharedPreferences userPref;

    Dialog ProgressDialog;
    RotateLoading cusRotateLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);

        layout1 = findViewById(R.id.layout_liniar);
        final ImageView imageView = (ImageView) findViewById(R.id.centerImage);
        txt_car_name = (TextView) findViewById(R.id.txt_car_name);
        txt_pickup_point = (TextView) findViewById(R.id.txt_pickup_point);
        txt_pickup_point_val = (TextView) findViewById(R.id.txt_pickup_point_val);
        txt_drop_point = (TextView) findViewById(R.id.txt_drop_point);
        txt_drop_point_val = (TextView) findViewById(R.id.txt_drop_point_val);
        txt_truct_type = (TextView) findViewById(R.id.txt_truct_type);
        txt_truct_type_val = (TextView) findViewById(R.id.txt_truct_type_val);
        img_car_image = (ImageView) findViewById(R.id.img_car_image);
        search = findViewById(R.id.searchtext);
        txt_distance = (TextView) findViewById(R.id.txt_distance);
        txt_distance_val = (TextView) findViewById(R.id.txt_distance_val);
        txt_distance_km = (TextView) findViewById(R.id.txt_distance_km);
        txt_ast_time = (TextView) findViewById(R.id.txt_ast_time);
        txt_ast_time_val = (TextView) findViewById(R.id.txt_ast_time_val);
        txt_booking_date = (TextView) findViewById(R.id.txt_booking_date);
        txt_booking_date_val = (TextView) findViewById(R.id.txt_booking_date_val);
        layout_back_arrow = (RelativeLayout) findViewById(R.id.layout_back_arrow);
        txt_total_price = (TextView) findViewById(R.id.txt_total_price);
        txt_total_price_dol = (TextView) findViewById(R.id.txt_total_price_dol);
        txt_total_price_val = (TextView) findViewById(R.id.txt_total_price_val);
        layout_confirm_request = (RelativeLayout) findViewById(R.id.layout_confirm_request);
        txt_payment_type_val = (TextView) findViewById(R.id.txt_payment_type_val);
        txt_payment_type = (TextView) findViewById(R.id.txt_payment_type);
        txt_to = (TextView) findViewById(R.id.txt_to);
        txt_vehicle_detail = (TextView) findViewById(R.id.txt_vehicle_detail);
        txt_payment_detail = (TextView) findViewById(R.id.txt_payment_detail);
        txt_confirm_request = (TextView) findViewById(R.id.txt_confirm_request);
        final RippleBackground rippleBackground = (RippleBackground) findViewById(R.id.content);
        ProgressDialog = new Dialog(TripDetailActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        ProgressDialog.setContentView(R.layout.custom_progress_dialog);
        ProgressDialog.setCancelable(false);
        cusRotateLoading = (RotateLoading) ProgressDialog.findViewById(R.id.rotateloading_register);
        layout2 = findViewById(R.id.layout_payment_detail);
        layout3 = findViewById(R.id.layout_payment_type);
        layout4 = findViewById(R.id.layout_distance);
        layout5 = findViewById(R.id.layout_ast_time);
        layout6 = findViewById(R.id.layout_total_price);
        userPref = PreferenceManager.getDefaultSharedPreferences(TripDetailActivity.this);

        pickup_point = getIntent().getExtras().getString("pickup_point");
        System.out.println("Pick up point in trip details : " + pickup_point);
        drop_point = getIntent().getExtras().getString("drop_point");
        truckIcon = getIntent().getExtras().getString("truckIcon");
        truckType = getIntent().getExtras().getString("truckType");
        CabId = getIntent().getExtras().getString("CabId");
        AreaId = getIntent().getExtras().getString("AreaId");
        distance = getIntent().getExtras().getFloat("distance");
        totlePrice = getIntent().getExtras().getFloat("totlePrice");
        booking_date = getIntent().getExtras().getString("booking_date");
        PickupLatitude = getIntent().getExtras().getDouble("PickupLatitude");
        PickupLongtude = getIntent().getExtras().getDouble("PickupLongtude");
        DropLatitude = getIntent().getExtras().getDouble("DropLatitude");
        DropLongtude = getIntent().getExtras().getDouble("DropLongtude");
        comment = getIntent().getExtras().getString("comment");
        DayNight = getIntent().getExtras().getString("DayNight");
        transfertype = getIntent().getExtras().getString("transfertype");
        PaymentType = getIntent().getExtras().getString("PaymentType");
        person = getIntent().getExtras().getString("person");
        transaction_id = getIntent().getExtras().getString("transaction_id");
        BookingType = getIntent().getExtras().getString("BookingType");
        AstTime = getIntent().getExtras().getString("AstTime");

        OpenSans_Regular = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular_0.ttf");
        Roboto_Regular = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        Roboto_Medium = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
        Roboto_Bold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold_0.ttf");
        OpenSans_Semibold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Semibold_0.ttf");

        txt_car_name.setTypeface(OpenSans_Regular);

        txt_pickup_point.setTypeface(Roboto_Regular);
        txt_booking_date.setTypeface(Roboto_Regular);
        txt_drop_point.setTypeface(Roboto_Regular);
        txt_truct_type.setTypeface(Roboto_Regular);
        txt_distance_km.setTypeface(Roboto_Regular);
        txt_total_price_dol.setTypeface(Roboto_Regular);
        txt_total_price_dol.setText(Common.Currency);
        txt_payment_type.setTypeface(Roboto_Regular);
        txt_to.setTypeface(Roboto_Bold);
        txt_vehicle_detail.setTypeface(Roboto_Bold);
        txt_payment_detail.setTypeface(Roboto_Bold);
        txt_confirm_request.setTypeface(Roboto_Bold);

        txt_pickup_point_val.setTypeface(OpenSans_Regular);
        txt_pickup_point_val.setText(pickup_point);
        txt_booking_date_val.setTypeface(OpenSans_Regular);
        txt_booking_date_val.setText(booking_date);
        txt_drop_point_val.setTypeface(OpenSans_Regular);
        txt_drop_point_val.setText(drop_point);
        txt_truct_type_val.setTypeface(OpenSans_Regular);
        txt_truct_type_val.setText(truckType.toUpperCase());
        txt_distance.setTypeface(OpenSans_Regular);
        txt_distance_val.setTypeface(OpenSans_Regular);
        txt_distance_val.setText(distance + "");
        txt_ast_time.setTypeface(OpenSans_Regular);
        txt_ast_time_val.setTypeface(OpenSans_Regular);
        txt_ast_time_val.setText(AstTime);
        txt_total_price.setTypeface(OpenSans_Regular);
        txt_total_price_val.setTypeface(OpenSans_Regular);
        txt_total_price_val.setText(Math.round(totlePrice) + "");
        txt_payment_type_val.setTypeface(OpenSans_Regular);
        txt_payment_type_val.setText(PaymentType);

//        updateConversationHandler = new Handler();
//        this.serverThread = new Thread(new ServerThread());
//        this.serverThread.start();

        Log.d("truckIcon", "truckIcon = " + truckIcon);
        Picasso.with(TripDetailActivity.this)
                .load(Uri.parse(Url.carImageUrl + truckIcon))
                .placeholder(R.drawable.truck_icon)
                .into(img_car_image);

        layout_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a, d, MMM yyyy,EEE");
        try {
            Date parceDate = simpleDateFormat.parse(booking_date);
            SimpleDateFormat parceDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            pickup_date_time = parceDateFormat.format(parceDate.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }


        layout_confirm_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                imageView.setVisibility(View.VISIBLE);
//                search.setVisibility(View.VISIBLE);
//                layout1.setVisibility(View.GONE);
//                layout2.setVisibility(View.GONE);
//                layout3.setVisibility(View.GONE);
//                layout4.setVisibility(View.GONE);
//                layout5.setVisibility(View.GONE);
//                layout6.setVisibility(View.GONE);
//                layout_confirm_request.setVisibility(View.GONE);
//                rippleBackground.startRippleAnimation();

                System.out.println("Book cab URL : - "+Url.bookCabUrl);
                Ion.with(TripDetailActivity.this)
                        .load(Url.bookCabUrl)
                        .setTimeout(10000)
                        //.setJsonObjectBody(json)
                        .setMultipartParameter("user_id", userPref.getString("id", ""))
                        .setMultipartParameter("username", userPref.getString("username", ""))
                        .setMultipartParameter("pickup_date_time", pickup_date_time)
                        .setMultipartParameter("drop_area", drop_point)
                        .setMultipartParameter("pickup_area", pickup_point)
                        .setMultipartParameter("time_type", DayNight)
                        .setMultipartParameter("amount", String.valueOf(totlePrice))
                        .setMultipartParameter("km", String.valueOf(distance))
                        .setMultipartParameter("pickup_lat", String.valueOf(PickupLatitude))
                        .setMultipartParameter("pickup_longs", String.valueOf(PickupLongtude))
                        .setMultipartParameter("drop_lat", String.valueOf(DropLatitude))
                        .setMultipartParameter("drop_longs", String.valueOf(DropLongtude))
                        .setMultipartParameter("isdevice", "1")
                        .setMultipartParameter("flag", "0")
                        .setMultipartParameter("taxi_type", truckType)
                        .setMultipartParameter("taxi_id", CabId)
                        .setMultipartParameter("area_id", AreaId)
                        .setMultipartParameter("purpose", transfertype)
                        .setMultipartParameter("comment", comment)
                        .setMultipartParameter("person", person)
                        .setMultipartParameter("payment_type", PaymentType)
                        .setMultipartParameter("book_create_date_time", BookingType)
                        .setMultipartParameter("transaction_id", transaction_id)
                        .setMultipartParameter("approx_time", AstTime)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception error, final JsonObject result) {
                                // do stuff with the result or error

                                Log.d("Booking results", "Booking results = " + result + "==" + error);
//                                rippleBackground.stopRippleAnimation();
//                                layout1.setVisibility(View.VISIBLE);
//                                layout2.setVisibility(View.VISIBLE);
//                                layout3.setVisibility(View.VISIBLE);
//                                layout4.setVisibility(View.VISIBLE);
//                                layout5.setVisibility(View.VISIBLE);
//                                layout6.setVisibility(View.VISIBLE);
//                                layout_confirm_request.setVisibility(View.VISIBLE);
//                                imageView.setVisibility(View.GONE);
//                                search.setVisibility(View.GONE);
//                                startActivity(new Intent(TripDetailActivity.this, SearchTruck.class));
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Intent ai = new Intent(TripDetailActivity.this, AllTripActivity.class);
//                                        startActivity(ai);
//                                        finish();
//                                    }
//                                }, 1000);
                                if (error == null) {


                                    try {
                                        String booking_id = "";
                                        JSONObject resObj = new JSONObject(result.toString());
                                        Log.d("Booking result", "Booking result = " + resObj);
                                        if (resObj.getString("status").equals("success")) {
                                            booking_id += resObj.getString("bookid");
//                                            booking_id += "1";
                                            System.out.println("Booking id : "+booking_id);
                                            //Common.showMkSucess(TripDetailActivity.this, resObj.getString("message").toString(), "yes");
                                            final String finalBooking_id = booking_id;
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Intent ai = new Intent(TripDetailActivity.this, SearchTruck.class);
                                                    ai.putExtra("book_id", finalBooking_id);
                                                    startActivity(ai);
                                                    finish();
                                                }
                                            }, 1000);
                                        }else if(resObj.getString("status").equals("false")){

                                            Common.user_InActive = 1;
                                            Common.InActive_msg = resObj.getString("message");

                                            SharedPreferences.Editor editor = userPref.edit();
                                            editor.clear();
                                            editor.commit();

                                            Intent logInt = new Intent(TripDetailActivity.this, LoginOptionActivity.class);
                                            logInt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            logInt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            logInt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(logInt);
                                        }
                                        else {
                                            Common.showMkError(TripDetailActivity.this, resObj.getString("error code").toString());
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
//
                                } else {
                                    Common.ShowHttpErrorMessage(TripDetailActivity.this, error.getMessage());
                                }
                            }
                        });


//                Log.d("confirmParam", "confirmParam = " + Url.bookCabUrl + "?" + confirmParam);
//                //ConfirmClient.get("",)
//                ConfirmClient.get(Url.bookCabUrl, confirmParam, new AsyncHttpResponseHandler() {
//
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                        Log.d("responseBody", "responseBody" + new String(responseBody));
//                        ProgressDialog.cancel();
//                        cusRotateLoading.stop();
//
//
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                        ProgressDialog.cancel();
//                        cusRotateLoading.stop();
//                        Common.ShowHttpErrorMessage(TripDetailActivity.this, error.getMessage());
//                    }
//                });
            }


        });

    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        try {
//            sSocket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    class ServerThread implements Runnable {
//
//        @Override
//        public void run() {
//            Socket socket = null;
//            try {
//                sSocket = new ServerSocket(SERVERPORT);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            while (!Thread.currentThread().isInterrupted()) {
//                try {
//                    socket = sSocket.accept();
//                    CommunicationThread commThread = new CommunicationThread(socket);
//                    new Thread(commThread).start();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    class CommunicationThread implements Runnable {

        private Socket clientSocket;
        private BufferedReader input;

        public CommunicationThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(TripDetailActivity.this, "Error connecting to driver!", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(TripDetailActivity.this, "Connected to driver", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    String read = input.readLine();
                    updateConversationHandler.post(new updateUIThread(read));
                    Toast.makeText(TripDetailActivity.this, "Waiting for acceptance from diver", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class updateUIThread implements Runnable {

        private String msg;

        public updateUIThread(String str) {
            this.msg = str;
        }

        @Override
        public void run() {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        txt_car_name = null;
        txt_pickup_point = null;
        txt_pickup_point_val = null;
        txt_drop_point = null;
        txt_drop_point_val = null;
        txt_truct_type = null;
        txt_truct_type_val = null;
        img_car_image = null;
        txt_distance = null;
        txt_distance_val = null;
        txt_distance_km = null;
        txt_booking_date = null;
        txt_booking_date_val = null;
        layout_back_arrow = null;
        txt_total_price = null;
        txt_total_price_dol = null;
        txt_total_price_val = null;
        layout_confirm_request = null;

        if (null != serverThread) {
            serverThread.interrupt();
            serverThread = null;
        }

    }

}
