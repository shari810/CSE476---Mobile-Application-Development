package edu.msu.sharinic.cloudhatter.Cloud;

import edu.msu.sharinic.cloudhatter.Cloud.Models.Catalog;
import edu.msu.sharinic.cloudhatter.Cloud.Models.LoadResult;
import edu.msu.sharinic.cloudhatter.Cloud.Models.SaveResult;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static edu.msu.sharinic.cloudhatter.Cloud.Cloud.CATALOG_PATH;
import static edu.msu.sharinic.cloudhatter.Cloud.Cloud.LOAD_PATH;
import static edu.msu.sharinic.cloudhatter.Cloud.Cloud.SAVE_PATH;

public interface HatterService {
    @GET(CATALOG_PATH)
    Call<Catalog> getCatalog(
            @Query("user") String userId,
            @Query("magic") String magic,
            @Query("pw") String password
    );

    @GET(LOAD_PATH)
    Call<LoadResult> loadHat(
            @Query("user") String userId,
            @Query("magic") String magic,
            @Query("pw") String password,
            @Query("id") String idHatToLoad
    );

    @FormUrlEncoded
    @POST(SAVE_PATH)
    Call<SaveResult> saveHat(@Field("xml") String xmlData);

}
