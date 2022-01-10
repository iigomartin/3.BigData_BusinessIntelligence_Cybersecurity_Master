import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.sql.functions._




case class InfraccionesAllStrings(date: String, time: String, agency: String, subAgency: String,
                        description: String, location: String, latitude: String, longitude: String, belts: String,
                        personal_injury: String, property_damage: String, fatal: String, commercial_license: String,
                        HAZMAT: String, commercial_vehicle: String, alcohol: String, work_zone: String, state: String,
                        vehicle_type: String, year: String, make: String, model: String, color: String,
                        violation_type: String, charge: String, article: String, contributed_to_accident: String,
                        driver_city: String, driver_state: String, DL_state:String, arrest_type: String)

case class InfraccionesCompletas(date: String, time: String, agency: String, subAgency: String,
                                  description: String, location: String, latitude: Double, longitude: Double, belts: String,
                                  personal_injury: String, property_damage: String, fatal: String, commercial_license: String,
                                  HAZMAT: String, commercial_vehicle: String, alcohol: String, work_zone: String, state: String,
                                  vehicle_type: String, year: Int, make: String, model: String, color: String,
                                  violation_type: String, charge: String, article: String, contributed_to_accident: String,
                                  driver_city: String, driver_state: String, DL_state:String, arrest_type: String, dia: Int, mes: Int, ano: Int, hora: Int, min: Int)

case class InfraccionesRecortadas(date: String, time: String, latitude: Double, longitude: Double, state: String,
                                 vehicle_type: String, color: String, violation_type: String, driver_city: String, driver_state: String)

object Practica2InigoMartinMelero {

  def mostViolationsCityPerState( a:Dataset[InfraccionesRecortadas], b:String ) : (String,Int) = {
    import org.apache.spark.sql.expressions.Window
    import org.apache.spark.sql.functions._
    val w3 = Window.partitionBy()
    val conductoresPorCiudad = a.filter(x=>x.driver_state==b).groupBy("driver_city").count().withColumn("maximo", max("count").over(w3))
    val ciudadMaxima= conductoresPorCiudad.filter(conductoresPorCiudad("count")===conductoresPorCiudad("maximo")).drop("count")
    val salida =ciudadMaxima.first()
    (salida.get(0).asInstanceOf[String],salida.get(1).asInstanceOf[Long].toInt)
  }

  def positionPerType( a:Dataset[InfraccionesRecortadas], b:Int ) : Dataset[_] = {
    import org.apache.spark.sql.functions._
    val conductoresPorPosiciones = a.filter(x=>x.date.split("/").last.toInt==b)
    val numentradas= conductoresPorPosiciones.groupBy("vehicle_type").agg(avg("longitude"),avg("latitude"))
      .sort(asc("vehicle_type"))
    numentradas
  }

  def main(args: Array[String]) {

    //Anadimos la creacion de la SparkSQL sesion. Despues de crearla, hay que importar session.implicits._
    val spark = SparkSession.builder
      .master("local[*]")
      .appName("Simple Application")
      .getOrCreate()
    import spark.implicits._

//0. Preprocesado de los datos

    println("0. Preprocesado de los datos")
    //Primero, extraemos los datos del csv. Configuramos para que se vea la cabecera, y mostramos los 4 primeros valores
    //para ver que haya funcionado.

    val datosDF1 =spark.read.format("csv").option("header", "true").load("infracciones.csv")
    datosDF1.collect()
    datosDF1.show(4)

    //Quitamos accident, geolocation, gender y race mediante drop, y volvemos a ensenar el DataFrame

    val datosDF2=datosDF1.drop("geolocation", "accident","race","gender")
    datosDF2.collect()
    datosDF2.show(4)

    //Gestionamos los valores null, dependiendo de la columna.
    //En general, para este ejercicio, solo voy a distinguir 2 tipos de datos: String y Numerico.
    //No voy a parchear las Fechas como Date ni los si / no como Boolean.

    val datosDF3=datosDF2.na.fill(0).na.fill(Map("date"->"","time"->"","agency"->"","subAgency"->"","description"->"",
                        "location"->"","latitude"->0, "longitude"->0,"belts"->"","personal_injury"->"","property_damage"->"","fatal"->"",
                        "commercial_license"->"","HAZMAT"->"","commercial_vehicle"->"","alcohol"->"","work_zone"->"",
                        "state"->"","vehicle_type"->"","year"->0,"make"->"","model"->"","color"->"", "violation_type"->"",
                        "charge"->"","article"->"","contributed_to_accident"->"","driver_city"->"","driver_state"->"",
                        "DL_state"->"","arrest_type"->""))
    datosDF3.collect()
    datosDF3.show(4)

    //Realizamos la conversion de tipos. Primero convertimos everything a una Dataset, de clase all Strings.
    //Luego, para evitarnos un map muy largo, creamos unas columnas extras con solo lo que queremos en formato numerico.
    //En este caso, los year en int y latitude y longitude en double.
    //Borramos las columnas con Strings y renombramos las que contienen los numericos.
    //En mi caso, tambien he preferido dividir las date y los time en dia, mes, ano, hora, minuto.
    //Se que esto se podria hacer desde el propio filter, cuando lo empleemos, por ejemplo filter(year(to_timestamp($"date","MM/dd/yy"))===2012).
    //pero creo que es mas repetitivo hacerlo asi. Para estos primeros apartados (1 - 6) usare mis columnas extras de tiempo, y ya en los ultimos
    //apartados en los que se restringe el DataSet a emplear utilizare la otra formula.


    val datosDS1 = datosDF3.as[InfraccionesAllStrings]
    val datosDS2 =datosDS1.withColumn("latitudeDouble",datosDS1("latitude").cast("Double"))
                          .withColumn("longitudeDouble",datosDS1("longitude").cast("Double"))
                          .withColumn("yearInt",datosDS1("year").cast("int"))
                          .drop("longitude", "year","latitude")
    val datosDS3 = datosDS2.withColumnRenamed("longitudeDouble","longitude")
                            .withColumnRenamed("latitudeDouble","latitude")
                            .withColumnRenamed("yearInt","year")
                            .withColumn("dia", dayofmonth(to_timestamp($"date", "MM/dd/yy")))
                            .withColumn("mes", month(to_timestamp($"date", "MM/dd/yy")))
                            .withColumn("ano", year(to_timestamp($"date", "MM/dd/yy")))
                            .withColumn("hora", hour(to_timestamp($"time", "HH:mm:ss")))
                            .withColumn("min", minute(to_timestamp($"time", "HH:mm:ss"))).as[InfraccionesCompletas].persist()
    datosDS3.collect()
    datosDS3.show(4)
    Thread.sleep(6000)
//1. Numero de infracciones por ano teniendo en cuenta desde el 2000, en orden descendente

    //Yo entiendo descendente como el primer ano el mas reciente, y de ahi hacia abajo.
    //En general, entiendo ano como ano de infraccion
    //Basicamente, agrupamos por clave (siendo la clave los anos), luego los contamos.
    //y para adelante, ordenamos de forma descendente y renombramos las columnas.

    println("1. Infracciones por ano desde el 2000")

    val Ejercicio1 = datosDS3.groupByKey(x=>x.ano).count().filter(x=>x._1>=2000).orderBy(desc("value"))
                    .withColumnRenamed("value", "anos").withColumnRenamed("count(1)","num infracciones")
    Ejercicio1.collect()
    Ejercicio1.show(100)
    Thread.sleep(6000)
//2. Porcentaje de infracciones agrupando por cada estado

    //Primero, contamos todas las infracciones en total.
    //Luego, realizamos un groupByKey con los estados de conductores, los contamos, y mapeamos para que quede
    //el estado y el valor dividido. Renombramos las columnas.
    //Llama la atencion como la mayoria estan en Maryland (MD). Precisamente, el condado de Montgomery esta en
    //Maryland, por lo que tiene sentido.

    println("2. Porcentaje infracciones agrupado por estado")

    val infraccionesTotales = datosDS3.count().toDouble
    println("Infracciones totales = " + infraccionesTotales)
    val Ejercicio2 = datosDS3.groupByKey(x=>x.driver_state).count().map(x=>(x._1,100*x._2/infraccionesTotales))
      .withColumnRenamed("_1","estado del conductor")
      .withColumnRenamed("_2","porcentaje infracciones")
    Ejercicio2.collect()
    Ejercicio2.show(100)
    Thread.sleep(6000)
//3. Numero de infracciones con danos personales por cada tipo de vehiculo,ordenando por numero de mayor a menor.


    println("3. Infracciones con danos personales por tipo de vehiculo")

    val Ejercicio3 = datosDS3.filter(x=>x.personal_injury=="Yes").groupByKey(x=>x.vehicle_type).count().orderBy(desc("count(1)"))
      .withColumnRenamed("value","tipo de vehiculo")
      .withColumnRenamed("count(1)","num infracciones con danos personales")
    Ejercicio3.collect()
    Ejercicio3.show(100)
    Thread.sleep(6000)

    //4. Color de coche que mas infracciones acumulo, del 2005 al 2015 incluidos.

    //Este creo que lo he hecho mucho mas complicado de lo que realmente es.
    //Esta claro que primero filtro los anos, agrupo por ano y color y de ahi realizo el count().
    //A partir de aqui, intente hacer aggregate, pero aggregate solo me daba como output el numero maximo de coches y el ano, sin el color.
    //He leido en Internet que para eludir este comportamiento se puede emplear Window, con el que consigo que la columna de colores no se vaya.
    //Con el Window creo una columna mas con los valores maximos de cada ano. Solo queda filtrar y quedarnos con las filas en cuyo color coincida
    //el count con ese valor maximo por ano obtenido de window. Esto lo he investigado en StackOverFlow.

    println("4. Color de coche con mas infracciones, por ano")

    import org.apache.spark.sql.expressions.Window
    val w1 = Window.partitionBy($"ano")
    val Ejercicio4 = datosDS3.filter(x=>x.ano>=2005&&x.ano<=2015).groupBy("ano","color").count()
      .withColumn("num_infracciones", max("count").over(w1)).filter($"num_infracciones"===$"count")
      .drop("count").orderBy(asc("ano"))
    Ejercicio4.collect()
    Ejercicio4.show(100)
    Thread.sleep(6000)

    //5. Hora del dia con mas infracciones, en 2012

    println("5. Hora de mas infracciones en 2012")

    //Este Ejercicio ahora resulta sencillo porque ya me habia separado previamente las horas de los minutos.
    //Filtramos por ano de incidente, agrupamos por horas y contamos, y nos interesa solo el intervalo de hora en el que
    //se han producido mas accidentes de 2012.
    val w2 = Window.partitionBy()
    val Ejercicio5 = datosDS3.filter(x=>x.ano==2012).groupBy("hora").count()
      .withColumn("maximo", max("count").over(w2)).filter($"maximo"===$"count")
      .drop("count")
    Ejercicio5.collect()
    Ejercicio5.show(100)
    Thread.sleep(6000)

    //val Ejercicio5= datosDS3.withColumn("nueva",split($"date","/").getItem(2).cast("Int")).filter($"nueva"===2012)
    //Ejercicio5.show()

    //6. Creacion de nuevo Dataset.

    //Creamos el nuevo Dataset a partir de datosDS3, eliminando el resto y quitando las filas con valores "" para texto y 0 para numerico.
    //Nos quedamos con date, time, state, vehicle_type, color, violation_type, driver_city, driver_state, latitude y longitude.
    //Como de antes ya sustitui los null por 0.0 y por "", aqui no empleo drop (que lo usaria con null), sino filter.

    println("6. Crear el DataSet")

    val datosDS4=datosDS3.drop("agency","subAgency","description","location","belts","personal_injury","property_damage","fatal","commercial_license",
    "HAZMAT","commercial_vehicle","alcohol","work_zone","make","model","charge","article","contributed_to_accident","DL_state","arrest_type",
    "year","dia","mes","ano","hora","min").as[InfraccionesRecortadas]
    val Ejercicio6 = datosDS4.filter(x=>x.latitude!=0.0).filter(x=>x.longitude!=0.0).filter(x=>x.driver_state!="").filter(x=>x.color!="").persist()
    datosDS3.unpersist()
    Ejercicio6.collect()
    Ejercicio6.show(4)
    Thread.sleep(6000)
    //7. Realizar funcion mostViolationsCityPerState

    println("7. Realizar mostViolationsCityPerState")
    //Primero, filtro que el estado del conductor coincida con el parametro pasado. Agrupamos las infracciones con groupBy por ciudad, y las contamos.
    //Hago uso de la window para no perder el nombre de la ciudad. Creamos una columna mas en la que se pone el valor maximo de todos los campos,
    //y solamente seleccionamos la fila cuyo valor coincida con el maximo. Utilizamos la Window, otra vez, para no ir perdiendo columnas por el camino.

    Thread.sleep(6000)
    //8. Probarla con el DataSet y con MD

    //Simplemente, probamos la funcion.
    println("8. Prueba mostViolationsCityPerState")
    val resultado1=mostViolationsCityPerState(Ejercicio6,"MD")
    println(resultado1)
    Thread.sleep(6000)
    //9. Realizar funcion positionPerType
    println("9. Realizamos la funcion positionPerType")
    Thread.sleep(6000)
    //En primer lugar, filtramos Dataset por ano propuesto, cogiendolo desde la fecha en String. Agrupar por tipo de vehiculo y con aggregate
    //se obtiene la media de las latitudes y longitudes.

    //10. Probarla con el DataSet y con el ano 2015.

    //Simplemente, probamos la funcion.
    println("10. Probamos positionPerType")
    val resultado2 = positionPerType(Ejercicio6,2015)
    resultado2.collect()
    resultado2.show(100)
    Ejercicio6.unpersist()
    Thread.sleep(6000)
  }

}

