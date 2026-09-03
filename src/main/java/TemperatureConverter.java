public class TemperatureConverter {
    public double fahrenheitToCelsius(double fahrenheit){
        return (fahrenheit - 32) * 5/9;
    }
    public double celsiusToFahrenheit(double celsius){
        return (celsius * 5/9 )+ 32 ;
    }
    public boolean isExtremeTemperature(double celsius){
        if(celsius < -40 || celsius > 50 ){
            return true;
        }else {
            return false;
        }
    }
}
