package xyz.holocons.mc.holoitemsrevamp.collection;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.strangeone101.holoitemsapi.item.CustomItem;

import xyz.holocons.mc.holoitemsrevamp.HoloItemsRevamp;
import xyz.holocons.mc.holoitemsrevamp.collection.en1.EN1Collection;
import xyz.holocons.mc.holoitemsrevamp.collection.en1.GawrGura;
import xyz.holocons.mc.holoitemsrevamp.collection.en1.IRyS;
import xyz.holocons.mc.holoitemsrevamp.collection.en1.MoriCalliope;
import xyz.holocons.mc.holoitemsrevamp.collection.en1.NinomaeInanis;
import xyz.holocons.mc.holoitemsrevamp.collection.en1.TakanashiKiara;
import xyz.holocons.mc.holoitemsrevamp.collection.en1.WatsonAmelia;
import xyz.holocons.mc.holoitemsrevamp.collection.en2.CeresFauna;
import xyz.holocons.mc.holoitemsrevamp.collection.en2.EN2Collection;
import xyz.holocons.mc.holoitemsrevamp.collection.en2.HakosBaelz;
import xyz.holocons.mc.holoitemsrevamp.collection.en2.NanashiMumei;
import xyz.holocons.mc.holoitemsrevamp.collection.en2.OuroKronii;
import xyz.holocons.mc.holoitemsrevamp.collection.en2.TsukumoSana;
import xyz.holocons.mc.holoitemsrevamp.collection.gamers.GamersCollection;
import xyz.holocons.mc.holoitemsrevamp.collection.gamers.InugamiKorone;
import xyz.holocons.mc.holoitemsrevamp.collection.gamers.NekomataOkayu;
import xyz.holocons.mc.holoitemsrevamp.collection.gamers.OokamiMio;
import xyz.holocons.mc.holoitemsrevamp.collection.gen0.AZKi;
import xyz.holocons.mc.holoitemsrevamp.collection.gen0.Gen0Collection;
import xyz.holocons.mc.holoitemsrevamp.collection.gen0.HoshimachiSuisei;
import xyz.holocons.mc.holoitemsrevamp.collection.gen0.Roboco;
import xyz.holocons.mc.holoitemsrevamp.collection.gen0.SakuraMiko;
import xyz.holocons.mc.holoitemsrevamp.collection.gen0.TokinoSora;
import xyz.holocons.mc.holoitemsrevamp.collection.gen1.AkaiHaato;
import xyz.holocons.mc.holoitemsrevamp.collection.gen1.AkiRosenthal;
import xyz.holocons.mc.holoitemsrevamp.collection.gen1.Gen1Collection;
import xyz.holocons.mc.holoitemsrevamp.collection.gen1.NatsuiroMatsuri;
import xyz.holocons.mc.holoitemsrevamp.collection.gen1.ShirakamiFubuki;
import xyz.holocons.mc.holoitemsrevamp.collection.gen1.YozoraMel;
import xyz.holocons.mc.holoitemsrevamp.collection.gen2.Gen2Collection;
import xyz.holocons.mc.holoitemsrevamp.collection.gen2.MinatoAqua;
import xyz.holocons.mc.holoitemsrevamp.collection.gen2.MurasakiShion;
import xyz.holocons.mc.holoitemsrevamp.collection.gen2.NakiriAyame;
import xyz.holocons.mc.holoitemsrevamp.collection.gen2.OozoraSubaru;
import xyz.holocons.mc.holoitemsrevamp.collection.gen2.YuzukiChoco;
import xyz.holocons.mc.holoitemsrevamp.collection.gen3.Gen3Collection;
import xyz.holocons.mc.holoitemsrevamp.collection.gen3.HoushouMarine;
import xyz.holocons.mc.holoitemsrevamp.collection.gen3.ShiranuiFlare;
import xyz.holocons.mc.holoitemsrevamp.collection.gen3.ShiroganeNoel;
import xyz.holocons.mc.holoitemsrevamp.collection.gen3.UruhaRushia;
import xyz.holocons.mc.holoitemsrevamp.collection.gen3.UsadaPekora;
import xyz.holocons.mc.holoitemsrevamp.collection.gen4.AmaneKanata;
import xyz.holocons.mc.holoitemsrevamp.collection.gen4.Gen4Collection;
import xyz.holocons.mc.holoitemsrevamp.collection.gen4.HimemoriLuna;
import xyz.holocons.mc.holoitemsrevamp.collection.gen4.KiryuCoco;
import xyz.holocons.mc.holoitemsrevamp.collection.gen4.TokoyamiTowa;
import xyz.holocons.mc.holoitemsrevamp.collection.gen4.TsunomakiWatame;
import xyz.holocons.mc.holoitemsrevamp.collection.gen5.Gen5Collection;
import xyz.holocons.mc.holoitemsrevamp.collection.gen5.MomosuzuNene;
import xyz.holocons.mc.holoitemsrevamp.collection.gen5.OmaruPolka;
import xyz.holocons.mc.holoitemsrevamp.collection.gen5.ShishiroBotan;
import xyz.holocons.mc.holoitemsrevamp.collection.gen5.YukihanaLamy;
import xyz.holocons.mc.holoitemsrevamp.collection.gen6.Gen6Collection;
import xyz.holocons.mc.holoitemsrevamp.collection.gen6.HakuiKoyori;
import xyz.holocons.mc.holoitemsrevamp.collection.gen6.KazamaIroha;
import xyz.holocons.mc.holoitemsrevamp.collection.gen6.LaplusDarknesss;
import xyz.holocons.mc.holoitemsrevamp.collection.gen6.SakamataChloe;
import xyz.holocons.mc.holoitemsrevamp.collection.gen6.TakaneLui;
import xyz.holocons.mc.holoitemsrevamp.collection.id1.AiraniIofifteen;
import xyz.holocons.mc.holoitemsrevamp.collection.id1.AyundaRisu;
import xyz.holocons.mc.holoitemsrevamp.collection.id1.ID1Collection;
import xyz.holocons.mc.holoitemsrevamp.collection.id1.MoonaHoshinova;
import xyz.holocons.mc.holoitemsrevamp.collection.id2.AnyaMelfissa;
import xyz.holocons.mc.holoitemsrevamp.collection.id2.ID2Collection;
import xyz.holocons.mc.holoitemsrevamp.collection.id2.KureijiOllie;
import xyz.holocons.mc.holoitemsrevamp.collection.id2.PavoliaReine;
import xyz.holocons.mc.holoitemsrevamp.collection.misc.Achan;
import xyz.holocons.mc.holoitemsrevamp.collection.misc.MiscCollection;
import xyz.holocons.mc.holoitemsrevamp.collection.stars1.Arurandeisu;
import xyz.holocons.mc.holoitemsrevamp.collection.stars1.HanasakiMiyabi;
import xyz.holocons.mc.holoitemsrevamp.collection.stars1.KanadeIzuru;
import xyz.holocons.mc.holoitemsrevamp.collection.stars1.Rikka;
import xyz.holocons.mc.holoitemsrevamp.collection.stars1.Stars1Collection;
import xyz.holocons.mc.holoitemsrevamp.collection.stars2.AstelLeda;
import xyz.holocons.mc.holoitemsrevamp.collection.stars2.KishidoTemma;
import xyz.holocons.mc.holoitemsrevamp.collection.stars2.Stars2Collection;
import xyz.holocons.mc.holoitemsrevamp.collection.stars2.YukokuRoberu;
import xyz.holocons.mc.holoitemsrevamp.collection.stars3.AragamiOga;
import xyz.holocons.mc.holoitemsrevamp.collection.stars3.KageyamaShien;
import xyz.holocons.mc.holoitemsrevamp.collection.stars3.Stars3Collection;
import xyz.holocons.mc.holoitemsrevamp.item.MagnetBook;
import xyz.holocons.mc.holoitemsrevamp.item.MementoItem;
import xyz.holocons.mc.holoitemsrevamp.item.TideRiderItem;
import xyz.holocons.mc.holoitemsrevamp.item.UnlimitedKrisWorksItem;

public class CollectionManager {

    private final List<IdolCollection> idolCollections;
    private final Map<String, CustomItem> customItems;

    public CollectionManager(HoloItemsRevamp plugin) {
        this.idolCollections = buildIdolCollections(plugin);

        // Creates a map from the list of idols, key is the internal name, value is the initialized class.
        this.customItems = idolCollections.stream()
            .flatMap(i -> i.getAllItem().stream())
            .collect(Collectors.toMap(CustomItem::getInternalName, Function.identity()));
    }

    /**
     * Gets a list of all generations
     * @return a list
     */
    public List<IdolCollection> getAllGens() {
        return idolCollections;
    }

    /**
     * Gets a map that contains all custom items as its values, and their internal name as the key. Used for
     * autocompletion
     * @return All custom items that the plugin contains
     */
    public Map<String, CustomItem> getAllItems() {
        return customItems;
    }

    private static List<IdolCollection> buildIdolCollections(HoloItemsRevamp plugin) {
        var gura = new GawrGura(
            new TideRiderItem(plugin)
        );
        var irys = new IRyS();
        var calliope = new MoriCalliope(
            new MementoItem(plugin)
        );
        var ina = new NinomaeInanis();
        var kiara = new TakanashiKiara();
        var amelia = new WatsonAmelia();

        var fauna = new CeresFauna();
        var baelz = new HakosBaelz();
        var mumei = new NanashiMumei();
        var kronii = new OuroKronii();
        var sana = new TsukumoSana();

        var korone = new InugamiKorone();
        var okayu = new NekomataOkayu();
        var mio = new OokamiMio();

        var azki = new AZKi();
        var suisei = new HoshimachiSuisei();
        var roboco = new Roboco(
            new MagnetBook(plugin)
        );
        var miko = new SakuraMiko();
        var sora = new TokinoSora();

        var haato = new AkaiHaato();
        var aki = new AkiRosenthal();
        var matsuri = new NatsuiroMatsuri();
        var fubuki = new ShirakamiFubuki();
        var mel = new YozoraMel();

        var aqua = new MinatoAqua();
        var shion = new MurasakiShion();
        var ayame = new NakiriAyame();
        var subaru = new OozoraSubaru();
        var choco = new YuzukiChoco();

        var marine = new HoushouMarine();
        var flare = new ShiranuiFlare();
        var noel = new ShiroganeNoel();
        var rushia = new UruhaRushia();
        var pekora = new UsadaPekora();

        var kanata = new AmaneKanata();
        var luna = new HimemoriLuna();
        var coco = new KiryuCoco();
        var towa = new TokoyamiTowa();
        var watame = new TsunomakiWatame();

        var nene = new MomosuzuNene();
        var polka = new OmaruPolka();
        var botan = new ShishiroBotan();
        var lamy = new YukihanaLamy();

        var koyori = new HakuiKoyori();
        var iroha = new KazamaIroha();
        var laplus = new LaplusDarknesss();
        var chloe = new SakamataChloe();
        var lui = new TakaneLui();

        var iofi = new AiraniIofifteen();
        var risu = new AyundaRisu();
        var moona = new MoonaHoshinova();

        var anya = new AnyaMelfissa(
          new UnlimitedKrisWorksItem(plugin)
        );
        var ollie = new KureijiOllie();
        var reine = new PavoliaReine();

        var achan = new Achan();

        var aruran = new Arurandeisu();
        var miyabi = new HanasakiMiyabi();
        var izuru = new KanadeIzuru();
        var rikka = new Rikka();

        var astel = new AstelLeda();
        var temma = new KishidoTemma();
        var roberu = new YukokuRoberu();

        var oga = new AragamiOga();
        var shien = new KageyamaShien();

        return List.of(
            new EN1Collection(gura, irys, calliope, ina, kiara, amelia),
            new EN2Collection(fauna, baelz, mumei, kronii, sana),
            new GamersCollection(korone, okayu, mio),
            new Gen0Collection(azki, suisei, roboco, miko, sora),
            new Gen1Collection(haato, aki, matsuri, fubuki, mel),
            new Gen2Collection(aqua, shion, ayame, subaru, choco),
            new Gen3Collection(marine, flare, noel, rushia, pekora),
            new Gen4Collection(kanata, luna, coco, towa, watame),
            new Gen5Collection(nene, polka, botan, lamy),
            new Gen6Collection(koyori, iroha, laplus, chloe, lui),
            new ID1Collection(iofi, risu, moona),
            new ID2Collection(anya, ollie, reine),
            new MiscCollection(achan),
            new Stars1Collection(aruran, miyabi, izuru, rikka),
            new Stars2Collection(astel, temma, roberu),
            new Stars3Collection(oga, shien)
        );
    }
}
