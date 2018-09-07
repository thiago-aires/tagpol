package thiaires.tagpol.controle.Fragments.homescreen;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import thiaires.tagpol.ClienteWsCamara.InicializadorRetrofit;
import thiaires.tagpol.Modelo.Deputados;
import thiaires.tagpol.R;
import thiaires.tagpol.controle.Adapter.DeputadoAdapter;

public class DeputadoFragment extends Fragment{

    private View v;
    private RecyclerView recycler;
    private DeputadoAdapter deputadoAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Deputados deputados;
    private int col;
    private Context context;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        v = inflater.inflate(R.layout.deputado_fragment, container, false) ;
        deputados = new Deputados();
        loadDeputados();
        recycler = (RecyclerView) v.findViewById(R.id.recycler);
        Configuration c = getResources().getConfiguration();
        if(c.orientation == Configuration.ORIENTATION_LANDSCAPE) col = 3;
        else col = 2;
        layoutManager = new GridLayoutManager(context, col, GridLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(layoutManager);
        deputadoAdapter = new DeputadoAdapter(context, deputados);
        recycler.setAdapter(deputadoAdapter);
        recycler.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                if(!view.canScrollVertically(0))
                    loadDeputados();
            }
        });

        return v;
    }

    public void loadDeputados(){
        //Log.i("DEP", "loadDeputados");
        String last = "";
        String self = "";
        for (Deputados.Link l : deputados.getLinks())
            if (l.getRel().equals("last"))
                last = l.getHref();
        for (Deputados.Link l : deputados.getLinks())
            if (l.getRel().equals("self"))
                self = l.getHref();
        int l=getPagina(last);
        int s=getPagina(self);
        if(s == 0 && l == 0)
            retrofit(1);
        else if(s+1<=l)
            retrofit(s+1);
    }

    private int getPagina(String link){
        if(link.equals(""))
            return 0;

        int i = link.indexOf("pagina=");
        link = link.substring(i, link.length());
        String[] links = link.split("&");
        String pagina = links[0].substring(7,links[0].length());

        Log.i("DEP", "link pagina " + pagina);
        return Integer.parseInt(pagina);
    }

    public void addDeputado(Deputados aux){
        //Log.i("DEP", "addDeputados count atual" + deputados.getDados().size());
        //Log.i("DEP", aux.toString());
        for(Deputados.Dado d : aux.getDados()){
            deputados.addDado(d);
        }
        //organizaNomeAlfabetico();
        //Log.i("DEP", "addDeputados count apos" + deputados.getDados().size());
        deputados.setLinks(aux.getLinks());
        //getPagina(deputados.getLinks().get(1).getHref());
        deputadoAdapter.setDeputados(deputados);
        deputadoAdapter.notifyDataSetChanged();
    }

    public void organizaNomeAlfabetico() {
        Collections.sort(this.deputados.getDados(), new Comparator<Deputados.Dado>() {
            @Override
            public int compare(Deputados.Dado deputado, Deputados.Dado t1) {
                return deputado.getNome().compareToIgnoreCase(t1.getNome());
            }
        });
    }

    public void organizaPartidoAlfabetico() {
        Collections.sort(this.deputados.getDados(), new Comparator<Deputados.Dado>() {
            @Override
            public int compare(Deputados.Dado deputado, Deputados.Dado t1) {
                return deputado.getSiglaPartido().compareToIgnoreCase(t1.getNome());
            }
        });
    }

    public void organizaEstadoAlfabetico() {
        Collections.sort(this.deputados.getDados(), new Comparator<Deputados.Dado>() {
            @Override
            public int compare(Deputados.Dado deputado, Deputados.Dado t1) {
                return deputado.getSiglaUf().compareToIgnoreCase(t1.getNome());
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle b = new Bundle();
        //Log.i("DEP", "salvando deputados");
        outState.putSerializable("deputados", deputados);
    }

    private void retrofit(int pag){
        try{
            Call<Deputados> call = new InicializadorRetrofit().getCamaraService().getDeputados(pag); //cria uma requisição
            call.enqueue(new Callback<Deputados>() { // enqueue significa uma requisição assincrona pois pode demorar e nao pode travar o app
                @Override
                public void onFailure(Call<Deputados> call, Throwable t) {
                    // metodo caso falhe a requisição
                    System.err.println("ERRO na chamada, exceção: " + t.toString());
                }

                @Override
                public void onResponse(Call<Deputados> call, Response<Deputados> rspns) {
                    try {
                        //System.out.println("onresponse");
                        Deputados aux = rspns.body();
                        //Log.i("DEP", "links" + aux.getLinks().toString());
                        addDeputado(aux);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
