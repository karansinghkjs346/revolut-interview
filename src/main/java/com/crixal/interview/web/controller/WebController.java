package com.crixal.interview.web.controller;

import org.rapidoid.http.Req;
import org.rapidoid.http.ReqHandler;
import org.rapidoid.setup.On;

import java.util.function.Function;

public abstract class WebController {
    public abstract void startAsync();

    void onGetJson(String path, Function<Req, Object> handler) {
        onJson("GET", path, handler);
    }

    void onPutJson(String path, Function<Req, Object> handler) {
        onJson("PUT", path, handler);
    }

    private void onJson(String method, String path, Function<Req, Object> handler) {
        On.route(method, path).json((ReqHandler) handler::apply);
    }
}
