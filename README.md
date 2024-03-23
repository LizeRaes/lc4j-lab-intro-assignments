# LangChain4j Intro

These exercises will make you familiar with the basic blocks needed for building apps around AI models with LangChain4j.
You'll need
- Java 8 or higher
- Maven


### Building Blocks of LangChain4j

<img src='https://raw.githubusercontent.com/amithkoujalgi/ollama4j/65a9d526150da8fcd98e2af6a164f055572bf722/ollama4j.jpeg' width='200' alt="langchain4j-building-blocks">

## Setup
- obtain an OpenAI API key to interact with OpenAI's models. Store the key as environment variable `OPENAI_API_KEY` and restart your IDE if needed
- add the following dependencies to your `pom.xml

LangChain4j base dependency:
```xml  
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId>
    <version>0.28.0</version>
</dependency>
```
LangChain4j OpenAI Integration dependency:   
```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai</artifactId>
    <version>0.28.0</version>
</dependency>
```

## Workshop Content

We will explore the following base components of LangChain4j:

1. [Text Generation](#1-text-generation)
2. [Image Generation](#2-image-generation-)
3. [AIServices](#3-aiservices-) 
4. [Memory](#4-memory-)
5. [Tools](#5-tools)
6. [RAG (chat with your documents)](#6-rag-chat-with-your-documents-)

To get the most out of this workshop, tackle them class per class. 
Per class, first read the intro here in the `README`. Then dive into the class where you will find the assignments and some prepared structure.
By giving you a structure to work in, you can spend your focus on learning to master LangChain4j, instead of losing time on setup.

### 1. Text Generation
Large Language Models take an input text (prompt) and return an answer.
IMAGE OF IN OUT
If we want to make the answer better, we can either tweak the prompt (prompt engineering), choose a better suited model, or finetune our model.
Models can be used 
- via a provider (SaaS model - commercial models)
- run them in our private cloud (open source models)
- run them on a local machine (open source models), using a framework like Ollama
LangChain4j provides the following integrations with model providers
IMAGE OF INTEGRATIONS
Have a look in the `_1_TextGeneration` class to see how to connect to a model and interact with it.

### 2. Image Generation     
The image models that are supported in LangChain4j take a text input and turn it into an image, or vice versa.
We currently support Dall-E and Gemini.
Have a look in the `_2_ImageGeneration` class to see how to use these image models in LangChain4j. 

### 3. AIServices         
AIServices are the work horse of the LangChain4j library. 
You can inform the model what to do by using annotations and input and output variable types.
AIServices can be augmented with memory, tools and RAG, as we will see later.
Have a look how versatile and easy to use they are in `_3_AiServices`

### 4. Memory 
Models and AiServices are stateless, so we have to take care of the memory. For this we use 
- `SystemMessage` (the general instruction for the model)
- `UserMessages` (what we said)
- `AiMessages` (what the model answered)
We can store all of these in a ChatMemory as follows
IMAGE CHATMEMORY
If we want to steer the model to reply in a format we want, we can use the few-shot technique, where we populate the memory with fake UserMessages and AiMessages.
The model will then try to behave like the former AiMessages.
There are more advanced ways to handle memory, eg. by persisting it and working with user IDs. 
You can explore these advanced setups in [langchain4j-examples/tutorials](https://github.com/langchain4j/langchain4j-examples/tree/main/tutorials/src/main/java).
Have a look in `_4_Memory` to get acquainted with LangChain4j's ChatMemory.

### 5. Tools
Some supported models have the ability to call tools when they deem it necessary.
You can define java methods that can perform any action (like database operations, calculations, ...) and let the model know these tools are available.
The model get the tool name, input and output parameters and optionally a description. 
Based on that information it can choose to call the tool and set correct input arguments.
It will then use the result of the tool call (if any) to help in constructing the right answer.
It's important to know that tools increase the latency of the final response, because more calls to the LLM are made in the background.
In `_5_Tools` you can try it out for yourself and observe the tool calls that are made behind the scene. 

IMAGE TOOLS

### 6. RAG (chat with your documents)  
RAG, or retrieval-augmented generation is a pattern to fetch relevant parts from your data and stuff them in the context window together with the original question.
This allows you to ask questions about your own set of documents, make sure the LLM complies with your terms of use, or create images based on your own story.
RAG is a cheaper way to give the model knowledge about your documents than finetuning.
RAG consists of two phases that will typically be two separate processes:
1. Ingestion phase: the documents (files, websites, ...) are loaded, splitted, turned into meaning vectors (embeddings) and stored in an embedding store
2. Retrieval phase: with every user prompt, the relevant fragments of our documents are collected by comparing the meaning vector of the prompt with the vectors in the embedding store. The relevant segments are then passed along to the model together with the original question.
IMAGE RAG

```plaintext
Congratulations, you now master the most important building blocks of the LangChain4j repository!
You're ready to start building more complex AI-powered applications. With GenAI, the sky is the limit!
```

## Further Resources                                   
For a deeper dive, have a look at:
- [LangChain4j Documentation](https://github.com/langchain4j/langchain4j-examples/tree/main/tutorials/src/main/java)
- [Tutorials](https://github.com/langchain4j/langchain4j-examples/tree/main/tutorials/src/main/java)
- [Examples for all dependencies and frameworks integrating with LangChain4j](https://github.com/langchain4j/langchain4j-examples/tree/main/other-examples/src/main/java)
- [LangChain4j Repo](https://github.com/langchain4j/langchain4j) 
