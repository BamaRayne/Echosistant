        var cardName ="";
        var endSession = true;
        var processedText;
        var process = false;
        var intentName = event.request.intent.name;
        var ttstext = event.request.intent.slots.ttstext.value;
        var ttsintentname = event.request.intent.slots.ttstext.name.value;
        var speechText;
        var outputTxt;
            if (intentName) {
                url += 't?ttstext=' + ttstext + '&intentName=' + intentName;
                process = true;
            } 
            if (!process) ("I'm sorry, I did not understand what you are requesting. please try again",context);
               else {
                    url += '&access_token=' + STtoken;
                    https.get( url, function( response ) {
                    response.on( 'data', function( data ) {
                    var resJSON = JSON.parse(data);
                    var speechText = "The SmartThings SmartApp returned an error. I was unable to complete your request";
                    console.log(speechText);
                output(resJSON.outputTxt, context, cardName);
                } );
            } );
        }
    };
        
            function output( text, context ) {
            var response = {
             outputSpeech: {
             type: "PlainText",
             text: text
                 },
                 card: {
                 type: "Simple",
                 title: "EchoSistant Smartapp",
                 content: text
                    },
                    shouldEndSession: true
                    };
                    context.succeed( { response: response } );
                    }
