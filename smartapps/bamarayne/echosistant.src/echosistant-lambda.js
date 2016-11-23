/**
 *  EchoSistant - Lambda Code
 *
 *  Version 3.0.0 - 11/21/2016 Copyright © 2016 Jason Headley
 *  Special thanks for Michael Struck @MichaelS (Developer of AskAlexa) for allowing me
 *  to build off of his base code.  Special thanks to Keith DeLong  @N8XD for his 
 *  assistance in troubleshooting.... as I learned.....  Special thanks to Bobby
 *  @SBDOBRESCU for jumping on board and being a co-consipirator in this adventure.
 *  
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
    var https = require( 'https' );
    // Paste app code here between the breaks------------------------------------------------
    var STappID = 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx';
    var STtoken = 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx';
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
        var ttsTxt;
        var speechText;
        var outputTxt;
        var pContCmds;
        var cancel;
        var no;
     //   var repeat = "";
console.log (event.request.type);
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
else if (intentName) {
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
