/*
 * EchoSistant - The Ultimate Voice and Text Messaging Assistant Using Your Alexa Enable Device.
 *		
 * 
 *		11/20/2016		Version 1.2.0	Fixes: SMS&Push not working, calling multiple profiles at initialize. Additions: Run Routines and Switch enhancements
 *		11/13/2016		Version 1.1.1a	Roadmap update and spelling errors
 *		11/13/2016		Version 1.1.1	Addition - Repeat last message
 *		11/12/2016		Version 1.1.0	OAuth bug fix, additional debug actions, Alexa feedback options, Intent and Utterance file updates
 *										Control Switches on/off with delay off, pre-message "null" bug
 *		11/07/2016		Version 1.0.1f	Additional Debug messages and Alexa missing profile Response
 *		11/06/2016		Version 1.0.1d	Debug measures fixed
 *		11/06/2016		Version 1.0.1c  Debug measures added
 *		11/05/2016		Version 1.0.1b	OAuth Fix and Version # update 
 *		11/05/2016 		Version 1.0.1a	OAuth Log error	@ 11:46EST OAuth - Bobby
 *		11/05/2016		Version 1.0.1	OAuth error fix
 *		11/04/2016      Version 1.0		Initial Release
 *
 * ROADMAP
 * Profile count and name list
 * CoRE integration (milestone)
 * External TTS
 * External SMS
 * Continuation Commands with a conversational mode (milestone)
 *
 *
 *
 * Credits
 * Thank you to @MichaelS (creator of AskAlexa) for guidance and for letting me use his outstanding Wiki
 * and to begin this project using his code as a jump start.  Thanks goes to Keith @n8xd for his help with  
 * troubleshooting my lambda code. And a huge thank you to @SBDOBRESCU, the co-author of this project, for 
 * jumping on board and helping me expand this project into something more. 
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
/**********************************************************************************************************************************************/
definition(
	name		: "EchoSistant",
	namespace	: "Echo",
	author		: "JH",
	description	: "The Ultimate Voice and Text Messaging Assistant Using Your Alexa Enable Device.",
	category	: "My Apps",
    singleInstance: true,
	iconUrl		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant.png",
	iconX2Url	: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png",
	iconX3Url	: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png")
/**********************************************************************************************************************************************/
preferences {  //SHOW MAIN PAGE
	page(name: "mainPage", title: "EchoSistant", install: true, uninstall: false) {
		section {
        	href "profiles", title: "View and Create Profiles", description: "Tap here to view and create new profiles....",
            image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Config.png"
			}
		section {
			href "about", title: "About EchoSistant", description: "Tap here for App information...Tokens, Version, License...",
            image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_About.png"
			}
        section ("EchoSistant Smartapp Information") {
        	paragraph "${textAppName()}\n${textVersion()}\n${textCopyright()}",
            image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant.png"
            }
        section ("Directions, How-to's, and Troubleshooting") { 
			href url:"http://thingsthataresmart.wiki/index.php?title=EchoSistant", title: "EchoSistant Wiki", description: none
        	}
        section ("Rename Main Intent") { 
			input "mainIntent", "text", title: "Main Intent", defaultValue: "assistant", required: false
        	}
}
	}
	page(name: "profiles", title: "Profiles", install: true, uninstall: false) {
        section {
        	app(name: "Profiles", appName: "echosistantProfile", namespace: "Echo", description: "Create New Profile...", multiple: true)
            image: "https://github.com/BamaRayne/Echosistant/blob/master/smartapps/bamarayne/echosistant.src/Echosistant_Config.png"
			}
		}
page name: "about"
	def about(){
		dynamicPage(name: "about", uninstall: true) {
            section ("Security Tokens - FOR PARENT APP ONLY"){
            	paragraph ("Log into the IDE on your computer and navigate to the Live Logs tab. Leave that window open, come back here, and open this section")
                input "ShowTokens", "bool", title: "Show Security Tokens", default: false, submitOnChange: true
                if (ShowTokens) paragraph "The Security Tokens are now displayed in the Live Logs section of the IDE"
            	def msg = state.accessToken != null ? state.accessToken : "Could not create Access Token. OAuth may not be enabled. "+
				"Go to the SmartApp IDE settings to enable OAuth."	
                if (ShowTokens) log.info "STappID = '${app.id}' , STtoken = '${state.accessToken}'"
                if (ShowTokens) paragraph "Access token:\n${msg}\n\nApplication ID:\n${app.id}"
				}
			section ("Revoke/Renew Access Token & Application ID"){
				href "Tokens", title: "Revoke/Reset Security Access Token", description: none
				}
			section ("Apache License"){
				input "ShowLicense", "bool", title: "Show License", default: false, submitOnChange: true
				def msg = textLicense()
					if (ShowLicense) paragraph "${msg}"
				}
            section("Debugging") {
            	input "debug", "bool", title: "Enable Debug Logging", default: false, submitOnChange: true 
            if (debug) log.info "${textAppName()}\n${textVersion()}"
            	}
			section("Tap below to remove the ${textAppName()} application.  This will remove ALL Profiles and the App from the SmartThings mobile App."){}
			}
		}      
page name: "Tokens"
	def Tokens(){
		dynamicPage(name: "Tokens", title: "Security Tokens", uninstall: false){
			section(""){
				paragraph "Tap below to Reset/Renew the Security Token. You must log in to the IDE and open the Live Logs tab before tapping here. "+
				"Copy and paste the displayed tokens into your Amazon Lambda Code."
				if (!state.accessToken) {
                	OAuthToken()
					paragraph "You must enable OAuth via the IDE to setup this app"
					}
            	}
					def msg = state.accessToken != null ? state.accessToken : "Could not create Access Token. "+
    				"OAuth may not be enabled. Go to the SmartApp IDE settings to enable OAuth." 
					log.trace "STappID = '${app.id}' , STtoken = '${state.accessToken}'"
			section ("Reset Access Token / Application ID"){
				href "pageConfirmation", title: "Reset Access Token and Application ID", description: none
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
page name: "pageReset"
	def pageReset(){
		dynamicPage(name: "pageReset", title: "Access Token Reset", uninstall: false){
			section{
				revokeAccessToken()
				state.accessToken = null
				OAuthToken()
				def msg = state.accessToken != null ? "New access token:\n${state.accessToken}\n\n" : "Could not reset Access Token."+
            	"OAuth may not be enabled. Go to the SmartApp IDE settings to enable OAuth."
				paragraph "${msg}"
                paragraph "The new access token and app ID are now displayed in the Live Logs tab of the IDE."
                log.info "STappID = '${app.id}' , STtoken = '${state.accessToken}'"
			}
			section(" "){ 
        		href "mainPage", title: "Tap Here To Go Back To Main Menu", description: none 
        		}
			}
		} 	
        def echoSistantHandler(evt) {
	log.debug "Refreshing CoRE Piston List"
    if (evt.value =="installed") { state.CoREPistons = evt.jsonData && evt.jsonData?.pistons ? evt.jsonData.pistons : [] }
}
def getCoREMacroList(){ return getChildApps().findAll {it.macroType !="CoRE"}.label }
//************************************************************************************************************
mappings {
      path("/t") {action: [GET: "processTts"]}
      }
//************************************************************************************************************
def installed() {
	if (debug) log.debug "Installed with settings: ${settings}"
	if (debug) log.trace "STappID = '${app.id}' , STtoken = '${state.accessToken}'"
	initialize()
}

def updated() {
	if (debug) log.debug "Updated with settings: ${settings}"
    initialize()
	unsubscribe()
}
def getProfileList() { return getChildApps()*.label }
	if (debug) log.debug "Your installed Profiles are ${getChildApps()*.label}"
def childUninstalled() {
	sendLocationEvent(name: "echoSistant", value: "refresh", data: [profiles: parent ? parent.getCoREList() : getCoreProfileList()] , isStateChange: true, descriptionText: "echoSistant Profile list refresh")
}
def initialize() {
	state.lastMessage = null
	state.lastIntent  = null
    state.lastTime  = null
	def children = getChildApps()
	if (debug) log.debug "$children.size Profiles installed"
		children.each { child ->
		}
	if (!state.accessToken) {
        subscribe(location, "CoRE", coreHandler) 
               OAuthToken()
    	section() {
    	paragraph "You must enable OAuth via the IDE to setup this app"
		}
		log.trace "STappID = '${app.id}' , STtoken = '${state.accessToken}'"            		
	}  
 }
/*************************************************************************************************************
   CREATE INITIAL TOKEN
*************************************************************************************************************/
def OAuthToken(){
	try {
		createAccessToken()
		log.debug "Creating new Access Token"
	} catch (e) {
		log.error "Access Token not defined. OAuth may not be enabled. Go to the SmartApp IDE settings to enable OAuth."
	}
}
/************************************************************************************************************
   TEXT TO SPEECH PROCESS 
************************************************************************************************************/
def processTts() {
		def ptts = params.ttstext 
            if (debug) log.debug "#1 Message received from Lambda (ptts) = '${ptts}'"
        def pttx = params.ttstext
        	if (debug) log.debug "#2 Message received from Lambda (pttx) = '${pttx}'"
   		def pintentName = params.intentName
			if (debug) log.debug "#3 Profile being called = '${pintentName}'"
        def outputTxt = ''
    	def dataSet = [ptts:ptts,pttx:pttx,pintentName:pintentName] 
		def repeat = "repeat"
       	def pMainIntent ="assistant"
        	if (mainIntent){
            		pMainIntent = mainIntent
        	}
        if (debug) log.debug "#4 Main intent being called = '${pMainIntent}'"    
        if (ptts==repeat) {
				if (pMainIntent == pintentName) {
                outputTxt = "The last message sent was," + state.lastMessage + ", and it was sent to, " + state.lastIntent + ", at, " + state.lastTime 
				}
                else {
                      	childApps.each { child ->
    						def cLast = child.label
            				if (cLast == pintentName) {
                        		if (debug) log.debug "Last Child was = '${cLast}'"  
                                def cLastMessage 
                       			def cLastTime
                                outputTxt = child.getLastMessage()
                                if (debug) log.debug "Profile matched is ${cLast}, last profile message was ${outputTxt}" 
                			}
               		}
               }
        }    
		else {
    			if (ptts){
     				state.lastMessage = ptts
                    state.lastIntent = pintentName
                    state.lastTime = new Date(now()).format("h:mm aa", location.timeZone)
                    childApps.each {child ->
						child.profileEvaluate(dataSet)
            			}
            			childApps.each { child ->
    					def cm = child.label
                	if (child.AfeedBack) {
            			if (cm == pintentName) {
                			if (child.Acustom) {
                            outputTxt = child.outputTxt
                            }
                            else {
                        		if (child.Arepeat) {
                                outputTxt = "I have delivered the following message to '${cm}',  " + ptts
								}
                                else {
                        			if (pintentName == repeatMessage) {
                                    	return result
                                    }
                        			else {
                            			outputTxt = "Message sent to ${pintentName}, " 
										if (debug) log.debug "#5 Alexa verbal response = '${outputTxt}'"
           							}
                                }
                             }
           				}  
                  	}
				}
      		}
         }
        return ["outputTxt":outputTxt]
        if (debug) log.debug "#6 Alexa response sent to Lambda = '${outputTxt}'"
        }
/************************************************************************************************************
   Version/Copyright/Information/Help
************************************************************************************************************/
private def textAppName() {
	def text = "echoSistant"
}	
private def textVersion() {
	def text = "Version 1.2.0 (11/20/2016)"
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
