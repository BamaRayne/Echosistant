/**
 *  Echosistant Profile 
 *
 *  3 Nov 2016       Most current version
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
 *********************************************************************************************************************************************/
definition(
    name: "echosistantProfile",
    namespace: "Echo",
    author: "JH",
    description: "Echosistant Profile DO NO intall this app directly.",
    category: "My apps",
    parent: "Echo:Echosistant",    
	iconUrl		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant.png",
	iconX2Url	: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png",
	iconX3Url	: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png")
//*********************************************************************************************************************************************
preferences {
    page name:"mainPage"
    	page name:"configuration"
    		page name: "notifications"
        		page name: "speech"
    			page name: "textMessage"
        		page name: "audioDevices"
    				page name: "sonos"
        	page name: "restrictions"
    			page name: "certainTime"
    	page name: "about"	
}
//************************************************************************************************************
//Show main page
def mainPage() {
    dynamicPage(name: "mainPage", title:"                      ${textAppName()}", install: true, uninstall: false) {
        section("") {
 	href "configuration", title: "Profile Configuration", description: "Tap here to configure installed application options (Pre-messages, restrictions, texts...)",
  			 	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Config.png"
    href "about", title: "About '${textAppName()}'", description: "Tap to get version, license information, Securty Tokens, and to remove the app",
            	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_About.png"
            }
            section("                               Rename Profile"){
        	label title:"              Rename Profile ", required:true, defaultValue: "${textAppName()}"    		
        }
	}
}
def configuration() {
	dynamicPage(name: "configuration", uninstall: false) {
    	section (""){
    		href "speech", title: "Audio Pre-message and Alexa Response", description: "Tap here to configure", 
            	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Media.png"
			href "textMessage", title: "Text Messages", description: "Tap here to set up text messages", 
            	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Text.png"
			href "audioDevices", title: "Audio Playback Devices", description: "Tap here to choose your playback devices", 
            	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Media.png"
			href "restrictions", title: "Profile Restrictions", description: "Tap here to configure this Profiles Restrictions", 
            	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_About.png"
 		}
	}
}    
page name: "about"
	def about(){
		dynamicPage(name: "about", uninstall: true) {
           section ("Security Tokens - FOR CHILD APP ONLY"){
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
			section("SmartApp Instructions") { paragraph ("For detailed installation and how-to instruction, follow the link below") 
			}
            section ("Source Code to create the Amazon Skill"){
            if (!state.accessToken) OAuthToken()
            if (!state.accessToken)  paragraph "**You must enable OAuth via the IDE to produce the command cheat sheet**"             
            else href url:"${getApiServerUrl()}/api/smartapps/installations/${app.id}/setup?access_token=${state.accessToken}", 
            style:"embedded", required:false, title: "", 
            description: "Tap to display the code needed to build Amazon Skill.\n"+
            "Use Live Logging in the SmartThings IDE to obtain the address for use on your computer broswer."
			}
			section("Tap below to remove the ${textAppName()} application.  This will remove this Profile only from the SmartThings mobile App."){}
		}
}
def restrictions(){
    dynamicPage(name: "restrictions", title: "Configure Restrictions", uninstall: false){
     	section ("Mode Restrictions") {
			input "modes", "mode", title: "Only when mode is", multiple: true, required: false, submitOnChange: true
	        }        
        section ("Days - Audio only on these days"){	
            input "runDay", title: "Only on certain days of the week", multiple: true, required: false, submitOnChange: true,
                "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
			}
        section ("Time - Audio only during these times"){
            href "certainTime", title: "Only during a certain time", description: timeLabel ?: "Tap to set", state: timeLabel ? "complete" : null
        }   
	}
}	
def speech(){
    dynamicPage(name: "speech", title: "Configure Speech Notifications", uninstall: false){
		section (""){ 
    	input "ShowPreMsg", "bool", title: "Enable TTS Pre-Message", default: false, submitOnChange: true
        	if (ShowPreMsg) input "PreMsg", "text", title: "Pre-Message Notification", required: true, defaultValue: "Attention.  Attention Please   "
            }
        section ( "" ){
        input "feedBack", "bool", title: "Enable Custom Alexa Response", default: false, submitOnChange: true
        	if (feedBack) input "outputTxt", "text", title: "Alexa Response", defaultValue: "Message Sent", required: false
      	} 
    }
}
def textMessage(){
	dynamicPage(name: "textMessage", uninstall: false) {
    	section (""){ 
    	input "sendContactText", "bool", title: "Enable Text Notifications to Contact Book (if available)", required: true, submitOnChange: true
        	if (sendContactText) input "recipients", "contact", title: "Send text notifications to (optional)", multiple: true, required: false
         }       	
         section ( "" ){            
         input "sendText", "bool", title: "Enable Text Notifications to non-contact book phone(s)", required: true, submitOnChange: true           
             if (sendText){      
                    paragraph "You may enter multiple phone numbers separated by semicolon to deliver the Alexa message as a text and a push notification. E.g. 8045551122;8046663344"
                	input name: "sms", title: "Send text notification to (optional):", type: "phone", required: false
					input "push", "bool", title: "Send Push Notification (optional)", required: false, defaultValue: false
            	}            
			}
		 section ( "" ){            
          if (sendContactText || sendText) {         
			paragraph "By default Echosistant will deliver both voice and text messages to selected devices and contacts(s). Enable text ONLY using toggle below"
    		input "disableTts", "bool", title: "Disable spoken notification (only send text message to selected contact(s)", required: true, submitOnChange: true  
            }            
		}        
	}  
}
def audioDevices(){
    dynamicPage(name: "audioDevices", title: "Media Devices", uninstall: false){
     	section("Media Speakers (Sonos, wi-fi, music players...)", hideWhenEmpty: true){
        	href "sonos", title: "Choose Media Speakers", description: none
            }
        section("Speech Synthesizer Devices (LanDroid, etc...)", hideWhenEmpty: true){
        	input "synthDevice", "capability.speechSynthesis", title: "Choose Speech Synthesis Devices", multiple: true, required: false, submitOnChange: true
        	if (synthDevice) input "synthVolume", "number", title: "Speaker Volume", description: "0-100%", required: false
         }
	}
}
def sonos(){
    dynamicPage(name: "sonos", uninstall: false){
    	section{ paragraph "Configure Media Speakers (Sonos Compatible" }
         section (" "){
        	input "sonosDevice", "capability.musicPlayer", title: "On this Media Speaker", required: false, multiple: true, submitOnChange: true
		    input "volume", "number", title: "Temporarily change volume", description: "0-100%", required: false
            input "resumePlaying", "bool", title: "Resume currently playing music after notification", required: false, defaultValue: false
		}  
	}
}
def certainTime() {
	dynamicPage(name:"certainTime",title: "Only during a certain time", uninstall: false) {
		section() {
			input "startingX", "enum", title: "Starting at", options: ["A specific time", "Sunrise", "Sunset"], defaultValue: "A specific time", submitOnChange: true
			if(startingX in [null, "A specific time"]) input "starting", "time", title: "Start time", required: false
			else {
				if(startingX == "Sunrise") input "startSunriseOffset", "number", range: "*..*", title: "Offset in minutes (+/-)", required: false
				else if(startingX == "Sunset") input "startSunsetOffset", "number", range: "*..*", title: "Offset in minutes (+/-)", required: false
			}
		}
		section() {
			input "endingX", "enum", title: "Ending at", options: ["A specific time", "Sunrise", "Sunset"], defaultValue: "A specific time", submitOnChange: true
			if(endingX in [null, "A specific time"]) input "ending", "time", title: "End time", required: false
			else {
				if(endingX == "Sunrise") input "endSunriseOffset", "number", range: "*..*", title: "Offset in minutes (+/-)", required: false
				else if(endingX == "Sunset") input "endSunsetOffset", "number", range: "*..*", title: "Offset in minutes (+/-)", required: false
			}
		}
	}
}
def installed() {
log.debug "Installed with settings: ${settings}"
initialize()
}
def updated() {
log.debug "Updated with settings: ${settings}"
initialize()
unsubscribe()
}
def initialize() {
	profileEvaluate(parent.processTts())
}    
def subscribeToEvents() {
	if (runModes) {
		subscribe(runMode, location.currentMode, modeChangeHandler)
	}
    if (runDay) {
       subscribe(runDay, location.day, location.currentDay)
       }
} 
def unsubscribeToEvents() {
	if (triggerModes) {
    	unsubscribe(location, modeChangeHandler)
    }
}    
/************************************************************************************************************
	WEB PAGE ELEMENTS
******************************************************************************************************/
mappings {
	path("/u") { action: [GET: "getURLs"] }
    path("/setup") { action: [GET: "setupData"] }
}
def setupData(){
	log.info "Install setup data web page located at : ${getApiServerUrl()}/api/smartapps/installations/${app.id}/setup?access_token=${state.accessToken}"
    def result ="<div style='padding:10px'><i><b><a href='https://developer.amazon.com' target='_blank'>Amazon Skill Code</a></i>"+
    "</b>Intent Schema:</b><br>Code goes here<br>var STtoken;<br>"
    result += "var url='${getApiServerUrl()}/api/smartapps/installations/' + STappID + '/' ;<br><br><hr>"
	displayData(result)
}
def OAuthToken(){
	try {
        createAccessToken()
		log.debug "Creating new Access Token"
	} catch (e) { log.error "Access Token not defined. OAuth may not be enabled. Go to the SmartApp IDE settings to enable OAuth." }
}
def displayData(display){
	render contentType: "text/html", data: """<!DOCTYPE html><html><head><meta charset="UTF-8" /><meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0"/></head><body style="margin: 0;">${display}</body></html>"""
}
/******************************************************************************************************
   SPEECH AND TEXT PROCESSING
******************************************************************************************************/
def profileEvaluate(params) {
        def tts = params.ptts 
        def txt = params.pttx
		def intent  = params.pintentName
        def childName = app.label
   	if (intent == childName){
		if (!disableTts){
			tts = PreMsg + tts
            if (getDayOk()==true && getModeOk()==true && getTimeOk()==true) {
            	if (synthDevice) synthDevice?.speak(tts)
        		if (mediaDevice) mediaDevice?.speak(tts)
            	if (tts) {
					state.sound = textToSpeech(tts instanceof List ? tts[0] : tts)
				}
				else {
					state.sound = textToSpeech("You selected the custom message option but did not enter a message in the $app.label Smart App")
				}
				if (sonosDevice) {
					sonosDevice.playTrackAndResume(state.sound.uri, state.sound.duration, volume)
        		}
    		}
    		sendtxt(txt)
		}
		else {
    		sendtxt(txt)
		}
	} 
}
/***********************************************************************************************************************
    RESTRICTIONS HANDLER
***********************************************************************************************************************/
private def textHelp() {
	def text =
		"This smartapp allows you to speak freely to your Alexa device and have it repeated back on a remote playback device"h
}
private getModeOk() {
    def result = !modes || modes?.contains(location.mode)
	result
} 
private getDayOk() {
    def df = new java.text.SimpleDateFormat("EEEE")
		location.timeZone ? df.setTimeZone(location.timeZone) : df.setTimeZone(TimeZone.getTimeZone("America/New_York"))
		def day = df.format(new Date())
	    def result = !runDay || runDay?.contains(day)
    	return result
}
private getTimeOk() {
	def result = true
	if ((starting && ending) ||
	(starting && endingX in ["Sunrise", "Sunset"]) ||
	(startingX in ["Sunrise", "Sunset"] && ending) ||
	(startingX in ["Sunrise", "Sunset"] && endingX in ["Sunrise", "Sunset"])) {
		def currTime = now()
		def start = null
		def stop = null
		def s = getSunriseAndSunset(zipCode: zipCode, sunriseOffset: startSunriseOffset, sunsetOffset: startSunsetOffset)
		if(startingX == "Sunrise") start = s.sunrise.time
		else if(startingX == "Sunset") start = s.sunset.time
		else if(starting) start = timeToday(starting,location.timeZone).time
		s = getSunriseAndSunset(zipCode: zipCode, sunriseOffset: endSunriseOffset, sunsetOffset: endSunsetOffset)
		if(endingX == "Sunrise") stop = s.sunrise.time
		else if(endingX == "Sunset") stop = s.sunset.time
		else if(ending) stop = timeToday(ending,location.timeZone).time
		result = start < stop ? currTime >= start && currTime <= stop : currTime <= stop || currTime >= start
	}
	return result
}
private hhmm(time, fmt = "h:mm a") {
	def t = timeToday(time, location.timeZone)
	def f = new java.text.SimpleDateFormat(fmt)
	f.setTimeZone(location.timeZone ?: timeZone(time))
	f.format(t)
}
private offset(value) {
	def result = value ? ((value > 0 ? "+" : "") + value + " min") : ""
}
private timeIntervalLabel() {
	def result = ""
	if      (startingX == "Sunrise" && endingX == "Sunrise") result = "Sunrise" + offset(startSunriseOffset) + " to Sunrise" + offset(endSunriseOffset)
	else if (startingX == "Sunrise" && endingX == "Sunset") result = "Sunrise" + offset(startSunriseOffset) + " to Sunset" + offset(endSunsetOffset)
	else if (startingX == "Sunset" && endingX == "Sunrise") result = "Sunset" + offset(startSunsetOffset) + " to Sunrise" + offset(endSunriseOffset)
	else if (startingX == "Sunset" && endingX == "Sunset") result = "Sunset" + offset(startSunsetOffset) + " to Sunset" + offset(endSunsetOffset)
	else if (startingX == "Sunrise" && ending) result = "Sunrise" + offset(startSunriseOffset) + " to " + hhmm(ending, "h:mm a z")
	else if (startingX == "Sunset" && ending) result = "Sunset" + offset(startSunsetOffset) + " to " + hhmm(ending, "h:mm a z")
	else if (starting && endingX == "Sunrise") result = hhmm(starting) + " to Sunrise" + offset(endSunriseOffset)
	else if (starting && endingX == "Sunset") result = hhmm(starting) + " to Sunset" + offset(endSunsetOffset)
	else if (starting && ending) result = hhmm(starting) + " to " + hhmm(ending, "h:mm a z")
}
private void sendText(number, message) {
    if (sms) {
        def phones = sms.split("\\;")
        for (phone in phones) {
            sendSms(phone, message)
        }
    }
}
private void sendtxt(message) {
    log.debug message
    if (sendContactText) { 
    //if (location.contactBookEnabled) {
        sendNotificationToContacts(message, recipients)
    } //else {
    	if (push) {
            sendPush message
        } else {
            sendNotificationEvent(message)
        }
        if (sms) {
            sendText(sms, message)
	}
}
/************************************************************************************************************
  Version/Copyright/Information/Help
*************************************************************************************************************/
private def textAppName() {
	def text = "This Profile"
}
private def textVersion() {
    def text = "Version 0.1.0 	(10/25/2016)"
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
private def textAmazonSkill() {
	def text =
	"Follow this link to create a new Alexa Skill using the code below "+
	"\n\n"+
	" https://developer.amazon.com/home.html"+
	"\n\n"+
	"Copy this code to populate the Intent Schema: "+
	"\n\n"+   
    "Copy this code to populate the Sample Utterances:"
}