var webSocketSupport = false;
var ws;
var name;
var id;
var room;
var member_list;
var cp;

$(function () {
  member_list = $('#list');
  room = $('#information');

  // hide element
  name = document.getElementById('p_name').value;
  id = document.getElementById('p_id').value;
  cp = $('#cp').val();

  $('#connect').click(function () {
    open();
  });
  $('#disconnect').click(function () {
    close();
  });
  $('#send').click(function () {
    sendMsg();
  });
  $('#msg').keydown(function (e) {
    if (e.which == 13) {
      sendMsg();
    }
  });
});

// Check if current browser support Web Socket
if ("WebSocket" in window) {
  webSocketSupport = true;
} else {
  alert("WebSocket not supported by your Browser!");
}

function open() {
  // Check if current browser support Web Socket
  if (!webSocketSupport) {
    alert("WebSocket not supported by your Browser!");
    return;
  }

  if (ws == null) {
    var ws_url = "ws://" + location.host + cp + "/../chat/physician/" + id + "/" + name;
    ws = new WebSocket(ws_url);

    ws.onopen = function () {
      room.empty();
      member_list.empty();
      var op = $('<div/>', {'class': "system-font", 'text': name});
      member_list.append(op);
      addSystemMsg(getTime(), "Connection's been set up.");
      $('#room-name').text(name + "'s chat room");
    };

    ws.onmessage = function (evt) {
      var json = JSON.parse(evt.data);
      if (json.type == "system") {
        addSystemMsg(json.time, json.message);
      } else if (json.type == "chat") {
        addChatMsg(json);
      } else if (json.type == "room_member_update") {
        updateRoomMember(json);
      }
    };

    ws.onclose = function () {
      addSystemMsg(getTime(), "Connection's been shut down. Your room is close.");
      member_list.empty();
      ws = null;
      $('#room-name').text("Not in chat room");
    };
  } else {
    alert("Your chat room has already been open.");
  }
}

function close() {
  // Check if current browser support Web Socket
  if (!webSocketSupport) {
    alert("WebSocket not supported by your Browser!");
    return;
  }

  if (ws != null) {
    ws.close();
    ws = null;
  } else {
    alert("Connection's not been set up yet.");
  }
}

function sendMsg() {
  // Check if current browser support Web Socket
  if (!webSocketSupport) {
    alert("WebSocket not supported by your Browser!");
    return;
  }

  // Check if web socket connection has been established
  if (typeof (ws) == "undefined" || ws == null) {
    alert("Before sending message, please set up web socket connection first by clicking 'Open Room' button.");
    return;
  }

  var msg = $('#msg').val();
  if (msg == "" || msg.trim() == "") {
    alert("Please fill in the content to send!");
  } else {
    ws.send(msg);
    addYourMsg(msg);
    $('#msg').val("");
  }
}

function addSystemMsg(time, msg) {
  var msg = 'System ' + time + ' : ' + msg;
  var p = $('<p/>', {'text': msg, 'class': 'span7 system-font'});
  room.append(p);
  room.scrollTop(room[0].scrollHeight);
}

function addYourMsg(msg) {
  var div_main = $('<div/>', {'class': 'pull-right'});

  var div_msg = $('<div/>', {'class': 'span3 chat-yourself'});
  div_main.append(div_msg);
  var p = $('<p/>', {'text': msg});
  div_msg.append(p);

  var div_role = $('<div/>', {'class': 'span2 speaker-info'});
  div_main.append(div_role);
  var photo = $('<img/>', {'class': 'role-image', 'src': "../img/physician.jpg"});
  div_role.append(photo);
  div_role.append("<h4>" + name + "<h4/>");
  div_role.append("<p>" + getTime() + "<p/>");

  room.append(div_main);
  room.scrollTop(room[0].scrollHeight);
}

function addChatMsg(json) {
  var div_main = $('<div/>', {'class': 'pull-left'});

  var div_role = $('<div/>', {'class': 'span2 speaker-info'});
  div_main.append(div_role);

  var photo = $('<img/>', {'class': 'role-image', 'src': "../img/patient.jpg"});
  div_role.append(photo);
  div_role.append("<h4>" + json.speaker + "<h4/>");
  div_role.append("<p>" + json.time + "<p/>");

  var div_msg = $('<div/>', {'class': 'span3 chat-patient'});
  div_main.append(div_msg);
  var p = $('<p/>', {'text': json.message});
  div_msg.append(p);

  room.append(div_main);
  room.scrollTop(room[0].scrollHeight);
}

function updateRoomMember(json) {
  if (json.event == "join") {
    var op = $('<div/>', {'id': json.id, 'class': "system-font", 'text': json.name});
    member_list.append(op);
    addSystemMsg(getTime(), "Paitent " + json.name + " has joined the chat.");
  } else {
    $('#' + json.id).remove();
    addSystemMsg(getTime(), "Paitent " + json.name + " has left the chat.");
  }
}

function getTime() {
  var date = new Date();
  var time = date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
  return time;
}


