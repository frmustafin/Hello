package docker

import fixture.docker.DockerCompose
import io.ktor.http.*

// для отладки тестов, предполагается, что докер-компоуз запущен вручную
object DebugDockerCompose : DockerCompose {
    override fun start() {
    }

    override fun stop() {
    }

    override val inputUrl: URLBuilder
        get() = URLBuilder(
            protocol = URLProtocol.HTTP,
            host = "localhost",
            port = 8080,
        )
}