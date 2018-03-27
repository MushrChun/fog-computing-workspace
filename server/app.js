const app = require('http').createServer(handler);
const io = require('socket.io')(app);
const fs = require('fs');

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
    fs.writeFile('testfile', imageBuf, (err)=> {
      if(err) console.log(err);
      console.log('image saved');
    });
    const frame = [
      {
        x: 400,
        y: 400,
        w: 200,
        h: 200,
        label: 'object1'
      },
      {
        x: 670,
        y: 1200,
        w: 123,
        h: 645,
        label: 'object2'}
    ];
    socket.emit('detection response', frame);
  });

  socket.on('disconnect', (reason) => {
    console.log('disconnect');
  });
});