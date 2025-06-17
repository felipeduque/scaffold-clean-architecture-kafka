package co.com.fac.r2dbc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "postgresql")
public record PostgresqlConnectionProperties(
        String host,
        Integer port,
        String database,
        String schema,
        String username,
        String password
) {
    // Constructor con valores por defecto para desarrollo local
    public PostgresqlConnectionProperties {
        if (host == null) host = "localhost";
        if (port == null) port = 5432;
        if (database == null) database = "postgres";
        if (schema == null) schema = "public";
        if (username == null) username = "postgres";
        if (password == null) password = "Develop@0531";
    }
}
