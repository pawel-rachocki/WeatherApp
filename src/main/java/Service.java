import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import org.json.JSONObject;
import org.json.JSONArray;


public class Service {
    private String country;
    private String openWeatherMapApiKey;

    public Service(String country){
        this.country = country;
        this.openWeatherMapApiKey = loadApiKey("OPENWEATHERMAP_API_KEY");
    }

    private String loadApiKey(String keyName) {
        Properties properties = new Properties();
        try (InputStream inputStream = new FileInputStream("env/api_keys.properties")){
            properties.load(inputStream);
            return properties.getProperty(keyName);
        }catch (IOException exception){
            throw new RuntimeException("Can't load API key" + keyName, exception);
        }
    }

    public String getWeather(String city) {
        try {
            // Get geo coordinates
            JSONObject coordinates = getCoordinates(city);
            double lat = coordinates.getDouble("lat");
            double lon = coordinates.getDouble("lon");

            // Creating url for Current Weather API
            String apiUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + openWeatherMapApiKey + "&units=metric";
            URL url = new URL(apiUrl);

            // Open HTTP connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Check server response
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse JSON
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONObject main = jsonResponse.getJSONObject("main");
                JSONObject weather = jsonResponse.getJSONArray("weather").getJSONObject(0);

                // Specify needed data
                String cityName = jsonResponse.getString("name");
                double temperature = main.getDouble("temp");
                String weatherDescription = weather.getString("description");

                // Format return
                return "Current forecast in " + cityName + ":\n" +
                        "Temp: " + temperature + "°C\n" +
                        "Note: " + weatherDescription;
            } else {
                return "Error getting forecast data. Response Code: " + responseCode;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }
    public Double getRate(){
        return 0.0;
    }

    public Double getNBPRate(){
        return 0.0;
    }
    private JSONObject getCoordinates(String city) {
        try {
            // Creating URL for Geocoding API
            String apiUrl = "http://api.openweathermap.org/geo/1.0/direct?q=" + city + "," + country + "&limit=1&appid=" + openWeatherMapApiKey;
            URL url = new URL(apiUrl);

            // Open HTTP connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Check server response
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse JSON response
                JSONArray jsonArray = new JSONArray(response.toString());
                if (jsonArray.length() > 0) {
                    return jsonArray.getJSONObject(0); // Return 1st result
                } else {
                    throw new RuntimeException("Coordinates were not found for city: " + city);
                }
            } else {
                throw new RuntimeException("Error getting coordinates data. Response Code: " + responseCode);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }
}
