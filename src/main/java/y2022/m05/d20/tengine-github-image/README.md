
```shell
docker build -t adolphor/tengine-github .

TEMP_NINGX_PATH=/Users/adolphor/IdeaProjects/bob/adolphor.github.io/src/main/java/y2022/m05/d20/check-conf
docker stop tengine-github && docker rm tengine-github
docker run --name tengine-github -d \
    -p 8988:8988 \
    -v ${TEMP_NINGX_PATH}/nginx.conf:/etc/nginx/nginx.conf \
    -v ${TEMP_NINGX_PATH}/conf.d:/etc/nginx/conf.d \
    adolphor/tengine-github
docker exec -it tengine-github /bin/sh
```




