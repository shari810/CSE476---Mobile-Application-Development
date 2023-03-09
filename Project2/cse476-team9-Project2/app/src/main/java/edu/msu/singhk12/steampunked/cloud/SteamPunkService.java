package edu.msu.singhk12.steampunked.cloud;

import static edu.msu.singhk12.steampunked.cloud.Cloud.CHANGETURN_PATH;
import static edu.msu.singhk12.steampunked.cloud.Cloud.CREATE_PATH;
import static edu.msu.singhk12.steampunked.cloud.Cloud.DELETEPIPE_PATH;
import static edu.msu.singhk12.steampunked.cloud.Cloud.DELETEROOM_PATH;
import static edu.msu.singhk12.steampunked.cloud.Cloud.JOIN_PATH;
import static edu.msu.singhk12.steampunked.cloud.Cloud.LOGIN_PATH;
import static edu.msu.singhk12.steampunked.cloud.Cloud.REGISTER_PATH;
import static edu.msu.singhk12.steampunked.cloud.Cloud.ROOMINFO_PATH;
import static edu.msu.singhk12.steampunked.cloud.Cloud.ROOMSTATE_PATH;
import static edu.msu.singhk12.steampunked.cloud.Cloud.SAVE_PATH;
import static edu.msu.singhk12.steampunked.cloud.Cloud.LOAD_PATH;
import static edu.msu.singhk12.steampunked.cloud.Cloud.TURNINFO_PATH;

import edu.msu.singhk12.steampunked.cloud.model.LoadPipe;
import edu.msu.singhk12.steampunked.cloud.model.RegisterResult;
import edu.msu.singhk12.steampunked.cloud.model.Room;
import edu.msu.singhk12.steampunked.cloud.model.SavePipe;
import edu.msu.singhk12.steampunked.cloud.model.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SteamPunkService {
    @GET(LOGIN_PATH)
    Call<User> logIn(
            @Query("user") String userId,
            @Query("magic") String magic,
            @Query("pw") String password
    );

    @FormUrlEncoded
    @POST(REGISTER_PATH)
    Call<RegisterResult> register(@Field("xml") String xmlData);

    @FormUrlEncoded
    @POST(CREATE_PATH)
    Call<Room> createRoom(@Field("xml") String xmlData);

    @FormUrlEncoded
    @POST(JOIN_PATH)
    Call<Room> joinRoom(@Field("xml") String xmlData);

    @FormUrlEncoded
    @POST(SAVE_PATH)
    Call<User> savePipe(@Field("xml") String xmlData);

    @GET(LOAD_PATH)
    Call<LoadPipe> loadPipe(
            @Query("user") String username,
            @Query("magic") String magic,
            @Query("pw") String password
            //@Query("id") String idHatToLoad
    );

    @GET(ROOMSTATE_PATH)
    Call<User> roomState(
            @Query("user") String userId,
            @Query("magic") String magic,
            @Query("pw") String password
    );

    @GET(ROOMINFO_PATH)
    Call<ResponseBody> roomInfo(
            @Query("user") String userId,
            @Query("magic") String magic,
            @Query("pw") String password,
            @Query("index") String index
    );

    @GET(TURNINFO_PATH)
    Call<ResponseBody> turnInfo(
            @Query("user") String userId,
            @Query("magic") String magic,
            @Query("pw") String password
    );

    @GET(CHANGETURN_PATH)
    Call<ResponseBody> changeTurn(
            @Query("user") String userId,
            @Query("magic") String magic,
            @Query("pw") String password
    );

    @GET(DELETEPIPE_PATH)
    Call<User> deletePipe(
            @Query("user") String userId,
            @Query("magic") String magic,
            @Query("pw") String password
    );

    @GET(DELETEROOM_PATH)
    Call<User> deleteRoom(
            @Query("user") String userId,
            @Query("magic") String magic,
            @Query("pw") String password
    );
}

