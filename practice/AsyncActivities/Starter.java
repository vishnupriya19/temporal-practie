package io.workshop.practice.AsyncActivities;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Starter {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
        WorkflowClient client = WorkflowClient.newInstance(service);
        GreetingWorkflow workflow =
                client.newWorkflowStub(
                        GreetingWorkflow.class,
                        WorkflowOptions.newBuilder()
                                .setWorkflowId("greetingWorkFlow")
                                .setTaskQueue(AsyncActivityWorker.TASK_QUEUE)
                                .build());

        CompletableFuture<String> greeting = WorkflowClient.execute(workflow::getGreeting, "World");
        System.out.println(greeting.get());
        System.exit(0);
        /*try {
            // Wait for workflow execution to complete and display its results.
            System.out.println(greeting.get());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }*/
    }
}

