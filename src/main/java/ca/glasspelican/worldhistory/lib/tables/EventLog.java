package ca.glasspelican.worldhistory.lib.tables;

public class EventLog {
    private static final String initQuery = "CREATE TABLE IF NOT EXISTS `events` (\n" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT," +
            "  `eventType` int(11) NOT NULL," +
            "  `x` int(11) NOT NULL," +
            "  `y` int(11) NOT NULL," +
            "  `z` int(11) NOT NULL," +
            "  `dimensionID` int(11) NOT NULL," +
            "  `user` varchar(255) NOT NULL," +
            "  `time` datetime NOT NULL," +
            "  `blockName` varchar(255) NOT NULL," +
            "  PRIMARY KEY (`id`)" +
            ");";

    public static String getInitQuery() {
        return initQuery;
    }
}
