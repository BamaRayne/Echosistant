/**
 *  Alexa Speaks - Lambda Code
 *
 *  Version 1.0.0 - 10/29/2016 Copyright © 2016 Jason Headley
 *  Special thanks for Michael Struck @MichaelS (Developer of AskAlexa) for allowing me
 *  to build off of his base code.  Special thanks to Keith DeLong  @N8XD for his 
 *  assistance in troubleshooting.... as I learned.....  Special thanks to Bobby
 *  @SBDOBRESCU for jumping on board and being a co-consipirator in this adventure.
 *  
 *  Version 1.0.0 - Initial release
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
    var STappID = 'xxxxx';
    var STtoken = 'xxxxx';
    var url='https://graph.api.smartthings.com:443/api/smartapps/installations/' + STappID + '/' ;
    //---------------------------------------------------------------------------------------
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
                 title: "Alexa Speaks Smartapp",
                 content: text
                    },
                    shouldEndSession: true
                    };
                    context.succeed( { response: response } );
                    }
