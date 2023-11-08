//package com.imooc.pan.server.common.listener;
//
//import lombok.extern.log4j.Log4j2;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.stereotype.Component;
//import org.springframework.boot.ansi.AnsiColor;
//import org.springframework.boot.ansi.AnsiOutput;
//import org.springframework.context.ConfigurableApplicationContext;
//
///**
// * listener for log printing
// */
//@Component
//@Log4j2
//public class StartedListener implements ApplicationListener<ApplicationReadyEvent> {
//    /**
//     * print project starting information after project being started
//     */
//    @Override
//    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
//        String serverPort = applicationReadyEvent.getApplicationContext().getEnvironment().getProperty("server.port");
//        String serverUrl = String.format("http://%s:%s", "127.0.0.1", serverPort);
//        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_BLUE, "Drive Harbor server started at: ", serverUrl));
//        if (checkShowServerDoc(applicationReadyEvent.getApplicationContext())) {
//            log.info(AnsiOutput.toString(AnsiColor.BRIGHT_BLUE, "Drive Harbor server's doc started at:", serverUrl + "/doc.html"));
//        }
//        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_YELLOW, "Driver Harbor server has started successfully!"));
//    }
//
//    /**
//     * verify if the interface doc has been started
//     *
//     * @param applicationContext
//     * @return
//     */
//    private boolean checkShowServerDoc(ConfigurableApplicationContext applicationContext) {
//        return applicationContext.getEnvironment().getProperty("swagger2.show", Boolean.class, true) && applicationContext.containsBean("swagger2Config");
//    }
//
//
//
//}
