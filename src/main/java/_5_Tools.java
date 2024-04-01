import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public class _5_Tools {

    // Tools are java methods that the model can call.
    // When the tool has a clear description, the model will know when to call it and which input arguments to provide.
    // Tool calls result in a higher latency

    // -----------------------
    // Assignment:
    //      - annotate the two tools in UserInformationRetriever with a clarifying description as @Tool
    //      - have a look at how @V is used in the GamingBot AIService interface declaration
    //      - add the tools to the AIService
    //      - observe the result when calling gamingbot.chat()

    // 1. Define tools
    static class UserInformationRetriever {

        String getUserNameFromEmail(String email) {
            // For illustration purpose we print the call to console
            System.out.println("Called getUserNameFromEmail() with email address='" + email + "'");
            // Here we can write any java method, using maths, database queries or whatever we want
            return "Darth Vader";
        }

        int lastCompletedGameLevel(String email) {
            System.out.println("Called lastCompletedGameLevel with email address =" + email);
            // Return a dummy for illustration purpose
            return 4;
        }
    }

    interface GamingBot {
        @SystemMessage("You are a gaming chatbot that invites the user in an enticing way to start the next level of Forth Fantastic. Speak to the user using their full user name.")
        @UserMessage("Email address: {{useremail}} User Message: {{usertext}}")
        String chat(@V("useremail") String email, @V("usertext") String userText);
    }


    public static void main(String[] args) {

        ChatLanguageModel model = OpenAiChatModel.withApiKey(ApiKeys.OPENAI_API_KEY);
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);

        GamingBot gamingBot = AiServices.builder(GamingBot.class)
                .chatLanguageModel(model)
                .chatMemory(chatMemory)
                .build();

        String answer = gamingBot.chat("vader@hotmail.com","What should I do next?");

        System.out.println();
        System.out.println(answer);
    }
}
