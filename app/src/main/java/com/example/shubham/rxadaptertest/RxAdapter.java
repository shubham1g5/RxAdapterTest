package com.example.shubham.rxadaptertest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by dimagi on 22/09/17.
 */

public class RxAdapter extends BaseAdapter {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null);
        loadTextView(position, view.findViewById(R.id.text));
        return view;
    }

    private void loadTextView(final int position, final TextView tv) {
       compositeDisposable.add(getObservable(position)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> tv.setText("" + integer), new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                })
        );
    }

    private Observable<Integer> getObservable(final int position) {
        return Observable.fromCallable((Callable)() -> {
            Thread.sleep(10000);
            return position;
        });
    }

    public void onDestroy() {
        compositeDisposable.clear();
    }
}
