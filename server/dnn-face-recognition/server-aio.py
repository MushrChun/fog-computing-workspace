import socketio
import os
import base64
import time
import io
from datetime import datetime
import face_recognition

from aiohttp import web


sio = socketio.AsyncServer()
app = web.Application()
sio.attach(app)


@sio.on('connect')
def connect(sid, environ):
    print('connect to a fog client at: ', datetime.now(), sid)

@sio.on('detection request')
async def task(sid, data):
    print('beginning of the request from a fog client at: ', datetime.now(), sid)
    target_image = base64.b64decode(data['imageData'])
    
    detected_faces = detect_face(target_image)
    # detected_faces = dummy_face()
    # print(detected_faces)
    await sio.emit('detection response', detected_faces)

@sio.on('disconnect')
def disconnect(sid):
    print('disconnect to a fog client at: ', datetime.now(), sid)


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
    print('finish load the image at: ', datetime.now())
    print('=>beginning of the face detection at: ', datetime.now())
    face_locations = face_recognition.face_locations(image)
    print('<=end of the face detection at: ', datetime.now())
    frames = []
    for face_location in face_locations:
        top, right, bottom, left = face_location
        newFrame = {}
        newFrame['x'] = left
        newFrame['y'] = bottom
        newFrame['w'] = (right-left)
        newFrame['h'] = (top-bottom)
        newFrame['label'] = 'face'
        frames.append(newFrame)
    return frames


if __name__ == '__main__':
    web.run_app(app)