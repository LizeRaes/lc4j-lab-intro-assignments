import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.structured.StructuredPrompt;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.structured.Description;
import dev.langchain4j.service.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.Duration.ofSeconds;
import static java.util.Arrays.asList;

public class _3_AiServices {

    // AI Services allow you to declare what you want your services to do via an interface.
    // You can declare the input variables, the output type and more detailed instructions.

    static ChatLanguageModel model = OpenAiChatModel.builder()
            .apiKey(ApiKeys.OPENAI_API_KEY)
            .timeout(ofSeconds(60))
            .build();

    static class BasicAIService {

        // -----------------------
        // a. Basic AIService
        // -----------------------
        // Assignment:
        //      - declare a basic AIService interface with one method chat
        //      - create the AIService and use it to generate a message

        // 1. AIService interface declaration
        interface Assistant {
            String chat(String message);
        }

        public static void main(String[] args) {
            // 2. Create AIService
            Assistant assistant = AiServices.create(Assistant.class, model);

            // 3. Use AIService
            String userMessage = "Translate 'Plus-Values des cessions de valeurs mobilières, de droits sociaux et gains assimilés'";
            String answer = assistant.chat(userMessage);
            System.out.println(answer);
        }
    }

    static class AIServiceWithVariablesAndMessagesAndOutputType {

        // -----------------------
        // b. Add Variables, Messages and an output type to AIServices
        // -----------------------
        // Assignment:
        //      - in TextUtils AIService interface, declare methods to
        //          - translate a String text into a String language
        //          - summarize a String text in int n bullet points
        //          - extract a LocalDateTime from a String text
        //      - create your AIService and test it out

        // 1. AIService interface declaration
        interface TextUtils {

            @SystemMessage("You are a professional translator into {{language}}")
            @UserMessage("Translate the following text: {{text}}")
            String translate(@V("text") String text, @V("language") String language);

            @SystemMessage("Summarize every message from the user in {{n}} bullet points. Provide only bullet points.")
            List<String> summarize(@UserMessage String text, @V("n") int n);

            @UserMessage("Extract date and time from {{it}}")
            LocalDateTime extractDateTimeFrom(String text);
        }

        public static void main(String[] args) {

            // 2. Create AIService
            TextUtils utils = AiServices.create(TextUtils.class, model);

            // 3. Use AIService
            // Try out translator service
            String translation = utils.translate("Hello, how are you?", "italian");
            System.out.println(translation); // Ciao, come stai?

            String text = "AI, or artificial intelligence, is a branch of computer science that aims to create "
                    + "machines that mimic human intelligence. This can range from simple tasks such as recognizing "
                    + "patterns or speech to more complex tasks like making decisions or predictions.";

            // Try out summarizer
            List<String> bulletPoints = utils.summarize(text, 3);
            bulletPoints.forEach(System.out::println);

            // Try out DateTime extractor
            text = "The tranquility pervaded the evening of 1968, just fifteen minutes shy of midnight,"
                    + " following the celebrations of Independence Day.";
            LocalDateTime dateTime = utils.extractDateTimeFrom(text);
            System.out.println(dateTime); // 1968-07-04T23:45
        }
    }


    static class AIServicePOJOWithDescriptions {

        // --------------------------------
        // b. AIService that returns a POJO
        // --------------------------------
        // Assignment:
        //      - decorate the fields in Recipe with @Description in a similar manner as the title. Be creative!
        //      - use the chef AIService that is provided and have fun generating recipes

        // 1. POJO definition
        static class Recipe {

            @Description("short title, 3 words maximum")
            private String title;

            @Description("short description, 2 sentences maximum")
            private String description;

            @Description("each step should be described in 6 to 8 words, steps should rhyme with each other")
            private List<String> steps;

            private Integer preparationTimeMinutes;

            @Override
            public String toString() {
                return "Recipe {" +
                        " title = \"" + title + "\"" +
                        ", description = \"" + description + "\"" +
                        ", steps = " + steps +
                        ", preparationTimeMinutes = " + preparationTimeMinutes +
                        " }";
            }
        }

        // 2. AIService interface declaration
        interface Chef {
            Recipe createRecipeFrom(String... ingredients);
        }

        public static void main(String[] args) {

            // 3. Create AIService
            Chef chef = AiServices.create(Chef.class, model);

            // 4. Use AIService

            Recipe recipe = chef.createRecipeFrom("cucumber", "tomato", "feta", "onion", "olives", "lemon");

            System.out.println(recipe);
        }
    }
}
