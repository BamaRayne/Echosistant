/* 
* EchoSistant Rooms Profile - EchoSistant Add-on 
*
*		9/12/2018		Version:4.5 R.0.0.2		Rework for streamlining and added Echo Device selection
*		6/12/2017		Version:4.5 R.0.0.1		Alpha Release
*		2/17/2017		Version:4.0 R.0.0.1		Public Release
* 
*  Copyright 2016 Jason Headley & Bobby Dobrescu
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
import groovy.json.*
import java.text.SimpleDateFormat
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import java.security.InvalidKeyException
import java.security.MessageDigest

include 'asynchttp_v1'


definition(
	name			: "EchoSistant Rooms v4.5",
    namespace		: "Echo",
    author			: "JH/BD",
	description		: "EchoSistant Rooms - Custom rooms and Custom Control",
	category		: "",
    parent			: "Echo:EchoSistant v4.5",
	iconUrl			: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant.png",
	iconX2Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png",
	iconX3Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png")
/**********************************************************************************************************************************************/
private release() {
	def text = "R.0.4.5"
}
/**********************************************************************************************************************************************/
preferences {

    page name: "mainProfilePage"
    page name: "pSend"          
    page name: "pActions"
    page name: "pGroups"
    page name: "pRestrict"
    page name: "pDeviceControl"
    page name: "pPerson"
    page name: "pVirPerAction"
  	page name: "renamePage"
}

// MAIN PROFILE - HOME PAGE
def mainProfilePage() {	
    dynamicPage(name: "mainProfilePage", title:"", install: true, uninstall: installed) {
        section ("") {
            label title:"Name this Room", required:true
        } 
        section("") {
            href "messaging", title: "Outgoing Messages", description: pSendComplete(), state: pSendSettings()   
        	}
        section("") {
            href "feedback", title: "Control Groups and Feedback", description: mIntentD(), state: mIntentS()
        }
        section("") {    
            href "Shortcuts", title: "Shortcuts", description: mRoomsD(), state: mRoomsS()
            }
		section ("") {
            href "pTrackers", title: "Task Trackers", description: pTrackComplete(), state: pTrackSettings()    
        }
        section ("") {
            href "pRestrict", title: "Action Restrictions", description: pRestrictComplete(), state: pRestrictSettings()   
        }
	}
}


// OUTPUT MESSAGES HOME PAGE
page name: "messaging"
def messaging(){
    dynamicPage(name: "messaging", title: "Messaging Configuration", uninstall: false){    
        section("") {
            href "pSend", title: "Audio and Text Message Settings", description: pMsgComplete(), state: pMsgSettings()   
        }
        section ("") {    
            href "pConfig", title: "Message Output Settings", description: pConfigComplete(), state: pConfigSettings()
        }
    }
}

// SHORTCUT PHRASES
page name: "Shortcuts"
def Shortcuts(){
	dynamicPage(name: "Shortcuts", title: "Create shortcut phrases and custom Alexa responses", uninstall: false){
		if (childApps?.size()>0) {
			section("",  uninstall: false){
            app(name: "EchoSistant Rooms Shortcuts v4.5", appName: "EchoSistant Rooms Shortcuts v4.5", namespace: "Echo", title: "Create a New ShortCut", displayChildApps: false, multiple: true,  uninstall: false)
            }
        }
        else {
            section("",  uninstall: false){
                paragraph "NOTE: Looks like you haven't created any Rooms yet.\n \nPlease make sure you have installed the EchoSistant Shortcuts Add-on before creating a new Profile!"
                app(name: "EchoSistant Rooms Shortcuts", appName: "EchoSistant Rooms Shortcuts", namespace: "Echo", title: "Create a New Shortcut", multiple: true,  uninstall: false)
            }
		}
    }
}     

// FEEDBACK CONFIGURATION HOME PAGE
page name: "feedback"
def feedback(){
    dynamicPage(name: "feedback", title: "Device Groups Control and Feedback Configuration", uninstall: false){  
        section("") {
            href "pGroups", title: "Device Control Groups", description: pGroupComplete(), state: pGroupSettings()
        }
        section("") {    
        	href "fDevices", title: "Device Feedback", description: fDeviceComplete(), state: fDeviceSettings()
        }
        section("") {    
            href "pActions", title: "Location and Profile Actions (to execute when Profile runs)", description: pActionsComplete(), state: pActionsSettings()
        }        
    }
}

// FEEDBACK DEVICE GROUPS SELECTION
page name: "fDevices"
def fDevices(){
    dynamicPage(name: "fDevices", title: "Select devices physically in this room for Feedback", uninstall: false){
		section("Netatmo Weather Station") {
        	input "NetatmoTrue", "bool", title: "Do you have any of the Netatmo Weather Station Modules?", required: false, default: false, submitOnChange: true
        		if (NetatmoTrue) {
				input "fWind", "capability.sensor", title: "Wind Speed", multiple: false, required: false, submitOnChange: true
            	input "fRain", "capability.sensor", title: "Rain Accumulation", multiple: false, required: false, submitOnChange: true
            	input "fOutDoor", "capability.sensor", title: "Outdoor Module", multiple: false, required: false, submitOnChange: true
            	input "fBase", "capability.sensor", title: "Base Station", multiple: false, required: false, submitOnChange: true
            	input "fIndoor", "capability.sensor", title: "Indoor Modules", multiple: true, required: false, submitOnChange: true
			}
        }        
		section("Lights, Bulbs, and Switches") {
            input "fSwitches", "capability.switch", title: "Lights, Bulbs, and Switches...", multiple: true, required: false
        }
        section("Doors and Windows") {
            input "fGarage", "capability.garageDoorControl", title: "Garage Doors....", multiple: true, required: false
        	input "fDoors", "capability.contactSensor", title: "Contacts only on Doors...", multiple: true, required: false
        	input "fWindows", "capability.contactSensor", title: "Contacts only on Windows...", multiple: true, required: false
        	input "fShades", "capability.windowShade", title: "Curtains, Blinds, Shades...", multiple: true, required: false
        }
        section("Environmental Controls") {
            input "fFans", "capability.switch", title: "Ceiling Fans...", multiple: true, required: false
        	input "fVents", "capability.switchLevel", title: "Smart Vents...", multiple: true, required: false
        	input "fTemp", "capability.temperatureMeasurement", title: "Devices that Report Temperature...", multiple: true, required: false
			input "fHum", "capability.relativeHumidityMeasurement", title: "Devices that Report Humidity...", multiple: true, required: false
        }
        section("Locks, Motion, and Presence") {
            input "fLock", "capability.lock", title: "Smart Locks...", multiple: true, required: false
        	input "fPresence", "capability.presenceSensor", title: "Presence Sensors...", required: false, multiple: true
        	input "fMotion", "capability.motionSensor", title: "Motion Sensors...", required: false, multiple: true
        }
    }
}   

// TASK TRACKERS CONFIGURATION
page name: "pTrackers" 
def pTrackers(){
    dynamicPage(name: "pTrackers", title: "", uninstall: false) {
        section ("Tell me about the Task Trackers", hideable: true, hidden: true) {
	        paragraph "Your Task Trackers are now active for ${app.label}. \n" +
                    "\n" +
                  "Task trackers are designed as a quick way for you to keep track of important events or tasks. " +
                  "Do you have trouble remembering when you gave your pet their medicine? Or, when you changed the " +
                  "oil in your vehicles? Or, when the last time you called your Mom was? Well, now you can just tell " +
                  "Alexa that you've done something and she will record the date and time. Then when you want to find " +
                  "out when it was last done, you just ask! \n" +
                  "  \n" +
                  "Configuring a Task Tracker is fairly simple. This example is using a pets daily medicine. " +
                  "My cat is name Calliope and she is diabetic. She recieves an insulin shot twice daily. There are a lot " +
                  "of people in my home, so to ensure we do not over medicate, we use this Task Tracker ~~ \n" +
                    "\n" +
                  "First, I created a profile named Calliope and configured the Task Tracker in that profile " +
                  "I want to say this, 'Alexa, tell Calliope that she got her shot' \n" +
                  "I also want anyone in the home to be able to know right away when she was last medicated. So, using the " +
                  "Task Tracker, anyone can say, 'Alexa, ask Calliope when she was shot', and they are given the date and time \n" +
                  " \n" +
                  "Decide what phrase you want to say to record the Task, and then pull out the key words. In my " +
                  "example the key words are 'Got' 'Her' 'Shot'. \n" +
                  " \n" +  
                  "Put those key words into the Tracker in that order and you're done. Now give it a try! \n"  
        	}
            section ("Task Tracker One", hideable: true, hidden: false) {
            	input "trackerOne2", "text", title: "1st Tracker Key Word #1", required: false, default: "", submitOnChange: true
                input "trackerOne3", "text", title: "1st Tracker Key Word #2", required: false, default: "", submitOnChange: true
                input "trackerOne1", "text", title: "1st Tracker Key Word #3", required: false, default: "", submitOnChange: true
                input "t1notify", "bool", title: "Do you want to set a reminder?", required: false, default: false, submitOnChange: true
                	if (t1notify) {
                    	def title1
                    	if (reminderTitle != null) title1 = "${reminderTitle}"
                        	else {
                            	title1 = "A Reminder has not been created for this Task Tracker" }
                    	paragraph "You can create a reminder that will be scheduled for a future time based on the execution of this " +
                        "Task Tracker. Example: Reminder is configured to run every 12 hours, it will be scheduled for a ONE TIME execution " +
                        "12 hours AFTER this Task Tracker executes. "
                        href "reminderPage1", title: "Create a Reminder for this Task Tracker", description: "${title1}"
                        }
        			}
        	section ("Task Tracker Two", hideable: true, hidden: false) {
            	input "trackerTwo2", "text", title: "2nd Tracker Key Word #1", required: false, default: "", submitOnChange: true
                input "trackerTwo3", "text", title: "2nd Tracker Key Word #2", required: false, default: "", submitOnChange: true
                input "trackerTwo1", "text", title: "2nd Tracker Key Word #3", required: false, default: "", submitOnChange: true
                input "t2notify", "bool", title: "Do you want to set a reminder?", required: false, default: false, submitOnChange: true
                if (t2notify) {
                	def title2
                	if (reminderTitle2 != null) { title2 = "${reminderTitle2}" }
                    	else { 
                        	title2 = "A Reminder has not been created for this Task Tracker" }
                    	paragraph "You can create a reminder that will be scheduled for a future time based on the execution of this " +
                        "Task Tracker. Example: Reminder is configured to run every 12 hours, it will be scheduled for a ONE TIME execution " +
                        "12 hours AFTER this Task Tracker executes. "
                        href "reminderPage2", title: "Create a Reminder for this Task Tracker", description: "${title2}"
                        }
            }
        	section ("Task Tracker Three", hideable: true, hidden: false) {
        		input "trackerThree2", "text", title: "3rd Tracker Key Word #1", required: false, default: false, submitOnChange: true
                input "trackerThree3", "text", title: "3rd Tracker Key Word #2", required: false, default: false, submitOnChange: true
                input "trackerThree1", "text", title: "3rd Tracker Key Word #3", required: false, default: false, submitOnChange: true
                input "t3notify", "bool", title: "Do you want to set a reminder?", required: false, default: false, submitOnChange: true
                if (t3notify) {
                	def title3
                    if (remindertitle3 != null) { title3 = "${reminderTitle3}" }
                    	else {
                        	title3 = "A Reminder has not been created for this Task Tracker" }
                    	paragraph "You can create a reminder that will be scheduled for a future time based on the execution of this " +
                        "Task Tracker. Example: Reminder is configured to run every 12 hours, it will be scheduled for a ONE TIME execution " +
                        "12 hours AFTER this Task Tracker executes. "
        				href "reminderPage3", title: "Create a Reminder for this Task Tracker", description: "${title3}"
                        }
            }
        	section ("Task Tracker Four", hideable: true, hidden: false) {
                input "trackerFour2", "text", title: "4th Tracker Key Word #1", required: false, default: false, submitOnChange: true
                input "trackerFour3", "text", title: "4th Tracker Key Word #2", required: false, default: false, submitOnChange: true
                input "trackerFour1", "text", title: "4th Tracker Key Word #3", required: false, default: false, submitOnChange: true
                input "t4notify", "bool", title: "Do you want to set a reminder?", required: false, default: false, submitOnChange: true
                if (t4notify) {
                	def title4
                    if (remindertitle4 != null) { title4 = "${reminderTitle4}" }
                    	else {
                        	title4 = "A Reminder has not been created for this Task Tracker" }
                    	paragraph "You can create a reminder that will be scheduled for a future time based on the execution of this " +
                        "Task Tracker. Example: Reminder is configured to run every 12 hours, it will be scheduled for a ONE TIME execution " +
                        "12 hours AFTER this Task Tracker executes. "
        				href "t4Reminder", title: "Create a Reminder for this Task Tracker", description: "${title4}"
                        }
            }
        section ("Configure Notifications for $app.label", hideable: true, hidden: true) {
            paragraph "\n" +
                "Send a text message when a tracker is updated. Separate multiple numbers with a comma and " +
                "use this format: +19045554444,+19045554433 " 
            input name: "psms", type: "phone", title: "Send text notification to (optional):", required: false
            input "pPush", "bool", title: "Do you want to send a Push message when Trackers are updated?", required: false, defaultValue: false, submitOnChange: true
			}
    	}
	}           
       
// VIRTUAL PERSON NOTIFICATIONS (SENDS WHEN VP CHECKS IN/OUT)
page name: "pVPNotifyPage"
def pVPNotifyPage() {
    dynamicPage(name: "pVPNotifyPage", title: "Notification Settings") {
        section {
            input "vpPhone", "phone", title: "Text This Number", description: "Phone number", required: false, submitOnChange: true
            paragraph "For multiple SMS recipients, separate phone numbers with a comma"
            input "vpNotification", "bool", title: "Send A Push Notification", description: "Notification", required: false, submitOnChange: true
        }
    }
}   

// VIRTUAL PERSON CONFIG HOME PAGE
page name: "pPerson"
	def pPerson(){
    	dynamicPage(name: "pPerson", title: "", uninstall: false){
			def deviceId = "${app.label}" 
			def d = getChildDevice("${app.label}")
            section("Configure the Virtual Person Device") {
        		if(d==null) {
            	href "pPersonCreate", title: "Create Virtual Person Device"
                }
                if(d) {
                	href "pPersonDelete", title: "Delete Virtual Person Device"
      				input "notifyVPArrive", "bool", title: "Notify when Virtual Person Arrives", required: false, submitOnChange: true
					input "notifyVPDepart", "bool", title: "Notify when Virtual Person Departs", required: false, submitOnChange: true
					if (notifyVPArrive || notifyVPDepart) {
                	href "pVPNotifyPage", title: "Virtual Person Notification Settings", description: VPNotifyComplete(), state: VPNotifySettings()
                	}
                }
            }    
        }
	}

// VIRTUAL PERSON SENSOR DELETE
page name: "pPersonDelete"
def pPersonDelete(){
    dynamicPage(name: "pPersonDelete", title: "", uninstall: false) {
        section ("") {
            paragraph "You have deleted a virtual presence sensor device. You will no longer see this device in your " +
                "SmartThings Environment.  "
        }
        removeChildDevices(getAllChildDevices())
    }
}

// VIRTUAL PERSON SENSOR CREATE
page name: "pPersonCreate"
def pPersonCreate(){
    dynamicPage(name: "pPersonCreate", title: "", uninstall: false) {
        section ("") {
            paragraph "You have created a virtual presence sensor device named ($app.label). !!!DO NOT RENAME THIS DEVICE!!!. " +
              "You will now see this device in your 'Devices' list in the Smartthings IDE and SmartApp."
        }
        virtualPerson()
    }
}

// OUTPUT MESSAGES CONFIGURATION PAGE
page name: "pSend"
def pSend(){
    dynamicPage(name: "pSend", title: "Audio and Text Message Settings", uninstall: false){
        section ("") {
        	input "echoDevice", "capability.notification", title: "Amazon Alexa Devices", multiple: true, required: false
            	input "eVolume", "number", title: "Set the volume", description: "1-10 (default value = 3)", required: false, defaultValue: 3
            }
        section (""){
            input "synthDevice", "capability.speechSynthesis", title: "Speech Synthesis Devices", multiple: true, required: false
        	}
        section ("") {
            input "sonosDevice", "capability.musicPlayer", title: "Music Player Devices", required: false, multiple: true, submitOnChange: true    
            if (sonosDevice) {
                input "volume", "number", title: "Temporarily change volume", description: "0-100% (default value = 30%)", required: false
            	}
            }
        section ("") {
        	input "smc", "bool", title: "Send the message to Smart Message Control", default: false, submitOnChange: true
            }
        section ("" ) {
            input "sendText", "bool", title: "Enable Text Notifications", required: false, submitOnChange: true     
            if (sendText){      
                paragraph "You may enter multiple phone numbers separated by comma to deliver the Alexa message. E.g. +18045551122,+18046663344"
                input name: "sms", title: "Send text notification to (optional):", type: "phone", required: false
            }
        }    
        section ("Push Messages") {
            input "push", "bool", title: "Send Push Notification (optional)", required: false, defaultValue: false
        }        
    }                 
}   

// ALEXA RESPONSES PAGE
page name: "pConfig"
def pConfig(){
    dynamicPage(name: "pConfig", title: "Message Output Settings", uninstall: false) {
        section ("Configure Alexa Responses for Voice Messaging") {
            input "pDisableAlexaProfile", "bool", title: "Disable Alexa Feedback Responses (silence Alexa - overrides all other Alexa Options)?", defaultValue: false
            input "pAlexaCustResp", "text", title: "Custom Response from Alexa...", required: false, defaultValue: none
            input "pAlexaRepeat", "bool", title: "Alexa repeats the message to the sender as the response...", defaultValue: false, submitOnChange: true
            if (pAlexaRepeat) {			
                if (pAlexaRepeat && pAlexaCustResp){
                    paragraph 	"NOTE: only one custom Alexa response can"+
                        " be delivered at once. Please only enable Custom Response OR Repeat Message"
                }				
            }
            input "pContCmdsProfile", "bool", title: "Disable Conversation? (Alexa no longer prompts for additional commands, after a message is sent to a remote speaker, except for 'try again' if an error ocurs)", defaultValue: false
        }
        section ("Remote Speaker Settings") {
            input "pRunMsg", "Text", title: "Play this predetermined message when this profile executes...", required: false
            input "pPreMsg", "text", title: "Play this message before your spoken message...", defaultValue: none, required: false 
        	input "pDisableALLProfile", "bool", title: "Disable Audio Output on the Remote Speaker(s)?", required: false
        }
        section ("Text Notifications") {
            input "pRunTextMsg", "Text", title: "Send this predetermined text when this profile executes...", required: false
            input "pPreTextMsg", "text", title: "Append this text before the text message...", defaultValue: none, required: false 
        }             
    }             
}  

// PROFILE ACTIONS ARE EXECUTED EACH TIME THE PROFILE RUNS
page name: "pActions"
def pActions() {
    dynamicPage(name: "pActions", uninstall: false) {
        section ("Trigger these lights and actions when the Profile runs...") {
            href "pDeviceControl", title: "Select Devices...", description: pDevicesComplete() , state: pDevicesSettings()
            input "pMode", "enum", title: "Choose Mode to change to...", options: location.modes.name.sort(), multiple: false, required: false 
            input "shmState", "enum", title: "Set Smart Home Monitor to...", options:["stay":"Armed Stay","away":"Armed Away","off":"Disarmed"], multiple: false, required: false, submitOnChange: true
            if (shmState) {
                input "shmStateKeypads", "capability.lockCodes",  title: "Send status change to these keypads...", multiple: true, required: false, submitOnChange: true
            }
            input "pVirPer", "bool", title: "Toggle the Virtual Person State Automatically when this Profile Runs", default: false, submitOnChange: true, required: false
        		if (pVirPer) {
                	href "pPerson", title: "Configure the Virtual Person for ${app.label}", description: VPCreateComplete(), state: VPCreateSettings()
                    }
        	}
    }
}

// DEVICE CONTROL SELECTION PAGE
page name: "pDeviceControl"
def pDeviceControl() {
    dynamicPage(name: "pDeviceControl", title: "",install: false, uninstall: false) {
    	section ("Trigger these lights and actions when the Profile runs..."){
        	input "gDoor1", "capability.garageDoorControl", title: "Select Garage Doors...", multiple: true, required: false, submitOnChange: true
            }
        section (""){
            input "sSwitches", "capability.switch", title: "Select Lights and Switches...", multiple: true, required: false, submitOnChange: true
            if (sSwitches) {
                input "sSwitchCmd", "enum", title: "Command To Send",
                    options:["on":"Turn on","off":"Turn off","toggle":"Toggle"], multiple: false, required: false, submitOnChange:true
                input "delaySwitches", "bool", title: "Delay Actions?", required: false, defaultValue: false, submitOnChange:true
                if (delaySwitches) {
                    input "sSecondsOn", "number", title: "Turn on in Seconds?", defaultValue: none, required: false
                    input "sSecondsOff", "number", title: "Turn off in Seconds?", defaultValue: none, required: false
                }
                if (sSwitchCmd) input "sOtherSwitch", "capability.switch", title: "...and these other switches?", multiple: true, required: false, submitOnChange: true                        
                if (sOtherSwitch) input "sOtherSwitchCmd", "enum", title: "Command To Send to these other switches", 
                    options: ["on1":"Turn on","off1":"Turn off","toggle1":"Toggle"], multiple: false, required: false, submitOnChange: true
                if (sOtherSwitchCmd)	input "delayOtherSwitches", "bool", title: "Delay Actions?", required: false, defaultValue: false, submitOnChange:true
                if (delayOtherSwitches) {
                    input "sOtherSecondsOn", "number", title: "Turn on in Seconds?", defaultValue: none, required: false
                    input "sOtherSecondsOff", "number", title: "Turn off in Seconds?", defaultValue: none, required: false
                }
            }
        }
        section (""){
            input "sDimmers", "capability.switchLevel", title: "Select Dimmers...", multiple: true, required: false , submitOnChange:true
            if (sDimmers) { input "sDimmersCmd", "enum", title: "Command To Send",
                options:["on":"Turn on","off":"Turn off", "set":"Set level"], multiple: false, required: false, submitOnChange:true
                          }
            if (sDimmersCmd) {                       
                input "sDimmersLVL", "number", title: "Dimmers Level", description: "Set dimmer level", required: false, submitOnChange: true	
                input "delayDimmers", "bool", title: "Delay Actions?", required: false, defaultValue: false, submitOnChange:true      
                if (delayDimmers) {
                    input "sSecondsDimmers", "number", title: "Turn on in Seconds?", defaultValue: none, required: false
                    input "sSecondsDimmersOff", "number", title: "Turn off in Seconds?", defaultValue: none, required: false                        
                }
                input "sOtherDimmers", "capability.switchLevel", title: "...and these other Dimmers...", multiple: true, required: false , submitOnChange:true
                if (sOtherDimmers) { 
                    input "sOtherDimmersCmd", "enum", title: "Command To Send to these other Dimmers", 
                        options:["on":"Turn on","off":"Turn off","set":"Set level"], multiple: false, required: false, submitOnChange:true
                }
                if (sOtherDimmersCmd) {
                    input "sOtherDimmersLVL", "number", title: "Dimmers Level", description: "Set dimmer level", required: false, submitOnChange: true
                    input "delayOtherDimmers", "bool", title: "Delay Actions?", required: false, defaultValue: false, submitOnChange: true
                    if (delayOtherDimmers) {
                        input "sSecondsOtherDimmers", "number", title: "Turn on in Seconds?", defaultValue: none, required: false
                        input "sSecondsOtherDimmersOff", "number", title: "Turn off in Seconds?", defaultValue: none, required: false                        
                    }
                }
            }
        }
        section (""){
            input "sHues", "capability.colorControl", title: "Select These Colored Lights...", multiple: true, required: false, submitOnChange:true
            if (sHues) {
                input "sHuesCmd", "enum", title: "Command To Send ", 
                    options:["on":"Turn on","off":"Turn off","setColor":"Set Color"], multiple: false, required: false, submitOnChange:true
                if(sHuesCmd == "setColor") {
                    input "sHuesColor", "enum", title: "Hue Color?", required: false, multiple:false, options: fillColorSettings().name
                }
                if(sHuesCmd == "setColor" || sHuesCmd == "on") {
                    input "sHuesLevel", "enum", title: "Light Level?", required: false, options: [[10:"10%"],[20:"20%"],[30:"30%"],[40:"40%"],[50:"50%"],[60:"60%"],[70:"70%"],[80:"80%"],[90:"90%"],[100:"100%"]], submitOnChange:true                        
                }
            }
            if (sHuesLevel)	input "sHuesOther", "capability.colorControl", title: "...and these other Colored Lights?", multiple: true, required: false, submitOnChange:true
            if (sHuesOther) {
                input "sHuesOtherCmd", "enum", title: "Command To Send to these other Colored Lights", options:["on":"Turn on","off":"Turn off","setColor":"Set Color"], multiple: false, required: false, submitOnChange:true
                if(sHuesOtherCmd == "setColor") {
                    input "sHuesOtherColor", "enum", title: "Which Color?", required: false, multiple:false, options: fillColorSettings().name
                }
                if(sHuesOtherCmd == "on" || sHuesOtherCmd == "setColor") {
                    input "sHuesOtherLevel", "enum", title: "Light Level?", required: false, options: [[10:"10%"],[20:"20%"],[30:"30%"],[40:"40%"],[50:"50%"],[60:"60%"],[70:"70%"],[80:"80%"],[90:"90%"],[100:"100%"]]                       
                }
            }
        }
        section ("") {
            input "sFlash", "capability.switch", title: "Flash Switch(es)", multiple: true, required: false, submitOnChange:true
            if (sFlash) {
                input "numFlashes", "number", title: "This number of times (default 3)", required: false, submitOnChange:true
                input "onFor", "number", title: "On for (default 1 second)", required: false, submitOnChange:true			
                input "offFor", "number", title: "Off for (default 1 second)", required: false, submitOnChange:true
            }
        }
    }
} 

// DEVICE GROUPS PAGE
page name: "pGroups"
def pGroups() {
    dynamicPage(name: "pGroups", title: "", install: false, uninstall: false) {
        section ("") {	
        	paragraph "Control all groups by saying, 'Alexa, turn on/off the ~group name~ in the $app.label'"
        }
        section ("Garage Doors"){
        	input "gGarage", "capability.garageDoorControl", title: "Garage Door(s)...", multiple:true, required: false
        }
        section ("Vents and Window Coverings"){ 
            input "gVents", "capability.switchLevel", title: "Group Smart Vent(s)...", multiple: true, required: false
            input "gShades",  "capability.windowShade", title: "Group These Window Covering Devices...", multiple: true, required: false   
        }                
        section ("Media"){
            input "sMedia", "capability.mediaController", title: "Use This Media Controller", multiple: false, required: false
            input "sSpeaker", "capability.musicPlayer", title: "Use This Media Player Device For Volume Control", required: false, multiple: false
            input "sSynth", "capability.speechSynthesis", title: "Use This Speech Synthesis Capable Device", multiple: false, required: false
        }             
        section ("Lights and Switches"){
            input "gSwitches", "capability.switch", title: "Group Lights and Switches...", multiple: true, required: false
        }
        section ("Ceiling Fans and Automations"){
   			input "gDisable", "capability.switch", title: "Automation Disable Switches (disable = off, enable = on)", multiple: true, required: false
            input "gFans", "capability.switch", title: "Fans and Ceiling Fans...", multiple: true, required: false
        }
        section ("Create Custom Groups") {
            input "gCustom1N", "text", title: "Name this Group...", multiple: false, required: false
            input "gCustom1", "capability.switch", title: "Select Switches for $gCustom1N...", multiple: true, required: false
            	if (gCustom1) {         
            input "gCustom2N", "text", title: "Name this Group...", multiple: false, required: false
            input "gCustom2", "capability.switch", title: "Select Switches for $gCustom2N...", multiple: true, required: false
            }
            	if (gCustom2) {
            input "gCustom3N", "text", title: "Name this Group...", multiple: false, required: false    
			input "gCustom3", "capability.switch", title: "Select Switches for $gCustom3N...", multiple: true, required: false
            }
            	if (gCustom3) {
            input "gCustom4N", "text", title: "Name this Group...", multiple: false, required: false    
			input "gCustom4", "capability.switch", title: "Select Switches for $gCustom4N...", multiple: true, required: false
            }
            	if (gCustom4) {
            input "gCustom5N", "text", title: "Name this Group...", multiple: false, required: false    
			input "gCustom5", "capability.switch", title: "Select Switches for $gCustom5N...", multiple: true, required: false
        	}
        }
    }
}

// RESTRICTIONS HOME PAGE
page name: "pRestrict"
def pRestrict(){
    dynamicPage(name: "pRestrict", title: "Profile Restrictions", uninstall: false) {
        section ("") {
            input "modes", "mode", title: "Audio only when mode is", multiple: true, required: false
        }        
        section (""){	
            input "days", title: "Audio only on these days of the week", multiple: true, required: false,
                "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
        }
        section (""){
            href "certainTime", title: "Audio only during a certain time", description: pTimeComplete(), state: pTimeSettings()
        }   
    }
}

// RESTRICTION PAGE - TIME SELECTIONS
page name: "certainTime"
def certainTime() {
    dynamicPage(name:"certainTime",title: "Only during a certain time", uninstall: false) {
        section("Beginning at....") {
            input "startingX", "enum", title: "Starting at...", options: ["A specific time", "Sunrise", "Sunset"], required: false , submitOnChange: true
            if(startingX in [null, "A specific time"]) input "starting", "time", title: "Start time", required: false, submitOnChange: true
            else {
                if(startingX == "Sunrise") input "startSunriseOffset", "number", range: "*..*", title: "Offset in minutes (+/-)", required: false, submitOnChange: true
                else if(startingX == "Sunset") input "startSunsetOffset", "number", range: "*..*", title: "Offset in minutes (+/-)", required: false, submitOnChange: true
                    }
        }
        section("Ending at....") {
            input "endingX", "enum", title: "Ending at...", options: ["A specific time", "Sunrise", "Sunset"], required: false, submitOnChange: true
            if(endingX in [null, "A specific time"]) input "ending", "time", title: "End time", required: false, submitOnChange: true
            else {
                if(endingX == "Sunrise") input "endSunriseOffset", "number", range: "*..*", title: "Offset in minutes (+/-)", required: false, submitOnChange: true
                else if(endingX == "Sunset") input "endSunsetOffset", "number", range: "*..*", title: "Offset in minutes (+/-)", required: false, submitOnChange: true
                    }
        }
    }
}

/************************************************************************************************************
**** HANDLERS FOLLOW
************************************************************************************************************/

// VIRTUAL PERSON CREATE HANDLER
def virtualPerson() {
    log.trace "Creating Voice Companion Virtual Person Device"
    def deviceId = "${app.label}" 
    def d = getChildDevice("${app.label}")
    if(!d) {
        d = addChildDevice("Assistant", "Voice Companion Simulated Presence Sensor", deviceId, null, [label:"${app.label}"])
        log.trace "Voice Companion Virtual Person Device - Created ${app.label} "
    }
    else {
        log.trace "NOTICE!!! Found that the EVPD ${d.displayName} already exists. Only one device per profile permitted"
    }
}  

// VIRTUAL PERSON DELETE HANDLER
private removeChildDevices(delete) {
    log.debug "The Virtual Person Device '${app.label}' has been deleted from your SmartThings environment"
    delete.each {
        deleteChildDevice(it.deviceNetworkId)
    }
}

// VIRTUAL PERSON CHECK IN/OUT TOGGLE HANDLER
private pVirToggle() {
	def vp = getChildDevice("${app.label}")
    if(vp) {
    if (vp?.currentValue('presence')?.contains('not')) {
            log.info "${app.label} has arrived"
            vp.arrived()
            }
        else if (vp?.currentValue('presence')?.contains('present')) {
            log.info "${app.label} has departed"
            vp.departed()
            }
    	}
	}

/************************************************************************************************************
Base Process
************************************************************************************************************/    
def installed() {
    log.debug "Installed with settings: ${settings}, current app version: ${release()}"
    state.ProfileRelease ="Profile: "  + release()
    initialize()
}

def updated() {
    log.debug "Updated with settings: ${settings}, current app version: ${release()}"
    state.ProfileRelease = "Profile: " + release()
    unsubscribe()
    initialize()
    if(!atomicState?.isInstalled) { atomicState?.isInstalled = true }
}
def initialize() {
    state.lastMessage
    state.lastTime
    state.lastAction = null
    state.lastActivity
    //Sound Cancellation    
    state.pMuteAlexa = settings.pDisableAlexaProfile ?: false
    state.pMuteAll = settings.pDisableALLProfile ?: false
    // Turn OFF the Color Loop
    unschedule("startLoop")
    unschedule("continueLoop")
    //SHM status change and keypad initialize
    subscribe(location, locationHandler)
    state.responseTxt = null
    state.lambdaReleaseTxt = "Not Set"
    state.lambdaReleaseDt = "Not Set" 
    state.lambdatextVersion = "Not Set"
    //Alexa Responses
    
    state.pTryAgain = false
    state.pContCmds = settings.pContCmdsProfile == false ? true : settings.pContCmdsProfile == true ? false : false
    state.pMuteAlexa = settings.pEnableMuteAlexa
    state.pShort = settings.pUseShort
    state.pContCmdsR = "init"       
    //OTHER 
    state.sched
    state.sched2
    state.sched3
    state.sched4
    state.date
    if (fMinutes1) { state.fSched1 = "1" }
    	else { state.fSched1 = "0" }
    	log.info "fSched1 = $state.fSched1"
    def String deviceType = (String) null
    def String outputTxt = (String) null
    def String result = (String) null
    def String deviceM = (String) null
    def currState
    def stateDate
    def stateTime
    def data = [:]
    if (fDevice != null) {
        fDevice = fDevice.replaceAll("[^a-zA-Z0-9 ]", "") }
    def fProcess = true
    state.pTryAgain = false
    if (taskTrackers) {
        log.info "Initializing Task Tracker variable for '${app.label}'"
        if (state.trackerOne == null) {state.trackerOne = "I'm sorry, I have not been told when the ${trackerOne1} was ${trackerOne2}" }
        if (state.trackerTwo == null) {state.trackerTwo = "I'm sorry, I have not been told when the ${trackerTwo1} was ${trackerTwo2}" }
        if (state.trackerThree == null) {state.trackerThree = "I'm sorry, I have not been told when the ${trackerThree1} was ${trackerThree2}" }
        if (state.trackerFour == null) {state.trackerFour = "I'm sorry, I have not been told when the ${trackerFour1} was ${trackerFour1}" }
    }
}

def uninstalled() {
	revokeAccessToken()
    LogAction("${app?.getLabel()} has been Uninstalled...", "warn", true)
}

private stripBrackets(str) {
  	str = str.replace("[", "")
	return str.replace("]", "")
}

/******************************************************************************************************
PARENT STATUS CHECKS
******************************************************************************************************/
def checkState() {
return state.pMuteAlexa
}
def checkRelease() {
return state.ProfileRelease
}

/******************************************************************************************************
SPEECH AND TEXT PROCESSING INTERNAL - FEEDBACK
******************************************************************************************************/
def profileFeedbackEvaluate(params) {
    log.info "profileFeedbackEvaluate has been activated with params: $params"
    def tts = params.ptts
    def intent = params.pintentName        
    def childName = app.label       
    //Data for CoRE 
    def data = [args: tts]
    def dataSet = [:]
    //Output Variables
    def pTryAgain = false
    def pPIN = false
    def pShort = state.pShort
    def String pContCmdsR = (String) "tts"
    def String outputTxt = (String) null 
    def String scheduler = (String) null     
    def String ttsR = (String) null
    def String command = (String) null
    def String deviceType = (String) null
    def String colorMatch = (String) null
    if (tts != null) {
        tts = tts.replaceAll("[^a-zA-Z0-9 ]", "") }
    if (debug) log.debug "Messaging Profile Data: (ptts) = '${ptts}', (pintentName) = '${pintentName}'"   

    if (command == "undefined") {
        outputTxt = "Sorry, I didn't get that, "
        state.pTryAgain = false
        state.pContCmdsR = "clear"
        state.lastAction = null
        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
    }    
	    if (intent == childName){
            if (test){
                outputTxt = "Congratulations! Your EchoSistant is now setup properly, good job" 
                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
            }
            
           if(parent.debug) log.debug "I have received a feedback command: ${command}, deviceType:  ${deviceType}, with this text: ${tts}"


		//  FEEDBACK HANDLER

          def fDevice = tts.contains("garage") ? fGarage : tts.contains("vent") ? fVents : tts.contains("light") ? fSwitches : tts.contains("door") ? fDoors : tts.contains("window") ? fWindows : tts.contains("fan") ? fFans : 
            tts.contains("motion") ? fMotion : tts.contains("lock") ? fLocks : tts.contains("shade") ? fShades : tts.contains("curtains") ? fShades : tts.contains("blinds") ? fShades : tts.startsWith("who") ? fPresence : tts.contains("batteries") ? fBattery : null
            
          def fValue = tts.contains("garage") ? "contact" : tts.contains("vent") ? "switch" : tts.contains("light") ? "switch" : tts.contains("door") ? "contact" : tts.contains("window") ? "contact" : tts.contains("fan") ? "switch" : 
            tts.contains("lock") ? "lock" : tts.contains("shade") ? "windowShade" : tts.contains("blind") ? "windowShade" :  tts.contains("who") ? "presence" : tts.contains("curtains") ? "windowShade" : null
            
          def fName = tts.contains("motion") ? "motion sensors" : tts.contains("vent") ? "vent" : tts.contains("lock") ? "lock" : tts.contains("door") ? "door" : tts.contains("window") ? "window" : tts.contains("fan") ? "fan" : 
            tts.contains("light") ? "light" : tts.contains("shade") ? "shade" : tts.contains("blind") ? "blind" : tts.contains("curtains") ? "curtain" : null
            
          def fCommand = tts=="who is at home" ? "present" : tts=="who is home" ? "present" : tts=="who is not home" ? "not present" : tts=="who is not at home" ? "not present" : tts.contains("open") ? "open" : 
            tts.contains("closed") ? "closed" : tts.contains("on") ? "on" : tts.contains("off") ? "off" : null
          
          if (tts.contains("check") && tts.contains("light")) { fCommand = "on" }
            if (tts.contains("motion")) { fCommand = "active" }
            if (tts.contains("check")) {
          	if (tts.contains("lock") || tts.contains("door") || tts.contains("window") || tts.contains("vent") || tts.contains("shades") || tts.contains("blind") || tts.contains("curtain")) {
          		fCommand = "open" }}
			
            // TEMPERATURE //
            if (tts == "what is the temperature" || tts == "whats the temperature" || tts == "whats the temp"){
                if(fTemp){
                    def sensors = fTemp?.size()
                    def tempAVG = fTemp ? getAverage(fTemp, "temperature") : "undefined device"          
                    def currentTemp = tempAVG
                    outputTxt = "The current temperature in the $app.label is $currentTemp degrees."
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                }
                else {
                    outputTxt = "There are no temperature sensors selected, go to the Voice Companion Smart App and select one or more sensors"
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]		
                }                            
            }

            // HUMIDITY //
            if (tts == "what is the humidity" || tts == "whats the humidity" || tts == "how humid is it"){
                if(fHum){
                    def sensors = fHum?.size()
                    def humidAVG = fHum ? getAverage(fHum, "humidity") : "undefined device"          
                    def currentHum = humidAVG
                    outputTxt = "The current humidity in the $app.label is $currentHum percent."
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                }
                else {
                    outputTxt = "There are no humidity sensors selected, go to the Voice Companion Smart App and select one or more sensors"
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]		
                }                            
            }
            
            
            /// CHECK FOR MOTION
            if (tts.contains("motion") || tts=="is there" || tts=="is anyone" || tts=="is there anyone" || tts=="is something moving" || tts=="is someone" || tts=="is there someone") {
                if (fMotion == null) {
                outputTxt = "There are no sensors selected for me to determine if there is motion in the ${app.label}"
                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
            	}
            }
            if(tts.contains("check the status") || tts.contains("motion") || tts=="is there" || tts=="is anyone" || tts=="is there anyone" || tts=="is something moving" || tts=="is someone" || tts=="is there someone") { 
                if(fMotion != null) {
                    def devListMotion = []
                    def devStatus
                    fMotion.find { deviceName ->
                        if (deviceName.latestValue("occupancy")=="occupied" || deviceName.latestValue("occupancy")=="vacant" || deviceName.latestValue("occupancy")=="locked" || deviceName.latestValue("occupancy")=="checking") {
                        	devStatus = deviceName.latestValue("occupancy")
                            String device = (String) deviceName
                            devListMotion += device
                            if (tts.contains("check")) { 
                                outputTxt = "The $app.label is currently $devStatus" //There is activity in the $app.label"
                                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN] 
                            }
                        outputTxt = "Yes, the $app.label is currently $devStatus" //there is activity in the ${app.label}" 
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN] 
                        }
                        else if (deviceName.latestValue("motion")=="active") {
                        	String device = (String) deviceName
                            devListMotion += device
                            if (tts.contains("check")) { 
                                outputTxt = "There is activity in the $app.label"
                                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN] 
                            }
                        outputTxt = "Yes, there is activity in the ${app.label}" 
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN] 
                        }
                    	else {
                        outputTxt = "I am not detecting any activity in the ${app.label}"
                            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN] 
                        }
            		}
                }
                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN] 
            }
            
            //  MODE STATUS FEEDBACK
            if (tts.contains("mode")) {
                outputTxt = "The Current Mode is " + location.currentMode      
                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
            }

            //  MISC DEVICES FEEDBACK           
            if (tts.contains("who") || tts.contains("window") || tts.contains("vent") || tts.contains("lock") || tts.contains("blind") || tts.contains("curtain") || tts.contains("shade") || tts.contains("door") || tts.contains("lights") || tts.contains("fan")) {
                if (tts.contains("how") || tts.contains("on") || tts.endsWith("off") || tts.contains("open") || tts.contains("closed") || tts.endsWith("home") || tts.startsWith("check")) {
                    def devList = [] 
                   if (fDevice == null) {
                  		outputTxt = "I'm sorry, it seems that you have not selected any devices for this query, please configure your feedback devices in the EchoSistant smartapp."
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
		                    }
							fDevice.each { deviceName ->
                                if (deviceName.latestValue("${fValue}")=="${fCommand}") {
                                    String device  = (String) deviceName
                                    devList += device
                                }
                            }
					// PRESENCE RETURNS //        
                  	if (tts.startsWith("who") && tts.endsWith("home")) {
                    	if (devList.size() >0) {
                      		if (devList.size() == 1) { outputTxt = "The only person $fCommand is $devList" }
                      		if (devList.size() >1) { outputTxt = "The following people are $fCommand, $devList" }}
                      		else if (fCommand == "not present") { outputTxt = "Everyone is currently at home" }
                    	return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                    	}
                    // MISC DEVICES RETURNS A YES OR NO //
                    if (tts.startsWith("are") || tts.startsWith("how")|| tts.startsWith("check")) {
                        if (devList.size() > 0) {
                        	if (devList.size() == 1) { outputTxt = "There is one $fName " + fCommand + " in the ${app.label} " }
                            else { outputTxt = "There are " + devList.size() + " " + fName + " 's " + fCommand + " in the ${app.label} " }}    
                        	else (outputTxt = "There are no ${fName}'s " + fCommand + " in the ${app.label} " )
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                    }
                    // RETURNS A LIST OF DEVICES //        
                    if (tts.startsWith("what") || tts.startsWith("which") || tts.startsWith("is")) {
                        if (devList.size() > 0) { 
                            if (devList.size() == 1) {  outputTxt = "The" + devList + " is the only " + fName + " " + fCommand +  " in the ${app.label} " }
                            else {outputTxt = "The following $fName's are $fCommand in the $app.label $devList" }}
                        	else (outputTxt = "There are no $fName's $fCommand in the $app.label" )   
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                    	}
                    }
                }
            }


// NETATMO WEATHER STATION HANDLER FOR FEEDBACK
    if (NetatmoTrue) {
    log.info "Netatmo Weather Station called"
    	def WindMaxTime = fWind?.currentValue("date_max_wind_str")
    	def WindMax = fWind?.currentValue("max_wind_str")
    	def WindGust = fWind?.currentValue("GustStrength")
        def WindSpeed = fWind?.currentValue("WindStrength")
        def WindDir = fWind?.currentValue("WindDirection")
        def WindUpdate = fWind?.currentValue("lastupdate")
       	def RainFall = fRain?.currentValue("rain")
        def RainUpdate = fRain?.currentValue("lastupdate")
       	def humidity = fOutDoor?.currentValue("humidity")
        def currTemp = fOutDoor?.currentValue("temperature")
        def minTemp = fOutDoor?.currentValue("min_temp")
        def maxTemp = fOutDoor?.currentValue("max_temp")
        def trend = fOutDoor?.currentValue("temp_trend")
        def outdoorUpdate = fOutDoor?.currentValue("lastupdate")

		if (tts.contains("wind speed") || tts.contains("wind blowing")) {
        	if (WindSpeed == 0) { outputTxt = "There is currently not any wind blowing" }
            if (WindSpeed > 0) { outputTxt = "At $WindUpdate, I recorded the wind blowing at $WindSpeed miles per hour, at $WindDir. I've also recorded a wind gust of $WindGust miles per hour, " +
            "with a max wind speed of $WindMax miles per hour recorded at $WindMaxTime ." }
            }

		if (tts=="is it raining") {
        	if (RainFall == 0) { outputTxt = "No, it is not currently raining" } 
        	if (RainFall > 0) { outputTxt = "Yes, as of $RainUpdate, there has been $RainFall inches of rain" } }
        if (tts=="has it rained" || tts=="did it rain" || tts.contains("how much has it rained")) {
        	if (RainFall == 0) { outputTxt = "There has not been any rain recorded in the past 24 hours" } 
        	if (RainFall > 0) { outputTxt = "Yes, as of $RainUpdate, there has been $RainFall inches of rain" } }
        if (tts.contains("will it rain") || tts.contains("going to rain")) {
        	outputTxt = "I'm sorry, I can not forecast the rain. Please simply say, Alexa, is it going to rain" }
       
		if (tts=="whats the temperature" || tts=="what is the temperature") {
            outputTxt = "At $outdoorUpdate today, the temperature was $currTemp degrees and the temperature is currently trending $trend" }
        if (tts.contains("temperature and humidity") || tts.contains("humidity and temperature") || tts.contains("temperature and the humidity")) {
        	outputTxt = "The current temperature is $currTemp and the humidity is $humidity percent" }
        
        if (tts=="whats the humidity" || tts=="what is the humidity") {
        	outputTxt = "The humidity is currently $humidity percent" }
        
        if (tts.contains("what is the weather like") || tts.contains("whats the weather like") || tts=="how is it" || tts.contains("whats the weather") || tts.contains("whats it like outside")) {    
            outputTxt = "At $outdoorUpdate today, the temperature was $currTemp degrees, $minTemp degrees was the low, and $maxTemp degrees was the high. " +
            "Currently the temperature is trending $trend, the humidity is $humidity percent, and the wind is blowing at $WindSpeed miles per hour."
    		return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]	
            }
            
    log.info "outputTxt = $outputTxt"        
    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]	

}            
            //  TASK TRACKER FEEDBACK
            if (tts.startsWith("was") || tts.startsWith("has") || tts.startsWith("when") || tts.startsWith("did") || tts.startsWith("what")) {
                if (tts.contains("she") || tts.contains("he") || tts.contains("has") || tts.contains("was") || tts.contains("were") || tts.contains("did") || tts.contains("it")) {
                    if (tts.contains("${trackerOne1}".toLowerCase()) && state.trackerOne != null ) {
                    	if (t1notify){ outputTxt = state.trackerOne + " , and there is a reminder scheduled for " + state.sched }
                        	else { outputTxt = state.trackerOne } }
                    if (tts.contains("${trackerTwo1}".toLowerCase()) && state.trackerTwo != null ) {
                    	if (t2notify){ outputTxt = state.trackerTwo + " , and there is a reminder scheduled for " + state.sched2 }
                        	else { outputTxt = state.trackerTwo } }
                    if (tts.contains("${trackerThree1}".toLowerCase()) && state.trackerThree != null ) {
                    	if (t3notify){ outputTxt = state.trackerThree + " , and there is a reminder scheduled for " + state.sched3 }
                        	else { outputTxt = state.trackerThree } }
                    if (tts.contains("${trackerFour1}".toLowerCase()) && state.trackerFour != null ) {
                    	if (t4notify){ outputTxt = state.trackerFour + " , and there is a reminder scheduled for " + state.sched4 }
                        	else { outputTxt = state.trackerFour } }
                }
                else {outputTxt = "I'm sorry, I have not been given this information"}
            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
            }
			else {
            outputTxt = "Sorry, you must first set up your profile before trying to execute it."
            pTryAgain = true
            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
        }
    }

/******************************************************************************************************
SPEECH AND TEXT PROCESSING INTERNAL - CONTROL & MESSAGING
******************************************************************************************************/
def profileEvaluate(params) {
    def tts = params.ptts
    def intent = params.pintentName        
    def childName = app.label 
    //Data for CoRE 
    def data = [args: tts]
    def dataSet = [:]
    //Output Variables
    def pTryAgain = false
    def pPIN = false
    def pShort = state.pShort
    def String pContCmdsR = (String) "tts"
    def String outputTxt = (String) null 
    def String scheduler = (String) null     
    def String ttsR = (String) null
    def String command = (String) null
    def String deviceType = (String) null
    def String colorMatch = (String) null

    //Voice Activation Settings
    def muteAll = tts.contains("disable sound") ? "mute" : tts.contains("disable audio") ? "mute" : tts.contains("mute audio") ? "mute" : tts.contains("silence audio") ? "mute" : null
    muteAll = tts.contains("activate sound") ? "unmute" : tts.contains("enable audio") ? "unmute" : tts.contains("unmute audio") ? "unmute" : muteAll
    def muteAlexa = tts.contains("disable Alexa") ? "mute" : tts.contains("silence Alexa") ? "mute" : tts.contains("mute Alexa") ? "mute" : null
    muteAlexa = tts.contains("enable Alexa") ? "unmute" : tts.contains("start Alexa") ? "unmute" : tts.contains("unmute Alexa") ? "unmute" : muteAll
    def test = tts.contains("this is a test") ? true : tts.contains("a test") ? true : false
    if (parent.debug) log.debug "Message received from Parent with: (tts) = '${tts}', (intent) = '${intent}', (childName) = '${childName}', current app version: ${release()}"  
//def result
        if (command == "undefined") {
        outputTxt = "Sorry, I didn't get that, "
        state.pTryAgain = true
        state.pContCmdsR = "clear"
        state.lastAction = null
        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
    }    

        if (test){
            outputTxt = "Congratulations! Your EchoSistant is now setup properly" 
            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]       
        }
        def getCMD = getCommand(tts) 
        deviceType = getCMD.deviceType
        command = getCMD.command
        if(parent.debug) log.debug "I have received a control command: ${command}, deviceType:  ${deviceType}"
			
        if(muteAll == "mute" || muteAll == "unmute"){
            if(muteAll == "mute"){
                state.pMuteAll = true
                outputTxt = "Ok, audio messages have been disabled"       
                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]               
            }
            else { 
                state.pMuteAll = false
                outputTxt = "Ok, audio messages have been enabled"       
                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN] 
            }
        }
        if(muteAlexa == "mute" || muteAlexa == "unmute"){
            if(muteAlexa == "mute"){
                state.pMuteAlexa = true
                outputTxt = "Ok, Alexa Feedback Responses have been disabled"       
                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]                
            }
            else { 
                state.pMuteAlexa = false
                outputTxt = "Ok, Alexa Feedback Responses have been enabled"       
                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
            }
        }
       	if (tts.contains("stop the conversation")) {
            state.pContCmds = false
            outputTxt = "Ok, disabling conversational features. To activate just say, start the conversation"
            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
        }
        if (tts.contains("cancel notifications") || tts.contains("cancel reminders")) {
        	unschedule()
            outputTxt = "Ok, I've cancelled all scheduled Task Tracker Reminders for the $app.label"
            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
        }           
        if (tts.contains("start the conversation")) {
            state.pContCmds = true
            outputTxt = "Ok, activating conversational features. To deactivate just say, stop the conversation"
            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
        }
        if (tts == ("repeat last message")) {
            outputTxt = "The last message sent to ${app.label} was, " + state.lastMessage
            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
        }
        if (tts.contains("feedback")) {
        	if (tts.contains("activate") || tts.contains("start") || tts.contains("enable")) {
            	state.pMuteAlexa = true
            	outputTxt = "Ok, enabling Alexa feedback. To deactivate just say, deactivate the feedback"
            	}
        	if (tts.contains("deactivate") || tts.contains("stop") || tts.contains("disable")) {
            	state.pMuteAlexa = false
            	outputTxt = "Ok, disabling Alexa feedback. To activate just say, activate the feedback"
            	}
            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
        }
        if (tts.contains("short answer") || tts.contains("short answers")) {
            if (tts.contains("activate") || tts.contains("start") || tts.contains("enable")) {
            	state.pShort = true
            	outputTxt = "Ok, short answers are on."
                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]    
		        }        
            else if (tts.contains("deactivate") || tts.contains("stop") || tts.contains("disable")) {
            	state.pShort = false
            	outputTxt = "Ok, disabling short answers. To activate just say, enable the short answers"
            	return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]    
        	}
        }        
        if (tts.contains("garage door")) {
            if (deviceType == "door") {
                if (command == "open") {
                    log.info "opening device: $gGarage"
                    gGarage.open()
                    outputTxt = "Ok, opening the garage door"
                }
                else if (command == "closed") {
                    gGarage.close()
                    outputTxt = "Ok, closing the garage door"
                }
            }
            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
        }
		if (command == null && deviceType == null) {
			outputTxt = ttsHandler(tts)            
            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]       
        	}
        if (command == "undefined" && deviceType != null) {
        	outputTxt = ttsHandler(tts)
            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]       
            }
                

/**********************
FREE TEXT CONTROL ENGINE 
***********************/       
                //Feedback for devices
                if (gHues?.size()>0) {
                    if (feedback == true) {
                    }
                }    
                if (deviceType == "volume") {
                    log.info "Volume controls activated"
                        if(sSpeaker || sSynth){
                            def deviceD = sSpeaker? sSpeaker : sSynth? sSynth : "undefined"
                            if (command == "increase" || command == "decrease" || command == "mute" || command == "unmute"){
                                def currLevel = deviceD.latestValue("level")
                                def currState = deviceD.latestValue("switch")
                                def newLevel = parent.cVolLevel*10  
                                if (command == "mute" || command == "unmute") {
                                    deviceD."${command}"()
                                    def volText = command == "mute" ? "muting" : command == "unmute" ? "unmuting" : "adjusting" 
                                    outputTxt = "Ok, " + volText + " the " + deviceD.label
                                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                                }
                                if (command == "increase") {
                                    newLevel =  currLevel + newLevel
                                    newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
                                }
                                if (command == "decrease") {
                                    newLevel =  currLevel - newLevel
                                    newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
                                }                        
                                if (newLevel > 0 && currState == "off") {
                                    deviceD.on()
                                    deviceD.setLevel(newLevel)
                                }
                                else {                                    
                                    if (newLevel == 0 && currState == "on") {deviceD.off()}
                                    else {deviceD.setLevel(newLevel)}
                                } 
                                outputTxt = "Ok, setting  " + deviceD.label + " volume to " + newLevel + " percent"
                                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                            } 
                        }
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                    }                
                if (command != null && deviceType != null && command != "undefined" ) {
                    //LIGHT SWITCHES && CUSTOM GROUPS
                    if (deviceType == "light" || deviceType == "light1" || deviceType == "light2" || deviceType == "light3" || deviceType == "light4" || deviceType == "light5"){
                        if (command == "decrease") {
                        dataSet =  ["command": command, "deviceType": deviceType]
                        outputTxt = advCtrlHandler(dataSet)
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]                                 
                    	}
                    }
                            else if (command == "decrease" || command == "increase" || command == "high" || command == "medium" || command == "low"){
                                dataSet =  ["command": command, "deviceType": deviceType]
                                outputTxt = advCtrlHandler(dataSet)
                                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                            }
                    
                    //DISABLE SWITCHES
                    if (deviceType == "light" || deviceType == "light1" || deviceType == "light2" || deviceType == "light3" || deviceType == "light4" || deviceType == "light5"){
                        dataSet =  ["command": command, "deviceType": deviceType]
                        outputTxt = advCtrlHandler(dataSet)
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]                                  
                    }
                    //DISABLE AUTOMATIONS/LOCK ROOMS
                    if (deviceType == "disable") {
                            if (command == "on" || command == "off") {
                                if (parent.rManager==true) {
                                	if (command == "on") {
                                    	rLock?.checking()
                                        outputTxt = "Ok, I am checking the $childName for activity and setting the room appropriately"
                                        }
                                    if (command == "off") {
                                    	rLock?.lock()
                                        outputTxt = "Ok, I am locking the $childName automations"
                                        }
                                    }
                                    else {
                                    	gDisable?."${command}"()
                                if (command == "on") {
                                    outputTxt = "Ok, turning the $childName automations $command" 
                                }
                                if (command == "off") {
                                    outputTxt = "Ok, turning the $childName automations $command"
                                	}
                                }    
                            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                            }
                        }
                    //FANS CONTROL
                    if (deviceType == "fan"){
                        if (gFans?.size()>0) {
                            if (command == "on" || command == "off") {
                                gFans?."${command}"()
                                outputTxt = "Ok, turning the fan " + command
                                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                            }
                            else if (command == "decrease" || command == "increase" || command == "high" || command == "medium" || command == "low"){
                                dataSet =  ["command": command, "deviceType": deviceType]
                                outputTxt = advCtrlHandler(dataSet)
                                return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                            }
                            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                        }     
                    } 
                    //TASK TRACKER CONTROL
                    if (deviceType == "trackerNotification") {
                    	if (command == "${trackerOne1}".toLowerCase() || command == "${trackerTwo1}".toLowerCase() || command == "${trackerThree1}".toLowerCase() || command == "${trackerFour1}".toLowerCase()) {
                            def timeDate = new Date().format("hh:mm aa", location.timeZone)
                            def dateDate = new Date().format("EEEE, MMMM d", location.timeZone)
                            if (tts.contains("${trackerOne1}".toLowerCase())) {
                                if(t1notify) {
                                	scheduleHandler1(sched)
                                    outputTxt = "Ok, recording that ${app.label}" + " ${trackerOne2}".toLowerCase() + " ${trackerOne3}".toLowerCase() + " ${trackerOne1}".toLowerCase() + " on " + dateDate + " at " + timeDate + ", and I'm scheduling a reminder for " + state.sched  
            								}
    								else {
                                    outputTxt = "Ok, recording that ${app.label} "+ " ${trackerOne2}".toLowerCase() + " ${trackerOne3}".toLowerCase() + " ${trackerOne1}".toLowerCase() + " on " + dateDate + " at " + timeDate  
                                	}
                                if(command == "${trackerOne1}".toLowerCase()) {state.trackerOne = "${app.label} last " + "${trackerOne2} " + "${trackerOne3} " + "${trackerOne1} on " + dateDate + " at " + timeDate }
                                }
                            else if (tts.contains("${trackerTwo1}".toLowerCase())) {
                                if(t2notify) {
                                	scheduleHandler2()
                                    outputTxt = "Ok, recording that ${app.label}" + " ${trackerOne2}".toLowerCase() + " ${trackerOne3}".toLowerCase() + " ${trackerOne1}".toLowerCase() + " on " + dateDate + " at " + timeDate + ", and I'm scheduling a reminder for " + state.sched  
    								}
                                    else {
                                outputTxt = "Ok, recording that ${app.label} last " + " ${trackerTwo2}".toLowerCase() + " ${trackerTwo3}".toLowerCase() + " ${trackerTwo1}".toLowerCase() + " on " + dateDate + " at " + timeDate
                                }
                                if(command == "${trackerTwo1}".toLowerCase()) {state.trackerTwo = "${app.label} last " + "${trackerTwo2} " + "${trackerTwo3} " + "${trackerTwo1} on " + dateDate + " at " + timeDate }
                                }
                            else if (tts.contains("${trackerThree1}".toLowerCase())) {
                                if(t3notify) {
                                	scheduleHandler3()
                                    outputTxt = "Ok, recording that ${app.label}" + " ${trackerOne2}".toLowerCase() + " ${trackerOne3}".toLowerCase() + " ${trackerOne1}".toLowerCase() + " on " + dateDate + " at " + timeDate + ", and I'm scheduling a reminder for " + state.sched  
            						}
                                    else {
                                outputTxt = "Ok, recording that ${app.label} last " + " ${trackerThree2}".toLowerCase() + " ${trackerThree3}".toLowerCase() + "${trackerThree1}".toLowerCase() + " on " + dateDate + " at " + timeDate
                                }
                                if(command == "${trackerThree1}".toLowerCase()) {state.trackerThree = "${app.label} last " + "${trackerThree2} " + "${trackerThree3} " + "${trackerThree1} on " + dateDate + " at " + timeDate }
                                }
                            else if (tts.contains("${trackerFour1}".toLowerCase())) {
                                if(t4notify) {
                                	scheduleHandler4()
                                    outputTxt = "Ok, recording that ${app.label}" + " ${trackerOne2}".toLowerCase() + " ${trackerOne3}".toLowerCase() + " ${trackerOne1}".toLowerCase() + " on " + dateDate + " at " + timeDate + ", and I'm scheduling a reminder for " + state.sched  
            						}
                                    else {
                                outputTxt = "Ok, recording that ${app.label} last " + " ${trackerFour2}".toLowerCase() + " ${trackerFour3}".toLowerCase() + "${trackerFour1}".toLowerCase() + " on " + dateDate + " at " + timeDate
                                }
                                if(command == "${trackerFour1}".toLowerCase()) {state.trackerFour = "${app.label} last " + "${trackerFour2} " + "${trackerFour3} " + "${trackerFour1} on " + dateDate + " at " + timeDate }
                                }
                                if(psms) { sendtxt(outputTxt) }
                                if(pPush) { sendPush outputTxt }
                            }
                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                    	}
                    //VENTS AND WINDOWS CONTROL
                    if (deviceType == "vent" || deviceType == "shade") { 
                        if (command == "open"  || command == "close") {
                            if (command == "open") {
                                if(deviceType == "vent"){
                                    gVents.on()
                                    gVents.setLevel(100)
                                    	outputTxt = "Ok, opening the vents in the ${app.label}"
                                }
                                else {
                                    gShades.open()
                                    	outputTxt = "Ok, opening the window coverings in the ${app.label}"
                                }
                            }
                            	else {   
                                	if(deviceType == "vent"){
                                    	gVents.off()
                                    	outputTxt = "Ok, closing the vents in the ${app.label}"
                                }
                                else {
                                    gShades.close()
                                    	outputTxt = "Ok, closing the window coverings in the ${app.label}"
                                }
                            }  
                            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                        }
                    } 
                    if (deviceType == "tv") {
                    log.info "step one"
                        if(sMedia){
                            if (command == "startActivity"){
                                if(state.lastActivity != null){
                                    def activityId = null
                                    def activities = sMedia.currentState("activities").value
                                    def activityList = new groovy.json.JsonSlurper().parseText(activities)
                                    activityList.each { it ->  
                                        def activity = it
                                        if(activity.name == state.lastActivity) {
                                            activityId = activity.id
                                        }    	
                                    }
                                    log.warn "starting activity id = ${activityId}, command = ${command}, lastActivity ${state.lastActivity}"
                                    sMedia."${command}"(activityId)
                                    sMedia.refresh()
                                    outputTxt = "Ok, starting " + state.lastActivity + " activity "
                                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                                }
                                else { 
                                    outputTxt = "Sorry for the trouble, but in order for EchoSistant to be able to start where you left off, the last activity must be saved"
                                    pTryAgain = true
                                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                                }
                            }
                            else {
                                if (command == "activityoff"){
                                    def activityId = null
                                    def currState = sMedia.currentState("currentActivity").value
                                    def activities = sMedia.currentState("activities").value
                                    def activityList = new groovy.json.JsonSlurper().parseText(activities)
                                    if (currState != "--"){
                                        activityList.each { it ->  
                                            def activity = it
                                            if(activity.name == currState) {
                                                activityId = activity.id
                                            }    	
                                        }
                                        state.lastActivity = currState
                                        sMedia."${command}"()
                                        sMedia.refresh()
                                        outputTxt = "Ok, turning off " + currState
                                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                                    }
                                    else {
                                        outputTxt = sMedia.label + " is already off"
                                        pTryAgain = true
                                        return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                                    }
                                }
                            }
                        }
                    }

                }
                if (parent.debug) {log.debug "end of control engine, command=${command}, deviceType = ${deviceType}"}
                if (!sonosDevice && !synthDevice) { //added 2/19/17 Bobby  
                    state.lastMessage = tts
                    state.lastTime = new Date(now()).format("h:mm aa, dd-MMMM-yyyy", location.timeZone)
                    outputTxt = ttsHandler(tts)
                    pContCmdsR = "profile"
                    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
                	}
                else {
            outputTxt = "Sorry, you must first set up your profile before trying to execute it."
            pTryAgain = true
            return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
        	}
        }
      
/******************************************************************************************************
ADVANCED CONTROL HANDLER
******************************************************************************************************/
def advCtrlHandler(data) {
	def deviceCommand = data.command
	def deviceType = data.deviceType
    def result
    if (deviceType == "light" || deviceType == "light1" || deviceType == "light2" || deviceType == "light3" || deviceType == "light4" || deviceType == "light5"){
    deviceType = deviceType == "light" && gSwitches ? gSwitches : deviceType == "light1" && gCustom1 ? gCustom1 : deviceType == "light2" && gCustom2 ? gCustom2 : deviceType == "light3" && gCustom3 ? gCustom3 : deviceType == "light4" && gCustom4 ? gCustom4 : deviceType == "light5" && gCustom5 ? gCustom5 : null
		if (deviceCommand == "increase" || deviceCommand == "decrease" || deviceCommand == "on" || deviceCommand == "off") { 
                    deviceType.each {s ->
                   		if (deviceCommand == "on" || deviceCommand == "off") {
							s?."${deviceCommand}"()
                            result = "Ok, turning lights " + deviceCommand
							if (deviceType == gCustom1) {
                            	result = "Ok, turning " + gCustom1N + " " + deviceCommand
                            	}
                            	if (deviceType == gCustom2) {
                                	result = "Ok, turning " + gCustom2N + " " +  deviceCommand
                                	}
                                    if (deviceType == gCustom3) {
                                		result = "Ok, turning " + gCustom3N + " " +  deviceCommand
                                		}
                                    	if (deviceType == gCustom4) {
                                			result = "Ok, turning " + gCustom4N + " " +  deviceCommand
                                			}
                                    		if (deviceType == gCustom5) {
                                				result = "Ok, turning " + gCustom5N + " " +  deviceCommand
                                				}
                            				}
                        else {
                            def	currLevel = s?.latestValue("level")
                            def currState = s?.latestValue("switch") 
                            if (currLevel) {
                            def newLevel = 3*10     
                                if (deviceCommand == "increase") {
                                    if (currLevel == null){
                                        s?.on()
                                        result = "Ok, turning " + app.label + " lights on"   
                                    }
                                    else {
                                        newLevel =  currLevel + newLevel
                                        newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
                                    }
                                }
                                if (deviceCommand == "decrease") {
                                    if (currLevel == null) {
                                        s?.off()
                                        result = "Ok, turning " + app.label + " lights off"                   
                                    }
                                    else {
                                        newLevel =  currLevel - newLevel
                                        newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
                                    }
                                }            
                                if (newLevel > 0 && currState == "off") {
                                    s?.on()
                                    s?.setLevel(newLevel)
                                }
                                else {                                    
                                    if (newLevel == 0 && currState == "on") {
                                    s?.off()
                                    }
                                    else {
                                        s?.setLevel(newLevel)
                                    }
                                } 
                            }
                            else if  (deviceCommand == "increase" && currState == "off") {s?.on()}
                            //else if (deviceCommand == "decrease" && currState == "on") {s?.off()} removed as annoying when used in conjunction with dimmable bulbs on ON/OFF switches Bobby 3/14/2017
                            result = "Ok, adjusting the lights in the  " + app.label 
                        } 
    				}
                    return result
    	}
    }
    if (deviceType == "fan"){
		def cHigh = 99
		def cMedium = 66
        def cLow = 33
        def cFanLevel = 33
        def newLevel
			gFans?.each {deviceD -> 
                def currLevel = deviceD.latestValue("level")
                def currState = deviceD.latestValue("switch")
                	newLevel = cFanLevel     
                if (deviceCommand == "increase") {
                    newLevel =  currLevel + newLevel
                    newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
                }
				else if (deviceCommand == "decrease") {
					newLevel =  currLevel - newLevel
                    newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel      
             	}
                else if (deviceCommand == "high") {newLevel = cHigh}
                else if (deviceCommand == "medium") {newLevel = cMedium}
                else if (deviceCommand == "low") {newLevel = cLow}
                deviceD.setLevel(newLevel)
            }
            result = "Ok, I am adjusting the fans in the  " + app.label 
            return result
	}
    
}

/******************************************************************************************************
SPEECH AND TEXT ALEXA RESPONSE
******************************************************************************************************/
def ttsHandler(tts) {
	def String outputTxt = (String) null 
	def cm = app.label
	if(parent.debug) log.debug " ttshandler settings: pAlexaCustResp=${pAlexaCustResp},pAlexaRepeat=${pAlexaRepeat},tts=${tts}"
    // SHORTCUT PHRASES AND RESPONSES PROCESSING

    def sc = childApps.find {s -> s.label?.toLowerCase() == tts.toLowerCase()}
    if (sc) {
        sc.runShortcutAction()
        if (sc.scResponse) {
        	outputTxt = sc.scResponse
            }
            else {outputTxt = "I'm executing the shortcut for the room, " + cm}
        return outputTxt
    }
    def s1 = childApps.find {s -> s.alias1?.toLowerCase() == tts.toLowerCase()}
    if (s1) {
        s1.runShortcutAction()
        if (s1.scResponse1) {
        	outputTxt = s1.scResponse1
            }
            else {outputTxt = "I'm executing the shortcut for the room, " + cm}
        return outputTxt
    }
    def s2 = childApps.find {s -> s.alias2?.toLowerCase() == tts.toLowerCase()}
    if (s2) {
        s2.runShortcutAction()
        if (s2.scResponse2) {
        	outputTxt = s2.scResponse2
            }
            else {outputTxt = "I'm executing the shortcut for the room, " + cm}
        return outputTxt
    }
    def s3 = childApps.find {s -> s.alias3?.toLowerCase() == tts.toLowerCase()}
    if (s3) {
        s3.runShortcutAction()
        if (s3.scResponse3) {
        	outputTxt = c3.scResponse3
            }
            else {outputTxt = "I'm executing the shortcut for the room, " + cm}
        return outputTxt
    }
	else {
        ttsActions(tts)
        outputTxt = "Your message has been sent to " + app.label
        return outputTxt
    }
    if(parent.debug) log.debug "running actions, sending result to Parent = ${result}"
    return outputTxt
}

/******************************************************************************************************
SPEECH AND TEXT ACTION
******************************************************************************************************/
def ttsActions(tts) {
log.info "ttsactions have been called by: $tts"
    tts = tts
    def msg = tts
    def String ttx = (String) null 	
    def String = tts
	
    //define audio message
    if(pRunMsg){
    	tts = settings.pRunMsg
    }
    else {
        if (pPreMsg) {
            tts = pPreMsg + tts
        }
	}
    //define text message
    if(pRunTextMsg){
        ttx = settings.pRunTextMsg
    }
    else {
        if (pPreTextMsg) {
            ttx = pPreTextMsg + tts
        }
        else {
            ttx = tts
        }
        if(parent.debug) log.debug "defined sms = ${ttx}"
    }
    if(state.pMuteAll == false){
        if (getDayOk()==true && getModeOk()==true && getTimeOk()==true) {
            if (synthDevice) {
                synthDevice?.speak(String) 
                if (parent.debug) log.debug "Sending message to Synthesis Devices"
            }
            if (smc) {
            	sendLocationEvent(name: "EchoSistantMsg", value: "ESv4.5 Room: $app.label", isStateChange: true, descriptionText: "${tts}")
                log.info "Message sent to Smart Message Control: Msg = $tts"
                }
            if (echoDevice) {
            	settings.echoDevice.each { spk->
                		spk.speak(String)
				}
            }     
            if (tts) {
                state.sound = textToSpeech(tts instanceof List ? tts[9] : tts)
            }
            else {
                state.sound = textToSpeech("You selected the custom message option but did not enter a message in the $app.label Smart App")
                if (parent.debug) log.debug "You selected the custom message option but did not enter a message"
            }
            if (sonosDevice){ // 2/22/2017 updated Sono handling when speaker is muted
                def currVolLevel = sonosDevice.latestValue("level")
                def currMuteOn = sonosDevice.latestValue("mute").contains("muted")
                if (parent.debug) log.debug "currVolSwitchOff = ${currVolSwitchOff}, vol level = ${currVolLevel}, currMuteOn = ${currMuteOn} "
                if (currMuteOn) { 
                    if (parent.debug) log.warn "speaker is on mute, sending unmute command"
                    sonosDevice.unmute()
                }
                def sVolume = settings.volume ?: 20
                sonosDevice?.playTrackAndResume(state.sound.uri, state.sound.duration, sVolume)
                if (parent.debug) log.info "Playing message on the music player '${sonosDevice}' at volume '${volume}'" 
            }

//if (echoDevice) {
//echoDevice?.setVolumeAndSpeak(eVolume, msg)
//                }
        }
        if(recipients || sms){				//if(recipients.size()>0 || sms.size()>0){ removed: 2/18/17 Bobby
            sendtxt(ttx)
        }
    }
    else {
        if(recipients || sms){				//if(recipients.size()>0 || sms.size()>0){ removed: 2/18/17 Bobby
            if (parent.debug) log.debug "Only sending sms because disable voice message is ON"
            sendtxt(ttx)
        }
    }
    if (pVirPer) {
        pVirToggle()
    }
    if (shmState) {
        shmStateChange()
    }    
    if (sHues) {               
        processColor()
    }
    if (sFlash) {
        flashLights()
    }
    profileDeviceControl()
    if (pRoutine) {
        location.helloHome?.execute(settings.pRoutine)
    }
    if (pRoutine2) {
        location.helloHome?.execute(settings.pRoutine2)
    }
    if (pMode) {
        setLocationMode(pMode)
    }
    if (push && pPreTextMsg) {
        tts = pPreTextMsg + tts
        sendPushMessage(tts)
    }
    else if (push) {
        sendPushMessage(tts)
    }    
    state.lastMessage = tts
    return ["outputTxt":outputTxt, "pContCmds":state.pContCmds, "pShort":state.pShort, "pContCmdsR":state.pContCmdsR, "pTryAgain":state.pTryAgain, "pPIN":pPIN]
}

/***********************************************************************************************************************
RESTRICTIONS HANDLER
***********************************************************************************************************************/
private getAllOk() {
    modeOk && daysOk && timeOk
}
private getModeOk() {
    def result = !modes || modes?.contains(location.mode)
    if(parent.debug) log.debug "modeOk = $result"
    result
} 
private getDayOk() {
    def result = true
    if (day) {
        def df = new java.text.SimpleDateFormat("EEEE")
        if (location.timeZone) {
            df.setTimeZone(location.timeZone)
        }
        else {
            df.setTimeZone(TimeZone.getTimeZone("America/New_York"))
        }
        def day = df.format(new Date())
        result = day.contains(day)
    }
    if(parent.debug) log.debug "daysOk = $result"
    result
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
    def result = "complete"
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
private void sendtxt(message) {
    //    if (parent.debug) log.debug "Request to send sms received with message: '${message}'"
    if (sendContactText) { 
        sendNotificationToContacts(message, recipients)
        if (parent.debug) log.debug "Sending sms to selected reipients"
    } 
    else {
        if (push || shmNotification) { 
            sendPushMessage
            if (parent.debug) log.debug "Sending push message to mobile app"
        }
    } 
    if (notify) {
        sendNotificationEvent(message)
        if (parent.debug) log.debug "Sending notification to mobile app"

    }
    if (notifyGdoorOpen) {
        if (message.contains("open")) {
            sendTextGarage(message)
        }
    }
    if (notifyGdoorClose) {
        if (message.contains("close")) {
            sendTextGarage(message)
        }
    }
    if (notifyVPArrive) {
        if (message.contains("check") && message.contains("in")) {
            sendTextvp(message)
            if (vpNotification) {
                sendPush(message)
            }
        }
    }
    if (notifyVPDepart) {
        if (message.contains("check") && message.contains("out")) {
            sendTextvp(message)
            if (vpNotification) {
                sendPush(message)
            }
        }
    }    
    if (notifySHMArm) {
        if (message.contains("Stay") || message.contains("Away")) {
            sendTextshm(sms, message)
        }
    }
    if (notifySHMDisarm) {
        if (message.contains("Disarm")) {
            sendTextshm(sms, message)
        }
    }    
    if (sms) {
        sendText(sms, message)
        if (parent.debug) log.debug "Processing message for selected phones"
    }
    if (psms) {
        processpsms(psms, message)
    }
}
private void sendTextvp(message) { 
    if (vpPhone != null) {
        def vpPhones = vpPhone.split("\\,")
        for (phone in vpPhones) {
            sendSms(vpPhone, message)
        }
    }
}    
private void sendTextGarage(message) {
    if (garagePhone != null) {
        def garagePhones = garagePhone.split("\\,")
        for (phone in garagePhones) {
            sendSms(garagePhone, message)
            //    if (parent.debug) log.debug "Sending sms to selected phones for the garage door"
        }
    }
}
private void sendTextshm(number, message) {
    if (shmPhone != null) {
        def shmPhones = shmPhone.split("\\,")
        for (phone in shmPhones) {
            sendSms(shmPhone, message)
            //    if (parent.debug) log.debug "Sending sms to selected phones for SHM"
        }
    }
} 
private void sendText(number, message) {
    if (sms) {
        def phones = sms.split("\\,")
        for (phone in phones) {
            sendSms(phone, message)
            if (parent.debug) log.debug "Sending sms to selected phones"
        }
    }
}
private void processpsms(psms, message) {
    if (psms) {
        def phones = psms.split("\\,")
        for (phone in phones) {
            sendSms(phone, message)
        }
    }
}

/************************************************************************************************************
Switch/Color/Dimmer/Toggle Handlers
************************************************************************************************************/
// Used for delayed devices
def turnOnSwitch() { sSwitches?.on() }  
def turnOffSwitch() { sSwitches?.off() }
def turnOnOtherSwitch() { sOtherSwitch?.on() }
def turnOffOtherSwitch() { sOtherSwitch?.off() }  
def turnOnDimmers() { def level = dimmersLVL < 0 || !dimmersLVL ?  0 : dimmersLVL >100 ? 100 : dimmersLVL as int
    sDimmers?.setLevel(sDimmersLVL) }
def turnOffDimmers() { sDimmers?.off() }
def turnOnOtherDimmers() { def otherlevel = otherDimmersLVL < 0 || !otherDimmersLVL ?  0 : otherDimmersLVL >100 ? 100 : otherDimmersLVL as int
    sOtherDimmers?.setLevel(sOtherDimmersLVL) }
def turnOffOtherDimmers() { sOtherDimmers?.off() }   

// Primary control of profile triggered lights/switches when delayed handler
def profileDeviceControl() {
    if (sSecondsOn) { runIn(sSecondsOn,turnOnSwitch)}
    if (sSecondsOff) { runIn(sSecondsOff,turnOffSwitch)}
    if (sOtherSecondsOn)  { runIn(sOtherSecondsOn,turnOnOtherSwitch)}
    if (sOtherSecondsOff) { runIn(sOtherSecondsOff,turnOffOtherSwitch)}
    if (sSecondsDimmers) { runIn(sSecondsDimmers,turnOnDimmers)}
    if (sSecondsDimmersOff) { runIn(sSecondsDimmersOff,turnOffDimmers)}
    if (sSecondsOtherDimmers) { runIn(sSecondsOtherDimmers,turnOnOtherDimmers)}
    if (sSecondsOtherDimmersOff) { runIn(sSecondsOtherDimmersOff,turnOffOtherDimmers)}
        if (sDimmersCmd == "set" && sDimmers) { def level = sDimmersLVL < 0 || !sDimmersLVL ?  0 : sDimmersLVL >100 ? 100 : sDimmersLVL as int
            sDimmers?.setLevel(level) }

// Control of Lights and Switches when not delayed handler         
    if (!sSecondsOn) {
        if  (sSwitchCmd == "on") { sSwitches?.on() }
        else if (sSwitchCmd == "off") { sSwitches?.off() }
        if (sSwitchCmd == "toggle") { toggle() }
        if (sOtherSwitchCmd == "on") { sOtherSwitch?.on() }
        else if (sOtherSwitchCmd == "off") { sOtherSwitch?.off() }
        if (otherSwitchCmd == "toggle") { toggle() }

        if (sOtherDimmersCmd == "set" && sOtherDimmers) { def otherLevel = sOtherDimmersLVL < 0 || !sOtherDimmersLVL ?  0 : sOtherDimmersLVL >100 ? 100 : sOtherDimmersLVL as int
            sOtherDimmers?.setLevel(otherLevel) }
    }
}

// SWITCHES TOGGLE HANDLER
private toggle() {
  sSwitches.each { deviceName ->
    def switchattr = deviceName.currentSwitch
    if (switchattr.contains('on')) {
      deviceName.off()
    }
    else {
      deviceName.on()
    }
  }
  sOtherSwitch.each { deviceName ->
    def switchattr = deviceName.currentSwitch
    if (switchattr.contains('on')) {
      deviceName.off()
    }
    else {
      deviceName.on()
    }
  }		
  
}


/************************************************************************************************************
Flashing Lights Handler
************************************************************************************************************/
private flashLights() {
    if (parent.debug) log.debug "The Flash Switches Option has been activated"
    def doFlash = true
    def onFor = onFor ?: 60000/60
    def offFor = offFor ?: 60000/60
    def numFlashes = numFlashes ?: 3

    if (state.lastActivated) {
        def elapsed = now() - state.lastActivated
        def sequenceTime = (numFlashes + 1) * (onFor + offFor)
        doFlash = elapsed > sequenceTime
    }
    if (doFlash) {
        state.lastActivated = now()
        def initialActionOn = sFlash.collect{it.currentflashSwitch != "on"}
        def delay = 0L

        numFlashes.times {
            sFlash.eachWithIndex {s, i ->
                if (initialActionOn[i]) {
                    s.on(delay: delay)
                }
                else {
                    s.off(delay:delay)                   
                } 
            }
            delay += onFor
            sFlash.eachWithIndex {s, i ->
                if (initialActionOn[i]) {
                    s.off(delay: delay)
                }
                else {
                    s.on(delay:delay)
                }
            }
            delay += offFor
        }
    }
} 
        
/******************************************************************************************************
CUSTOM COMMANDS - CONTROL
******************************************************************************************************/
private getCommand(text){
	log.info "getCommand method activated with this text: $text "
    def String command = (String) null
    def String deviceType = (String) null
    text = text.toLowerCase()
    log.info "${trackerOne1}".toLowerCase()
//Task Tracker
    if (text.startsWith("The".toLowerCase()) || text.startsWith("She".toLowerCase()) || text.startsWith("He".toLowerCase()) || text.startsWith("It".toLowerCase()) || text.startsWith("I".toLowerCase())) {
        if (text.contains("${trackerOne1}".toLowerCase()) || text.contains("$trackerTwo1".toLowerCase()) || text.contains("$trackerThree1".toLowerCase()) || text.contains("$trackerFour1".toLowerCase())) {
            command = text.contains("${trackerOne1}".toLowerCase()) ? "${trackerOne1}".toLowerCase() : text.contains("${trackerTwo1}".toLowerCase()) ? "${trackerTwo1}".toLowerCase() : text.contains("${trackerThree1}".toLowerCase()) ? "${trackerThree1}".toLowerCase() : text.contains("${trackerFour1}".toLowerCase()) ? "${trackerFour1}".toLowerCase() : "undefined"
            deviceType = "trackerNotification"
        }
    }
    
    
//case "Dimmer Commands":
        if (text.contains("darker") || text.contains("too bright") || text.contains("dim") || text.contains("dimmer") || text.contains("turn down")) {
            command = "decrease" 
            deviceType = "light"
        }
        else if  (text.contains("not bright enough") || text.contains("brighter") || text.contains("too dark") || text.contains("turn up") ||text.contains("brighten")) {
            command = "increase" 
            deviceType = "light"     
        } 
        else if (unit == "percent") {
        	deviceType = "light"
        }
        else if (text.contains("increase") && text.contains("volume")) {  // volume control for speakers & harmony
        	command = "increase"
            deviceType = "volume"
        }
        else if (text.contains("decrease") && text.contains("volume")) {  // volume control for speakers & harmony
        	command = "decrease"
            deviceType = "volume"
        }

//GARAGE DOORS
	else if (gGarage) {
    	command = text.contains("open the garage door") ? "open" : text.contains("close the garage door") ? "close" : "undefined"
        deviceType = "garage"
        }


//LIGHT SWITCHES & CUSTOM GROUPS
    else if (gSwitches || gCustom1N || gCustom2N || gCustom3N || gCustom4N || gCustom5N){
        if (gSwitches) {
            command = text.contains("turn on") ? "on" : text.contains("turn off") ? "off" : text.contains("switch on") ? "on" : text.contains("lights on") ? "on" : text.contains("lights off") ? "off" : text.contains("switch off") ? "off" : "undefined"
            if (command == "undefined") {
               	command = text.contains("darker") ? "decrease" : text.contains("decrease") ? "decrease" : text.contains("dim") ? "decrease" : text.contains("dimmer") ? "decrease" : text.contains("lower") ? "decrease" :"undefined"
            	}
            if (command == "undefined") {
                command = text.contains("raise") ? "increase" : text.contains("brighter")  ? "increase" : text.contains("increase") ? "increase" : text.contains("brighten") ? "increase" : "undefined"
            	}
            log.warn "command = $command"
            deviceType = "light"
        	}
        if (gCustom1N) {
        	if (text.contains(settings.gCustom1N.toLowerCase())) {
            command = text.contains("turn on") ? "on" : text.contains("turn off") ? "off" : text.contains("switch on") ? "on" : text.contains("lights on") ? "on" : text.contains("lights off") ? "off" : text.contains("switch off") ? "off" : "undefined"
            if (command == "undefined") {
               	command = text.contains("darker") ? "decrease" : text.contains("decrease") ? "decrease" : text.contains("dim") ? "decrease" : text.contains("dimmer") ? "decrease" : text.contains("lower") ? "decrease" :"undefined"
            	}
            if (command == "undefined") {
                command = text.contains("raise") ? "increase" : text.contains("brighter")  ? "increase" : text.contains("increase") ? "increase" : text.contains("brighten") ? "increase" : "undefined"
            	}
            log.warn "command = $command"
            deviceType = "light1"
        	}
        }    
        if (gCustom2N) {
            if (text.contains(settings.gCustom2N.toLowerCase())) {
                command = text.contains("turn on") ? "on" : text.contains("turn off") ? "off" : text.contains("switch on") ? "on" : text.contains("lights on") ? "on" : text.contains("lights off") ? "off" : text.contains("switch off") ? "off" : "undefined"
            if (command == "undefined") {
               	command = text.contains("darker") ? "decrease" : text.contains("decrease") ? "decrease" : text.contains("dim") ? "decrease" : text.contains("dimmer") ? "decrease" : text.contains("lower") ? "decrease" :"undefined"
            	}
            if (command == "undefined") {
                command = text.contains("raise") ? "increase" : text.contains("brighter")  ? "increase" : text.contains("increase") ? "increase" : text.contains("brighten") ? "increase" : "undefined"
            	}
                deviceType = "light2"
            }
        }
        if (gCustom3N) {
            if (text.contains(settings.gCustom3N.toLowerCase())) {
                command = text.contains("turn on") ? "on" : text.contains("turn off") ? "off" : text.contains("switch on") ? "on" : text.contains("lights on") ? "on" : text.contains("lights off") ? "off" : text.contains("switch off") ? "off" : "undefined"
            if (command == "undefined") {
               	command = text.contains("darker") ? "decrease" : text.contains("decrease") ? "decrease" : text.contains("dim") ? "decrease" : text.contains("dimmer") ? "decrease" : text.contains("lower") ? "decrease" :"undefined"
            	}
            if (command == "undefined") {
                command = text.contains("raise") ? "increase" : text.contains("brighter")  ? "increase" : text.contains("increase") ? "increase" : text.contains("brighten") ? "increase" : "undefined"
            	}
                deviceType = "light3"
            }
        }
        if (gCustom4N) {
            if (text.contains(settings.gCustom4N.toLowerCase())) {
                command = text.contains("turn on") ? "on" : text.contains("turn off") ? "off" : text.contains("switch on") ? "on" : text.contains("lights on") ? "on" : text.contains("lights off") ? "off" : text.contains("switch off") ? "off" : "undefined"
            if (command == "undefined") {
               	command = text.contains("darker") ? "decrease" : text.contains("decrease") ? "decrease" : text.contains("dim") ? "decrease" : text.contains("dimmer") ? "decrease" : text.contains("lower") ? "decrease" :"undefined"
            	}
            if (command == "undefined") {
                command = text.contains("raise") ? "increase" : text.contains("brighter")  ? "increase" : text.contains("increase") ? "increase" : text.contains("brighten") ? "increase" : "undefined"
            	}
                deviceType = "light4"
            }
        }
        if (gCustom5N) {
            if (text.contains(settings.gCustom5N.toLowerCase())) {
                command = text.contains("turn on") ? "on" : text.contains("turn off") ? "off" : text.contains("switch on") ? "on" : text.contains("lights on") ? "on" : text.contains("lights off") ? "off" : text.contains("switch off") ? "off" : "undefined"
            if (command == "undefined") {
               	command = text.contains("darker") ? "decrease" : text.contains("decrease") ? "decrease" : text.contains("dim") ? "decrease" : text.contains("dimmer") ? "decrease" : text.contains("lower") ? "decrease" :"undefined"
            	}
            if (command == "undefined") {
                command = text.contains("raise") ? "increase" : text.contains("brighter")  ? "increase" : text.contains("increase") ? "increase" : text.contains("brighten") ? "increase" : "undefined"
            	}
                deviceType = "light5"
            }
        }        
    }  
    //Virtual Presence Check In/Out
    if (text.contains ("checking in") || text.contains ("checking out")) {
        deviceType = "virPres"
        command = "checking" //text.contains(" checking") ? "checking" : "undefined"
    }
    
    //Disable Switches
    //if (gDisable){
    if (text=="stop the automations" || text=="turn the automations off" || text=="turn off the automations" || text.startsWith("cut off") || text.startsWith("disengage") || text.startsWith("disable automation") || text.startsWith("stop turning the") || text.startsWith("stop the motion sensor") || text.startsWith ("turn the motion sensor off") || text.startsWith("stop the sensor") || text.startsWith("kill the automation") || text.contains("kill the sensor") || text.contains("sensor off")){
        command = "off"
        deviceType = "disable"
    }
    else if (text=="start the automations" || text=="turn the automations on" || text=="turn on the automations" || text.startsWith("cut on") || text.startsWith("engage") ||text.contains("enable automation") || text.startsWith("start turning the") || text.startsWith("start the motion sensor") || text.startsWith("turn the motion sensor on") || text.startsWith ("start the sensor")|| text.contains("sensor on")){
        command = "on"
        deviceType = "disable"
    }
    
    // Fans
    //if(gFans) {
    if (text.contains("fan") || text.contains("fans")) {
        if (text.contains("on") || text.contains("start")) {
            command = "on" 
            deviceType = "fan"
        }
        else if (text.contains("off") || text.contains("stop")) {
            command = "off" 
            deviceType = "fan"
        }
        else if (text.contains("high") || text.contains("medium") || text.contains("low")) {
            command = text.contains("high") ? "high" : text.contains("medium") ? "medium" : text.contains("low") ? "low" : "undefined"
            deviceType = "fan"
        }
        else if  (text.contains("slow down") || text.contains("too fast" ) || text.contains("turn down")) {
            command = "decrease"
            deviceType = "fan" 
        }
        else if  (text.contains("speed up") || text.contains("too slow") || text.contains("turn up")) {
            command = "increase"
            deviceType = "fan" 
        }
        else {
            command = "undefined"
            deviceType = "fan"
        }      
    }

    // Vents
    if (text.contains("vent")) {  
        if (text.contains("open")) {
            command = "on" 
            deviceType = "vent"
        }
        if (text.contains("close")) {
            command = "off" 
            deviceType = "vent"
        }
    }
    
    // Doors
    if (text.contains("door")) {
        if (text.contains("open")) {
            command = "open" 
            deviceType = "door"
        }
        else if (text.contains("close")) {
            command = "closed" 
            deviceType = "door"
        }
        else {
            command = "undefined"
            deviceType = "door"
        }
    }    

    // Locks
    if (text.contains("lock")) {
        if (text.contains("unlocked")) {
            command = "unlocked" 
            deviceType = "locks"
        }
        if (text.contains("locked")) {
            command = "locked" 
            deviceType = "locks"
        }
    }    
    
    // Shades
    if (text.contains("shade") || text.contains("blinds") || text.contains("curtains") ) {  
        if (text.contains("open")) {
            command = "open" 
            deviceType = "shade"
        }
        else if (text.contains("closed")) {
            command = "closed"
            deviceType = "shade"
        }    
        else if (text.contains("close")) {
            command = "close" 
            deviceType = "shade"
        }
    } 
    
    //Harmony
    if (text.contains("tv")) {
        if  (text.contains("start") || text.startsWith("turn on") || text.contains("switch to") || text.contains("on")){
            command = "startActivity"
            deviceType = "tv"
            log.info "Harmony start activity control"
        }
        else if  (text.contains("stop") || text.startsWith("turn off") || text.contains("switch off") || text.contains("off")){
            command = "activityoff"
            deviceType = "tv"
        }
        else { 
            command = "undefined"
            deviceType = "tv"
            log.info "Harmony stop activity control"
        }
    }
    return ["deviceType":deviceType, "command":command ]
}

/************************************************************************************************************
Custom Color Filter
************************************************************************************************************/       
def profileLoop(child) {
    def childName = app.label
    def result
    if(childName == child){
        if(gHues){
            int hueLevel = !level ? 100 : level
            int hueHue = Math.random() *100 as Integer
            def randomColor = [hue: hueHue, saturation: 100, level: hueLevel]
            gHues.setColor(randomColor)
            runIn(60, "startLoop")
            result =  "Ok, turning the color loop on, in the " + childName

        }
        else result = "Sorry, I wasn't able to turn the color loop on in the " + childName
    }
    return result
}
private startLoop() {
    def device =  state.lastDevice
    def deviceMatch = cSwitch.find {s -> s.label.toLowerCase() == device.toLowerCase()}	
    int hueLevel = !level ? 100 : level
    int hueHue = Math.random() *100 as Integer
    def randomColor = [hue: hueHue, saturation: 100, level: hueLevel]
    gHues.setColor(randomColor)
    runIn(60, "continueLoop")
}


private continueLoop() {
    int hueLevel = !level ? 100 : level
    int hueHue = Math.random() *100 as Integer
    def randomColor = [hue: hueHue, saturation: 100, level: hueLevel]
    gHues.setColor(randomColor)
    runIn(60, "startLoop")
}
def profileLoopCancel(child) {
    def childName = app.label 
    def result
    if(childName == child){
        unschedule("startLoop")
        unschedule("continueLoop")
        result =  "Ok, turning the color loop off in the " + childName
    }
    else result = "Sorry, I wasn't able to turn the color loop off"
    return result
}
private setRandomColorName(){
    for (bulb in gHues) {    
        int hueLevel = !level ? 100 : level
        int hueHue = Math.random() *100 as Integer
        def randomColor = [hue: hueHue, saturation: 100, level: hueLevel]
        bulb.setColor(randomColor)
    }
}
private processColor() {
    if (sHuesCmd == "on") { sHues?.on() }
    if (sHuesCmd == "off") { sHues?.off() }
    if (sHuesOtherCmd == "on") { sHuesOther?.on() }
    if (sHuesOtherCmd == "off") { sHuesOther?.off() }
    def hueSetVals = getColorName("${sHuesColor}",level)
    	sHues?.setColor(hueSetVals)
    def	hueSetValsOther = getColorName("${sHuesOtherColor}",level)
    	sHuesOther?.setColor(hueSetValsOther)
}
private getColorName(sHuesColor, level) {
    for (color in fillColorSettings()) {
		if (color.name.toLowerCase() == sHuesColor.toLowerCase()) {
        log.warn "found a color match"
        	int hueVal = Math.round(color.h / 3.6)
            int hueLevel = !level ? color.l : level
			def hueSet = [hue: hueVal, saturation: color.s, level: hueLevel]
            return hueSet
		}
	}
}

def fillColorSettings() {
    return [
        [ name: "Soft White",				rgb: "#B6DA7C",		h: 83,		s: 44,		l: 67,	],
        [ name: "Warm White",				rgb: "#DAF17E",		h: 51,		s: 20,		l: 100,	],
        [ name: "Very Warm White",			rgb: "#DAF17E",		h: 51,		s: 60,		l: 51,	],
        [ name: "Daylight White",			rgb: "#CEF4FD",		h: 191,		s: 9,		l: 90,	],
        [ name: "Daylight",					rgb: "#CEF4FD",		h: 191,		s: 9,		l: 90,	],        
        [ name: "Cool White",				rgb: "#F3F6F7",		h: 187,		s: 19,		l: 96,	],
        [ name: "White",					rgb: "#FFFFFF",		h: 0,		s: 0,		l: 100,	],
        [ name: "Alice Blue",				rgb: "#F0F8FF",		h: 208,		s: 100,		l: 97,	],
        [ name: "Antique White",			rgb: "#FAEBD7",		h: 34,		s: 78,		l: 91,	],
        [ name: "Aqua",						rgb: "#00FFFF",		h: 180,		s: 100,		l: 50,	],
        [ name: "Aquamarine",				rgb: "#7FFFD4",		h: 160,		s: 100,		l: 75,	],
        [ name: "Azure",					rgb: "#F0FFFF",		h: 180,		s: 100,		l: 97,	],
        [ name: "Beige",					rgb: "#F5F5DC",		h: 60,		s: 56,		l: 91,	],
        [ name: "Bisque",					rgb: "#FFE4C4",		h: 33,		s: 100,		l: 88,	],
        [ name: "Blanched Almond",			rgb: "#FFEBCD",		h: 36,		s: 100,		l: 90,	],
        [ name: "Blue",						rgb: "#0000FF",		h: 240,		s: 100,		l: 50,	],
        [ name: "Blue Violet",				rgb: "#8A2BE2",		h: 271,		s: 76,		l: 53,	],
        [ name: "Brown",					rgb: "#A52A2A",		h: 0,		s: 59,		l: 41,	],
        [ name: "Burly Wood",				rgb: "#DEB887",		h: 34,		s: 57,		l: 70,	],
        [ name: "Cadet Blue",				rgb: "#5F9EA0",		h: 182,		s: 25,		l: 50,	],
        [ name: "Chartreuse",				rgb: "#7FFF00",		h: 90,		s: 100,		l: 50,	],
        [ name: "Chocolate",				rgb: "#D2691E",		h: 25,		s: 75,		l: 47,	],
        [ name: "Coral",					rgb: "#FF7F50",		h: 16,		s: 100,		l: 66,	],
        [ name: "Corn Flower Blue",			rgb: "#6495ED",		h: 219,		s: 79,		l: 66,	],
        [ name: "Corn Silk",				rgb: "#FFF8DC",		h: 48,		s: 100,		l: 93,	],
        [ name: "Crimson",					rgb: "#DC143C",		h: 348,		s: 83,		l: 58,	],
        [ name: "Cyan",						rgb: "#00FFFF",		h: 180,		s: 100,		l: 50,	],
        [ name: "Dark Blue",				rgb: "#00008B",		h: 240,		s: 100,		l: 27,	],
        [ name: "Dark Cyan",				rgb: "#008B8B",		h: 180,		s: 100,		l: 27,	],
        [ name: "Dark Golden Rod",			rgb: "#B8860B",		h: 43,		s: 89,		l: 38,	],
        [ name: "Dark Gray",				rgb: "#A9A9A9",		h: 0,		s: 0,		l: 66,	],
        [ name: "Dark Green",				rgb: "#006400",		h: 120,		s: 100,		l: 20,	],
        [ name: "Dark Khaki",				rgb: "#BDB76B",		h: 56,		s: 38,		l: 58,	],
        [ name: "Dark Magenta",				rgb: "#8B008B",		h: 300,		s: 100,		l: 27,	],
        [ name: "Dark Olive Green",			rgb: "#556B2F",		h: 82,		s: 39,		l: 30,	],
        [ name: "Dark Orange",				rgb: "#FF8C00",		h: 33,		s: 100,		l: 50,	],
        [ name: "Dark Orchid",				rgb: "#9932CC",		h: 280,		s: 61,		l: 50,	],
        [ name: "Dark Red",					rgb: "#8B0000",		h: 0,		s: 100,		l: 27,	],
        [ name: "Dark Salmon",				rgb: "#E9967A",		h: 15,		s: 72,		l: 70,	],
        [ name: "Dark Sea Green",			rgb: "#8FBC8F",		h: 120,		s: 25,		l: 65,	],
        [ name: "Dark Slate Blue",			rgb: "#483D8B",		h: 248,		s: 39,		l: 39,	],
        [ name: "Dark Slate Gray",			rgb: "#2F4F4F",		h: 180,		s: 25,		l: 25,	],
        [ name: "Dark Turquoise",			rgb: "#00CED1",		h: 181,		s: 100,		l: 41,	],
        [ name: "Dark Violet",				rgb: "#9400D3",		h: 282,		s: 100,		l: 41,	],
        [ name: "Deep Pink",				rgb: "#FF1493",		h: 328,		s: 100,		l: 54,	],
        [ name: "Deep Sky Blue",			rgb: "#00BFFF",		h: 195,		s: 100,		l: 50,	],
        [ name: "Dim Gray",					rgb: "#696969",		h: 0,		s: 0,		l: 41,	],
        [ name: "Dodger Blue",				rgb: "#1E90FF",		h: 210,		s: 100,		l: 56,	],
        [ name: "Fire Brick",				rgb: "#B22222",		h: 0,		s: 68,		l: 42,	],
        [ name: "Floral White",				rgb: "#FFFAF0",		h: 40,		s: 100,		l: 97,	],
        [ name: "Forest Green",				rgb: "#228B22",		h: 120,		s: 61,		l: 34,	],
        [ name: "Fuchsia",					rgb: "#FF00FF",		h: 300,		s: 100,		l: 50,	],
        [ name: "Gainsboro",				rgb: "#DCDCDC",		h: 0,		s: 0,		l: 86,	],
        [ name: "Ghost White",				rgb: "#F8F8FF",		h: 240,		s: 100,		l: 99,	],
        [ name: "Gold",						rgb: "#FFD700",		h: 51,		s: 100,		l: 50,	],
        [ name: "Golden Rod",				rgb: "#DAA520",		h: 43,		s: 74,		l: 49,	],
        [ name: "Gray",						rgb: "#808080",		h: 0,		s: 0,		l: 50,	],
        [ name: "Green",					rgb: "#008000",		h: 120,		s: 100,		l: 25,	],
        [ name: "Green Yellow",				rgb: "#ADFF2F",		h: 84,		s: 100,		l: 59,	],
        [ name: "Honeydew",					rgb: "#F0FFF0",		h: 120,		s: 100,		l: 97,	],
        [ name: "Hot Pink",					rgb: "#FF69B4",		h: 330,		s: 100,		l: 71,	],
        [ name: "Indian Red",				rgb: "#CD5C5C",		h: 0,		s: 53,		l: 58,	],
        [ name: "Indigo",					rgb: "#4B0082",		h: 275,		s: 100,		l: 25,	],
        [ name: "Ivory",					rgb: "#FFFFF0",		h: 60,		s: 100,		l: 97,	],
        [ name: "Khaki",					rgb: "#F0E68C",		h: 54,		s: 77,		l: 75,	],
        [ name: "Lavender",					rgb: "#E6E6FA",		h: 240,		s: 67,		l: 94,	],
        [ name: "Lavender Blush",			rgb: "#FFF0F5",		h: 340,		s: 100,		l: 97,	],
        [ name: "Lawn Green",				rgb: "#7CFC00",		h: 90,		s: 100,		l: 49,	],
        [ name: "Lemon Chiffon",			rgb: "#FFFACD",		h: 54,		s: 100,		l: 90,	],
        [ name: "Light Blue",				rgb: "#ADD8E6",		h: 195,		s: 53,		l: 79,	],
        [ name: "Light Coral",				rgb: "#F08080",		h: 0,		s: 79,		l: 72,	],
        [ name: "Light Cyan",				rgb: "#E0FFFF",		h: 180,		s: 100,		l: 94,	],
        [ name: "Light Golden Rod Yellow",	rgb: "#FAFAD2",		h: 60,		s: 80,		l: 90,	],
        [ name: "Light Gray",				rgb: "#D3D3D3",		h: 0,		s: 0,		l: 83,	],
        [ name: "Light Green",				rgb: "#90EE90",		h: 120,		s: 73,		l: 75,	],
        [ name: "Light Pink",				rgb: "#FFB6C1",		h: 351,		s: 100,		l: 86,	],
        [ name: "Light Salmon",				rgb: "#FFA07A",		h: 17,		s: 100,		l: 74,	],
        [ name: "Light Sea Green",			rgb: "#20B2AA",		h: 177,		s: 70,		l: 41,	],
        [ name: "Light Sky Blue",			rgb: "#87CEFA",		h: 203,		s: 92,		l: 75,	],
        [ name: "Light Slate Gray",			rgb: "#778899",		h: 210,		s: 14,		l: 53,	],
        [ name: "Light Steel Blue",			rgb: "#B0C4DE",		h: 214,		s: 41,		l: 78,	],
        [ name: "Light Yellow",				rgb: "#FFFFE0",		h: 60,		s: 100,		l: 94,	],
        [ name: "Lime",						rgb: "#00FF00",		h: 120,		s: 100,		l: 50,	],
        [ name: "Lime Green",				rgb: "#32CD32",		h: 120,		s: 61,		l: 50,	],
        [ name: "Linen",					rgb: "#FAF0E6",		h: 30,		s: 67,		l: 94,	],
        [ name: "Maroon",					rgb: "#800000",		h: 0,		s: 100,		l: 25,	],
        [ name: "Medium Aquamarine",		rgb: "#66CDAA",		h: 160,		s: 51,		l: 60,	],
        [ name: "Medium Blue",				rgb: "#0000CD",		h: 240,		s: 100,		l: 40,	],
        [ name: "Medium Orchid",			rgb: "#BA55D3",		h: 288,		s: 59,		l: 58,	],
        [ name: "Medium Purple",			rgb: "#9370DB",		h: 260,		s: 60,		l: 65,	],
        [ name: "Medium Sea Green",			rgb: "#3CB371",		h: 147,		s: 50,		l: 47,	],
        [ name: "Medium Slate Blue",		rgb: "#7B68EE",		h: 249,		s: 80,		l: 67,	],
        [ name: "Medium Spring Green",		rgb: "#00FA9A",		h: 157,		s: 100,		l: 49,	],
        [ name: "Medium Turquoise",			rgb: "#48D1CC",		h: 178,		s: 60,		l: 55,	],
        [ name: "Medium Violet Red",		rgb: "#C71585",		h: 322,		s: 81,		l: 43,	],
        [ name: "Midnight Blue",			rgb: "#191970",		h: 240,		s: 64,		l: 27,	],
        [ name: "Mint Cream",				rgb: "#F5FFFA",		h: 150,		s: 100,		l: 98,	],
        [ name: "Misty Rose",				rgb: "#FFE4E1",		h: 6,		s: 100,		l: 94,	],
        [ name: "Moccasin",					rgb: "#FFE4B5",		h: 38,		s: 100,		l: 85,	],
        [ name: "Navajo White",				rgb: "#FFDEAD",		h: 36,		s: 100,		l: 84,	],
        [ name: "Navy",						rgb: "#000080",		h: 240,		s: 100,		l: 25,	],
        [ name: "Old Lace",					rgb: "#FDF5E6",		h: 39,		s: 85,		l: 95,	],
        [ name: "Olive",					rgb: "#808000",		h: 60,		s: 100,		l: 25,	],
        [ name: "Olive Drab",				rgb: "#6B8E23",		h: 80,		s: 60,		l: 35,	],
        [ name: "Orange",					rgb: "#FFA500",		h: 39,		s: 100,		l: 50,	],
        [ name: "Orange Red",				rgb: "#FF4500",		h: 16,		s: 100,		l: 50,	],
        [ name: "Orchid",					rgb: "#DA70D6",		h: 302,		s: 59,		l: 65,	],
        [ name: "Pale Golden Rod",			rgb: "#EEE8AA",		h: 55,		s: 67,		l: 80,	],
        [ name: "Pale Green",				rgb: "#98FB98",		h: 120,		s: 93,		l: 79,	],
        [ name: "Pale Turquoise",			rgb: "#AFEEEE",		h: 180,		s: 65,		l: 81,	],
        [ name: "Pale Violet Red",			rgb: "#DB7093",		h: 340,		s: 60,		l: 65,	],
        [ name: "Papaya Whip",				rgb: "#FFEFD5",		h: 37,		s: 100,		l: 92,	],
        [ name: "Peach Puff",				rgb: "#FFDAB9",		h: 28,		s: 100,		l: 86,	],
        [ name: "Peru",						rgb: "#CD853F",		h: 30,		s: 59,		l: 53,	],
        [ name: "Pink",						rgb: "#FFC0CB",		h: 350,		s: 100,		l: 88,	],
        [ name: "Plum",						rgb: "#DDA0DD",		h: 300,		s: 47,		l: 75,	],
        [ name: "Powder Blue",				rgb: "#B0E0E6",		h: 187,		s: 52,		l: 80,	],
        [ name: "Purple",					rgb: "#800080",		h: 300,		s: 100,		l: 25,	],
        [ name: "Red",						rgb: "#FF0000",		h: 0,		s: 100,		l: 50,	],
        [ name: "Rosy Brown",				rgb: "#BC8F8F",		h: 0,		s: 25,		l: 65,	],
        [ name: "Royal Blue",				rgb: "#4169E1",		h: 225,		s: 73,		l: 57,	],
        [ name: "Saddle Brown",				rgb: "#8B4513",		h: 25,		s: 76,		l: 31,	],
        [ name: "Salmon",					rgb: "#FA8072",		h: 6,		s: 93,		l: 71,	],
        [ name: "Sandy Brown",				rgb: "#F4A460",		h: 28,		s: 87,		l: 67,	],
        [ name: "Sea Green",				rgb: "#2E8B57",		h: 146,		s: 50,		l: 36,	],
        [ name: "Sea Shell",				rgb: "#FFF5EE",		h: 25,		s: 100,		l: 97,	],
        [ name: "Sienna",					rgb: "#A0522D",		h: 19,		s: 56,		l: 40,	],
        [ name: "Silver",					rgb: "#C0C0C0",		h: 0,		s: 0,		l: 75,	],
        [ name: "Sky Blue",					rgb: "#87CEEB",		h: 197,		s: 71,		l: 73,	],
        [ name: "Slate Blue",				rgb: "#6A5ACD",		h: 248,		s: 53,		l: 58,	],
        [ name: "Slate Gray",				rgb: "#708090",		h: 210,		s: 13,		l: 50,	],
        [ name: "Snow",						rgb: "#FFFAFA",		h: 0,		s: 100,		l: 99,	],
        [ name: "Spring Green",				rgb: "#00FF7F",		h: 150,		s: 100,		l: 50,	],
        [ name: "Steel Blue",				rgb: "#4682B4",		h: 207,		s: 44,		l: 49,	],
        [ name: "Tan",						rgb: "#D2B48C",		h: 34,		s: 44,		l: 69,	],
        [ name: "Teal",						rgb: "#008080",		h: 180,		s: 100,		l: 25,	],
        [ name: "Thistle",					rgb: "#D8BFD8",		h: 300,		s: 24,		l: 80,	],
        [ name: "Tomato",					rgb: "#FF6347",		h: 9,		s: 100,		l: 64,	],
        [ name: "Turquoise",				rgb: "#40E0D0",		h: 174,		s: 72,		l: 56,	],
        [ name: "Violet",					rgb: "#EE82EE",		h: 300,		s: 76,		l: 72,	],
        [ name: "Wheat",					rgb: "#F5DEB3",		h: 39,		s: 77,		l: 83,	],
        [ name: "White Smoke",				rgb: "#F5F5F5",		h: 0,		s: 0,		l: 96,	],
        [ name: "Yellow",					rgb: "#FFFF00",		h: 60,		s: 100,		l: 50,	],
        [ name: "Yellow Green",				rgb: "#9ACD32",		h: 80,		s: 61,		l: 50,	],
    ]
}

/******************************************************************************
FEEDBACK SUPPORT - GET AVERAGE										
******************************************************************************/
def getAverage(device,type){
    def total = 0
    if(debug) log.debug "calculating average temperature"  
    device.each {total += it.latestValue(type)}
    return Math.round(total/device?.size())
}

/************************************************************************************************************
Page status and descriptions 
************************************************************************************************************/       

def pSendSettings() {def result = ""
                     if (echoDevice || synthDevice || sonosDevice || sendContactText || sendText || push || sms || pAlexaCustResp || pAlexaRepeat || pContCmdsProfile || pRunMsg || pPreMsg || pDisableAlexaProfile || pDisableALLProfile || pRunTextMsg || pPreTextMsg) {
                         result = "complete"}
                     result}
def pSendComplete() {def text = "Tap here to Configure" 
                     if (echoDevice ||synthDevice || sonosDevice || sendContactText || sendText || push || sms || pAlexaCustResp || pAlexaRepeat || pContCmdsProfile || pRunMsg || pPreMsg || pDisableAlexaProfile || pDisableALLProfile || pRunTextMsg || pPreTextMsg) {
                         text = "Configured"}
                     else text = "Tap here to Configure"
                     text}
def pMsgSettings() {def result = ""
                     if (echoDevice || synthDevice || sonosDevice || sendContactText || sendText || push || sms) {
                         result = "complete"}
                     result}
def pMsgComplete() {def text = "Tap here to Configure" 
                     if (echoDevice || synthDevice || sonosDevice || sendContactText || sendText || push || sms) {
                         text = "Configured"}
                     else text = "Tap here to Configure"
                     text}
def pConfigSettings() {def result = ""
                       if (pAlexaCustResp || pAlexaRepeat || pContCmdsProfile || pRunMsg || pPreMsg || pDisableAlexaProfile || pDisableALLProfile || pRunTextMsg || pPreTextMsg) {
                           result = "complete"}
                       result}
def pConfigComplete() {def text = "Tap here to Configure" 
                       if (pAlexaCustResp || pAlexaRepeat || pContCmdsProfile || pRunMsg || pPreMsg || pDisableAlexaProfile || pDisableALLProfile || pRunTextMsg || pPreTextMsg) {
                           text = "Configured"}
                       else text = "Tap here to Configure"
                       text}

def pActionsSettings(){def result = ""
                       def pDevicesProc = ""
                       if (sSwitches || sDimmers || sHues || sFlash || pMode || pVirPer) {
                           result = "complete"
                           pDevicesProc = complete}
                       result}
def pActionsComplete() {def text = "" 
                  //      def pDevicesProc = pDevicesComplete()
                        if (pDevicesSettings()=="complete" || pMode || pVirPer) {
                            text = "Configured"}
                        else text = "Tap here to configure"
                        text}        
// PROFILE RESTRICTIONS
def pRestrictSettings(){ def result = "" 
                        if (modes || days || startingX || endingX) { 
                            result = "complete"}
                        result}
def pRestrictComplete() {def text = "Tap here to configure" 
                         if (modes || days || startingX || endingX) {
                             text = "Configured"}
                         else text = "Tap here to Configure"
                         text}
def pTimeSettings(){ def result = "" 
                        if (startingX || endingX) { 
                            result = "complete"}
                        result}
def pTimeComplete() {def text = "Tap here to configure" 
                         if (startingX || endingX) {
                             text = "Configured"}
                         else text = "Tap here to Configure"
                         text}

// FEEDBACK AND CONTROL
def mIntentS(){
	def result = ""
    def IntentS = ""
    if (pActionsSettings() || gDoor1 || sSwitches || sOtherSwitch || sDimmers || sOtherDimmers || sHues || sHuesOther || sFlash || fSwitches || fGarage || fDoors || fWindows || fFans || fVents || fShades || fLocks || fPresence || fMotion || gDisable || gSwitches || gFans || gHues || sVent || sMedia || sSpeaker) {
    	IntentS = "comp"
        result = "complete"
    }    	
    	result
}
def mIntentD() {
    def text = "Tap here to Configure"
	def mIntentS = mIntentS()
    if (mIntentS) 
    {
        text = "Configured"
    }
    else text = "Tap here to Configure"
	    text
}  

// DEVICE CONTROL GROUPS                         
def pGroupSettings() {def result = ""
                      if (gDisable || gSwitches || gFans || gHues || sVent || sMedia || sSpeaker) {
                          result = "complete"}
                      result}
def pGroupComplete() {def text = "Tap here to Configure" 
                      if (gDisable || gSwitches || gFans || gHues || sVent || sMedia || sSpeaker) {
                          text = "Configured"}
                      else text = "Tap here to Configure"
                      text
                     }
// DEVICE FEEDBACK
def fDeviceSettings() {def result = ""
                      if (fSwitches || fGarage || fDoors || fWindows || fFans || fVents || fShades || fLocks || fPresence || fMotion) {
                          result = "complete"}
                      result}
def fDeviceComplete() {def text = "Configured" 
                      if (fSwitches || fGarage || fDoors || fWindows || fFans || fVents || fShades || fLocks || fPresence || fMotion) {
                          text = "Configured"}
                      else text = "Tap here to Configure"
                      text
                     }
// PROFILE ACTIONS
def pDevicesSettings() {def result = ""
                     if (gDoor1 || sSwitches || sOtherSwitch || sDimmers || sOtherDimmers || sHues || sHuesOther || sFlash) {
                         result = "complete"}
                     result}
def pDevicesComplete() {def text = "Tap here to select devices" 
                     if (gDoor1 || sSwitches || sOtherSwitch || sDimmers || sOtherDimmers || sHues || sHuesOther || sFlash) {
                         text = "Configured"}
                     else text = "Tap here to select devices"
                     text
                     }
// TASK TRACKERS
def pTrackSettings() {def result = ""
                     if (trackerOne2 || trackerTwo2 || trackerThree2 || trackerFour2) {
                         result = "complete"}
                     result}
def pTrackComplete() {def text = "Tap here to configure Task Trackers" 
                     if (trackerOne2 || trackerTwo2 || trackerThree2 || trackerFour2) {
                         text = "Task Trackers Configured"}
                     else text = "Tap here to configure Task Trackers"
                     text}


// VIRTUAL PERSON  
def VPCreateSettings() {def result = ""
                     def deviceId = "${app.label}"
                     def d = getChildDevice("${app.label}")
                     if ("${d}"== ("${app.label}")) {
                         result = "complete"}
                     result}
def VPCreateComplete() {def text = "Tap here to Configure" 
                     def deviceId = "${app.label}"
                     def d = getChildDevice("${app.label}")
                     if ("${d}"== ("${app.label}")) {
                         text = "Virtual Person Created - '$app.label'"}
                     else text = "Tap here to Configure"
                     text}

def VPNotifySettings() {def result = ""
                     if (vpNotification || vpPhone) {
                         result = "complete"}
                     result}
def VPNotifyComplete() {def text = "Tap here to Configure" 
                     if (vpNotification || vpPhone) {
                         text = "Configured"}
                     else text = "Tap here to Configure"
                     text}
def mRoomsS(){
    def result = ""
if (childApps?.size()>0) {
    result = "Complete"	
    }
    result
}
def mRoomsD() {
    def text = "Shortcut have not been created. Tap here to begin"
    if (childApps?.size()>0) {
        if (childApps?.size() == 1) {
        	text = "One shortcut has been created."
        }
        else { 
        	text = childApps?.size() + " shortcuts have been created."
            }
    }
    else text = "Shortcut have not been created. Tap here to begin"
    text
}

/*////////////////////////////////////////////////////////////////////////////////////////////////////
	TASK TRACKER REMINDERS HANDLERS FOLLOW
/////////////////////////////////////////////////////////////////////////////////////////////////////*/

// TASK TRACKER REMINDER #1

page name: "reminderPage1"
def reminderPage1() {
    dynamicPage (name: "reminderPage1", install: false, uninstall: false) {
        section ("Name this Reminder") {
            input "reminderTitle", "text", title:"Reminder Name ", required:true, defaultValue: ""  
        }
        section ("Reminder Type") {  
            input "actionType", "enum", title: "Choose a Notification Type", required: false, defaultValue: "Default", submitOnChange: true, 
                options: ["Custom Sound","Custom Text",]
        }    
        section ("Customize Audio Voice") {	
            input "stVoice", "enum", title: "SmartThings Voice", required: true, defaultValue: "en-US Salli", 
                options: [
                    "da-DK Naja","da-DK Mads","de-DE Marlene","de-DE Hans","en-US Salli","en-US Joey","en-AU Nicole",
                    "en-AU Russell","en-GB Amy","en-GB Brian","en-GB Emma","en-GB Gwyneth","en-GB Geraint","en-IN Raveena","en-US Chipmunk","en-US Eric","en-US Ivy","en-US Jennifer",
                    "en-US Justin","en-US Kendra","en-US Kimberly","es-ES Conchita","es-ES Enrique","es-US Penelope","es-US Miguel","fr-CA Chantal","fr-FR Celine","fr-FR Mathieu",
                    "is-IS Dora","is-IS Karl","it-IT Carla","it-IT Giorgio","nb-NO Liv","nl-NL Lotte","nl-NL Ruben","pl-PL Agnieszka","pl-PL Jacek","pl-PL Ewa","pl-PL Jan",
                    "pl-PL Maja","pt-BR Vitoria","pt-BR Ricardo","pt-PT Cristiano","pt-PT Ines","ro-RO Carmen","ru-RU Tatyana","ru-RU Maxim","sv-SE Astrid","tr-TR Filiz",
                ]
        }
        section ("Output Devices") {
            href "reminderDevices1", title: "Configure Reminder Output Devices"
        }
        if (actionType == "Custom Text") {
            section ("Send this message...") {
                input "reminderText", "text", title: "What do you want the message to say", required: true, defaultValue: "", submitOnChange: true
            }
        }                 
        if (actionType == "Custom Sound") {        
            section ("Play this sound...") {
                input "custSound", "enum", title: "Choose a Sound", required: false, defaultValue: "Bell 1", submitOnChange: true, 
                    options: [
                        "Custom URI","Alexa: Bada Bing Bada Boom","Alexa: Beep Beep","Alexa: Boing","Alexa: Open Sesame","Bell 1","Bell 2",
                        "Dogs Barking","Fire Alarm","The mail has arrived","A door opened","There is motion","Smartthings detected a flood",
                        "Smartthings detected smoke","Soft Chime","Someone is arriving","Piano","Lightsaber"
                    ]
                if(custSound == "Custom URI") {
                    input "cSound", "text", title: "Use this URI", required:false, multiple: false, defaultValue: "", submitOnChange: true
                    if(cSound) input "cDuration", "text", title: "Track Duration", required:true, multiple: false, defaultValue: "10", submitOnChange: true
                }
            }            
        }
        section ("When do you want the reminder?") {
            paragraph "Remember, the reminder is automatically scheduled when the Task Tracker is executed."
            input "reminderType", "enum", title: "Send the reminder in...", required: false, defaultValue: "Default", submitOnChange: true, 
                options: ["Minutes","Hours","Days","Specific Date"]
            if (reminderType == "Minutes") {    
                input "minutes", "number", title: "Send in this many minutes", range: "1..60", required: false, submitOnChange: true
            }
            if (reminderType == "Hours") {
                input "hours", "number", title: "Send in this many hours", range: "1..24", required: false, submitOnChange: true
            }
            if (reminderType == "Days") {
                input "days", "number", title: "Send in this many days", required: false, submitOnChange: true
            }
            if (minutes || hours || days) {
            	input "followUp1", "bool", title: "Would you like a follow-up reminder?", required: false, submitOnChange: true
                if (followUp1) {
		        	paragraph "The follow-up reminder will send the same type reminder as the primary reminder, one time."
            		input "fMinutes1", "number", title: "Send a follow-up reminder in this many minutes (1-120)", range: "1..120", required: false, submitOnChange: true
            		if (fMinutes1) {
                    	input "fRecurring1", "bool", title: "Would you like to have a recurring follow-up reminder?", required: false, submitOnChange: true
                     		if (fRecurring1) paragraph "Recurring reminders will continue to repeat until you manually reset them. You can reset the reminders by 1) Going into the " +
                        "SmartThings mobile app. 2) Telling Alexa that you have completed the task. 3) Telling Alexa to cancel reminders for this profile. "
                     }
                }
            }
        }    
        section ("Unschedule All Reminders") {
            href "unscheduler1", title: "Tap here to unschedule all current reminders for this Task Tracker"
        }
    }
}

page name: "unscheduler1"
def unscheduler1(){
    dynamicPage(name: "unscheduler1", title: "Unschedule Reminders", uninstall: false) {
        section ("") {
            unschedule()
            paragraph "You have successfully unscheduled all Reminders for this Task Tracker. " +
                "Press 'Save' to continue"
        }
    }  
} 

page name: "reminderDevices1"
def reminderDevices1(){
    dynamicPage(name: "reminderDevices1", title: "Configure Reminder Output Types", uninstall: false) {
		section ("") {
        	input "remindEchoDevice1", "capability.notification", title: "Play on this Amazon Echo Device(s)", required: false, multiple: true, submitOnChange: true
        }
		section ("") {
            input "notifyDevice1", "capability.notification", title: "Display on this Notification Capable Device(s)", required: false, multiple: true, submitOnChange: true
        }
        section (""){
            input "synthDevice1", "capability.speechSynthesis", title: "Play on these Speech Synthesis Devices", multiple: true, required: false, submitOnChange: true
        }
        section ("") {
            input "sonosDevice1", "capability.musicPlayer", title: "Play on these Music Player Devices", required: false, multiple: true, submitOnChange: true    
            if (sonosDevice1) {
                input "volume1", "number", title: "Temporarily change volume", description: "0-100% (default value = 30%)", required: false
            }
        }  
        section ("" ) {
            input "sendText1", "bool", title: "Enable Text Notifications", required: false, submitOnChange: true     
            if (sendText1){      
                paragraph "You may enter multiple phone numbers separated by comma to deliver the Alexa message. E.g. +18045551122,+18046663344"
                input name: "sms1", title: "Send text notification to (optional):", type: "phone", required: false
            }
        }    
        section ("Push Messages") {
            input "push1", "bool", title: "Send Push Notification (optional)", required: false, defaultValue: false
        }        
    }
}

page name: "reminderSMS1"
def reminderSMS1(){
    dynamicPage(name: "reminderSMS1", title: "Send SMS and/or Push Messages...", uninstall: false) {
        section ("Push Messages") {
            input "push1", "bool", title: "Send Push Notification...", required: false, defaultValue: false
            input "timeStamp1", "bool", title: "Add time stamp to Push Messages...", required: false, defaultValue: false  
        }
        section ("Text Messages" , hideWhenEmpty: true) {
            input "sendContactText1", "bool", title: "Enable Text Notifications to Contact Book (if available)", required: false, submitOnChange: true
            if (sendContactText1){
                input "recipients1", "contact", title: "Send text notifications to...", multiple: true, required: false
            }
            input "sendText1", "bool", title: "Enable Text Notifications to non-contact book phone(s)", required: false, submitOnChange: true      
            if (sendText1){      
                paragraph "You may enter multiple phone numbers separated by comma to deliver the Alexa message as a text and a push notification. E.g. 8045551122,8046663344"
                input name: "sms1", title: "Send text notification to...", type: "phone", required: false
            }
        }    
    }        
}
/***********************************************************************************************************************
    TAKE ACTIONS HANDLER
***********************************************************************************************************************/
private takeAction() {
	def tts
	if (fRecurring1 && fMinutes1 && state.fSched1 == "0") {
    	followUpSched1(sched)
        tts = "Attention, This is your follow-up reminder. This reminder will repeat every $fMinutes1 minutes until you manually reset it. " +
        " so please, pay " + reminderText
        	}
        else if (fMinutes1 && state.fSched1 == "0") {
    	tts = "Attention, This is your only follow-up reminder, it has been $fMinutes1 minutes since your task was due, so please, pay close " + reminderText
        }else {
    			tts = "${reminderText}"
                }
    if (fMinutes1 && state.fSched1 == "1") { 
    	followUpSched1(sched) 
        state.fSched1 = "0"}
			settings.remindEchoDevice1.each { dev->
   			dev.speak(tts)
			}
			settings.notifyDevice1.each { dev->
   			dev.speak(tts)
			}


    if (synthDevice) {
        synthDevice?.speak(tts) 
    }
    if (tts) {
        state.sound = textToSpeech(tts instanceof List ? tts[9] : tts)
    }
    else {
        state.sound = textToSpeech("You selected the custom message option but did not enter a message in the $app.label Smart App")
    }
    if (sonosDevice){ 
        def currVolLevel = sonosDevice1.latestValue("level")
        def currMuteOn = sonosDevice1.latestValue("mute").contains("muted")
        if (currMuteOn) { 
            if (parent.debug) log.warn "speaker is on mute, sending unmute command"
            sonosDevice1.unmute()
        }
        def sVolume1 = settings.volume1 ?: 20
        sonosDevice1?.playTrackAndResume(state.sound.uri, state.sound.duration, sVolume1)
    }
//    if(sms1){
    sendtxt(tts)
//	}
}    
/***********************************************************************************************************************
    SCHEDULE HANDLER
***********************************************************************************************************************/
def scheduleHandler1(sched) {
	if(reminderType == "Minutes") {
        if(minutes) {
        def timeDateMin = new Date(now()+60000*minutes).format("hh:mm aa", location.timeZone)
            runIn(60 * minutes, "takeAction")
            state.sched = timeDateMin 
            log.info "$state.sched"
        }
    }
    if(reminderType == "Hours") {
        if(hours) {
        def timeDateHours = new Date(now()+3600000*hours).format("hh:mm aa", location.timeZone) 
		def dateDateHours = new Date(now()+3600000*hours).format("EEEE, MMMM d", location.timeZone)
            runIn(3600*hours, "takeAction")
            state.sched = dateDateHours + " at " + timeDateHours 
            log.info "$state.sched"
        }
    }
    if(reminderType == "Days") {
        if(days) {
        	def timeDateHours = new Date(now()+86400000*days).format("hh:mm aa", location.timeZone)
            def dateDateDays = new Date(now()+86400000*days).format("EEEE, MMMM d, YYYY", location.timeZone)
            runOnce(new Date(now()+86400000*days), "takeAction")
            state.sched = dateDateDays + " at " + timeDateHours
            log.info "$state.sched"
        }
    }
    if (fMinutes1) {
    	state.fSched1 = "1"
        }
        return state.sched
}
/***********************************************************************************************************************
    FOLLOW-UP REMINDER HANDLER
***********************************************************************************************************************/
def followUpSched1(sched) {
        if(fMinutes1) {
        def timeDateMin = new Date(now()+60000*fMinutes1).format("hh:mm aa", location.timeZone)
            runIn(60 * fMinutes1, "takeAction")
            state.sched = timeDateMin
            log.info "Follow-up reminder will play at $state.sched"
        return state.sched    
        }
    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////

// TASK TRACKER REMINDER #2

page name: "reminderPage2"
def reminderPage2() {
    dynamicPage (name: "reminderPage2", install: false, uninstall: false) {
        section ("Name this Reminder") {
            input "reminderTitle2", "text", title:"Reminder Name ", required:true, defaultValue: ""  
        }
        section ("Reminder Type") {  
            input "actionType2", "enum", title: "Choose a Notification Type", required: false, defaultValue: "Default", submitOnChange: true, 
                options: ["Custom Sound","Custom Text",]
        }    
        section ("Customize Audio Voice") {	
            input "stVoice2", "enum", title: "SmartThings Voice", required: true, defaultValue: "en-US Salli", 
                options: [
                    "da-DK Naja","da-DK Mads","de-DE Marlene","de-DE Hans","en-US Salli","en-US Joey","en-AU Nicole",
                    "en-AU Russell","en-GB Amy","en-GB Brian","en-GB Emma","en-GB Gwyneth","en-GB Geraint","en-IN Raveena","en-US Chipmunk","en-US Eric","en-US Ivy","en-US Jennifer",
                    "en-US Justin","en-US Kendra","en-US Kimberly","es-ES Conchita","es-ES Enrique","es-US Penelope","es-US Miguel","fr-CA Chantal","fr-FR Celine","fr-FR Mathieu",
                    "is-IS Dora","is-IS Karl","it-IT Carla","it-IT Giorgio","nb-NO Liv","nl-NL Lotte","nl-NL Ruben","pl-PL Agnieszka","pl-PL Jacek","pl-PL Ewa","pl-PL Jan",
                    "pl-PL Maja","pt-BR Vitoria","pt-BR Ricardo","pt-PT Cristiano","pt-PT Ines","ro-RO Carmen","ru-RU Tatyana","ru-RU Maxim","sv-SE Astrid","tr-TR Filiz",
                ]
        }
        section ("Output Devices") {
           href "reminderDevice2", title: "Configure Reminder Output Devices"
        }
        if (actionType == "Custom Text") {
            section ("Send this message...") {
                input "reminderText2", "text", title: "What do you want the message to say", required: true, defaultValue: "", submitOnChange: true
            }
        }                 
        if (actionType == "Custom Sound") {        
            section ("Play this sound...") {
                input "custSound2", "enum", title: "Choose a Sound", required: false, defaultValue: "Bell 1", submitOnChange: true, 
                    options: [
                        "Custom URI",
                        "Alexa: Bada Bing Bada Boom",
                        "Alexa: Beep Beep",
                        "Alexa: Boing",
                        "Alexa: Open Sesame",
                        "Bell 1",
                        "Bell 2",
                        "Dogs Barking",
                        "Fire Alarm",
                        "The mail has arrived",
                        "A door opened",
                        "There is motion",
                        "Smartthings detected a flood",
                        "Smartthings detected smoke",
                        "Soft Chime",
                        "Someone is arriving",
                        "Piano",
                        "Lightsaber"
                    ]
                if(custSound2 == "Custom URI") {
                    input "cSound2", "text", title: "Use this URI", required:false, multiple: false, defaultValue: "", submitOnChange: true
                    if(cSound2) input "cDuration", "text", title: "Track Duration", required:true, multiple: false, defaultValue: "10", submitOnChange: true
                }
            }            
        }
        section ("When do you want the reminder?") {
            paragraph "Remember, the reminder is automatically scheduled when the Task Tracker is executed."
            input "reminderType2", "enum", title: "Send the reminder in...", required: false, defaultValue: "Default", submitOnChange: true, 
                options: ["Minutes","Hours","Days","Specific Date"]
            if (reminderType2 == "Minutes") {    
                input "minutes2", "number", title: "Send in this many minutes", range: "1..59", required: false, submitOnChange: true
            }
            if (reminderType2 == "Hours") {
                input "hours2", "number", title: "Send in this many hours", range: "1..23", required: false, submitOnChange: true
            }
            if (reminderType2 == "Days") {
                input "days2", "number", title: "Send in this many days", required: false, submitOnChange: true
            }
        }    
        section ("Unschedule All Reminders") {
            href "unscheduler1", title: "Tap here to unschedule all current reminders for this Task Tracker"
        }
    }
}

page name: "reminderDevices2"
def reminderDevices2(){
    dynamicPage(name: "reminderDevices2", title: "Configure Reminder Output Types", uninstall: false) {
        section ("") {
            input "notifyDevice2", "capability.notification", title: "Display on this Notification Capable Device(s)", required: false, multiple: true, submitOnChange: true
        }
        section (""){
            input "synthDevice2", "capability.speechSynthesis", title: "Play on these Speech Synthesis Devices", multiple: true, required: false, submitOnChange: true
        }
        section ("") {
            input "sonosDevice2", "capability.musicPlayer", title: "Play on these Music Player Devices", required: false, multiple: true, submitOnChange: true    
            if (sonosDevice2) {
                input "volume2", "number", title: "Temporarily change volume", description: "0-100% (default value = 30%)", required: false
            }
        }  
        section ("" ) {
            input "sendText2", "bool", title: "Enable Text Notifications", required: false, submitOnChange: true     
            if (sendText2){      
                paragraph "You may enter multiple phone numbers separated by comma to deliver the Alexa message. E.g. +18045551122,+18046663344"
                input name: "sms2", title: "Send text notification to (optional):", type: "phone", required: false
            }
        }    
        section ("Push Messages") {
            input "push2", "bool", title: "Send Push Notification (optional)", required: false, defaultValue: false
        }        
    }
}

page name: "reminderSMS2"
def reminderSMS2(){
    dynamicPage(name: "reminderSMS2", title: "Send SMS and/or Push Messages...", uninstall: false) {
        section ("Push Messages") {
            input "push2", "bool", title: "Send Push Notification...", required: false, defaultValue: false
            input "timeStamp2", "bool", title: "Add time stamp to Push Messages...", required: false, defaultValue: false  
        }
        section ("Text Messages" , hideWhenEmpty: true) {
            input "sendContactText2", "bool", title: "Enable Text Notifications to Contact Book (if available)", required: false, submitOnChange: true
            if (sendContactText2){
                input "recipients2", "contact", title: "Send text notifications to...", multiple: true, required: false
            }
            input "sendText2", "bool", title: "Enable Text Notifications to non-contact book phone(s)", required: false, submitOnChange: true      
            if (sendText2){      
                paragraph "You may enter multiple phone numbers separated by comma to deliver the Alexa message as a text and a push notification. E.g. 8045551122,8046663344"
                input name: "sms2", title: "Send text notification to...", type: "phone", required: false
            }
        }    
    }        
}

/***********************************************************************************************************************
    TAKE ACTIONS HANDLER
***********************************************************************************************************************/
private takeAction2() {
    def tts = "${reminderText2}"
    if (synthDevice2 || notifyDevice2) {
        synthDevice2?.speak(tts2) 
    }
    if (tts) {
        state.sound = textToSpeech(tts instanceof List ? tts[9] : tts)
    }
    else {
        state.sound = textToSpeech("You selected the custom message option but did not enter a message in the $app.label Smart App")
    }
    if (sonosDevice2){ 
        def currVolLevel2 = sonosDevice2.latestValue("level")
        def currMuteOn2 = sonosDevice2.latestValue("mute").contains("muted")
        if (currMuteOn2) { 
            if (parent.debug) log.warn "speaker is on mute, sending unmute command"
            sonosDevice2.unmute()
        }
        def sVolume2 = settings.volume ?: 20
        sonosDevice2?.playTrackAndResume(state.sound.uri, state.sound.duration, sVolume)
    } 
    sendtxt(tts)
}
/***********************************************************************************************************************
    SCHEDULE HANDLER
***********************************************************************************************************************/
def scheduleHandler2(sched) {
    if(reminderType2 == "Minutes") {
        if(minutes2) {
        def timeDateMin = new Date(now()+60000*minutes2).format("hh:mm aa", location.timeZone)
            runIn(60 * minutes2, "takeAction2")
            state.sched2 = timeDateMin 
            log.info "$state.sched2"
        return state.sched2    
        }
    }
    if(reminderType2 == "Hours") {
        if(hours2) {
        def timeDateHours = new Date(now()+3600000*hours2).format("hh:mm aa", location.timeZone) 
		def dateDateHours = new Date(now()+3600000*hours2).format("EEEE, MMMM d", location.timeZone)
            runIn(3600*hours2, "takeAction2")
            state.sched2 = dateDateHours + " at " + timeDateHours 
            log.info "$state.sched2"
        return state.sched2
        }
    }
    if(reminderType == "Days") {
        if(days2) {
        	def timeDateHours = new Date(now()+86400000*days).format("hh:mm aa", location.timeZone)
            def dateDateDays = new Date(now()+86400000*days).format("EEEE, MMMM d, YYYY", location.timeZone)
            runOnce(new Date(now()+86400000*days2), "takeAction2")
            state.sched = dateDateDays + " at " + timeDateHours
            log.info "${state.sched2}"
        return state.sched2
        }
    }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

// TASK TRACKER REMINDER #3

page name: "reminderPage3"
def reminderPage3() {
    dynamicPage (name: "reminderPage3",install: false, uninstall: false) {
        section ("Name this Reminder") {
            input "reminderTitle3", "text", title:"Reminder Name ", required:true, defaultValue: ""  
        }
        section ("Reminder Type") {  
            input "actionType3", "enum", title: "Choose a Notification Type", required: false, defaultValue: "Default", submitOnChange: true, 
                options: ["Custom Sound","Custom Text",]
        }    
        section ("Customize Audio Voice") {	
            input "stVoice3", "enum", title: "SmartThings Voice", required: true, defaultValue: "en-US Salli", 
                options: [
                    "da-DK Naja","da-DK Mads","de-DE Marlene","de-DE Hans","en-US Salli","en-US Joey","en-AU Nicole",
                    "en-AU Russell","en-GB Amy","en-GB Brian","en-GB Emma","en-GB Gwyneth","en-GB Geraint","en-IN Raveena","en-US Chipmunk","en-US Eric","en-US Ivy","en-US Jennifer",
                    "en-US Justin","en-US Kendra","en-US Kimberly","es-ES Conchita","es-ES Enrique","es-US Penelope","es-US Miguel","fr-CA Chantal","fr-FR Celine","fr-FR Mathieu",
                    "is-IS Dora","is-IS Karl","it-IT Carla","it-IT Giorgio","nb-NO Liv","nl-NL Lotte","nl-NL Ruben","pl-PL Agnieszka","pl-PL Jacek","pl-PL Ewa","pl-PL Jan",
                    "pl-PL Maja","pt-BR Vitoria","pt-BR Ricardo","pt-PT Cristiano","pt-PT Ines","ro-RO Carmen","ru-RU Tatyana","ru-RU Maxim","sv-SE Astrid","tr-TR Filiz",
                ]
        }
        section ("Output Devices") {
            href "reminderDevices3", title: "Configure Reminder Output Devices"
        }
        if (actionType3 == "Custom Text") {
            section ("Send this message...") {
                input "reminderText3", "text", title: "What do you want the message to say", required: true, defaultValue: "", submitOnChange: true
            }
        }                 
        if (actionType3 == "Custom Sound") {        
            section ("Play this sound...") {
                input "custSound3", "enum", title: "Choose a Sound", required: false, defaultValue: "Bell 1", submitOnChange: true, 
                    options: [
                        "Custom URI",
                        "Alexa: Bada Bing Bada Boom",
                        "Alexa: Beep Beep",
                        "Alexa: Boing",
                        "Alexa: Open Sesame",
                        "Bell 1",
                        "Bell 2",
                        "Dogs Barking",
                        "Fire Alarm",
                        "The mail has arrived",
                        "A door opened",
                        "There is motion",
                        "Smartthings detected a flood",
                        "Smartthings detected smoke",
                        "Soft Chime",
                        "Someone is arriving",
                        "Piano",
                        "Lightsaber"
                    ]
                if(custSound3 == "Custom URI") {
                    input "cSound3", "text", title: "Use this URI", required:false, multiple: false, defaultValue: "", submitOnChange: true
                    if(cSound3) input "cDuration3", "text", title: "Track Duration", required:true, multiple: false, defaultValue: "10", submitOnChange: true
                }
            }            
        }
        section ("When do you want the reminder?") {
            paragraph "Remember, the reminder is automatically scheduled when the Task Tracker is executed."
            input "reminderType3", "enum", title: "Send the reminder in...", required: false, defaultValue: "Default", submitOnChange: true, 
                options: ["Minutes","Hours","Days","Specific Date"]
            if (reminderType3 == "Minutes") {    
                input "minutes3", "number", title: "Send in this many minutes", range: "1..59", required: false, submitOnChange: true
            }
            if (reminderType3 == "Hours") {
                input "hours3", "number", title: "Send in this many hours", range: "1..23", required: false, submitOnChange: true
            }
            if (reminderType3 == "Days") {
                input "days3", "number", title: "Send in this many days", required: false, submitOnChange: true
            }
            if (reminderType3 == "Specific Date") {
                input "xFutureTime3", "time", title: "At this time...",  required: false, submitOnChange: true
                def todayYear3 = new Date(now()).format("yyyy")
                def todayMonth3 = new Date(now()).format("MMMM")
                def todayDay3 = new Date(now()).format("dd")
                input "xFutureDay3", "number", title: "On this Day - maximum 31", range: "1..31", submitOnChange: true, description: "Example: ${todayDay}", required: false
                if(xFutureDay3) input "xFutureMonth3", "enum", title: "Of this Month", submitOnChange: true, required: false, multiple: false, description: "Example: ${todayMonth}",
                    options: ["1": "January", "2":"February", "3":"March", "4":"April", "5":"May", "6":"June", "7":"July", "8":"August", "9":"September", "10":"October", "11":"November", "12":"December"]
                if(xFutureMonth3) input "xFutureYear3", "number", title: "Of this Year - maximum 2025", range: "2017..2025", submitOnChange: true, description: "Example: ${todayYear}", required: false
            }
        }    
        section ("Unschedule All Reminders") {
            href "unscheduler3", title: "Tap here to unschedule all current reminders for this Task Tracker"
        }
    }
}

page name: "unscheduler3"
def unscheduler3(){
    dynamicPage(name: "unscheduler3", title: "Unschedule Reminders", uninstall: false) {
        section ("") {
            unschedule()
            paragraph "You have successfully unscheduled all Reminders for this Task Tracker. " +
                "Press 'Save' to continue"
        }
    }  
} 

page name: "reminderDevices3"
def reminderDevices3(){
    dynamicPage(name: "reminderDevices3", title: "Configure Reminder Output Types", uninstall: false) {
        section ("") {
            input "notifyDevice3", "capability.notification", title: "Display on this Notification Capable Device(s)", required: false, multiple: true, submitOnChange: true
        }
        section (""){
            input "synthDevice3", "capability.speechSynthesis", title: "Play on these Speech Synthesis Devices", multiple: true, required: false, submitOnChange: true
        }
        section ("") {
            input "sonosDevice3", "capability.musicPlayer", title: "Play on these Music Player Devices", required: false, multiple: true, submitOnChange: true    
            if (sonosDevice3) {
                input "volume3", "number", title: "Temporarily change volume", description: "0-100% (default value = 30%)", required: false
            }
        }  
        section ("" ) {
            input "sendText3", "bool", title: "Enable Text Notifications", required: false, submitOnChange: true     
            if (sendText3){      
                paragraph "You may enter multiple phone numbers separated by comma to deliver the Alexa message. E.g. +18045551122,+18046663344"
                input name: "sms3", title: "Send text notification to (optional):", type: "phone", required: false
            }
        }    
        section ("Push Messages") {
            input "push3", "bool", title: "Send Push Notification (optional)", required: false, defaultValue: false
        }        
    }
}

page name: "reminderSMS3"
def reminderSMS3(){
    dynamicPage(name: "reminderSMS3", title: "Send SMS and/or Push Messages...", uninstall: false) {
        section ("Push Messages") {
            input "push3", "bool", title: "Send Push Notification...", required: false, defaultValue: false
            input "timeStamp3", "bool", title: "Add time stamp to Push Messages...", required: false, defaultValue: false  
        }
        section ("Text Messages" , hideWhenEmpty: true) {
            input "sendContactText3", "bool", title: "Enable Text Notifications to Contact Book (if available)", required: false, submitOnChange: true
            if (sendContactText3){
                input "recipients3", "contact", title: "Send text notifications to...", multiple: true, required: false
            }
            input "sendText3", "bool", title: "Enable Text Notifications to non-contact book phone(s)", required: false, submitOnChange: true      
            if (sendText3){      
                paragraph "You may enter multiple phone numbers separated by comma to deliver the Alexa message as a text and a push notification. E.g. 8045551122,8046663344"
                input name: "sms3", title: "Send text notification to...", type: "phone", required: false
            }
        }    
    }        
}

/***********************************************************************************************************************
    TAKE ACTIONS HANDLER
***********************************************************************************************************************/
private takeAction3() {
    def tts3 = "${reminderText3}"
    if (synthDevice3 || notifyDevice3) {
        synthDevice3?.speak(tts3) 
    }
    if (tts3) {
        state.sound3 = textToSpeech(tts3 instanceof List ? tts[9] : tts3)
    }
    else {
        state3.sound = textToSpeech("You selected the custom message option but did not enter a message in the $app.label Smart App")
    }
    if (sonosDevice3){ 
        def currVolLevel3 = sonosDevice3.latestValue("level")
        def currMuteOn3 = sonosDevice3.latestValue("mute").contains("muted")
        if (currMuteOn3) { 
            if (parent.debug) log.warn "speaker is on mute, sending unmute command"
            sonosDevice3.unmute()
        }
        def sVolume3 = settings.volume ?: 20
        sonosDevice3?.playTrackAndResume(state.sound.uri, state.sound.duration, sVolume)
    } 
    sendtxt(tts3)
}
/***********************************************************************************************************************
    CRON HANDLER
***********************************************************************************************************************/
def scheduleHandler3() {
    if(reminderType3 == "Minutes") {
        if(minutes3) { 
            runIn(60 * minutes3, "takeAction3")
        }
    }
    if(reminderType3 == "Hours") {
        if(hours3) {
            runIn(3600 * hours3, "takeAction3")
        }
    }
    if(reminderType == "Days") {
        if(days3) {
            runIn(new Date() + days3, "takeAction3")
        }
    }
    if(reminderType3 == "Specific Date") {
        if(xFutureDay3) {
            oneTimeHandler3()
        }
    }
}

/***********************************************************************************************************************
    ONE TIME SCHEDULING HANDLER
***********************************************************************************************************************/
def oneTimeHandler3() {
    def result
    def todayYear3 = new Date(now()).format("yyyy")
    def todayMonth3 = new Date(now()).format("MM")
    def todayDay3 = new Date(now()).format("dd")
    def yyyy = xFutureYear3 ?: todayYear
    def MM = xFutureMonth3 ?: todayMonth
    def dd = xFutureDay3 ?: todayDay

    if(!xFutureDay3) {
        runOnce(xFutureTime3, "takeAction3")
    }
    else{
        def timeSchedule3 = hhmmssZ(xFutureTime3)
        result = "${yyyy}-${MM}-${dd}T${timeSchedule3}" 
        Date date = Date.parse("yyyy-MM-dd'T'HH:mm:ss.SSSZ", result)
        runOnce(date, "takeAction3")
    }
}
private hhmmssZ3(time, fmt = "HH:mm:ss.SSSZ") {
    def t = timeToday(time, location.timeZone)
    def f = new java.text.SimpleDateFormat(fmt)
    f.setTimeZone(location.timeZone ?: timeZone(time))
    f.format(t)
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

// TASK TRACKER REMINDER #4

page name: "reminderPage4"
def reminderPage4() {
    dynamicPage (name: "reminderPage4", install: false, uninstall: false) {
        section ("Name this Reminder") {
            input "reminderTitle4", "text", title:"Reminder Name ", required:true, defaultValue: ""  
        }
        section ("Reminder Type") {  
            input "actionType4", "enum", title: "Choose a Notification Type", required: false, defaultValue: "Default", submitOnChange: true, 
                options: [
                    "Custom Sound",
                    "Custom Text",
                ]
        }    
        section ("Customize Audio Voice") {	
            input "stVoice4", "enum", title: "SmartThings Voice", required: true, defaultValue: "en-US Salli", 
                options: [
                    "da-DK Naja","da-DK Mads","de-DE Marlene","de-DE Hans","en-US Salli","en-US Joey","en-AU Nicole",
                    "en-AU Russell","en-GB Amy","en-GB Brian","en-GB Emma","en-GB Gwyneth","en-GB Geraint","en-IN Raveena","en-US Chipmunk","en-US Eric","en-US Ivy","en-US Jennifer",
                    "en-US Justin","en-US Kendra","en-US Kimberly","es-ES Conchita","es-ES Enrique","es-US Penelope","es-US Miguel","fr-CA Chantal","fr-FR Celine","fr-FR Mathieu",
                    "is-IS Dora","is-IS Karl","it-IT Carla","it-IT Giorgio","nb-NO Liv","nl-NL Lotte","nl-NL Ruben","pl-PL Agnieszka","pl-PL Jacek","pl-PL Ewa","pl-PL Jan",
                    "pl-PL Maja","pt-BR Vitoria","pt-BR Ricardo","pt-PT Cristiano","pt-PT Ines","ro-RO Carmen","ru-RU Tatyana","ru-RU Maxim","sv-SE Astrid","tr-TR Filiz",
                ]
        }
        section ("Output Devices") {
            href "reminderDevices4", title: "Configure Reminder Output Devices"
        }
        if (actionType4 == "Custom Text") {
            section ("Send this message...") {
                input "reminderText4", "text", title: "What do you want the message to say", required: true, defaultValue: "", submitOnChange: true
            }
        }                 
        if (actionType4 == "Custom Sound") {        
            section ("Play this sound...") {
                input "custSound4", "enum", title: "Choose a Sound", required: false, defaultValue: "Bell 1", submitOnChange: true, 
                    options: [
                        "Custom URI",
                        "Alexa: Bada Bing Bada Boom",
                        "Alexa: Beep Beep",
                        "Alexa: Boing",
                        "Alexa: Open Sesame",
                        "Bell 1",
                        "Bell 2",
                        "Dogs Barking",
                        "Fire Alarm",
                        "The mail has arrived",
                        "A door opened",
                        "There is motion",
                        "Smartthings detected a flood",
                        "Smartthings detected smoke",
                        "Soft Chime",
                        "Someone is arriving",
                        "Piano",
                        "Lightsaber"
                    ]
                if(custSound4 == "Custom URI") {
                    input "cSound4", "text", title: "Use this URI", required:false, multiple: false, defaultValue: "", submitOnChange: true
                    if(cSound4) input "cDuration4", "text", title: "Track Duration", required:true, multiple: false, defaultValue: "10", submitOnChange: true
                }
            }            
        }
        section ("When do you want the reminder?") {
            paragraph "Remember, the reminder is automatically scheduled when the Task Tracker is executed."
            input "reminderType4", "enum", title: "Send the reminder in...", required: false, defaultValue: "Default", submitOnChange: true, 
                options: [
                    "Minutes",
                    "Hours",
                    "Days",
                    "Specific Date"
                ]
            if (reminderType4 == "Minutes") {    
                input "minutes4", "number", title: "Send in this many minutes", range: "1..59", required: false, submitOnChange: true
            }
            if (reminderType4 == "Hours") {
                input "hours4", "number", title: "Send in this many hours", range: "1..23", required: false, submitOnChange: true
            }
            if (reminderType4 == "Days") {
                input "days4", "number", title: "Send in this many days", required: false, submitOnChange: true
            }
            if (reminderType4 == "Specific Date") {
                input "xFutureTime4", "time", title: "At this time...",  required: false, submitOnChange: true
                def todayYear4 = new Date(now()).format("yyyy")
                def todayMonth4 = new Date(now()).format("MMMM")
                def todayDay4 = new Date(now()).format("dd")
                input "xFutureDay4", "number", title: "On this Day - maximum 31", range: "1..31", submitOnChange: true, description: "Example: ${todayDay}", required: false
                if(xFutureDay4) input "xFutureMonth2", "enum", title: "Of this Month", submitOnChange: true, required: false, multiple: false, description: "Example: ${todayMonth}",
                    options: ["1": "January", "2":"February", "3":"March", "4":"April", "5":"May", "6":"June", "7":"July", "8":"August", "9":"September", "10":"October", "11":"November", "12":"December"]
                if(xFutureMonth4) input "xFutureYear4", "number", title: "Of this Year - maximum 2025", range: "2017..2025", submitOnChange: true, description: "Example: ${todayYear}", required: false
            }
        }    
        section ("Unschedule All Reminders") {
            href "unscheduler4", title: "Tap here to unschedule all current reminders for this Task Tracker"

        }
    }
}

page name: "unscheduler4"
def unscheduler4(){
    dynamicPage(name: "unscheduler4", title: "Unschedule Reminders", uninstall: false) {
        section ("") {
            unschedule()
            paragraph "You have successfully unscheduled all Reminders for this Task Tracker. " +
                "Press 'Save' to continue"
        }
    }  
}    
page name: "reminderDevices4"
def reminderDevices4(){
    dynamicPage(name: "reminderDevices4", title: "Configure Reminder Output Types", uninstall: false) {
        section ("") {
            input "notifyDevice4", "capability.notification", title: "Display on this Notification Capable Device(s)", required: false, multiple: true, submitOnChange: true
        }
        section (""){
            input "synthDevice4", "capability.speechSynthesis", title: "Play on these Speech Synthesis Devices", multiple: true, required: false, submitOnChange: true
        }
        section ("") {
            input "sonosDevice4", "capability.musicPlayer", title: "Play on these Music Player Devices", required: false, multiple: true, submitOnChange: true    
            if (sonosDevice4) {
                input "volume4", "number", title: "Temporarily change volume", description: "0-100% (default value = 30%)", required: false
            }
        }  
        section ("" ) {
            input "sendText4", "bool", title: "Enable Text Notifications", required: false, submitOnChange: true     
            if (sendText4){      
                paragraph "You may enter multiple phone numbers separated by comma to deliver the Alexa message. E.g. +18045551122,+18046663344"
                input name: "sms4", title: "Send text notification to (optional):", type: "phone", required: false
            }
        }    
        section ("Push Messages") {
            input "push4", "bool", title: "Send Push Notification (optional)", required: false, defaultValue: false
        }        
    }
}    
page name: "reminderSMS4"
def reminderSMS4(){
    dynamicPage(name: "reminderSMS4", title: "Send SMS and/or Push Messages...", uninstall: false) {
        section ("Push Messages") {
            input "push4", "bool", title: "Send Push Notification...", required: false, defaultValue: false
            input "timeStamp4", "bool", title: "Add time stamp to Push Messages...", required: false, defaultValue: false  
        }
        section ("Text Messages" , hideWhenEmpty: true) {
            input "sendContactText4", "bool", title: "Enable Text Notifications to Contact Book (if available)", required: false, submitOnChange: true
            if (sendContactText4){
                input "recipients4", "contact", title: "Send text notifications to...", multiple: true, required: false
            }
            input "sendText4", "bool", title: "Enable Text Notifications to non-contact book phone(s)", required: false, submitOnChange: true      
            if (sendText4){      
                paragraph "You may enter multiple phone numbers separated by comma to deliver the Alexa message as a text and a push notification. E.g. 8045551122,8046663344"
                input name: "sms4", title: "Send text notification to...", type: "phone", required: false
            }
        }    
    }        
}

/***********************************************************************************************************************
TASK TRACKER REMINDERS TAKE ACTIONS HANDLER
***********************************************************************************************************************/
private takeAction4() {
    def tts = "${reminderText4}"
    if (synthDevice2 || notifyDevice4) {
        synthDevice4?.speak(tts) 
    }
    if (tts) {
        state.sound = textToSpeech(tts instanceof List ? tts[9] : tts)
    }
    else {
        state.sound = textToSpeech("You selected the custom message option but did not enter a message in the $app.label Smart App")
    }
    if (sonosDevice4){ 
        def currVolLevel = sonosDevice4.latestValue("level")
        def currMuteOn = sonosDevice4.latestValue("mute").contains("muted")
        if (currMuteOn) { 
            if (parent.debug) log.warn "speaker is on mute, sending unmute command"
            sonosDevic4.unmute()
        }
        def sVolume = settings.volume ?: 20
        sonosDevice4?.playTrackAndResume(state.sound.uri, state.sound.duration, sVolume)
    } 
    sendtxt(tts)
}
/***********************************************************************************************************************
TASK TRACKER REMINDERS SCHEDULING HANDLER
***********************************************************************************************************************/
def scheduleHandler4() {
    if(reminderType4 == "Minutes") {
        if(minutes4) { 
            runIn(60 * minutes4, "takeAction4")
        }
    }
    if(reminderType4 == "Hours") {
        if(hours4) {
            runIn(3600 * hours4, "takeAction4")
        }
    }
    if(reminderType4 == "Days") {
        if(days4) {
            runIn(new Date() + days4, "takeAction4")
        }
    }
    if(reminderType4 == "Specific Date") {
        if(xFutureDay4) {
            oneTimeHandler4()
        }
    }
}

/***********************************************************************************************************************
ONE TIME SCHEDULING HANDLER
***********************************************************************************************************************/
def oneTimeHandler4() {
    def result
    def todayYear = new Date(now()).format("yyyy")
    def todayMonth = new Date(now()).format("MM")
    def todayDay = new Date(now()).format("dd")
    def yyyy = xFutureYear4 ?: todayYear
    def MM = xFutureMonth4 ?: todayMonth
    def dd = xFutureDay4 ?: todayDay

    if(!xFutureDay4) {
        runOnce(xFutureTime4, "takeAction")
    }
    else{
        def timeSchedule = hhmmssZ(xFutureTime4)
        result = "${yyyy}-${MM}-${dd}T${timeSchedule4}" 
        Date date = Date.parse("yyyy-MM-dd'T'HH:mm:ss.SSSZ", result)
        runOnce(date, "takeAction")
    }
}
private hhmmssZ4(time, fmt = "HH:mm:ss.SSSZ") {
    def t = timeToday(time, location.timeZone)
    def f = new java.text.SimpleDateFormat(fmt)
    f.setTimeZone(location.timeZone ?: timeZone(time))
    f.format(t)
}

