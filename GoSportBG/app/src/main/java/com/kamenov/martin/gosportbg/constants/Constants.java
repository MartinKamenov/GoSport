package com.kamenov.martin.gosportbg.constants;

import android.graphics.Color;

import static com.google.maps.android.ui.IconGenerator.STYLE_BLUE;
import static com.google.maps.android.ui.IconGenerator.STYLE_GREEN;
import static com.google.maps.android.ui.IconGenerator.STYLE_ORANGE;
import static com.google.maps.android.ui.IconGenerator.STYLE_PURPLE;
import static com.google.maps.android.ui.IconGenerator.STYLE_RED;
import static com.google.maps.android.ui.IconGenerator.STYLE_WHITE;

/**
 * Created by Martin on 28.3.2018 г..
 */

public class Constants {
    public static String[] MONTHS = {"Януари", "Февруари", "Март", "Април",
            "Май", "Юни", "Юли", "Август",
            "Септември", "Октомври", "Ноември", "Декември"};
    public static String[] SPORTS = {"Футбол", "Баскетбол", "Бокс", "Кикбокс",
            "Тенис на маса", "Шах", "Волейбол", "Тенис на корт", "Боен спорт",
            "Катерене", "Планинарство", "Ски", "Бейсбол", "Колоездене",
            "Кънки", "Картинг", "Голф", "Хандбол", "Хокей", "Кънки на лед", "Плуване",
            "Ръгби", "Фехтовка", "Гимнастика", "Пайнтбол",
            "Скуош", "Болинг", "Билярд", "Скейтборд", "Сноуборд", "Крикет",
            "Карти", "Риболов", "Ловен спорт", "Спортно Ходене", "Друг"};
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static String DOMAIN;
    public static int[] STYLES = { STYLE_RED, STYLE_GREEN, STYLE_BLUE, STYLE_ORANGE, STYLE_PURPLE, STYLE_WHITE };
    public static String[] CITIES = new String[] {"София", "Пловдив", "Варна", "Бургас", "Русе", "Стара Загора",
    "Плевен", "Сливен", "Добрич", "Шумен", "Перник", "Хасково", "Ямбол", "Пазарджик", "Благоевград",
    "Велико Търново", "Враца", "Габрово", "Асеновград", "Видин", "Казанлък", "Кюстендил",
            "Кърджали", "Монтана", "Димитровград" };
    public static double[][] CITIESCOORDINATES = {{42.698334, 23.319941}, {42.1499994, 24.749997},
            {43.2166658, 27.916663}, {42.499998, 27.4666648}, {43.85639, 25.97083}, {42.43278, 25.64194},
            {43.41667, 24.61667}, {42.68583, 26.32917}, {43.56667, 27.83333}, {43.27064, 26.92286},
            {42.6, 23.0333333}, {41.934437, 25.555446}, {42.48333, 26.5}, {42.192765, 24.333566},
            {42.01667, 23.1}, {43.08124, 25.62904}, {43.21, 23.5625}, {42.87472, 25.33417},
            {42.01667, 24.86667}, {43.99, 22.8725}, {42.6166667, 25.4}, {42.28389, 22.69111},
            {41.6499974, 25.3666652}, {43.4125, 23.225}, {42.05, 25.6}};
    public static int TEAMSIDDIFFERENCE = 10000000;
    public static int MAINCOLOR = Color.parseColor("#282828");
    public static int SECONDCOLOR = Color.parseColor("#ffffff");
    public static int CARDCOLOR = Color.parseColor("#444444");
    public static int CARDTEXTCOLOR = Color.parseColor("#ffffff");
    public static int CLICKEDCARDCOLOR = Color.parseColor("#aaaaaa");
    public static String[] MAP_TYPES = new String[]{"Хибрид", "Нормален"};
    public static int[][] THEMES = new int[][] {{Color.parseColor("#282828"), Color.parseColor("#ffffff")},
            {Color.parseColor("#ffffff"), Color.parseColor("#282828")}};
    public static String[] THEME_NAMES = new String[] {"Черна", "Бяла"};

    public static int BG_ZONE_DIFFERENCE = 3;
}
