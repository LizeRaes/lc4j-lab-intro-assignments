import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.checkerframework.checker.units.qual.A;

public class _0_KeyTest {
    public static void main(String[] args) {
        ChatLanguageModel model = OpenAiChatModel.withApiKey(ApiKeys.OPENAI_API_KEY);
        System.out.println("Answer A: " + model.generate("say 'your key is working'"));
    }
}
