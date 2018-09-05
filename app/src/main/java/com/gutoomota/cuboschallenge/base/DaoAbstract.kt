package com.gutoomota.cuboschallenge.base

import android.content.Context

import io.realm.Realm
import io.realm.RealmConfiguration

abstract class DaoAbstract
//RealmMigration migration = new MigrationRealm();

(context: Context) {

    protected var realmConfig: RealmConfiguration

    init {
        Realm.init(context)
        realmConfig = RealmConfiguration.Builder()
                //.migration(migration)
                .deleteRealmIfMigrationNeeded()
                .build()
    }
}