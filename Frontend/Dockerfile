FROM node:21 AS build
WORKDIR /app
COPY package.json package.json
RUN npm install
COPY . .
RUN npm run dev
