package ru.otus.http.jserver;

import ru.otus.http.jserver.processors.CalculatorProcessor;
import ru.otus.http.jserver.processors.Default404Processor;
import ru.otus.http.jserver.processors.RequestProcessor;
import ru.otus.http.jserver.processors.WelcomeProcessor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    private Map<String, RequestProcessor> router;
    private Default404Processor default404Processor;

    public Dispatcher() {
        this.router = new HashMap<>();
        this.router.put("/calc", new CalculatorProcessor());
        this.router.put("/welcome", new WelcomeProcessor());
        this.default404Processor = new Default404Processor();
    }

    public void execute(HttpRequest request, OutputStream output) throws IOException {
        if (!router.containsKey(request.getUri())) {
            default404Processor.execute(request, output);
            return;
        }
        router.get(request.getUri()).execute(request, output);
    }
}
