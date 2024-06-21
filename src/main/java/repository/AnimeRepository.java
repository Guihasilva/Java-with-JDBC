package repository;

import conn.ConnectionFactory;
import dominio.Anime;
import dominio.Producer;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
public class AnimeRepository {


    public static List<Anime> findByName(String names) {
    List<Anime> animes = new ArrayList<>();
    log.info("Finding by name '{}' ",names);
    String sql = """
            SELECT a.id, a.name, a.episodes, a.producer_id, p.name as 'producer_name' FROM anime_store.anime a innerjoin 
            anime_store.producer p on a .producer_id = p.id
            where a.name like ?;""";


    try (Connection conn = ConnectionFactory.getConnection()) {
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Producer producer = Producer.builder()
                    .name(rs.getString("producer_name"))
                    .id(rs.getInt("producer_id"))
                    .build();

           Anime anime = Anime.builder()
                   .id(rs.getInt("id"))
                   .name(rs.getString("name"))
                   .episodes(rs.getInt("episodes"))
                   .producer(producer)
                   .build();

            animes.add(anime);



        }
    } catch (SQLException e) {
        log.error("Error at Trying finding animes ", e);
    }
    return animes;
}
//Return List<Anime> content All Anime
    public static List<Anime> findAll() {
        log.info("Finding all");
        return findByName("");
    }
//DELETE USER BY ID
    public static void deleteById(int id) {
        String sql = "DELETE FROM anime_store.anime WHERE id =?;";
        try(Connection conn = ConnectionFactory.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,id);
           int arrowsDeleted = ps.executeUpdate();

           if (arrowsDeleted > 0 ){
               log.info("Deleted with SUCCESS, lines affected = '{}'", arrowsDeleted);
           }else{
               log.info("Failed to delete, lines affected = '{}'", arrowsDeleted);
           }

        } catch (SQLException e) {
            log.error("Error while trying delete anime '{}'", id, e);
        }
    }

    public static void saveAnime (Anime anime){
        String sql = "INSERT INTO `anime_store`.`anime` (`name`, `episodes`, `producer_id`) VALUES (?, ?, ?);";
        try(Connection conn = ConnectionFactory.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, anime.getName());
            ps.setInt(2,anime.getEpisodes());
            ps.setInt(3,anime.getProducer().getId());
            int arrowsAffected = ps.executeUpdate();
            if(arrowsAffected > 0 ) {
                log.info("Success arrows affected = '{}'", arrowsAffected);
            }
        } catch (SQLException e) {
            log.error("Error while trying insert anime '{}'", anime.getName(), e);
        }
    }

    public static void updateProducer (Anime anime){
        String sql = "UPDATE `anime_store`.`anime` SET `name` = ?, `episodes`= ?  WHERE (`id` = ?);";
        try(Connection conn = ConnectionFactory.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, anime.getName());
            ps.setInt(2,anime.getEpisodes());
            ps.setInt(3,anime.getId());
            int arrowsAffected = ps.executeUpdate();
            if(arrowsAffected > 0) {
                log.info("Success to update, arrows affected = '{}'", arrowsAffected);
            }

        } catch (SQLException e) {
            log.error("Error while trying update Object ",e);
        }
    }

    public static Optional<Anime> findById(int id) {
        String sql = """
            SELECT a.id, a.name, a.episodes, a.producer_id, p.name as 'producer_name' FROM anime_store.anime a innerjoin 
            anime_store.producer p on a .producer_id = p.id
            where a.name = ?;""";
        try(Connection conn = ConnectionFactory.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                Producer producer = Producer.builder()
                        .name(rs.getString("producer_name"))
                        .id(rs.getInt("producer_id"))
                        .build();

                Anime anime = Anime.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .episodes(rs.getInt("episodes"))
                        .producer(producer)
                        .build();
                return Optional.of(anime);
            }else{
                log.error("ID not found");
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
