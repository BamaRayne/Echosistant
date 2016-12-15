/**
 *  EchoSistant - Lambda Code
 *
 *  Copyright © 2016 Jason Headley
 *  Special thanks for Michael Struck @MichaelS (Developer of AskAlexa) for allowing me
 *  to build off of his base code.  Special thanks to Keith DeLong  @N8XD for his 
 *  assistance in troubleshooting.... as I learned.....  Special thanks to Bobby
 *  @SBDOBRESCU for jumping on board and being a co-consipirator in this adventure.
 *
 *  Version 3.1.2 - 12/12/2016
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
    var https = require( 'https' );
//-------- Paste app code here between the breaks------------------------------------------------
    var STappID = '4515cd7e-b04b-44f9-b076-62e2d72ba607';
    var STtoken = '4da26c84-89dd-4660-92d1-61de80d10824';
    var url='https://graph.api.smartthings.com:443/api/smartapps/installations/' + STappID + '/' ;
//-----------------------------------------------------------------------------------------------
        var cardName ="";
        var areWeDone = true;
//-------- Validation process and begining interaction with SmartThings app-------------------- 
        var versionTxt = '3.0';
        var versionDate= '12/12/2016';
        var releaseTxt = "3.1.2";
        var beginURL = url + 'b?&versionTxt=' + versionTxt + '&versionDate=' + versionDate + '&releaseTxt=' + releaseTxt + '&access_token=' + STtoken;
        https.get( beginURL, function( response ) {
        response.on( 'data', function( data ) {
            var startJSON = JSON.parse(data);
            var contOptions = startJSON.pContinue; //setting global variable if continuation is allowed for either control or tts
            var verST = startJSON.versionSTtxt;
//-------- Error trapping--------------------------------------------------------------------
            if (startJSON.error) { 
                output("There was an error. If this continues to happen, please reach out for help", context, "Lambda Error", areWeDone); 
            }
            if (startJSON.error === "invalid_token" || startJSON.type === "AccessDenied") {
                output("There was an error accessing the SmartThings cloud environment. Please check your security token and application ID and try again. ", context, "Lambda Error", areWeDone); 
            }
            if (verST != versionTxt) { 
                output("You are using outdated smart apps. Please make sure to update both the Lambda code and the SmartThings code to most recent versions, and then try again.", context, "Lambda Error", areWeDone);
            }
//-------- Begining Request------------------------------------------------------------------            
            if (event.request.type == "LaunchRequest") { 
                alexaResp ("LaunchRequest", context, areWeDone); 
            }
            else if (event.request.type == "SessionEndedRequest"){}
            else if (event.request.type == "IntentRequest") {
                var process = false;
                var intentName = event.request.intent.name;
                if (intentName.startsWith("AMAZON") && intentName.endsWith("Intent")) { 
                    alexaResp (intentName, context, "Amazon Intent", areWeDone); 
                }

                else if (intentName == "main"){           
                    var pCommand = event.request.intent.slots.pCommand.value;
                    var pProfile = event.request.intent.slots.pProfile.value;
                    var pNum = event.request.intent.slots.pNum.value;
                    var pDevice = event.request.intent.slots.pDevice.value;
                    url += 'c?pDevice=' + pDevice + '&pCommand=' + pCommand + '&pNum=' + pNum + '&pProfile=' + pProfile;    
                    process = true;
                    cardName = "EchoSistant Control";
                }
//-------- Control Type Request------------------------------------------------------------------
                else if (intentName != "main") {
                    var ttstext = event.request.intent.slots.ttstext.value;
                    url += 't?ttstext=' + ttstext + '&intentName=' + intentName;
                    process = true;
                    cardName = "EchoSistant Free Text";
                }
//-------- TTS Type Request------------------------------------------------------------------
                if (!process) {
                    output("I am not sure what you are asking. Please try again", context, areWeDone); 
                }                                        
                else {
                    url += '&access_token=' + STtoken;
                    https.get( url, function( response ) {
                        response.on( 'data', function( data ) {
                        var resJSON = JSON.parse(data);
                        var pContCmds = resJSON.pContCmds;
                        var speechText = resJSON.outputTxt;
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
        } );
    } );
};
    
function alexaResp(type, context, cardName, areWeDone){
    if (type == "AMAZON.YesIntent") { 
        areWeDone=false;
        output("Please continue...", context, "EchoSistant Stop", areWeDone);
    }
    else if (type == "AMAZON.NoIntent") { 
        areWeDone=true;
        output(" It has been my pleasure.  Goodbye ", context, "EchoSistant Stop", areWeDone);
    }
    else if (type == "AMAZON.StopIntent") { 
        areWeDone=true;
        output(" Cancelling. Goodbye ", context, "EchoSistant Cancel", areWeDone);
    }
    else if (type == "AMAZON.CancelIntent") { 
        areWeDone=true;
        output(" Cancelling. Goodbye ", context, "EchoSistant Cancel", areWeDone);
    }
}
function output( text, context, cardName, areWeDone) {
        var response = {
             outputSpeech: {
             type: "PlainText",
             text: text
                 },
                 card: {
                 type: "Simple",
                 title: cardName,
                 content: text
                    },
        shouldEndSession: areWeDone
        };
        context.succeed( { response: response } );
  }
