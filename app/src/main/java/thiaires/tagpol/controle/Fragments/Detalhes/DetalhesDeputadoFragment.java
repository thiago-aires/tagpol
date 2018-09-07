package thiaires.tagpol.controle.Fragments.Detalhes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import thiaires.tagpol.Modelo.Deputado;
import thiaires.tagpol.Modelo.Despesas;
import thiaires.tagpol.R;

public class DetalhesDeputadoFragment extends Fragment {
    private Deputado deputado;
    private SimpleDraweeView imgDeputado;
    private TextView nomeDeputado;
    private TextView partidoDeputado;
    private TextView estadoDeputado;
    private TextView telefoneGabinete;
    private TextView emailGabinete;
    private TextView condicaoEleitoral;
    private TextView cpfDeputado;
    private TextView dataNascimento;
    private TextView ufNascimento;
    private TextView municipioNascimento;
    private TextView escolaridade;
    private TextView site;
    private TextView redesocial;
    private TextView situacao;
    private TextView descricaoStatus;
    private Despesas despesas;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.detalhes_deputado_fragment, container, false);
        context = getContext();

        imgDeputado = (SimpleDraweeView) v.findViewById(R.id.imgDeputado);
        nomeDeputado = (TextView) v.findViewById(R.id.nomeDeputado);
        partidoDeputado = (TextView) v.findViewById(R.id.partidoDeputado);
        estadoDeputado = (TextView) v.findViewById(R.id.estadoDeputado);
        telefoneGabinete = (TextView) v.findViewById(R.id.telefoneGabinete);
        emailGabinete = (TextView) v.findViewById(R.id.emailGabinete);
        condicaoEleitoral = (TextView) v.findViewById(R.id.condicaoEleitoral);
        cpfDeputado = (TextView) v.findViewById(R.id.cpfDeputado);
        dataNascimento = (TextView) v.findViewById(R.id.dataNascimentoDeputado);
        ufNascimento= (TextView) v.findViewById(R.id.ufNascimentoDeputado);
        municipioNascimento= (TextView) v.findViewById(R.id.municipioNascimentoDeputado);
        escolaridade= (TextView) v.findViewById(R.id.escolaridadeDeputado);
        site= (TextView) v.findViewById(R.id.siteDeputado);
        redesocial= (TextView) v.findViewById(R.id.redeSocialDeputado);
        situacao= (TextView) v.findViewById(R.id.situacaoDeputado);
        descricaoStatus= (TextView) v.findViewById(R.id.descricaoStatus);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle b = getArguments();

        if (b != null) {
            deputado = (Deputado) b.getSerializable("dep");
        } else {
            Toast.makeText(context, "OPS, ocorreu algum problema!!", Toast.LENGTH_LONG).show();
            deputado = new Deputado();
        }
        atualizaTela();
    }

    private void atualizaTela() {
        imgDeputado.setImageURI(deputado.getDados().getUltimoStatus().getUrlFoto());
        nomeDeputado.setText(deputado.getDados().getNomeCivil());
        partidoDeputado.setText(deputado.getDados().getUltimoStatus().getSiglaPartido());
        estadoDeputado.setText(deputado.getDados().getUltimoStatus().getSiglaUf());
        telefoneGabinete.setText(deputado.getDados().getUltimoStatus().getGabinete().getTelefone());
        emailGabinete.setText(deputado.getDados().getUltimoStatus().getGabinete().getEmail());
        condicaoEleitoral.setText(deputado.getDados().getUltimoStatus().getCondicaoEleitoral());
        cpfDeputado.setText(deputado.getDados().getCpf());
        dataNascimento.setText(deputado.getDados().getDataNascimento());
        ufNascimento.setText(deputado.getDados().getUfNascimento());
        municipioNascimento.setText(deputado.getDados().getMunicipioNascimento());
        escolaridade.setText(deputado.getDados().getEscolaridade());
        site.setText(deputado.getDados().getUrlWebsite());
        redesocial.setText(deputado.getDados().getRedeSocial().toString());
        situacao.setText(deputado.getDados().getUltimoStatus().getSituacao());
        descricaoStatus.setText(deputado.getDados().getUltimoStatus().getDescricaoStatus());
    }
}