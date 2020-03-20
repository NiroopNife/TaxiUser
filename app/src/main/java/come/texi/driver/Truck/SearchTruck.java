package come.texi.driver.Truck;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.skyfishjy.library.RippleBackground;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import come.texi.driver.AllTripActivity;
import come.texi.driver.R;
import come.texi.driver.utils.Url;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit.http.Multipart;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;

public class SearchTruck extends AppCompatActivity {

    Timer timer;
    String bookingid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searching_truck);

        Bundle bundle = getIntent().getExtras();
        bookingid = bundle.getString("book_id");
        System.out.println("Booking id is : "+bookingid);

        final RippleBackground rippleBackground = findViewById(R.id.content);
        rippleBackground.startRippleAnimation();



        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(SearchTruck.this, AllTripActivity.class));
            }
        }, 10000);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("bookingid", bookingid)
                .build();

        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(Url.baseUrl+"booking_status")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("Search truck "+response.body().string());
                if (response.isSuccessful()){
                    timer.cancel();
                    startActivity(new Intent(SearchTruck.this, AllTripActivity.class));
                }
            }
        });

//        Retrofit retrofit = TruckClient.sendStatus();
//        TruckService service = retrofit.create(TruckService.class);
//        Call<TruckResponse> call = service.sendStatus(bookingid);
//        call.enqueue(new Callback<TruckResponse>() {
//            @Override
//            public void onResponse(Call<TruckResponse> call, Response<TruckResponse> response) {
//                if (response.isSuccessful()){
//                    TruckResponse resObj = response.body();
//                    if (resObj.getStatus().equals("true")){
//                        timer.cancel();
//                        startActivity(new Intent(SearchTruck.this, AllTripActivity.class));
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<TruckResponse> call, Throwable t) {
//
//            }
//        });
    }
}
