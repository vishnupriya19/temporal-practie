package io.workshop.practice.Versioning;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.workshop.practice.Versioning.model.Account;
import io.workshop.practice.Versioning.model.Customer;

import java.time.Duration;

public class VersioningStarter2 {
    public static final WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
    public static final WorkflowClient client = WorkflowClient.newInstance(service);

    public static void main(String[] args) {
        Customer customer = new Customer("c4", "John", "john@john.com", "new",
                Duration.ofMinutes(1));
        WorkflowOptions newCustomerWorkflowOptions =
                WorkflowOptions.newBuilder()
                        .setWorkflowId(customer.getAccountNum())
                        .setTaskQueue(VersioningWorker.TASK_QUEUE)
                        .build();
        CustomerWorkflow newCustomerWorkflow = client.newWorkflowStub(CustomerWorkflow.class, newCustomerWorkflowOptions);

        Account account = newCustomerWorkflow.execute(customer);

        System.out.println("Account amount: " + account.getAmount());
    }
}
