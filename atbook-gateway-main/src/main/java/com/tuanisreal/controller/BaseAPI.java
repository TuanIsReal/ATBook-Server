package com.tuanisreal.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import com.tuanisreal.constant.ActionName;
import com.tuanisreal.constant.ResponseCode;
import com.tuanisreal.controller.request.Request;
import com.tuanisreal.controller.response.ContextResponse;
import com.tuanisreal.controller.response.Response;
import com.tuanisreal.rbmq.publisher.APIGatewayPublisher;
import com.tuanisreal.rbmq.publisher.api.AuthenticationPublisher;
import com.tuanisreal.rbmq.publisher.api.UserPublisher;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public abstract class BaseAPI {

    @Autowired
    protected ApplicationContext applicationContext;
    @Autowired
    protected UserPublisher userPublisher;
    @Autowired
    protected AuthenticationPublisher authenticationPublisher;

    private static final int MAX_STEP_NUMBER = 30;

    protected String requestId;
    private volatile Map<Integer, Boolean> sentSteps = new ConcurrentHashMap<>();
    private volatile Map<Integer, Set<Integer>> conditionSteps = new ConcurrentHashMap<>();
    private volatile Map<Integer, Set<Integer>> reverseConditionSteps = new ConcurrentHashMap<>();
    private Set<Integer> startSteps = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private Set<Integer> allSteps = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private volatile Set<Integer> doneSteps = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private boolean isSuccess = true;
    private boolean isCompleted;
    protected Response response;

    public BaseAPI() {
        init();
        for (int i = 1; i <= MAX_STEP_NUMBER; i++) {
            sentSteps.put(i, false);
        }
    }

    protected abstract void init();

    protected final void addStartStep(int step) {
        startSteps.add(step);
        allSteps.add(step);
    }

    protected final void addCondition(int step, int requiredStep) {
        if (!conditionSteps.containsKey(step)) {
            conditionSteps.put(step, Collections.newSetFromMap(new ConcurrentHashMap<>()));
        }
        conditionSteps.get(step).add(requiredStep);

        if (!reverseConditionSteps.containsKey(requiredStep)) {
            reverseConditionSteps.put(requiredStep, Collections.newSetFromMap(new ConcurrentHashMap<>()));
        }
        reverseConditionSteps.get(requiredStep).add(step);
        allSteps.add(step);
        allSteps.add(requiredStep);
    }

    public void handleRequest(String payload) {
        parseRequest(payload);
        doStartSteps();
    }


    protected void doStartSteps() {
        validRequest();
        List<Integer> steps = getStartSteps();
        steps.forEach((step) -> {
            doStep(step);
        });
    }

    protected abstract Request parseRequest(String payload);

    protected void validRequest() {
    }


    public void handleStepResponse(ContextResponse contextResponse) {
        if (contextResponse.getCode() != ResponseCode.SUCCESS) {
            handleStepError(contextResponse);
            log.debug("HANDLE RESPONSE ERROR: {}", contextResponse);
            return;
        }
        saveResponseData(contextResponse);
        addDoneStep(contextResponse.getStep());
    }

    public void handleStepError(ContextResponse contextResponse) {
        response = new Response(contextResponse.getCode());
        response.setData(null);
        response.setRequestId(requestId);
        isSuccess = false;
        isCompleted = true;
    }

    private Object parseErrorResponseData(ContextResponse contextResponse) {
        Object result = null;

        return result;
    }

    protected abstract void saveResponseData(ContextResponse response);

    protected abstract void doStep(int step);

    private boolean checkCompleted() {
        return doneSteps.size() == allSteps.size() || isCompleted;
    }

    public Set<Integer> getDoneSteps() {
        return doneSteps;
    }

    public Set<Integer> getAllSteps() {
        return allSteps;
    }

    protected abstract Response createResponseData();

    public Response getResponseAPI()  throws InterruptedException{
        while (!isCompleted){
            Thread.sleep(10);
        }

        if (!isSuccess){
            return response;
        }
        response = createResponseData();
        response.setRequestId(requestId);
        return response;
    }

    protected final void callNonWaitingInternalAPI(String apiName, String payload, int step) {
//        BaseAPI baseController = (BaseAPI) applicationContext.getBean(apiName);
//        if (baseController == null) {
//            return;
//        }
//
//        String internalAPIRequestId = HttpCache.generateRequestId();
//        baseController.setRequestId(internalAPIRequestId);
//        ControllerCache.add(internalAPIRequestId, baseController);
//        addSendingStep(step);
//        addDoneStep(step);
//        try {
//            baseController.handleRequest(payload);
//        } catch (Exception ex) {
//            log.error("", ex);
//        }
    }

    protected final void callWaitingInternalAPI(String apiName, String payload, int step) {
//        InternalAPI baseController = (InternalAPI) applicationContext.getBean(apiName);
//        if (baseController == null) {
//            return;
//        }
//        String internalAPIRequestId = HttpRepository.generateRequestId();
//        baseController.setRequestId(internalAPIRequestId);
//        baseController.setParentAPIRequestId(requestId);
//        baseController.setParentAPICurrentStep(step);
//        ControllerRepository.add(internalAPIRequestId, baseController);
//        addSendingStep(step);
//        try {
//            baseController.handleRequest(payload);
//        } catch (Exception ex) {
//            log.error("", ex);
//        }
    }

    protected final void sendNoResponseRequest(APIGatewayPublisher publisher, int step, ActionName actionName, Object requestData) {
        publisher.sendNonWaitingContextRequest(requestId, step, actionName, requestData);
        addSendingStep(step);
        addDoneStep(step);
    }

    protected final void resetCacheRequest(APIGatewayPublisher publisher, int step, ActionName actionName, Object requestData) {
        publisher.sendContextRequest(requestId, step, actionName, requestData);
        addSendingStep(step);
        addDoneStep(step);
    }

    protected final void sendRealTimeRequest(APIGatewayPublisher publisher, int step, ActionName actionName, Object requestData) {
        log.info("sendContextRequest");
        publisher.sendContextRequest(requestId, step, actionName, requestData);
        addSendingStep(step);
    }

    protected void addDoneStep(int step) {
        doneSteps.add(step);
        if (reverseConditionSteps.containsKey(step)) {
            Set<Integer> steps = reverseConditionSteps.get(step);
            steps.stream().filter((stepRequired) -> (conditionSteps.containsKey(stepRequired)))
                    .forEachOrdered((stepRequired) -> {
                        conditionSteps.get(stepRequired).remove(step);
                    });
        }
        isCompleted = checkCompleted();
        if (isCompleted) {
            return;
        }
        List<Integer> nextSteps = getNextSteps();
        nextSteps.forEach((nextStep) -> {
            doStep(nextStep);
        });
    }

    protected final void rejectSteps(int baseStep) {
        sentSteps.put(baseStep, true);
        doneSteps.add(baseStep);
        if (reverseConditionSteps.containsKey(baseStep)) {
            Set<Integer> steps = reverseConditionSteps.get(baseStep);
            steps.forEach((key) -> {
                rejectSteps(key);
            });
        }
        isCompleted = checkCompleted();
        if (isCompleted) {
            return;
        }
    }

    protected List<Integer> getStartSteps() {
        return startSteps.stream().filter((key) -> (sentSteps.get(key) == false)).collect(Collectors.toList());
    }

    private synchronized List<Integer> getNextSteps() {
        List<Integer> steps = new ArrayList<>();
        conditionSteps.keySet().stream()
                .filter((key) -> (conditionSteps.get(key).isEmpty() && sentSteps.get(key) == false))
                .forEachOrdered(step -> {
                    sentSteps.put(step, true);
                    steps.add(step);
                });

        return steps;
    }

    private void addSendingStep(int step) {
        sentSteps.put(step, true);
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    protected String getRequestId() {
        return requestId;
    }

}
