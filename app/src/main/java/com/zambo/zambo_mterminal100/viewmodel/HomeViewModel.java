package com.zambo.zambo_mterminal100.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.zambo.zambo_mterminal100.model.ActiveServiceResponse;
import com.zambo.zambo_mterminal100.repositories.HomeRepository;

public class HomeViewModel extends AndroidViewModel {
    private HomeRepository homeRepository;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        homeRepository =new HomeRepository(application);
    }

    public LiveData<ActiveServiceResponse> getActiveService(String userId) {
        return homeRepository.getActiveService(userId);
    }
}
