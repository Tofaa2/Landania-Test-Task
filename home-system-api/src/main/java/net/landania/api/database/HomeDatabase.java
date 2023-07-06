package net.landania.api.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.landania.api.Home;
import net.landania.api.HomeAPI;
import net.landania.api.HomePlayer;
import net.landania.api.Platform;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a MySQL database connection for storing and retrieving homes.
 */
public final class HomeDatabase {

    private static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .create();
    private final Connection connection;
    private final Platform platform;
    private final HomeDatabaseCredentials credentials;

    public HomeDatabase(@NotNull Platform platform, @NotNull HomeDatabaseCredentials credentials) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        this.connection = DriverManager.getConnection(credentials.url());
        this.platform = platform;
        this.credentials = credentials;
    }

    public CompletableFuture<HomePlayer> load(@NotNull UUID uuid) {
        CompletableFuture<HomePlayer> future = new CompletableFuture<>();
        platform.runAsync(() -> {
            verifyTableExists();
            try (PreparedStatement ps = connection.prepareStatement("SELECT `data` FROM `" + credentials.tableName() + "` WHERE `uuid` = ?")) {
                ps.setString(1, uuid.toString());

                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    future.complete(HomeAPI.getPlatform().createPlayerUnsafe(uuid, List.of()));
                    return;
                }
                List<Home> homes = GSON.fromJson(rs.getString("data"), TypeToken.getParameterized(List.class, Home.class).getType());
                future.complete(HomeAPI.getPlatform().createPlayerUnsafe(uuid, homes));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return future;
    }

    public void save(@NotNull HomePlayer player) {
        UUID uuid = player.getUUID();
        List<Home> homes = player.getHomes();
        platform.runAsync(() -> {
            verifyTableExists();
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO `" + credentials.tableName() + "` (`uuid`, `data`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `data` = VALUES(`data`)")) {
                ps.setString(1, uuid.toString());
                ps.setString(2, GSON.toJson(homes, TypeToken.getParameterized(List.class, Home.class).getType()));
                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void verifyTableExists() {
        try (PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `" + credentials.tableName() + "` (" +
                "`uuid` VARCHAR(36) NOT NULL," +
                "`data` TEXT NOT NULL," +
                "PRIMARY KEY (`uuid`))")) {
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
