import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.Tokenizer;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.service.AiServices;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static dev.langchain4j.data.message.UserMessage.userMessage;

public class _4_Memory {

    // Models are stateless and don't remember the previous conversation
    // Memory makes them stateful
    static class AIServiceWithMemory {

        // ------------------------
        // a. Out-of-the-box Memory
        // ------------------------
        // Assignment:
        //      - create a ChatMemory storing a maximum of 10 messages
        //      - add it to the AIService
        //      - test it out (verify that it remembers what has been said before)

        // 1. Declare AIService
        interface Assistant {
            String chat(String message);
        }

        public static void main(String[] args) {

            // 2. Create ChatMemory
            ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);

            // 3. Create model
            ChatLanguageModel model = OpenAiChatModel.withApiKey(ApiKeys.OPENAI_API_KEY);

            // 4. Generate AIService with memory and model
            Assistant assistant = AiServices.builder(Assistant.class)
                    .chatLanguageModel(model)
                    .chatMemory(chatMemory)
                    .build();

            // 5. Use AIService with memory
            String answer = assistant.chat("Hello! My name is Gandalf.");
            System.out.println(answer);

            String answerWithName = assistant.chat("What is my name?");
            System.out.println(answerWithName);
        }
    }

    // The ChatMemory is flexible: we can add our own SystemMessage, UserMessage or AIMessage.
    // We usually pay per token and the context has a limit, so we have to limit the size of our memory.
    // We can set the maximum number of tokens, or implement a version of the ChatMemory that fits our needs.
    static class ManualMemoryIntervention {

        // -------------------------------------
        // b. Fake the memory to steer the model
        // -------------------------------------
        // Assignment:
        //      - create a ChatMemory storing a maximum of 1000 tokens
        //      - add the given SystemMessage (= model instruction)
        //      - inspect what happens in populateWithExamples, observe the difference between AiMessage and UserMessage
        //      - add the memory to the AIService
        //      - test it out and observe how it sticks to the format of the examples
        //      - try to trick it into answering other things

        public static void main(String[] args) {

            // 1. Initialize ChatMemory with token limiter
            Tokenizer tokenizer = new OpenAiTokenizer("gpt-3.5-turbo");
            ChatMemory chatMemory = TokenWindowChatMemory.withMaxTokens(1000, tokenizer);

            // 2. Add SystemMessage to instruct the model how to behave
            SystemMessage systemMessage = SystemMessage.from(
                    "You are a customer assistant for a phone manufacturer. " +
                            "You turn feedback into tickets for the internal engineering team, and send a friendly reply to the customer. " +
                            "If the topic is not about the phone's hardware or software, you politely refuse to answer");
            chatMemory.add(systemMessage);

            // Add some examples of fictive UserMessages and AIMessages to force the model to answer in our format.
            // This is called the few-shot method
            populateWithExamples(chatMemory);

            // 3. Create model
            ChatLanguageModel model = OpenAiChatModel.withApiKey(ApiKeys.OPENAI_API_KEY);

            // 4. Generate AIService with memory and model
            AIServiceWithMemory.Assistant assistant = AiServices.builder(AIServiceWithMemory.Assistant.class)
                    .chatLanguageModel(model)
                    .chatMemory(chatMemory)
                    .build();

            // 5. Use AIService with few-shot memory
            String answer = assistant.chat("How can your app be so slow? Please do something about it!");
            System.out.print(answer);
        }

        private static void populateWithExamples(ChatMemory chatMemory) {
            // Adding positive feedback example to history
            chatMemory.add(UserMessage.from(
                    "I love the new update! The interface is very user-friendly and the new features are amazing!"));
            chatMemory.add(AiMessage.from(
                    "Action: forward input to positive feedback storage\nReply: Thank you very much for this great feedback! We have transmitted your message to our product development team who will surely be very happy to hear this. We hope you continue enjoying using our product."));

            // Adding negative feedback example to history
            chatMemory.add(UserMessage
                    .from("I am facing frequent crashes after the new update on my Android device."));
            chatMemory.add(AiMessage.from(
                    "Action: open new ticket - crash after update Android\nReply: We are so sorry to hear about the issues you are facing. We have reported the problem to our development team and will make sure this issue is addressed as fast as possible. We will send you an email when the fix is done, and we are always at your service for any further assistance you may need."));

            // Adding another positive feedback example to history
            chatMemory.add(UserMessage
                    .from("Your app has made my daily tasks so much easier! Kudos to the team!"));
            chatMemory.add(AiMessage.from(
                    "Action: forward input to positive feedback storage\nReply: Thank you so much for your kind words! We are thrilled to hear that our app is making your daily tasks easier. Your feedback has been shared with our team. We hope you continue to enjoy using our app!"));

            // Adding another negative feedback example to history
            chatMemory.add(UserMessage
                    .from("The new feature is not working as expected. It’s causing data loss."));
            chatMemory.add(AiMessage.from(
                    "Action: open new ticket - data loss by new feature\nReply:We apologize for the inconvenience caused. Your feedback is crucial to us, and we have reported this issue to our technical team. They are working on it on priority. We will keep you updated on the progress and notify you once the issue is resolved. Thank you for your patience and support."));
        }
    }
}
