# 🌦 Paweł Rachocki - Weather App

A simple Java desktop application using Swing to fetch and display current weather data based on the city and country input. It uses the [OpenWeatherMap API](https://openweathermap.org/api) to get current weather information and geographic coordinates.

## 📦 Features

- User-friendly GUI built with `Swing`
- Real-time weather data (temperature & description)
- Fetches geographic coordinates dynamically
- Displays errors for incorrect input or failed requests

## 🚀 Getting Started

### Prerequisites

- Java 8+
- Internet connection (for API requests)
- OpenWeatherMap API key

### Project Structure

📁 src/ ├── Main.java ├── Service.java └── WeatherAppGUI.java
📁 env/ └── api_keys.properties|

### 🔑 API Key Setup

Create a file: `env/api_keys.properties` and add your OpenWeatherMap API key like this:
OPENWEATHERMAP_API_KEY=your_api_key_here


### 🛠 How to Run

1. Compile the code:
   ```bash
   javac Main.java Service.java WeatherAppGUI.java
   java Main

### 🖼 Application GUI

  Enter City and Country Code (e.g., London, GB)
  Click "Get Data"
  See the current weather info displayed

### 📃 License

This project is for educational purposes.
