package pollub.ism.ism_lab06;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import pollub.ism.ism_lab06.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private ArrayAdapter<CharSequence> adapter;
    MagazynBazaDanych bazaDanych;
    String wybraneWarzywoNazwa=null;
    Integer wybraneWarywoIlosc=null;

    public enum OperacjaMagazynowa{SKLADUJ,WYDAJ}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter=ArrayAdapter.createFromResource(this,R.array.Asortyment, android.R.layout.simple_dropdown_item_1line);
        binding.spinner.setAdapter(adapter);

       binding.przyciskSkladuj.setOnClickListener(v -> zmienStan(OperacjaMagazynowa.SKLADUJ));

       binding.przyciskWydaj.setOnClickListener(v-> zmienStan(OperacjaMagazynowa.WYDAJ));

       binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            wybraneWarzywoNazwa=adapter.getItem(position).toString();
            aktualizuj();
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

       bazaDanych=new MagazynBazaDanych(this);
    }

    private void aktualizuj(){
        wybraneWarywoIlosc=bazaDanych.podajIlosc(wybraneWarzywoNazwa);
        binding.tekstStanuMagazynu.setText("Stan magazynu dla "+ wybraneWarzywoNazwa+" wynosi: "+wybraneWarywoIlosc);

    }

    private void zmienStan(OperacjaMagazynowa operacja){
        Integer zmianaIlosci,nowaIlosc=null;
        try{
            zmianaIlosci=Integer.parseInt(binding.edycjaIlosc.getText().toString());
        }catch (NumberFormatException ex){
            return;
        }finally {
            binding.edycjaIlosc.setText("");
        }

        switch (operacja){
            case SKLADUJ:nowaIlosc=wybraneWarywoIlosc+zmianaIlosci;break;
            case WYDAJ:
                if(wybraneWarywoIlosc>zmianaIlosci)
                nowaIlosc=wybraneWarywoIlosc-zmianaIlosci;
                else {
                    nowaIlosc=0;
                    Toast.makeText(this,"Ujemna ilość towarów",Toast.LENGTH_SHORT).show();
                }
                break;

        }
        bazaDanych.zmienStanMagazynu(wybraneWarzywoNazwa,nowaIlosc);
        aktualizuj();

    }
}