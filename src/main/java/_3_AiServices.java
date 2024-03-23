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
        //      - declare a basic AIService interface Assistant with one method chat
        //      - create the AIService and use it to generate a message

        // 1. AIService interface declaration

        public static void main(String[] args) {
            // 2. Create AIService


            // 3. Use AIService
            String userMessage = "Translate 'Plus-Values des cessions de valeurs mobilières, de droits sociaux et gains assimilés'";
            String answer = null;
            System.out.println(answer);
        }
    }

    static class AIServiceWithVariablesAndMessagesAndOutputType {

        // -----------------------
        // b. Add Variables, Messages and an output type to AIServices
        // -----------------------
        // Assignment:
        //      - in TextUtils AIService interface, declare methods to
        //          - 'translate' a String text into a String language
        //          - 'summarize' a String text in int n bullet points
        //          - extractDateTimeFrom a String, returning a LocalDateTime
        //      - create your AIService and test it out

        // 1. AIService interface declaration
        interface TextUtils {


        }

        public static void main(String[] args) {

            // 2. Create AIService
            TextUtils utils = null;

            // 3. Use AIService
            // Try out translator service (uncomment)
            // String translation = utils.translate("Hello, how are you?", "italian");
            // System.out.println(translation);

            String text = "AI, or artificial intelligence, is a branch of computer science that aims to create "
                    + "machines that mimic human intelligence. This can range from simple tasks such as recognizing "
                    + "patterns or speech to more complex tasks like making decisions or predictions.";

            // Try out summarizer (uncomment)
            // List<String> bulletPoints = utils.summarize(text, 3);
            // bulletPoints.forEach(System.out::println);

            // Try out DateTime extractor (uncomment)
            text = "The tranquility pervaded the evening of 1968, just fifteen minutes shy of midnight,"
                    + " following the celebrations of Independence Day.";
            // LocalDateTime dateTime = utils.extractDateTimeFrom(text);
            // System.out.println(dateTime);
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

            private String description;

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

            Recipe recipe = chef.createRecipeFrom();

            System.out.println(recipe);
        }
    }
}
