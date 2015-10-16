statusListener(OnConsoleStatusListener)

String logPattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"

appender("CONSOLE", ConsoleAppender) {
    append = true
    encoder(PatternLayoutEncoder) {
        pattern = logPattern
    }
}

appender("FILE", RollingFileAppender) {
    append = true
    file = "logs/liner.log"
    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = "logs/liner.%d{yyyy-MM-dd}.log.gz"
        maxHistory = 5
    }
    encoder(PatternLayoutEncoder) {
        pattern = logPattern
    }
}

root(INFO, ["CONSOLE", "FILE"])
logger("com.stefanski.liner", DEBUG)

scan("30 seconds")

