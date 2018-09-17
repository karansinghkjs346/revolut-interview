package com.crixal.interview.web;

import com.crixal.interview.config.ApplicationConfig;
import com.crixal.interview.dto.ExceptionDTO;
import com.crixal.interview.exception.BadInputDataException;
import com.crixal.interview.exception.NotEnoughMoneyException;
import com.crixal.interview.exception.RetryOperationException;
import com.crixal.interview.exception.UserNotFoundException;
import com.crixal.interview.web.controller.WebController;
import org.rapidoid.http.HttpWrapper;
import org.rapidoid.http.NotFound;
import org.rapidoid.setup.My;
import org.rapidoid.setup.On;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Set;

public class WebStarter {
    private final ApplicationConfig applicationConfig;
    private final Set<WebController> controllers;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    public WebStarter(ApplicationConfig applicationConfig, Set<WebController> controllers) {
        this.applicationConfig = applicationConfig;
        this.controllers = controllers;
    }

    public void start() {
        On.config().set("port", applicationConfig.getServerPort());
        On.defaults().wrappers((HttpWrapper) (req, invocation) -> {
            log.info("{} | params: {}", req, req.data());
            return invocation.invoke();
        });

        registerErrorHandlers();
        controllers.forEach(WebController::startAsync);
    }

    private void registerErrorHandlers() {
        My.error(BadInputDataException.class).handler((req, resp, error) -> resp.code(400).result(new ExceptionDTO(error.getMessage())));
        My.error(UserNotFoundException.class).handler((req, resp, error) -> resp.code(404).result(new ExceptionDTO(error.getMessage())));
        My.error(NotEnoughMoneyException.class).handler((req, resp, error) -> resp.code(400).result(new ExceptionDTO(error.getMessage())));
        My.error(RetryOperationException.class).handler((req, resp, error) -> resp.code(503).result(new ExceptionDTO(error.getMessage())));
        My.error(NotFound.class).handler((req, resp, error) -> resp.code(404).result(new ExceptionDTO("Bad url")));
    }
}
