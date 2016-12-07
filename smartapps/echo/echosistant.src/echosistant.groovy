/*
 * EchoSistant - The Ultimate Voice and Text Messaging Assistant Using Your Alexa Enabled Device.
 *		
 *		??/??/2016		Version 3.0.0	Additions: Msg to Notify Tab in Mobile App, Push Msg, Complete Reconfigure of Profile Build, More Control of Dimmers, and Switches,
 *										Control of Thermostats, Doors, and Locks. Common speech device/room commands. Status Feedback. Activity Alerts Page
 *		11/23/2016		Version 2.0.1	Bug fix: Pre-message not showing correctly.  Set to default false.
 *		11/22/2016		Version 2.0.0	CoRE integration, Cont Commands per profile, Repeat Message per profile, one app and many bug fixes.
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
 * Alarms and Timers with Voice Feedback
 * External TTS
 * External SMS
 * Google Calendar
 * Personal Message Que per User
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
	name			: "EchoSistant${parent ? " - Profile" : ""}",
    namespace		: "Echo",
    author			: "JH",
	description		: "The Ultimate Voice and Text Messaging Assistant Using Your Alexa Enabled Device.",
	singleInstance	: true,
    parent: parent ? "Echo.EchoSistant" : null,
    category		: "My Apps",
	iconUrl			: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant.png",
	iconX2Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png",
	iconX3Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png")
/**********************************************************************************************************************************************/

/************************************************************************************************************
   PARENT PAGES
************************************************************************************************************/
preferences {
    page name: "pageMain"
    //Parent Pages    
    page name: "mainParentPage"
    page name: "about"
    page name: "Tokens"
    page name: "pageConfirmation"
    page name: "pageReset"
    //Profile Pages    
    page name: "mainProfilePage"
    page name: "Alert"
    page name: "Profiles"
	page name: "configuration"
    		page name: "notifications"
    			page name: "audioDevices"
    				page name: "sonos"
            	page name: "devices"
        	page name: "restrictions"
    			page name: "certainTime"
            page name: "CoRE"
}

def pageMain() { if (!parent) mainParentPage() else mainProfilePage() }

/***********************************************************************************************************************
    PARENT UI CONFIGURATION
***********************************************************************************************************************/
def mainParentPage() {	
	dynamicPage(name: "mainParentPage", title: "EchoSistant", install: true, uninstall: false) {
        section {
        	href "Profiles", title: "Profiles", description: "Tap here to view and create your profiles....",
            image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Config.png"
			}    
		section {
			href "about", title: "Settings and Security", description: "Tap here for App information...Tokens, Version, License...",
            image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_About.png"
			}
        section ("EchoSistant Smartapp Information") {
        	def Profiles = childApps.size()     
            href(name: "", title: "*  ${textAppName()}\n*  ${textVersion()}\n*  ${Profiles} Profiles Created"+
            					  "\n*  ${textCopyright()}\n*  Click Here to visit EchoSistant Wiki",
            description: "" ,
            required: false,
            image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant.png",
            url: "http://thingsthataresmart.wiki/index.php?title=EchoSistant")
            }      
        section ("Rename Main Intent") { 
			input "mainIntent", "text", title: "Main Intent", defaultValue: "assistant", required: false
        }
	}
}
def Profiles() {
        dynamicPage (name: "Profiles", title: "", install: false, uninstall: false) {
        if (childApps.size()) { 
            	section(childApps.size()==1 ? "One Profile configured" : childApps.size() + " Profiles configured" )
        section("Create New Profiles"){
        		app(appName: "EchoSistant", namespace: "Echo", multiple: true, description: "Tap Here to Create a New Profile...")
        	} 
		}
    } 
}
def about(){
	dynamicPage(name: "about", uninstall: true) {
         	section ("Directions, How-to's, and Troubleshooting") { 
			href url:"http://thingsthataresmart.wiki/index.php?title=EchoSistant", title: "EchoSistant Wiki", description: none
        	}
			section("Debugging") {
            	input "debug", "bool", title: "Enable Debug Logging", default: false, submitOnChange: true 
            	if (debug) log.info "${textAppName()}\n${textVersion()}"
            	}
			section ("Apache License"){
				input "ShowLicense", "bool", title: "Show License", default: false, submitOnChange: true
				def msg = textLicense()
					if (ShowLicense) paragraph "${msg}"
				}
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
            section("Tap below to remove the ${textAppName()} application.  This will remove ALL Profiles and the App from the SmartThings mobile App."){}
			}
		}      

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

def pageConfirmation(){
		dynamicPage(name: "pageConfirmation", title: "Reset/Renew Access Token Confirmation", uninstall: false){
			section {
				href "pageReset", title: "Reset/Renew Access Token", description: "Tap here to confirm action - READ WARNING BELOW"
				paragraph "PLEASE CONFIRM! By resetting the access token you will disable the ability to interface this SmartApp with your Amazon Echo."+
            	"You will need to copy the new access token to your Amazon Lambda code to re-enable access." +
				"Tap below to go back to the main menu with out resetting the token. You may also tap Done above."
				}
			section(" "){
        		href "mainParentPage", title: "Cancel And Go Back To Main Menu", description: none 
       			}
			}
		}

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
        		href "mainParentPage", title: "Tap Here To Go Back To Main Menu", description: none 
        		}
			}
		} 	
/***********************************************************************************************************************
    PROFILE UI CONFIGURATION
***********************************************************************************************************************/
def mainProfilePage() {	
    dynamicPage(name: "mainProfilePage", title:"", install: true, uninstall: true) {
        section  ("I want this Profile to....") {
            if (!RouPro && !StaPro && !DevPro) {
            input "MsgPro", "bool", title: "Send these message types...", defaultValue: false, submitOnChange: true,
            image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_msg.png" 
            	if (MsgPro) {
                	input "audioMsg", "bool", title: "Audio Messages..", defaultValue: false, submitOnChange: true,
        			image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Media.png" 
                    if (audioMsg) {
                    	href "audioDevices", title: "Using these speakers..." 
                        }
	                input "SMS", "bool", title: "Text & Push Messages...", dafaultValue: false, submitOnChange: true,
                    image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Text.png" 
            			if (SMS) {
            				input "sendContactText", "bool", title: "Enable Text Notifications to Contact Book (if available)", required: true, submitOnChange: true
        						if (sendContactText) input "recipients", "contact", title: "Send text notifications to (optional)", multiple: true, required: false
							input "sendText", "bool", title: "Enable Text Notifications to non-contact book phone(s)", required: true, submitOnChange: true           
        						if (sendText){      
           						paragraph "You may enter multiple phone numbers separated by semicolon to deliver the Alexa message as a text and a push notification. E.g. 8045551122;8046663344"
            				input name: "sms", title: "Send text notification to (optional):", type: "phone", required: false
							}
                    		input "push", "bool", title: "Send Push Notification (optional)", required: false, defaultValue: false
                     		if (parent.debug) log.debug "disable TTS='${disableTts}" 
                    			}
               		input "notify", "bool", title: "Send message to Mobile App Notifications Tab (optional)", required: false, defaultValue: false, submitOnChange: true,
                    image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Text.png"
                    	}
					}                            
			if (!MsgPro && !RouPro && !StaPro) {
            input "DevPro", "bool", title: "And/Or control these devices in my home...", defaultValue: false, submitOnChange: true,
            image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_devices.png"
				if (DevPro) {
					href "devices", title: "Control Devices...", description: "Tap here to configure",
       		    	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_devices.png"
                	href "CoRE", title: "Integrate CoRe...",
                    image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_CoRE.png"
                }
            }
            if (!MsgPro && !StaPro && !DevPro) {
            input "RouPro", "bool", title: "And/Or execute these routines...", defaultValue: false, submitOnChange: true,
            image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Routines.png"
                if (RouPro) {
                	def actions = location.helloHome?.getPhrases()*.label
            		if (actions) {
            			actions.sort()
                    if (parent.debug) log.info actions
                		input "runRoutine", "enum", title: "Select a Routine(s) to execute", required: false, options: actions, multiple: true,
						image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Routines.png"
							}
						}
					}
            if (!MsgPro && !RouPro && !DevPro) {
            input "StaPro", "bool", title: "And/Or give me status feedback about my home as well as Event Alerts...", defaultValue: false, submitOnChange: true,
            image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/echosistant_About.png"
            	if (StaPro) {
                href "Alert", title: "Create an Event Alert...", description: "Tap here to configure"
               
                }
			}
		}
		section ("Global Profile Options") {
            		input "MsgOpt", "bool", title: "I want to use these configuration options...", defaultValue: false, submitOnChange: true,
            		image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Media.png"
            	if (MsgOpt) {
            		input "ShowPreMsg", "bool", title: "Pre-Message (plays on Audio Playback Device before message)", defaultValue: false, submitOnChange: true
        				if (ShowPreMsg) input "PreMsg", "Text", title: "Pre-Message...", defaultValue: none, submitOnChange: true, required: false 
        			input "Acustom", "bool", title: "Custom Response from Alexa...", defaultValue: false, submitOnChange: true
        				if (Acustom) input "outputTxt", "text", title: "Input custom phrase...", required: false, defaultValue: "Message sent,   ", submitOnChange: true
       	 			input "Arepeat", "bool", title: "Alexa repeats message to sender when sent...", defaultValue: false, submitOnChange: true
            			if (Arepeat) {			
	          			if (Arepeat && Acustom){
          					paragraph "NOTE: only one custom Alexa response can"+
            		  		" be delivered at once. Please only enable Custom Response OR Repeat Message"
							}
                		}
                        
                	input "AfeedBack", "bool", title: "Turn on to disable Alexa Feedback Responses (silence Alexa) Overrides all other Alexa Options...", defaultValue: false, submitOnChange: true
        	 			if (AfeedBack) {
                		if (parent.debug) log.debug "Afeedback = '${AfeedBack}"
                	}
                    input "disableTts", "bool", title: "Disable All spoken notifications (No voice output from the speakers or Alexa)", required: false, submitOnChange: true  
                	input "ContCmds", "bool", title: "Allow Alexa to prompt for additional commands after message is delivered...", defaultValue: false, submitOnChange: true
       				input "ContCmdsR", "bool", title: "Allow Alexa to prompt for additional commands after Repeat command is given...", defaultValue: false, submitOnChange: true
         			}
                   
           		 	href "restrictions", title: "I Want These Restrictions For This Message Profile", description: "Tap here to configure",
            		image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Extra.png"
					}
            	
            
            
        section ("Name and/or Remove this Profile") {
 		   	label title:"              Rename Profile ", required:false, defaultValue: "New Profile"  
            }
		}       
	}
page name: "Alert"
def Alert(){
	dynamicPage(name: "Alert", uninstall: false) {
   	section ("Switches and Dimmers") {
    input "ShowSwitches", "bool", title: "Switches and Dimmers", default: false, submitOnChange: true
	if (ShowSwitches) {        
    		input "TheSwitch", "capability.switch", title: "Choose Switches...", required: false, multiple: true, submitOnChange: true
    		input "audioTextOn", "textOn", title: "Play this message", description: "Message to play when the switch turns on", required: false, capitalization: "sentences"
    		input "audioTextOff", "textOff", title: "Play this message", description: "Message to play when the switch turns off", required: false, capitalization: "sentences"
    		input "speech1", "capability.speechSynthesis", title: "Message Player", required: false, multiple: true
  			}
    	}
	section("Doors and Windows") {
    input "ShowContacts", "bool", title: "Doors and Windows", default: false, submitOnChange: true
	if (ShowContacts) {
    		input "TheContact", "capability.contactSensor", title: "Choose Doors and Windows..", required: false, multiple: true, submitOnChange: true
    		input "audioTextOpen", "textOpen", title: "Play this message", description: "Message to play when the door opens", required: false, capitalization: "sentences"
    		input "audioTextClosed", "textClosed", title: "Play this message", description: "Message to play when the door closes", required: false, capitalization: "sentences"
    		input "speech2", "capability.speechSynthesis", title: "Message Player", required: false, multiple: true
  			}
    	}
	section("Locks") {
    input "ShowLocks", "bool", title: "Locks", default: false, submitOnChange: true
	if (ShowLocks) {
    		input "TheLock", "capability.lock", title: "Choose Locks...", required: false, multiple: true
    		input "audioTextLocked", "textLocked", title: "Play this message", description: "Message to play when the lock locks", required: false, capitalization: "sentences"
    		input "audioTextUnlocked", "textUnlocked", title: "Play this message", description: "Message to play when the lock unlocks", required: false, capitalization: "sentences"
    		input "speech3", "capability.speechSynthesis", title: "Message Player", required: false, multiple: true
  			}
    	}
	section("Motion Sensors") {
    input "ShowMotion", "bool", title: "Motion Sensors", default: false, submitOnChange: true
	if (ShowMotion) {
	    	input "TheMotion", "capability.motionSensor", title: "Choose Motion Sensors...", required: false, multiple: true
	    	input "audioTextActive", "textActive", title: "Play this message", description: "Message to play when motion is detected", required: false, capitalization: "sentences"
	    	input "audioTextInactive", "textInactive", title: "Play this message", description: "Message to play when motion stops", required: false, capitalization: "sentences"
	    	input "speech4", "capability.speechSynthesis", title: "Message Player", required: false, multiple: true
	  		}
	    }
  	section("Presence Sensors") {
    input "ShowPresence", "bool", title: "Presence Sensors", default: false, submitOnChange: true
	if (ShowPresence) {
	    	input "ThePresence", "capability.presenceSensor", title: "Choose Presence Sensors...", required: false, multiple: true
	    	input "audioTextPresent", "textPresent", title: "Play this message", description: "Message to play when the Sensor arrives", required: false, capitalization: "sentences"
	    	input "audioTextNotPresent", "textNotPresent", title: "Play this message", description: "Message to play when the Sensor Departs", required: false, capitalization: "sentences"
	    	input "speech5", "capability.speechSynthesis", title: "Message Player", required: false, multiple: true
	  		}
	  	}
    section("Water Sensors") {
    input "ShowWater", "bool", title: "Water Detectors", default: false, submitOnChange: true
	if (ShowWater) {
	    	input "TheWater", "capability.waterSensor", title: "Choose Water Sensors...", required: false, multiple: true
	    	input "audioTextWet", "textWet", title: "Play this message", description: "Message to play when water is detected", required: false, capitalization: "sentences"
	    	input "audioTextDry", "textDry", title: "Play this message", description: "Message to play when is no longer detected", required: false, capitalization: "sentences"
	    	input "speech6", "capability.speechSynthesis", title: "Message Player", required: false, multiple: true
	  		}
		}
    section("Garage Doors") {
    input "ShowGarage", "bool", title: "Garage Doors", default: false, submitOnChange: true
	if (ShowGarage) {
	    	input "TheGarage", "capability.garageDoorControl", title: "Choose Garage Doors...", required: false, multiple: true
	    	input "audioTextOpening", "textOpening", title: "Play this message", description: "Message to play when the Garage Door Opens", required: false, capitalization: "sentences"
	        input "audioTextClosing", "textClosing", title: "Play this message", description: "Message to play when the Garage Door Closes", required: false, capitalization: "sentences" 
			input "speech7", "capability.speechSynthesis", title: "Message Player", required: false, multiple: true
			}
		}
    }
}
def switchOnHandler(evt) {
    log.debug "switchOnHandler called: $evt"
    theswitch.on()
}
def switchOffHandler(evt) {
    log.debug "switchOffHandler called: $evt"
}
def audioEvent(evt) {
  ("on" == evt.value) 
  audioEvent()
  } 
    
page name: "CoRE"
def CoRE(){
		dynamicPage(name: "CoRE", uninstall: false) {
            section ("Welcome to the CoRE integration page"){
				paragraph ("--- This integration is in place to enhance the"+
                " communication abilities of EchoSistant and your SmartThings"+
                " Home Automation Project. It is not intended"+
                " for the control of HA devices."+
                " --- CoRE integration is currently one way only. You can NOT "+
                " trigger profiles from within CoRE. CoRE listens for a "+
                " profile execution and then performs the programmed tasks."+
				" --- Configuration is simple. In EchoSistant create your profile."+
                " Then open CoRE and create a new piston. In the condition section"+
                " choose 'Echosistant Profile' as the trigger. Choose the appropriate"+
                " profile and then finish configuring the piston."+
                " --- When the profile is executed the CoRE piston will also execute.")
    	}
    }
}    
				
def devices(){
    dynamicPage(name: "devices", title: "Select Devices to use with this profile",install: false, uninstall: false) {
        section ("Switches", hideWhenEmpty: true){
            input "switches", "capability.switch", title: "Control These Switches...", multiple: true, required: false, submitOnChange: true
            if (switches) input "switchCmd", "enum", title: "What do you want to do with these switches?", options:["on":"Turn on","off":"Turn off"], multiple: false, required: false, submitOnChange:true
        	if (switchCmd) input "otherSwitch", "capability.switch", title: "...and these other switches?", multiple: true, required: false, submitOnChange: true
            if (otherSwitch) input "otherSwitchCmd", "enum", title: "What do you want to do with these other switches?", options: ["on":"Turn on", "off":"Turn off"], multiple: false, required: false, submitOnChange: true
        	}
        section ("Dimmers", hideWhenEmpty: true){
            input "dimmers", "capability.switchLevel", title: "Control These Dimmers...", multiple: true, required: false , submitOnChange:true
            if (dimmers) input "dimmersCMD", "enum", title: "Command To Send To Dimmers", options:["on":"Turn on","off":"Turn off","set":"Set level"], multiple: false, required: false, submitOnChange:true
            if (dimmersCMD == "set" && dimmers) input "dimmersLVL", "number", title: "Dimmers Level", description: "Set dimmer level", required: false
            if (dimmersLVL) input "otherDimmers", "capability.switchLevel", title: "Control These Dimmers...", multiple: true, required: false , submitOnChange:true
            if (otherDimmers) input "otherDimmersCMD", "enum", title: "Command To Send To Dimmers", options:["on":"Turn on","off":"Turn off","set":"Set level"], multiple: false, required: false, submitOnChange:true
            if (otherDimmersCMD == "set" && otherDimmers) input "otherDimmersLVL", "number", title: "Dimmers Level", description: "Set dimmer level", required: false
        }
        section("Turn on these switches after a delay of..."){
         	input "sSecondsOn", "number", title: "Seconds?", defaultValue: none, required: false
        }
		section("And then turn them off after a delay of..."){
			input "sSecondsOff", "number", title: "Seconds?", defaultValue: none, required: false
		}
        section ("Thermostats", hideWhenEmpty: true){
            input "tstat", "capability.thermostat", title: "Control These Thermostat(s)...", multiple: true, required: false
        }
        section ("Locks", hideWhenEmpty: true){
            input "lock", "capability.lock", title: "Control These Lock(s)...", multiple: true, required: false
        }
        section ("Doors", hideWhenEmpty: true){
     		input "doors", "capability.doorControl", title: "Control These Door(s)...)" , multiple: true, required: false, submitOnChange: true    
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
/*************************************************************************************************************
   CREATE INITIAL TOKEN
************************************************************************************************************/
def OAuthToken(){
	try {
		createAccessToken()
		log.debug "Creating new Access Token"
	} catch (e) {
		log.error "Access Token not defined. OAuth may not be enabled. Go to the SmartApp IDE settings to enable OAuth."
	}
}

//************************************************************************************************************
mappings {
      path("/t") {action: [GET: "processTts"]}
      }i
//************************************************************************************************************

def installed() {
	if (debug) log.debug "Installed with settings: ${settings}"
    if (debug) log.trace "STappID = '${app.id}' , STtoken = '${state.accessToken}'"
    alertHandler(evt)
	initialize()
    
}
def updated() { 
	if (debug) log.debug "Updated with settings: ${settings}"
    initialize() 
}

def getProfileList(){return getChildApps()*.label
		if (debug) log.debug "Refreshing Profiles for CoRE, ${getChildApps()*.label}"
}

def childUninstalled() {
	if (debug) log.debug "Profile has been deleted, refreshing Profiles for CoRE, ${getChildApps()*.label}"
    sendLocationEvent(name: "echoSistant", value: "refresh", data: [profiles: getProfileList()] , isStateChange: true, descriptionText: "echoSistant Profile list refresh")
}

def initialize() {
	alertHandler(evt)
	if (!parent){
    	if (debug) log.debug "Initialize !parent"
        sendLocationEvent(name: "echoSistant", value: "refresh", data: [profiles: getProfileList()] , isStateChange: true, descriptionText: "echoSistant Profile list refresh")
    	state.lastMessage = null
		state.lastIntent  = null
    	state.lastTime  = null
		def children = getChildApps()
    		if (debug) log.debug "$children.size Profiles installed"
			children.each { child ->
			}
			if (!state.accessToken) {
        		if (debug) log.debug "Access token not defined. Attempting to refresh. Ensure OAuth is enabled in the SmartThings IDE."
                OAuthToken()
        		if (debug) log.debug "STappID = '${app.id}' , STtoken = '${state.accessToken}'" 
			}
	}
    else{
        if (debug) log.debug "Initialize else block"
        state.lastMessage = null
    	state.lastTime  = null
     }
}

def subscribeToEvents() {
	if (runModes) {
		subscribe(runMode, location.currentMode, modeChangeHandler)
	}
    if (runDay) {
   		subscribe(runDay, location.day, location.currentDay)
	} 
    if (switches) {
    subscribeDevices()
    }
}
def unsubscribeToEvents() {
	if (triggerModes) {
    	unsubscribe(location, modeChangeHandler)
    }
} 

           
/************************************************************************************************************
   TEXT TO SPEECH PROCESS (PARENT) 
************************************************************************************************************/
def processTts() {
		def ptts = params.ttstext 
        def pttx = params.ttstext
   		def pintentName = params.intentName
        def pCommands = params.pCommands
        def pProfiles = params.pProfiles
            if (debug) log.debug "Message received from Lambda with: (pProfiles) = '${pProfiles}', (pCommands) = '${pCommands}', intentName = '${pintentName}', (ptts) = '${ptts}'"

        def outputTxt = ''
    	def pContCmds = "false"
        def pContCmdsR = "false"
        def dataSet = [ptts:ptts,pttx:pttx,pintentName:pintentName] 
		def controlData = [pCommands:pCommands,pProfiles:pProfiles,pintentName:pintentName] 

        def repeat = "repeat last message"
       	def pMainIntent ="assistant"
        	if (mainIntent){
            		pMainIntent = mainIntent
        	}
   
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
                                pContCmds = child.ContCmds
                                pContCmdsR = child.ContCmdsR
                                if (pContCmdsR == false) {
                                	pContCmds = pContCmdsR
                                }
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
                    if (debug) log.debug "Running main loop with '${ptts}'"                      
                    childApps.each {child ->
						child.profileEvaluate(dataSet)
            			child.profileControl(controlData)
                        }
            			childApps.each { child ->
    						def cm = child.label
            					if (cm == pintentName) {
                              		def cAcustom = child.Acustom
									def cArepeat = child.Arepeat
									def cAfeedBack = child.AfeedBack
                                    pContCmds = child.ContCmds
     	                			if (cAfeedBack != true) {
                                		if (cAcustom != false) {
                            				outputTxt = child.outputTxt
                            			}
                            			else {
                        					if (cArepeat == !false || cArepeat == null ) {
                                            	outputTxt = "I have delivered the following message to '${cm}',  " + ptts
											}
                        					else {
                            					outputTxt = "Message sent to ${pintentName}, " 
												if (debug) log.debug "Alexa verbal response = '${outputTxt}'"
           									}
                                		}
                             		}
           						}  
                  		}
				}
      	}
        if (debug) log.debug "#6 Alexa response sent to Lambda = '${outputTxt}', '${pContCmds}' "
		return ["outputTxt":outputTxt, "pContCmds":pContCmds]
        if (debug) log.debug "#6 Alexa response sent to Lambda = '${outputTxt}', '${pContCmds}' "
}
/******************************************************************************************************
   SPEECH AND TEXT PROCESSING
******************************************************************************************************/
def profileEvaluate(params) {
        def tts = params.ptts
        def txt = params.pttx
        def intent = params.pintentName        
        def childName = app.label       
        def data = [args: tts ]
        if (intent == childName){
        	sendLocationEvent(name: "echoSistantProfile", value: app.label, data: data, displayed: true, isStateChange: true, descriptionText: "EchoSistant activated '${app.label}' profile.")
      		if (parent.debug) log.debug "sendNotificationEvent sent to CoRE was '${app.label}' from the TTS process section"
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
                        state.lastMessage = tts
                        state.lastTime = new Date(now()).format("h:mm aa", location.timeZone)
            	if (parent.debug) log.debug "Sending sms and voice message to selected phones and speakers"  
				}
				else {
    					sendtxt(txt)
                        state.lastMessage = txt
                        state.lastTime = new Date(now()).format("h:mm aa", location.timeZone)
           					if (parent.debug) log.debug "Only sending sms because disable voice message is ON"  
				}
                deviceControl()
        }
}

/******************************************************************************************************
   CONTROL PROCESSING
******************************************************************************************************/
def profileControl(params) {

        def intent = params.pintentName
        def profile = params.pProfiles
        def command = params.pCommands
        def childName = app.label       
        if (parent.debug) log.debug "Message received from Parent with: (profile) = '${profile}', (command) = '${command}', intent = '${intent}', childName = '${childName}' "
        
        if (profile.toLowerCase()  == childName.toLowerCase()){
			if (parent.debug) log.debug "Profile called is '${profile}' with command '${command}'"
 		

        if (command == "dark" || command == "brighter" || command == "on") {
            if (sSecondsOn) {
            	runIn(sSecondsOn,turnOnSwitch)
                runIn(sSecondsOn,turnOnOtherSwitch)
                runIn(sSecondsOn,turnOnDimmers)
                runIn(sSecondsOn,turnOnOtherDimmers)
                }
             else {
       	    	if (parent.debug) log.debug "Turning switches on"
                switches?.on()
				}
          }
		if (command == "bright" || command == "darker" || command == "off") {     	
        	if (sSecondsOff) {
          		runIn(sSecondsOff,turnOffSwitch)
                runIn(sSecondsOff,turnOffOtherSwitch)
                runIn(sSecondsOff,turnOffDimmers)
                runIn(sSecondsOff,turnOffOtherDimmers)
			}	
        	else {
        		if (parent.debug) log.debug "Turning switches off"
                switches?.off()
            }
        }
	}
}

/***********************************************************************************************************************
    LAST MESSAGE HANDLER
***********************************************************************************************************************/
def getLastMessage() {
	def cOutputTxt = "The last message sent to " + app.label + " was," + state.lastMessage + ", and it was sent at, " + state.lastTime
	return  cOutputTxt 
	if (parent.debug) log.debug "Sending last message to parent '${cOutputTxt}' "
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
    if (parent.debug) log.debug message
    if (sendContactText) { 
        sendNotificationToContacts(message, recipients)
    } 
    if (push) {
        sendPush message
    } 
    ////////// Send message to notifications tab in mobile app
	if (notify) {
        sendNotificationEvent(message)
    }
    if (sms) {
        sendText(sms, message)		
    }
}
/************************************************************************************************************
   Switch/Dimmer Handlers
************************************************************************************************************/
def turnOnSwitch() {
		if (intent == childName){
			switches?.on()
    		otherSwitch?.on()
    		dimmers?.on()
    		otherDimmers?.on()
		}   
}

def turnOffSwitch() {
		if (intent == childName){
			switches?.off()
    		otherSwitch?.off()
    		dimmers?.off()
    		otherDimmers?.off()
		}   
} 
private deviceControl() {
        if (intent == childName){
            if (sSecondsOn) {
            	if (parent.debug) log.debug "Turn switches on in '${sSecondsOn}' seconds"
            	runIn(sSecondsOn,turnOnSwitch)
                runIn(sSecondsOn,turnOnOtherSwitch)
                runIn(sSecondsOn,turnOnDimmers)
                runIn(sSecondsOn,turnOnOtherDimmers)
                }
            else {
       	    	if (parent.debug) log.debug "Turning switches on"
//                switches?.on()
				
        	if  (switchCmd == "on") {
            	switches?.on()
                }
           		else if (switchCmd == "off") {
            		 switches?.off()
                	}
          	if (otherSwitchCmd == "on") {
            	otherSwitch?.on()
                }
            	else if (otherSwitchCmd == "off") {
                	otherSwitch?.off()
                	}
          	if (dimmersCMD == "set" && dimmers){
				if (dimmersCMD == "set"){
        			def level = dimmersLVL < 0 || !dimmersLVL ?  0 : dimmersLVL >100 ? 100 : dimmersLVL as int
        			dimmers?.setLevel(level)
            		}
				}                    
            if (otherDimmersCMD == "set" && otherDimmers){
				if (otherDimmersCMD == "set"){
        		def otherlevel = otherDimmersLVL < 0 || !otherDimmersLVL ?  0 : otherDimmersLVL >100 ? 100 : otherDimmersLVL as int
        		otherDimmers?.setLevel(otherlevel)
			}
        }            
	}
}    
            	if (sSecondsOff) {
            	if (parent.debug) log.debug "Turn switches off in '${sSecondsOff}' seconds"            
          		runIn(sSecondsOff,turnOffSwitch)
                runIn(sSecondsOff,turnOffOtherSwitch)
                runIn(sSecondsOff,turnOffDimmers)
                runIn(sSecondsOff,turnOffOtherDimmers)
			}	
        	else {
        		if (parent.debug) log.debug "Turning switches off"
//                switches?.off()
            }
}

/************************************************************************************************************
   Alerts Handler
************************************************************************************************************/
def myHandler(evt) {
    if ("on" == evt.value) {
  	speech1?.speak(audioTextOn)
    }
    	else {
        speech1?.speak(audioTextOff)
        }
    if ("open" == evt.value) {
  	speech2?.speak(audioTextOpen)
    }
    	else {
        speech2?.speak(audioTextClosed)
    }
    if ("locked" == evt.value) {
    speech3?.speak(audioTextLocked)
    }
    	else {
        speech3?.speak(audioTextUnlocked)
        }
    if ("active" == evt.value) {
    speech4?.speak(audioTextActive)
    }
    	else {
        speech4?.speak(audioTextInactive)
        }
    if ("present" == evt.value) {
    speech5?.speak(audioTextPresent)
    }
    	else {
        speech5?.speak(audioTextNotPresent)
        }
    if ("dry" == evt.value) {
    speech6?.speak(audioTextDry)
    }
    	else {
        speech6?.speak(audioTextWet)
        }
    if ("opening" == evt.value) {
    speech7?.speak(audioTextOpening)
    }
    	else {
        speech7?.speak(audioTextClosing)
        }
}

def alertHandler(evt) {
	subscribe(TheSwitch, "switch", myHandler)
	subscribe(TheContact, "contact.open", myHandler)
    subscribe(TheContact, "contact.closed", myHandler)
    subscribe(TheLock, "lock.locked", myHandler)
    subscribe(TheLock, "lock.unlocked", myHandler)
    subscribe(TheMotion, "motion.active", myHandler)
    subscribe(TheMotion, "motion.inactive", myHandler)
    subscribe(ThePresence, "presence.present", myHandler)
    subscribe(ThePresence, "presence.notPresent", myHandler)
    subscribe(TheWater, "waterSensor.dry", myHandler)
    subscribe(TheWater, "waterSensor.wet", myHandler)
}
/************************************************************************************************************
   Version/Copyright/Information/Help
************************************************************************************************************/
private def textAppName() {
	def text = "EchoSistant"
}	
private def textVersion() {
	def text = "Version 3.0.0 Alpha (11/30/2016)"
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
