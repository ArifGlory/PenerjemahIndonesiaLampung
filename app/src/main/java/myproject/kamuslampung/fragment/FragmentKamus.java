package myproject.kamuslampung.fragment;


import android.content.ClipData;
import android.content.Context;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import android.content.ClipboardManager;


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
    Button btnTerjemah;
    String translated_id; //ini cm buat dummy aja

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
        btnTerjemah = view.findViewById(R.id.btnTerjemah);
        txtArti = view.findViewById(R.id.txtArti);

        cursor.moveToFirst();
        for (int cc=0; cc < cursor.getCount(); cc++){
            cursor.moveToPosition(cc);

            if (cursor.getString(1) == null || cursor.getString(1).length() == 0){
                Log.d("getDB:","kata kosong ID  : "+cursor.getString(0));
            }else{
                list_id.add(cursor.getString(0).toString());
                idFilter.add(cursor.getString(0).toString());

                list_nama_indo.add(cursor.getString(1).toString());
                arrayFilter.add(cursor.getString(1).toString());
            }

        }


        btnTerjemah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etKalimat.getText().toString().length() > 0){
                    String kalimat = etKalimat.getText().toString();
                    doTranslate(kalimat);
                }else{
                    Toast.makeText(getActivity(),"Anda belum menulis apapun",Toast.LENGTH_SHORT).show();
                }


            }
        });
        txtArti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String arti = txtArti.getText().toString();
                if (arti.equals(".") || arti == null){

                }else{
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Kalimat bahasa Lampung", arti);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getActivity(),"Kalimat dicopy ke clipboard",Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    private void doTranslate(String kalimat){
        //String kalimat  = ""+s;
        String[] frasa  = kalimat.split(" ");
        list_translate.clear();
        list_translate_id.clear();
        list_hasil_translate.clear();
        txtArti.setText("");

        //simpan frasa
        for (int d=0;d<frasa.length;d++){
            list_translate.add(frasa[d].toLowerCase().toString());
        }

        Log.d("jml_frasa:",""+list_translate.size());
        for (int d=0;d<list_translate.size();d++){

            for (int c=0;c<list_nama_indo.size();c++){
                String translate = list_translate.get(d);
                String nama_indo = list_nama_indo.get(c).toLowerCase();


                if (translate.equals(nama_indo)){
                    //binary search
                    int index_translate = Collections.binarySearch(list_nama_indo,translate);
                    translated_id = list_id.get(index_translate);

                    String translate_id = list_id.get(c);
                    list_translate_id.add(translate_id);

                    if (isNumeric(translate)){
                        translate = "'"+translate;
                    }
                    if (list_translate_id.contains(translate)){
                        list_translate_id.remove(translate);
                    }
                    //sudahi for nya goto dext d
                    c = list_nama_indo.size();
                }else{

                    if (isNumeric(translate)){
                        translate = "'"+translate;
                    }
                    if (!list_translate_id.contains(translate)){
                        list_translate_id.add(translate);
                    }
                }
            }
        }

        for (int a=0;a<list_translate_id.size();a++){
            //txtArti.append("ID = "+list_translate_id.get(a)+" \n");
            String idIndo = list_translate_id.get(a);
            String kataLampung = getArtiById(idIndo);
            list_hasil_translate.add(kataLampung);
        }

        //Collections.reverse(list_hasil_translate);
        Log.d("jml_hasil_trans:",""+list_hasil_translate.size());
        for (int b=0;b<list_hasil_translate.size();b++){
            txtArti.append(""+list_hasil_translate.get(b)+" ");
        }
    }

    public String getArtiById(String idKata){

        list_id_penghubung.clear();
        list_nama_lampung.clear();
        list_id_lampung.clear();
        String hasil = "";

        if (isNumeric(idKata)){
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

            hasil = list_nama_lampung.get(0).toString();
        }else{
            String karakterPertama = idKata.substring(0,1);
            Log.d("karakterpertama: ",karakterPertama);
            if (karakterPertama.equals("'")){
                idKata = idKata.substring(1);
                Log.d("idkatanya:",idKata);
            }
            hasil = idKata;
        }



       // return list_nama_lampung.get(0).toString();
        return hasil;
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
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
