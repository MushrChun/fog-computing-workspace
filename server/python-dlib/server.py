import socketio
import eventlet
import os
import base64
import time
import io
import face_recognition
from flask import Flask, render_template

sio = socketio.Server()
app = Flask(__name__)

@app.route('/')
def index():
    """Serve the client-side application."""
    return render_template('index.html')

@sio.on('connect')
def connect(sid, environ):
    print('connect ', sid)

@sio.on('detection request')
def task(sid, data):

    target_image = base64.b64decode(data['imageData'])
    print('detection request ')
    detected_faces = detect_face(target_image)
    # detected_faces = dummy_face()
    print(detected_faces)
    
    sio.emit('detection response', detected_faces)

@sio.on('disconnect')
def disconnect(sid):
    print('disconnect ', sid)


def find_images():
    images = os.listdir('../instructors')
    image_paths = ['../instructors/'+ x for x in images]
    print(images)
    return image_paths

#store the trained face models
encode_faces = []

def encode_face():
    try:
        image_paths = find_images()
        for path in image_paths:
            image = face_recognition.load_image_file(path)
            face_encoding = face_recognition.face_encodings(image)[0]
            encode_faces.append(face_encoding)
    except IndexError:
        print("I wasn't able to locate any faces in at least one of the images. Check the image files. Aborting...")
        quit()
    return encode_faces

def recognise_face(unknown_face_encoding):
    #results = face_recognition.compare_faces(encode_faces, unknown_face_encoding)
    tolerance=0.4
    distances = face_recognition.face_distance(encode_faces, unknown_face_encoding)
    result = list(distances <= tolerance)

    return result

def dummy_face():
    frames = []
    newFrame = {}
    newFrame['x'] = 300
    newFrame['y'] = 300
    newFrame['w'] = 300
    newFrame['h'] = 300
    newFrame['label'] = 'face'
    frames.append(newFrame)
    return frames

def detect_face(imageStream):
    # image = Image.open(io.BytesIO(image_data))
    # with open(str(time.time())+'.jpg','wb+') as f:
    #  f.write(imageStream)
    image = face_recognition.load_image_file(io.BytesIO(imageStream))

    face_locations = face_recognition.face_locations(image)
    frames = []
    for face_location in face_locations:
        top, right, bottom, left = face_location
        # print("A face is located at pixel location Top: {}, Left: {}, Bottom: {}, Right: {}".format(top, left, bottom, right))

        newFrame = {}
        newFrame['x'] = left
        newFrame['y'] = top
        newFrame['w'] = right-left
        newFrame['h'] = top-bottom
        newFrame['label'] = 'face'
        frames.append(newFrame)
    print('finish detection!')
    return frames


if __name__ == '__main__':

    # wrap Flask application with socketio's middleware
    app = socketio.Middleware(sio, app)

    # deploy as an eventlet WSGI server
    eventlet.wsgi.server(eventlet.listen(('', 8000)), app)