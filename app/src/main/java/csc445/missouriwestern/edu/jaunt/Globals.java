package csc445.missouriwestern.edu.jaunt;

import csc445.missouriwestern.edu.jaunt.extensions.datastructure.CaseInsensitiveMap;

/**
 * Created by byan on 3/1/2018.
 */

public class Globals {
    //Web and Database Server
    public static final String SERVER_DOMAIN = "http://ec2-34-211-83-145.us-west-2.compute.amazonaws.com";
    public static final String SERVER_URL = SERVER_DOMAIN + "/driver";
    public static final String PLACE_HISTORY_KEY = "PlaceHistoryRecords";
    public static final String ACCOUNT_INFO_KEY = "AccountInfo";
    public static final String GUEST_BOOK = "GuestBook";
    public static final String PROFILE_PHOTO_SIGNATURE = "Profile_Photo_Signature";
    public static final String HOURS_RECORD_KEY = "HoursRecord";
    public static final String AVAILABILITY_KEY = "Availability";
    public static final int MY_SOCKET_TIMEOUT_MS = 10000;
    public static final String WEATHER_KEY = "Weather";
    //https://openweathermap.org/weather-conditions
    public static final String OPEN_WEATHER_ICON_FOLDER_URL = "http://openweathermap.org/img/w/";
    public static final String GEONAME_TIMEZONE_API_URL = "http://api.geonames.org/timezone?lat=%f&lng=%f&username=baoqiangy";
    public static final String OPEN_WEATHER_MAP_API = "http://api.openweathermap.org/data/2.5/forecast?id=%d&appid=%s&units=metric";

    public static final CaseInsensitiveMap states = new CaseInsensitiveMap(){{
        put("Alabama","AL");
        put("Alaska","AK");
        put("Alberta","AB");
        put("American Samoa","AS");
        put("Arizona","AZ");
        put("Arkansas","AR");
        put("Armed Forces (AE)","AE");
        put("Armed Forces Americas","AA");
        put("Armed Forces Pacific","AP");
        put("British Columbia","BC");
        put("California","CA");
        put("Colorado","CO");
        put("Connecticut","CT");
        put("Delaware","DE");
        put("District Of Columbia","DC");
        put("Florida","FL");
        put("Georgia","GA");
        put("Guam","GU");
        put("Hawaii","HI");
        put("Idaho","ID");
        put("Illinois","IL");
        put("Indiana","IN");
        put("Iowa","IA");
        put("Kansas","KS");
        put("Kentucky","KY");
        put("Louisiana","LA");
        put("Maine","ME");
        put("Manitoba","MB");
        put("Maryland","MD");
        put("Massachusetts","MA");
        put("Michigan","MI");
        put("Minnesota","MN");
        put("Mississippi","MS");
        put("Missouri","MO");
        put("Montana","MT");
        put("Nebraska","NE");
        put("Nevada","NV");
        put("New Brunswick","NB");
        put("New Hampshire","NH");
        put("New Jersey","NJ");
        put("New Mexico","NM");
        put("New York","NY");
        put("Newfoundland","NF");
        put("North Carolina","NC");
        put("North Dakota","ND");
        put("Northwest Territories","NT");
        put("Nova Scotia","NS");
        put("Nunavut","NU");
        put("Ohio","OH");
        put("Oklahoma","OK");
        put("Ontario","ON");
        put("Oregon","OR");
        put("Pennsylvania","PA");
        put("Prince Edward Island","PE");
        put("Puerto Rico","PR");
        put("Quebec","QC");
        put("Rhode Island","RI");
        put("Saskatchewan","SK");
        put("South Carolina","SC");
        put("South Dakota","SD");
        put("Tennessee","TN");
        put("Texas","TX");
        put("Utah","UT");
        put("Vermont","VT");
        put("Virgin Islands","VI");
        put("Virginia","VA");
        put("Washington","WA");
        put("West Virginia","WV");
        put("Wisconsin","WI");
        put("Wyoming","WY");
        put("Yukon Territory","YT");
    }};
    public static final String PASS_RESET_VERIFICATION_CODE = "Pass_Reset_Verification_Code";
    public static final String PASS_RESET_VERIFICATION_CODE_SAVE_TIME = "Pass_Reset_Verification_Code_Save_Time";
    public static final String PASS_RESET_VERIFICATION_EMAIL = "Pass_Reset_Verification_Email";
}
