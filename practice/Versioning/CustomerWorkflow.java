package io.workshop.practice.Versioning;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import io.workshop.practice.Versioning.model.Customer;
import io.workshop.practice.Versioning.model.Account;

@WorkflowInterface
public interface CustomerWorkflow {
    @WorkflowMethod
    Account execute(Customer customer);
}