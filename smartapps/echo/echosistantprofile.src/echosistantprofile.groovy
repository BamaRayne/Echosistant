/**
 *  Echosistant Profile 
 *
 *		11/12/2016		version 1.1.0	OAuth bug fix, additional debug actions, Alexa feedback options, Intent and Utterance file updates
 *										Control Switches on/off with delay off
 *		11/09/2016		Version 1.0.1b	Message Config Options
 * 		11/06/2016		Version 1.0.1a	Additional Debug messages
 *		11/06/2016		Version 1.0.1	Debugging added
 *		11/04/2016     	Version 1.0		Initial Release    
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
            page name: "devices"
        	page name: "restrictions"
    			page name: "certainTime"
            page name: "CoRE"
}
//************************************************************************************************************
//Show main page
def mainPage() {	       
    dynamicPage(name: "mainPage", title:"", install: true, uninstall: true) {
        section("") {
    		href "speech", title: "Audio Message Options", description: "Tap here to configure", 
            	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_msg.png"
			href "textMessage", title: "Text Messages", description: "Tap here to configure", 
            	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Text.png"
			href "audioDevices", title: "Audio Playback Devices", description: "Tap here to configure", 
            	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Media.png"
			href "restrictions", title: "Profile Restrictions", description: "Tap here to configure", 
                image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Rest.png"
            href "devices", title: "Control these devices", description: "Tap here to configure",
            	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Rest.png"
//            href "CoRE", title: "CoRE Integration", description: "Tap here to configure CoRE options...",
//            	image: "https://cdn.rawgit.com/ady624/CoRE/master/resources/images/app-CoRE.png"
        }
        section ("") {
 		   	label title:"              Rename Profile ", required:false, defaultValue: "New Profile"  
            }
        section ("") {
          	paragraph "      Tap below to remove this Profile"    	
        }
	}
} 
//def CoRE() {
//	dynamicPage(name: "CoRE", install: false, uninstall: false) {
//		section { paragraph "CoRE Trigger Settings" }
//		section (" "){
//   			input "CoREName", "enum", title: "Choose CoRE Piston", options: parent.state.CoREPistons, required: false, multiple: false
//        	input "cDelay", "number", title: "Default Delay (Minutes) To Trigger", defaultValue: 0, required: false
//        }
//        if (!parent.state.CoREPistons){
//        	section("Missing CoRE pistons"){
//				paragraph "It looks like you don't have the CoRE SmartApp installed, or you haven't created any pistons yet. To use this capability, please install CoRE or, if already installed, create some pistons, then try again."
//            }
//        }	
//    }
//}
def devices(){
    dynamicPage(name: "devices", install: false, uninstall: false) {
        section { paragraph "Switches/Dimmers/Colored lights"} 
        section("Choose the switches to turn on with this profile...", hideWhenEmpty: true) {
            input "switches", "capability.switch", title: "Choose Switches", multiple: true, required: false
            input "dimmers", "capability.switchLevel", title: "Choose Dimmers", multiple: true, required: false
            input "cLights", "capability.colorControl", title: "Choose Colored Lights", multiple: true, required: false, submitOnChange: true
    	}
        section("And then off after a set amount of time..."){
			input "minutesLater", "number", title: "Minutes?", defaultValue: 0, required: false
            input "secondsLater", "number", title: "Seconds?", defaultValue: 0, required: false
		}
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
    	input "ShowPreMsg", "bool", title: "Pre-Message", defaultValue: true, submitOnChange: true
        	if (ShowPreMsg) input "PreMsg", "text", title: "Pre-Message to play before your message", required: false, defaultValue: "Attention, Attention please..  "
            if (!ShowPreMsg) paragraph "Enable for pre-messages"
            if (ShowPreMsg) paragraph "Disable to stop pre-messages"
            }
        section ( "" ){
        input "Acustom", "bool", title: "Custom Alexa Responses", default: false, submitOnChange: true
        	if (Acustom) input "outputTxt", "text", title: "Custom Alexa Responses", defaultValue: none, required: false
      		if (!Acustom) paragraph "Enable for Custom Alexa Responses"
            if (Acustom) paragraph "Disable to stop Custom Alexa Responses"
        }
        section ( "" ){
        input "AfeedBack", "bool", title: "Alexa Feedback Responses", defaultValue: true, submitOnChange: true
			if (!AfeedBack) paragraph "Enable for Alexa Responses"
        	if (AfeedBack) paragraph "Disable to stop all Alexa Responses"
        }
        section ( "" ){
        input "Arepeat", "bool", title: "Repeat message to Sender", defaultValue: false, submitOnChange: true
			if (!Arepeat) paragraph "Enable to have Alexa repeat the message to the Sender"
        	if (Arepeat) paragraph "Disable to stop Alexa from repeating message to the Sender"
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
             if (parent.debug) log.debug "'${disableTts}"
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
        //	if (synthDevice) input "synthVolume", "number", title: "Speaker Volume", description: "0-100%", required: false
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
	if (parent.debug) log.debug "Installed with settings: ${settings}"
	initialize()
}

def updated() {
	if (parent.debug) log.debug "Updated with settings: ${settings}"
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
        			    if (parent.debug) log.debug "Sending message to Synthesis Devices"  
                if (mediaDevice) mediaDevice?.speak(tts)
                		if (parent.debug) log.debug "Sending message to Media Devices"  
            	if (tts) {
					state.sound = textToSpeech(tts instanceof List ? tts[0] : tts)
				}
				else {
					state.sound = textToSpeech("You selected the custom message option but did not enter a message in the $app.label Smart App")
				}
				if (sonosDevice) {
					sonosDevice.playTrackAndResume(state.sound.uri, state.sound.duration, volume)
                    if (parent.debug) log.debug "Sending message to Sonos Devices"  
        		}
    		}
    		sendtxt(txt)
             	if (parent.debug) log.debug "Sending sms and voice message to selected phones and speakers"  
		}
		else {
    		sendtxt(txt)
            	if (parent.debug) log.debug "Only sending sms because disable voice message is ON"  
		}
	switches.on()
    def delay = minutesLater * 60
    def delaySeconds = secondsLater
	if (parent.debug) log.debug "Turning off in ${minutesLater} minutes (${delaySeconds}seconds)"
	if (delay >0) runIn(delay, turnOffSwitch)
    if (delaySeconds >0) runIn(secondsLater, turnOffSwitch)
    }
}
def turnOffSwitch() {
	switches.off()
}
//CoRE Handler-----------------------------------------------------------
/*def CoREResults(sDelay){	
	String result = ""
    def delay
    if (cDelay>0 || sDelay>0) delay = sDelay==0 ? cDelay as int : sDelay as int
	result = (!delay || delay == 0) ? "I am triggering the CORE macro named '${app.label}'. " : delay==1 ? "I'll trigger the '${app.label}' CORE macro in ${delay} minute. " : "I'll trigger the '${app.label}' CORE macro in ${delay} minutes. "
		if (sDelay == 9999) { 
		result = "I am cancelling all scheduled executions of the CORE macro, '${app.label}'. "  
		state.scheduled = false
		unschedule() 
	}
	if (!state.scheduled) {
		if (!delay || delay == 0) CoREHandler() 
		else if (delay < 9999) { runIn(delay*60, CoREHandler, [overwrite: true]) ; state.scheduled=true}
		if (delay < 9999) result = voicePost && !noAck ? replaceVoiceVar(voicePost, delay) : noAck ? " " : result
	}
	else result = "The CORE macro, '${app.label}', is already scheduled to run. You must cancel the execution or wait until it runs before you can run it again. %1%"
	return result
}
def CoREHandler(){ 
	state.scheduled = false
    def data = [pistonName: CoREName, args: "I am activating the CoRE Macro: '${app.label}'."]
    sendLocationEvent (name: "CoRE", value: "execute", data: data, isStateChange: true, descriptionText: "Ask Alexa triggered '${CoREName}' piston.")
	if (noteFeedAct && noteFeed) sendNotificationEvent("Ask Alexa activated CoRE macro: '${app.label}'.")
}*/
/***********************************************************************************************************************
    RESTRICTIONS HANDLER
***********************************************************************************************************************/
private getModeOk() {
    def result = !modes || modes?.contains(location.mode)
	result
} 
private getDayOk() {
    def df = new java.text.SimpleDateFormat("EEEE")
		def timeZone = location.timeZone
        location.timeZone ? df.setTimeZone(location.timeZone) : df.setTimeZone(TimeZone.getTimeZone("America/New_York"))
		def day = df.format(new Date())
	    def result = !runDay || runDay?.contains(day)
        def mode = location.mode
        if (parent.debug) log.trace "modeOk = $result; Location Mode is: $mode"
        if (parent.debug) log.trace "getDayOk = $result. Location time zone is: $timeZone"
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
	if (parent.debug) log.trace "getTimeOk = $result."
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
/***********************************************************************************************************************
    SMS HANDLER
***********************************************************************************************************************/
private void sendText(number, message) {
    if (sms) {
        def phones = sms.split("\\;")
        if (parent.debug) log.trace "SMS phones = $phones."
        for (phone in phones) {
            sendSms(phone, message)
        }
    }
}
private void sendtxt(message) {
    if (sendContactText) { 
    if (location.contactBookEnabled) {
        sendNotificationToContacts(message, recipients)
    	if (parent.debug) log.trace "Sending text to recipients(s) $recipients."
    } else {
    	if (push) {
            sendPush message
        	if (parent.debug) log.trace "Sending push $message."
        } else {
            sendNotificationEvent(message)
        	if (parent.debug) log.trace "Sending notification event: $message."
        }
        if (sms) {
            sendText(sms, message)	
            }		
        }
	}
}

