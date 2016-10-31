/**
 *  Echosistant Profile - PreRelease Alpha Testing
 *		
 *		10/26/2016	version 0.1.2b		Addition of SMS by @SBDOBRESCU.
 *		10/26/2016  version 0.1.2a      Name change
 *		10/26/2016	version 0.1.2		Added Icons by @SBDOBRESCU.
 *		10/25/2016	version 0.1.1		Bug Fix in the Token Renew process
 *  	10/25/2016  version 0.1.0		Alpha Testing of code complete.
 *		10/23/2016	version 0.0.2		Restrictions operational (modes/days/hours), Sonos operational.		
 *		10/23/2016	version 0.0.1i		Day restriction added, code clean up, UI changes
 *		10/20/2016	version 0.0.1h		Mode restriction working
 *  	10/18/2016	version 0.0.1g		Access Token Reset fixed. Multiple UI Changes.  **VERIFY YOUR PROFILES AFTER UPDATE**
 *  	10/17/2016	version 0.0.1f		bug fixes. Sonos, media player, speech synthesizer working.  **SONOS STILLS NEEDS TESTING**
 *		10/17/2016 	version 0.0.1e 		bug fixes.
 *  	10/15-2016	version 0.0.1d		Added custom "Pre-messages", UI changes
 * 		10/14/2016 	version 0.0.1c		Added Sonos support and OAuth tokens to logs for copy and paste
 *		10/11/2016	version 0.0.1b		Fixed audio output for both media and synth
 *		10/10/2016 	Version 0.0.1a		Added media player support
 *		10/09/2016	Version 0.0.1		Initial File
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
    name: "echosistantProfile",
    namespace: "Echo",
    author: "JH",
    description: "Echosistant Profile DO NO intall this app directly.",
    category: "My apps",
    parent: "Echo:Echosistant",    
	iconUrl		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant.png",
	iconX2Url	: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png",
	iconX3Url	: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png")

preferences {
    page name:"mainPage"
    page name:"pageConfiguration"
    	page name: "pageSpeech"
        	page name: "pageAudioDevices"
    		page name: "pageSonos"
    	page name: "pageTextMessage"
    	page name: "certainTime"
        page name: "pageRestrictions"
   	page name: "pageAbout"	
}
//************************************************************************************************************
//Show main page
def mainPage() {
    dynamicPage(name: "mainPage", title:"", install: true, uninstall: false) {
    	section(""){
        	label title:"Name Profile", required:true, defaultValue: "New Profile"    		
        }
    	
        section("") {
			href "pageAudioDevices", title: "Media Devices", description: "Tap here to choose your playback devices", 
            	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Media.png"
 			href "pageTextMessage", title: "Text Notification", description: "Tap here to set up text notifications messages", 
            	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Text.png"
 			href "pageConfiguration", title: "Speech Notifications", description: "Tap here to set up tts message (Pre-messages and restrictions)",
  			 	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Config.png"
    		href "pageAbout", title: "About ${textAppName()}", description: "Tap to get details about Amazon Skill (invocation name), to remove the app and more...",
            	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_About.png"
        }
	}
}

def pageAudioDevices(){
    dynamicPage(name: "pageAudioDevices", title: "Media Devices", uninstall: false){
     	section("Media Speakers (Sonos, wi-fi, music players...)", hideWhenEmpty: true){
        	href "pageSonos", title: "Choose Media Speakers", description: none
            }
        section("Speech Synthesizer Devices (LanDroid, etc...)", hideWhenEmpty: true){
        	input "synthDevice", "capability.speechSynthesis", title: "Choose Speech Synthesis Devices", multiple: true, required: false, submitOnChange: true
        	if (synthDevice) input "synthVolume", "number", title: "Speaker Volume", description: "0-100%", required: false
         }
	}
}
def pageConfiguration(){
	dynamicPage(name: "pageConfiguration", uninstall: false) {
    	section (""){ 
        href "pageSpeech", title: "Speech Notifications", description: "Tap here configure Speech Notifications", 
            	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Media.png"
		href "pageRestrictions", title: "Restrictions", description: "Tap here to configure Restrictions", 
            	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_About.png"
		}
    }
}  
def pageSpeech(){
    dynamicPage(name: "pageSpeech", title: "Configure Speech Notifications", uninstall: false){
		section (""){ 
    	input "ShowPreMsg", "bool", title: "Enable TTS Pre-Message", default: false, submitOnChange: true
        	if (ShowPreMsg) input "PreMsg", "text", title: "Pre-Message Notification", required: true, defaultValue: "Attention.  Attention Please "
            }
        section ( "" ){
        input "feedBack", "bool", title: "Enable Custom Alexa Response", default: false, submitOnChange: true
        	if (feedBack) input "outputTxt", "text", title: "Alexa Response", defaultValue: "Message Sent", required: false
      	    } 
        section("Audio Playback Devices", hideWhenEmpty: true){
        	href "pageAudioDevices", title: "Choose Playback Devices", description: none
            image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Media.png"
		}
    }
}
def pageTextMessage(){
	dynamicPage(name: "pageTextMessage", uninstall: false) {
    	section (""){ 
    	input "sendContactText", "bool", title: "Enable Text Notifications to Contact Book (if available)", required: true, submitOnChange: true
        	if (sendContactText) input "recipients", "contact", title: "Send text notifications to (optional)", multiple: true, required: false
         }       	
         section ( "" ){            
         input "sendText", "bool", title: "Enable Text Notifications to non-contact book phone(s)", required: true, submitOnChange: true           
             if (sendText){      
                    paragraph "You may enter multiple phone numbers separated by semicolon to deliver the Alexa message as a text and a push notification. E.g. 8045551122;8046663344"
                	input name: "sms", title: "Send text notification to (optional):", type: "phone", required: false
					input "push", "bool", title: "Send Push Notification (optional)", required: false, defaultValue: true
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
def pageRestrictions(){
    dynamicPage(name: "pageRestrictions", title: "Configure Restrictions", uninstall: false){
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

def pageSonos(){
    dynamicPage(name: "pageSonos", uninstall: false){
    	section{ paragraph "Configure Media Speakers (Sonos Compatible" }
         section (" "){
        	input "sonosDevice", "capability.musicPlayer", title: "On this Media Speaker", required: false, multiple: true, submitOnChange: true
		    input "volume", "number", title: "Temporarily change volume", description: "0-100%", required: false
            input "resumePlaying", "bool", title: "Resume currently playing music after notification", required: false, defaultValue: false
		}  
	}
}
def pageAbout(){
	dynamicPage(name: "pageAbout", uninstall: true) {
        section {
        	paragraph "${textAppName()}\n${textVersion()}",
            image: "https://raw.githubusercontent.com/BamaRayne/alexaspeaks.src/master/app-Echosistant.png"
 		}
    	section("Instructions") { 
        	paragraph ("For detailed installation and how-to instruction, follow the link below") 
        }
        section("Setup Data for Alexa Skills Kit"){
        	paragraph "Add here code to insert in the Interaction Model "+
            "Copy and paste the displayed content into your new Alexa Skills Kit."+
            "Intent Schema Code:"+
            "Sample Utterances Code"
		}
        section("Tap below to remove the ${textAppName()} application"){}
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

//*** TEXT TO SPEECH PROCESSING ***
def processTts() {
        def tts = params.ttstext 
        def txt = params.ttstext
		def intent  = params.Param
        tts = PreMsg + tts

	log.debug "Command = $params, intent = '${intent}' , tts = '${tts}'"

if (!disableTts){
	if (getDayOk()==true && getModeOk()==true && getTimeOk()==true) {
            if (synthDevice) synthDevice?.speak(tts)
        	if (mediaDevice) mediaDevice?.speak(tts)
    		
            if (tts) {
			state.sound = textToSpeech(tts instanceof List ? tts[0] : tts) // not sure why this is (sometimes) needed)
			}
			else {
			state.sound = textToSpeech("You selected the custom message option but did not enter a message in the $app.label Smart App")
			}
			if (sonosDevice) {
			sonosDevice.playTrackAndResume(state.sound.uri, state.sound.duration, volume)
			log.trace "${state.sound}"
        	}
    }
    //log.debug "sending sms ${txt} after sound"
    sendtxt(txt)
    return ["outputTxt":outputTxt]
}
else {
	//log.debug "sending sms ${txt}"
    sendtxt(txt)
	}
}   
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
        def phones = sms.split("\\+")
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
//}
//************************************************************************************************************
//Version/Copyright/Information/Help
private def textAppName() {
	def text = "This Profile"
}	
private def textVersion() {
    def text = "Version 0.1.0 	(10/25/2016)"
}
