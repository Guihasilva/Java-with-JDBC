package repository;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import conn.ConnectionFactory;
import dominio.Producer;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
public class ProducerRepository {


    public static List<Producer> findByName(String names) {
    List<Producer> producers = new ArrayList<>();
    log.info("Finding by name '{}' ",names);
    String sql = "SELECT * FROM anime_store.producer where name like '%%%s%%';"
            .formatted(names);

    try (Connection conn = ConnectionFactory.getConnection()) {
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            var id =  rs.getInt("id");
            var name =  rs.getString("name");

            Producer producer = Producer.builder().id(id).name(name).build();
            producers.add(Producer.builder().id(id).name(name).build());



        }
    } catch (SQLException e) {
        log.error("Error at Trying finding producers ", e);
    }
    return producers;
}
//Return List<Producer> content All Producer
    public static List<Producer> findAll() {
        log.info("Finding all");
        return findByName("");
    }
//DELETE USER BY ID
    public static void deleteById(int id) {
        String sql = "DELETE FROM anime_store.producer WHERE id =?;";
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
            log.error("Error while trying delete producer '{}'", id, e);
        }
    }

    public static void saveProducer (Producer producer){
        String sql = "INSERT INTO `anime_store`.`producer` (`name`) VALUES (?);";
        try(Connection conn = ConnectionFactory.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, producer.getName());
            int arrowsAffected = ps.executeUpdate();
            if(arrowsAffected > 0 ) {
                log.info("Success arrows affected = '{}'", arrowsAffected);
            }
        } catch (SQLException e) {
            log.error("Error while trying insert producer '{}'", producer.getName(), e);
        }
    }

    public static void updateProducer (Producer producer){
        String sql = "UPDATE `anime_store`.`producer` SET `name` = ? WHERE (`id` = ?);";
        try(Connection conn = ConnectionFactory.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, producer.getName());
            ps.setInt(2,producer.getId());
            int arrowsAffected = ps.executeUpdate();
            if(arrowsAffected > 0) {
                log.info("Success to update, arrows affected = '{}'", arrowsAffected);
            }

        } catch (SQLException e) {
            log.error("Error while trying update Object ",e);
        }
    }

    public static Optional<Producer> findById(int id) {
        String sql = "SELECT * FROM anime_store.producer WHERE id = ?;";
        try(Connection conn = ConnectionFactory.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                Producer producer = Producer.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .build();
                return Optional.of(producer);
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
