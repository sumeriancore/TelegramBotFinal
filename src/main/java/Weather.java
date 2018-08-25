import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Weather {
    //03ad7d706d1d86f541979cc4c3e1946f
    public static String getWeather(String message, Model model) throws IOException {
         URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + message + "&units=metric&appid=03ad7d706d1d86f541979cc4c3e1946f");

        Scanner in = new Scanner((InputStream) url.getContent());

        String result = "";

        while(in.hasNext()){
            result += in.nextLine();
        }

        JSONObject object = new JSONObject(result);

        model.setName(object.getString("name"));

        JSONObject main = object.getJSONObject("main");
        model.setTemp(main.getDouble("temp"));
        model.setHumidity(main.getDouble("humidity"));

        String weather = "";



        JSONArray getArray = object.getJSONArray("weather");
        for(int i = 0; i < getArray.length(); i++){
            JSONObject obj = getArray.getJSONObject(i);
            model.setMain((String)obj.get("main"));
        }


        switch (model.getMain()){
            case "Rain":
                weather = "\u2614";
                break;
            case "Clouds":
                weather = "\u2601";
                break;
            case "Clear":
                weather = "\u2600";
                break;
            case "Thunderstorm":
                weather = "\u26A1";
                break;
        }

        return "\uD83C\uDFEB" + "Город"+ "\uD83C\uDFEB" + " - " + model.getName() + "\n" +
                "\u2600" + "Температура"+ "\u2600" + " - " + model.getTemp() + "C" + "\n" +
                "\uD83D\uDCA7" + "Влажность" + "\uD83D\uDCA7" + " - " + model.getHumidity() + "%" + "\n" +
                "\uD83D\uDCD6" + "Информация о погоде" + "\uD83D\uDCD6" + " - " + model.getMain() + " " +
                weather + weather + weather + "\n" ;
    }
}
