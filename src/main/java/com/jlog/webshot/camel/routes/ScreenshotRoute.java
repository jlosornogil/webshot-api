package com.jlog.webshot.camel.routes;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.exec.ExecBinding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Jose Luis Osorno <joseluis.osorno@ixxus.com>
 */
@Component
public class ScreenshotRoute extends RouteBuilder {

    public static final String ROUTE_URI = "direct:screenshotGeneration";

    @Value("${webshot.screenshot.output.directory}")
    private String screenshotOutputDirectory;
    @Value("${webshot.screenshot.resource.path}")
    private String screenshotResourcePath;

    @Override
    public void configure() throws Exception {
        from(ROUTE_URI)
                .log("Capturing the web")
                .choice()
                    .when(simple("${body.engine} == 'CHR'"))
                        .to(HeadlessChromeScreenshotRoute.ROUTE_URI)
                    .otherwise()
                        .to(HeadlessFirefoxScreenshotRoute.ROUTE_URI)
                .end()
                .log("Moving the screenshot to the static resources directory")
                .process(ex -> {
                    UUID screenshotId = UUID.randomUUID();
                    ex.getIn().setHeader("screenshotId", screenshotId);
                    ex.getIn().setHeader(ExecBinding.EXEC_COMMAND_ARGS, Stream.of("screenshot.png", screenshotOutputDirectory + screenshotId + ".png").collect(
                            Collectors.toList()));
                })
                .to("exec:mv")
                .setBody(simple(screenshotResourcePath + "${header.screenshotId}.png"))
                .log("${body}");
    }
}
