package thiaires.tagpol.controle;

import android.content.Context;

import java.io.Serializable;

import thiaires.tagpol.Modelo.Deputados;
import thiaires.tagpol.controle.Adapter.DeputadoAdapter;

public class Controlador implements Serializable {
    private Deputados deputados;
    private DeputadoAdapter deputadoAdapter;


    public Controlador(Context c){
        this.deputados = new Deputados();
        this.deputadoAdapter = new DeputadoAdapter(c, deputados);
    }

    public Deputados getDeputados() {
        return deputados;
    }

    public void setDeputados(Deputados deputados) {
        this.deputados = deputados;
    }



}
