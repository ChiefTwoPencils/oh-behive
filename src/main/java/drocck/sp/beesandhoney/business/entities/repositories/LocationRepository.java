package drocck.sp.beesandhoney.business.entities.repositories;

import drocck.sp.beesandhoney.business.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Chai on 10/10/2015.
 */

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findAll();
    Location findById(Long id);
}
