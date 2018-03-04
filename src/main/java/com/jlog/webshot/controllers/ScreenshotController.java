package com.jlog.webshot.controllers;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jlog.webshot.camel.routes.ScreenshotRoute;
import com.jlog.webshot.model.Screenshot;

/**
 * @author Jose Luis Osorno <joseluis.osorno@ixxus.com>
 */
@RestController
@RequestMapping("/webshot-api/screenshot")
public class ScreenshotController {

    @Autowired
    private ProducerTemplate producerTemplate;

    @RequestMapping(method = RequestMethod.POST)
    public String create(@RequestBody Screenshot screenshot) {
        return producerTemplate.requestBody(ScreenshotRoute.ROUTE_URI, screenshot, String.class);
    }
}