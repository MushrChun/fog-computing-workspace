FROM node:8-stretch

RUN apt-get -y update
RUN apt-get install -y --fix-missing \
    libopencv-dev \
    cmake

COPY . /root/node-opencv
RUN cd /root/node-opencv && \
    npm install

EXPOSE 5000

CMD node /root/node-opencv/app.js