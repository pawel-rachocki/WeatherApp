import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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

    public String getWeather(String city){
        StringBuilder result = new StringBuilder();
        return "";
    }
    public Double getRate(){
        return 0.0;
    }

    public Double getNBPRate(){
        return 0.0;
    }

//    public static void main(String[] args) {
//        String country = "Poland";
//        Service service = new Service(country);
//        String apiKey = service.loadApiKey("OPENWEATHERMAP_API_KEY");
//        System.out.println("Api key: " + apiKey);
//
//        if (apiKey.isEmpty() || apiKey == null){
//            System.err.println("Error loading api key");
//        }else{
//            System.out.println("Api key loaded");
//        }
//    }
}
