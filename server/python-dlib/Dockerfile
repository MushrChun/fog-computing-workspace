FROM python:3.6-stretch

RUN apt-get -y update
RUN apt-get install -y --fix-missing \
    cmake

RUN cd ~ && \
    mkdir -p dlib && \
    git clone -b 'v19.9' --single-branch https://github.com/davisking/dlib.git dlib/ && \
    cd  dlib/ && \
    python3 setup.py install --yes USE_AVX_INSTRUCTIONS

COPY . /root/python-dlib

RUN cd /root/python-dlib && \
    pip3 install -r requirements.txt

EXPOSE 8000

CMD cd /root/python-dlib && \
    python3 /root/python-dlib/server.py
