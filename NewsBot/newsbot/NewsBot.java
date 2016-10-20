package newsbot;


import java.io.IOException;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;



import java.util.Iterator;

//import javax.lang.model.element.Element;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class NewsBot extends TelegramLongPollingBot {


	@Override
	public String getBotUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean contains(String st1, String st2, String st3) {
		st1 = st1 == null ? "" : st1;
		st2 = st2 == null ? "" : st2;
		st3 = st3 == null ? "" : st3;

		return st1.toLowerCase().contains(st2.toLowerCase())
				&& st1.toLowerCase().contains(st3.toLowerCase());
	}
	


	@Override
	public void onUpdateReceived(Update update) {
		// TODO Auto-generated method stub
		if (update.hasMessage()) {
			Message message = update.getMessage();

			if (message.hasText()) {
				

				SendMessage sendMessageRequest = new SendMessage();
				sendMessageRequest.setChatId(message.getChatId()
						.toString());
				sendMessageRequest.enableMarkdown(true);

				ArrayList<String> nouns = new ArrayList<String>();

				// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

				// Initialize the tagger
				MaxentTagger tagger = new MaxentTagger(
						"taggers/english-left3words-distsim.tagger");

				// The sample string

				String sample1 = message.getText();

				// tokenize the sentence
				String delimeter2 = " ";
				// split the string using the delimeter and the parameter
				String[] words1 = sample1.split(delimeter2);
				System.out.println(words1);

				for (String word : words1) {
					System.out.println(word);
					// The tagged string
					String tagged = tagger.tagString(word);

					// Output the result
					System.out.println(tagged);
					if (tagger.tagString(word).contains("NNP")
							|| tagger.tagString(word).contains("NNS")
							|| tagger.tagString(word).contains("NN")
							|| tagger.tagString(word).contains("NNPS")
							|| tagger.tagString(word).contains("JJ")) {

						if (tagged.contains("whats") || tagged.contains("news")
								|| tagged.contains("Whats")
								|| tagged.contains("News")) {

						} else {
							nouns.add(word);
			
						}

					}

				}

				// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				if (nouns.size() == 0) {

					org.jsoup.nodes.Document doc = null;
					try {
						doc = (org.jsoup.nodes.Document) Jsoup.connect(
								"https://news.google.com/").get();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						sendMessageRequest.setText("Error connecting - please check your internet connection");
						try {
							sendMessage(sendMessageRequest);

						} catch (TelegramApiException a) {
							// TODO Auto-generated catch block
							a.printStackTrace();
						}
					}

					Elements el = doc.select("h2");

					int i = 0;

					for (org.jsoup.nodes.Element element : el) {


						if (element.select("a").attr("href") != null) {
							i++;

							sendMessageRequest.setText(element.select(
									"[class=titletext]").text()
									+ " - To read more "
									+ "[click here]("
									+ element.select("a").attr("href") + ")");

							try {
								sendMessage(sendMessageRequest);

							} catch (TelegramApiException a) {
								// TODO Auto-generated catch block
								a.printStackTrace();
							}

							if (i > 5) {
								break;
							}
						}

					}
				} else if (nouns.size() == 1) {
					System.out.println("Searching for " + nouns.get(0));
					
					try {
						System.out.println("Reading text...");

						org.jsoup.nodes.Document doc = Jsoup.connect("https://news.google.com/news/feeds?cf=all&ned=us&hl=en&q=" + nouns.get(0) + "&output=rss").get();

						Elements titleElement =  doc.select("title");
						Elements linkElement =  doc.select("link"); 

						Iterator<org.jsoup.nodes.Element> iter1 = titleElement.iterator();
						Iterator<org.jsoup.nodes.Element> iter2 = linkElement.iterator();
						
						int i2 = 0;
						
						while (iter1.hasNext() && iter2.hasNext()) {
							
							i2++;
							
						    Element titleElement1 = (Element) iter1.next();
						   
						    Element element2 = (Element) iter2.next();
						    if (element2.text().equals("http://news.google.com/news?hl=en&ned=us&q=" + nouns.get(0))) {
						    	
						    } else {
						    	sendMessageRequest.setText(titleElement1.text() + " - To read more " + "[click here](" + element2.text() + ")");
								sendMessage(sendMessageRequest);
						    }
						    
						    if (i2 > 5) {
						    	break;
						    }
						}

					} catch (IOException e) {
						// TODO Auto-generated catch block
						sendMessageRequest.setText("Error retrieving articles");
						
						try {
							sendMessage(sendMessageRequest);

						} catch (TelegramApiException a) {
							// TODO Auto-generated catch block
							a.printStackTrace();
						}
						
						e.printStackTrace();
					} catch (TelegramApiException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					
					String compoundNoun = "";
					
					
					for (int i = 0; i < nouns.size(); i++) {
						
						String noun = nouns.get(i);
						
						compoundNoun += noun + " ";
						
					}

			System.out.println("Searching for " + compoundNoun);
					
					try {
						System.out.println("Reading text...");

						org.jsoup.nodes.Document doc = Jsoup.connect("https://news.google.com/news/feeds?cf=all&ned=us&hl=en&q=" + compoundNoun + "&output=rss").get();

						Elements titleElement =  doc.select("title");
						Elements linkElement =  doc.select("link"); 

						Iterator<org.jsoup.nodes.Element> iter1 = titleElement.iterator();
						Iterator<org.jsoup.nodes.Element> iter2 = linkElement.iterator();
						
						int i2 = 0;
						
						while (iter1.hasNext() && iter2.hasNext()) {
							
							i2++;
							
						    Element titleElement1 = (Element) iter1.next();
						   
						    Element element2 = (Element) iter2.next();
						    if (element2.toString().contains("http://news.google.com/news?hl=en&amp;ned=us&amp;q=")) {
						    							    	
						    } else {
						    	sendMessageRequest.setText(titleElement1.text() + " - To read more " + "[click here](" + element2.text() + ")");
								sendMessage(sendMessageRequest);
						    }
						    
						    if (i2 > 5) {
						    	break;
						    }
						}

					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.print("failed");
						sendMessageRequest.setText("Error retrieving articles");
						
						try {
							sendMessage(sendMessageRequest);

						} catch (TelegramApiException a) {
							// TODO Auto-generated catch block
							a.printStackTrace();
						}
						
						e.printStackTrace();
					} catch (TelegramApiException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}

			}

		}

	}

	@Override
	public String getBotToken() {
		// TODO Auto-generated method stub
		return botConfig.BOT_TOKEN;
	}

}

class botConfig {
	public static final String BOT_USERNAME = "Stephaneaibot";
	public static final String BOT_TOKEN = "264738614:AAGzpvhSnDCWVSONBgNBtQa0kheZ_jGzIIM";

	public static void main(String[] args) {
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
		System.out.println("Compiled");
		try {
			telegramBotsApi.registerBot(new NewsBot());
		} catch (TelegramApiException e) {
			e.printStackTrace();

		}
	}

}
