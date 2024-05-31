package server;

public class Configuration {
    public static int port = 6379;
    public static String masterIp = "";
    public static String masterPort = "";
    public static String role = "master";
    public static String masterRepliId = "8371b4fb1155b71f4a04d3e1bc3e18c4a990aeeb"; // TODO : this is hardcoded replication id
    public static Integer masterReplOffset = 0;

    public static String replicationInfo() {
        return """
                # Replication
                role:%s
                master_replid:%s
                master_repl_offset:%s
                """
                .formatted(
                        role,
                        masterRepliId,
                        masterReplOffset
                );
    }
}
