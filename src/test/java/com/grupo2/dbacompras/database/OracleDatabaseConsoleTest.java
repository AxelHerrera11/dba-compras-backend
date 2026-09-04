package com.grupo2.dbacompras.database;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public final class OracleDatabaseConsoleTest {

    private static final String DEFAULT_HOST = "svr-sql-ctezo.southcentralus.cloudapp.azure.com";
    private static final String DEFAULT_PORT = "1521";
    private static final String DEFAULT_SERVICE = "FREEPDB1";
    private static final String DEFAULT_USER = "DBA_COMPRAS";

    private static final List<String> TABLES = List.of(
            "TBL_CLIENTES",
            "TBL_MARCAS",
            "TBL_TARJETAS",
            "TBL_CATEGORIAS",
            "TBL_PRODUCTOS",
            "TBL_ENC_COMPRAS",
            "TBL_DET_COMPRAS"
    );

    private static final List<ForeignKeyRelation> EXPECTED_RELATIONS = List.of(
            new ForeignKeyRelation("TBL_TARJETAS", "ID_CLIENTE", "TBL_CLIENTES", "ID_CLIENTE"),
            new ForeignKeyRelation("TBL_TARJETAS", "ID_MARCA", "TBL_MARCAS", "ID_MARCA"),
            new ForeignKeyRelation("TBL_PRODUCTOS", "ID_CATEGORIA", "TBL_CATEGORIAS", "ID_CATEGORIA"),
            new ForeignKeyRelation("TBL_ENC_COMPRAS", "ID_CLIENTE", "TBL_CLIENTES", "ID_CLIENTE"),
            new ForeignKeyRelation("TBL_ENC_COMPRAS", "ID_TARJETA", "TBL_TARJETAS", "ID_TARJETA"),
            new ForeignKeyRelation("TBL_DET_COMPRAS", "ID_COMPRA", "TBL_ENC_COMPRAS", "ID_COMPRA"),
            new ForeignKeyRelation("TBL_DET_COMPRAS", "ID_PRODUCTO", "TBL_PRODUCTOS", "ID_PRODUCTO")
    );

    private OracleDatabaseConsoleTest() {
    }

    public static void main(String[] args) throws SQLException {
        String command = args.length == 0 ? "all" : args[0].toLowerCase();

        try (Connection connection = getConnection()) {
            switch (command) {
                case "connection" -> validateConnection(connection);
                case "tables" -> validateTables(connection);
                case "counts" -> validateCounts(connection);
                case "relations" -> validateRelations(connection);
                case "all" -> {
                    validateConnection(connection);
                    validateTables(connection);
                    validateCounts(connection);
                    validateRelations(connection);
                }
                default -> throw new IllegalArgumentException("Comando no soportado: " + command);
            }
        }
    }

    private static Connection getConnection() throws SQLException {
        Map<String, String> dotenv = loadDotenv();
        String host = setting("ORACLE_HOST", DEFAULT_HOST, dotenv);
        String port = setting("ORACLE_PORT", DEFAULT_PORT, dotenv);
        String service = setting("ORACLE_SERVICE", DEFAULT_SERVICE, dotenv);
        String user = setting("ORACLE_USER", DEFAULT_USER, dotenv);
        String password = requiredSetting("ORACLE_PASSWORD", dotenv);

        Properties properties = new Properties();
        properties.put("user", user);
        properties.put("password", password);

        return DriverManager.getConnection("jdbc:oracle:thin:@//" + host + ":" + port + "/" + service, properties);
    }

    private static String setting(String name, String defaultValue, Map<String, String> dotenv) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            value = dotenv.get(name);
        }
        return value == null || value.isBlank() ? defaultValue : value.trim();
    }

    private static String requiredSetting(String name, Map<String, String> dotenv) {
        String value = setting(name, null, dotenv);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Falta configurar la variable de entorno " + name);
        }
        return value;
    }

    private static Map<String, String> loadDotenv() {
        Path dotenvPath = Path.of(".env");
        Map<String, String> values = new HashMap<>();
        if (!Files.exists(dotenvPath)) {
            return values;
        }

        try {
            for (String line : Files.readAllLines(dotenvPath)) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#") || !trimmed.contains("=")) {
                    continue;
                }
                int separator = trimmed.indexOf('=');
                values.put(trimmed.substring(0, separator).trim(), trimmed.substring(separator + 1).trim());
            }
        } catch (IOException exception) {
            throw new IllegalStateException("No se pudo leer el archivo .env", exception);
        }
        return values;
    }

    private static void validateConnection(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT 1 FROM DUAL")) {
            if (!resultSet.next() || resultSet.getInt(1) != 1) {
                throw new IllegalStateException("SELECT 1 FROM DUAL no devolvio el valor esperado.");
            }
            System.out.println("[OK] Conexion a Oracle establecida correctamente.");
            System.out.println("SELECT 1 FROM DUAL -> " + resultSet.getInt(1));
        }
    }

    private static void validateTables(Connection connection) throws SQLException {
        Set<String> foundTables = new HashSet<>();
        String sql = """
                SELECT table_name
                FROM user_tables
                WHERE table_name IN ('TBL_CLIENTES', 'TBL_MARCAS', 'TBL_TARJETAS', 'TBL_CATEGORIAS', 'TBL_PRODUCTOS', 'TBL_ENC_COMPRAS', 'TBL_DET_COMPRAS')
                ORDER BY table_name
                """;

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                foundTables.add(resultSet.getString("table_name"));
            }
        }

        for (String table : TABLES) {
            if (!foundTables.contains(table)) {
                throw new IllegalStateException("No se encontro acceso a la tabla " + table);
            }
            System.out.println("[OK] " + table);
        }
        System.out.println();
        System.out.println("[OK] " + foundTables.size() + " de " + TABLES.size() + " tablas disponibles.");
    }

    private static void validateCounts(Connection connection) throws SQLException {
        for (String table : TABLES) {
            String sql = "SELECT COUNT(*) FROM " + table;
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                if (!resultSet.next()) {
                    throw new IllegalStateException("La consulta de conteo no devolvio resultado para " + table);
                }
                Object count = resultSet.getObject(1);
                if (!(count instanceof Number || count instanceof BigDecimal)) {
                    throw new IllegalStateException("El conteo de " + table + " no es numerico.");
                }
                System.out.printf("%-18s -> %s registros%n", table, count);
            }
        }
    }

    private static void validateRelations(Connection connection) throws SQLException {
        Set<ForeignKeyRelation> foundRelations = new HashSet<>();
        String sql = """
                SELECT child.table_name AS child_table,
                       child_cols.column_name AS child_column,
                       parent.table_name AS parent_table,
                       parent_cols.column_name AS parent_column
                FROM user_constraints child
                JOIN user_cons_columns child_cols
                  ON child.constraint_name = child_cols.constraint_name
                JOIN user_constraints parent
                  ON child.r_constraint_name = parent.constraint_name
                JOIN user_cons_columns parent_cols
                  ON parent.constraint_name = parent_cols.constraint_name
                 AND child_cols.position = parent_cols.position
                WHERE child.constraint_type = 'R'
                  AND child.table_name IN ('TBL_CLIENTES', 'TBL_MARCAS', 'TBL_TARJETAS', 'TBL_CATEGORIAS', 'TBL_PRODUCTOS', 'TBL_ENC_COMPRAS', 'TBL_DET_COMPRAS')
                """;

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                foundRelations.add(new ForeignKeyRelation(
                        resultSet.getString("child_table"),
                        resultSet.getString("child_column"),
                        resultSet.getString("parent_table"),
                        resultSet.getString("parent_column")
                ));
            }
        }

        List<ForeignKeyRelation> missingRelations = new ArrayList<>();
        for (ForeignKeyRelation relation : EXPECTED_RELATIONS) {
            if (foundRelations.contains(relation)) {
                System.out.println("[OK] " + relation);
            } else {
                missingRelations.add(relation);
            }
        }

        if (!missingRelations.isEmpty()) {
            throw new IllegalStateException("Relaciones no encontradas: " + missingRelations);
        }
        System.out.println();
        System.out.println("[OK] " + EXPECTED_RELATIONS.size() + " de " + EXPECTED_RELATIONS.size() + " relaciones FK disponibles.");
    }

    private record ForeignKeyRelation(String childTable, String childColumn, String parentTable, String parentColumn) {
        @Override
        public String toString() {
            return childTable + "." + childColumn + " -> " + parentTable + "." + parentColumn;
        }
    }
}
