package thiaires.tagpol.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import thiaires.tagpol.ClienteWsCamara.InicializadorRetrofit;
import thiaires.tagpol.Modelo.Deputado;
import thiaires.tagpol.Modelo.Deputados;
import thiaires.tagpol.R;
import thiaires.tagpol.View.DetalhesDeputado;


public class DeputadoAdapter extends RecyclerView.Adapter<DeputadoAdapter.cardViewHolder> {
    private Context mContext;
    private Deputados deputados;
    private LayoutInflater mLayoutInflater;
    private Deputado depExpandido;


    public Deputados getDeputados() {
        return deputados;
    }

    private void setDeputadoExpandido(Deputado d){
        this.depExpandido = d;
    }

    public void setDeputados(Deputados deputados){
        if(deputados.equals(null))
            return;

        for(Deputados.Dado d : deputados.getDados()){
            this.deputados.getDados().add(d);
        }

        organiza();
    }

    public void organiza(){
        Collections.sort(this.deputados.getDados(), new Comparator<Deputados.Dado>() {
            @Override
            public int compare(Deputados.Dado deputado, Deputados.Dado t1) {
                return deputado.getNome().compareToIgnoreCase(t1.getNome());
            }
        });
        notifyDataSetChanged();
    }

    public DeputadoAdapter(Context mContext, Deputados deputados) {
        this.mContext = mContext;
        this.deputados = deputados;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.depExpandido = new Deputado();
    }

    @Override
    public cardViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.card_deputado, viewGroup, false);
        cardViewHolder mvh = new cardViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(cardViewHolder holder, final int position) {
        Deputados.Dado d = this.deputados.getDados().get(position);
        String[] nome = d.getNome().split("\\s");
        System.out.println(Arrays.toString(nome));
        String aux;
        if(nome.length > 1)
            if(nome[1].toUpperCase().equals("DE") ||
                    nome[1].toUpperCase().equals("DO") ||
                    nome[1].toUpperCase().equals("DA"))
                aux = nome[0] + " " + nome[1] + "\n" + nome[2];
            else aux = nome[0] + "\n" + nome[1];
        else
            aux = nome[0];
        Picasso.with(mContext).load(d.getUrlFoto()).into(holder.imageDeputado);
        holder.nomeDeputado.setText(aux);
        holder.partidoDeputado.setText(d.getSiglaPartido());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Deputados.Dado a = deputados.getDados().get(position);
                //(new ClienteCamara(DeputadoAdapter.this)).execute(a.getId());

                try {
                    final Call<Deputado> call = new InicializadorRetrofit().getCamaraService().getDeputado(a.getId()); //cria uma requisição
                    call.enqueue(new Callback<Deputado>() { // enqueue significa uma requisição assincrona pois pode demorar e nao pode travar o app
                        @Override
                        public void onFailure(Call<Deputado> call, Throwable t) {
                            // metodo caso falhe a requisição
                            System.err.println("ERRO na chamada, exceção: " + t.toString());
                            Toast.makeText(mContext, "Servidor da Câmara dos Deputados indisponível", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onResponse(Call<Deputado> call, Response<Deputado> rspns) {
                            try {
                                Deputado da = rspns.body();
                                System.out.println("da.tostring " + da.toString());
                                Intent intent = new Intent(mContext, DetalhesDeputado.class);
                                intent.putExtra("dep", da);
                                mContext.startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return this.deputados.getDados().size();
    }


    public class cardViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageDeputado;
        public TextView nomeDeputado;
        public TextView partidoDeputado;
        public CardView cardView;

        public cardViewHolder(View itemView) {
            super(itemView);
            imageDeputado = (ImageView) itemView.findViewById(R.id.imgDeputado);
            nomeDeputado = (TextView) itemView.findViewById(R.id.nomeDeputado);
            partidoDeputado = (TextView) itemView.findViewById(R.id.partidoDeputado);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
        }
    }


    public static class ClienteCamara extends AsyncTask<Integer, Deputado, Void> {
        private DeputadoAdapter deputadoAdapter;


        public ClienteCamara(DeputadoAdapter c){
            this.deputadoAdapter = c;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            System.out.println("id " + integers[0]);
            try {
                final Call<Deputado> call = new InicializadorRetrofit().getCamaraService().getDeputado(integers[0]); //cria uma requisição
                call.enqueue(new Callback<Deputado>() { // enqueue significa uma requisição assincrona pois pode demorar e nao pode travar o app
                    @Override
                    public void onFailure(Call<Deputado> call, Throwable t) {
                        // metodo caso falhe a requisição
                        System.err.println("ERRO na chamada, exceção: " + t.toString());
                        publishProgress(new Deputado());
                    }

                    @Override
                    public void onResponse(Call<Deputado> call, Response<Deputado> rspns) {
                        try {
                            Deputado da = rspns.body();
                            System.out.println("da.tostring " + da.toString());
                            publishProgress(da);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Deputado... values) {
            deputadoAdapter.setDeputadoExpandido(values[0]);
        }
    }
}
