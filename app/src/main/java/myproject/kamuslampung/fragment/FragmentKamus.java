package myproject.kamuslampung.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import myproject.kamuslampung.R;
import myproject.kamuslampung.activity.DetailActivity;
import myproject.kamuslampung.activity.HomeActivity;
import myproject.kamuslampung.adapter.DBAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentKamus extends Fragment {


    ArrayAdapter<String> adapter;
    ListView listKamus;
    DBAdapter mDB;
    protected Cursor cursor;
    SQLiteDatabase db;
    public static List<String> list_id          = new ArrayList();
    public static List<String> list_nama_indo   = new ArrayList();
    public static List<String> list_translate   = new ArrayList();
    public static List<String> list_translate_id   = new ArrayList();
    public static List<String> list_hasil_translate   = new ArrayList();
    public static List<String> arrayFilter      = new ArrayList();
    public static List<String> idFilter      = new ArrayList();

    public static List<String> list_id_penghubung          = new ArrayList();
    public static List<String> list_id_lampung = new ArrayList();
    public static List<String> list_nama_lampung   = new ArrayList();
    EditText etKalimat;
    TextView txtArti;

    public FragmentKamus() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_kamus, container, false);
        mDB = DBAdapter.getInstance(getActivity());


        list_id.clear();
        list_nama_indo.clear();

        db = mDB.getWritableDatabase();
        cursor = db.rawQuery("SELECT * FROM kata_indo",null);
        etKalimat = view.findViewById(R.id.etKalimat);
        txtArti = view.findViewById(R.id.txtArti);

        cursor.moveToFirst();
        for (int cc=0; cc < cursor.getCount(); cc++){
            cursor.moveToPosition(cc);
            list_id.add(cursor.getString(0).toString());
            idFilter.add(cursor.getString(0).toString());

            list_nama_indo.add(cursor.getString(1).toString());
            arrayFilter.add(cursor.getString(1).toString());
        }

        etKalimat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String kalimat  = ""+s;
                String[] frasa  = kalimat.split(" ");
                list_translate.clear();
                list_translate_id.clear();
                list_hasil_translate.clear();
                txtArti.setText("");

                //simpan frasa
               for (int d=0;d<frasa.length;d++){
                   list_translate.add(frasa[d].toLowerCase().toString());
               }

               //ambil iD tiap frasa
              /* for (int c=0;c<list_nama_indo.size();c++){

                   for (int d=0;d<list_translate.size();d++){
                       String translate = list_translate.get(d);
                       String nama_indo = list_nama_indo.get(c).toLowerCase();

                       if (translate.equals(nama_indo)){
                           String translate_id = list_id.get(c);
                           list_translate_id.add(translate_id);
                       }
                   }
               }*/

                for (int d=0;d<list_translate.size();d++){

                    for (int c=0;c<list_nama_indo.size();c++){
                        String translate = list_translate.get(d);
                        String nama_indo = list_nama_indo.get(c).toLowerCase();

                        if (translate.equals(nama_indo)){
                            String translate_id = list_id.get(c);
                            list_translate_id.add(translate_id);
                        }
                    }
                }


               Log.d("tranlate id  = ",""+list_translate_id.size());
               Log.d("list translate  = ",""+list_translate.size());

               //cari artinya


               for (int a=0;a<list_translate_id.size();a++){
                   //txtArti.append("ID = "+list_translate_id.get(a)+" \n");
                   String idIndo = list_translate_id.get(a);
                   String kataLampung = getArtiById(idIndo);
                   list_hasil_translate.add(kataLampung);
               }

               //Collections.reverse(list_hasil_translate);
               for (int b=0;b<list_hasil_translate.size();b++){
                   txtArti.append(""+list_hasil_translate.get(b)+" ");
               }

//               for (int c=0;c< frasa.length;c++){
//                   txtArti.append(frasa[c] + " \n");
//               }
            }
        });



        return view;
    }

    public String getArtiById(String idKata){

        list_id_penghubung.clear();
        list_nama_lampung.clear();
        list_id_lampung.clear();

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
                "and kata_indo.id_indo="+idKata,null);
        cursor.moveToFirst();
        for (int cc=0; cc < cursor.getCount(); cc++){
            cursor.moveToPosition(cc);
            list_id_penghubung.add(cursor.getString(0).toString());
            list_id_lampung.add(cursor.getString(3).toString());
            list_nama_lampung.add(cursor.getString(4).toString());
        }

        return list_nama_lampung.get(0).toString();
    }

    /*public void filter(String keyword){
        keyword = keyword.toLowerCase();
        arrayFilter.clear();
        idFilter.clear();

        for (int c = 0; c < list_nama_indo.size(); c++ ){
            if (list_nama_indo.get(c).toLowerCase().contains(keyword) ){
                idFilter.add(list_id.get(c));
                arrayFilter.add(list_nama_indo.get(c));
            }
        }

        adapter.notifyDataSetChanged();

    }*/



}
