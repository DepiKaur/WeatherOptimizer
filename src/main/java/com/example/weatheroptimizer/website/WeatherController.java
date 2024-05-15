package com.example.weatheroptimizer.website;

import com.example.weatheroptimizer.service.WeatherAggregator;
import com.example.weatheroptimizer.util.CustomDateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WeatherController {
    private WeatherAggregator aggregator;

    public WeatherController(WeatherAggregator aggregator) {
        this.aggregator = aggregator;
    }

    @GetMapping("/weather/forecast")
    public String getForecast(Model model) {
        String dateFormatted = CustomDateTimeFormatter.getFormattedDateTime(aggregator.getBestForecast().validTime());
        model.addAttribute("dateTime", dateFormatted);
        model.addAttribute("forecast", aggregator.getBestForecast());
        return "weatherForecast";
    }
}
