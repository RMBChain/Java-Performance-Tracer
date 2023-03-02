# export V_PATH=/Users/spooner/.verdaccio
# docker run -d --restart always --name verdaccio -p 4873:4873 -v $V_PATH/conf:/verdaccio/conf -v $V_PATH/storage:/verdaccio/storage -v $V_PATH/plugins:/verdaccio/plugins verdaccio/verdaccio:5.21
# docker build --network=host -t jpt-ui-frontend:v0.2 . --no-cache
# docker ps -a|grep 'Exited'|cut -d' ' -f1|xargs -I {} docker rm {} # remove all unused container.
# docker rm -f jpt-ui-frontend
# docker run -d --restart always --network=host --name jpt-ui-frontend -p 3000:3000 jpt-ui-frontend:v0.2
# yarn install --registry http://localhost:4873

FROM node:14.21 AS build_base
WORKDIR /node_source
COPY . .
RUN yarn add react-scripts@5.0.1 --registry https://registry.npm.taobao.org
RUN yarn install --registry https://registry.npm.taobao.org

EXPOSE 3000
ENTRYPOINT ["yarn", "start"]
