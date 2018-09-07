package thiaires.tagpol.controle.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import thiaires.tagpol.ClienteWsCamara.InicializadorRetrofit;
import thiaires.tagpol.Modelo.Deputado;
import thiaires.tagpol.Modelo.Deputados;
import thiaires.tagpol.R;
import thiaires.tagpol.View.DetalhesDeputado;


public class DeputadoAdapter extends RecyclerView.Adapter<DeputadoAdapter.cardViewHolder>{
    private Context context;
    private Deputados deputados;
    private Deputados lstDeputadosFiltrada;
    private LayoutInflater mLayoutInflater;

    public void setContext(Context context){
        this.context = context;
    }

    public Deputados getDeputados() {
        return deputados;
    }

    public void setDeputados(Deputados deputados){
        this.deputados = deputados;
    }


    public DeputadoAdapter(Context mContext, Deputados deputados) {
        this.context = mContext;
        this.deputados = deputados;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        for(int i=0;i<nome.length;i++)
            nome[i] = nome[i].substring(0,1).toUpperCase().concat(nome[i].substring(1).toLowerCase());
        String aux;
        if(nome.length > 1)
            if(nome[1].toUpperCase().equals("DE") ||
                    nome[1].toUpperCase().equals("DO") ||
                    nome[1].toUpperCase().equals("DA"))
                aux = nome[0] + " " + nome[1] + "\n" + nome[2];
            else aux = nome[0] + "\n" + nome[1];
        else
            aux = nome[0];

        holder.img.setImageURI(d.getUrlFoto());
        holder.nomeDeputado.setText(aux);
        holder.partidoDeputado.setText(d.getSiglaPartido());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Deputados.Dado a = deputados.getDados().get(position);

                try {
                    final Call<Deputado> call = new InicializadorRetrofit().getCamaraService().getDeputado(a.getId()); //cria uma requisição
                    call.enqueue(new Callback<Deputado>() { // enqueue significa uma requisição assincrona pois pode demorar e nao pode travar o app
                        @Override
                        public void onFailure(Call<Deputado> call, Throwable t) {
                            // metodo caso falhe a requisição
                            System.err.println("ERRO na chamada, exceção: " + t.toString());
                            Toast.makeText(context, "Servidor da Câmara dos Deputados indisponível", Toast.LENGTH_LONG).show();
                        }

                        @SuppressLint("ResourceType")
                        @Override
                        public void onResponse(Call<Deputado> call, Response<Deputado> rspns) {
                            try {
                                Deputado da = rspns.body();
                                //System.out.println("da.tostring " + da.toString());
                                Bundle extras = new Bundle();
                                extras.putSerializable("dep", da);
                                Intent i = new Intent(context, DetalhesDeputado.class);
                                i.putExtras(extras);
                                context.startActivity(i);
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

    private String formataNome(String nome) {
        String[] n = nome.split("\\s");
        String res = "";

        for(String aux : n){
            char[] c = (aux.toLowerCase()).toCharArray();
            c[0] = (char) (c[0] + 32);
            res+= c.toString() + " ";
        }

        return res;
    }


    @Override
    public int getItemCount() {
        return this.deputados.getDados().size();
    }


    public class cardViewHolder extends RecyclerView.ViewHolder {
        public TextView nomeDeputado;
        public TextView partidoDeputado;
        public CardView cardView;
        public SimpleDraweeView img;

        public cardViewHolder(View itemView) {
            super(itemView);
            nomeDeputado = (TextView) itemView.findViewById(R.id.nomeDeputado);
            partidoDeputado = (TextView) itemView.findViewById(R.id.partidoDeputado);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            img = (SimpleDraweeView) itemView.findViewById(R.id.imgDeputadoFresco);
        }
    }
}
