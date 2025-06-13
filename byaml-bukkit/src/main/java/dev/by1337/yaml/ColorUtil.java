package dev.by1337.yaml;


import org.bukkit.Color;

class ColorUtil {
    public static final Color BLACK = Color.BLACK;
    public static final Color RED = fromHex0("FF5555");
    public static final Color DARK_BLUE = fromHex0("0000AA");
    public static final Color LIGHT_PURPLE = fromHex0("FF55FF");
    public static final Color DARK_GREEN = fromHex0("00AA00");
    public static final Color YELLOW = fromHex0("FFFF55");
    public static final Color DARK_AQUA = fromHex0("00AAAA");
    public static final Color WHITE = Color.WHITE;
    public static final Color DARK_RED = fromHex0("AA0000");
    public static final Color DARK_PURPLE = fromHex0("AA00AA");
    public static final Color GOLD = fromHex0("FFAA00");
    public static final Color GRAY = fromHex0("AAAAAA");
    public static final Color DARK_GRAY = fromHex0("555555");
    public static final Color BLUE = fromHex0("5555FF");
    public static final Color GREEN = fromHex0("55FF55");
    public static final Color AQUA = fromHex0("55FFFF");

    public static Color fromHex(String hex) {
        return switch (hex) {
            case "black" -> BLACK;
            case "red" -> RED;
            case "dark_blue" -> DARK_BLUE;
            case "light_purple" -> LIGHT_PURPLE;
            case "dark_green" -> DARK_GREEN;
            case "yellow" -> YELLOW;
            case "dark_aqua" -> DARK_AQUA;
            case "white" -> WHITE;
            case "dark_red" -> DARK_RED;
            case "dark_purple" -> DARK_PURPLE;
            case "gold" -> GOLD;
            case "gray" -> GRAY;
            case "dark_gray" -> DARK_GRAY;
            case "blue" -> BLUE;
            case "green" -> GREEN;
            case "aqua" -> AQUA;
            default -> fromHex0(hex);
        };
    }

    private static Color fromHex0(String hex) {
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }
        int red = Integer.parseInt(hex.substring(0, 2), 16);
        int green = Integer.parseInt(hex.substring(2, 4), 16);
        int blue = Integer.parseInt(hex.substring(4, 6), 16);
        return org.bukkit.Color.fromRGB(red, green, blue);
    }

    public static String toHex(Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        return String.format("#%02X%02X%02X", red, green, blue).toLowerCase();
    }
}
