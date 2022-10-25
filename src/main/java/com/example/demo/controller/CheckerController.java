package com.example.demo.controller;

import com.example.demo.service.CheckerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api")
public class CheckerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckerController.class);

    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    private final CheckerService checkerService;

    @Autowired
    public CheckerController(final CheckerService checkerService) {
        this.checkerService = checkerService;
    }

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public void start() {
        COUNTER.incrementAndGet();

        //new Thread(() -> {
        final long start = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("{} TARGET | Checker started at {}",
                COUNTER.get(), Instant.now());
        }

        checkerService.run(COUNTER.get());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("{} TARGET | Checker done at {} for {} seconds",
                COUNTER.get(), Instant.now(), (System.currentTimeMillis() - start) / 1000L);
        }
        //}).start();
    }
}
