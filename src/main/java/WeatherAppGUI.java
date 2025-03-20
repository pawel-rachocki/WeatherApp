import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;

public class WeatherAppGUI extends JFrame {
    private Button getDataButton;
    private JTextField cityField;
    private JTextField countryField;
    private JTextArea resultArea;

    public WeatherAppGUI(){
        setTitle("Paweł Rachocki - Weather APP");
        setSize(400,400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //Input data Panel - 2x JTextField (city + country) + 1x button (getData)
        JPanel inputPanel = new JPanel(new GridLayout(3,2));
        inputPanel.add(new Label("City:"));
        this.cityField = new JTextField();
        inputPanel.add(cityField);
        inputPanel.add(new Label("Country:"));
        this.countryField = new JTextField();
        inputPanel.add(countryField);
        this.getDataButton = new Button("Get Data");
        inputPanel.add(getDataButton);

        // Result View

        this.resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // Add all the components to JFrame
        add(inputPanel,BorderLayout.NORTH);
        add(scrollPane,BorderLayout.CENTER);

        // Listeners
        getDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getData();
            }
        });
    }

    // Method for getting data on button click - referring to Service class
    private void getData(){
        String city = this.cityField.getText().trim();
        String country = this.countryField.getText().trim();

        // Check if fields aren't empty
        if (city.isEmpty() || country.isEmpty()){
            this.resultArea.setText("Error: City and country fields cannot be empty.");
            return;
        }
        Service service = new Service(country);
        String weatherInfo;
        try {
           weatherInfo = service.getWeather(city);
           if (weatherInfo.contains("Error")){
               this.resultArea.setText("Error: Couldn't fetch forecast data for " + city + ", " + country + ". Please check the inputs and try again.");
           }else{
               this.resultArea.setText(weatherInfo);
           }
        }catch (Exception exception){
            this.resultArea.setText("Error: Couldn't fetch forecast data.");
            exception.printStackTrace();
        }

    }
}
