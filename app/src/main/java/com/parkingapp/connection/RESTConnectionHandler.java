package com.parkingapp.connection;

import android.os.StrictMode;

import com.parkingapp.exception.ParkingAppException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by pooja on 4/13/2015.
 * Singleton class for REST connection handling
 */
public class RESTConnectionHandler {

//  Same class should be used for calling both google maps and SF park APIs.

    private static RESTConnectionHandler restHandler ;
    private RESTConnectionHandler() {
           }

    public static RESTConnectionHandler getRESTConnectionHandler() {
        if(restHandler == null)
                restHandler = new RESTConnectionHandler();
        return restHandler;
    }

    /**
     * generates REST URL for passed URI parameters.
     * @param uri URI of the API to be accessed
     * @param parameters the parameters of the API
     * @return generated URL
     *         If URI is empty, exception is thrown
     */

    public String generateURL(String uri, List<String> parameters) throws  ParkingAppException{
        char uroConnector = '?';
        char paramConnector = '&';
        StringBuilder url = new StringBuilder();

        if(uri == null) {
            throw new ParkingAppException(" URI can not be null ");
        }
        url.append(uri);
        url.append(uroConnector);

        for(String param : parameters) {
           url.append(param);
           url.append(paramConnector);
        }
        url.deleteCharAt(url.length()-1);
        return url.toString();
    }

    /**
     * opens a HTTP call and buffers the response into String.
     * @param urlStr
     * @return API response string
     */
  public StringBuilder connect(String urlStr) throws ParkingAppException{

      //  String urlStr = "http://api.sfpark.org/sfpark/rest/availabilityservice?lat=37.792275&long=-122.397089&radius=0.25&uom=mile&method=availability&response=xml";
      // String uri = urlStr;
      BufferedReader rd = null;
      HttpURLConnection conn =null;
      try {

          int SDK_INT = android.os.Build.VERSION.SDK_INT;

          if (SDK_INT > 8)
          {
              StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                      .permitAll().build();
              StrictMode.setThreadPolicy(policy);
              //your codes here

          }
          URL url = new URL(urlStr);

          // Open a new connection for the passed URL
          conn =  (HttpURLConnection) url.openConnection();

          /* When the connection is successful, the response code is 200 OK,
            If not, the connection is unsuccessful. In case of unsuccessful connection,
            exception is thrown.
          */
          if (conn.getResponseCode() != 200) {
              throw new IOException(conn.getResponseMessage());
          }

          // Buffer the result into a string
          rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
          StringBuilder sb = new StringBuilder();
          String line;
          while ((line = rd.readLine()) != null) {
              sb.append(line);
          }
        return sb;
      } catch (Exception e) {
          throw new ParkingAppException(e,e.getMessage());
      }
        finally {
          if(rd != null) {
              try {
                  rd.close();
              } catch(IOException e) {
                  throw new ParkingAppException(e,e.getMessage());
              }

          }
          conn.disconnect();
      }
  }
}
