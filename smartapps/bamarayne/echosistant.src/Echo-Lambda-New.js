/**
 *  EchoSistant - Lambda Code
 *
 *  Copyright © 2016 Jason Headley
 *  Special thanks for Michael Struck @MichaelS (Developer of AskAlexa) for allowing me
 *  to build off of his base code.  Special thanks to Keith DeLong  @N8XD for his 
 *  assistance in troubleshooting.... as I learned.....  Special thanks to Bobby
 *  @SBDOBRESCU for jumping on board and being a co-consipirator in this adventure.
 *
 *  Version 3.1.0 - 12/7/2016
 *  Version 3.0.0 - 12/1/2016  Added new parent variables
 *  Version 2.0.0 - 11/20/2016  Continued Commands
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
'use strict';
exports.handler = function( event, context ) {
    var versionTxt = '3.1.0';
    var versionDate= '12/7/2016';
    var pName = "assistant";
    var https = require( 'https' );
    // Paste app code here between the breaks------------------------------------------------
    var STappID = '8c44d313-74d0-4f85-b4a1-97487ea74e0b';
    var STtoken = '6ba53670-0e98-4855-87cb-0b154fa630f9';
    var url='https://graph.api.smartthings.com:443/api/smartapps/installations/' + STappID + '/' ;
    //---------------------------------------------------------------------------------------
        var cardName ="";
        var stop;
        var areWeDone = false;
        var endSession = true;
        var processedText;
        var process = false;
        var intentName = event.request.intent.name;
        var ttstext = event.request.intent.slots.ttstext.value;
        var ttsintentname = event.request.intent.slots.ttstext.name.value;
        var speechText;
        var outputTxt;
        var pContCmds;
        //var pCommands = event.request.intent.slots.pCommands.value;
        //var pProfiles = event.request.intent.slots.pProfiles.value;
        var cancel;
        var no;
        console.log (event.request.type);   

        var beginURL = url + 'b?Ver=' + versionTxt + '&Date=' + versionDate;
        https.get( beginURL, function( response ) {
        response.on( 'data', function( data ) {
            var beginJSON = JSON.parse(data);
            pName = beginJSON.pMain;
        });
        });
if (event.request.type == "IntentRequest") {
if (ttstext=="stop") {
areWeDone=true;
output(" Stopping. Goodbye ", context, "Amazon Stop", areWeDone);
} 
else if (ttstext=="no") {
areWeDone=true;
output(" It has been my pleasure.  Goodbye ", context, "Amazon Stop", areWeDone);
}
else if (ttstext=="nope") {
areWeDone=true;
output(" It has been my pleasure.  Goodbye ", context, "Amazon Stop", areWeDone);
}
else if (ttstext=="no thank you") {
areWeDone=true;
output(" It has been my pleasure.  Goodbye ", context, "Amazon Stop", areWeDone);
}
else if (ttstext=="no we're done") {
areWeDone=true;
output(" It has been my pleasure.  Goodbye ", context, "Amazon Stop", areWeDone);
}
else if (ttstext=="no we're good") {
areWeDone=true;
output(" It has been my pleasure.  Goodbye ", context, "Amazon Stop", areWeDone);
}
else if (ttstext=="no I'm done") {
areWeDone=true;
output(" It has been my pleasure.  Goodbye ", context, "Amazon Stop", areWeDone);
}
else if (ttstext=="no thanks") {
areWeDone=true;
output(" It has been my pleasure.  Goodbye ", context, "Amazon Stop", areWeDone);
}
else if (ttstext=="cancel") {
areWeDone=true;
output(" Cancelling. Goodbye ", context, "Amazon Stop", areWeDone);
}
else if (ttstext=="yes") {
        areWeDone=false;    
    output("please continue...", context, areWeDone);
}
else if (ttstext=="okay") {
        areWeDone=false;
    output("please continue...", context, areWeDone);
}
else if (ttstext=="yeah") {
        areWeDone=false;
    output("please continue...", context, areWeDone);
}
else if (ttstext=="sure") {
        areWeDone=false;
    output("please continue...", context, areWeDone);
}
else if (ttstext === "repeat"+"last"+"message") {
                url += 't?ttstext=' + ttstext + '&intentName=' + intentName;
                process = true;
                areWeDone = false;
}
else if    (intentName === pName) {
                var pCommands = event.request.intent.slots.pCommands.value;
                var pProfiles = event.request.intent.slots.pProfiles.value;
                url += 't?ttstext=' + ttstext + '&intentName=' + intentName + '&pCommands=' + pCommands + '&pProfiles=' + pProfiles;
                process = true;
            }
else if (intentName !== pName) {
                url += 't?ttstext=' + ttstext + '&intentName=' + intentName;
                process = true;
}
if (!process) {
areWeDone=true;
output("I am not sure what you are asking. Please try again", context, areWeDone); 
}
else {
                    url += '&access_token=' + STtoken;
                    https.get( url, function( response ) {
                    response.on( 'data', function( data ) {
                    var resJSON = JSON.parse(data);
                    var pContCmds = resJSON.pContCmds;
                    var speechText = resJSON.outputTxt;
                    var pName = resJSON.pMainIntent;
                    console.log(speechText);
                    if (pContCmds === true) { 
                        areWeDone=false;
                        speechText = speechText + ', send another message?'; 
                    }
                    else {
                        areWeDone=true;
                    }
                    output(speechText, context, cardName, areWeDone);
                } );
            } );
        }
        }


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
                    shouldEndSession: areWeDone
                    };
                    context.succeed( { response: response } );
  }
};
