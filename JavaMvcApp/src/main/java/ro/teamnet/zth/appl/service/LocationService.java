package ro.teamnet.zth.appl.service;

import ro.teamnet.zth.appl.domain.Location;

import java.util.List;

/**
 * Created by user on 15.07.2016.
 */
public interface LocationService {

    List<Location> findAllLocations();

    Location findOneLocation(Long id);

    void deleteOneLocation(Long id);
}
