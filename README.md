# WA2G17

## Instruction to build

To create the server image use the gradle command jibDockerBuild.

To create the project container use the command docker compose up in the root folder.

Port localhost:8080 is exposed for keycloak in order to be more correctable.

Port localhost:8081 is exposed for the server.

Port localhost:9090 is exposed for the Prometheus.

Port localhost:3000 is exposed for the Grafana.

All the other ports are in the internal network of docker.

