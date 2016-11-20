/**
 *  Echosistant Profile 
 *
 *		11/20/2016		Version 1.2.0a	Added delays (Bobby)
 *		11/19/2016		Version 1.2.0	Bug Fixes: SMS & Push not working, calling multiple profiles at initialize. Additions: Run Routines and Switch enhancements
 *		11/18/2016		Version 1.2.0	Added Triggering Routines, fixed SMS not sending.
 *		11/12/2016		version 1.1.0	OAuth bug fix, additional debug actions, Alexa feedback options, Intent and Utterance file updates
 *										Control Switches on/off with delay off, pre-message "null" bug
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
        		page name: "mOptions"
    			page name: "audioDevices"
    				page name: "sonos"
            page name: "pOptions"        
            	page name: "devices"
            	page name: "routines"
        	page name: "restrictions"
    			page name: "certainTime"
            page name: "CoRE"
}
/***********************************************************************************************************************
    UI CONFIGURATION
***********************************************************************************************************************/
def mainPage() {	       
    dynamicPage(name: "mainPage", title:"", install: true, uninstall: true) {
        section("") {
    		href "audioDevices", title: "Audio Playback Devices...", description: "Tap here to configure", 
            	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Media.png"
            href "mOptions", title: "Message Options...", description: "Tap here to configure", 
            	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_msg.png"
            href "pOptions", title: "Extra Control Settings...", description: "Tap here to configure",
            	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Rest.png"
            href "restrictions", title: "Restrictions", description: "Tap here to configure", 
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
def CoRE() {
	dynamicPage(name: "CoRE", install: false, uninstall: false) {
		section { paragraph "CoRE Trigger Settings" }
		section (" "){
   			input "CoREName", "enum", title: "Choose CoRE Piston", options: parent.state.CoREPistons, required: false, multiple: false
        	input "cDelay", "number", title: "Default Delay (Minutes) To Trigger", defaultValue: 0, required: false
        }
        if (!parent.state.CoREPistons){
        	section("Missing CoRE pistons"){
				paragraph "It looks like you don't have the CoRE SmartApp installed, or you haven't created any pistons yet. To use this capability, please install CoRE or, if already installed, create some pistons, then try again."
            }
        }	
    }
}
page name: "pOptions"
	def pOptions(){
		dynamicPage(name: "pOptions", uninstall: false) {
            section (""){
				href "routines", title: "Execute Routines...", description: "Tap here to configure",
            	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Rest.png"
				}    
			section {
				href "devices", title: "Control Devices...", description: "Tap here to configure...",
       		    image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Rest.png"
				}
            section {
                 href "CoRE", title: "CoRE Integration", description: "Tap here to configure CoRE options...",
            	image: "https://cdn.rawgit.com/ady624/CoRE/master/resources/images/app-CoRE.png"
                }
	}
}				
def routines(){
    dynamicPage(name: "routines", title: "Select Hello Home Action(s) (Routines) to Execute", install: false, uninstall: false) {
        // get the available actions
            def actions = location.helloHome?.getPhrases()*.label
            if (actions) {
            // sort them alphabetically
            actions.sort()
                    section("Hello Home Actions") {
                            if (parent.debug) log.info actions
                // use the actions as the options for an enum input
                input "runRoutine", "enum", title: "Select a Routine(s) to execute", required: false, options: actions, multiple: true
			}
		}
    }
}
def devices(){
    dynamicPage(name: "devices", install: false, uninstall: false) {
        section {} 
        section("Choose the switches to turn on with this profile...", hideWhenEmpty: true) {
			input "switches", "capability.switch", title: "Control These Switches...", multiple: true, required: false, submitOnChange:true
// add later            if (switches) input "switchesCMD", "enum", title: "Command To Send To Switches", options:["on":"Turn on","off":"Turn off", "toggle":"Toggle the switches' on/off state"], multiple: false, required: false
        }
         section("Turn on these switches after a delay of..."){
         	input "sSecondsOn", "number", title: "Seconds?", defaultValue: none, required: false
         }
		 section("And then turn them off after a delay of..."){
			input "sSecondsOff", "number", title: "Seconds?", defaultValue: none, required: false
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
def mOptions(){
    dynamicPage(name: "mOptions", title: "Configure Audio and Text Messages...", uninstall: false){
		section (""){ 
    	input "ShowPreMsg", "bool", title: "Pre-Message (plays before message)", defaultValue: true, submitOnChange: true
        	if (ShowPreMsg) input "PreMsg", "Text", title: "Pre-Message", description: "Pre-Message to play before your message", required: false, defaultValue: "Attention, Attention please..  ", submitOnChange: true
            }
        section ( "" ){
        input "Acustom", "bool", title: "Custom Alexa Responses to sender", defaultValue: false, submitOnChange: true
        	if (Acustom) input "outputTxt", "text", title: "Custom Alexa Responses to sender", defaultValue: none, required: false, submitOnChange: true
        }
        section ( "" ){
        input "Arepeat", "bool", title: "Alexa repeats message back to sender", defaultValue: false, submitOnChange: true
        }
    	section ( "" ){
        input "AfeedBack", "bool", title: "Alexa Feedback Responses (disable to silence Alexa)", defaultValue: true, submitOnChange: true
        } 
        section ( "" ){
        input "disableTts", "bool", title: "Disable All spoken notifications (Use for sending texts or when controlling only devices and a verbal response is not wanted.)", required: false, submitOnChange: true  
             if (parent.debug) log.debug "'${disableTts}"
        }
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
            }
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
    		input "disableTts", "bool", title: "Disable spoken notification (only send text message to selected contact(s)", required: false, submitOnChange: true  
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
	subscribe(theSwitch, "switch.on", switchOnHandler)
}
def updated() {
	if (parent.debug) log.debug "Updated with settings: ${settings}"
	initialize()
	unsubscribe()
}
def initialize() {
	state.lastMessage = null
	state.lastIntent  = null
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
        def intent = params.pintentName
        def childName = app.label
        if (intent == childName){
        	location.helloHome?.execute(runRoutine)
         	if (sSecondsOn) {
             	if (parent.debug) log.debug "Scheduling switches to turn on in '${sSecondsOn}' seconds"
            	runIn(sSecondsOn, turnOnSwitch)
			}	
        	else {
        		if (parent.debug) log.debug "Turning switches on"
                switches?.on()
        	}
          	if (sSecondsOff) {
             	if (parent.debug) log.debug "Scheduling switches to turn off in '${sSecondsOff}' seconds"
          		runIn(sSecondsOff, turnOffSwitch)
			}	
        	else {
        		if (parent.debug) log.debug "Turning switches off"
                switches?.off()
        }
            if (!disableTts){
        			if (PreMsg) 
        				tts = PreMsg + tts
        				if (parent.debug) log.debug "#3 tts = '${tts}'"
         			else {
            			tts = tts
            			if (parent.debug) log.debug "#4 tts = '${tts}'"
            		}
            			if (getDayOk()==true && getModeOk()==true && getTimeOk()==true) {
            					if (synthDevice) synthDevice?.speak(tts)
        			    			if (parent.debug) log.debug "#5 Sending message to Synthesis Devices"  
                				if (mediaDevice) mediaDevice?.speak(tts)
                					if (parent.debug) log.debug "#6 Sending message to Media Devices"  
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
		}
}
def turnOnSwitch() {
	switches?.on()
    dimmers?.on()
}	
def turnOffSwitch() {
	switches?.off()
    dimmers?.off()
}	
//CoRE Handler-----------------------------------------------------------
def CoREResults(sDelay){	
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
}
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
