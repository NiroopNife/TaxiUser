//package come.texi.driver.Truck;
//
//import java.net.URL;
//
//import come.texi.driver.utils.Url;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class TruckClient {
//
//    private static Retrofit retrofit = null;
//
//    private TruckClient(){}
//
//    public static Retrofit sendStatus(){
//        if (retrofit == null){
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(Url.baseUrl)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//
//        return retrofit;
//    }
//}
