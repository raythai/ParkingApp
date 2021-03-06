package com.parkingapp.connection;

import com.parkingapp.exception.ParkingAppException;
import com.parkingapp.utility.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pooja on 4/13/2015.
 *  Class to handle SF PARK APIs
 */
public class SFParkHandler {

    /**
     * calls SF API availability service to check  available parking spots within the given radius of the given location.
     * @param latitude latitude of the search location
     * @param longitude longitude of the search location
     * @param radius radius for the parking spot search
     * @return response of availability service API
     * @throws ParkingAppException
     */
    public StringBuilder callAvailabilityService(String latitude, String longitude, String radius) throws ParkingAppException{

        List<String> parameters = new ArrayList<String>();
        parameters.add("lat=" + latitude);
        parameters.add("long=" + longitude);
        parameters.add("radius=" + radius);
        parameters.add("uom=mile");
        parameters.add("method=availability");
        parameters.add("response=xml");
        RESTConnectionHandler restHandler = RESTConnectionHandler.getRESTConnectionHandler();

        String url = restHandler.generateURL(Constants.SF_PARK_URI + Constants.SF_PARK_AVAILABILITY_SERVICE, parameters);
        StringBuilder response =  restHandler.connect(url);
        return response;
    }

}
