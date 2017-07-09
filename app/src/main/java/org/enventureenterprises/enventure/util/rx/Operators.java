package org.enventureenterprises.enventure.util.rx;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

/**
 * Created by mossplix on 4/28/17.
 */

public final class Operators {
    private Operators() {}

    /**
     * When a response errors, send an {@link ApiException} or {@link ResponseException} to
     * {@link Subscriber#onError}, otherwise send the response to {@link Subscriber#onNext}.
     */
    public static @NonNull
    <T> ApiErrorOperator<T> apiError(final @NonNull Gson gson) {
        return new ApiErrorOperator<>(gson);
    }
}
