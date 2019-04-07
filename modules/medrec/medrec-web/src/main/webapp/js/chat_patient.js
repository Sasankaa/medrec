var webSocketSupport = false;
var ws;
var name;
var id;
var can_chat = false;
var physician_name;
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

  $('#join').click(function () {
    join();
  });
  $('#leave').click(function () {
    leave();
  });
  $('#send').click(function () {
    sendMsg();
  });
  $('#msg').keydown(function (e) {
    if (e.which == 13) {
      sendMsg();
    }
  });
  $('#choose').click(function () {
    choose();
  });
});

// Check if current browser support Web Socket
if ("WebSocket" in window) {
  webSocketSupport = true;
} else {
  alert("WebSocket not supported by your Browser!");
}

function join() {
  // Check if current browser support Web Socket
  if (!webSocketSupport) {
    alert("WebSocket not supported by your Browser!");
    return;
  }

  if (ws == null) {
    var ws_url = "ws://" + location.host + cp + "/chat/patient/" + id + "/" + name;
    ws = new WebSocket(ws_url);

    ws.onopen = function () {
      $('#information').empty();
      addSystemMsg(getTime(), "Connection's been set up.");
      getOnlinePhysiciansList();
    };

    ws.onmessage = function (evt) {
      var json = JSON.parse(evt.data);
      if (json.type == "all_online_physicians") {
        openChooseDialog(json.physicians);
      } else if (json.type == "all_room_member") {
        openChat(json.members);
      } else if (json.type == "system") {
        addSystemMsg(json.time, json.message);
      } else if (json.type == "chat") {
        addChatMsg(json);
      } else if (json.type == "room_member_update") {
        updateRoomMember(json);
      } else if (json.type == "room_close") {
        closeRoom(json.name);
      }
    };

    ws.onclose = function () {
      addSystemMsg(getTime(), "Connection's been shut down. ");
      member_list.empty();
      ws = null;
    };
  } else {
    getOnlinePhysiciansList();
  }
}

function leave() {
  // Check if current browser support Web Socket
  if (!webSocketSupport) {
    alert("WebSocket not supported by your Browser!");
    return;
  }

  if (ws != null) {
    var json_object = new Object();
    json_object.type = 'leave';
    var json_text = JSON.stringify(json_object);
    ws.send(json_text);
    member_list.empty();
    can_chat = false;
    if (physician_name != null) {
      addSystemMsg(getTime(), "Your have left physician " + physician_name + "'s chat room.");
    }
    $('#room-name').text("Not in chat room");
  } else {
    alert("Connection's not been set up yet.");
  }
}

function closeRoom(name) {
  if (name == physician_name) {
    can_chat = false;
    member_list.empty();
    addSystemMsg(getTime(), "Physician " + physician_name + " is offline. The chat room is close.");
    $('#room-name').text("Not in chat room");
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
    alert("Before sending message, please set up web socket connection first by clicking 'Join' button.");
    return;
  }

  if (!can_chat) {
    alert("Before sending message, please join one physician's chat.");
    return;
  }

  var msg = $('#msg').val();
  if (msg == "" || msg.trim() == "") {
    alert("Please fill in the content to send!");
  } else {
    var json_object = new Object();
    json_object.type = "chat";
    json_object.message = msg;
    var json_text = JSON.stringify(json_object);
    ws.send(json_text);
    addYourMsg(msg);
    $('#msg').val("");
  }
}

function getOnlinePhysiciansList() {
  var json_object = new Object();
  json_object.type = 'physicians';
  var json_text = JSON.stringify(json_object);
  ws.send(json_text);
}

function openChooseDialog(physicians) {
  var select = $('#physicians');
  select.empty();
  var op = $('<option/>', {'id': "eliza", 'class': "system-font", 'text': "Eliza"});
  select.append(op);
  for (var i = 0; i < physicians.length; i++) {
    var op = $('<option/>', {'id': physicians[i].id, 'class': "system-font", 'text': physicians[i].name});
    select.append(op);
  }
  $('#myModal').modal('show');
}

function choose() {
  var op = $('#physicians').find("option:selected");
  if (op.attr("id") != null) {
    var json_object = new Object();
    json_object.type = 'join';
    json_object.room_id = op.attr("id");
    var json_text = JSON.stringify(json_object);
    ws.send(json_text);
    physician_name = op.text();
    if (json_object.room_id == "eliza") {
      can_chat = true;
    }
  }
}

function openChat(members) {
  can_chat = true;
  member_list.empty();
  for (var i = 0; i < members.length; i++) {
    var op = $('<div/>', {'id': members[i].id, 'class': "system-font", 'text': members[i].name});
    member_list.append(op);
  }
  $('#information').empty();
  addSystemMsg(getTime(), "Your have join the chat.");
  $('#room-name').text(physician_name + "'s chat room");
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
  var photo = $('<img/>', {'class': 'role-image', 'src': "../img/patient.jpg"});
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

  var photo;
  if (json.role == "patient") {
    photo = $('<img/>', {'class': 'role-image', 'src': "../img/patient.jpg"});
  } else {
    photo = $('<img/>', {'class': 'role-image', 'src': "../img/physician.jpg"});
  }
  div_role.append(photo);
  div_role.append("<h4>" + json.speaker + "<h4/>");
  div_role.append("<p>" + json.time + "<p/>");

  var div_msg;
  if (json.role == "patient") {
    div_msg = $('<div/>', {'class': 'span3 chat-patient'});
  } else {
    div_msg = $('<div/>', {'class': 'span3 chat-physician'});
  }
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


