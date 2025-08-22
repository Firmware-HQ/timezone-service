REMOTE_HOST = mars3142@172.16.20.1
PROJECT_NAME = firmware-hq
SERVICE_NAME = timezone-service
RASPI_PATH = /mnt/data/${PROJECT_NAME}

install: build deploy import cleanup

build:
	docker buildx build --platform linux/arm64 --no-cache -t ${PROJECT_NAME}/${SERVICE_NAME}:latest -o type=docker,dest=./${SERVICE_NAME}-arm64.tar .

deploy:
	scp ./${SERVICE_NAME}-arm64.tar ${REMOTE_HOST}:${RASPI_PATH}/

import:
	ssh ${REMOTE_HOST} "sudo docker load -i ${RASPI_PATH}/${SERVICE_NAME}-arm64.tar && rm ${RASPI_PATH}/${SERVICE_NAME}-arm64.tar"

cleanup:
	rm -f ${SERVICE_NAME}-arm64.tar

.PHONY: build deploy import cleanup
