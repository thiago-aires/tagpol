package thiaires.tagpol.Modelo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author thiaires
 */
public class Deputado {
    private String id,
                    uri,
                    nome,
                    siglaPartido,
                    uriPartido,
                    siglaUf,
                    idLegislatura,
                    urlFoto;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSiglaPartido() {
        return siglaPartido;
    }

    public void setSiglaPartido(String siglaPartido) {
        this.siglaPartido = siglaPartido;
    }

    public String getUriPartido() {
        return uriPartido;
    }

    public void setUriPartido(String uriPartido) {
        this.uriPartido = uriPartido;
    }

    public String getSiglaUf() {
        return siglaUf;
    }

    public void setSiglaUf(String siglaUf) {
        this.siglaUf = siglaUf;
    }

    public String getIdLegislatura() {
        return idLegislatura;
    }

    public void setIdLegislatura(String idLegislatura) {
        this.idLegislatura = idLegislatura;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    @Override
    public String toString() {
        return "Deputado{" + "id=" + id + ", uri=" + uri + ", nome=" + nome + ", siglaPartido=" + siglaPartido + ", uriPartido=" + uriPartido + ", siglaUf=" + siglaUf + ", idLegislatura=" + idLegislatura + ", urlFoto=" + urlFoto + '}';
    }
    
}
