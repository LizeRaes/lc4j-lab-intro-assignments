
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.openai.OpenAiImageModel;
import dev.langchain4j.model.output.Response;

import java.net.URISyntaxException;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

public class _2_ImageGeneration {

    static class createPng {

        // -----------------------
        // a. generate a png image
        // -----------------------
        // Assignment:
        //      - connect to OpenAi's Dall-E ImageModel
        //      - generate and inspect your image
        public static void main(String[] args) {

            ImageModel model = null;

            Response<Image> response = null;
            System.out.println(response.content().url());
        }
    }

    static class savePngLocally {

        public static void main(String[] args) throws URISyntaxException {

            // -----------------------
            // a. handle and store a png image
            // -----------------------
            // Assignment:
            //      - connect to OpenAi's Dall-E ImageModel and set some more parameters
            //      - generate your image and store it in the root (file handling code provided)

            // TODO make an overview of the options in readme
            ImageModel model = null;

            Response<Image> response = model.generate("2 funny cats");

            // TODO figure out why it's null
            String base64Image = response.content().base64Data();
            System.out.println(base64Image);

          /*  byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            InputStream stream = new ByteArrayInputStream(imageBytes);

            OutputStream out = null;
            try {
                out = Files.newOutputStream(Paths.get("aiImage.png"));
                out.write(imageBytes);
                out.close();
            } catch (IOException e) {
                throw new RuntimeException("Error when writing image to disk: " + e);
            } */

            // TODO remove this method and check if always stored locally or only on .url()
            System.out.println(response.content().url());
        }
    }
}
