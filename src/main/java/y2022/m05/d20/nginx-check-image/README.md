

```shell
docker build -t adolphor/nginx-check .

TEMP_NINGX_PATH=/Users/adolphor/IdeaProjects/bob/adolphor.github.io/src/main/java/y2022/m05/d20/check-conf
docker stop nginx-check && docker rm nginx-check
docker run --name nginx-check -d \
    -p 8988:8988 \
    -v ${TEMP_NINGX_PATH}/nginx.conf:/usr/local/nginx/conf/nginx.conf \
    -v ${TEMP_NINGX_PATH}/conf.d:/usr/local/nginx/conf/conf.d \
    adolphor/nginx-check

docker exec -it nginx-check /bin/bash
```

