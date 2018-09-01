package thiaires.tagpol.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import thiaires.tagpol.ClienteWsCamara.InicializadorRetrofit;
import thiaires.tagpol.Modelo.Deputado;
import thiaires.tagpol.R;

public class DetalhesDeputado extends AppCompatActivity {

    private Deputado deputado;
    private ImageView imgDeputado;
    private TextView nomeDeputado;
    private TextView partidoDeputado;
    private TextView estadoDeputado;
    private TextView telefoneGabinete;
    private TextView emailGabinete;
    private TextView condicaoEleitoral;
    private ImageButton bubble;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhes_deputado);
        Bundle b = getIntent().getExtras();

        deputado = (Deputado) b.getSerializable("dep");
        imgDeputado = (ImageView) findViewById(R.id.imgDeputado);
        nomeDeputado = (TextView) findViewById(R.id.nomeDeputado);
        partidoDeputado = (TextView) findViewById(R.id.partidoDeputado);
        estadoDeputado = (TextView) findViewById(R.id.estadoDeputado);
        telefoneGabinete = (TextView) findViewById(R.id.telefoneGabinete);
        emailGabinete = (TextView) findViewById(R.id.emailGabinete);
        condicaoEleitoral = (TextView) findViewById(R.id.condicaoEleitoral);
        bubble = (ImageButton) findViewById(R.id.bubbleVisualization);
        /*
        bubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetalhesDeputado.this, visualisacoes.class);
                intent.putExtra("dados", "dados");
                intent.putExtra("cdVisu", 0);
                startActivity(intent);
            }
        });
         */
        //deputado = new Deputado();
        atualizaTela();
    }

    private void carregaDados(int idDep) {
        System.out.println(idDep);
        try {
            final Call<Deputado> call = new InicializadorRetrofit().getCamaraService().getDeputado(idDep); //cria uma requisição
            call.enqueue(new Callback<Deputado>() { // enqueue significa uma requisição assincrona pois pode demorar e nao pode travar o app
                @Override
                public void onFailure(Call<Deputado> call, Throwable t) {
                    // metodo caso falhe a requisição
                    System.err.println("ERRO na chamada, exceção: " + t.toString());
                }

                @Override
                public void onResponse(Call<Deputado> call, Response<Deputado> rspns) {
                    try {
                        System.out.println("onRespense");
                        deputado = rspns.body();
                        System.out.println(deputado.toString());
                        atualizaTela();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void atualizaTela() {
        Picasso.with(this).load(deputado.getDados().getUltimoStatus().getUrlFoto()).into(imgDeputado);
        nomeDeputado.setText(deputado.getDados().getNomeCivil());
        partidoDeputado.setText(deputado.getDados().getUltimoStatus().getSiglaPartido());
        estadoDeputado.setText(deputado.getDados().getUltimoStatus().getSiglaUf());
        telefoneGabinete.setText(deputado.getDados().getUltimoStatus().getGabinete().getTelefone());
        emailGabinete.setText(deputado.getDados().getUltimoStatus().getGabinete().getEmail());
        condicaoEleitoral.setText(deputado.getDados().getUltimoStatus().getCondicaoEleitoral());
    }
}
