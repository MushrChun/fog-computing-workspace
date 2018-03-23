const app = require('http').createServer(handler);
const io = require('socket.io')(app);
const fs = require('fs');

app.listen(5000);

function handler (req, res) {
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
  console.log('connection built');

  socket.on('detection request', function (data) {
    console.log('detection request');
    console.log(data);
    socket.emit('detection response', 'image plane');
  });
});