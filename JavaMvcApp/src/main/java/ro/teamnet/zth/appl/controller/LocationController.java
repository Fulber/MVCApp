package ro.teamnet.zth.appl.controller;

import ro.teamnet.zth.api.annotations.MyController;
import ro.teamnet.zth.api.annotations.MyRequestMethod;

/**
 * Created by user on 14.07.2016.
 */
@MyController(urlPath = "/locations")
public class LocationController {

    @MyRequestMethod(urlPath = "/all")
    public String getAllLocations() {

        return "allLocations";
    }

    @MyRequestMethod(urlPath = "/one")
    public String getOneLocation() {

        return "oneRandomLocation";
    }
}
