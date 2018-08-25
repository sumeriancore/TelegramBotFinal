import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    public static void main(String[] args) {
        ApiContextInitializer.init();

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        try{
            telegramBotsApi.registerBot(new Bot());
        }catch (TelegramApiRequestException e){
            e.printStackTrace();
        }
    }


    public void sendMsg(Message message, String text){

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString()); //в какой чат отвечать
        sendMessage.setReplyToMessageId(message.getMessageId()); //сообщение на которое отвечать
        sendMessage.setText(text);

        try {
            setButtons(sendMessage);
            sendMessage(sendMessage);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }



    public void onUpdateReceived(Update update) { //Метод приёма сообщений. Получает обновления через LongPoll

        Model model = new Model();
        Message message = update.getMessage();
        EmojiParser.EmojiTransformer emojiTransformer = new EmojiParser.EmojiTransformer() {
            @Override
            public String transform(EmojiParser.UnicodeCandidate unicodeCandidate) {
                return null;
            }
        };
        if(message != null && message.hasText()){

            switch (message.getText()){
                case "/help":
                    sendMsg(message, "Вас приветствует @WeatherTogetherBot"  +
                            EmojiParser.parseToUnicode(":alien:")+ "\n" +
                            "Я могу предоставить вам погоду из любой точки планеты" +
                            "\uD83C\uDF0F" +
                            "\n" +
                            "Просто отправь мне город и получишь погоду!" + "\u2600");
                    break;
                case "/setting":
                    sendMsg(message, "Прости, но я ещё в разработке!" + "\uD83D\uDE05");
                    break;
                case "/start":
                    sendMsg(message, "Привет, " + message.getChat().getFirstName() + "\uD83C\uDF7B" + "\n" +
                            "В каком городе" + " \uD83C\uDFEB" + " интересует погода?" + " \u2600");

                default:
                    if(!message.getText().equals("/start")) {
                        try {
                            sendMsg(message, Weather.getWeather(message.getText(), model));

                        } catch (IOException e) {
                            sendMsg(message, "Город" + " \uD83C\uDFEB" + message.getText() +
                                    " \uD83C\uDFEB" + " не найден!");
                        }
                    }
            }
        }

    }

    public void setButtons(SendMessage sendMessage){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();//создание клавиатуры
        sendMessage.setReplyMarkup(replyKeyboardMarkup);//связываем клавиатуру с сообщением
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);//подгонка размеров
        replyKeyboardMarkup.setOneTimeKeyboard(false);//скрывать клаву чи не

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();//строка клавиатуры

        keyboardFirstRow.add(new KeyboardButton("/help"));
        keyboardFirstRow.add(new KeyboardButton("/setting"));

        keyboardRowList.add(keyboardFirstRow);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);

    }

    public String getBotUsername() {
        return "WeatherTogetherBot";
    }

    public String getBotToken() {
        return "642623586:AAEdhUOnNM2N0284EnKNQOzquHiatgxMC8A";
    }
}
