FROM node:19-alpine
WORKDIR /app
COPY package*.json .
RUN npm i --silent
COPY . .
EXPOSE 5173
CMD ["npm", "run", "dev"]