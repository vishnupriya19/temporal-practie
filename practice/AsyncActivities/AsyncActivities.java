package io.workshop.practice.AsyncActivities;

import io.temporal.activity.*;
import io.temporal.client.ActivityCompletionClient;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

/** Sample Temporal Workflow Definition that demonstrates asynchronous Activity Execution */
public class AsyncActivities {

    // Define the task queue name
    static final String TASK_QUEUE = "HelloAsyncActivityCompletionTaskQueue";

    // Define the workflow unique id
    static final String WORKFLOW_ID = "HelloAsyncActivityCompletionWorkflow";
    @WorkflowInterface
    public interface GreetingWorkflow {
        @WorkflowMethod
        String getGreeting(String name);
    }

    @ActivityInterface
    public interface GreetingActivities {
        String composeGreeting(String greeting, String name);
    }

    // Define the workflow implementation which implements the getGreeting workflow method.
    public static class GreetingWorkflowImpl implements GreetingWorkflow {
        private final GreetingActivities activities =
                Workflow.newActivityStub(
                        GreetingActivities.class,
                        ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(10)).build());

        @Override
        public String getGreeting(String name) {
            // This is a blocking call that returns only after the activity has completed.
            return activities.composeGreeting("Hello", name);
        }
    }

    static class GreetingActivitiesImpl implements GreetingActivities {
        private final ActivityCompletionClient completionClient;

        GreetingActivitiesImpl(ActivityCompletionClient completionClient) {
            this.completionClient = completionClient;
        }

        @Override
        public String composeGreeting(String greeting, String name) {

            // Get the activity execution context
            ActivityExecutionContext context = Activity.getExecutionContext();

            // Set a correlation token that can be used to complete the activity asynchronously
            byte[] taskToken = context.getTaskToken();
            ForkJoinPool.commonPool().execute(() -> composeGreetingAsync(taskToken, greeting, name));
            context.doNotCompleteOnReturn();

            // Since we have set doNotCompleteOnReturn(), the workflow action method return value is
            // ignored.
            return "ignored";
        }

        // Method that will complete action execution using the defined ActivityCompletionClient
        private void composeGreetingAsync(byte[] taskToken, String greeting, String name) {
            String result = greeting + " " + name + "!";

            // Complete our workflow activity using ActivityCompletionClient
            completionClient.complete(taskToken, result);
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // Get a Workflow service stub.
        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
        WorkflowClient client = WorkflowClient.newInstance(service);
        WorkerFactory factory = WorkerFactory.newInstance(client);
        Worker worker = factory.newWorker(TASK_QUEUE);

        worker.registerWorkflowImplementationTypes(GreetingWorkflowImpl.class);
        ActivityCompletionClient completionClient = client.newActivityCompletionClient();
        worker.registerActivitiesImplementations(new GreetingActivitiesImpl(completionClient));


        factory.start();

        // Create the workflow client stub. It is used to start our workflow execution.
        GreetingWorkflow workflow =
                client.newWorkflowStub(
                        GreetingWorkflow.class,
                        WorkflowOptions.newBuilder()
                                .setWorkflowId(WORKFLOW_ID)
                                .setTaskQueue(TASK_QUEUE)
                                .build());

        /**
         * Here we use {@link io.temporal.client.WorkflowClient} to execute our workflow asynchronously.
         * It gives us back a {@link java.util.concurrent.CompletableFuture}. We can then call its get
         * method to block and wait until a result is available.
         */
        CompletableFuture<String> greeting = WorkflowClient.execute(workflow::getGreeting, "World");

        // Wait for workflow execution to complete and display its results.
        System.out.println(greeting.get());
        System.exit(0);
    }
}
