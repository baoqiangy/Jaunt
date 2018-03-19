package csc445.missouriwestern.edu.jaunt.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by byan on 3/17/2018.
 */

@Dao
public interface CityDao {

    @Query("SELECT * FROM city")
    List<City> getAll();

    @Query("SELECT * FROM city where name LIKE :name AND country LIKE :country")
    List<City> findByNameCountry(String name, String country);

    @Query("SELECT COUNT(*) from city")
    int countCities();

    @Query("DELETE FROM city")
    void nukeTable();

    @Insert
    void insertAll(City... cities);

    @Delete
    void delete(City city);
}
