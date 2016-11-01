/**
 *  Echosistant - The Ultimate Voice Assistant. 

	11/1/2016 - Initial Release

 *
 /******************* ROADMAP ********************
  - External TTS
  - Icons
 *
 *
 *  Copyright 2016 Jason Headley
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

definition(
name		: "Echosistant",
namespace	: "Echo",
author		: "JH",
description	: "The Ultimate Voice Assistant.",
category	: "My Apps",
iconUrl		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant.png",
iconX2Url	: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png",
iconX3Url	: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png")
//*
preferences {
	page(name: "mainPage", title: "", install: true, uninstall: true) {
   		section {
			href "about", title: "                       Echosistant", description: "Tap here for details about Echosistant; Security tokens, Version, License..."
			} 		
        section {        
          	app(name: "Profiles", appName: "echosistantProfile", namespace: "Echo", multiple: true, description: "                  Create New Profile..." )
			}
        section ("Tap below to remove the ${textAppName()} application"){}          	
            }
	}
page name: "about"
	def about(){
		dynamicPage(name: "about", uninstall: true) {
                section("Security Tokens"){
                	paragraph ("Log into the IDE on your computer and navigate to the Live Logs tab. Leave that window open, come back here and open this section")
                    input "ShowTokens", "bool", title: "Show Security Tokens", default: false, submitOnChange: true
                    def msg = state.accessToken
                    if (!state.accessToken)            		
                    if (ShowTokens) paragraph "The Access Token and App ID are now displayed in your Live Logging tab of the IDE website."
                    if (ShowTokens) paragraph "The Access Token and App ID are now displayed in your Live Logging tab of the IDE website."
                    if (ShowTokens) paragraph "Access token:\n${msg}\n\nApplication ID:\n${app.id}"
                    if (ShowTokens) log.trace "STappID = '${app.id}' , STtoken = '${state.accessToken}'"
				}
			section ("Access Token / Application ID"){
				href "Tokens", title: "Renew/Reset/Revoke Access Security Token", description: none
				}
			section ("Apache License"){
				input "ShowLicense", "bool", title: "Show License", default: false, submitOnChange: true
				def msg = textLicense()
					if (ShowLicense) paragraph "${msg}"
			}
			section("Instructions") { paragraph ("For detailed installation and how-to instruction, follow the link below") 
			}
	}
}
page name: "Tokens"
	def Tokens(){
		dynamicPage(name: "Tokens", title: "Security Tokens", uninstall: false){
			section("") {
				paragraph "Tap below to Reset/Renew/Revoke the Security Token. You must log in to the IDE and open the Live Logs tab before tapping here. "+
				"Copy and paste the displayed tokens into your Amazon Lambda Code."
				if (!state.accessToken) {
                	OAuthToken()
					paragraph "You must enable OAuth via the IDE to setup this app"
				}
            }
	def msg = state.accessToken != null ? state.accessToken : "Could not create Access Token. "+
    "OAuth may not be enabled. Go to the SmartApp IDE settings to enable OAuth." 
//	log.trace "STappID = '${app.id}' , STtoken = '${state.accessToken}'"
			section ("Reset Access Token / Application ID"){
				href "pageConfirmation", title: "Reset Access Token and Application ID", description: none
			}
		}
	} 
page name: "pageReset"
	def pageReset(){
		dynamicPage(name: "pageReset", title: "Access Token Reset", uninstall: false){
			section{
				revokeAccessToken()
				state.accessToken = null
				OAuthToken()
				def msg = state.accessToken != null ? "New access token:\n${state.accessToken}\n\nClick 'Done' above to return to the previous menu." : "Could not reset Access Token."+
            	"OAuth may not be enabled. Go to the SmartApp IDE settings to enable OAuth."
				paragraph "${msg}"
			}
			section(" "){ 
        		href "mainPage", title: "Tap Here To Go Back To Main Menu", description: none 
        	}
		}
	} 
page name: "pageConfirmation"
	def pageConfirmation(){
		dynamicPage(name: "pageConfirmation", title: "Reset/Renew Access Token Confirmation", uninstall: false){
			section {
				href "pageReset", title: "Reset/Renew Access Token", description: "Tap here to confirm action - READ WARNING BELOW"
				paragraph "PLEASE CONFIRM! By resetting the access token you will disable the ability to interface this SmartApp with your Amazon Echo."+
            	"You will need to copy the new access token to your Amazon Lambda code to re-enable access." +
				"Tap below to go back to the main menu with out resetting the token. You may also tap Done above."
			}
			section(" "){
        		href "mainPage", title: "Cancel And Go Back To Main Menu", description: none 
        	}
		}
	}	
//************************************************************************************************************
mappings {
      path("/r") {action: [GET: "readData"]}
      path("/w") {action: [GET: "writeData"]}
      path("/t") {action: [GET: "processTts"]}
      path("/b") { action: [GET: "processBegin"] }
	  path("/u") { action: [GET: "getURLs"] }
	  path("/setup") { action: [GET: "setupData"] }}
//************************************************************************************************************
def installed() {
//	log.debug "Installed with settings: ${settings}"
//	log.trace "STappID = '${app.id}' , STtoken = '${state.accessToken}'"
	initialize()
}
def updated() {
//	log.debug "Updated with settings: ${settings}"
//	log.trace "STappID = '${app.id}' , STtoken = '${state.accessToken}'"
	initialize()
	unsubscribe()
}
def initialize() {
	if (!state.accessToken) {
		log.error "Access token not defined. Ensure OAuth is enabled in the SmartThings IDE."
	}
}
def writeData() {
    log.debug "Command received with params $params"
	}

def readData() {
    log.debug "Command received with params $params"  
}
//************************************************************************************************************
def OAuthToken(){
	try {
		createAccessToken()
		log.debug "Creating new Access Token"
	} catch (e) {
		log.error "Access Token not defined. OAuth may not be enabled. Go to the SmartApp IDE settings to enable OAuth."
	}
}
//************************************************************************************************************
//*** TEXT TO SPEECH PROCESS ***
def processTts() {	
        def ptts = params.ttstext 
        def pttx = params.ttstext
		def pintentName = params.intentName
		def outputTxt = "Message sent to ${pintentName}"
        log.debug "Message sent to ${pintentName}" 
        def dataSet = [ptts:ptts,pttx:pttx,pintentName:pintentName]    
    		childApps.each {child ->
    			child.profileEvaluate(dataSet)
			    }
        return ["outputTxt":outputTxt]
}        
//************************************************************************************************************
//Version/Copyright/Information/Help
private def textAppName() {
	def text = "Echosistant"
}	
private def textVersion() {
	def text = "Version 1.0.0 (10/29/2016)"
}
private def textCopyright() {
	def text = "Copyright © 2016 Jason Headley"
}
private def textLicense() {
	def text =
	"Licensed under the Apache License, Version 2.0 (the 'License'); "+
	"you may not use this file except in compliance with the License. "+
	"You may obtain a copy of the License at"+
	"\n\n"+
	" http://www.apache.org/licenses/LICENSE-2.0"+
	"\n\n"+
	"Unless required by applicable law or agreed to in writing, software "+
	"distributed under the License is distributed on an 'AS IS' BASIS, "+
	"WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. "+
	"See the License for the specific language governing permissions and "+
	"limitations under the License."
}
private def textHelp() {
	def text =
		"This smartapp allows you to use an Alexa device to generate a voice or text message on on a different device"
        "See our Wikilinks page for user information!"
}
