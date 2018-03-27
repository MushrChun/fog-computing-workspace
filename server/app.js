const app = require('http').createServer(handler);
const io = require('socket.io')(app);
const fs = require('fs');
const cv = require('opencv');

app.listen(5000);

function handler(req, res) {
  fs.readFile(__dirname + '/index.html',
    function (err, data) {
      if (err) {
        res.writeHead(500);
        return res.end('Error loading index.html');
      }

      res.writeHead(200);
      res.end(data);
    });
}

io.on('connection', function (socket) {
  console.log('connect');

  socket.on('detection request', function (data) {
    console.log('detection request');
    const imageBuf = Buffer.from(data.imageData, 'base64');
    cv.readImage(imageBuf, (err, im) => {
      if (err) {
        throw err;
      }

      if (im.width() < 1 || im.height() < 1) throw new Error('Image has no size');

      im.detectObject('./haarcascade_frontalface_alt.xml', {}, function (err, faces) {
        if (err) throw err;
        const frames = [];
        faces.forEach(face => {
          const newFrame = {};
          newFrame.x = face.x;
          newFrame.y = face.y;
          newFrame.w = face.width;
          newFrame.h = face.height;
          newFrame.label = 'face';
          frames.push(newFrame);
        });
        console.log(faces);
        socket.emit('detection response', frames);
      });
    });
  });

  socket.on('disconnect', (reason) => {
    console.log('disconnect');
  });
});