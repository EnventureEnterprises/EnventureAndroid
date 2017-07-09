package org.enventureenterprises.enventure.util.rx;

import android.support.annotation.NonNull;
import android.util.Pair;

import rx.Observable;

/**
 * Created by mossplix on 4/29/17.
 */

public final class CombineLatestPairTransformer<S, T> implements Observable.Transformer<S, Pair<S, T>> {
    @NonNull
    private final Observable<T> second;

    public CombineLatestPairTransformer(final @NonNull Observable<T> second) {
        this.second = second;
    }

    @Override
    @NonNull public Observable<Pair<S, T>> call(final @NonNull Observable<S> first) {
        return Observable.combineLatest(first, second, Pair::new);
    }
}
