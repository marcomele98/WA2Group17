package it.polito.wa2.g17.server.monitoring

import io.micrometer.observation.Observation
import io.micrometer.observation.ObservationHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Component
internal class MyHandler : ObservationHandler<Observation.Context> {
    override fun onStart(context: Observation.Context) {
        log.info(
            "Before running the observation for context [{}]",
            context.name,
        )
    }

    override fun onStop(context: Observation.Context) {
        log.info(
            "After running the observation for context [{}]",
            context.name,
        )
    }

    override fun supportsContext(context: Observation.Context): Boolean {
        return true
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(MyHandler::class.java)
    }
}