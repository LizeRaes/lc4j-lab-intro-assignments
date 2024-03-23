import dev.langchain4j.chain.ConversationalRetrievalChain;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.retriever.EmbeddingStoreRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;
import static java.nio.file.FileVisitResult.CONTINUE;
import static java.util.stream.Collectors.joining;

public class _6_RAG {

    public static void main(String[] args) throws Exception {

        // -----------------------
        // a. Ingestion phase
        // -----------------------
        // Assignment:
        //      - have a close look at how we initialize an in-memory EmbeddingModel and EmbeddingStore
        //      - observe the blocks needed to build an EmbeddingStoreIngestor
        //      - observe how the files are ingested in ingestAllFiles() (we feed it the LangChain4j documentation)

        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();

        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(300, 30))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        ingestAllFiles(ingestor, Paths.get("src", "main", "resources", "langchain4jdocs"));


        // -----------------------
        // b. Retrieval phase
        // -----------------------
        //      - observe the building blocks needed to build a ContentRetriever
        //      - add the ContentRetriever to the CoderAgent AIService
        //      - enjoy interaction with our LangChain4j coding assistant over the console
        //
        // Don't believe it too much, this is an example of easy or naive RAG.
        // In order for it to return better results, it needs more tweaking.
        // If you want to know more, check out the Advanced RAG examples in langchain4j-examples.

        ContentRetriever retriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(3)
                .minScore(0.6)
                .build();

        ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

        ChatLanguageModel model = OpenAiChatModel.withApiKey(ApiKeys.OPENAI_API_KEY);

        CoderAgent coderAgent = AiServices.builder(CoderAgent.class)
                                .chatMemory(memory)
                                .chatLanguageModel(model)
                                .build();

        consoleInteraction(coderAgent);
    }

    public interface CoderAgent {
        @SystemMessage("""
        You are a coding assistant helping a developer write a java program with framework LangChain4j, 
        that helps developers interact with AI models and LLMs.
        Give concise advice about the user's question, be short and return code only if it makes sense for the question.
        """)
        String giveCodingAdvice(String userInput);
    }

    public static void ingestAllFiles(EmbeddingStoreIngestor ingestor, Path directory) {
        try {
            System.out.println("Ingesting ...");
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (!Files.isHidden(file)) { // Check if file is hidden
                        System.out.println("Ingesting " + file);
                        Document document = loadDocument(file, new TextDocumentParser());
                        ingestor.ingest(document);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    System.out.println("Ingesting failed");
                    System.err.println(exc);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void consoleInteraction(CoderAgent coderAgent) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Start typing your questions (type 'exit' to quit):");

        while (true) {
            System.out.print("> ");
            String userInput = scanner.nextLine().trim();

            if (userInput.equalsIgnoreCase("exit")) {
                System.out.println("Exiting...");
                scanner.close(); // Close the scanner
                return; // exit the loop
            }

            String answer = coderAgent.giveCodingAdvice(userInput);
            System.out.println(answer);
        }

    }

    private static Path toPath(String fileName) {
        try {
            URL fileUrl = _6_RAG.class.getResource(fileName);
            return Paths.get(fileUrl.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}