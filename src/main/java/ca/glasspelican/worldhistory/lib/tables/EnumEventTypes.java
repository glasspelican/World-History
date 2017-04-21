package ca.glasspelican.worldhistory.lib.tables;

/**
 *
 */
public enum EnumEventTypes {
    BLOCKBREAK(1, "Block Break", "event.worldhistory.blockbreak"),
    TILEENTITY(2, "TileEntity Break", "event.worldhistory.tilebreak"),
    INVENTORY(3, "Inventory Open", "event.worldhistory.inventoryopen"),
    BLOCKRIGHT(4, "Block Rightclick", "event.worldhistory.blockright"),;


    private final int id;
    private final String name;
    private final String unlocalizedName;

    EnumEventTypes(int id, String name, String unlocalizedName) {
        this.id = id;
        this.name = name;
        this.unlocalizedName = unlocalizedName;
    }

    public static String getTable() {
        StringBuilder stringBuilder = new StringBuilder("INSERT INTO `eventtypes` (`id`, `name`) VALUES");
        for (EnumEventTypes type :
                values()) {
            stringBuilder.append("(");
            stringBuilder.append(type.getId());
            stringBuilder.append(", '");
            stringBuilder.append(type.getName());
            stringBuilder.append("'),");
        }

        return stringBuilder.toString();
    }

    public static String getTableStructure() {
        StringBuilder structure = new StringBuilder("CREATE TABLE IF NOT EXISTS `eventtypes` (");
        structure.append("`id` int(11) NOT NULL AUTO_INCREMENT,");
        structure.append("`name` varchar(255) NOT NULL,");
        structure.append("PRIMARY KEY (`id`)");
        structure.append(");");

        return structure.toString();
    }

    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
