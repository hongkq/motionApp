var express = require('express'),
https = require('https'),
bodyParser = require('body-parser'),
mongoose = require('mongoose'),
path = require('path');

var UserSchema = new mongoose.Schema({
  utaid : String,
  email : String,
  first : String,
  last : String,
  mobile : String,
  address : String,
  gender : String,
  pwd : String,
  designation : String
}),
SportSchema = new mongoose.Schema({
  sportname : String,
  maxteamsize : String,
  minteamsize : String,
  venues : [String]
}),
MatchSchema = new mongoose.Schema({
  sportname : String,
  datetime : Date,
  venue : String,
  team1 : String,
  team2 : String,
  score : String,
  winner : String,
  referee : String
}),
TeamSchema = new mongoose.Schema({
  sportname : String,
  teamname : String,
  member1 : String,
  member2 : String,
  member3 : String,
  member4 : String,
  member5 : String,
  isapproved : Boolean
});

var UserModel = mongoose.model('users', UserSchema),
SportModel = mongoose.model('sport', SportSchema),
MatchModel = mongoose.model('match', MatchSchema),
TeamModel = mongoose.model('team', TeamSchema);

var app = express()

// body parser middleware
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// Connect mongodb
mongoose.Promise = global.Promise;
mongoose.connect("mongodb://neaGaze:neaGaze@ds159517.mlab.com:59517/macalerts", function (error) {
  if (error) console.error(error);
  else console.log('mongo connected');
});


app.get('/', function (req, res) {
  UserModel.find(function(err, data){
    if(err) console.error(err);
    var str = "";
    if(data != null && data.length > 0) {
      for(var i = 0; i < data.length; i++) {
        str += JSON.stringify(data);
      }
    }

    res.send('' + str);
  });
});


/**
* Login User
**/
app.post('/authenticate', function(req, res){
  var body = req.body;
  var reply = {auth : false, utaid : 000000, designation : "user"};
  UserModel.find({email : body.email, pwd : body.pwd}, function(err, data){
    if(err) console.error(err);
    console.log("request data: " + body.email + "," + body.pwd);
    if(data != null && data.length > 0) {
      for(var i = 0; i < data.length; i++) {
        reply.auth = true;
        reply.utaid = data[i].utaid;
        reply.designation = data[i].designation;
        console.log("return data: " + data[i].utaid);
      }
    }

    res.send(reply);
  });
});

/**
* Create Sporting Event
**/
app.post('/createSport', function(req, res){
  var body = req.body;
  var reply = {status : true};
  var sport = new SportModel();
  sport.sportname = body.sportname;
  sport.maxteamsize = body.maxteamsize;
  sport.minteamsize = body.minteamsize;

  sport.save(function(err, data){
    if(err) console.error(err);
    console.log("request data: " + body.sportname + "," + body.maxteamsize);
    if(data != null && data.length > 0) {
      for(var i = 0; i < data.length; i++) {
        console.log("return data: " + data[i]);
      }
    }
    res.send(reply);
  });
});


/**
* Remove a Sporting Event
**/
app.post('/removeSport', function(req, res){
  var body = req.body;
  var reply = {status : false};
  SportModel.remove({sportname : body.sportname}, function(err, data){
    if(err) console.error(err);
    console.log("remove data: " + body.sportname);
    reply.status = true;
    // TODO also delete all documents from matches
    res.send(reply);
  });
});

/**
* Get the list of Sporting Events
**/
app.get('/getSport', function (req, res) {
  SportModel.find(function(err, data){
    if(err) console.error(err);
    var str = "";
    if(data != null && data.length > 0) {
      for(var i = 0; i < data.length; i++) {
        str += JSON.stringify(data);
      }
    }

    res.send(data);
  });
});

/**
* Add Venues to a Sporting Event
**/
app.post('/addVenue', function(req, res){
  var body = req.body;
  var reply = {status : false};
  SportModel.update({'sportname' : body.sportname}, {'$push' : {'venues' : body.venue}}, function(err, data){
    if(err) console.error(err);
    console.log("update data: " + body.sportname);
    reply.status = true;
    // TODO also delete all documents from matches
    res.send(reply);
  });
});

/**
* Register an User
**/
app.post('/register', function(req, res){
  var body = req.body;
  var reply = {status : true};
  var user = new UserModel();
  user.utaid = body.utaid;
  user.email = body.email;
  user.first = body.first;
  user.last = body.last;
  user.mobile = body.mobile;
  user.address = body.address;
  user.gender = body.gender;
  user.pwd = body.pwd;
  user.designation = body.designation;

  user.save(function(err, data){
    if(err) console.error(err);
    console.log("request data: " + body.email + "," + body.pwd);
    if(data != null && data.length > 0) {
      for(var i = 0; i < data.length; i++) {
        console.log("return data: " + data[i]);
      }
    }

    res.send(reply);
  });
});

/**
* Get the list of matches
**/
app.get('/getSchedule', function (req, res) {
  var datetime = new Date(req.query('datetime'));
  MatchModel.find({'datetime' : datetime}, function(err, data){
    if(err) console.error(err);
    var str = "";
    if(data != null && data.length > 0) {
      for(var i = 0; i < data.length; i++) {
        str += JSON.stringify(data);
      }
    }

    res.send(data);
  });
});


/**
* Create Match
**/
app.post('/createMatch', function(req, res){
  var body = req.body;
  var reply = {status : true};
  var match = new MatchModel();
  match.sportname = body.sportname;
  match.datetime = new Date(body.datetime);
  match.venue = body.venue;
  match.team1 = body.team1;
  match.team2 = body.team2;
  match.score = "";
  match.winner = "";
  match.referee = "";

  match.save(function(err, data){
    if(err) console.error(err);
    console.log("request data: " + body.sportname);
    if(data != null && data.length > 0) {
      for(var i = 0; i < data.length; i++) {
        console.log("return data: " + data[i]);
      }
    }
    res.send(reply);
  });
});


/**
* Get the list of matches
**/
app.get('/getTeam', function (req, res) {

  TeamModel.find(function(err, data){
    if(err) console.error(err);
    var str = "";
    if(data != null && data.length > 0) {
      for(var i = 0; i < data.length; i++) {
        str += JSON.stringify(data);
      }
    }

    res.send(data);
  });
});


app.listen(1336, function () {
  console.log('Example app listening on port 1336!')
})
