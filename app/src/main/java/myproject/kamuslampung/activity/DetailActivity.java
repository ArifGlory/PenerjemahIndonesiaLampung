package myproject.kamuslampung.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import myproject.kamuslampung.R;
import myproject.kamuslampung.adapter.DBAdapter;

public class DetailActivity extends AppCompatActivity {

    TextView txtNamaKata,txtArti;
    Intent intent;
    private String id_kata,nama_indo;
    DBAdapter mDB;
    protected Cursor cursor;
    SQLiteDatabase db;
    public static List<String> list_id_penghubung          = new ArrayList();
    public static List<String> list_id_lampung = new ArrayList();
    public static List<String> list_nama_indo   = new ArrayList();
    public static List<String> list_nama_lampung   = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        intent = getIntent();
        id_kata = intent.getStringExtra("id");
        nama_indo    = intent.getStringExtra("nama_indo");

        list_id_penghubung.clear();
        list_id_lampung.clear();
        list_nama_indo.clear();
        list_nama_lampung.clear();
        txtArti = findViewById(R.id.txtArti);
        txtNamaKata = findViewById(R.id.txtNamaKata);

        mDB = DBAdapter.getInstance(getApplicationContext());
        db = mDB.getWritableDatabase();
        cursor = db.rawQuery("SELECT " +
                "    penghubung.id_penghubung " +
                "    , kata_indo.id_indo " +
                "    , kata_indo.nama_indo, " +
                " kata_lampung.id_lampung " +
                "    , kata_lampung.nama_lampung " +
                "FROM " +
                "    penghubung " +
                "    , kata_lampung " +
                "    , kata_indo " +
                "where kata_indo.id_indo = penghubung.id_indo and kata_lampung.id_lampung=penghubung.id_lampung  " +
                "and kata_indo.id_indo="+id_kata,null);
        cursor.moveToFirst();
        for (int cc=0; cc < cursor.getCount(); cc++){
            cursor.moveToPosition(cc);
            list_id_penghubung.add(cursor.getString(0).toString());
            list_id_lampung.add(cursor.getString(3).toString());
            list_nama_lampung.add(cursor.getString(4).toString());
        }

        txtArti.setText("\n");
        for (int c=0;c<list_id_lampung.size();c++){
            txtArti.append((c+1)+". "+list_nama_lampung.get(c) + " \n\n");
        }

        txtNamaKata.setText(nama_indo);


    }

    public void keHome(View view) {
        Intent intent = new Intent(DetailActivity.this,HomeActivity.class);
        startActivity(intent);
    }
}
