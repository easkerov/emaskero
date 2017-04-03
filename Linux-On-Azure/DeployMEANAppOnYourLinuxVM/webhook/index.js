var express = require('express'),
http = require('http'),
app = express();

app.set('port', process.env.PORT || 3030);

app.post('/push/', function (req, res) {
   var spawn = require('child_process').spawn,
       deploy = spawn('sh', [ './pull.sh' ]);

   deploy.stdout.on('data', function (data) {
       console.log(''+data);
   });

   deploy.on('close', function (code) {
       console.log('Child process exit code: ' + code);
   });
   res.json(200, {message: 'Received'})
});

http.createServer(app).listen(app.get('port'), function(){
console.log('Listening on: ' + app.get('port'));
});
