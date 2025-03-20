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
            // Pobierz współrzędne geograficzne
            JSONObject coordinates = getCoordinates(city);
            double lat = coordinates.getDouble("lat");
            double lon = coordinates.getDouble("lon");

            // Tworzenie URL do zapytania Current Weather API
            String apiUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + openWeatherMapApiKey + "&units=metric";
            URL url = new URL(apiUrl);

            // Otwarcie połączenia HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Sprawdzenie odpowiedzi serwera
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Odczytanie odpowiedzi
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parsowanie odpowiedzi JSON
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONObject main = jsonResponse.getJSONObject("main");
                JSONObject weather = jsonResponse.getJSONArray("weather").getJSONObject(0);

                // Wyciągnięcie potrzebnych informacji
                String cityName = jsonResponse.getString("name");
                double temperature = main.getDouble("temp");
                String weatherDescription = weather.getString("description");

                // Formatowanie wyniku
                return "Pogoda w " + cityName + ":\n" +
                        "Temperatura: " + temperature + "°C\n" +
                        "Opis: " + weatherDescription;
            } else {
                return "Błąd podczas pobierania danych pogodowych. Kod odpowiedzi: " + responseCode;
            }
        } catch (Exception e) {
            throw new RuntimeException("Wystąpił błąd: " + e.getMessage(), e);
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
            // Tworzenie URL do zapytania Geocoding API
            String apiUrl = "http://api.openweathermap.org/geo/1.0/direct?q=" + city + "," + country + "&limit=1&appid=" + openWeatherMapApiKey;
            URL url = new URL(apiUrl);

            // Otwarcie połączenia HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Sprawdzenie odpowiedzi serwera
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Odczytanie odpowiedzi
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parsowanie odpowiedzi JSON
                JSONArray jsonArray = new JSONArray(response.toString());
                if (jsonArray.length() > 0) {
                    return jsonArray.getJSONObject(0); // Zwróć pierwszy wynik
                } else {
                    throw new RuntimeException("Nie znaleziono współrzędnych dla miasta: " + city);
                }
            } else {
                throw new RuntimeException("Błąd podczas pobierania współrzędnych. Kod odpowiedzi: " + responseCode);
            }
        } catch (Exception e) {
            throw new RuntimeException("Wystąpił błąd: " + e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        String country = "Poland";
        Service service = new Service(country);

        // testing
        String city = "Warsaw";
        String weatherInfo = service.getWeather(city);
        System.out.println(weatherInfo);
    }
}
