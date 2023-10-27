package docker

import fixture.docker.AbstractDockerCompose

object KtorDockerCompose : AbstractDockerCompose(
    "app-ktor_1", 8080, "ktor/docker-compose-ktor.yml"
)