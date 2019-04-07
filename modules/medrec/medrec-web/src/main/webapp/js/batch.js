$(function () {
  var name = document.getElementById('a_name').value;
  var cp = $('#cp').val();
  $('.datepicker').datepicker({ dateFormat: 'yy-mm-dd'});

  var source;
  if (typeof(EventSource) == "undefined") {
    $('#result').text("Sorry, your browser does not support server-sent events...");
  }

  $('#processBatch').click(function () {
    initSSE();
    $.post("batch/report/" + name, {
      startDate: $('#startDate').val(),
      endDate: $('#endDate').val()
    }, function (data) {
      initTR(data);
    });
  });

  function initSSE() {
    source = new EventSource("batch/sse/" + name);
    source.onmessage = function (event) {
      var json = JSON.parse(event.data);
      initTR(json);
      var htd;
      if (json.type == "0") {
        htd = $('#' + json.seqId + '_r');
      } else if (json.type == "1") {
        htd = $('#' + json.seqId + '_p');
      } else if (json.type == "2") {
        htd = $('#' + json.seqId + '_d');
      }
      htd.append($('<a/>', {href: 'batch/report/' + json.fileName, text: json.fileName}));
    };
  }

  function initTR(json) {
    if ($('#' + json.seqId).length < 1) {
      var new_tr = $('<tr/>', {id: json.seqId});
      new_tr.append($('<td/>', {text: json.seqId}));
      new_tr.append($('<td/>', {text: json.startDate}));
      new_tr.append($('<td/>', {text: json.endDate}));
      new_tr.append($('<td/>', {id: json.seqId + '_r'}));
      new_tr.append($('<td/>', {id: json.seqId + '_p'}));
      new_tr.append($('<td/>', {id: json.seqId + '_d'}));
      $('#result').append(new_tr);
    }
  }
});