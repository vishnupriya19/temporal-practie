package io.workshop.practice.AsyncActivities;

import io.temporal.client.ActivityCompletionClient;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import io.workshop.practice.AsyncActivities.GreetingActivitiesImpl;

public class AsyncActivityWorker   {
    public static final String TASK_QUEUE = "HelloAsyncActivityCompletionTaskQueue";
    private static final WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
    private static final WorkflowClient client = WorkflowClient.newInstance(service);
    private static final WorkerFactory factory = WorkerFactory.newInstance(client);

    public static void main(String[] args) {
        try {
            Worker worker = factory.newWorker(TASK_QUEUE);

            worker.registerWorkflowImplementationTypes(GreetingWorkflowImpl.class);
            ActivityCompletionClient completionClient = client.newActivityCompletionClient();
            worker.registerActivitiesImplementations(new GreetingActivitiesImpl(completionClient));

            factory.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
