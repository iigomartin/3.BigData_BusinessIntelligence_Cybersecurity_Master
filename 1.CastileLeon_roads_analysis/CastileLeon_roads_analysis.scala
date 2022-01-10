import org.apache.spark.{SparkConf, SparkContext}

object Practica1InigoMartinMelero {
  def main(args: Array[String]) {
    //Primera practica de Spark.
    //En mi caso, lo estoy haciendo con Intellij. Me costo un poco integrarlo, pero al final lo consegui
    //y me ha funcionado.
    //Vengo de industriales, asiq mi codigo no sera lo mas eficiente del mundo, pero voy a intentar hacerlo
    //entendible y muy paso a paso.

    //Creamos contexto y variables para que funcione.
    val conf = new SparkConf().setAppName("Simple Application").setMaster(master="local[*]")
    val sc = new SparkContext(conf)

    //Leemos los ficheros de texto, separando cada entrada en BBDD por la coma y convirtiendo el Array
    //resultante a una Lista. De momento, no convertimos campos numericos a Double.
    //Quitamos los duplicados con distinct, como se ha recomendado

    val provincia1=sc.textFile("provincias.txt").map(x=>x.split(",").toList).distinct().persist()
    val municipio1=sc.textFile("municipios.txt").map(x=>x.split(",").toList).distinct().persist()
    val puente1=sc.textFile("puentes.txt").map(x=>x.split(",").toList).distinct().persist()
    val tramo1=sc.textFile("tramos.txt").map(x=>x.split(",").toList).distinct().persist()

    //1. Numero de municipios por provincia

    //Cambio orden RDD municipio a (id, nombre mun.) y hago reduceByKey

    println("Ejercicio 1")
    val resEjercicio1=municipio1.map(x=>(x(1),1)).reduceByKey((x,y)=>x+y)
    resEjercicio1.foreach(println)

    //2. Kilometraje total provincia

    //Mapeamos provincia1 a (id, nombre) y tramo1 a (id, kilometros)
    //Hacemos el join, resultando en (id, (nombre,kilometros)).
    //Nos quedamos con (nombre, kilometros) solo, y realizamos reduceByKey tomando ahora los nombres
    //de las provincias como si fueran los id.

    println("Ejercicio 2")
    val provincia2=provincia1.map(x=>(x(0),x(1))).persist()
    val tramo2=tramo1.map(x=>(x(0),x(3).toDouble))
    val resEjercicio2=provincia2.join(tramo2).map(x=>x._2).reduceByKey((x,y)=>x+y).persist()
    resEjercicio2.foreach(println)

    //3. Numero puentes por provincia

    ///Mapeamos municipio1 a (nombre, id) y puente1 a (nombre, 1)
    //Realizamos el join por el nombre de los pueblos, dando (nombre, (id, 1)). Nos quedamos con
    //(id, 1) y con reduceByKey sumamos los 1 siendo nuestra key el id de la provincia.
    //Lo persistimos porque luego lo usamos.

    println("Ejercicio 3")
    val municipio2=municipio1.map(x=>(x(0),x(1)))
    val puente2 = puente1.map(x=>(x(0),1))
    val resEjercicio3 = municipio2.join(puente2).map(x=>x._2).reduceByKey((x,y)=>x+y).persist()
    resEjercicio3.foreach(println)


    //4. Carretera con mayor numero de puentes, por provincia

    //Mapeamos puente1 como (nombre, carretera).
    //Unimos nombres de puentes con municipios, resultando en (nombre, (carretera, provincia))
    //Mapeamos a ((carretera, provincia), 1), y usamos reduceByKey para contar los pares (carretera, provincia),
    //resultando en ((carretera, provincia), n). Mapeamos a (provincia, (carretera, n)) y usamos reduceByKey con el
    //id de la provincia, para que en cada operación se compare n. Si n(x) > n(y), se coge x, y sino y. Volvemos a mapear
    //para cambiar el formato.

    println("Ejercicio 4")
    val puente3=puente1.map(x=>(x(0),x(1)))
    val resEjercicio4=puente3.join(municipio2).map(x=>(x._2,1)).reduceByKey((x,y)=>x+y).map(x=>(x._1._2,(x._1._1,x._2))).reduceByKey((x,y)=>{if (x._2<y._2){(y._1,y._2)}else{(x._1,x._2)}}).map(x=>(x._1,x._2._1,x._2._2))
    resEjercicio4.foreach(println)

    //5. Obtener promedio de puentes por carretera por cada provincia

    //El numero de puentes por provincia lo conozco, es el apartado 3.
    //Mapeamos tramo1 para saber (id, nombre carretera). Le pasamos un distinct para que cada carretera se cuente
    //solo 1 vez, independientemente del nº de tramos.
    //Mapeamos a (id, nombre carretera, 1) y usamos reduceByKey para contar carreteras por provincia.
    //Finalmente, hacemos join de las carreteras y los puentes, obteniendo (id, (n carreteras, n puentes)).
    //Con un ultimo map, hacemos la division n puentes / n carreteras, y ponemos el id delante (id, division)

    println("Ejercicio 5")
    val tramo3=tramo1.map(x=>(x(0),x(1))).distinct()
    val numcarreteras= tramo3.map(x=>(x._1,1)).reduceByKey((x,y)=>x+y)
    val resEjercicio5= numcarreteras.join(resEjercicio3).map(x=>(x._1,x._2._2.toDouble/x._2._1.toDouble))
    resEjercicio5.foreach(println)
    resEjercicio3.unpersist()

    //6. Obtener anchura media de las carreteras por cada provincia

    //Mapeamos tramo1 como (id, (anchura,1))
    //Para hacer la media, llevaremos 2 contadores: uno que sume todas las anchuras y otro que sume el numero
    //de anchuras que hemos contado. Esto lo vemos en el reduceByKey, donde se suman todas las anchuras de cada
    //id de provincia y a su vez sube el contador.
    //Se hace join con provincia2 para obtener el nombre de la provincia, y realizamos la division entre la anchura
    //y el contador.

    println("Ejercicio 6")
    val tramo4=tramo1.map(x=>(x(0),(x(6).toDouble,1)))
    val anchuramedia=tramo4.reduceByKey((x,y)=>(x._1+y._1,x._2+y._2))
    val resEjercicio6=provincia2.join(anchuramedia).map(x=>(x._2._1,x._2._2._1/x._2._2._2))
    resEjercicio6.foreach(println)
    provincia2.unpersist()

    //7. Obtener promedio de kilometros por habitante

    //Tenemos del Ejercicio 2 los kilometros totales por provincia, solo queda obtener los habitantes
    //por provincia. Lo guardamos en provincia3 (nombre provincia, habitantes) y usamos como clave del join
    //al nombre de la provincia, en vez del id, ya que el resultado del Ejercicio 2 trabaja con el nombre de la
    //provincia y no su id. Hacemos la division.
    println("Ejercicio 7")
    val provincia3=provincia1.map(x=>(x(1),x(2).toDouble))
    val resEjercicio7= provincia3.join(resEjercicio2).map(x=>(x._1,x._2._2/x._2._1))
    resEjercicio7.foreach(println)
    resEjercicio2.unpersist()

    provincia1.unpersist()
    puente1.unpersist()
    tramo1.unpersist()
    municipio1.unpersist()
    //Finalizamos el ejercicio.
  }
}
