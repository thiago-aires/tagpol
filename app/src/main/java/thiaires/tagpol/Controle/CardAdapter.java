package thiaires.tagpol.Controle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import thiaires.tagpol.Modelo.Deputado;
import thiaires.tagpol.R;


public class CardAdapter extends RecyclerView.Adapter<CardAdapter.cardViewHolder> {
    private Context mContext;
    private ArrayList<Deputado> deputados;
    private LayoutInflater mLayoutInflater;

    public void setDeputados(ArrayList<Deputado> deputados){
        for(Deputado d : deputados){
            this.deputados.add(d);
        }
        Collections.sort(this.deputados, new Comparator<Deputado>() {
            @Override
            public int compare(Deputado deputado, Deputado t1) {
                return deputado.getNome().compareToIgnoreCase(t1.getNome());
            }
        });

        System.out.println("COUnt " + getItemCount());
    }

    public CardAdapter(Context mContext, ArrayList<Deputado> deputados) {
        this.mContext = mContext;
        this.deputados = deputados;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public cardViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.card_deputado_constraint, viewGroup, false);
        cardViewHolder mvh = new cardViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(cardViewHolder holder, int position) {
        Deputado d = this.deputados.get(position);
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
        return this.deputados.size();
    }


    public class cardViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageDeputado;
        public TextView nomeDeputado;
        public TextView partidoDeputado;

        public cardViewHolder(View itemView) {
            super(itemView);
            imageDeputado = (ImageView) itemView.findViewById(R.id.imgDeputado);
            nomeDeputado = (TextView) itemView.findViewById(R.id.nomeDeputado);
            partidoDeputado = (TextView) itemView.findViewById(R.id.partidoDeputado);
        }
    }
}
