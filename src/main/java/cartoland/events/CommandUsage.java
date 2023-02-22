package cartoland.events;

import cartoland.events.commands.*;
import cartoland.mini_games.IMiniGame;
import cartoland.utilities.FileHandle;
import cartoland.utilities.IDAndEntities;
import cartoland.utilities.JsonHandle;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * {@code CommandUsage} is a listener that triggers when a user uses slash command. This class was registered in
 * {@link cartoland.Cartoland#main}, with the build of JDA.
 *
 * @since 1.0
 * @author Alex Cai
 */
public class CommandUsage extends ListenerAdapter
{
	/**
	 * The key of this map is the name of a command, and the value is the execution.
	 */
	private final HashMap<String, ICommand> commands = new HashMap<>();
	/**
	 * The key of this map is the name of a game, and the value is the actual game.
	 */
	private final HashMap<Long, IMiniGame> games = new HashMap<>();
	public HashMap<Long, IMiniGame> getGames()
	{
		return games;
	}

	/**
	 * Last user uses slash command.
	 */
	private long userID;
	public long getUserID()
	{
		return userID;
	}

	/**
	 * 356 images about Megumin.
	 */
	private final TA[] twitterAuthors = {new TA("_mexif",1375088851446755332L),new TA("_Mizugasi",1444998923739824131L),new TA("_Rivia_",1370370105696161794L),new TA("_ukoyr",1544612178556121088L),new TA("024NISHI024",1396492351346860038L),new TA("1G9Xw69kePSLI6p",1420731295156477954L),new TA("3CDtvmmpuqKhl7J",1471803818048634884L),new TA("6gcYq8nXYHQwui5",1332594611156500487L),new TA("7egTXS0GKWh3wGP",1334542548497387521L,1342110371662888960L,1346091304124497920L,1416720114066415617L,1455918876990996489L,1485297876947566594L,1527310236247560193L,1533389156755603456L,1557015010668609536L,1595465406420221952L,1606956350596317185L),new TA("9aBGoLUanvcjd7O",1599205425127501824L),new TA("ahmadjendro",1488530273373683713L),new TA("Aloe_04maho",1342795265724436481L,1360906615008886784L,1403669632108564485L,1418163778487652354L,1423601843368730625L,1445719564185075722L,1573801227376214017L),new TA("AONAGINEMURI",1390290873951285248L),new TA("askr_otr",1468201641723965447L),new TA("atasom",1343169561680519169L),new TA("az210309",1383254021201297414L,1391411800420151310L),new TA("bhive003",1382662692729618434L,1421734667326132225L,1506027874825895938L,1576769547117293572L,1588833560546676737L),new TA("BNC_0116",1120180446392807424L,1123935283995713543L,1128289624126738432L,1129402831587274752L,1211311585571917825L,1228335403221966849L,1353732864177623040L),new TA("cachi_lo",1423468322893357056L),new TA("catharpoon",1395598782675357698L,1428436939015741440L),new TA("chakichaki33",1529055947767050240L,1533765708513148928L),new TA("CHILLY_karon",1378395239845482499L),new TA("Coconatnuts09",1449003566610325504L),new TA("creates_blue",1389692031606267904L,1393257386287144963L,1394877336516907010L,1400192682480136194L),new TA("D2gg1cPdO8bfjFS",1333391702132867073L,1342283498783002626L,1256453504299757569L),new TA("dai_gazacy",1333072335717289986L),new TA("DlrfMimikyu",1352830077696962560L,1362948691376619524L,1400129807925628933L),new TA("E_GA_KU",1596505160175747072L),new TA("Edacchi73",1487748613908615182L),new TA("EJAMIARTSTUDIO",1370264344903045120L),new TA("Fagi_Kakikaki",1416334656371269634L,1558410423837523970L),new TA("FevJF0EICCKnLlD",1347026153098211331L),new TA("gakkari_orz",1388805801859452928L,1538592731962179587L),new TA("Gasolineillust",1565325467850518529L),new TA("gazercide",1373256680763396106L),new TA("GReeeeNmodoki2",1599057455702765568L),new TA("GuRa_fgo",1348538564900917248L,1352926702398185473L),new TA("HagGy0327_",1338114471143739392L),new TA("hagi_neco",1393868573106327563L,1429373932159660032L),new TA("hapycloud",1343423407442948101L),new TA("harehareota",1598270723747098624L),new TA("Harimax8",1383359465366323209L,1422891821772906500L,1538112903555215360L,1596347482916950018L),new TA("hatuganookome",1344206596310761472L),new TA("he_shan123",1557726596202606595L),new TA("HereticxxA",1398405163677671425L),new TA("Hi_me_520So",1399677759698337797L),new TA("hwa_n01",1353356379889700864L),new TA("icetea_art",1528012740656345088L,1545769743151353856L,1553379890128306177L,1555916614138150912L,1573673618172624896L,1593967339036450820L),new TA("isonnoha",1504494884765396994L),new TA("Ixy",1334516599760490498L,1353673446719250433L,1391319694859137024L,1417105811650727936L,1599059007498772480L),new TA("J02GGOgpQ6dueGu",1336638090828341250L,1336868237141704705L,1345199258220126216L,1345279854040649728L,1352167601951002624L,1355069629840855042L,1358677301731368964L,1367304489468796932L,1378316365128331264L,1380295158676090880L,1467024737448886274L),new TA("JackRockhardt",1421495091001135106L),new TA("JAM_pentail",1374333181684375556L,1379418416046477312L,1496121346824232966L),new TA("jirafuru1",1385345798372741120L),new TA("kadokawasneaker",1463416144765751300L,1463709023455399936L),new TA("kaiiruuu",1435533706161102852L),new TA("kamindani",1364578421406199822L),new TA("karst_bunny",1487062233343426561L,1502885083614973952L,1577615125720485888L),new TA("kasumiM224",1408089488358723585L,1530582872184483840L),new TA("kgs_jf",1459384335799562241L),new TA("kisasageDrawing",1571040020219428865L),new TA("KMTM_kmk0819",1342130873785745411L,1356814844792504321L,1388147012881707009L,1400465949963145226L,1446105335723298817L,1573212323644575744L,1606986710520565761L,1614200785335623682L),new TA("kosame_H2O",1367079770870046720L),new TA("kuragen000",1464202279897104391L),new TA("kuro_gbp02",1458399016023060481L,1606632160756396032L),new TA("kyo_k",1357357057213538309L),new TA("LiveTM_08",1484269503563264000L),new TA("lkpk_g",1333706453182341120L,1334414009840226305L,1394177004967108608L,1429742364428431367L),new TA("ma2_Matsu",1334957384381288448L),new TA("Mahdi_011",1401149547724820482L),new TA("maika_82",1401554662130556928L),new TA("MegaMouseArts",1370182918258184192L),new TA("mikazuki_akira",1379289441450139654L),new TA("mishima_kurone",1451479334636572676L,1456979191736377344L),new TA("mm_pentab",1403371578327404544L),new TA("mnk_nk1414",1381215456741138434L),new TA("mokuka_noe",1363396372309901313L),new TA("MP26player",1335131654415818753L),new TA("Mr_Tangsuyuk",1576950800261095424L),new TA("MyungYi_",1346389393750204416L,1367020232334663683L),new TA("namahyou",1441056569236463637L),new TA("Namakura_noelle",1408435172224114691L),new TA("nasubisamurai15",1524090497249075206L),new TA("natu_7273",1569990932568891392L),new TA("nemuhosi",1355473834326061062L),new TA("NNPS_KM_SONYA",1346410525853245445L),new TA("NohikariAi",1523465539678785536L),new TA("noir2more",1334513715660644355L),new TA("nomoregrass417",1526173684355870720L),new TA("noneru_pix",1349470312648290307L),new TA("nut_megu",1336584872471584768L,1338774453711355904L,1340946345096450049L,1345658481076887553L,1348193043229278208L,1363775871711014919L,1380808129676333064L,1401107716651638786L,1412336326108610563L,1417047602130993154L,1418859623973589000L,1436616211828129798L,1504421763479920640L,1507644448079253508L,1540621318374162433L),new TA("ogipote",712966825663795202L),new TA("omurice4684",1334513374726643715L),new TA("OnsenSyndicate",1401606597584068617L),new TA("Osuzu_botan",1458708798562918400L),new TA("pensukeo",888014816266211329L,895656010974502912L,914382762978680832L,925014913285734402L,942065366179627008L,1038481866993500163L,1068471996369915906L,1099923406571528193L,1105799480396279809L,1113333584654266368L,1123238336359886849L,1123913549934473216L,1127271844384395264L,1129119914638233600L,1129334190695100416L,1130947782209024000L,1132349932705214465L,1172969592458600448L,1184557949714219009L,1271368885682319360L,1334513603865686018L,1345392705837309952L,1400214535080878081L),new TA("Pictolita",1455005915992584196L),new TA("ptrtear",1413824587935010820L),new TA("ranfptn",1355848490383855617L),new TA("ronndomizukami",1468855768367898625L),new TA("rouka_2",1331916310100336645L),new TA("rouka0101",1377561442694488064L),new TA("Ruroi31",1416330421525041152L),new TA("Sabcoo",1354433463043022849L),new TA("sannshi_34",1334743269582938112L,1416979268928634880L),new TA("senrihinZK",1526404539121037312L),new TA("shisui0178",1380913346589822978L),new TA("shONe_Banana",1487825453427863553L),new TA("shouu_kyun",1452032402742669313L),new TA("shoyu_Sara",1333748893239046144L,1334837150714527745L,1350413371363848196L,1358023126894825472L,1363817144018866178L,1365634168839790601L,1365995755194880003L,1369256816794771456L,1382302645726027782L,1383375535967903749L,1388463621164859395L,1426514140554698753L,1434109889056428034L,1449698306620227589L,1454041990329352192L,1459477324437458946L,1466361693324210183L,1475441506437988352L,1482306755447050241L,1487746415602577409L,1497902031813890058L,1523274342465175553L,1535957094251507712L,1546102725196550144L,1586696036126711809L,1593937896784166912L,1600838962406625280L,1606620722864214017L,1606981310349967366L),new TA("sozoshu_kyo",1385928402499108866L),new TA("suke_yuno",1334837425667969024L,1342076712587206658L,1344635807965974528L,1348240561505853442L,1356574457360310273L,1359838927914373130L,1260533099361787905L,1267778380994236416L,1277202674916589570L,1360925354773241857L,1363822716260782080L,1378677005764292611L,1380491541450424320L,1384431678454521860L,1390275707347951621L,1401870084285493252L,1406218302515384322L,1416757349046382592L,1418673457491943428L,1422169100533460995L,1424335734085853193L,1432310655512506386L,1443910414455107588L,1454782046337781770L,1455867948883529732L,1460578197477552133L,1474349388307005448L,1476897920071114759L,1480872741784125441L,1493194450688159746L,1496087004211453960L,1521823893857931264L,1547189145818525696L,1558054431153082368L,1559510936616321024L,1565673703215886337L,1577266429568901124L,1583789431949230081L,1594666279511945216L,1602273539247476738L,1605171507097145344L,1609197162726055936L,1613869444387983362L),new TA("sugarwhite1046",1437002711405268993L),new TA("syu_an_n",1478490774585376768L),new TA("Takumi_ha_DX",1378299101071085569L,1380122050883293189L,1382673881903689730L),new TA("tkd14059560",1380758914401099777L),new TA("tmzr_ovo_",1373556726469775363L),new TA("toukan_drawing",1373572772983574537L,1424305682589708293L,1507656088103297026L),new TA("toketa15",1360559485664677889L),new TA("tQg_07",1460934239822696456L),new TA("TsutaKaede",1528329828012785664L,1541753284070965249L,1618230537969364996L),new TA("tUWU284MlWaU7VA",1367024008223354880L),new TA("ud864",1427601524688003072L),new TA("Usa4gi",1438652486806425603L),new TA("Vi3q1ahbJM31goT",1345234835741188105L),new TA("wa_ki_ya_ku",1360906994673016833L,1372881405705744384L,1387723681833447427L,1394971615864270850L,1406206671571984384L,1422886552280072195L,1424383682442268683L,1435205520265121793L,1448264639385509892L,1495134351272529926L,1599242577865478145L,1610423761794379776L),new TA("wumalutsufuri",1461324636524728323L,1461695108055658496L,1463503094977687554L,1464586977114607616L,1465279326392176641L,1474425440760897536L,1474737540821561345L,1493204800414322689L,1505947094192095235L,1514219512388026373L,1532330229880205312L,1559492213809496064L,1607322393818849281L),new TA("XaJgt7S9FkzWCiy",1553715061713039360L),new TA("yachiyo_naga",1422834901066207237L),new TA("yano_t",1378351221283594241L),new TA("Yansae81",1334864422934757376L,1345655525795512321L,1348915439527620608L,1467394004971323393L),new TA("yukimaru_sgk",1334992143715201025L),new TA("yuzufu_1",1544305092333129728L),new TA("zyu90gg",1334575930908397568L,1418913957067194368L,1423605503146426376L)};

	private final Random random = new Random();

	/**
	 * Put every command and their execution into {@link #commands}.
	 */
	public CommandUsage()
	{
		ICommand alias;

		//初始化map 放入所有指令
		//invite
		commands.put("invite", event -> event.reply("https://discord.gg/UMYxwHyRNE").queue());

		//help
		commands.put("help", event -> event.reply(minecraftCommandRelated("help", event)).queue());

		//cmd
		alias = event -> event.reply(minecraftCommandRelated("cmd", event)).queue();
		commands.put("cmd", alias);
		commands.put("mcc", alias);
		commands.put("command", alias);

		//faq
		alias = event -> event.reply(minecraftCommandRelated("faq", event)).queue();
		commands.put("faq", alias);
		commands.put("question", alias);

		//dtp
		alias = event -> event.reply(minecraftCommandRelated("dtp", event)).queue();
		commands.put("dtp", alias);
		commands.put("datapack", alias);

		//lang
		alias = event -> event.reply(minecraftCommandRelated("lang", event)).queue();
		commands.put("lang", alias);
		commands.put("language", alias);

		commands.put("megumin", event ->
		{
			TA author = twitterAuthors[random.nextInt(twitterAuthors.length)];
			long artwork = author.artworks()[random.nextInt(author.artworks().length)];
			event.reply("https://twitter.com/" + author.name() + "/status/" + artwork).queue();
		}); //隨機一張惠惠

		//shutdown
		commands.put("shutdown", event ->
		{
			if (userID != IDAndEntities.AC_ID) //不是我
			{
				event.reply("You can't do that.").queue();
				return;
			}

			event.reply("Shutting down...").queue(interactionHook ->
			{
				IDAndEntities.jda.shutdown();
				IDAndEntities.botChannel.sendMessage("Cartoland Bot is now offline.").queue();
			});
		});

		//reload
		commands.put("reload", event ->
		{
			if (userID != IDAndEntities.AC_ID) //不是我
			{
				event.reply("You can't do that.").queue();
				return;
			}

			event.reply("reloading...").queue(interactionHook -> JsonHandle.reloadLanguageFiles());
		});

		//one_a_two_b
		commands.put("one_a_two_b", new OneATwoBCommand(this));

		//lottery
		commands.put("lottery", new LotteryCommand(this));

		//tool
		commands.put("tool", new ToolCommand());
	}

	/**
	 * The method that inherited from {@link ListenerAdapter}, triggers when a user uses slash command.
	 *
	 * @param event The event that carries information of the user and the command.
	 * @since 1.0
	 * @author Alex Cai
	 */
	@Override
	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event)
	{
		User user = event.getUser();
		userID = user.getIdLong();
		String commandName = event.getName();
		String subCommandName = event.getSubcommandName();
		FileHandle.log(user.getName() + "(" + userID + ") used /" + commandName +
							   (subCommandName != null ? " " + subCommandName + " " : " ") +
							   event.getOptions().stream().map(OptionMapping::getName).collect(Collectors.joining(" ")));
		if (commands.containsKey(commandName))
			commands.get(commandName).commandProcess(event);
	}

	/**
	 * When it comes to /help, /cmd, /faq, /dtp and /lang that needs to use lang/*.json files, those lambda
	 * expressions will call this method.
	 *
	 * @param jsonKey the command name, only "help", "cmd", "faq", "dtp" and "lang" are allowed.
	 * @param event The event that carries information of the user and the command.
	 * @return The content that the bot is going to reply the user.
	 * @since 1.0
	 * @author Alex Cai
	 */
	private String minecraftCommandRelated(String jsonKey, SlashCommandInteractionEvent event)
	{
		String argument = event.getOption(jsonKey + "_name", OptionMapping::getAsString); //獲得參數
		if (argument == null) //沒有參數
			return JsonHandle.command(userID, jsonKey); //儘管/lang的參數是必須的 但為了方便還是讓他用這個方法處理
		return JsonHandle.command(userID, jsonKey, argument);
	}
}

/**
 * {@code TA} refers to TwitterAuthor, it stores the name of a Twitter author and his/her artworks ID.
 *
 * @since 1.3
 * @author Alex Cai
 */
record TA(String name, long... artworks) {}