package br.com.fiap.mgmtmedia.sqs.consumer;

import br.com.fiap.mgmtmedia.entity.MediaMetadata;
import br.com.fiap.mgmtmedia.exception.custom.MediaException;
import br.com.fiap.mgmtmedia.repository.MediaRepository;
import br.com.fiap.mgmtmedia.sqs.model.MediaMessage;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class SQSConsumer {

    @Value("${variables.aws.queue-name-result}")
    private String consumerQueueName;

    @Autowired
    private AmazonSQS amazonSQSClient;

    private final MediaRepository mediaRepository;

    // TODO: add this class to context (Bean)
    private final ModelMapper modelMapper = new ModelMapper();

    @Scheduled(fixedDelay = 5000)
    public void consumeResultMessages() {
        String queueUrl = null;

        try {
            queueUrl = amazonSQSClient.getQueueUrl(consumerQueueName).getQueueUrl();
        } catch (Exception e) {
            log.error("Failed to get SQS queue URL for {}: {}", consumerQueueName, e.getMessage(), e);
            return;
        }

        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest()
                .withQueueUrl(queueUrl)
                .withMaxNumberOfMessages(10)
                .withWaitTimeSeconds(10);

        try {
            ReceiveMessageResult receiveMessageResult = amazonSQSClient.receiveMessage(receiveMessageRequest);
            List<Message> messages = receiveMessageResult.getMessages();

            if (messages.isEmpty()) {
                log.info("No messages found in the queue.");
                return;
            }

            for (Message message : messages) {
                // Separate this logic into a method
                try {
                    processMessage(message);
                    deleteMessage(queueUrl, message);
                } catch (Exception e) {
                    log.error("Error processing message {}: {}", message.getMessageId(), e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            log.error("Failed to consume messages from SQS queue: {}", e.getMessage(), e);
        }
    }

    private void processMessage(Message message) {
        log.info("Processing message with ID: {}", message.getMessageId());
        log.info("Message body: {}", message.getBody());

        MediaMessage mediaMessageReceived = modelMapper.map(message.getBody(), MediaMessage.class);

        MediaMetadata mediaMetadata = mediaRepository.findById(mediaMessageReceived.mediaId().toString()).orElseThrow(
                () -> new MediaException("Media not found")
        );

        mediaMetadata.setStatus(mediaMessageReceived.status());
        mediaMetadata.setZippedFolderPath(mediaMessageReceived.zippedPath());

        mediaRepository.save(mediaMetadata);
    }

    private void deleteMessage(String queueUrl, Message message) {
        try {
            amazonSQSClient.deleteMessage(queueUrl, message.getReceiptHandle());
            log.info("Successfully deleted message with ID: {}", message.getMessageId());
        } catch (Exception e) {
            log.error("Failed to delete message {}: {}", message.getMessageId(), e.getMessage(), e);
        }
    }
}
