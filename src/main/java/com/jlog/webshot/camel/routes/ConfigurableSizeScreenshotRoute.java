package com.jlog.webshot.camel.routes;

import java.util.Objects;

import org.apache.camel.builder.RouteBuilder;

/**
 * <p>
 * Represent a screenshot route which can configure the width and height of the capture.
 * </p>
 *
 * @author Jose Luis Osorno <joseluis.osorno@ixxus.com>
 */
public abstract class ConfigurableSizeScreenshotRoute extends RouteBuilder {

    protected static final String WINDOW_SIZE_ARG = "--window-size=";

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
    protected String buildWindowSizeArg(final Integer width, final Integer height) {
        StringBuilder windowSizeArg = new StringBuilder(WINDOW_SIZE_ARG);
        windowSizeArg.append(width);
        if(Objects.nonNull(height)) {
            windowSizeArg.append(",");
            windowSizeArg.append(height);
        }
        return windowSizeArg.toString();
    }
}