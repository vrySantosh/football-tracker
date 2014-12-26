package com.example.kvarnsen.footballtracker;

import java.util.HashMap;
import java.util.Map;

/*
    HashMap to hold team-subreddit key value pairs
 */
public class TeamMap {

    private Map<String, String> bundesligaMap = new HashMap<String, String>();
    private Map<String, String> ligaMap = new HashMap<String, String>();
    private Map<String, String> premierMap = new HashMap<String, String>();


    public TeamMap() {

        bundesligaMap.put("koln", "effzeh");
        bundesligaMap.put("mainz", "mainz05");
        bundesligaMap.put("leverkusen", "bayer04");
        bundesligaMap.put("bayern", "fcbayern");
        bundesligaMap.put("dortmund", "borussiadortmund");
        bundesligaMap.put("frankfurt", "eintracht");
        bundesligaMap.put("schalke", "schalke04");
        bundesligaMap.put("hertha", "herthabsc");
        bundesligaMap.put("hsv", "hsv");
        bundesligaMap.put("stuttgart", "vfbstuttgart");
        bundesligaMap.put("wolfsburg", "diewolfe");
        bundesligaMap.put("bremen", "svw");

        ligaMap.put("athletic", "athleticclub");
        ligaMap.put("atletico", "atletico");
        ligaMap.put("barcelona", "barca");
        ligaMap.put("coruna", "deportivo");
        ligaMap.put("granada", "granadacf");
        ligaMap.put("madrid", "realmadrid");
        ligaMap.put("sociedad", "sociedad");
        ligaMap.put("eibar", "sdeibar");
        ligaMap.put("valencia", "valenciacf");
        ligaMap.put("villareal", "villarealcf");

        premierMap.put("arsenal", "gunners");
        premierMap.put("aston", "avfc");
        premierMap.put("burnley", "burnley");
        premierMap.put("chelsea", "chelseafc");
        premierMap.put("crystalpalace", "crystalpalace");
        premierMap.put("everton", "everton");
        premierMap.put("hull", "hullcity");
        premierMap.put("leicester", "lcfc");
        premierMap.put("liverpool", "liverpoolfc");
        premierMap.put("mancity", "mcfc");
        premierMap.put("manutd", "reddevils");
        premierMap.put("newcastle", "nufc");
        premierMap.put("southampton", "saintsfc");
        premierMap.put("stoke", "stokecityfc");
        premierMap.put("sunderland", "safc");
        premierMap.put("tottenham", "coys");
        premierMap.put("westbrom", "wbafootball");
        premierMap.put("westham", "hammers");

    }

    public String fetchSub(String id, String key) {

        String sub = null;

        switch(id) {

            case "bundesliga":
                sub = bundesligaMap.get(key);
                break;
            case "liga":
                sub = ligaMap.get(key);
                break;
            case "premier":
                sub = premierMap.get(key);
                break;

        }

        return sub;
    }

}
