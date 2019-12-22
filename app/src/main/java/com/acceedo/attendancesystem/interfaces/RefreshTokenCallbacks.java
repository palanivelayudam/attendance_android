package com.acceedo.attendancesystem.interfaces;


import androidx.annotation.NonNull;

public interface RefreshTokenCallbacks
{
    void onSuccess(@NonNull boolean value);

    void onError(@NonNull Throwable throwable);
}
