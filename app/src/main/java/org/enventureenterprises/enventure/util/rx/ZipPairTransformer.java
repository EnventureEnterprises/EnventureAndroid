package org.enventureenterprises.enventure.util.rx;

import android.support.annotation.NonNull;
import android.util.Pair;

import rx.Observable;

/**
 * Created by mossplix on 4/29/17.
 */

public final class ZipPairTransformer<T, R> implements Observable.Transformer<T, Pair<T, R>> {
    @NonNull
    private final Observable<R> second;

    public ZipPairTransformer(final @NonNull Observable<R> second) {
        this.second = second;
    }

    @Override
    @NonNull public Observable<Pair<T, R>> call(final @NonNull Observable<T> first) {
        return Observable.zip(first, second, Pair::new);
    }
}
