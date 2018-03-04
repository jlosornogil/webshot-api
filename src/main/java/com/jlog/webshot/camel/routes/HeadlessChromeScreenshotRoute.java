package com.jlog.webshot.camel.routes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.camel.component.exec.ExecBinding;
import org.springframework.stereotype.Component;

import com.jlog.webshot.model.Screenshot;

/**
 * <p>
 * Route to create a screenshot of a web page making use of the headless mode of Chrome.
 * </p>
 * <p>
 * The Chrome executable version must be 59+ and its path must be configured in the property <code>webshot.headless-browser.chrome.path</code>.
 * </p>
 *
 * @author Jose Luis Osorno <joseluis.osorno@ixxus.com>
 */
@Component
public class HeadlessChromeScreenshotRoute extends ConfigurableSizeScreenshotRoute {

    static final String ROUTE_URI = "direct:chromeScreenshotGeneration";
    private static final String HEADLESS_ARG = "--headless";
    private static final String NO_SANDBOX_ARG = "--no-sandbox";
    private static final String DISABLE_GPU_ARG = "--disable-gpu";
    private static final String SCREENSHOT_ARG = "--screenshot";
    private static final String CHROME_EXEC_PATH = "{{webshot.headless-browser.chrome.path}}";

    @Override
    public void configure() throws Exception {
        from(ROUTE_URI)
                .log("Creating web screenshot with headless Chrome")
                .process(ex -> {
                    Screenshot screenshot = ex.getIn().getBody(Screenshot.class);
                    ex.getIn().setHeader(ExecBinding.EXEC_COMMAND_ARGS, buildCommandArgs(screenshot));
                })
                .to("exec:" + CHROME_EXEC_PATH);
    }

    /**
     * <p>
     * Build the headless chrome screenshot command arguments.
     * </p>
     *
     * @param screenshot The {@link Screenshot} with the parameters of the requested screenshot.
     * @return The {@link List} of command arguments regarding the requested screenshot.
     */
    private List<String> buildCommandArgs(final Screenshot screenshot) {
        Objects.requireNonNull(screenshot, "The screenshot parameter is mandatory");
        Objects.requireNonNull(screenshot.getUrl(), "The target URL is mandatory");
        List<String> commandArgs = new ArrayList<>();
        commandArgs.add(HEADLESS_ARG);
        commandArgs.add(NO_SANDBOX_ARG);
        commandArgs.add(DISABLE_GPU_ARG);
        commandArgs.add(SCREENSHOT_ARG);
        Optional.ofNullable(screenshot.getWidth()).ifPresent(width -> commandArgs.add(buildWindowSizeArg(width, screenshot.getHeight())));
        commandArgs.add(screenshot.getUrl());
        return commandArgs;
    }
}