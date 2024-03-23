import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;

import java.time.Duration;

public class _1_TextGeneration {


    public static void main(String[] args) {

        // -------------------
        // a. the shortest way
        // -------------------
        // assignment:
        //      - create a ChatLanguageModel of type OpenAi
        //      - set your key
        //      - generate an answer based on your input and print to console
        ChatLanguageModel modelA = OpenAiChatModel.withApiKey(ApiKeys.OPENAI_API_KEY);
        String answerA = modelA.generate("Say Hello World");
        System.out.println("Answer A: " + answerA);

        // LangChain4j has many more integrations with model providers, such as Gemini (Google Vertex) and Mistral AI
        // Communicating with local models is also supported, eg. via Ollama
        // There is also an integration with HuggingFace, where you can run any open source model as well as your own finetuned models

        // --------------------------
        // b. control more parameters
        // --------------------------
        // and add logging (set tinylog.properties writer level to 'debug')
        // assignment:
        //      - create another ChatLanguageModel of type OpenAi
        //      - set your key, some model parameters and set logging on for requests and responses
        //      - generate an answer based on your input and print to console
        ChatLanguageModel modelB = OpenAiChatModel.builder()
                .apiKey(ApiKeys.OPENAI_API_KEY)
                .timeout(Duration.ofSeconds(30))
                .temperature(0.5)
                .logRequests(true)
                .logResponses(true)
                .build();

        String answerB = modelA.generate("Name five words that developers hate to hear most");
        System.out.println("Answer B: " + answerB);

        // Parameter settings depend on the model, and can usually be found on the model provider's website
        // Eg. for OpenAI's models: https://platform.openai.com/docs/api-reference/chat/create
        // The cost per token can be found in the provider's terms of use (not applicable for locally running models)

        // -----------------------------
        // c. add streaming (to console)
        // -----------------------------
        // assignment:
        //      - create a StreamingChatLanguageModel of type OpenAi
        //      - write an answer to the console and observe how it is rendered token per token
        StreamingChatLanguageModel modelC = OpenAiStreamingChatModel.withApiKey(ApiKeys.OPENAI_API_KEY);
        System.out.println("Answer C: ");

        modelC.generate("Write a poem about unicorns and grizzly bears"
                , new StreamingResponseHandler<AiMessage>() {
                    @Override
                    public void onNext(String s) {
                        System.out.println(s);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("Whoopsie!");
                    }
                });

    }
}
