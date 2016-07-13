package com.devfactory.assignment4.queue;

/**
 * Created by vaibhavtulsyan on 11/07/16.
 */

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class AWSQueueService {

    private AmazonSQS sqs;
    public AWSQueueService() {
        try {
            sqs = new AmazonSQSClient(new DefaultAWSCredentialsProviderChain());
        } catch (Exception e) {
            //TODO: cannot find credentials, throw exception
            System.out.println("Could not find credentials");
        }
        Region usEast1 = Region.getRegion(Regions.US_EAST_1);
        sqs.setRegion(usEast1);
        this.sqs = sqs;
    }

    public void sendMessage(Object object) {
        try {
            System.out.println("Sending message...");
            String myQueueUrl = sqs.getQueueUrl("cnu2016_vtulsyan_log_queue").getQueueUrl();
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(object);
            sqs.sendMessage(new SendMessageRequest(myQueueUrl, json));
            System.out.println("Sent message: " + json);
        } catch (AmazonServiceException ase) {
            //TODO: catch exception
            System.out.println(ase.getMessage());
        } catch (AmazonClientException ace) {
            //TODO: catch exception
            System.out.println(ace.getMessage());
        } catch(JsonProcessingException jpe) {
            //TODO: catch json processing exception
            System.out.println(jpe.getMessage());
        }
    }
}