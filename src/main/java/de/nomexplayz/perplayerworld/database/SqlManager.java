package de.nomexplayz.perplayerworld.database;

import de.nomexplayz.perplayerworld.PerPlayerWorld;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.sql.SqlService;

import javax.sql.DataSource;
import java.io.UncheckedIOException;
import java.sql.Connection;
import java.sql.SQLException;

public class SqlManager {
    private Logger logger;
    public DataSource dataSource;
    private SqlService sql;

    public SqlManager(PerPlayerWorld perplayerworld, Logger logger) {
        this.logger = logger;

        try {
            dataSource = sql.getDataSource("jdbc:" + perplayerworld.getDatabaseUrl() + "?username=" + perplayerworld.getDatabaseUser() + "&password=" + perplayerworld.getDatabasePassword());
        } catch (SQLException ex) {
            logger.warn("[PPW] Error getting data source!");
        } catch (UncheckedIOException uex) {
            logger.warn("[PPW] Error connecting to database! Check the config and make sure the database credentials are correct!");
        }
    }

    /**
     * Get the data source using the passed in JBDC url.
     *
     * @param jdbcUrl The JDBC url
     * @return DataSource
     * @throws SQLException Thrown when there's an exception getting the data source
     */
    public DataSource getDataSource(String jdbcUrl) throws SQLException {
        if (sql == null) {
            sql = Sponge.getServiceManager().provide(SqlService.class).get();
        }

        return sql.getDataSource(jdbcUrl);
    }

    /**
     * Create a new table in the database.
     *
     * @param tableName Name of the table to be created
     * @param cols The columns that the table should have
     * @return boolean Result of the query
     */
    public boolean createTable(String tableName, String cols) {
        try {
            Connection conn = dataSource.getConnection();

            boolean result =  conn.prepareStatement("CREATE TABLE IF NOT EXISTS " + tableName + " (" + cols + ")").execute();

            conn.close();

            return result;
        } catch (SQLException e) {
            logger.warn("[PPW] An error occurred while creating a table!");
            e.printStackTrace();
        }
        return false;
    }
}
