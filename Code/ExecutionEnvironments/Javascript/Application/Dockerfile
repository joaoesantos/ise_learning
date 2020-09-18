FROM node:14
WORKDIR /app
COPY package.json /app
RUN npm install
COPY startup.sh .
RUN chmod +x startup.sh 
COPY . /app
ENTRYPOINT [ "./startup.sh" ]