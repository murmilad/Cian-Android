package info.akosarev.cianrobot;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.util.Log;

public class Search {

		protected List<Shape> shapes;
		protected List<Metro> metro;
		
		public Search() {
			// Metro
			shapes = getMetroShapes();
			// CAO
			shapes.add(new Shape("55.7399_37.5389,55.7522_37.5324,55.7681_37.5344,55.7743_37.5516,55.7860_37.5633,55.7916_37.5760,55.7903_37.6385,55.7945_37.6577,55.7853_37.6797,55.7797_37.7040,55.7656_37.7095,55.7495_37.7147,55.7381_37.7013,55.7289_37.6817,55.7179_37.6606,55.7057_37.6671,55.6974_37.6555,55.6995_37.6333,55.6858_37.6299,55.6848_37.6095,55.7055_37.5944,55.7206_37.5588,55.7357_37.5396", "Охотный ряд (ЦАО)", new Coordinate((double) 37.615327, (double) 55.756523)));
			// Yaroslavka
			shapes.add(new Shape("55.8788_37.7095,55.8850_37.7184,55.8830_37.7220,55.8808_37.7233,55.8810_37.7253,55.8805_37.7282,55.8777_37.7333,55.8759_37.7314,55.8753_37.7310,55.8737_37.7260,55.8732_37.7233,55.8785_37.7099", "ВДНХ (Ярославское шоссе)", new Coordinate((double) 37.641090, (double) 55.821401)));
			shapes.add(new Shape("55.8791_37.7091,55.8730_37.7237,55.8723_37.7190,55.8700_37.7158,55.8683_37.7134,55.8731_37.7003,55.8787_37.7086", "ВДНХ (Ярославское шоссе)", new Coordinate((double) 37.641090, (double) 55.821401)));
			shapes.add(new Shape("55.8735_37.7001,55.8683_37.7139,55.8653_37.7080,55.8636_37.7059,55.8626_37.7029,55.8621_37.7019,55.8670_37.6925,55.8733_37.6998", "ВДНХ (Ярославское шоссе)", new Coordinate((double) 37.641090, (double) 55.821401)));
			shapes.add(new Shape("55.8675_37.6922,55.8620_37.7022,55.8613_37.7017,55.8614_37.7060,55.8605_37.7181,55.8528_37.7215,55.8502_37.7199,55.8470_37.7138,55.8482_37.7046,55.8502_37.7009,55.8538_37.7011,55.8605_37.6917,55.8636_37.6856,55.8673_37.6916", "ВДНХ (Ярославское шоссе)", new Coordinate((double) 37.641090, (double) 55.821401)));
			shapes.add(new Shape("55.8639_37.6855,55.8607_37.6917,55.8539_37.7014,55.8503_37.7013,55.8490_37.7010,55.8477_37.6962,55.8516_37.6933,55.8487_37.6910,55.8519_37.6834,55.8562_37.6885,55.8601_37.6798,55.8637_37.6849", "ВДНХ (Ярославское шоссе)", new Coordinate((double) 37.641090, (double) 55.821401)));
			shapes.add(new Shape("55.8604_37.6796,55.8563_37.6888,55.8519_37.6840,55.8486_37.6913,55.8442_37.6834,55.8487_37.6737,55.8546_37.6819,55.8569_37.6754,55.8601_37.6793", "ВДНХ (Ярославское шоссе)", new Coordinate((double) 37.641090, (double) 55.821401)));
			shapes.add(new Shape("55.8572_37.6752,55.8546_37.6822,55.8479_37.6729,55.8473_37.6717,55.8533_37.6724,55.8569_37.6750", "ВДНХ (Ярославское шоссе)", new Coordinate((double) 37.641090, (double) 55.821401)));
			shapes.add(new Shape("55.8474_37.6712,55.8490_37.6736,55.8441_37.6837,55.8417_37.6796,55.8396_37.6778,55.8387_37.6765,55.8382_37.6718,55.8405_37.6631,55.8446_37.6506,55.8456_37.6502,55.8471_37.6508,55.8475_37.6601,55.8508_37.6580,55.8538_37.6661,55.8508_37.6709,55.8487_37.6712", "ВДНХ (Ярославское шоссе)", new Coordinate((double) 37.641090, (double) 55.821401)));
			shapes.add(new Shape("55.8450_37.6498,55.8380_37.6729,55.8250_37.6708,55.8253_37.6657,55.8300_37.6506,55.8333_37.6540,55.8400_37.6474,55.8443_37.6496", "ВДНХ (Ярославское шоссе)", new Coordinate((double) 37.641090, (double) 55.821401)));
			shapes.add(new Shape("55.8405_37.6470,55.8338_37.6544,55.8300_37.6515,55.8257_37.6658,55.8245_37.6700,55.8209_37.6686,55.8199_37.6681,55.8272_37.6451,55.8278_37.6437,55.8300_37.6460,55.8333_37.6364,55.8403_37.6363,55.8405_37.6457", "ВДНХ (Ярославское шоссе)", new Coordinate((double) 37.641090, (double) 55.821401)));
			//Timeryazevskiy
			shapes.add(new Shape("55.833831_37.541485,55.838651_37.546549,55.839904_37.550325,55.841880_37.559767,55.831614_37.561827,55.829444_37.568693,55.827661_37.572641,55.821296_37.573500,55.818885_37.585859,55.813002_37.578049,55.807359_37.572641,55.800461_37.583542,55.792596_37.584829,55.791776_37.575130,55.789170_37.570324,55.795154_37.566719,55.798531_37.556849,55.805092_37.551184,55.806973_37.556419,55.812785_37.552643,55.817246_37.555003,55.820453_37.560282,55.825419_37.556591,55.824286_37.548566,55.826238_37.544317,55.830722_37.546549,55.833301_37.549853,37.541485,55.833397", "Дмитровская (Тимерязевский район)", new Coordinate((double) 37.581997, (double) 55.808083))); 

		}

		public Search(LinkedList<Shape> polygons) {
			this.shapes = polygons;
		}


		public Set<String> lookForFlats(SharedPreferences settings, SharedPreferences.Editor editor, CheckHandler handler) throws IOException{

			return null;
		}


		private LinkedList<Shape> getMetroShapes(){
				
				metro = new LinkedList<Metro>();
				
				metro.add(new Metro("Авиамоторная", new Coordinate((double) 37.716621, (double) 55.751432), true));
				metro.add(new Metro("Автозаводская", new Coordinate((double) 37.657008, (double) 55.706634), false));
				metro.add(new Metro("Академическая", new Coordinate((double) 37.573339, (double) 55.687660), true));
				metro.add(new Metro("Александровский сад", new Coordinate((double) 37.609308, (double) 55.752075), false));
				metro.add(new Metro("Алексеевская", new Coordinate((double) 37.638737, (double) 55.807800), false));
				metro.add(new Metro("Алма//Атинская", new Coordinate((double) 37.765678, (double) 55.633490), false));
				metro.add(new Metro("Алтуфьево", new Coordinate((double) 37.587344, (double) 55.898376), true));
				metro.add(new Metro("Аннино", new Coordinate((double) 37.596812, (double) 55.583657), false));
				metro.add(new Metro("Арбатская", new Coordinate((double) 37.604116, (double) 55.752516), false));
				metro.add(new Metro("Арбатская", new Coordinate((double) 37.601519, (double) 55.752131), false));
				metro.add(new Metro("Аэропорт", new Coordinate((double) 37.532870, (double) 55.800261), true));
				metro.add(new Metro("Бабушкинская", new Coordinate((double) 37.664581, (double) 55.869794), true));
				metro.add(new Metro("Багратионовская", new Coordinate((double) 37.497863, (double) 55.743801), true));
				metro.add(new Metro("Баррикадная", new Coordinate((double) 37.581280, (double) 55.760818), false));
				metro.add(new Metro("Бауманская", new Coordinate((double) 37.679035, (double) 55.772406), false));
				metro.add(new Metro("Беговая", new Coordinate((double) 37.545518, (double) 55.773505), true));
				metro.add(new Metro("Белорусская", new Coordinate((double) 37.586194, (double) 55.777170), false));
				metro.add(new Metro("Белорусская", new Coordinate((double) 37.582107, (double) 55.777439), false));
				metro.add(new Metro("Беляево", new Coordinate((double) 37.526115, (double) 55.642357), true));
				metro.add(new Metro("Бибирево", new Coordinate((double) 37.603011, (double) 55.883868), false));
				metro.add(new Metro("Библиотека имени Ленина", new Coordinate((double) 37.611482, (double) 55.752501), false));
				metro.add(new Metro("Битцевский парк", new Coordinate((double) 37.555328, (double) 55.601188), false));
				metro.add(new Metro("Борисово", new Coordinate((double) 37.743831, (double) 55.633587), false));
				metro.add(new Metro("Боровицкая", new Coordinate((double) 37.609254, (double) 55.750454), false));
				metro.add(new Metro("Ботанический сад", new Coordinate((double) 37.637811, (double) 55.844597), true));
				metro.add(new Metro("Братиславская", new Coordinate((double) 37.750514, (double) 55.659460), false));
				metro.add(new Metro("Бульвар адмирала Ушакова", new Coordinate((double) 37.542329, (double) 55.545207), false));
				metro.add(new Metro("Бульвар Дмитрия Донского", new Coordinate((double) 37.577346, (double) 55.569667), false));
				metro.add(new Metro("Бульвар Рокоссовского", new Coordinate((double) 37.735117, (double) 55.814264), true));
				metro.add(new Metro("Бунинская аллея", new Coordinate((double) 37.515919, (double) 55.537964), false));
				metro.add(new Metro("Варшавская", new Coordinate((double) 37.619522, (double) 55.653294), true));
				metro.add(new Metro("ВДНХ", new Coordinate((double) 37.641090, (double) 55.821401), true));
				metro.add(new Metro("Владыкино", new Coordinate((double) 37.590282, (double) 55.847922), true));
				metro.add(new Metro("Водный стадион", new Coordinate((double) 37.486616, (double) 55.840209), true));
				metro.add(new Metro("Войковская", new Coordinate((double) 37.497791, (double) 55.818923), true));
				metro.add(new Metro("Волгоградский проспект", new Coordinate((double) 37.687102, (double) 55.724900), true));
				metro.add(new Metro("Волжская", new Coordinate((double) 37.754314, (double) 55.690446), false));
				metro.add(new Metro("Волоколамская", new Coordinate((double) 37.382034, (double) 55.835508), false));
				metro.add(new Metro("Воробьёвы горы", new Coordinate((double) 37.559317, (double) 55.710438), true));
				metro.add(new Metro("Выставочная", new Coordinate((double) 37.543021, (double) 55.749547), false));
				metro.add(new Metro("Выхино", new Coordinate((double) 37.817969, (double) 55.715682), false));
				metro.add(new Metro("Деловой центр", new Coordinate((double) 37.542671, (double) 55.748843), false));
				metro.add(new Metro("Динамо", new Coordinate((double) 37.558212, (double) 55.789704), true));
				metro.add(new Metro("Дмитровская", new Coordinate((double) 37.580831, (double) 55.807881), true));
				metro.add(new Metro("Добрынинская", new Coordinate((double) 37.622711, (double) 55.729012), false));
				metro.add(new Metro("Домодедовская", new Coordinate((double) 37.717905, (double) 55.610697), false));
				metro.add(new Metro("Достоевская", new Coordinate((double) 37.614716, (double) 55.781484), false));
				metro.add(new Metro("Дубровка", new Coordinate((double) 37.676259, (double) 55.718070), true));
				metro.add(new Metro("Жулебино", new Coordinate((double) 37.855123, (double) 55.684539), false));
				metro.add(new Metro("Зябликово", new Coordinate((double) 37.745205, (double) 55.612329), false));
				metro.add(new Metro("Измайловская", new Coordinate((double) 37.781380, (double) 55.787746), true));
				metro.add(new Metro("Калужская", new Coordinate((double) 37.540075, (double) 55.656682), true));
				metro.add(new Metro("Кантемировская", new Coordinate((double) 37.656218, (double) 55.636107), true));
				metro.add(new Metro("Каховская", new Coordinate((double) 37.598232, (double) 55.653177), false));
				metro.add(new Metro("Каширская", new Coordinate((double) 37.649256, (double) 55.655432), true));
				metro.add(new Metro("Киевская", new Coordinate((double) 37.567545, (double) 55.744596), false));
				metro.add(new Metro("Киевская", new Coordinate((double) 37.566449, (double) 55.744075), false));
				metro.add(new Metro("Киевская", new Coordinate((double) 37.564132, (double) 55.743117), false));
				metro.add(new Metro("Китай//город", new Coordinate((double) 37.631326, (double) 55.756498), false));
				metro.add(new Metro("Китай//город", new Coordinate((double) 37.633877, (double) 55.754360), false));
				metro.add(new Metro("Кожуховская", new Coordinate((double) 37.685710, (double) 55.706320), false));
				metro.add(new Metro("Коломенская", new Coordinate((double) 37.663719, (double) 55.677423), true));
				metro.add(new Metro("Комсомольская", new Coordinate((double) 37.654772, (double) 55.775672), false));
				metro.add(new Metro("Комсомольская", new Coordinate((double) 37.654565, (double) 55.774072), false));
				metro.add(new Metro("Коньково", new Coordinate((double) 37.520024, (double) 55.633658), true));
				metro.add(new Metro("Красногвардейская", new Coordinate((double) 37.746355, (double) 55.613717), false));
				metro.add(new Metro("Краснопресненская", new Coordinate((double) 37.577211, (double) 55.760211), false));
				metro.add(new Metro("Красносельская", new Coordinate((double) 37.666072, (double) 55.779849), false));
				metro.add(new Metro("Красные ворота", new Coordinate((double) 37.648888, (double) 55.768795), false));
				metro.add(new Metro("Крестьянская застава", new Coordinate((double) 37.664788, (double) 55.732464), false));
				metro.add(new Metro("Кропоткинская", new Coordinate((double) 37.603487, (double) 55.745068), false));
				metro.add(new Metro("Крылатское", new Coordinate((double) 37.408139, (double) 55.756842), true));
				metro.add(new Metro("Кузнецкий мост", new Coordinate((double) 37.623780, (double) 55.761598), false));
				metro.add(new Metro("Кузьминки", new Coordinate((double) 37.765902, (double) 55.705417), false));
				metro.add(new Metro("Кунцевская", new Coordinate((double) 37.446874, (double) 55.730877), false));
				metro.add(new Metro("Кунцевская", new Coordinate((double) 37.445123, (double) 55.730634), true));
				metro.add(new Metro("Курская", new Coordinate((double) 37.660287, (double) 55.758463), false));
				metro.add(new Metro("Курская", new Coordinate((double) 37.659155, (double) 55.758640), false));
				metro.add(new Metro("Кутузовская", new Coordinate((double) 37.534236, (double) 55.740178), true));
				metro.add(new Metro("Ленинский проспект", new Coordinate((double) 37.586239, (double) 55.707689), true));
				metro.add(new Metro("Лермонтовский проспект", new Coordinate((double) 37.852275, (double) 55.701765), false));
				metro.add(new Metro("Лесопарковая", new Coordinate((double) 37.577310, (double) 55.581968), false));
				metro.add(new Metro("Лубянка", new Coordinate((double) 37.627346, (double) 55.759162), false));
				metro.add(new Metro("Люблино", new Coordinate((double) 37.762003, (double) 55.676265), false));
				metro.add(new Metro("Марксистская", new Coordinate((double) 37.656802, (double) 55.740993), false));
				metro.add(new Metro("Марьина роща", new Coordinate((double) 37.616180, (double) 55.793723), true));
				metro.add(new Metro("Марьино", new Coordinate((double) 37.744118, (double) 55.649368), false));
				metro.add(new Metro("Маяковская", new Coordinate((double) 37.596192, (double) 55.769808), false));
				metro.add(new Metro("Медведково", new Coordinate((double) 37.661527, (double) 55.887473), true));
				metro.add(new Metro("Международная", new Coordinate((double) 37.533041, (double) 55.748640), true));
				metro.add(new Metro("Менделеевская", new Coordinate((double) 37.598735, (double) 55.781788), false));
				metro.add(new Metro("Митино", new Coordinate((double) 37.361220, (double) 55.846098), false));
				metro.add(new Metro("Молодёжная", new Coordinate((double) 37.416386, (double) 55.741004), true));
				metro.add(new Metro("Мякинино", new Coordinate((double) 37.384747, (double) 55.823990), false));
				metro.add(new Metro("Нагатинская", new Coordinate((double) 37.623061, (double) 55.683676), true));
				metro.add(new Metro("Нагорная", new Coordinate((double) 37.610745, (double) 55.672854), true));
				metro.add(new Metro("Нахимовский проспект", new Coordinate((double) 37.605274, (double) 55.662379), true));
				metro.add(new Metro("Новогиреево", new Coordinate((double) 37.817295, (double) 55.751675), false));
				metro.add(new Metro("Новокосино", new Coordinate((double) 37.864052, (double) 55.745113), false));
				metro.add(new Metro("Новокузнецкая", new Coordinate((double) 37.629125, (double) 55.742276), false));
				metro.add(new Metro("Новослободская", new Coordinate((double) 37.601421, (double) 55.779565), false));
				metro.add(new Metro("Новоясеневская", new Coordinate((double) 37.553442, (double) 55.601833), false));
				metro.add(new Metro("Новые Черёмушки", new Coordinate((double) 37.554493, (double) 55.670077), true));
				metro.add(new Metro("Октябрьская", new Coordinate((double) 37.612766, (double) 55.731257), false));
				metro.add(new Metro("Октябрьская", new Coordinate((double) 37.610979, (double) 55.729255), false));
				metro.add(new Metro("Октябрьское поле", new Coordinate((double) 37.493317, (double) 55.793581), true));
				metro.add(new Metro("Орехово", new Coordinate((double) 37.695214, (double) 55.612690), false));
				metro.add(new Metro("Отрадное", new Coordinate((double) 37.604843, (double) 55.863384), true));
				metro.add(new Metro("Охотный ряд", new Coordinate((double) 37.615327, (double) 55.756523), false));
				metro.add(new Metro("Павелецкая", new Coordinate((double) 37.636329, (double) 55.731536), false));
				metro.add(new Metro("Павелецкая", new Coordinate((double) 37.638961, (double) 55.729787), false));
				metro.add(new Metro("Парк культуры", new Coordinate((double) 37.595061, (double) 55.736077), false));
				metro.add(new Metro("Парк культуры", new Coordinate((double) 37.592905, (double) 55.735150), false));
				metro.add(new Metro("Парк Победы", new Coordinate((double) 37.516925, (double) 55.736164), false));
				metro.add(new Metro("Парк Победы", new Coordinate((double) 37.514401, (double) 55.736478), true));
				metro.add(new Metro("Партизанская", new Coordinate((double) 37.749265, (double) 55.788424), true));
				metro.add(new Metro("Первомайская", new Coordinate((double) 37.799364, (double) 55.794376), true));
				metro.add(new Metro("Перово", new Coordinate((double) 37.786887, (double) 55.751320), false));
				metro.add(new Metro("Петровско//Разумовская", new Coordinate((double) 37.575558, (double) 55.836524), true));
				metro.add(new Metro("Печатники", new Coordinate((double) 37.728398, (double) 55.692972), false));
				metro.add(new Metro("Пионерская", new Coordinate((double) 37.467078, (double) 55.735986), true));
				metro.add(new Metro("Планерная", new Coordinate((double) 37.436382, (double) 55.860529), true));
				metro.add(new Metro("Площадь Ильича", new Coordinate((double) 37.680589, (double) 55.747024), false));
				metro.add(new Metro("Площадь Революции", new Coordinate((double) 37.622360, (double) 55.756741), false));
				metro.add(new Metro("Полежаевская", new Coordinate((double) 37.517895, (double) 55.777201), true));
				metro.add(new Metro("Полянка", new Coordinate((double) 37.618471, (double) 55.736807), false));
				metro.add(new Metro("Пражская", new Coordinate((double) 37.603972, (double) 55.611577), false));
				metro.add(new Metro("Преображенская площадь", new Coordinate((double) 37.715022, (double) 55.796167), true));
				metro.add(new Metro("Пролетарская", new Coordinate((double) 37.666917, (double) 55.731546), false));
				metro.add(new Metro("Проспект Вернадского", new Coordinate((double) 37.505831, (double) 55.676910), true));
				metro.add(new Metro("Проспект Мира", new Coordinate((double) 37.633482, (double) 55.781757), false));
				metro.add(new Metro("Проспект Мира", new Coordinate((double) 37.633464, (double) 55.779631), false));
				metro.add(new Metro("Профсоюзная", new Coordinate((double) 37.562595, (double) 55.677671), true));
				metro.add(new Metro("Пушкинская", new Coordinate((double) 37.603900, (double) 55.765747), false));
				metro.add(new Metro("Пятницкое шоссе", new Coordinate((double) 37.354025, (double) 55.855644), false));
				metro.add(new Metro("Речной вокзал", new Coordinate((double) 37.476231, (double) 55.854891), true));
				metro.add(new Metro("Рижская", new Coordinate((double) 37.636123, (double) 55.792513), true));
				metro.add(new Metro("Римская", new Coordinate((double) 37.681254, (double) 55.746228), false));
				metro.add(new Metro("Рязанский проспект", new Coordinate((double) 37.793606, (double) 55.717366), false));
				metro.add(new Metro("Савёловская", new Coordinate((double) 37.588296, (double) 55.793313), true));
				metro.add(new Metro("Свиблово", new Coordinate((double) 37.653379, (double) 55.855558), true));
				metro.add(new Metro("Севастопольская", new Coordinate((double) 37.598384, (double) 55.651552), true));
				metro.add(new Metro("Семёновская", new Coordinate((double) 37.719423, (double) 55.783195), true));
				metro.add(new Metro("Серпуховская", new Coordinate((double) 37.625199, (double) 55.726680), false));
				metro.add(new Metro("Славянский бульвар", new Coordinate((double) 37.472171, (double) 55.729828), true));
				metro.add(new Metro("Смоленская", new Coordinate((double) 37.581658, (double) 55.749060), false));
				metro.add(new Metro("Смоленская", new Coordinate((double) 37.583841, (double) 55.747642), false));
				metro.add(new Metro("Сокол", new Coordinate((double) 37.514787, (double) 55.805042), true));
				metro.add(new Metro("Сокольники", new Coordinate((double) 37.679700, (double) 55.789198), true));
				metro.add(new Metro("Спартак", new Coordinate((double) 37.434801, (double) 55.817234), true));
				metro.add(new Metro("Спортивная", new Coordinate((double) 37.562227, (double) 55.722761), false));
				metro.add(new Metro("Сретенский бульвар", new Coordinate((double) 37.636374, (double) 55.766299), false));
				metro.add(new Metro("Строгино", new Coordinate((double) 37.403118, (double) 55.803691), true));
				metro.add(new Metro("Студенческая", new Coordinate((double) 37.548375, (double) 55.738784), false));
				metro.add(new Metro("Сухаревская", new Coordinate((double) 37.632332, (double) 55.772315), false));
				metro.add(new Metro("Сходненская", new Coordinate((double) 37.439787, (double) 55.850510), true));
				metro.add(new Metro("Таганская", new Coordinate((double) 37.653146, (double) 55.742433), false));
				metro.add(new Metro("Таганская", new Coordinate((double) 37.653613, (double) 55.739199), false));
				metro.add(new Metro("Тверская", new Coordinate((double) 37.605939, (double) 55.764455), false));
				metro.add(new Metro("Театральная", new Coordinate((double) 37.617680, (double) 55.758808), false));
				metro.add(new Metro("Текстильщики", new Coordinate((double) 37.732117, (double) 55.709211), false));
				metro.add(new Metro("Тёплый Стан", new Coordinate((double) 37.505912, (double) 55.618730), false));
				metro.add(new Metro("Тимирязевская", new Coordinate((double) 37.574498, (double) 55.818660), true));
				metro.add(new Metro("Третьяковская", new Coordinate((double) 37.626142, (double) 55.741125), false));
				metro.add(new Metro("Третьяковская", new Coordinate((double) 37.625981, (double) 55.740319), false));
				metro.add(new Metro("Трубная", new Coordinate((double) 37.621902, (double) 55.767939), false));
				metro.add(new Metro("Тропарёво", new Coordinate((double) 37.4725, (double) 55.6459), true));
				metro.add(new Metro("Тульская", new Coordinate((double) 37.622612, (double) 55.708841), false));
				metro.add(new Metro("Тургеневская", new Coordinate((double) 37.636742, (double) 55.765276), false));
				metro.add(new Metro("Тушинская", new Coordinate((double) 37.437604, (double) 55.827080), true));
				metro.add(new Metro("Улица 1905 года", new Coordinate((double) 37.561419, (double) 55.764273), false));
				metro.add(new Metro("Улица академика Янгеля", new Coordinate((double) 37.600675, (double) 55.595883), false));
				metro.add(new Metro("Улица Горчакова", new Coordinate((double) 37.531226, (double) 55.541825), false));
				metro.add(new Metro("Улица Скобелевская", new Coordinate((double) 37.554618, (double) 55.548034), false));
				metro.add(new Metro("Улица Старокачаловская", new Coordinate((double) 37.576708, (double) 55.568838), false));
				metro.add(new Metro("Университет", new Coordinate((double) 37.534532, (double) 55.692440), true));
				metro.add(new Metro("Филёвский парк", new Coordinate((double) 37.483328, (double) 55.739519), true));
				metro.add(new Metro("Фили", new Coordinate((double) 37.514949, (double) 55.745970), true));
				metro.add(new Metro("Фрунзенская", new Coordinate((double) 37.580328, (double) 55.727232), false));
				metro.add(new Metro("Царицыно", new Coordinate((double) 37.669612, (double) 55.620982), true));
				metro.add(new Metro("Цветной бульвар", new Coordinate((double) 37.620986, (double) 55.771616), false));
				metro.add(new Metro("Черкизовская", new Coordinate((double) 37.744819, (double) 55.802988), true));
				metro.add(new Metro("Чертановская", new Coordinate((double) 37.606065, (double) 55.640538), true));
				metro.add(new Metro("Чеховская", new Coordinate((double) 37.608167, (double) 55.765843), false));
				metro.add(new Metro("Чистые пруды", new Coordinate((double) 37.638683, (double) 55.764794), false));
				metro.add(new Metro("Чкаловская", new Coordinate((double) 37.659263, (double) 55.755930), false));
				metro.add(new Metro("Шаболовская", new Coordinate((double) 37.607799, (double) 55.718821), false));
				metro.add(new Metro("Шипиловская", new Coordinate((double) 37.743723, (double) 55.620982), false));
				metro.add(new Metro("Шоссе Энтузиастов", new Coordinate((double) 37.751583, (double) 55.758255), true));
				metro.add(new Metro("Щёлковская", new Coordinate((double) 37.798556, (double) 55.810228), false));
				metro.add(new Metro("Щукинская", new Coordinate((double) 37.463772, (double) 55.808827), true));
				metro.add(new Metro("Электрозаводская", new Coordinate((double) 37.705284, (double) 55.782066), true));
				metro.add(new Metro("Югозападная", new Coordinate((double) 37.482852, (double) 55.663146), true));
				metro.add(new Metro("Южная", new Coordinate((double) 37.609047, (double) 55.622436), true));
				metro.add(new Metro("Ясенево", new Coordinate((double) 37.533400, (double) 55.606182), false));

				DecimalFormat dff;
		        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
		        otherSymbols.setDecimalSeparator('.');

				dff = new DecimalFormat("##.######", otherSymbols);
		    	dff.setRoundingMode(RoundingMode.DOWN);

				LinkedList<Shape> metroShapes = new LinkedList<Shape>();
				for (Metro metroCoordinate : metro) {
					if (metroCoordinate.getIsActiveForSearch()) {
						String metroShape = "";
						String comma = "";
						Log.i("CianTask", "JSON: metro search point " + dff.format(metroCoordinate.getMetro().getDoubleLon())+","+dff.format(metroCoordinate.getMetro().getDoubleLat()));
	
						for (Integer i=0; i < 360; i += 360 / 15) {
							Coordinate radiusCoordinate = calcEndPoint(metroCoordinate.getMetro(), 1200, i);
	
	
							metroShape += comma + dff.format(radiusCoordinate.getDoubleLat()) + "_" + dff.format(radiusCoordinate.getDoubleLon());
							comma = ",";
						}
						metroShapes.add(new Shape(metroShape, metroCoordinate.getName(), metroCoordinate.getMetro()));
					}
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
		 public static float distFrom(float lat1, float lng1, float lat2, float lng2) {
			    double earthRadius = 6371000; //meters
			    double dLat = Math.toRadians(lat2-lat1);
			    double dLng = Math.toRadians(lng2-lng1);
			    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
			               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
			               Math.sin(dLng/2) * Math.sin(dLng/2);
			    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
			    float dist = (float) (earthRadius * c);

			    return dist;
		}

		 public String getClossestStation(String position){
				DecimalFormat dff;
		        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
		        otherSymbols.setDecimalSeparator('.');

				dff = new DecimalFormat("##.######", otherSymbols);
		    	dff.setRoundingMode(RoundingMode.DOWN);

				String positionUri  = position.replace(" ", ",");
		    	
		    	Pattern downloadPattern = Pattern.compile("([\\d\\.]+) ([\\d\\.]+)");
		    	Matcher downloadMatcher = downloadPattern.matcher(position);
		    	
	    	    Float findDestantion = null;
	    	    String findMetro = "";
		    	
		    	 
		    	if (downloadMatcher.find()){
		    		String flatLat = downloadMatcher.group(1);
		    	    String flatLng = downloadMatcher.group(2);
			    	for (Metro metro: this.metro) {
		            	float destantion = distFrom(Float.parseFloat(flatLat), Float.parseFloat(flatLng),(float) metro.getMetro().getDoubleLat(),(float) metro.getMetro().getDoubleLon());
		            	if (findDestantion == null || destantion < findDestantion) {
		            		findDestantion = destantion;
		            		findMetro      = metro.getName();
		            	}
				
				        Log.i("CianTask", "JSON: destantion " + destantion);
				
			    	}
		    	}
					    
			
		    	DecimalFormat df = new DecimalFormat("##");
		    	df.setRoundingMode(RoundingMode.DOWN);

		    	Log.i("CianTask", "JSON: metro  " + findMetro + " (" + df.format(findDestantion) + "m)");

		    	return "".equals(findMetro) ? "Метро не найдено" : (findMetro + " (" + df.format(findDestantion) + "m)");
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
