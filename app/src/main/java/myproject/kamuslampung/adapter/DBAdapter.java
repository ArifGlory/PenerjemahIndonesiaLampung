package myproject.kamuslampung.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Glory on 28/09/2016.
 */
public class DBAdapter extends SQLiteAssetHelper {

    private static final String DB_NAME         = "db_kamuslampung2";
    private static final int   DB_VER           = 1;
    public static final String TABLE_INDO       = "kata_indo";
    public static final String TABLE_LAMPUNG    = "kata_lampung";
    public static final String TABLE_PENGHUBUNG = "penghubung";

    public static final String COL_ID_INDO      = "id_indo";
    public static final String COL_ID_LAMPUNG   = "id_lampung";
    public static final String COL_ID_PENGHUBUNG= "id_penghubung";
    public static final String COL_NAMA_INDO    = "nama_indo";
    public static final String COL_NAMA_LAMPUNG = "nama_lampung";

    public static DBAdapter        dbInstance;
    public static SQLiteDatabase db;



    /**
     * private Constructor, untuk menggunakan kelas ini gunakan getInstance()
     * @param context
     */


    private DBAdapter(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }


    public  static synchronized DBAdapter getInstance(Context context){

        if (dbInstance == null){
            dbInstance = new DBAdapter(context.getApplicationContext());
            db = dbInstance.getReadableDatabase();
        }

        return dbInstance;
    }

    public SQLiteDatabase ambilDB(){
        db = this.getWritableDatabase();
        return db;

    }

    public void getSemuaKata(){

    }

   /* public DBAdapter getInstance(Context context){

        if (dbInstance == null){

            dbInstance = new DBAdapter(context);
            db = dbInstance.getReadableDatabase();

        }

        return  dbInstance;
    }*/



    @Override
    public synchronized void close(){

        super.close();
        if (dbInstance!=null){

            dbInstance.close();
        }
    }


//method untuk mengambil semua data soal





}
