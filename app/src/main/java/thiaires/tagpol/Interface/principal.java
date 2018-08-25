package thiaires.tagpol.Interface;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import thiaires.tagpol.ClienteWsCamara.InicializadorRetrofit;
import thiaires.tagpol.Controle.CardAdapter;
import thiaires.tagpol.Modelo.Deputado;
import thiaires.tagpol.Modelo.Deputados;
import thiaires.tagpol.R;

public class principal extends Activity {

    private RecyclerView recycler;
    private CardAdapter cardAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        recycler = (RecyclerView) findViewById(R.id.recycler);
    }

    @Override
    protected void onStart() {
        super.onStart();
        layoutManager = new GridLayoutManager(principal.this, 2, GridLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(layoutManager);
        cardAdapter = new CardAdapter(principal.this, new ArrayList<Deputado>());
        recycler.setAdapter(cardAdapter);
        carregaDados(cardAdapter);
    }

    private void carregaDados(CardAdapter cardAdapter) {
        (new ClienteCamara(cardAdapter)).execute(1);
        (new ClienteCamara(cardAdapter)).execute(2);
        (new ClienteCamara(cardAdapter)).execute(3);
        (new ClienteCamara(cardAdapter)).execute(4);
        (new ClienteCamara(cardAdapter)).execute(5);
        (new ClienteCamara(cardAdapter)).execute(6);
    }

    public static class ClienteCamara extends AsyncTask<Integer, ArrayList<Deputado>, ArrayList<Deputado>>{
        private CardAdapter cardAdapter;


        public ClienteCamara(CardAdapter c){
            this.cardAdapter = c;
        }


        @Override
        protected ArrayList<Deputado> doInBackground(Integer... integers) {
            final ArrayList<Deputado> resposta = new ArrayList<>();
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
                            ArrayList<Deputado> aux = rspns.body().getDados();
                            for(Deputado d : aux){
                                resposta.add(d);
                                //System.err.println(" d" + d.toString());
                            }
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
        protected void onProgressUpdate(ArrayList<Deputado>... values) {
            cardAdapter.setDeputados(values[0]);
            cardAdapter.notifyDataSetChanged();
        }
    }
}
