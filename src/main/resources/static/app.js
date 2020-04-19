$(function () {
    'use strict';

    var client;

    function showMessage(mesg) {
        $('#messages').append('<tr>' +
            '<td>' + mesg.from + '</td>' +
            '<td>' + mesg.topic + '</td>' +
            '<td>' + mesg.text + '</td>' +
            '<td>' + mesg.date + '</td>' +
            '</tr>');
    }

    function setConnected(connected) {
        $("#connect").prop("disabled", connected);
        $("#disconnect").prop("disabled", !connected);
        $('#from').prop('disabled', connected);
        $('#text').prop('disabled', !connected);
        if (connected) {
            $("#conversation").show();
            $('#text').focus();
        }
        else $("#conversation").hide();
        $("#messages").html("");
    }

    $("form").on('submit', function (e) {
        e.preventDefault();
    });

    $('#from').on('blur change keyup', function (ev) {
        $('#connect').prop('disabled', $(this).val().length == 0);
    });
    $('#connect,#disconnect,#text').prop('disabled', true);

    $('#connect').click(function () {
        try{
            console.log("step1")
            var skj = new SockJS('http://localhost:8085/ymn/bridge');
            console.log("step2")
            client = Stomp.over(skj);
            console.log("step3")
            client.connect({}, function (frame) {
                console.log("step4")
                console.log(frame)
                setConnected(true);
                
                client.subscribe('/ymn/topic/messages', function (message) {
                    console.log("step5")
                    console.log("message: "+message)
                    showMessage(JSON.parse(message.body));
                });
                start();
            });
        }catch(e){
            console.log(e);
        }
    });

    $('#disconnect').click(function () {
        if (client != null) {
            client.disconnect();
            setConnected(false);
        }
        client = null;
    });

    $('#send').click(function () {
        var topic = $('#topic').val();
        client.send("/ymn/cross/chat", {}, JSON.stringify({
            from: $("#from").val(),
            text: $('#text').val(),
            topic: $('#topic').val()
        }));
        $('#text').val("");
    });
    
    function start(){
    	setInterval(function() {
        	client.send("/ymn/cross/chat", {}, JSON.stringify({
                from: $("#from").val(),
                text: $('#text').val(),
                topic: $('#topic').val()
            }));
        }, 3000);
    }
});