package thiaires.tagpol.Interface;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import thiaires.tagpol.ClienteWsCamara.InicializadorRetrofit;
import thiaires.tagpol.Controle.DeputadoAdapter;
import thiaires.tagpol.Modelo.Deputados;
import thiaires.tagpol.R;

public class principal extends Activity {

    private RecyclerView recycler;
    private DeputadoAdapter deputadoAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private int col;
    private int pagina=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        Configuration c = getResources().getConfiguration();
        if(c.orientation == Configuration.ORIENTATION_LANDSCAPE) col = 3;
        else col = 2;
        pagina = 1;
    }

    @Override
    protected void onStart() {
        super.onStart();

        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(!recyclerView.canScrollVertically(1)) {
                    //nao consegue descer mais carrega mais dados
                    System.out.println("can scroll verticaly 1");
                    //carregaDados(adapter);
                }
            }
        });

        layoutManager = new GridLayoutManager(principal.this, col, GridLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(layoutManager);
        deputadoAdapter = new DeputadoAdapter(principal.this, new Deputados());
        recycler.setAdapter(deputadoAdapter);
        carregaDados(deputadoAdapter);
    }

    private void carregaDados(DeputadoAdapter a) {
        //System.out.println("carregaDados");
        //if(pagina <= 6) {
           // (new ClienteCamara(a)).execute(pagina);
           // pagina++;
        //}
        (new ClienteCamara(a)).execute(1);
        (new ClienteCamara(a)).execute(2);
        (new ClienteCamara(a)).execute(3);
        (new ClienteCamara(a)).execute(4);
        (new ClienteCamara(a)).execute(5);
        (new ClienteCamara(a)).execute(6);
        a.organiza();
    }

    public static class ClienteCamara extends AsyncTask<Integer, Deputados, Deputados>{
        private DeputadoAdapter deputadoAdapter;


        public ClienteCamara(DeputadoAdapter c){
            this.deputadoAdapter = c;
        }

        @Override
        protected Deputados doInBackground(Integer... integers) {
            System.out.println("doInBackground");
            final Deputados resposta = new Deputados();
            try {
                final Call<Deputados> call = new InicializadorRetrofit().getCamaraService().getDeputados(integers[0]); //cria uma requisição
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
                            for(Deputados.Dado d : aux.getDados()){
                                resposta.getDados().add(d);
                                //System.err.println(" d" + d.toString());
                            }
                            //System.out.println("onresponse " + aux.toString());
                            publishProgress(resposta);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resposta;
        }

        @Override
        protected void onProgressUpdate(Deputados... values) {
            deputadoAdapter.setDeputados(values[0]);
        }
    }
}