package com.jlog.webshot.camel.routes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.exec.ExecBinding;
import org.springframework.stereotype.Component;

import com.jlog.webshot.model.Screenshot;

/**
 * <p>
 * Route to create a screenshot of a web page making use of the headless mode of Firefox.
 * </p>
 * <p>
 * The Firefox executable version must be 56+ and its path must be configured in the property <code>webshot.headless-browser.firefox.path</code>.
 * </p>
 *
 * @author Jose Luis Osorno <joseluis.osorno@ixxus.com>
 */
@Component
public class HeadlessFirefoxScreenshotRoute extends RouteBuilder {

    static final String ROUTE_URI = "direct:firefoxScreenshotGeneration";
    private static final String SCREENSHOT_ARG = "-screenshot";
    private static final String WINDOW_SIZE_ARG = "--window-size=";
    private static final String FIREFOX_EXEC_PATH = "{{webshot.headless-browser.firefox.path}}";

    @Override
    public void configure() throws Exception {
        from(ROUTE_URI)
                .log("Creating web screenshot with headless Firefox")
                .process(ex -> {
                    Screenshot screenshot = ex.getIn().getBody(Screenshot.class);
                    ex.getIn().setHeader(ExecBinding.EXEC_COMMAND_ARGS, buildCommandArgs(screenshot));
                })
                .to("exec:" + FIREFOX_EXEC_PATH);
    }

    /**
     * <p>
     * Build the headless firefox screenshot command arguments.
     * </p>
     *
     * @param screenshot The {@link Screenshot} with the parameters of the requested screenshot.
     * @return The {@link List} of command arguments regarding the requested screenshot.
     */
    private List<String> buildCommandArgs(final Screenshot screenshot) {
        Objects.requireNonNull(screenshot, "The screenshot parameter is mandatory");
        Objects.requireNonNull(screenshot.getUrl(), "The target URL is mandatory");
        List<String> commandArgs = new ArrayList<>();
        commandArgs.add(SCREENSHOT_ARG);
        commandArgs.add(screenshot.getUrl());
        Optional.ofNullable(screenshot.getWidth()).ifPresent(width -> commandArgs.add(buildWindowSizeArg(width, screenshot.getHeight())));
        return commandArgs;
    }

    /**
     * <p>
     * Build the window size argument for the screenshot command.
     * </p>
     * <p>
     * The window size argument follows the pattern: --window-size=width[,height].
     * </p>
     *
     * @param width The width of the viewport to capture.
     * @param height The height of the viewport to capture (Optional).
     * @return The window size argument.
     */
    private String buildWindowSizeArg(final Integer width, final Integer height) {
        StringBuilder windowSizeArg = new StringBuilder(WINDOW_SIZE_ARG);
        windowSizeArg.append(width);
        if(Objects.nonNull(height)) {
            windowSizeArg.append(",");
            windowSizeArg.append(height);
        }
        return windowSizeArg.toString();
    }
}