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
    int col;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        Configuration c = getResources().getConfiguration();
        if(c.orientation == Configuration.ORIENTATION_LANDSCAPE) col = 3;
        else col = 2;
    }

    @Override
    protected void onStart() {
        super.onStart();
        layoutManager = new GridLayoutManager(principal.this, col, GridLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(layoutManager);
        deputadoAdapter = new DeputadoAdapter(principal.this, new Deputados());
        recycler.setAdapter(deputadoAdapter);
        carregaDados(deputadoAdapter);
    }

    private void carregaDados(DeputadoAdapter a) {
        //System.out.println("carregaDados");
        (new ClienteCamara(a)).execute(1);
        (new ClienteCamara(a)).execute(2);
        (new ClienteCamara(a)).execute(3);
        (new ClienteCamara(a)).execute(4);
        (new ClienteCamara(a)).execute(5);
        (new ClienteCamara(a)).execute(6);
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
            deputadoAdapter.notifyDataSetChanged();
        }
    }
}