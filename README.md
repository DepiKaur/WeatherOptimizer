# WeatherOptimizer

This application shows the best weather forecast (after 24 hours) for station Liljeholmen in Stockholm.

The required data is gathered from the following weather services (or client api(s)):

1. [DWD](https://api.open-meteo.com/v1/dwd-icon?latitude=59.3094&longitude=18.0234&hourly=temperature_2m,relative_humidity_2m,precipitation&timezone=Europe%2FBerlin&forecast_days=2)
2. [MET](https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=59.3110&lon=18.0300)
3. [SMHI](https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/18.0300/lat/59.3110/data.json)

### Best Weather
In this application, only temperature and humidity are taken into account.

That with the max temperature value is appreciated and considered best.
However, if two services return the same temperature value, then the one with greater humidity is preferred.

### Get forecast
Run this application and click on one of the following links to see the tomorrow's forecast:

1. [As a webpage](http://localhost:8080/weather/forecast)
2. [As Json object](http://localhost:8080/api/v1/weather/forecast)

### Useful resources
[Create URI with Query Parameters](https://www.baeldung.com/webflux-webclient-parameters)

[Generate Java classes from JSON object](https://www.jsonschema2pojo.org/)