package com.example.exchangeapp.Database;

import android.widget.RadioButton;

import com.example.exchangeapp.Bean.Rate;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class RealmHelper {

    public void save(final Rate e) {
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(e);
            }
        });
    }

    public boolean delete(final String country) {
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Rate> listMultimedia = realm.where(Rate.class).equalTo("country", country).findAll();
                listMultimedia.deleteAllFromRealm();
            }
        });
        return Realm.getDefaultInstance().where(Rate.class).equalTo("country", country).findAll().size() == 0;
    }

    public List<Rate> read(String country) {
        return Realm.getDefaultInstance().where(Rate.class).equalTo("country", country).findAll();
    }

    public List<Rate> readAll() {
        return Realm.getDefaultInstance().where(Rate.class).findAll();
    }


    public List<Rate> readAllAscending() {
        return Realm.getDefaultInstance().where(Rate.class).findAll().sort("country",Sort.ASCENDING);
    }

    public List<Rate> readAllSelectedAscending() {
        return Realm.getDefaultInstance().where(Rate.class).equalTo("isSelected",true).findAll().sort("country",Sort.ASCENDING);
    }

    public Rate readOne(String country) {
        return Realm.getDefaultInstance().where(Rate.class).equalTo("country", country).findFirst();
    }

    public void updateValue(final String country, final double value) {
        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Rate rate = readOne(country);
                rate.setValue(value);
                realm.copyToRealmOrUpdate(rate);
            }
        });
    }

    public void updateSelected(final String country, final boolean isSelected) {
        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Rate rate = readOne(country);
                rate.setSelected(isSelected);
                realm.copyToRealmOrUpdate(rate);
            }
        });
    }




}
