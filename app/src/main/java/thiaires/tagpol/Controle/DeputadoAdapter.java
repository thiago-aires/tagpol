package thiaires.tagpol.Controle;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        for(Deputados.Dado d : deputados.getDados()){
            this.deputados.getDados().add(d);
        }
        Collections.sort(this.deputados.getDados(), new Comparator<Deputados.Dado>() {
            @Override
            public int compare(Deputados.Dado deputado, Deputados.Dado t1) {
                return deputado.getNome().compareToIgnoreCase(t1.getNome());
            }
        });

        System.out.println("COUnt " + getItemCount());
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
    public void onBindViewHolder(cardViewHolder holder, int position) {
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

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Deputados.Dado a = deputados.getDados().get(0);
                    (new ClienteCamara(DeputadoAdapter.this)).execute("178957");
                    System.out.println(depExpandido.toString());
                }
            });
        }
    }

    public static class ClienteCamara extends AsyncTask<String, Deputado, Void> {
        private DeputadoAdapter deputadoAdapter;


        public ClienteCamara(DeputadoAdapter c){
            this.deputadoAdapter = c;
        }

        @Override
        protected Void doInBackground(String... strings) {
            System.out.println("id " + strings[0]);
            try {
                final Call<Deputado> call = new InicializadorRetrofit().getCamaraService().getDeputado(strings[0]); //cria uma requisição
                call.enqueue(new Callback<Deputado>() { // enqueue significa uma requisição assincrona pois pode demorar e nao pode travar o app
                    @Override
                    public void onFailure(Call<Deputado> call, Throwable t) {
                        // metodo caso falhe a requisição
                        System.err.println("ERRO na chamada, exceção: " + t.toString());
                        publishProgress(null);
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
