package dev.by1337.yaml;


import org.bukkit.Color;

class ColorUtil {

    public static Color fromHex(String hex) {
        return switch (hex) {
            case "black" -> Color.BLACK;
            case "red" -> fromHex0("FF5555");
            case "dark_blue" -> fromHex0("0000AA");
            case "light_purple" -> fromHex0("FF55FF");
            case "dark_green" -> fromHex0("00AA00");
            case "yellow" -> fromHex0("FFFF55");
            case "dark_aqua" -> fromHex0("00AAAA");
            case "white" -> Color.WHITE;
            case "dark_red" -> fromHex0("AA0000");
            case "dark_purple" -> fromHex0("AA00AA");
            case "gold" -> fromHex0("FFAA00");
            case "gray" -> fromHex0("AAAAAA");
            case "dark_gray" -> fromHex0("555555");
            case "blue" -> fromHex0("5555FF");
            case "green" -> fromHex0("55FF55");
            case "aqua" -> fromHex0("55FFFF");
            default -> fromHex0(hex);
        };
    }
    private static Color fromHex0(String hex){
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
