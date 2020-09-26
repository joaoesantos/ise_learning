FROM node:14
WORKDIR /app
COPY package.json /app
RUN npm install
COPY . /app
RUN chmod +x startup.sh 
ENTRYPOINT [ "./startup.sh" ]