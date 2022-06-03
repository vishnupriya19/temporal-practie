package io.workshop.practice.BlockingWorkflow;

import io.temporal.workflow.Workflow;
import org.slf4j.Logger;
import java.util.Objects;

public class HelloWorldNewImpl implements HelloWorldNew {
    private final Logger workflowLogger = Workflow.getLogger(HelloWorldNewImpl.class);
    private String greeting;

    @Override
    public void sayHello(String name) {
        int count = 0;
        String oldGreeting = name;
        Workflow.await(() -> Objects.equals(greeting, oldGreeting));

        workflowLogger.info(++count + ": " + greeting + " " + name + "!");
    }

    @Override
    public void updateGreeting(String greeting) {
        this.greeting = greeting;
    }
}