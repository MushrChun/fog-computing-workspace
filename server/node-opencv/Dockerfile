FROM node:8-stretch

RUN apt-get -y update
RUN apt-get install -y --fix-missing \
    libopencv-dev \
    cmake

COPY . /root/app
RUN cd /root/app && \
    npm install

EXPOSE 5000

CMD node /root/app/app.js