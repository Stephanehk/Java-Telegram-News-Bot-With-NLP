# Java-Telegram-News-Bot-With-NLP
This project allows the user to retrieve the latest news about a specific topic, or just in general, using Telegram.

Just by the way I would love to hear of any other sugestions you guys have for improving this project. It is my first real project in Java and I am only 14 years old.

## Examples:


## How does it work?

First, the user types in a query such as "whats the technology news?". The program reads this sentence, tokenizes it, and then tags each individual token with what part of speech it is.Then the program checks if the sentence matches the sentence "whats the news?" or similar variations of that. If it does, the program retrieves the first few artciels on google news and displays their titles with a link.

If however, the query is more specific such as "whats the technology news?" or "what is going on in Syria?" (or once again any variation of these two sentences), the program recognizes the noun (unless the noun is news) and retrieves a few of the google news articles related to the desired search. 

The third type of query is one where there is multipke nouns such as "Whats happening in New York City?" or "Whats the news with the presidential elections?". The program overcomes this challenge by storing all the nouns in the sentence in an arraylist, and then counts them to see how many their are. If there is more then 1 noun (meaning there is some sort of conjuntion), then the program concatenates the arraylist and searches for the whole entire thing.


Some problems to exist in this code. One of the main ones is that if the user types in "Whats going on in new york?" (without the capital letters) then the program only searches for "york" instead of "new york"

