package com.sciento.wumu.gpscontroller.DeviceSdk;

import com.sciento.wumu.gpscontroller.CommonModule.AppContext;
import com.sciento.wumu.gpscontroller.ConfigModule.Config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.RealmSchema;

/**
 * Created by wumu on 17-7-17.
 */

public class RealmDbHelper {
    private static RealmDbHelper realmDbHelper = new RealmDbHelper(Config.REALMDB,Config.dbVersion);

    protected RealmConfiguration mDefaultConfig;
    protected Set<RealmConfiguration> mConfigsList;
    private RealmDbHelper(String dbName, int dbVersion){
        mDefaultConfig = new RealmConfiguration.Builder()
                .schemaVersion(dbVersion)
                .migration(migration)
                .name(dbName)
                .build();
        mConfigsList = new HashSet<RealmConfiguration>();
    }


    public static RealmDbHelper getInstance(){
        return realmDbHelper;
    }



    protected Realm createRealm() {
        //一个Realm只能在同一个线程中访问，在子线程中进行数据库操作必须重新获取Realm对象
        return createRealmByConfig(mDefaultConfig);
    }
    protected Realm createRealmByConfig(RealmConfiguration config){
        mConfigsList.add(config);
        return Realm.getInstance(config);
    }
    /**
     * 添加指定类到数据库
     * @param info
     */
    public void insertRealmObject(RealmObject info){
        Realm realm = createRealm();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(info);
        realm.commitTransaction();
        realm.close();
    }
    /**
     * 批量插入数据
     * @param infos
     */
    public void insertRealmObjects(List<? extends RealmObject> infos){
        Realm realm = createRealm();
        realm.beginTransaction();
        //要使用update这种方法，那插入的对象必须是有主键的
        realm.copyToRealmOrUpdate(infos);
        realm.commitTransaction();
        realm.close();
    }
    /**
     * 查询指定类的全部存储信息
     * @return
     * @param clazz
     */
    public List<? extends RealmObject> queryRealmObjects(Class<? extends RealmObject> clazz){
        Realm realm = createRealm();
        RealmResults<? extends RealmObject> realmResults = realm.where(clazz).findAll();
        List<? extends RealmObject> arrayList = realm.copyFromRealm(realmResults);
        realm.close();
        return arrayList;
    }
    /**
     * 删除指定类的全部数据库信息
     */
    public void deleteRealmObjects(Class<? extends RealmObject> clazz){
        Realm realm = createRealm();
        realm.beginTransaction();
        realm.delete(clazz);
        realm.commitTransaction();
        realm.close();
    }
    /**
     * 删除此realm对应的全部数据库信息
     */
    public void deleteAllRealmObjects(){
        Realm realm = createRealm();
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
        realm.close();
    }
    /**
     * 升级数据库
     */
    protected RealmMigration migration = new RealmMigration() {//升级数据库
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            // DynamicRealm exposes an editable schema
            RealmSchema schema = realm.getSchema();
            if (oldVersion > newVersion) {//数据库降级
                if (mConfigsList != null && mConfigsList.size() > 0) {
                    for (RealmConfiguration config : mConfigsList) {
                        Realm.deleteRealm(config);
                    }
                }
            } else if (oldVersion < newVersion) {//数据库升级
            }
        }
    };
}
