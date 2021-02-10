package com.zambo.zambo_mterminal100.repositories;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.zambo.zambo_mterminal100.model.ActiveServiceResponse;
import com.zambo.zambo_mterminal100.network.RestApiService;
import com.zambo.zambo_mterminal100.network.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeRepository {

    private Application application;

    public HomeRepository(Application application){
        this.application = application;
    }

    public LiveData<ActiveServiceResponse> getActiveService(String userId) {
        MutableLiveData<ActiveServiceResponse> activeServiceMutableLiveData = new MutableLiveData<>();

        RestApiService apiService = RetrofitInstance.getApiService();

        Call<ActiveServiceResponse> call = apiService.activeService(userId);
        call.enqueue(new Callback<ActiveServiceResponse>() {
            @Override
            public void onResponse(@NonNull Call<ActiveServiceResponse> call, @NonNull Response<ActiveServiceResponse> response) {
                if (response.isSuccessful()) {
                    activeServiceMutableLiveData.setValue(response.body());
                } else {
                    Log.e(this.getClass().getSimpleName(),"ERROR SERVICE");
//                    JSONObject jsonObject;
//                    try {
//                        loginResponse = new LoginResponse();
//                        jsonObject = new JSONObject(response.errorBody().string());
//                        JSONObject jsonObject1 = jsonObject.getJSONObject("error");
//                        String userMessage = jsonObject1.getString("message");
//                        int errorCode = jsonObject1.optInt("status_code");
//                        loginResponse.setMessage(userMessage);
//                        loginResponse.setStatusCode(errorCode);
//                        loginResponseMutableLiveData.setValue(loginResponse);
//                        Log.e(this.getClass().getName(),"Error_code" + userMessage );
//
//                    } catch (JSONException | IOException e) {
//                        e.printStackTrace();
//                    }
                }
            }


            @Override
            public void onFailure(@NonNull Call<ActiveServiceResponse> call, @NonNull Throwable t) {
                Log.e("Reposotry","Error==" + t.toString());
//                loginResponse = new LoginResponse();
//                loginResponse.setMessage("Network error");
//                loginResponseMutableLiveData.setValue(loginResponse);
            }
        });

        return activeServiceMutableLiveData;
    }
}
