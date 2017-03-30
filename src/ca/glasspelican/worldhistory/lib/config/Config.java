package ca.glasspelican.worldhistory.lib.config;

import net.minecraftforge.common.config.Configuration;

import java.util.ArrayList;
import java.util.List;

public class Config {

    private static final List<configOption> configOptions = new ArrayList<configOption>();
    private static String[] modUsers; //TODO: Make me something that is better configurable and hooks in with colorChat

    static {
        configOptions.add(new configOption("host", "localhost").setCategory("MySQL"));
        configOptions.add(new configOption("username", "root").setCategory("MySQL"));
        configOptions.add(new configOption("password", "root").setCategory("MySQL"));
        configOptions.add(new configOption("database", "blocklog").setCategory("MySQL"));
        configOptions.add(new configOption("logBlockBreak", false));

    }

    public static void loadConfigOptions(Configuration c) {
        modUsers = c.get(c.CATEGORY_GENERAL, "modUsers", new String[]{}).getStringList();
        for (configOption config : configOptions) {
            config.loadFromConfig(c);
        }
    }

    public static boolean getBool(String key) {
        for (configOption config : configOptions) {
            if (config.getKey().equals(key)) {
                return config.getBool();
            }
        }
        return false;
    }

    public static int getInt(String key) {
        for (configOption config : configOptions) {
            if (config.getKey().equals(key)) {
                return config.getInt();
            }
        }
        return 0;
    }

    public static String getString(String key) {
        for (configOption config : configOptions) {
            if (config.getKey().equals(key)) {
                return config.getString();
            }
        }
        return "";
    }

    public static boolean isUserAMod(String nick) {
        for (String modUser : modUsers) {
            if (modUser.equalsIgnoreCase(nick)) {
                return true;
            }
        }
        return false;
    }

    private static class configOption {
        private String key;
        private boolean isBool;
        private boolean isInt;
        private boolean isString;
        private boolean val;
        private boolean def;
        private int valInt;
        private int defInt;
        private String valString;
        private String defString;
        private String category = Configuration.CATEGORY_GENERAL;

        public configOption(String _key, boolean _def) {
            key = _key;
            val = _def;
            def = _def;
            isBool = true;
            isInt = false;
            isString = false;
        }

        public configOption(String _key, int _def) {
            key = _key;
            valInt = _def;
            defInt = _def;
            isBool = false;
            isInt = true;
            isString = false;
        }

        public configOption(String _key, String _def) {
            key = _key;
            valString = _def;
            defString = _def;
            isBool = false;
            isInt = false;
            isString = true;
        }

        public configOption setCategory(String newCat) {
            category = newCat;
            return this;
        }

        public String getKey() {
            return key;
        }

        public boolean getBool() {
            return val;
        }

        public int getInt() {
            return valInt;
        }

        public String getString() {
            return valString;
        }

        public void loadFromConfig(Configuration config) {
            if (isBool) {
                val = config.get(category, key, def).getBoolean(def);
            } else if (isInt) {
                valInt = config.get(category, key, defInt).getInt(defInt);
            } else if (isString) {
                valString = config.get(category, key, defString).getString();
            }
        }
    }
}
