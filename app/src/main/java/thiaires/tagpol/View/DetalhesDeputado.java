package thiaires.tagpol.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.facebook.drawee.backends.pipeline.Fresco;

import thiaires.tagpol.Modelo.Deputado;
import thiaires.tagpol.R;
import thiaires.tagpol.controle.Fragments.Detalhes.AnualFragment;
import thiaires.tagpol.controle.Fragments.Detalhes.DetalhesDeputadoFragment;
import thiaires.tagpol.controle.Fragments.Detalhes.MensalFragment;

public class DetalhesDeputado extends AppCompatActivity  implements BottomNavigationView.OnNavigationItemSelectedListener{
    private Deputado deputado;
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.detalhesDeputado:
                fragment = new DetalhesDeputadoFragment();
                break;
            case R.id.mensal:
                fragment = new MensalFragment();
                break;
            case R.id.anual:
                fragment = new AnualFragment();
                break;
        }
        Bundle b = new Bundle();
        b.putSerializable("dep", deputado);
        Log.i("FRAG", deputado.toString());
        if (fragment != null) {
            fragment.setArguments(b);
        }
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentLayout, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_detalhes_deputado);
        deputado = (Deputado) getIntent().getSerializableExtra("dep");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        Fragment f = new DetalhesDeputadoFragment();
        Bundle b = new Bundle();
        b.putSerializable("dep", deputado);
        f.setArguments(b);
        loadFragment(f);
    }

}
