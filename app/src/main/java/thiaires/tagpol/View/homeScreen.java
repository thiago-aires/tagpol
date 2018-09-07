package thiaires.tagpol.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.facebook.drawee.backends.pipeline.Fresco;

import thiaires.tagpol.R;
import thiaires.tagpol.controle.Fragments.homescreen.DeputadoFragment;
import thiaires.tagpol.controle.Fragments.homescreen.GraficosFragment;
import thiaires.tagpol.controle.Fragments.homescreen.SobreFragment;

public class homeScreen extends AppCompatActivity  implements BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_home_screen);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        loadFragment(new DeputadoFragment());
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_deputados:
                fragment = new DeputadoFragment();
                break;
            case R.id.navigation_graficos:
                fragment = new GraficosFragment();
                break;
            case R.id.navigation_sobre:
                fragment = new SobreFragment();
                break;
        }
        return loadFragment(fragment);
    }
}
