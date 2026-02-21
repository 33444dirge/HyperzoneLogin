表结构

表UserDataTableV3

    private static final String fieldOnlineUUID = "online_uuid";
    private static final String fieldOnlineName = "online_name";
    private static final String fieldServiceId = "service_id";
    private static final String fieldInGameProfileUuid = "in_game_profile_uuid";
    private static final String fieldWhitelist = "whitelist";
    private final SQLManager sqlManager;
    private final String tableName;
    private final String tableNameV2;

    public UserDataTableV3(SQLManager sqlManager, String tableName, String tableNameV2) {
        this.sqlManager = sqlManager;
        this.tableName = tableName;
        this.tableNameV2 = tableNameV2;
    }

    public void init(Connection connection) throws SQLException {
        String sql = MessageFormat.format(
                "CREATE TABLE IF NOT EXISTS {0} ( " +
                        "{1} BINARY(16) NOT NULL, " +
                        "{2} INTEGER NOT NULL, " +
                        "{3} VARCHAR(64) DEFAULT NULL, " +
                        "{4} BINARY(16) DEFAULT NULL, " +
                        "{5} BOOL DEFAULT FALSE, " +
                        "PRIMARY KEY ( {1}, {2} ))"
                , tableName, fieldOnlineUUID, fieldServiceId, fieldOnlineName, fieldInGameProfileUuid, fieldWhitelist);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

InGameProfileTableV3

    private static final String fieldInGameUuid = "in_game_uuid";
    private static final String fieldCurrentUsernameLowerCase = "current_username_lower_case";
    private static final String fieldCurrentUsernameOriginal = "current_username_original";
    private final String tableName;
    private final String tableNameV2;
    private final SQLManager sqlManager;

    public InGameProfileTableV3(SQLManager sqlManager, String tableName, String tableNameV2) {
        this.tableName = tableName;
        this.sqlManager = sqlManager;
        this.tableNameV2 = tableNameV2;
    }


    public void init(Connection connection) throws SQLException {
        String sql = MessageFormat.format(
                "CREATE TABLE IF NOT EXISTS {0} ( " +
                        "{1} BINARY(16) NOT NULL, " +
                        "{2} VARCHAR(64) DEFAULT NULL, " +
                        "{3} VARCHAR(64) DEFAULT NULL, " +
                        "CONSTRAINT IGPT_V3_PR PRIMARY KEY ( {1} ), " +
                        "CONSTRAINT IGPT_V3_UN UNIQUE ( {2} ))"
                , tableName, fieldInGameUuid, fieldCurrentUsernameLowerCase, fieldCurrentUsernameOriginal);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

迁移方案
in_game_profile_uuid和in_game_uuid是一一对应的
UserDataTableV3对应我们的EntryTable
InGameProfileTableV3对应我们的ProfileTable
对应的字段看一下就知道了，profile ID用我们自己的算法进行生成

需要有迁移配置，配置项包括连接方式（H2DB和MYSQL），迁移后要生成单独的merge-ml.log ，要有每一条数据的迁移结果和对应情况，以及最后的数据汇总
需要有用于触发的命令 hzl-merge ，然后子命令 ml 进行迁移