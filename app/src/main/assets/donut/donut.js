var svg = d3.select("body")
  .append("svg")
  .append("g")

svg.append("g")
  .attr("class", "slices");
svg.append("g")
  .attr("class", "labels");
svg.append("g")
  .attr("class", "lines");

var width = 720,
    height = 480,
  radius = Math.min(width, height) / 2;

var pie = d3.layout.pie()
  .sort(null)
  .value(function(d) {
    return d.value;
  });

var arc = d3.svg.arc()
  .outerRadius(radius * 0.8)
  .innerRadius(radius * 0.4);

var outerArc = d3.svg.arc()
  .innerRadius(radius * 0.9)
  .outerRadius(radius * 0.9);

svg.attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");

var key = function(d){ return d.data.categoria; };

var color = d3.scale.ordinal()
.domain(['Assinatura de publicações', 'Combustíveis e Lubrificantes', 'Emissão Bilhete Aéreo',
                   'Fornecimento de Alimentação do Parlamentar', 'Locação ou Fretamento de Veículos Automotores',
                   'Passagens Aéreas', 'Serviço de Táxi, Pedágio e Estacionamento', 'Serviços Postais',
                   'Telefonia'])
.range(["#b2ebf2", "#80deea", "#4dd0e1", "#42A5F5", "#26c6da", "#00bcd4", "#00acc1", "#0097a7", "#00838f"]);

function carregaDados(data){

  /*var data = Json.parse(Android.loadData());
  var labels = color.domain();
  return labels.map(function(categoria){
    return data
  });
*/
  var json = Json.parse(Android.loadData(data));
  document.getElementById("a").innerHTML = json;
  var labels = color.domain();
    var res = labels.map(function(categoria){
      return { categoria: json.categoria, value: json.value }
    });

    return res;


    //var labels = Android.loadData(data);
    //return labels;
}

//var now = new Date;
//var aux = now.getFullYear() + "-" + now.getMonth();
//change(carregaDados(aux);


d3.select(".mes")
  .on("change", function(){
    var date = document.getElementById("mes").value;
    var dados = Android.loadData(date);
    //document.getElementById("a").innerHTML = dados;
    change(carregaDados(date));
  });


function change(data) {

  /* ------- PIE SLICES -------*/
  var slice = svg.select(".slices").selectAll("path.slice")
    .data(pie(data), key);

  slice.enter()
    .insert("path")
    .style("fill", function(d) { return color(d.data.categoria); })
    .attr("class", "slice");

  slice
    .transition().duration(1000)
    .attrTween("d", function(d) {
      this._current = this._current || d;
      var interpolate = d3.interpolate(this._current, d);
      this._current = interpolate(0);
      return function(t) {
        return arc(interpolate(t));
      };
    })

  slice.exit()
    .remove();

  /* ------- TEXT LABELS -------*/

  var text = svg.select(".labels").selectAll("text")
    .data(pie(data), key);

  text.enter()
    .append("text")
    .attr("dy", ".35em")
    .text(function(d) {
      return d.data.categoria;
    });

  function midAngle(d){
    return d.startAngle + (d.endAngle - d.startAngle)/2;
  }

  text.transition().duration(1000)
    .attrTween("transform", function(d) {
      this._current = this._current || d;
      var interpolate = d3.interpolate(this._current, d);
      this._current = interpolate(0);
      return function(t) {
        var d2 = interpolate(t);
        var pos = outerArc.centroid(d2);
        pos[0] = radius * (midAngle(d2) < Math.PI ? 1 : -1);
        return "translate("+ pos +")";
      };
    })
    .styleTween("text-anchor", function(d){
      this._current = this._current || d;
      var interpolate = d3.interpolate(this._current, d);
      this._current = interpolate(0);
      return function(t) {
        var d2 = interpolate(t);
        return midAngle(d2) < Math.PI ? "start":"end";
      };
    });

  text.exit()
    .remove();

  /* ------- SLICE TO TEXT POLYLINES -------*/

  var polyline = svg.select(".lines").selectAll("polyline")
    .data(pie(data), key);

  polyline.enter()
    .append("polyline");

  polyline.transition().duration(1000)
    .attrTween("points", function(d){
      this._current = this._current || d;
      var interpolate = d3.interpolate(this._current, d);
      this._current = interpolate(0);
      return function(t) {
        var d2 = interpolate(t);
        var pos = outerArc.centroid(d2);
        pos[0] = radius * 0.95 * (midAngle(d2) < Math.PI ? 1 : -1);
        return [arc.centroid(d2), outerArc.centroid(d2), pos];
      };
    });

  polyline.exit()
    .remove();
};
