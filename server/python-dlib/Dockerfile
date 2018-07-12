FROM python:3.7-stretch

RUN apt-get -y update
RUN apt-get install -y --fix-missing \
    cmake

COPY . /root/python-dlib

RUN cd /root/python-dlib && \
    pip3 install -r requirements.txt
    # pip3 face_recognition \
    # socketio \
    # eventlet

EXPOSE 8000

CMD cd /root/python-dlib && \
    python3 /root/python-dlib/server.py
