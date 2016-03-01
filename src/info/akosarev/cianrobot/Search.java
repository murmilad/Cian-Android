package info.akosarev.cianrobot;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.content.SharedPreferences;
import android.util.Log;

public class Search {

		protected List<String> shapes;

		
		public Search() {
			this(getMetroShapes());
		}

		public Search(LinkedList<String> polygons) {
			this.shapes = polygons;
			// CAO
			shapes.add("55.7399_37.5389,55.7522_37.5324,55.7681_37.5344,55.7743_37.5516,55.7860_37.5633,55.7916_37.5760,55.7903_37.6385,55.7945_37.6577,55.7853_37.6797,55.7797_37.7040,55.7656_37.7095,55.7495_37.7147,55.7381_37.7013,55.7289_37.6817,55.7179_37.6606,55.7057_37.6671,55.6974_37.6555,55.6995_37.6333,55.6858_37.6299,55.6848_37.6095,55.7055_37.5944,55.7206_37.5588,55.7357_37.5396");

		}

		public List<HashMap<String, Object>> lookForFlats(SharedPreferences settings, SharedPreferences.Editor editor, CheckHandler handler){
			List<HashMap<String, Object>> objects = new LinkedList<HashMap<String, Object>>();

			return objects;
		}


		private static LinkedList<String> getMetroShapes(){
				
				LinkedList<Coordinate> metro = new LinkedList<Coordinate>();
				
				metro.add(new Coordinate((double) 37.716621, (double) 55.751432)); //Авиамоторная
		///		metro.add(new Coordinate((double) 37.657008, (double) 55.706634)); //Автозаводская
				metro.add(new Coordinate((double) 37.573339, (double) 55.687660)); //Академическая
		///		metro.add(new Coordinate((double) 37.609308, (double) 55.752075)); //Александровский сад
//				metro.add(new Coordinate((double) 37.638737, (double) 55.807800)); //Алексеевская
//				metro.add(new Coordinate((double) 37.765678, (double) 55.633490)); //Алма//Атинская
				metro.add(new Coordinate((double) 37.587344, (double) 55.898376)); //Алтуфьево
//				metro.add(new Coordinate((double) 37.596812, (double) 55.583657)); //Аннино
		///		metro.add(new Coordinate((double) 37.604116, (double) 55.752516)); //Арбатская
		///		metro.add(new Coordinate((double) 37.601519, (double) 55.752131)); //Арбатская
				metro.add(new Coordinate((double) 37.532870, (double) 55.800261)); //Аэропорт
				metro.add(new Coordinate((double) 37.664581, (double) 55.869794)); //Бабушкинская
				metro.add(new Coordinate((double) 37.497863, (double) 55.743801)); //Багратионовская
		///		metro.add(new Coordinate((double) 37.581280, (double) 55.760818)); //Баррикадная
		///		metro.add(new Coordinate((double) 37.679035, (double) 55.772406)); //Бауманская
				metro.add(new Coordinate((double) 37.545518, (double) 55.773505)); //Беговая
		///		metro.add(new Coordinate((double) 37.586194, (double) 55.777170)); //Белорусская
		///		metro.add(new Coordinate((double) 37.582107, (double) 55.777439)); //Белорусская
				metro.add(new Coordinate((double) 37.526115, (double) 55.642357)); //Беляево
				metro.add(new Coordinate((double) 37.603011, (double) 55.883868)); //Бибирево
		///		metro.add(new Coordinate((double) 37.611482, (double) 55.752501)); //Библиотека имени Ленина
//				metro.add(new Coordinate((double) 37.555328, (double) 55.601188)); //Битцевский парк
//				metro.add(new Coordinate((double) 37.743831, (double) 55.633587)); //Борисово
//				metro.add(new Coordinate((double) 37.609254, (double) 55.750454)); //Боровицкая
				metro.add(new Coordinate((double) 37.637811, (double) 55.844597)); //Ботанический сад
//				metro.add(new Coordinate((double) 37.750514, (double) 55.659460)); //Братиславская
//				metro.add(new Coordinate((double) 37.542329, (double) 55.545207)); //Бульвар адмирала Ушакова
//				metro.add(new Coordinate((double) 37.577346, (double) 55.569667)); //Бульвар Дмитрия Донского
				metro.add(new Coordinate((double) 37.735117, (double) 55.814264)); //Бульвар Рокоссовского
//				metro.add(new Coordinate((double) 37.515919, (double) 55.537964)); //Бунинская аллея
				metro.add(new Coordinate((double) 37.619522, (double) 55.653294)); //Варшавская
				metro.add(new Coordinate((double) 37.641090, (double) 55.821401)); //ВДНХ
				metro.add(new Coordinate((double) 37.590282, (double) 55.847922)); //Владыкино
				metro.add(new Coordinate((double) 37.486616, (double) 55.840209)); //Водный стадион
				metro.add(new Coordinate((double) 37.497791, (double) 55.818923)); //Войковская
				metro.add(new Coordinate((double) 37.687102, (double) 55.724900)); //Волгоградский проспект
//				metro.add(new Coordinate((double) 37.754314, (double) 55.690446)); //Волжская
//				metro.add(new Coordinate((double) 37.382034, (double) 55.835508)); //Волоколамская
				metro.add(new Coordinate((double) 37.559317, (double) 55.710438)); //Воробьёвы горы
		///		metro.add(new Coordinate((double) 37.543021, (double) 55.749547)); //Выставочная
//				metro.add(new Coordinate((double) 37.817969, (double) 55.715682)); //Выхино
		///		metro.add(new Coordinate((double) 37.542671, (double) 55.748843)); //Деловой центр
				metro.add(new Coordinate((double) 37.558212, (double) 55.789704)); //Динамо
				metro.add(new Coordinate((double) 37.580831, (double) 55.807881)); //Дмитровская
		///		metro.add(new Coordinate((double) 37.622711, (double) 55.729012)); //Добрынинская
				metro.add(new Coordinate((double) 37.717905, (double) 55.610697)); //Домодедовская
		///		metro.add(new Coordinate((double) 37.614716, (double) 55.781484)); //Достоевская
				metro.add(new Coordinate((double) 37.676259, (double) 55.718070)); //Дубровка
//				metro.add(new Coordinate((double) 37.855123, (double) 55.684539)); //Жулебино
//				metro.add(new Coordinate((double) 37.745205, (double) 55.612329)); //Зябликово
				metro.add(new Coordinate((double) 37.781380, (double) 55.787746)); //Измайловская
				metro.add(new Coordinate((double) 37.540075, (double) 55.656682)); //Калужская
				metro.add(new Coordinate((double) 37.656218, (double) 55.636107)); //Кантемировская
//				metro.add(new Coordinate((double) 37.598232, (double) 55.653177)); //Каховская
				metro.add(new Coordinate((double) 37.649256, (double) 55.655432)); //Каширская
				metro.add(new Coordinate((double) 37.649256, (double) 55.655432)); //Каширская
		///		metro.add(new Coordinate((double) 37.567545, (double) 55.744596)); //Киевская
		///		metro.add(new Coordinate((double) 37.566449, (double) 55.744075)); //Киевская
		///		metro.add(new Coordinate((double) 37.564132, (double) 55.743117)); //Киевская
//				metro.add(new Coordinate((double) 37.631326, (double) 55.756498)); //Китай//город
//				metro.add(new Coordinate((double) 37.633877, (double) 55.754360)); //Китай//город
//				metro.add(new Coordinate((double) 37.685710, (double) 55.706320)); //Кожуховская
				metro.add(new Coordinate((double) 37.663719, (double) 55.677423)); //Коломенская
		///		metro.add(new Coordinate((double) 37.654772, (double) 55.775672)); //Комсомольская
		///		metro.add(new Coordinate((double) 37.654565, (double) 55.774072)); //Комсомольская
				metro.add(new Coordinate((double) 37.520024, (double) 55.633658)); //Коньково
//				metro.add(new Coordinate((double) 37.746355, (double) 55.613717)); //Красногвардейская
		///		metro.add(new Coordinate((double) 37.577211, (double) 55.760211)); //Краснопресненская
		///		metro.add(new Coordinate((double) 37.666072, (double) 55.779849)); //Красносельская
		///		metro.add(new Coordinate((double) 37.648888, (double) 55.768795)); //Красные ворота
		///		metro.add(new Coordinate((double) 37.664788, (double) 55.732464)); //Крестьянская застава
		///		metro.add(new Coordinate((double) 37.603487, (double) 55.745068)); //Кропоткинская
				metro.add(new Coordinate((double) 37.408139, (double) 55.756842)); //Крылатское
		///		metro.add(new Coordinate((double) 37.623780, (double) 55.761598)); //Кузнецкий мост
//				metro.add(new Coordinate((double) 37.765902, (double) 55.705417)); //Кузьминки
//				metro.add(new Coordinate((double) 37.446874, (double) 55.730877)); //Кунцевская
				metro.add(new Coordinate((double) 37.445123, (double) 55.730634)); //Кунцевская
		///		metro.add(new Coordinate((double) 37.660287, (double) 55.758463)); //Курская
		///		metro.add(new Coordinate((double) 37.659155, (double) 55.758640)); //Курская
				metro.add(new Coordinate((double) 37.534236, (double) 55.740178)); //Кутузовская
				metro.add(new Coordinate((double) 37.586239, (double) 55.707689)); //Ленинский проспект
//				metro.add(new Coordinate((double) 37.852275, (double) 55.701765)); //Лермонтовский проспект
//				metro.add(new Coordinate((double) 37.577310, (double) 55.581968)); //Лесопарковая
		///		metro.add(new Coordinate((double) 37.627346, (double) 55.759162)); //Лубянка
//				metro.add(new Coordinate((double) 37.762003, (double) 55.676265)); //Люблино
		///		metro.add(new Coordinate((double) 37.656802, (double) 55.740993)); //Марксистская
				metro.add(new Coordinate((double) 37.616180, (double) 55.793723)); //Марьина роща
//				metro.add(new Coordinate((double) 37.744118, (double) 55.649368)); //Марьино
		///		metro.add(new Coordinate((double) 37.596192, (double) 55.769808)); //Маяковская
				metro.add(new Coordinate((double) 37.661527, (double) 55.887473)); //Медведково
				metro.add(new Coordinate((double) 37.533041, (double) 55.748640)); //Международная
		///		metro.add(new Coordinate((double) 37.598735, (double) 55.781788)); //Менделеевская
				metro.add(new Coordinate((double) 37.361220, (double) 55.846098)); //Митино
				metro.add(new Coordinate((double) 37.416386, (double) 55.741004)); //Молодёжная
//				metro.add(new Coordinate((double) 37.384747, (double) 55.823990)); //Мякинино
				metro.add(new Coordinate((double) 37.623061, (double) 55.683676)); //Нагатинская
				metro.add(new Coordinate((double) 37.610745, (double) 55.672854)); //Нагорная
				metro.add(new Coordinate((double) 37.605274, (double) 55.662379)); //Нахимовский проспект
//				metro.add(new Coordinate((double) 37.817295, (double) 55.751675)); //Новогиреево
//				metro.add(new Coordinate((double) 37.864052, (double) 55.745113)); //Новокосино
		///		metro.add(new Coordinate((double) 37.629125, (double) 55.742276)); //Новокузнецкая
		///		metro.add(new Coordinate((double) 37.601421, (double) 55.779565)); //Новослободская
//				metro.add(new Coordinate((double) 37.553442, (double) 55.601833)); //Новоясеневская
				metro.add(new Coordinate((double) 37.554493, (double) 55.670077)); //Новые Черёмушки
		///		metro.add(new Coordinate((double) 37.612766, (double) 55.731257)); //Октябрьская
		///		metro.add(new Coordinate((double) 37.610979, (double) 55.729255)); //Октябрьская
				metro.add(new Coordinate((double) 37.493317, (double) 55.793581)); //Октябрьское поле
				metro.add(new Coordinate((double) 37.695214, (double) 55.612690)); //Орехово
				metro.add(new Coordinate((double) 37.604843, (double) 55.863384)); //Отрадное
		///		metro.add(new Coordinate((double) 37.615327, (double) 55.756523)); //Охотный ряд
		///		metro.add(new Coordinate((double) 37.636329, (double) 55.731536)); //Павелецкая
		///		metro.add(new Coordinate((double) 37.638961, (double) 55.729787)); //Павелецкая
		///		metro.add(new Coordinate((double) 37.595061, (double) 55.736077)); //Парк культуры
		///		metro.add(new Coordinate((double) 37.592905, (double) 55.735150)); //Парк культуры
		///		metro.add(new Coordinate((double) 37.516925, (double) 55.736164)); //Парк Победы
				metro.add(new Coordinate((double) 37.514401, (double) 55.736478)); //Парк Победы
				metro.add(new Coordinate((double) 37.749265, (double) 55.788424)); //Партизанская
				metro.add(new Coordinate((double) 37.799364, (double) 55.794376)); //Первомайская
				metro.add(new Coordinate((double) 37.786887, (double) 55.751320)); //Перово
				metro.add(new Coordinate((double) 37.575558, (double) 55.836524)); //Петровско//Разумовская
//				metro.add(new Coordinate((double) 37.728398, (double) 55.692972)); //Печатники
				metro.add(new Coordinate((double) 37.467078, (double) 55.735986)); //Пионерская
				metro.add(new Coordinate((double) 37.436382, (double) 55.860529)); //Планерная
		///		metro.add(new Coordinate((double) 37.680589, (double) 55.747024)); //Площадь Ильича
		///		metro.add(new Coordinate((double) 37.622360, (double) 55.756741)); //Площадь Революции
				metro.add(new Coordinate((double) 37.517895, (double) 55.777201)); //Полежаевская
//				metro.add(new Coordinate((double) 37.618471, (double) 55.736807)); //Полянка
//				metro.add(new Coordinate((double) 37.603972, (double) 55.611577)); //Пражская
				metro.add(new Coordinate((double) 37.715022, (double) 55.796167)); //Преображенская площадь
		///		metro.add(new Coordinate((double) 37.666917, (double) 55.731546)); //Пролетарская
				metro.add(new Coordinate((double) 37.505831, (double) 55.676910)); //Проспект Вернадского
		///		metro.add(new Coordinate((double) 37.633482, (double) 55.781757)); //Проспект Мира
		///		metro.add(new Coordinate((double) 37.633464, (double) 55.779631)); //Проспект Мира
				metro.add(new Coordinate((double) 37.562595, (double) 55.677671)); //Профсоюзная
		///		metro.add(new Coordinate((double) 37.603900, (double) 55.765747)); //Пушкинская
				metro.add(new Coordinate((double) 37.354025, (double) 55.855644)); //Пятницкое шоссе
				metro.add(new Coordinate((double) 37.476231, (double) 55.854891)); //Речной вокзал
				metro.add(new Coordinate((double) 37.636123, (double) 55.792513)); //Рижская
		///		metro.add(new Coordinate((double) 37.681254, (double) 55.746228)); //Римская
//				metro.add(new Coordinate((double) 37.793606, (double) 55.717366)); //Рязанский проспект
				metro.add(new Coordinate((double) 37.588296, (double) 55.793313)); //Савёловская
				metro.add(new Coordinate((double) 37.653379, (double) 55.855558)); //Свиблово
				metro.add(new Coordinate((double) 37.598384, (double) 55.651552)); //Севастопольская
				metro.add(new Coordinate((double) 37.719423, (double) 55.783195)); //Семёновская
		///		metro.add(new Coordinate((double) 37.625199, (double) 55.726680)); //Серпуховская
				metro.add(new Coordinate((double) 37.472171, (double) 55.729828)); //Славянский бульвар
		///		metro.add(new Coordinate((double) 37.581658, (double) 55.749060)); //Смоленская
		///		metro.add(new Coordinate((double) 37.583841, (double) 55.747642)); //Смоленская
				metro.add(new Coordinate((double) 37.514787, (double) 55.805042)); //Сокол
				metro.add(new Coordinate((double) 37.679700, (double) 55.789198)); //Сокольники
				metro.add(new Coordinate((double) 37.434801, (double) 55.817234)); //Спартак
		///		metro.add(new Coordinate((double) 37.562227, (double) 55.722761)); //Спортивная
//				metro.add(new Coordinate((double) 37.636374, (double) 55.766299)); //Сретенский бульвар
				metro.add(new Coordinate((double) 37.403118, (double) 55.803691)); //Строгино
		///		metro.add(new Coordinate((double) 37.548375, (double) 55.738784)); //Студенческая
		///		metro.add(new Coordinate((double) 37.632332, (double) 55.772315)); //Сухаревская
				metro.add(new Coordinate((double) 37.439787, (double) 55.850510)); //Сходненская
		///		metro.add(new Coordinate((double) 37.653146, (double) 55.742433)); //Таганская
		///		metro.add(new Coordinate((double) 37.653613, (double) 55.739199)); //Таганская
		///		metro.add(new Coordinate((double) 37.605939, (double) 55.764455)); //Тверская
		///		metro.add(new Coordinate((double) 37.617680, (double) 55.758808)); //Театральная
//				metro.add(new Coordinate((double) 37.732117, (double) 55.709211)); //Текстильщики
//				metro.add(new Coordinate((double) 37.505912, (double) 55.618730)); //Тёплый Стан
				metro.add(new Coordinate((double) 37.574498, (double) 55.818660)); //Тимирязевская
		///		metro.add(new Coordinate((double) 37.626142, (double) 55.741125)); //Третьяковская
		///		metro.add(new Coordinate((double) 37.625981, (double) 55.740319)); //Третьяковская
		///		metro.add(new Coordinate((double) 37.621902, (double) 55.767939)); //Трубная
		///		metro.add(new Coordinate((double) 37.622612, (double) 55.708841)); //Тульская
		///		metro.add(new Coordinate((double) 37.636742, (double) 55.765276)); //Тургеневская
				metro.add(new Coordinate((double) 37.437604, (double) 55.827080)); //Тушинская
		///		metro.add(new Coordinate((double) 37.561419, (double) 55.764273)); //Улица 1905 года
//				metro.add(new Coordinate((double) 37.600675, (double) 55.595883)); //Улица академика Янгеля
//				metro.add(new Coordinate((double) 37.531226, (double) 55.541825)); //Улица Горчакова
//				metro.add(new Coordinate((double) 37.554618, (double) 55.548034)); //Улица Скобелевская
//				metro.add(new Coordinate((double) 37.576708, (double) 55.568838)); //Улица Старокачаловская
				metro.add(new Coordinate((double) 37.534532, (double) 55.692440)); //Университет
				metro.add(new Coordinate((double) 37.483328, (double) 55.739519)); //Филёвский парк
				metro.add(new Coordinate((double) 37.514949, (double) 55.745970)); //Фили
		///		metro.add(new Coordinate((double) 37.580328, (double) 55.727232)); //Фрунзенская
				metro.add(new Coordinate((double) 37.669612, (double) 55.620982)); //Царицыно
		///		metro.add(new Coordinate((double) 37.620986, (double) 55.771616)); //Цветной бульвар
				metro.add(new Coordinate((double) 37.744819, (double) 55.802988)); //Черкизовская
				metro.add(new Coordinate((double) 37.606065, (double) 55.640538)); //Чертановская
		///		metro.add(new Coordinate((double) 37.608167, (double) 55.765843)); //Чеховская
		///		metro.add(new Coordinate((double) 37.638683, (double) 55.764794)); //Чистые пруды
		///		metro.add(new Coordinate((double) 37.659263, (double) 55.755930)); //Чкаловская
		///		metro.add(new Coordinate((double) 37.607799, (double) 55.718821)); //Шаболовская
//				metro.add(new Coordinate((double) 37.743723, (double) 55.620982)); //Шипиловская
				metro.add(new Coordinate((double) 37.751583, (double) 55.758255)); //Шоссе Энтузиастов
//				metro.add(new Coordinate((double) 37.798556, (double) 55.810228)); //Щёлковская
				metro.add(new Coordinate((double) 37.463772, (double) 55.808827)); //Щукинская
				metro.add(new Coordinate((double) 37.705284, (double) 55.782066)); //Электрозаводская
				metro.add(new Coordinate((double) 37.482852, (double) 55.663146)); //Юго//Западная
				metro.add(new Coordinate((double) 37.609047, (double) 55.622436)); //Южная
//				metro.add(new Coordinate((double) 37.533400, (double) 55.606182)); //Ясенево

				DecimalFormat dff;
		        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
		        otherSymbols.setDecimalSeparator('.');

				dff = new DecimalFormat("##.######", otherSymbols);
		    	dff.setRoundingMode(RoundingMode.DOWN);

				LinkedList<String> metroShapes = new LinkedList<String>();
				for (Coordinate metroCoordinate : metro) {
					String metroShape = "";
					String comma = "";
					Log.i("CianTask", "JSON: metro search point " + dff.format(metroCoordinate.getDoubleLon())+","+dff.format(metroCoordinate.getDoubleLat()));

					for (Integer i=0; i < 360; i += 360 / 15) {
						Coordinate radiusCoordinate = calcEndPoint(metroCoordinate, 1200, i);


						metroShape += comma + dff.format(radiusCoordinate.getDoubleLat()) + "_" + dff.format(radiusCoordinate.getDoubleLon());
						comma = ",";
					}
					metroShapes.add(metroShape);
					
				} 
				
				return metroShapes;
			 }

		 public static Coordinate calcEndPoint(Coordinate center , int distance, double  bearing)
		  {
		      Coordinate gp=null;

		      double R = 6371000; // meters , earth Radius approx
		      double PI = 3.1415926535;
		      double RADIANS = PI/180;
		      double DEGREES = 180/PI;

		      double lat2;
		      double lon2;

		      double lat1 = center.getDoubleLat() * RADIANS;
		      double lon1 = center.getDoubleLon() * RADIANS;
		      double radbear = bearing * RADIANS;

		     // System.out.println("lat1="+lat1 + ",lon1="+lon1);

		      lat2 = Math.asin( Math.sin(lat1)*Math.cos(distance / R) +
		              Math.cos(lat1)*Math.sin(distance/R)*Math.cos(radbear) );
		      lon2 = lon1 + Math.atan2(Math.sin(radbear)*Math.sin(distance / R)*Math.cos(lat1),
		                     Math.cos(distance/R)-Math.sin(lat1)*Math.sin(lat2));

		     // System.out.println("lat2="+lat2*DEGREES + ",lon2="+lon2*DEGREES);

		      gp = new Coordinate( lon2*DEGREES, lat2*DEGREES);

		      return(gp);
		  }

}


//shapes.add("55.846410_37.661133,55.867991_37.680016,55.889561_37.683449,55.895529_37.675209,55.897454_37.644310,55.893796_37.641563,55.882629_37.639503,55.861248_37.634010,55.852577_37.634354");
//shapes.add("55.852192_37.634354,55.845831_37.660789,55.794333_37.652893,55.790666_37.636070,55.791824_37.617188,55.822309_37.613068,55.849686_37.615471,55.851999_37.632637");
//shapes.add("55.851228_37.566719,55.816523_37.548180,55.792403_37.572899,55.790666_37.614098,55.795877_37.618217,55.851035_37.618561");
//shapes.add("55.873770_37.455826,55.882436_37.459259,55.886480_37.481232,55.861056_37.501144,55.836386_37.538910,55.823466_37.523460,55.815944_37.547150,55.791245_37.573586,55.779855_37.556763,55.798965_37.502518,55.827902_37.475739,55.841398_37.470245,55.871651_37.452393");
//shapes.add("55.778697_37.556763,55.767303_37.539597,55.769042_37.469215,55.792403_37.451706,55.809385_37.442093,55.812665_37.419777,55.824045_37.410507,55.856817_37.401581,55.871651_37.416344");
//shapes.add("55.871651_37.415314,55.778697_37.558479,55.877237_37.441063,55.867991_37.428703");
//shapes.add("55.828287_37.398148,55.809192_37.431107,55.798965_37.446213,55.779469_37.446899,55.779469_37.402267,55.794912_37.398148,55.799544_37.384071,55.828095_37.395401");
//shapes.add("55.759770_37.534447,55.745473_37.532043,55.738709_37.536163,55.728850_37.531700,55.725563_37.497025,55.711640_37.472992,55.716668_37.447929,55.716282_37.421150,55.727883_37.419090,55.736969_37.388191,55.754361_37.380638,55.763827_37.398491,55.768655_37.417717,55.758418_37.433167,55.740448_37.454796,55.759964_37.493591,55.760929_37.530327");
//shapes.add("55.726917_37.547493,55.712607_37.579079,55.705063_37.597275,55.693842_37.595558,55.652411_37.554703,55.643112_37.545776,55.678359_37.475739,55.696551_37.481575,55.726143_37.545776");
//shapes.add("55.682617_37.467499,55.640980_37.551956,55.600463_37.506294,55.665580_37.430763,55.682811_37.465782");
//shapes.add("55.638849_37.688942,55.628577_37.620277,55.647955_37.551613,55.599881_37.504578,55.576404_37.593498,55.575433_37.624741,55.608996_37.636757,55.612874_37.675209,55.637880_37.687569");
//shapes.add("55.611905_37.675896,55.592509_37.711601,55.605312_37.757607,55.620048_37.768250,55.629353_37.783699,55.646793_37.780266,55.639430_37.743530,55.640980_37.696152,55.638267_37.689972");
//shapes.add("55.662869_37.716064,55.642530_37.716064,55.641755_37.747650,55.648924_37.774773,55.670615_37.758980,55.663063_37.717438");
//shapes.add("55.759577_37.799149,55.739095_37.791595,55.739095_37.755547,55.748565_37.746277,55.741414_37.703705,55.756873_37.696495,55.770394_37.746964,55.772132_37.789536");
//shapes.add("55.766724_37.693748,55.772904_37.736664,55.778311_37.760353,55.780049_37.799492,55.793368_37.808075,55.801474_37.803955,55.801474_37.788506,55.794333_37.764130,55.795684_37.739754,55.786033_37.684135,55.779469_37.676582,55.767497_37.691345");
//shapes.add("55.826166_37.711945,55.815944_37.740440,55.807648_37.755203,55.803211_37.754860,55.794719_37.743187,55.785068_37.685852,55.779083_37.673836,55.786999_37.663193,55.797035_37.667656,55.807841_37.710915,55.810542_37.719498,55.818259_37.696152");
//shapes.add("55.771359_37.598991,55.773677_37.626457,55.770200_37.643967,55.759964_37.657356,55.752815_37.655983,55.753781_37.693748,55.779083_37.682419,55.793561_37.659760,55.796842_37.637787,55.796842_37.624054,55.797421_37.596931,55.795877_37.581139,55.773290_37.595558");
//shapes.add("55.796070_37.593155,55.770587_37.603111,55.760929_37.587662,55.748371_37.586288,55.741028_37.588005,55.736582_37.597275,55.717055_37.576332,55.738322_37.529297,55.758611_37.530670,55.770587_37.533417,55.791631_37.568779");
//shapes.add("55.732910_37.548523,55.728077_37.541656,55.712027_37.575302,55.703322_37.654266,55.724403_37.711601,55.737162_37.696838,55.747212_37.699242,55.754361_37.695808,55.753588_37.653236,55.740448_37.650490,55.732716_37.638130,55.731363_37.615471,55.737356_37.593155,55.721116_37.574615,55.731943_37.550926");
//shapes.add("55.740255_37.708855,55.738902_37.691345,55.759770_37.680359,55.763827_37.704735");
//
//shapes.add("55.704870_37.596245,55.701001_37.611351,55.705644_37.621651,55.705063_37.622681,55.673906_37.627144,55.663063_37.617188,55.665000_37.601051,55.675649_37.569122,55.704290_37.595901");
//shapes.add("55.662482_37.618904,55.632454_37.619934,55.630709_37.618904,55.636136_37.574959,55.642337_37.549210,55.646405_37.537537,55.675068_37.567406,55.676810_37.571526,55.665580_37.612038");
//shapes.add("55.683198_37.624054,55.687262_37.676926,55.689972_37.685509,55.675649_37.694778,55.666742_37.653923,55.663450_37.620964,55.674293_37.625084,55.682230_37.623711");
//shapes.add("55.663838_37.619247,55.667323_37.648773,55.668098_37.662506,55.654929_37.654953,55.652411_37.659416,55.643305_37.680359,55.641562_37.685509,55.633423_37.658386,55.631291_37.620964,55.633810_37.616844,55.661126_37.616844,");
//
//shapes.add("55.910541_37.566719,55.897454_37.558823,55.881088_37.568779,55.877815_37.595901,55.876659_37.609978,55.885517_37.622337,55.893219_37.620621,55.902458_37.610664,55.904190_37.608948,55.910734_37.584572,55.910734_37.567749");
//shapes.add("55.881859_37.566032,55.877044_37.613068,55.867895_37.624397,55.863946_37.628002,55.862212_37.621651,55.857009_37.622681,55.850072_37.617874,55.858069_37.565689,55.881281_37.565861");
//shapes.add("55.859418_37.566719,55.845253_37.571869,55.844386_37.617359,55.853733_37.614613,55.859514_37.569122");
//shapes.add("55.842458_37.617702,55.857106_37.612896,55.860478_37.635384,55.857491_37.645168,55.847952_37.649460,55.841012_37.644997,55.842265_37.619419");
