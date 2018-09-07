package thiaires.tagpol.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import thiaires.tagpol.R;

public class visualisacoes extends AppCompatActivity {
    private WebView webView;
    private String dados;
    private int cdVisu;

    public visualisacoes(){
        this.dados = "[\n" +
                "  {\n" +
                "    \"nome\": \"ASSINATURA DE PUBLICAÇÕES\",\n" +
                "    \"tamanho\": 129.9\n" +
                "    \"valor\": 129,9\n" +
                "  },\n" +
                "  {\n" +
                "    \"nome\": \"COMBUSTÍVEIS E LUBRIFICANTES.\",\n" +
                "    \"tamanho\": 1011.35\n" +
                "    \"valor\": 1.011,35\n" +
                "  },\n" +
                "  {\n" +
                "    \"nome\": \"Emissão Bilhete Aéreo\",\n" +
                "    \"tamanho\": 10517.26\n" +
                "    \"valor\": 10.517,26\n" +
                "  },\n" +
                "  {\n" +
                "    \"nome\": \"FORNECIMENTO DE ALIMENTAÇÃO DO PARLAMENTAR\",\n" +
                "    \"tamanho\": 23.24\n" +
                "    \"valor\": 23,24\n" +
                "  },\n" +
                "  {\n" +
                "    \"nome\": \"LOCAÇÃO OU FRETAMENTO DE VEÍCULOS AUTOMOTORES\",\n" +
                "    \"tamanho\": 7600\n" +
                "    \"valor\": 7,600\n" +
                "  },\n" +
                "  {\n" +
                "    \"nome\": \"PASSAGENS AÉREAS\",\n" +
                "    \"tamanho\": 1580.17\n" +
                "    \"valor\": 1.580,17\n" +
                "  },\n" +
                "  {\n" +
                "    \"nome\": \"SERVIÇO DE TÁXI, PEDÁGIO E ESTACIONAMENTO\",\n" +
                "    \"tamanho\": 84\n" +
                "    \"valor\": 84\n" +
                "  },\n" +
                "  {\n" +
                "    \"nome\": \"SERVIÇOS POSTAIS\",\n" +
                "    \"tamanho\": 1175.23\n" +
                "    \"valor\": 1.175,23\n" +
                "  },\n" +
                "  {\n" +
                "    \"nome\": \"TELEFONIA\",\n" +
                "    \"tamanho\": 2342.25\n" +
                "    \"valor\": 2.342,25\n" +
                "  }\n" +
                "]";
    }


    public class WebAppInterface {
        private Context context;

        public WebAppInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public String loadData() {
            return dados;
        }
    }

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onResume() {
        super.onResume();

        final WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setAllowFileAccess(true);
        ws.setDomStorageEnabled(true);
        ws.setAllowContentAccess(true);
        ws.setAllowFileAccessFromFileURLs(true);
        ws.setAllowUniversalAccessFromFileURLs(true);
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface( new WebAppInterface( this ), "Android");


        switch (cdVisu){
            case 0:
                webView.loadUrl("file:///android_asset/bubble.html");
                //webView.loadDataWithBaseURL("", bubbleVisualization(), "text/html", "UTF-8", "");
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualisacoes);
        webView = (WebView) findViewById(R.id.webView);
        //dados = this.getIntent().getStringExtra("dados");

        cdVisu = this.getIntent().getIntExtra("cdVisu", 0);
    }

    private String bubbleVisualization(){
        return " \n" +
                "<html>\n" +
                "<header><title>This is title</title></header>\n" +
                "<body>\n" +
                "Hello world\n" +
                "</body>\n" +
                "</html>\n";
    }


}
